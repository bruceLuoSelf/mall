package com.wzitech.gamegold.facade.frontend.service.shorder.impl;


/**
 * Created by Administrator on 2017/1/6.
 */

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.dto.BaseResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.IShGameConfigService;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaserRepositoryLimitResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.ShGameConfigResponse;
import com.wzitech.gamegold.shorder.business.IPurchaserDataManager;
import com.wzitech.gamegold.shorder.business.IShGameConfigManager;
import com.wzitech.gamegold.shorder.entity.PurchaserData;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收货游戏配置
 */
@Service("ShGameConfigService")
@Path("shGameConfig")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class ShGameConfigServiceImpl extends AbstractBaseService implements IShGameConfigService {
    @Autowired
    IShGameConfigManager shGameConfigManager;

    @Autowired
    IPurchaserDataManager purchaserDataManager;

    /**
     * 查询金币的收货开关配置
     *
     * @param gameName
     * @return
     */
    @Path("getConfigByGameName")
    @GET
    @Override
    public IServiceResponse getConfigByGameName(@QueryParam("gameName") String gameName) {
        ShGameConfigResponse response = new ShGameConfigResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            ShGameConfig shGameConfig = shGameConfigManager.getConfigByGameName(gameName, ServicesContants.GOODS_TYPE_GOLD, true, null);
            response.setShGameConfig(shGameConfig);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取收货游戏发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取收货游戏发生未知异常:{}", e);
        }

        return response;

    }

    /**
     * 根据游戏名称和开关查询游戏类目配置
     *
     * @param gameName   游戏名称，不能为空
     * @param isEnabled  收货开关，可以为空
     * @param enableMall 商城开关，可以为空
     * @return
     */
    @Path("getAllConfigByGameName")
    @GET
    @Override
    public IServiceResponse getAllConfigByGameName(@QueryParam("gameName") String gameName, @QueryParam("isEnabled") Boolean isEnabled, @QueryParam("enableMall") Boolean enableMall) {
        ShGameConfigResponse response = new ShGameConfigResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            if (StringUtils.isBlank(gameName)) {
                responseStatus.setStatus(ResponseCodes.EmptyGameName.getCode(), ResponseCodes.EmptyGameName.getMessage());
                return response;
            }
            List<ShGameConfig> configList = shGameConfigManager.getConfigByGameNameAndSwitch(gameName, isEnabled, enableMall);
            response.setShGameConfigList(configList);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("根据游戏名称和开关查询游戏类目配置发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据游戏名称和开关查询游戏类目配置发生未知异常:{}", e);
        }

        return response;

    }

    @Override
    @GET
    @Path("getAllConfigInfo")
    @Produces("application/json;charset=UTF-8")
    public IServiceResponse getConfigForALL() {
        ShGameConfigResponse response = new ShGameConfigResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            List<ShGameConfig> list = shGameConfigManager.selectEnableConfig();
            response.setShGameConfigList(list);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取收货游戏发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取收货游戏发生未知异常:{}", e);
        }

        return response;
    }

    /**
     * ADD_20170609_通货优化
     *
     * @param gameName
     * @param goodsTypeName
     * @return
     */
    @Path("getGameConfig")
    @GET
    @Override
    public IServiceResponse getGameConfig(@QueryParam("gameName") String gameName, @QueryParam("goodsTypeName") String goodsTypeName) {
        ShGameConfigResponse response = new ShGameConfigResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            ShGameConfig shGameConfig = shGameConfigManager.getConfigByGameName(gameName, goodsTypeName, null, null);
            response.setShGameConfig(shGameConfig);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取收货配置最低购买金额发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取收货配置最低购买金额发生未知异常:{}", e);
        }
        return response;
    }

    @Override
    @Path("getGameList")
    @GET
    public IServiceResponse getGameList(@QueryParam("goodsTypeName") String goodsTypeName) {
        BaseResponse response = new BaseResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            if (StringUtils.isNotBlank(goodsTypeName)) {
                map.put("goodsTypeName",goodsTypeName);
                map.put("isEnabled",true);
            }
            List<ShGameConfig> list = shGameConfigManager.queryByMap(map, "id", false);
            response.setData(list);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("根据商品类型获取游戏列表发生异常", e);
        }catch (Exception e){
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据商品类型获取游戏列表发生未知异常:{}", e);
        }
        return response;
    }

    /**
     * 查询收货商的库存上限配置
     * @param gameName
     * @param goodsTypeName
     * @return
     */
    @Path("queryRepositoryLimit")
    @GET
    @Override
    public IServiceResponse queryRepositoryLimit(@QueryParam("gameName")String gameName, @QueryParam("goodsTypeName") String goodsTypeName) {
        PurchaserRepositoryLimitResponse response = new PurchaserRepositoryLimitResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try{
            String loginAccount = CurrentUserContext.getUser().getLoginAccount();
            if (StringUtils.isBlank(loginAccount)) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
            }
            if (StringUtils.isBlank(gameName)) {
                throw new SystemException(ResponseCodes.EmptyGameName.getCode(),ResponseCodes.EmptyGameName.getMessage());
            }
            if (StringUtils.isBlank(goodsTypeName)) {
                throw new SystemException(ResponseCodes.EmptyGoodsType.getCode(),ResponseCodes.EmptyGoodsType.getMessage());
            }
            ShGameConfig shGameConfig = shGameConfigManager.getConfigByGameName(gameName, goodsTypeName, null, null);
            if (shGameConfig == null) {
                throw new SystemException(ResponseCodes.NotAvailableConfig.getCode(),ResponseCodes.NotAvailableConfig.getMessage());
            }
            PurchaserData purchaserData = purchaserDataManager.queryUnique(loginAccount);
            if (purchaserData == null) {
                throw new SystemException(ResponseCodes.NoPurchaseData.getCode(),ResponseCodes.NoPurchaseData.getMessage());
            }
            response.setIsSplit(purchaserData.getIsSplit() == null ? false : purchaserData.getIsSplit());
            response.setPurchaserNeedCount(purchaserData.getNeedCount());
            response.setPurchaserRepositoryCount(purchaserData.getRepositoryCount());
            response.setGameRepositoryCount(shGameConfig.getRepositoryCount());
            response.setGameNeedCount(shGameConfig.getNeedCount());
            response.setSplitRepositorySwitch(shGameConfig.getIsSplit() == null ? false : shGameConfig.getIsSplit());
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        }catch (SystemException e){
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("查询收货商的库存上限配置发生异常", e);
        }catch (Exception e) {
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("查询收货商的库存上限配置发生未知异常:{}", e);
        }
        return response;
    }

    /**
     * 修改收货商库存限制
     * @param isSplit
     * @param repositoryCount
     * @param needCount
     * @return
     */
    @Path("updateRepositoryLimit")
    @GET
    @Override
    public IServiceResponse updateRepositoryLimit(@QueryParam("isSplit") Boolean isSplit,
                                                  @QueryParam("repositoryCount")Long repositoryCount,
                                                  @QueryParam("needCount")Long needCount) {
        BaseResponse response = new BaseResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try{
            if (isSplit != null && isSplit && (repositoryCount == null || needCount == null)) {
                throw new SystemException(ResponseCodes.NullRepositoryCount.getCode(),ResponseCodes.NullRepositoryCount.getMessage());
            }
            purchaserDataManager.updateRepositoryCount(isSplit,repositoryCount,needCount);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        }catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("修改收货商库存限制发生异常", e);
        }catch (Exception e){
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("修改收货商库存限制发生未知异常:{}", e);
        }
        return response;
    }
}
