package com.wzitech.gamegold.order.business.impl.orderstate;

import java.util.Date;

import com.wzitech.gamegold.common.enums.LogType;
import com.wzitech.gamegold.common.enums.OrderCenterOrderStatus;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.business.IOrderLogManager;
import com.wzitech.gamegold.common.log.entity.OrderLogInfo;
import com.wzitech.gamegold.order.business.IOrderPushMainManager;
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

/**
 * 订单改变到等待付款状态的管理接口实现类
 * 
 * @author ztjie
 * 
 */
@Component
public class WaitPayment extends AbstractState {
	protected static final Log log = LogFactory.getLog(WaitPayment.class);

	@Autowired
	IOrderInfoDBDAO orderInfoDBDAO;

	/*@Autowired
	ILogManager logManager;*/
    @Autowired
    IOrderLogManager orderLogManager;

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
		OrderInfoEO orderInfo = orderInfoDBDAO.selectUniqueByProp("orderId",
				orderId);

		// 订单状态无改变，就不做后续操作
		if (orderState.equals(orderInfo.getOrderState())) {
			return null;
		}

		// 增加订单修改日志
		StringBuffer sb = new StringBuffer();
		sb.append("订单号：").append(orderInfo.getOrderId()).append("，订单状态从");
		if(orderInfo.getOrderState()==null){
			sb.append("无");
		}else{
			sb.append(OrderState.getTypeByCode(orderInfo.getOrderState()).getName());			
		}

		// 修改订单状态
		orderInfo.setOrderState(orderState.getCode());

		sb.append("变成").append(orderState.getName());

		//logManager.add(ModuleType.ORDER, sb.toString(), CurrentUserContext.getUser());
		log.info(sb.toString());

        OrderLogInfo orderLogInfo = new OrderLogInfo();
        orderLogInfo.setLogType(LogType.ORDER_CREATE);
        orderLogInfo.setOrderId(orderId);
        orderLogInfo.setRemark(sb.toString());
        orderLogManager.add(orderLogInfo);

		return orderInfo;
	}

	@Override
	@Transactional
	public OrderInfoEO handle(String orderId) throws SystemException {
		OrderInfoEO orderInfo = this.changeOrderState(orderId,
				OrderState.WaitPayment);
		if(orderInfo==null){
			return orderInfo;
		}
		// 更新订单状态
		orderInfo.setCreateTime(new Date());
		orderInfoDBDAO.updateOrderState(orderInfo);

		//商城销售订单同步推送Mq
		int orderStatus= OrderCenterOrderStatus.WAIT_PAY.getCode();
		orderPushMainManager.orderPushMain(orderInfo);
//		 客服处理订单数增1
//		servicerOrderManager.addOrderNum(orderInfo.getGameName(),
//				orderInfo.getRegion(), orderInfo.getServer(),
//				orderInfo.getGameRace(), orderInfo.getServicerId());
		return orderInfo;
	}
}
