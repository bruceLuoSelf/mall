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
import com.wzitech.gamegold.common.log.entity.RepositoryLogInfo;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.repository.business.IRepositoryLogManager;

/**
 * 库存日志查询
 * @author Chen Guanghui
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class RepositoryLogAction extends AbstractAction {
	@Autowired
	private IRepositoryLogManager repositoryLogManager;
	
	private LogType logType;
    private String userAccount;
    private Date startTime;
    private Date endTime;
    private String orderId;
    private String gameName;
    private String region;
    private String server;
    private String gameRace;
    private String sellerAccount;
    private String gameAccount;
    private String sellerGameRole;
    

	private List<RepositoryLogInfo> logs;
    
    
    
    /**
     * 查询库存日志
     * @return
     */
    public String queryRepositoryLogs() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("logType", logType);
        queryMap.put("userAccount", userAccount);
        queryMap.put("startTime", startTime);
        queryMap.put("endTime", WebServerUtil.oneDateLastTime(endTime));
        queryMap.put("orderId", orderId);
        queryMap.put("gameName", gameName);
        queryMap.put("region", region);
        queryMap.put("server", server);
        queryMap.put("gameRace", gameRace);
        queryMap.put("sellerAccount", sellerAccount);
        queryMap.put("gameAccount", gameAccount);
        queryMap.put("sellerGameRole", sellerGameRole);
        GenericPage<RepositoryLogInfo> genericPage = repositoryLogManager.queryLog(queryMap, limit, start, "ID", false);
        logs = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }
    
    public void setSellerAccount(String sellerAccount) {
		this.sellerAccount = sellerAccount;
	}

	public void setGameAccount(String gameAccount) {
		this.gameAccount = gameAccount;
	}

	public void setSellerGameRole(String sellerGameRole) {
		this.sellerGameRole = sellerGameRole;
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

	public List<RepositoryLogInfo> getLogs() {
        return logs;
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

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
