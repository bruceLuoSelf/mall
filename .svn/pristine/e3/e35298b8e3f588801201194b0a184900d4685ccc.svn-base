package com.wzitech.gamegold.gamegold.app.service.order.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import com.wzitech.gamegold.gamegold.app.service.order.AppNewInterfaceService;
import com.wzitech.gamegold.gamegold.app.service.order.dto.AppNewInterfaceRequest;
import com.wzitech.gamegold.gamegold.app.service.order.dto.AppNewInterfaceResponse;
import com.wzitech.gamegold.shorder.business.ISystemConfigManager;
import com.wzitech.gamegold.shorder.dao.ISystemConfigDBDAO;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;

/**
 * Created by 340032 on 2018/3/22.
 */
@Service("RowConfigService")
@Path("rowConfig")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class RowConfigImpl extends AbstractBaseService implements AppNewInterfaceService {

    @Autowired
    private ISystemConfigManager systemConfigManager;

    @Path("queryRowSwitch")
    @GET
    @Override
    public AppNewInterfaceResponse queryAppNewInterface() {
        logger.info("根据配置值查app新接口切换配置查询请求：{}");
        // 初始化返回数据
        AppNewInterfaceResponse response=new AppNewInterfaceResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            // 查询配置   SystemConfigEnum
            SystemConfig systemConfig=systemConfigManager.getSystemConfig(SystemConfigEnum.APP_SWITCH.getKey());
            if (systemConfig ==null){
                throw new SystemException(
                        ResponseCodes.NotAppCurrency.getCode(), ResponseCodes.NotAppCurrency.getCode());
            }
            response.setEnabled(systemConfig.getEnabled());
            logger.info("根据配置值查app新接口是否开启查询成功:{}");
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.info("根据配置值与是否开启开关配置查询发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(
                    ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.info("根据配置值与是否开启开关配置查询发生未知异常:{}", ex);
        }
        logger.info("根据配置值与是否开启开关配置查询响应信息:{}", response);
        return response;
    }
}
