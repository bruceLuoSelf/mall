package com.wzitech.gamegold.order.business.impl.orderstate;

import com.wzitech.gamegold.common.enums.LogType;
import com.wzitech.gamegold.common.enums.OrderCenterOrderStatus;
import com.wzitech.gamegold.order.business.*;
import com.wzitech.gamegold.common.log.entity.OrderLogInfo;
import com.wzitech.gamegold.order.entity.InsuranceOrder;
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
import com.wzitech.gamegold.order.entity.OrderInfoEO;

import java.util.Date;

/**
 * 订单改变到已发货状态的管理接口实现类
 * 
 * @author ztjie
 * 
 */
@Component
public class Delivery extends AbstractState {

	protected static final Log log = LogFactory.getLog(Delivery.class);

	@Autowired
	IOrderInfoDBDAO orderInfoDBDAO;

	/*@Autowired
	ILogManager logManager;*/
    @Autowired
    IOrderLogManager orderLogManager;
	
	@Autowired
	IServicerOrderManager servicerOrderManager;

	@Autowired
	private IInsuranceOrderManager insuranceOrderManager;

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

		// 订单状态先前状态必须是待发货
		if (orderInfo.getOrderState() != OrderState.WaitDelivery.getCode()) {
			throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(),
					"订单先前状态必须是待发货");
		}

		/*
		 * // 订单状态只能修改成已收货、结单 if (orderState.getCode() ==
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
		// 设置发货时间
		orderInfo.setSendTime(new Date());

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
				OrderState.Delivery);
		if(orderInfo==null){
			return orderInfo;
		}
		// 更新订单状态
		orderInfoDBDAO.updateOrderState(orderInfo);
		//商城销售订单同步推送Mq
		int orderStatus= OrderCenterOrderStatus.ALREADY_SEND.getCode();
		orderPushMainManager.orderPushMain(orderInfo);
		// 创建需要创建保单的订单记录
		if (orderInfo.getIsBuyInsurance() != null && orderInfo.getIsBuyInsurance()) {
			InsuranceOrder insuranceOrder = new InsuranceOrder();
			insuranceOrder.setOrderId(orderId);
			insuranceOrder.setType(InsuranceOrder.TYPE_CREATE_ORDER);
			insuranceOrderManager.addOrder(insuranceOrder);
		}

		/*// 客服处理订单数减1
		servicerOrderManager.subOrderNum(orderInfo.getGameName(),
				orderInfo.getRegion(), orderInfo.getServer(),
				orderInfo.getGameRace(), orderInfo.getServicerId());*/

		serviceSortManager.decOrderCount(orderInfo.getServicerId());

		log.info("订单状态修改成功");
		return orderInfo;
	}
}
