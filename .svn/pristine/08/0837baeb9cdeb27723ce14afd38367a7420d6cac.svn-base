package com.wzitech.gamegold.order.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.order.dao.IConfigPowerDBDAO;
import com.wzitech.gamegold.order.entity.ConfigPowerEO;

@Repository
public class ConfigPowerDBDAOImpl extends AbstractMyBatisDAO<ConfigPowerEO, Long> implements IConfigPowerDBDAO{

	@Override
	public ConfigPowerEO getByGameName(String gameName, String goodsTypeName) {
		Map map = new HashMap();
		map.put("gameName", gameName);
		map.put("goodsTypeName", goodsTypeName);
		List<ConfigPowerEO> configPowerlists = this.getSqlSession().selectList(this.getMapperNamespace()+".selectByGameName",map);
		if(configPowerlists==null || configPowerlists.size() != 1){
			throw new SystemException(
					ResponseCodes.ConfigPower.getCode(),ResponseCodes.ConfigPower.getMessage());
		}
		return configPowerlists.get(0);
	}
}
