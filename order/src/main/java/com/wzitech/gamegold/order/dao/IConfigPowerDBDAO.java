
package com.wzitech.gamegold.order.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.order.entity.ConfigPowerEO;

/**
 * 自动配单权限配置DB
 */
public interface IConfigPowerDBDAO extends IMyBatisBaseDAO<ConfigPowerEO, Long>{
	
	/**
	 * 通过单独的gameNamede得到唯一对象
	 */
	ConfigPowerEO getByGameName(String gameName, String goodsTypeName);
}
