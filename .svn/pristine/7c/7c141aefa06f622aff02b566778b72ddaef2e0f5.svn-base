package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.gamegold.common.enums.FundsQueryType;
import com.wzitech.gamegold.common.paymgmt.IPayManager;
import com.wzitech.gamegold.common.paymgmt.dto.QueryDetailResponse;
import com.wzitech.gamegold.shorder.business.IAutoPayShManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 汪俊杰 on 2016/12/17.
 */
@Component
public class AutoPayShManagerImpl implements IAutoPayShManager {
    @Autowired
    IPayManager payManager;

    @Override
    public Boolean queryPaymentDetail(String orderId, String uid) {
        QueryDetailResponse queryDetailResponse = payManager.queryDetail(FundsQueryType.Payment.getCode(), orderId, uid);
        /**
         * 提交状态(CommitStatus)：0：初始，1：已提交，2：取消
         * 转账状态(EarnStatus)：0：初始，1：成功，3：取消
         * 注：判断资金明细是否生效一般判断转账状态为1即可。除(1：支付以外)
         */
        if(queryDetailResponse!=null && queryDetailResponse.getCommitStatus()==1
                && queryDetailResponse.getTradeNo().equals(orderId)){
            return true;
        }else if(queryDetailResponse!=null && queryDetailResponse.getCommitStatus()!=1){
            return false;
        }else {
            return null;
        }
    }
}
