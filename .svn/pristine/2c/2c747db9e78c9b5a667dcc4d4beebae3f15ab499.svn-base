package com.wzitech.gamegold.funds.business.impl;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.funds.business.ITransactionInfoManager;
import com.wzitech.gamegold.funds.dao.ITransactionInfoDBDAO;
import com.wzitech.gamegold.funds.entity.TransactionInfo;

/**
 * 订单管理接口实现类
 * @author SunChengfei
 *
 */
@Component
public class TransactionInfoManagerImpl extends AbstractBusinessObject implements ITransactionInfoManager{
	
	@Autowired
	ITransactionInfoDBDAO transactionInfoDBDAO;

	@Override
	public GenericPage<TransactionInfo> queryTransactionList(
			Map<String, Object> queryMap, String orderBy, boolean isAsc,
			int pageSize, int start) throws SystemException {
		if(StringUtils.isEmpty(orderBy)){
			orderBy = "ID";
		}
		queryMap.put("configResultIsDel", false);
		return transactionInfoDBDAO.selectByMap(queryMap, pageSize, start, orderBy, isAsc);
	}

	@Override
	public List<TransactionInfo> queryTransactionList(
			Map<String, Object> queryMap, String orderBy, boolean isAsc) {
		if(StringUtils.isEmpty(orderBy)){
			orderBy = "ID";
		}
		queryMap.put("configResultIsDel", false);
		List<TransactionInfo> transactionInfos = transactionInfoDBDAO.selectAllByMap(queryMap,orderBy, isAsc);
		return transactionInfos;
	}
}
