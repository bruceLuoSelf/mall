package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.shorder.business.IPayDetailManager;
import com.wzitech.gamegold.shorder.entity.PayDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 付款明细
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class PayDetailAction extends AbstractAction{
    private PayDetail payDetail;

    private Date startTime;

    private Date endTime;

//    private String account;

    private List<PayDetail> payDetailList;

    @Autowired
    IPayDetailManager payDetailManager;

    /**
     * 查询付款明细信息列表
     */
    public String queryPayDetail(){
        Map<String,Object> queryMap=new HashMap<String, Object>();
        if(payDetail!=null) {
            if(StringUtils.isNotBlank(payDetail.getOrderId())){
                queryMap.put("orderId", payDetail.getOrderId().trim());
            }
            if(StringUtils.isNotBlank(payDetail.getPayOrderId().trim())){
                queryMap.put("payOrderId",payDetail.getPayOrderId().trim());
            }
            if(StringUtils.isNotBlank(payDetail.getChOrderId())){
                queryMap.put("chOrderId",payDetail.getChOrderId());
            }
            queryMap.put("startCreateTime",startTime);
            queryMap.put("endCreateTime", WebServerUtil.oneDateLastTime(endTime));
        }
            GenericPage<PayDetail> genericPage=payDetailManager.queryListInPage(queryMap,this.start,this.limit,"create_time",false);
            payDetailList=genericPage.getData();
            totalCount=genericPage.getTotalCount();
            return this.returnSuccess();
    }

    public PayDetail getPayDetail() {
        return payDetail;
    }

    public void setPayDetail(PayDetail payDetail) {
        this.payDetail = payDetail;
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

    public List<PayDetail> getPayDetailList() {
        return payDetailList;
    }

    public void setPayDetailList(List<PayDetail> payDetailList) {
        this.payDetailList = payDetailList;
    }

//    public String getAccount() {
//        return account;
//    }
//
//    public void setAccount(String account) {
//        this.account = account;
//    }
}
