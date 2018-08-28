package com.wzitech.gamegold.facade.backend.action.goodsmgmt;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.goods.business.ISevenBaoBindLogManager;
import com.wzitech.gamegold.goods.entity.SevenBaoBindLogEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/16 wangmin				ZW_C_JB_00021 商城资金改造
 *
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class SevenBaoBindLogAction extends AbstractAction {
    @Autowired
    ISevenBaoBindLogManager sevenBaoBindLogManager;
    /**
     * 查询绑定7bao 的日志记录
     * @return
     */
    public String selectLog(){
        Map<String,Object> querymap= new HashMap<String,Object>();
        if (StringUtils.isNotBlank(userAccount)){
            querymap.put("userAccount",userAccount);
        }
        if(userType != null){
            querymap.put("userType",userType);
        }
        querymap.put("startTime",startTime);
        querymap.put("endTime", WebServerUtil.oneDateLastTime(endTime));
        GenericPage<SevenBaoBindLogEO> page = sevenBaoBindLogManager.selectLog(querymap,this.limit,this.start,"id",false);
        sevenBaoBindLogEOList=page.getData();
        totalCount=page.getTotalCount();
        return this.returnSuccess();
    }

    private SevenBaoBindLogEO sevenBaoBindLogEO;
    private Date startTime;

    private Date endTime;

    private String userAccount;

    private Integer userType;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    private List<SevenBaoBindLogEO> sevenBaoBindLogEOList;

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

    public List<SevenBaoBindLogEO> getSevenBaoBindLogEOList() {
        return sevenBaoBindLogEOList;
    }

    public void setSevenBaoBindLogEOList(List<SevenBaoBindLogEO> sevenBaoBindLogEOList) {
        this.sevenBaoBindLogEOList = sevenBaoBindLogEOList;
    }
}
