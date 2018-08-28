package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.repository.business.IShrobotRefundOrderManager;
import com.wzitech.gamegold.shorder.business.IFundDetailManager;
import com.wzitech.gamegold.shorder.business.ISystemConfigManager;
import com.wzitech.gamegold.shorder.entity.FundDetail;
import com.wzitech.gamegold.shorder.entity.PayOrder;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金明细
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class FundDetailAction extends AbstractAction {
    private FundDetail fundDetail;

    private List<FundDetail> fundDetailList;

    private Date startTime;

    private Date endTime;

    private String orderId;

    private String loginAccount;

    @Autowired
    private IFundDetailManager fundDetailManager;
    @Autowired
    private IShrobotRefundOrderManager shrobotRefundOrderManager;
    @Autowired
    ISystemConfigManager systemConfigManager;

    public String queryFundDetail() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (fundDetail != null) {
            if (StringUtils.isNotBlank(fundDetail.getBuyerAccount())) {
                queryMap.put("buyerAccount", fundDetail.getBuyerAccount().trim());
            }
            if (StringUtils.isNotBlank(fundDetail.getPayOrderId())) {
                queryMap.put("payOrderId", fundDetail.getPayOrderId().trim());
            }
            if (StringUtils.isNotBlank(fundDetail.getRefundOrderId())) {
                queryMap.put("refundOrderId", fundDetail.getRefundOrderId().trim());
            }
            if (StringUtils.isNotBlank(fundDetail.getDeliveryOrderId())) {
                queryMap.put("deliveryOrderId", fundDetail.getDeliveryOrderId().trim());
            }
            if (StringUtils.isNotBlank(fundDetail.getPayDetailOrderId())) {
                queryMap.put("payDetailOrderId", fundDetail.getPayDetailOrderId());
            }
            if (fundDetail.getType() != null && fundDetail.getType() != 0) {
                queryMap.put("type", fundDetail.getType());
            }
            queryMap.put("startTime", startTime);
            queryMap.put("endTime", WebServerUtil.oneDateLastTime(endTime));
        }
        GenericPage<FundDetail> genericPage = fundDetailManager.queryPage(queryMap, this.limit, this.start, "create_time", false);
        fundDetailList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 内部接口，用于补偿
     *
     * @return
     */
    public String refund() {
        String currentLoginAccount = CurrentUserContext.getUserLoginAccount();
        if(!currentLoginAccount.equals("wangjj@5173.com")){
            return this.returnError("权限不够");
        }
        SystemConfig automateTimeout = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.AUTO_TIMEOUT_PAY_ORDER.getKey());
        if (automateTimeout == null || !automateTimeout.getEnabled()) {
            return this.returnError("配置不存在");
        }
        String configValue = automateTimeout.getConfigValue();
        int day = Integer.parseInt(configValue);//超过多少天自动退款

        shrobotRefundOrderManager.autoRefundTimeoutPayOrderPrivate(orderId, loginAccount, day);
        return this.returnSuccess();
    }

    public FundDetail getFundDetail() {
        return fundDetail;
    }

    public void setFundDetail(FundDetail fundDetail) {
        this.fundDetail = fundDetail;
    }

    public List<FundDetail> getFundDetailList() {
        return fundDetailList;
    }

    public void setFundDetailList(List<FundDetail> fundDetailList) {
        this.fundDetailList = fundDetailList;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }
}
