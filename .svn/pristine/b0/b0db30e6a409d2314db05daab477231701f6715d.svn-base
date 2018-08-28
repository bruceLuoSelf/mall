package com.wzitech.gamegold.facade.frontend.service.agree.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.main.IMainStationManager;
import com.wzitech.gamegold.facade.frontend.service.agree.IAgrreInitService;
import com.wzitech.gamegold.facade.frontend.service.agree.dto.AgrreInitResponse;
import com.wzitech.gamegold.repository.business.IAgreeInitAccountManager;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


/**
 * Created by 340032 on 2017/8/19.
 */
@Service("AgrreService")
@Path("/agrre")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class AgrreInitService  extends AbstractBaseService implements IAgrreInitService {
    @Autowired
    IAgreeInitAccountManager agreeInitAccountManager;
    @Autowired
    IMainStationManager mainStationManager;
    /**
     * 初始化老用户 绑定7bao账户 设置未读字段
     * @param
     * @return
     */
    @GET
    @Path("/agreeInitAccount")
    @Override
    public IServiceResponse agreeInitAccount() {
        AgrreInitResponse response = new AgrreInitResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        IUser user = CurrentUserContext.getUser();
        String uid = user.getUid();
        logger.info("当前用户uid:{}",uid);
        try {
            String realName = agreeInitAccountManager.getRealName(uid);
            if ("-1".equals(realName)){
                throw new SystemException(ResponseCodes.UnRealName.getCode(),ResponseCodes.UnRealName.getMessage());
            }
            agreeInitAccountManager.initAccount(realName);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        }catch (SystemException ex) {
            // 捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.error("同意协议发生异常:{}", ex);
        } catch (Exception e){
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("同意协议发生异常:{}", e);
        }
        return response;
    }



}
