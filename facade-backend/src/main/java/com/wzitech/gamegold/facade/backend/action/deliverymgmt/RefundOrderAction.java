package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.RefundStatus;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.shorder.business.IRefundOrderManager;
import com.wzitech.gamegold.shorder.entity.RefundOrder;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 退款订单
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class RefundOrderAction extends AbstractAction {
    private RefundOrder refundOrder;

    private List<RefundOrder> refundOrderList;

    private Date startTime;

    private Date endTime;

    @Autowired
    private IRefundOrderManager refundOrderManager;

    public String queryRefundOrder(){
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            if(refundOrder!=null){
                if(StringUtils.isNotBlank(refundOrder.getOrderId())){
                    queryMap.put("orderId", refundOrder.getOrderId().trim());
                }
                if(StringUtils.isNotBlank(refundOrder.getPayOrderId())){
                    queryMap.put("payOrderId", refundOrder.getPayOrderId().trim());
                }
                if(refundOrder.getStatus()!=0){
                    queryMap.put("status",refundOrder.getStatus());
                }
                if(StringUtils.isNotBlank(refundOrder.getBuyerAccount())){
                    queryMap.put("buyerAccount", refundOrder.getBuyerAccount().trim());
                }
                if(StringUtils.isNotBlank(refundOrder.getUid())){
                    queryMap.put("uid", refundOrder.getUid().trim());
                }
                if(StringUtils.isNotBlank(refundOrder.getPhone())){
                    queryMap.put("phone", refundOrder.getPhone().trim());
                }
                if(StringUtils.isNotBlank(refundOrder.getName())){
                    queryMap.put("name", refundOrder.getName().trim());
                }
                if(StringUtils.isNotBlank(refundOrder.getQq())){
                    queryMap.put("qq", refundOrder.getQq().trim());
                }

                queryMap.put("createStartTime", startTime);
                queryMap.put("createEndTime", WebServerUtil.oneDateLastTime(endTime));
            }
            GenericPage<RefundOrder> genericPage= refundOrderManager.queryListInPage(queryMap, this.start,this.limit,"create_time", false);
            refundOrderList=genericPage.getData();
            totalCount = genericPage.getTotalCount();
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }


    /**
     * 删除卖家
     *
     * @return
     */
    public String auditRefund() {
        try {
            UserInfoEO user = (UserInfoEO) CurrentUserContext.getUser();
            if(user==null){
                return this.returnError("登录超时，请重新登录！");
            }

            refundOrder.setAuditor(user.getLoginAccount());
            refundOrder.setAuditTime(new Date());

            String msg= refundOrderManager.updateRefundOrder(refundOrder);

            //返回操作结果
            if(!msg.equals("")){
                return this.returnError(msg);
            }
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<RefundOrder> getRefundOrderList() {
        return refundOrderList;
    }

    public void setRefundOrderList(List<RefundOrder> refundOrderList) {
        this.refundOrderList = refundOrderList;
    }

    public void setRefundOrder(RefundOrder refundOrder) {
        this.refundOrder = refundOrder;
    }

    public RefundOrder getRefundOrder() {
        return refundOrder;
    }
}