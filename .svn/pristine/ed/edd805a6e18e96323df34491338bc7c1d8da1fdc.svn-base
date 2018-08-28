package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.service.shorder.IMainGameConfigService;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.MainGameConfigResponse;
import com.wzitech.gamegold.shorder.business.IMainGameConfigManager;
import com.wzitech.gamegold.shorder.entity.MainGameConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;


/**
 * Created by jhlcitadmin on 2017/2/23.
 */
@Service("MainGameConfigService")
@Path("maingameconfig")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class MainGameConfigServiceImpl extends AbstractBaseService implements IMainGameConfigService{


    @Autowired
    private IMainGameConfigManager mainGameConfigManager;
    /**
     * 获取游戏主配置
     * @return
     */
    @GET
    @Override
    @Path("getgamelist")
    public IServiceResponse getMainGameConfig() {
        MainGameConfigResponse response=new MainGameConfigResponse();
        ResponseStatus responseStatus=new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            List<MainGameConfig> mainGameConfigList=mainGameConfigManager.getMainGameConfig();
            response.setMainGameConfigList(mainGameConfigList);
            responseStatus.setStatus(ResponseCodes.Success.getCode(),
                    ResponseCodes.Success.getMessage());
        }catch (SystemException ex){
            responseStatus.setStatus(ex.getErrorCode(),ex.getErrorMsg());
            logger.error("获取总游戏配置发生异常:{}",ex);
        }catch (Exception e){
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取总游戏配置发生未知错误", e);
        }
        return response;
    }
}
