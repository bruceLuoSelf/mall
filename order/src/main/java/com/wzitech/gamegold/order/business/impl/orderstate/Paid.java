package com.wzitech.gamegold.order.business.impl.orderstate;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.LogType;
import com.wzitech.gamegold.common.enums.OrderCenterOrderStatus;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.log.entity.OrderLogInfo;
import com.wzitech.gamegold.common.servicer.IServicerOrderManager;
import com.wzitech.gamegold.order.business.*;
import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 订单改变到已付款状态的管理接口实现类
 * 
 * @author ztjie
 * 
 */
@Component
public class Paid extends AbstractState {

	protected static final Log log = LogFactory.getLog(Paid.class);

	@Autowired
	IOrderInfoDBDAO orderInfoDBDAO;
	
	@Autowired
	IRepositoryManager repositoryManager;

	/*@Autowired
	ILogManager logManager;*/
    @Autowired
    IOrderLogManager orderLogManager;
	
	@Autowired
	IServicerOrderManager servicerOrderManager;
	
	@Autowired
	IAutoConfigManager autoConfigManager;

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
        OrderInfoEO orderInfo = orderInfoDBDAO.queryOrderForUpdate(orderId);

		// 订单状态无改变，就不做后续操作
		if (orderInfo.getOrderState() != null && orderState.getCode() == orderInfo.getOrderState()) {
			return null;
		}

		// 订单状态先前状态必须是待付款或待发货
		// 添加前置状态可以为已取消，修复用户实际支付，但是订单状态已被修改为已取消问题
		if (orderInfo.getOrderState() != OrderState.WaitPayment.getCode()
				//&& orderInfo.getOrderState() != OrderState.WaitDelivery.getCode()
				&& orderInfo.getOrderState() != OrderState.Cancelled.getCode()) {
			logger.error("订单号：{}, 设置成已付款失败，先前状态为：{}", orderId, orderInfo.getOrderState());
			throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(),
					"订单先前状态必须是待付款");
		}

		/*
		 * // 订单状态只能修改成待发货或已退款 if (orderState.getCode() ==
		 * OrderState.WaitDelivery.getCode() || orderState.getCode() ==
		 * OrderState.Refund.getCode()) { throw new StateChangeException(
		 * StateChangeException.StateAfterNotInCode, "订单状态只能修改成待发货或已退款"); }
		 */

		//订单状态不会从待发货状态退回到已付款状态(不做分单的时候要用这种方式进行订单退回重配置)
		/*// 订单先前状态必须是待发货，发货库存不够，要退回到已付款状态
		if (orderInfo.getOrderState().intValue() == OrderState.WaitDelivery.getCode()) {
			// 查询出之前对订单配置的库存信息，并修改之前的库存信息
			List<ConfigResultInfoEO> oldConfigs = configResultInfoDBDAO
					.selectByOrderId(orderInfo.getOrderId());
			for (ConfigResultInfoEO config : oldConfigs) {
				RepositoryInfo repositoryInfo = config.getRepositoryInfo();
				if(repositoryInfo!=null){
					repositoryInfo.setGoldCount(repositoryInfo.getGoldCount()
							+ config.getConfigGoldCount());
					repositoryManager.modifyRepository(repositoryInfo);				
				}
			}
			// 删除之前对订单配置的信息
			configResultInfoDBDAO.deleteByOrderId(orderInfo.getOrderId());
		}*/

		// 增加订单修改日志
		StringBuffer sb = new StringBuffer();
		sb.append("订单号:").append(orderInfo.getOrderId()).append("，订单状态从");
		sb.append(OrderState.getTypeByCode(orderInfo.getOrderState()).getName());

		// 修改订单状态
		orderInfo.setOrderState(orderState.getCode());

		sb.append("变成").append(orderState.getName());

		//logManager.add(ModuleType.ORDER, sb.toString(), CurrentUserContext.getUser());
		log.info(sb.toString());

        OrderLogInfo orderLogInfo = new OrderLogInfo();
        orderLogInfo.setLogType(LogType.ORDER_PAID);
        orderLogInfo.setOrderId(orderId);
        orderLogInfo.setRemark(sb.toString());
        orderLogManager.add(orderLogInfo);

		return orderInfo;
	}

	@Override
	@Transactional
	public OrderInfoEO handle(String orderId) throws SystemException {
		OrderInfoEO orderInfo = this.changeOrderState(orderId, OrderState.Paid);
		if (orderInfo == null) {
			return orderInfo;
		}
		// 更新订单状态
		orderInfo.setPayTime(new Date());
		orderInfoDBDAO.updateOrderState(orderInfo);
		//商城销售订单同步推送Mq
		int orderStatus= OrderCenterOrderStatus.WAIT_SEND.getCode();
		orderPushMainManager.orderPushMain(orderInfo);

		/* //客服处理订单数增1
		servicerOrderManager.addOrderNum(orderInfo.getGameName(),
				orderInfo.getRegion(), orderInfo.getServer(),
				orderInfo.getGameRace(), orderInfo.getServicerId());*/

		serviceSortManager.incOrderCount(orderInfo.getServicerId());

		autoConfigManager.autoConfigOrder(orderInfo);
		return orderInfo;
	}


}
