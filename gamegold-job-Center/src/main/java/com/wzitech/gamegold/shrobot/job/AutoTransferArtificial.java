package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.order.business.IAutoConfigManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ljn
 * @date 2018/5/29.
 * 自动转人工操作的订单
 * 每5分钟检查一次,把5分钟之前的订单修改成人工操作状态
 */
@Component
@JobHandler("autoTransferArtificial")
public class AutoTransferArtificial extends IJobHandler {

    protected final Log log = LogFactory.getLog(getClass());

    @Autowired
    IAutoConfigManager autoConfigManager;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            UserInfoEO user = new UserInfoEO();
            user.setId(-1L);
            user.setLoginAccount("autoTrans");
            user.setUserType(UserType.System.getCode());
            CurrentUserContext.setUser(user);
            log.info("自动转人工操作订单定时器开始");
            autoConfigManager.autoTrans(300);
            log.info("自动转人工操作订单定时器结束");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("autoTransferArtificial异常:{}",e);
            return FAIL;
        }
    }
}
