package com.wzitech.gamegold.facade.backend.action.shpurchase;


import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.utils.DateUtil;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.IPurchaseOrderManager;
import com.wzitech.gamegold.shorder.entity.PurchaseOrder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购单
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class shPurchaseOrderAction extends AbstractAction {

    private PurchaseOrder purchaseOrder;

    @Autowired
    private IPurchaseOrderManager purchaseOrderManager;

    private List<PurchaseOrder> purchaserOrderList;

    private Date sTime;

    private Date eTime;

    private String isOnline;

    private Long id;

    //-----------------------------------------------------------------------

    /**
     * 查询采购单列表
     * @return
     */
    public String queryShPurchaseOrder(){
        Map<String,Object> queryMap=new HashMap<String, Object>();
        if(StringUtils.isNotBlank(purchaseOrder.getGameName())){
            queryMap.put("gameName",purchaseOrder.getGameName());
        }
        if (StringUtils.isNotBlank(purchaseOrder.getBuyerAccount())){
            queryMap.put("buyerAccount",purchaseOrder.getBuyerAccount());
        }
        if(purchaseOrder.getDeliveryType()!=null&&purchaseOrder.getDeliveryType()!=0){
            queryMap.put("deliveryType",purchaseOrder.getDeliveryType());
        }
        if(isOnline!=null&&!isOnline.equals("-1")){
            if(isOnline.equals("true")){
                queryMap.put("isOnline",true);
            }else{
                queryMap.put("isOnline",false);
            }
        }
        queryMap.put("createStartTime", sTime);
        queryMap.put("createEndTime",  DateUtil.oneDateLastTime(eTime));
        GenericPage<PurchaseOrder> genericPage=purchaseOrderManager.queryPurchaseOrder
                (queryMap,this.limit,this.start,"id",false);
        purchaserOrderList=genericPage.getData();
        totalCount=genericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 上架
     * @return
     */
    public String online(){

        purchaseOrderManager.online(getId());
        return this.returnSuccess();

    }

    /**
     * 下架
     * @return
     */
    public String offline(){

        purchaseOrderManager.oldOffline(getId());
        return this.returnSuccess();
    }

    //-----------------------------------------------------------------------


    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public Date getSTime() {
        return sTime;
    }

    public void setSTime(Date sTime) {
        this.sTime = sTime;
    }

    public Date getETime() {
        return eTime;
    }

    public void setETime(Date eTime) {
        this.eTime = eTime;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public IPurchaseOrderManager getPurchaseOrderManager() {
        return purchaseOrderManager;
    }

    public void setPurchaseOrderManager(IPurchaseOrderManager purchaseOrderManager) {
        this.purchaseOrderManager = purchaseOrderManager;
    }

    public List<PurchaseOrder> getPurchaserOrderList() {
        return purchaserOrderList;
    }

    public void setPurchaserOrderList(List<PurchaseOrder> purchaserOrderList) {
        this.purchaserOrderList = purchaserOrderList;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
