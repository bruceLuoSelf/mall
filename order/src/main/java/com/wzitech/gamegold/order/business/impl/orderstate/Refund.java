package com.wzitech.gamegold.order.business.impl.orderstate;

import java.util.Date;
import java.util.List;

import com.wzitech.gamegold.common.enums.LogType;
import com.wzitech.gamegold.common.enums.OrderCenterOrderStatus;
import com.wzitech.gamegold.order.business.*;
import com.wzitech.gamegold.common.log.entity.OrderLogInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.servicer.IServicerOrderManager;
import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Order;

/**
 * 订单改变到已退款状态的管理接口实现类
 * 
 * @author ztjie
 * 
 */
@Component
public class Refund extends AbstractState {

	protected static final Log log = LogFactory.getLog(Refund.class);

	@Autowired
	IOrderInfoDBDAO orderInfoDBDAO;
	
	@Autowired
	IOrderConfigManager orderConfigQuery;

	/*@Autowired
	ILogManager logManager;*/
    @Autowired
    IOrderLogManager orderLogManager;

	@Autowired
	IAutoPayManager autoPayManager;

	@Autowired
	IServicerOrderManager servicerOrderManager;

	@Autowired
	IServiceSortManager serviceSortManager;


	@Autowired
	IOrderPushMainManager orderPushMainManager;

	private OrderInfoEO changeOrderState(String orderId, OrderState orderState)
			throws SystemException {
		if (orderId == null) {
			throw new SystemException(ResponseCodes.EmptyOrderId.getCode(),
					ResponseCodes.EmptyOrderId.getMessage());
		}
		// 更新订单设置订单状态
		//OrderInfoEO orderInfo = orderInfoDBDAO.selectUniqueByProp("orderId", orderId);

        OrderInfoEO orderInfo;
        try {
            // 从数据库查询一次数据，并锁定待修改
            orderInfo = orderInfoDBDAO.queryOrderForUpdateNowait(orderId);
        } catch (Exception e) {
            throw new SystemException(ResponseCodes.OrderIsEditor.getCode(), ResponseCodes.OrderIsEditor.getMessage());
        }

		// 订单状态无改变，就不做后续操作
		if (orderInfo.getOrderState() != null && orderState.getCode() == orderInfo.getOrderState()) {
			return null;
		}

		// 订单状态先前状态必须是已付款或待发货
		if (orderInfo.getOrderState() != OrderState.Paid.getCode()
				&& orderInfo.getOrderState() != OrderState.WaitDelivery
						.getCode()) {
			throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(),
					"订单状态先前状态必须是已付款或待发货");
		}


		//查询订单的子订单
		List<ConfigResultInfoEO> configList = orderConfigQuery.orderConfigList(orderInfo.getOrderId());
		for(ConfigResultInfoEO config : configList){
			//判断子订单是不是已经发货，如果是已经发货，那就不能取消
			if(config.getState().intValue()==OrderState.Delivery.getCode()){
				throw new SystemException(ResponseCodes.SomeChildOrderIsDelivery.getCode(),
						ResponseCodes.SomeChildOrderIsDelivery.getMessage());
			} else if (config.getState().intValue()== OrderState.WaitDelivery.getCode()) {
				// 刚配单未满1分钟的，不能取消
				long now = new Date().getTime();
				long minMillisecond = 40 * 1000; // 40秒毫秒数
				long configTime = config.getCreateTime().getTime();
				long diff = now - configTime;
				if (diff < minMillisecond) {
					throw new SystemException(ResponseCodes.LessThanTimeCantRefund.getCode(),
							ResponseCodes.LessThanTimeCantRefund.getMessage());
				}
			}
		}

		// 增加订单修改日志
		StringBuffer sb = new StringBuffer();
		sb.append("订单号：").append(orderInfo.getOrderId()).append("，订单状态从");
		sb.append(OrderState.getTypeByCode(orderInfo.getOrderState()).getName());

		// 修改订单状态
		orderInfo.setOrderState(orderState.getCode());

		sb.append("变成").append(orderState.getName());

		//logManager.add(ModuleType.ORDER, sb.toString(), CurrentUserContext.getUser());
		log.info(sb.toString());

        OrderLogInfo orderLogInfo = new OrderLogInfo();
        orderLogInfo.setLogType(LogType.ORDER_REFUND);
        orderLogInfo.setOrderId(orderId);
        orderLogInfo.setRemark(sb.toString());
        orderLogManager.add(orderLogInfo);

		return orderInfo;
	}

	@Override
	@Transactional
	public OrderInfoEO handle(String orderId) throws SystemException {
		OrderInfoEO orderInfo = this.changeOrderState(orderId,
				OrderState.Refund);
		if (orderInfo == null) {
			return orderInfo;
		}
		// 订单完成，设置完成时间
		log.info("订单状态是已退款状态，调用退款方法");
		orderInfo.setEndTime(new Date());
		boolean result = autoPayManager.refund(orderInfo);
		if (result) {
			// 更新订单状态
			orderInfoDBDAO.updateOrderState(orderInfo);

			//商城销售订单同步推送Mq
			int orderStatus= OrderCenterOrderStatus.FAILD_TRADE.getCode();
			orderPushMainManager.orderPushMain(orderInfo);
            List<ConfigResultInfoEO> configResults = orderConfigQuery.orderConfigList(orderId);
            if (!CollectionUtils.isEmpty(configResults)) {
                for (ConfigResultInfoEO configResult : configResults) {
                    orderConfigQuery.updateConfigState(configResult.getId(), OrderState.Cancelled.getCode());
                    if (configResult.getIsConsignment() != null && configResult.getIsConsignment()) {
                        /*// 寄售客服处理订单数减1
                        servicerOrderManager.subOrderNum(orderInfo.getGameName(),
                                orderInfo.getRegion(), orderInfo.getServer(),
                                orderInfo.getGameRace(), configResult.getOptionId());*/
						// 如果没有从RC2上先撤单的，则客服待发货订单数量-1
						if (configResult.getState() != OrderState.Cancelled.getCode()) {
							// 客服待发货订单数量-1
							serviceSortManager.decOrderCount(configResult.getOptionId());
						}
                    }
                }
            }

			/*// 客服处理订单数减1
			servicerOrderManager.subOrderNum(orderInfo.getGameName(),
					orderInfo.getRegion(), orderInfo.getServer(),
					orderInfo.getGameRace(), orderInfo.getServicerId());*/

			serviceSortManager.decOrderCount(orderInfo.getServicerId());

			log.info("订单状态修改成功，并退款成功");
		} else {
			log.info("订单状态修改失败，并退款失败");
			throw new SystemException(
					ResponseCodes.RefundFailed.getCode(),
					ResponseCodes.RefundFailed.getMessage());
		}
		return orderInfo;
	}
}
