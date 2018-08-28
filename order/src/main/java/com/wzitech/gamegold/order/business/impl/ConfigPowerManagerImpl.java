package com.wzitech.gamegold.order.business.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.TradeType;
import com.wzitech.gamegold.order.business.IConfigPowerManager;
import com.wzitech.gamegold.order.dao.IConfigPowerDBDAO;
import com.wzitech.gamegold.order.entity.ConfigPowerEO;

/**
 * 配单权限管理接口实现类
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/12  liuchanghua           ZW_C_JB_00008 商城增加通货
 */
@Component
public class ConfigPowerManagerImpl extends AbstractBusinessObject implements
		IConfigPowerManager {
	protected static final Log log = LogFactory.getLog(ConfigPowerManagerImpl.class);
	
	@Autowired
	IConfigPowerDBDAO configPowerDBDAO;

	@Override
	public GenericPage<ConfigPowerEO> queryConfig(Map<String, Object> queryMap,
			int limit, int start, String orderBy, boolean isAsc) {
		if(StringUtils.isEmpty(orderBy)){
			orderBy = "ID";
		}
		GenericPage<ConfigPowerEO> genericPage = configPowerDBDAO.selectByMap(queryMap, limit, start,orderBy,isAsc);
		return genericPage;
	}

	@Override
	public ConfigPowerEO alterTradeType(Long id) {
		ConfigPowerEO configPowerInfo = configPowerDBDAO.selectById(id);
		if(null == configPowerInfo){
			throw new SystemException(
					ResponseCodes.EmptyConfigPower.getCode(), ResponseCodes.EmptyConfigPower.getMessage());
		}
		Integer tradeType = configPowerInfo.getTradeType();
		if(null == tradeType){
			throw new SystemException(
					ResponseCodes.EmptyTradeType.getCode(), ResponseCodes.EmptyTradeType.getMessage());
		}
		if(tradeType==TradeType.Divided.getCode()){
			configPowerInfo.setTradeType(TradeType.NoDivid.getCode());
		}
		if(tradeType==TradeType.NoDivid.getCode()){
			configPowerInfo.setTradeType(TradeType.Divided.getCode());
		}
		configPowerDBDAO.update(configPowerInfo);
		return configPowerInfo;
	}

	@Override
	public ConfigPowerEO givePower(Long id) {
		ConfigPowerEO configPowerInfo = configPowerDBDAO.selectById(id);
		if(null == configPowerInfo){
			throw new SystemException(
					ResponseCodes.EmptyConfigPower.getCode(), ResponseCodes.EmptyConfigPower.getMessage());
		}
		configPowerInfo.setConfigPower(true);
		configPowerDBDAO.update(configPowerInfo);
		return configPowerInfo;
	}

	@Override
	public ConfigPowerEO cancelPower(Long id) {
		ConfigPowerEO configPowerInfo = configPowerDBDAO.selectById(id);
		if(null == configPowerInfo){
			throw new SystemException(
					ResponseCodes.EmptyConfigPower.getCode(), ResponseCodes.EmptyConfigPower.getMessage());
		}
		configPowerInfo.setConfigPower(false);
		configPowerDBDAO.update(configPowerInfo);
		return configPowerInfo;
	}

	@Override
	public ConfigPowerEO modifyMaxCount(Long id, Integer configMaxCount) {
		ConfigPowerEO configPowerInfo = configPowerDBDAO.selectById(id); 
		if(null == configPowerInfo){
			throw new SystemException(
					ResponseCodes.EmptyConfigPower.getCode(), ResponseCodes.EmptyConfigPower.getMessage());
		}
		configPowerInfo.setConfigMaxCount(configMaxCount);
		configPowerDBDAO.update(configPowerInfo);
		return configPowerInfo;
	}


	/**
	 * 新增管理配单
	 * @param configPowerEO
	 * ZW_C_JB_00008_20170512 增加通货类型查询 update by lch ADD
	 * @return
     */
	public ConfigPowerEO addConfigPower(ConfigPowerEO configPowerEO) {
		Map map = new HashedMap();
		map.put("gameName",configPowerEO.getGameName());
		map.put("goodsTypeName",configPowerEO.getGoodsTypeName());
		List<ConfigPowerEO> configPowerEOList = configPowerDBDAO.selectByMap(map,"ID",false);
		if(configPowerEOList.size()>0){
			throw new SystemException(
					ResponseCodes.ConfigPowerIsExist.getCode(), ResponseCodes.ConfigPowerIsExist.getMessage());
		}
		configPowerEO.setTradeType(1);
		configPowerDBDAO.insert(configPowerEO);
		return configPowerEO;
	}
	
}
