package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.service.shorder.IAddressConfigService;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.AddressConfigResponse;
import com.wzitech.gamegold.shorder.business.IConfigManager;
import com.wzitech.gamegold.shorder.entity.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import java.util.List;

/**
 * 交易地址
 */
@Service("AddressConfigService")
@Path("addressConfig")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class AddressConfigServiceImpl extends AbstractBaseService implements IAddressConfigService {
    @Autowired
    IConfigManager configManager;



    @Path("getConfigByGameName")
    @GET
    @Override
    public IServiceResponse getConfigByGameName(@QueryParam("gameName")  String gameName) {
        AddressConfigResponse response = new AddressConfigResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {
            List<Config> configList = configManager.getConfigByGameName(gameName);
            response.setAddressList(configList);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取交易地址发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取交易地址发生未知异常:{}", e);
        }

        return response;
    }

}
