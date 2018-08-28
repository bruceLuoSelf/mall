package com.wzitech.gamegold.facade.backend.action.log;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.LogType;
import com.wzitech.gamegold.common.log.entity.GoodsLogInfo;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.goods.business.IGoodsLogManager;

@Controller
@Scope("prototype")
@ExceptionToJSON
public class GoodsLogAction extends AbstractAction{
	@Autowired
	private IGoodsLogManager goodsLogManager;
	
	private LogType logType;
    private String userAccount;
    private Date startTime;
    private Date endTime;
    private String gameName;
    private String region;
    private String server;
    private String gameRace;
    private String title;
    

	private List<GoodsLogInfo> logs;


	/**
     * 查询商品日志
     * @return
     */
    public String queryGoodsLogs() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("logType", logType);
        queryMap.put("userAccount", userAccount);
        queryMap.put("startTime", startTime);
        queryMap.put("endTime", WebServerUtil.oneDateLastTime(endTime));
        queryMap.put("gameName", gameName);
        queryMap.put("region", region);
        queryMap.put("server", server);
        queryMap.put("gameRace", gameRace);
        queryMap.put("title", title);
        GenericPage<GoodsLogInfo> genericPage = goodsLogManager.queryLog(queryMap, limit, start, "ID", false);
        logs = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }
	
	
	
	public List<GoodsLogInfo> getLogs() {
		return logs;
	}


	public void setGoodsLogManager(IGoodsLogManager goodsLogManager) {
		this.goodsLogManager = goodsLogManager;
	}


	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setLogType(LogType logType) {
		this.logType = logType;
	}


	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}


	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}


	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	public void setGameName(String gameName) {
		this.gameName = gameName;
	}


	public void setRegion(String region) {
		this.region = region;
	}


	public void setServer(String server) {
		this.server = server;
	}


	public void setGameRace(String gameRace) {
		this.gameRace = gameRace;
	}

}
