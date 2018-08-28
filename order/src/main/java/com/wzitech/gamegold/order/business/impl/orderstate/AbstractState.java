package com.wzitech.gamegold.order.business.impl.orderstate;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.order.entity.OrderInfoEO;

public class AbstractState extends AbstractBusinessObject implements State{

	@Override
	public OrderInfoEO handle(String orderId) throws SystemException {
		return null;
	}

	@Override
	public void changeConfigState(Long configId) {
	}

}
