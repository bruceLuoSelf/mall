package com.wzitech.gamegold.order.business.impl.orderstate;

import com.wzitech.gamegold.common.enums.LogType;
import com.wzitech.gamegold.common.enums.OrderCenterOrderStatus;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.business.IOrderLogManager;
import com.wzitech.gamegold.common.log.entity.OrderLogInfo;
import com.wzitech.gamegold.order.business.IOrderPushMainManager;
import com.wzitech.gamegold.order.business.IOrderStateManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.order.dao.IConfigResultInfoDBDAO;
import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;

/**
 * 订单改变到等待发货状态的管理接口实现类
 * 
 * @author ztjie
 * 
 */
@Component
public class WaitDelivery extends AbstractState {

	protected static final Log log = LogFactory.getLog(WaitDelivery.class);

	@Autowired
	IOrderInfoDBDAO orderInfoDBDAO;
	
	@Autowired
	IConfigResultInfoDBDAO configResultInfoDBDAO;

	/*@Autowired
	ILogManager logManager;*/
    @Autowired
    IOrderLogManager orderLogManager;

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

		// 订单状态先前状态必须是已付款
		if (orderInfo.getOrderState() != OrderState.Paid.getCode()) {
			throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(),
					"订单先前状态必须是已付款");
		}

		/*
		 * // 订单状态只能修改成已发货、已退款或已付款 if (orderState.getCode() ==
		 * OrderState.Delivery.getCode() || orderState.getCode() ==
		 * OrderState.Refund.getCode() || orderState.getCode() ==
		 * OrderState.Paid.getCode()) { throw new StateChangeException(
		 * StateChangeException.StateAfterNotInCode, "订单状态只能修改成已发货、已退款或已付款"); }
		 */

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
        orderLogInfo.setLogType(LogType.ORDER_DELIVERY);
        orderLogInfo.setOrderId(orderId);
        orderLogInfo.setRemark(sb.toString());
        orderLogManager.add(orderLogInfo);

		return orderInfo;
	}

	@Override
	@Transactional
	public OrderInfoEO handle(String orderId) throws SystemException {
		OrderInfoEO orderInfo = this.changeOrderState(orderId,
				OrderState.WaitDelivery);
		if(orderInfo==null){
			return orderInfo;
		}
		// 更新订单状态
		logger.info("配单开始，更新订单状态开始：{}", orderInfo);
		orderInfoDBDAO.updateOrderState(orderInfo);
		logger.info("配单开始，推送Mq开始：{}", orderInfo);
		//商城销售订单同步推送Mq
		int orderStatus= OrderCenterOrderStatus.WAIT_SEND.getCode();
		orderPushMainManager.orderPushMain(orderInfo);
		logger.info("配单开始，推送Mq结束：{}", orderInfo);
		//设置订单配置结果的状态
		configResultInfoDBDAO.updateStateByOrderId(orderId, OrderState.WaitDelivery.getCode());
		logger.info("配单开始，设置订单配置结果的状态结束：{}", orderInfo);
		return orderInfo;
	}

	@Override
	@Transactional
	public void changeConfigState(Long configId) {
		configResultInfoDBDAO.updateStateById(configId, OrderState.WaitDelivery.getCode());
	}
}
