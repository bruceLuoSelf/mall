package com.wzitech.gamegold.order.business.impl.orderstate;

import java.util.Date;
import java.util.List;

import com.wzitech.gamegold.common.enums.*;
import com.wzitech.gamegold.order.business.*;
import com.wzitech.gamegold.common.log.entity.OrderLogInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.servicer.IServicerOrderManager;
import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;

/**
 * 订单改变到已取消状态的管理接口实现类
 * 
 * @author ztjie
 * 
 */
@Component
public class Cancelled extends AbstractState {

	protected static final Log log = LogFactory.getLog(Cancelled.class);

	@Autowired
	IOrderInfoDBDAO orderInfoDBDAO;

	/*@Autowired
	ILogManager logManager;*/
    @Autowired
    IOrderLogManager orderLogManager;
	
	@Autowired
	IOrderConfigManager orderConfigQuery;

	@Autowired
	IServicerOrderManager servicerOrderManager;


	@Autowired
	IOrderPushMainManager orderPushMainManager;

	private OrderInfoEO changeOrderState(String orderId, OrderState orderState)
			throws SystemException {
		if (orderId == null) {
			throw new SystemException(ResponseCodes.EmptyOrderId.getCode(),
					ResponseCodes.EmptyOrderId.getMessage());
		}
		// 更新订单设置订单状态
		OrderInfoEO orderInfo = orderInfoDBDAO.queryOrderForUpdate(orderId);

		// 订单状态无改变，就不做后续操作
		if (orderState.equals(orderInfo.getOrderState())) {
			return null;
		}

		// 订单状态先前状态必须是待付款
		if (orderInfo.getOrderState() != OrderState.WaitPayment.getCode()) {
			throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(),
					"订单状态先前状态必须是待付款");
		}
		
		//查询订单的子订单
		List<ConfigResultInfoEO> configList = orderConfigQuery.orderConfigList(orderInfo.getOrderId());
		for(ConfigResultInfoEO config : configList){
			//判断子订单是不是已经发货，如果是已经发货，那就不能取消
			if(config.getState().intValue()==OrderState.Delivery.getCode()){
				throw new SystemException(ResponseCodes.SomeChildOrderIsDelivery.getCode(),
						ResponseCodes.SomeChildOrderIsDelivery.getMessage());
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
        orderLogInfo.setLogType(LogType.ORDER_CANCEL);
        orderLogInfo.setOrderId(orderId);
        orderLogInfo.setRemark(sb.toString());
        orderLogManager.add(orderLogInfo);

		return orderInfo;
	}

	@Override
	@Transactional
	public OrderInfoEO handle(String orderId) throws SystemException {
		OrderInfoEO orderInfo = this.changeOrderState(orderId,
				OrderState.Cancelled);

		if (orderInfo == null) {
			return orderInfo;
		}
		// 订单完成，设置完成时间
		orderInfo.setEndTime(new Date());
		orderInfo.setCancelReson("超过时间没有支付，订单自动取消");
		// 更新订单状态
		orderInfoDBDAO.updateOrderState(orderInfo);

		//商城销售订单同步推送Mq
		int orderStatus= OrderCenterOrderStatus.FAILD_TRADE.getCode();
		orderPushMainManager.orderPushMain(orderInfo);

		// 客服处理订单数减1
		/*servicerOrderManager.subOrderNum(orderInfo.getGameName(),
				orderInfo.getRegion(), orderInfo.getServer(),
				orderInfo.getGameRace(), orderInfo.getServicerId());*/
		return orderInfo;
	}
}
