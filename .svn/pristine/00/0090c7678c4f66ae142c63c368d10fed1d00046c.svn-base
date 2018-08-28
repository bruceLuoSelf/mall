package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.DeliveryTypeEnum;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.dto.BaseResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.IPurchaseGameService;
import com.wzitech.gamegold.facade.frontend.service.shorder.ISellerData;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseGameRequest;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseGameResponse;
import com.wzitech.gamegold.shorder.business.IPurchaseGameManager;
import com.wzitech.gamegold.shorder.business.IPurchaserGameTradeManager;
import com.wzitech.gamegold.shorder.business.ITradeManager;
import com.wzitech.gamegold.shorder.dao.IPurchaseGameDao;
import com.wzitech.gamegold.shorder.dao.IShGameConfigDao;
import com.wzitech.gamegold.shorder.dto.GameTradeConfigDTO;
import com.wzitech.gamegold.shorder.entity.PurchaseGame;
import com.wzitech.gamegold.shorder.entity.PurchaserGameTrade;
import com.wzitech.gamegold.shorder.entity.Trade;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 收货商游戏配置service
 */
@Service("PurchaseGameService")
@Path("purchaseGame")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class PurchaseGameServiceImpl extends AbstractBaseService implements IPurchaseGameService {

    @Autowired
    private IPurchaseGameManager purchaseGameManager;

    @Autowired
    private ISellerData sellerData;

    @Autowired
    private IPurchaserGameTradeManager purchaserGameTradeManager;

    /**
     * 根据收货商账号名获取所有的游戏属性配置
     */
    @Override
    @GET
    @Path("getPurchaseGameConfig")
    public IServiceResponse getPurchaseGameConfig() {
        PurchaseGameResponse response = new PurchaseGameResponse();
         ResponseStatus responseStatus = new ResponseStatus();
         response.setResponseStatus(responseStatus);
        try {
            //当前账号名就是收货商账户名
            String purchaseAccount = CurrentUserContext.getUserLoginAccount();
            if (null == purchaseAccount) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }
            //检查卖家信息
            if (!sellerData.checkSellerForRecharge(response)) {
                return response;
            }
            List<PurchaseGame> list = purchaseGameManager.getGameTradeConfig(purchaseAccount);
            response.setPurchaseGameList(list);
            responseStatus.setStatus(ResponseCodes.Success.getCode(),
                    ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            responseStatus.setStatus(ex.getErrorCode(), ex.getErrorMsg());
            logger.error("获取游戏属性配置发生异常：{}",ex);
        } catch (Exception e) {
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(),
                    ResponseCodes.UnKnownError.getMessage());
            logger.error("获取游戏属性配置发生未知异常",e);
        }
        return response;
    }

    /**
     * 新增收货商游戏配置
     * @param purchaseGameRequest
     * @param request
     * @return
     */
    @Override
    @GET
    @Path("createPurchaseGame")
    public IServiceResponse createPurchaseGame(@QueryParam("")PurchaseGameRequest purchaseGameRequest, @Context HttpServletRequest request) {
        PurchaseGameResponse response = new PurchaseGameResponse();
        ResponseStatus status = new ResponseStatus();
        response.setResponseStatus(status);
        try{
            //获取当前登录用户
            UserInfoEO user = (UserInfoEO) CurrentUserContext.getUser();
            if(user == null){
                status.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
            }
            PurchaseGame purchaseGame = new PurchaseGame();
            purchaseGame.setPurchaseAccount(user.getLoginAccount());
            purchaseGame.setGameName(purchaseGameRequest.getGameName());
            purchaseGame.setDeliveryTypeId(purchaseGameRequest.getDeliveryTypeId());
            purchaseGame.setGoodsTypeId(purchaseGameRequest.getGoodsTypeId());
            purchaseGame.setTradeTypeId(purchaseGameRequest.getTradeTypeId());
            purchaseGameManager.addPurchaseGame(purchaseGame);
            status.setStatus(ResponseCodes.Success.getCode(),ResponseCodes.Success.getMessage());
        }catch (SystemException e) {
            status.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("新增收货商游戏配置发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            status.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("新增收货商游戏配置发生未知异常:{}", e);
        }
        return response;
    }

    /**
     * 修改收货商游戏配置
     * @param id
     * @param deliveryTypeId
     * @param tradeTypeId
     * @return
     */
    @Override
    @GET
    @Path("updatePurchaseGame")
    public IServiceResponse updatePurchaseGame(@QueryParam("id")String id,
                                                @QueryParam("deliveryTypeId")String deliveryTypeId,
                                                @QueryParam("tradeTypeId")String tradeTypeId) {
        PurchaseGameResponse response = new PurchaseGameResponse();
        ResponseStatus status = new ResponseStatus();
        response.setResponseStatus(status);
        try{
            Map<String,String> map = new HashMap<String, String>();
            map.put("id",id);
            map.put("deliveryTypeId",deliveryTypeId);
            map.put("tradeTypeId",tradeTypeId);
            purchaseGameManager.updatePurchaseGame(map);
            status.setStatus(ResponseCodes.Success.getCode(),ResponseCodes.Success.getMessage());
        }catch (SystemException e) {
            status.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取采购单发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            status.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取采购单发生未知异常:{}", e);
        }
        return response;
    }

    /**
     * 删除收货商游戏配置
     * @param id
     * @return
     */
    @Override
    @GET
    @Path("deletePurchaseGame")
    public IServiceResponse deletePurchaseGame(@QueryParam("id")String id) {
        PurchaseGameResponse response = new PurchaseGameResponse();
        ResponseStatus status = new ResponseStatus();
        response.setResponseStatus(status);
        try{
            if(StringUtils.isNotBlank(id)){
                purchaseGameManager.deletePurchaseGame(new Long(id));
                status.setStatus(ResponseCodes.Success.getCode(),ResponseCodes.Success.getMessage());
            }
        }catch (SystemException e) {
            status.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取采购单发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            status.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取采购单发生未知异常:{}", e);
        }
        return response;
    }
}
