package com.wzitech.gamegold.order.business.impl.orderstate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.order.business.IOrderStateManager;
import com.wzitech.gamegold.order.entity.OrderInfoEO;

/**
 * 订单状态管理接口实现类
 *
 * @author ztjie
 *
 */
@Component
public class OrderStateManagerImpl extends AbstractBusinessObject implements IOrderStateManager {

	protected static final Log log = LogFactory.getLog(OrderStateManagerImpl.class);

	@Autowired
	State waitPayment;

	@Autowired
	State paid;

	@Autowired
	State waitDelivery;

	@Autowired
	State delivery;

	@Autowired
	State receive;

	@Autowired
	State statement;

	@Autowired
	State refund;

	@Autowired
	State cancelled;

	@Override
	@Transactional
	public OrderInfoEO changeOrderState(String orderId, OrderState orderState) throws SystemException{
		OrderInfoEO orderInfo = null;
		if (orderId == null) {
			log.info("修改订单状态，但是传的订单号为空");
			throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
		}
		if (orderState == null) {
			log.info("修改订单状态，但是传的订单状态为空");
			throw new SystemException(ResponseCodes.EmptyOrderState.getCode(), ResponseCodes.EmptyOrderState.getMessage());
		}
		switch (orderState.getCode()) {
			case 1:
				orderInfo = waitPayment.handle(orderId);
				break;
			case 2:
				orderInfo = paid.handle(orderId);
				break;
			case 3:
				orderInfo = waitDelivery.handle(orderId);
				break;
			case 4:
				orderInfo = delivery.handle(orderId);
				break;
			case 5:
				orderInfo = statement.handle(orderId);
				break;
			case 6:
				orderInfo = refund.handle(orderId);
				break;
			case 7:
				orderInfo = cancelled.handle(orderId);
				break;
			case 8:
				orderInfo = receive.handle(orderId);
				break;
			default:
				break;
		}
		return orderInfo;
	}

	@Override
	@Transactional
	public void changeConfigState(Long configId, OrderState state)
			throws SystemException {
		if (configId == null) {
			log.info("修改配置状态，但是传的配置信息ID为空");
			throw new SystemException(ResponseCodes.EmptyConfigResultId.getCode(), ResponseCodes.EmptyConfigResultId.getMessage());
		}
		if (state == null) {
			log.info("修改配置状态，但是传的配置信息状态为空");
			throw new SystemException(ResponseCodes.EmptyOrderState.getCode(), ResponseCodes.EmptyOrderState.getMessage());
		}
		switch (state.getCode()) {
			case 1:
				waitPayment.changeConfigState(configId);
				break;
			case 2:
				paid.changeConfigState(configId);
				break;
			case 3:
				waitDelivery.changeConfigState(configId);
				break;
			case 4:
				delivery.changeConfigState(configId);
				break;
			case 5:
				statement.changeConfigState(configId);
				break;
			case 6:
				refund.changeConfigState(configId);
				break;
			case 7:
				cancelled.changeConfigState(configId);
				break;
			case 8:
				receive.changeConfigState(configId);
				break;
			default:
				break;
		}
	}


}
