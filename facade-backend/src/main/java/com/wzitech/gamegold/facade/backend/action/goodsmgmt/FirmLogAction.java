package com.wzitech.gamegold.facade.backend.action.goodsmgmt;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.goods.business.IFirmsLogManager;
import com.wzitech.gamegold.goods.entity.FirmsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/17  wurongfan           ZW_J_JB_00072金币商城对外接口优化
 *
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class FirmLogAction extends AbstractAction {

    @Autowired
    IFirmsLogManager firmsLogManager;

    /**
     * 查询符合条件的日志记录
     */
    public String selectLogs(){
        Map<String,Object> queryMap=new HashMap<String, Object>();
        queryMap.put("createStartTime",createStartTime);
        queryMap.put("createEndTime", WebServerUtil.oneDateLastTime(createEndTime));
        GenericPage<FirmsLog> genericPage=firmsLogManager.selectLog(queryMap,this.limit,this.start,"id",false);
        firmsLogList=genericPage.getData();
        totalCount=genericPage.getTotalCount();
        return this.returnSuccess();
    }

    private Date createStartTime;

    private Date createEndTime;

    private List<FirmsLog> firmsLogList;


    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    public List<FirmsLog> getFirmsLogList() {
        return firmsLogList;
    }

    public void setFirmsLogList(List<FirmsLog> firmsLogList) {
        this.firmsLogList = firmsLogList;
    }
}
