package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.shorder.business.ISplitRepositoryLogManager;
import com.wzitech.gamegold.shorder.entity.SplitRepositoryLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分仓日志
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class SplitRepositoryLogAction extends AbstractAction {
    private SplitRepositoryLog splitRepositoryLog;

    private Date startTime;

    private Date endTime;

    private List<SplitRepositoryLog> splitRepositoryLogList;

    @Autowired
    ISplitRepositoryLogManager splitRepositoryLogManager;

    /**
     * 根据订单号查询收货日志
     * @return
     */
    public String querySplitRepositoryLog(){
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if(splitRepositoryLog!=null) {
            if(StringUtils.isNotBlank(splitRepositoryLog.getBuyerAccount())){
                queryMap.put("buyerAccount", splitRepositoryLog.getBuyerAccount().trim());
            }
            if(StringUtils.isNotBlank(splitRepositoryLog.getGameAccount())){
                queryMap.put("gameAccount", splitRepositoryLog.getGameAccount().trim());
            }
//            if(splitRepositoryLog.getFcId()!=null){
//                queryMap.put("fcId", splitRepositoryLog.getFcId());
//            }
            if(splitRepositoryLog.getFcOrderId()!=null){
                queryMap.put("fcOrderId", splitRepositoryLog.getFcOrderId());
            }
            queryMap.put("startCreateTime", startTime);
            queryMap.put("endCreateTime", WebServerUtil.oneDateLastTime(endTime));
        }
        GenericPage<SplitRepositoryLog> genericPage= splitRepositoryLogManager.queryListInPage(queryMap, this.start,this.limit,"create_time", false);
        splitRepositoryLogList=genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    public SplitRepositoryLog getSplitRepositoryLog() {
        return splitRepositoryLog;
    }

    public void setSplitRepositoryLog(SplitRepositoryLog splitRepositoryLog) {
        this.splitRepositoryLog = splitRepositoryLog;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<SplitRepositoryLog> getSplitRepositoryLogList() {
        return splitRepositoryLogList;
    }

    public void setSplitRepositoryLogList(List<SplitRepositoryLog> splitRepositoryLogList) {
        this.splitRepositoryLogList = splitRepositoryLogList;
    }
}
