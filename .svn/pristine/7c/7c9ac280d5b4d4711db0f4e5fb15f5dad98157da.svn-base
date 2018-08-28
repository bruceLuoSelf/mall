/************************************************************************************
 * Copyright 2014 WZITech Corporation. All rights reserved.
 * <p>
 * 作	者：		lvshuyan
 * 创建时间：	    2017-5-12
 * 描	述：
 * 更新纪录：	1. lvshuyan 创建于 2017-5-12
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.game.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.RefererType;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.dto.BaseResponse;
import com.wzitech.gamegold.facade.frontend.service.game.IGameCategoryConfigService;
import com.wzitech.gamegold.facade.frontend.service.game.dto.QueryWarningRequest;
import com.wzitech.gamegold.goods.business.IWarningManager;
import com.wzitech.gamegold.goods.entity.Warning;
import com.wzitech.gamegold.shorder.business.IGameCategoryConfigManager;
import com.wzitech.gamegold.shorder.entity.GameCategoryConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询商品类型接口
 *  Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/16  liuchanghua           ZW_C_JB_00008 商城增加通货
 */
@Service("GameCategoryConfigService")
@Path("gameCategory")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class GameCategoryConfigServiceImp extends AbstractBaseService implements IGameCategoryConfigService {
    @Autowired
    IGameCategoryConfigManager gameCategoryConfigManager;
    @Autowired
    IWarningManager warningManager;

    /**
     * 查询所有启用的商品类型
     * @return
     */
    @Path("queryAllGameCategory")
    @GET
    @Override
    public IServiceResponse queryAllGameCategory() {
        logger.info("查询所有启用的商品类型开始");
        // 初始化返回数据
        BaseResponse<GameCategoryConfig> response = new BaseResponse<GameCategoryConfig>();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            Map map = new HashMap();
            map.put("isEnabled", true);
            List<GameCategoryConfig> gameCategoryConfigList = gameCategoryConfigManager.queryPage(map, "create_time", true);
            response.setData(gameCategoryConfigList);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("查询所有启用的商品类型发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(
                    ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("查询所有启用的商品类型发生未知异常:{}", ex);
        }
        logger.info("查询所有启用的商品类型响应信息:{}", response);
        return response;
    }


    /**
     * 根据游戏名、商品类型查询友情提示
     * @return
     */
    @Path("queryWarning")
    @GET
    @Override
    public IServiceResponse queryWarning(@QueryParam("") QueryWarningRequest queryWarningRequest) {
        logger.info("根据游戏名、商品类型查询友情提示开始");
        // 初始化返回数据
        BaseResponse<Warning> response = new BaseResponse<Warning>();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        try {
            Map map = new HashMap();
            if (StringUtils.isBlank(queryWarningRequest.getGameName())) {
                throw new SystemException(ResponseCodes.EmptyGameName.getCode(), ResponseCodes.EmptyGameName.getMessage());
            }
            if (StringUtils.isBlank(queryWarningRequest.getGoodsTypeName())) {
                throw new SystemException(ResponseCodes.EmptyGoodsTypeName.getCode(), ResponseCodes.EmptyGoodsTypeName.getMessage());
            }
            map.put("gameName", queryWarningRequest.getGameName());
            map.put("goodsTypeName", queryWarningRequest.getGoodsTypeName());
            //金币的前提默认查询主站配置
            map.put("tradeType", RefererType.goldOrder.getCode());
            List<Warning> warningList = warningManager.queryByMap(map, "ID", true);
            if (warningList != null && warningList.size() == 1) {
                response.setResult(warningList.get(0));
                return response;
            }
            map.remove("gameName");
            warningList = warningManager.queryByMap(map, "ID", true);
            if (warningList != null && warningList.size() == 1) {
                response.setResult(warningList.get(0));
                return response;
            }
            map.put("gameName", queryWarningRequest.getGameName());
            map.remove("goodsTypeName");
            warningList = warningManager.queryByMap(map, "ID", true);
            if (warningList != null && warningList.size() == 1) {
                response.setResult(warningList.get(0));
                return response;
            }

        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("根据游戏名、商品类型查询友情提示发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(
                    ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据游戏名、商品类型查询友情提示发生未知异常:{}", ex);
        }
        logger.info("根据游戏名、商品类型查询友情提示响应信息:{}", response);
        return response;
    }

}
