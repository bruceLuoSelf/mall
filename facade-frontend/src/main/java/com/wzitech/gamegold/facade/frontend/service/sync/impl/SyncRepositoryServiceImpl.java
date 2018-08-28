package com.wzitech.gamegold.facade.frontend.service.sync.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.AddressConfigResponse;
import com.wzitech.gamegold.facade.frontend.service.sync.ISyncRepositoryService;
import com.wzitech.gamegold.facade.frontend.service.sync.dto.SyncRepositoryRequest;
import com.wzitech.gamegold.facade.frontend.service.sync.dto.SyncRepositoryResponse;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.shorder.business.impl.ISyncRepositoryManager;
import com.wzitech.gamegold.shorder.entity.Config;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.ws.rs.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 340082
 * @date 2018/6/12
 */
@Path("sync")
@Service("SyncRepositoryService")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class SyncRepositoryServiceImpl extends AbstractBaseService implements ISyncRepositoryService {

    @Resource(name = "syncRepositoryManager")
    ISyncRepositoryManager syncRepositoryManager;

    @Autowired
    IRepositoryManager repositoryManager;

    @Path("syncSelectRepositoryPrice")
    @GET
    @Override
    public IServiceResponse syncSelectRepositoryPrice(@QueryParam("")SyncRepositoryRequest request) {
        SyncRepositoryResponse response = new SyncRepositoryResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {
            if(StringUtils.isBlank(request.getGameName()) || StringUtils.isBlank(request.getRegion()) || StringUtils.isBlank(request.getServer())){
                throw new SystemException(ResponseCodes.NullData.getCode(),ResponseCodes.NullData.getMessage());
            }
            Map<String, Object> selectMap = new HashMap<String, Object>();
            selectMap.put("gameName",request.getGameName());
            selectMap.put("region",request.getRegion());
            selectMap.put("server",request.getServer());
            selectMap.put("loginAccount", CurrentUserContext.getUserLoginAccount());
            selectMap.put("isDeleted", false);
            List<RepositoryInfo> repositoryInfos = repositoryManager.selectRepositoryByMap(selectMap);
            BigDecimal unitPrice = BigDecimal.ZERO;
            if(repositoryInfos != null && repositoryInfos.size()!= 0){
                unitPrice = repositoryInfos.get(0).getUnitPrice();
            }
            response.setPrice(unitPrice);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("syncSelectRepositoryPrice发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("syncSelectRepositoryPrice发生未知异常:{}", e);
        }
        return response;
    }

    @Path("syncAddRepository")
    @GET
    @Override
    public IServiceResponse syncAddRepository(@QueryParam("")SyncRepositoryRequest request) {
        SyncRepositoryResponse response = new SyncRepositoryResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {
            if(request.getShGameAccountId() == null || request.getPrice() == null){
                throw new SystemException(ResponseCodes.NullData.getCode(),ResponseCodes.NullData.getMessage());
            }
            syncRepositoryManager.syncAddRepository(request.getShGameAccountId(),request.getPrice());
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("syncAddRepository发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("syncAddRepository发生未知异常:{}", e);
        }

        return response;
    }

}
