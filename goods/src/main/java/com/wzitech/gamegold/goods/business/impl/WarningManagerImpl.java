package com.wzitech.gamegold.goods.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.goods.entity.Warning;
import com.wzitech.gamegold.goods.business.IWarningManager;
import com.wzitech.gamegold.goods.dao.IWarningDBDAO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 友情提示配置接口实现类
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/15  liuchanghua           ZW_C_JB_00008 商城增加通货
 */
@Component
public class WarningManagerImpl extends AbstractBusinessObject implements
		IWarningManager {
	protected static final Log log = LogFactory.getLog(WarningManagerImpl.class);
	
	@Autowired
	IWarningDBDAO warningDBDAO;


	@Override
	public Long insert(Warning warning) {
		Map map = new HashedMap();
		if(StringUtils.isEmpty(warning.getGameName())){
			warning.setGameName("");
		}
		if(StringUtils.isEmpty(warning.getGoodsTypeName())){
			warning.setGoodsTypeName("");
		}
		map.put("gameName",warning.getGameName());
		map.put("goodsTypeName",warning.getGoodsTypeName());
		map.put("tradeType",warning.getTradeType());
		List<Warning> list = warningDBDAO.queryByGameAndGoodsType(map);
		if(CollectionUtils.isNotEmpty(list)){
			throw new SystemException(ResponseCodes.ExistWarning.getCode());
		}
		warningDBDAO.insert(warning);
		return warning.getId();
	}

	@Override
	@Transactional
	public void deleteWarning(List<Long> ids) throws SystemException {
		if(ids==null||ids.size()==0){
			throw new SystemException(ResponseCodes.EmptyWarningId.getCode());
		}
		warningDBDAO.batchDeleteByIds(ids);
	}

	@Override
	public void update(Warning warning) {
		if(warning.getId()==null){
			throw new SystemException(ResponseCodes.EmptyWarningId.getCode(),
					ResponseCodes.EmptyWarningId.getMessage());
		}
		warningDBDAO.update(warning);
	}


	@Override
	public Warning findById(Long id) {
		return warningDBDAO.selectById(id);
	}

	@Override
	public GenericPage<Warning> selectByMap(Map<String,Object> queryMap, int limit, int start,
													 String sortBy, boolean isAsc){
		if(StringUtils.isEmpty(sortBy)){
			sortBy = "ID";
		}
		return warningDBDAO.selectByMap(queryMap, limit, start, sortBy, isAsc);
	}

	@Override
	public List<Warning> queryByMap(Map<String,Object> queryMap, String sortBy, boolean isAsc){
		if(StringUtils.isEmpty(sortBy)){
			sortBy = "ID";
		}
		return warningDBDAO.selectByMap(queryMap,sortBy, isAsc);
	}

}
