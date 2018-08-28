package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.order.business.IConfigPowerManager;
import com.wzitech.gamegold.order.entity.ConfigPowerEO;


/**
 * Created by Administrator on 2017/1/4.
 *
 *  Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/12  liuchanghua           ZW_C_JB_00008 商城增加通货
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class ConfigPowerAction extends AbstractAction {
	
	private Boolean configPower;
	
	private String gameName;
	
	private Integer tradeType;

	private String goodsTypeName;

	private List<ConfigPowerEO> configPowerList;
	
	private Integer configMaxCount;
	
	private Long id;

	private ConfigPowerEO configPowerEO;

	@Autowired
	IConfigPowerManager configPowerManager;
	
	/**
	 * 查询自动配单权限配置信息列表
	 * ZW_C_JB_00008_20170512 增加通货类型查询 update by lch
	 * @return
	 */
	public String queryConfig() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("gameName", gameName);
		map.put("configPower",configPower);
		map.put("tradeType", tradeType);
		map.put("goodsTypeName",goodsTypeName);////ZW_C_JB_00008_20170512 ADD
		try {
			GenericPage<ConfigPowerEO> genericPage = configPowerManager.queryConfig(map,this.limit,this.start,"ID",true);
			configPowerList = genericPage.getData();
			totalCount = genericPage.getTotalCount();
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}
	
	/*
	 *修改自动配单交易方式 
	 */
	public String alterTradeType(){
		try {
			configPowerManager.alterTradeType(id);
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}
	
	/*
	 *开启自动配单
	 */
	public String givePower(){
		try {
			configPowerManager.givePower(id);
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}
	
	/*
	 *取消自动配单 
	 */
	public String cancelPower(){
		try {
			configPowerManager.cancelPower(id);
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}
	/*
	 * 修改自动配单最大数额
	 */
	public String modifyMaxCount(){
		try{
			configPowerManager.modifyMaxCount(id, configMaxCount);
			return this.returnSuccess();
		}catch(SystemException e){
			return this.returnError(e);
		}
	}

	/*
	 * 新增管理配单
	 * @param configPowerEO
	 * ZW_C_JB_00008_20170512 增加通货类型查询 update by lch ADD
	 * @return
	 */
	/**ZW_C_JB_00008_20170512 START ADD**/
	public String addConfigPower(){
		try{
			configPowerManager.addConfigPower(configPowerEO);
			return this.returnSuccess();
		}catch(SystemException e){
			return this.returnError(e);
		}
	}
	/**ZW_C_JB_00008_20170512 END**/

	public Boolean getConfigPower() {
		return configPower;
	}

	public void setConfigPower(Boolean configPower) {
		this.configPower = configPower;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public String getGoodsTypeName() {
		return goodsTypeName;
	}

	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}

	public List<ConfigPowerEO> getConfigPowerList() {
		return configPowerList;
	}

	public void setConfigPowerList(List<ConfigPowerEO> configPowerList) {
		this.configPowerList = configPowerList;
	}

	public Integer getConfigMaxCount() {
		return configMaxCount;
	}

	public void setConfigMaxCount(Integer configMaxCount) {
		this.configMaxCount = configMaxCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ConfigPowerEO getConfigPowerEO() {
		return configPowerEO;
	}

	public void setConfigPowerEO(ConfigPowerEO configPowerEO) {
		this.configPowerEO = configPowerEO;
	}

	public IConfigPowerManager getConfigPowerManager() {
		return configPowerManager;
	}

	public void setConfigPowerManager(IConfigPowerManager configPowerManager) {
		this.configPowerManager = configPowerManager;
	}
}