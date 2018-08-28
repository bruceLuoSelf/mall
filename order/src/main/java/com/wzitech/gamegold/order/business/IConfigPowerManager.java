package com.wzitech.gamegold.order.business;

import java.util.Map;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.entity.ConfigPowerEO;

/**
 * 自动配单权限管理接口
 */
public interface IConfigPowerManager {
	
	/**
	 * 
	 * <p>分页查询出库订单信息列表</p> 
	 * @param queryMap
	 * @param limit
	 * @param start
	 * @param orderBy
	 * @param isAsc
	 * @return
	 * @see
	 */
	GenericPage<ConfigPowerEO> queryConfig(Map<String, Object> queryMap,
			int limit, int start, String orderBy, boolean isAsc);
	
	/**
	 * 修改自动配单交易方式
	 * param id
	 */
	public ConfigPowerEO alterTradeType(Long id);
	
	/**
	 * 开启自动配单
	 * param id
	 */
	public ConfigPowerEO givePower(Long id);
	
	/**
	 * 取消自动配单
	 * param id
	 */
	public ConfigPowerEO cancelPower(Long id);
	/**
	 * 修改最大配单数额
	 * @param id
	 * @param configMaxCount
	 * @return 
	 */
	public ConfigPowerEO modifyMaxCount(Long id,Integer configMaxCount);

	/**
	 * 新增管理配单
	 * @param configPowerEO
	 * ZW_C_JB_00008_20170512 增加通货类型查询 update by lch ADD
	 * @return
	 */
	public ConfigPowerEO addConfigPower(ConfigPowerEO configPowerEO);
}
