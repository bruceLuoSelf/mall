package com.wzitech.gamegold.facade.backend.action.shpurchase;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.IPurchaserDataManager;
import com.wzitech.gamegold.shorder.entity.PurchaserData;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.*;

/**
 * 采购商
 * Created by Administrator on 2016/12/14.
 */

@Controller
@Scope("prototype")
@ExceptionToJSON
public class shPurchaseAction extends AbstractAction {

    private PurchaserData purchaserDatas;

    private Date startTime;

    private Date endTime;

    @Autowired
    private IPurchaserDataManager purchaserDataManager;

    private List<PurchaserData> purchaserDataList;


    //-----------------------------------------------------------------------

    /**
     * 查询采购商列表
     * @return
     */
    public String queryShPurchase() {
        Map<String,Object> queryMap=new HashMap<String, Object>();
        if(StringUtils.isNotBlank(purchaserDatas.getName())){
            queryMap.put("name",purchaserDatas.getName());
        }
        if (StringUtils.isNotBlank(purchaserDatas.getLoginAccount())){
            queryMap.put("loginAccount",purchaserDatas.getLoginAccount());
        }
        GenericPage<PurchaserData> purchaserDataGenericPage=purchaserDataManager.
                queryPage(queryMap,this.limit,this.start);
        purchaserDataList =purchaserDataGenericPage.getData();
        totalCount = purchaserDataGenericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 修改采购商
     * @return
     */
    public String updateShPurchase(){
        purchaserDataManager.updatePurchaser(purchaserDatas);
        return this.returnSuccess();
    }

    //--------------------------------------------------------------------------------


    public PurchaserData getPurchaserDatas() {
        return purchaserDatas;
    }

    public IPurchaserDataManager getPurchaserDataManager() {
        return purchaserDataManager;
    }

    public void setPurchaserDataManager(IPurchaserDataManager purchaserDataManager) {
        this.purchaserDataManager = purchaserDataManager;
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

    public void setPurchaserDatas(PurchaserData purchaserDatas) {
        this.purchaserDatas = purchaserDatas;
    }

    public List<PurchaserData> getPurchaserDataList() {
        return purchaserDataList;
    }

    public void setPurchaserDataList(List<PurchaserData> purchaserDataList) {
        this.purchaserDataList = purchaserDataList;
    }
}
