/************************************************************************************
 *  Copyright 2012 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		AccountServiceImpl
 *	包	名：		com.wzitech.chinabeauty.facade.service.usermgmt.impl
 *	项目名称：	chinabeauty-facade-frontend
 *	作	者：		SunChengfei
 *	创建时间：	2013-9-26
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2013-9-26 下午2:51:45
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.backend.action.common;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.log.business.ILogManager;
import com.wzitech.gamegold.common.log.entity.LogInfo;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;

@Controller
@Scope("prototype")
@ExceptionToJSON
public class LogAction extends AbstractAction {

	protected static final Log log = LogFactory.getLog(LogAction.class);

	private LogInfo logInfo;

	private List<LogInfo> logList;

	private Date createStartTime;

	private Date createEndTime;

	@Autowired
	ILogManager logManager;
	
	public String queryLog() {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("createStartTime", createStartTime);
		queryMap.put("createEndTime", createEndTime);
		queryMap.put("currentUserId", logInfo.getCurrentUserId());
		queryMap.put("module", logInfo.getModule());
		queryMap.put("currentUserType", logInfo.getCurrentUserType());
		queryMap.put("currentThreadId", logInfo.getCurrentThreadId());
		queryMap.put("operateInfo", logInfo.getOperateInfo());
		GenericPage<LogInfo> genericPage = logManager.queryLog(queryMap, limit, start, "CREATE_TIME", false);
		logList = genericPage.getData();
		totalCount = genericPage.getTotalPageCount();
		return this.returnSuccess();
	}

	public LogInfo getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(LogInfo logInfo) {
		this.logInfo = logInfo;
	}

	public List<LogInfo> getLogList() {
		return logList;
	}

	public void setCreateStartTime(Date createStartTime) {
		this.createStartTime = createStartTime;
	}

	public void setCreateEndTime(Date createEndTime) {
		this.createEndTime = createEndTime;
	}

}