package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.shorder.business.IFundManager;
import com.wzitech.gamegold.shorder.business.IPayOrderManager;
import com.wzitech.gamegold.shorder.entity.PayOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 充值明细
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class PayOrderAction extends AbstractAction{
    private PayOrder payOrder;

    private Date startTime;


    private Date endTime;

    private List<PayOrder> payOrderlIST;

    @Autowired
    IPayOrderManager payOrderManager;
    @Autowired
    IFundManager fundManager;

    /**
     * 查询充值明细信息列表
     */
    public String queryPayOrder(){
        Map<String,Object> queryMap=new HashMap<String, Object>();
        if(payOrder!=null) {
            if (StringUtils.isNotBlank(payOrder.getOrderId())) {
                queryMap.put("orderId", payOrder.getOrderId().trim());
            }
            if (StringUtils.isNotBlank(payOrder.getUid().trim())) {
                queryMap.put("uid", payOrder.getUid().trim());
            }
            if (StringUtils.isNotBlank(payOrder.getAccount().trim())) {
                queryMap.put("account", payOrder.getAccount().trim());
            }
            if (payOrder.getStatus() != null) {
                if(Integer.valueOf(payOrder.getStatus())== -1){
                    queryMap.put("status", null);
                } else {
                    queryMap.put("status", Integer.valueOf(payOrder.getStatus()));
                }
            }
            queryMap.put("startCreateTime", startTime);
            queryMap.put("endCreateTime", WebServerUtil.oneDateLastTime(endTime));
        }
        GenericPage<PayOrder> genericPage=payOrderManager.queryPayOrders(queryMap,this.start,this.limit,"create_time",false);
        payOrderlIST=genericPage.getData();
        totalCount=genericPage.getTotalCount();
        return  this.returnSuccess();
    }

    /**
     * 人工补单
     */
    public String manualPayShOrder(){
        try {
            fundManager.manualPayShOrder(payOrder.getOrderId());
            return  this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public void setPayOrder(PayOrder payOrder) {
        this.payOrder = payOrder;
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

    public List<PayOrder> getPayOrderlIST() {
        return payOrderlIST;
    }

    public void setPayOrderlIST(List<PayOrder> payOrderlIST) {
        this.payOrderlIST = payOrderlIST;
    }

    public PayOrder getPayOrder() {
        return payOrder;
    }



}
