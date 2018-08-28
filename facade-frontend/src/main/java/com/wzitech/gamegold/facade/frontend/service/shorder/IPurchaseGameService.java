package com.wzitech.gamegold.facade.frontend.service.shorder;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseGameRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

/**
 * 收货商游戏配置数据
 */
public interface IPurchaseGameService {

    /**
     * 查询所有符合条件的游戏
     */
    IServiceResponse getPurchaseGameConfig();
    /**
     * 新增收货商游戏配置数据
     */
    IServiceResponse createPurchaseGame(PurchaseGameRequest purchaseGameRequest,
                                        @Context HttpServletRequest request);

    /**
     * 修改收货商游戏配置数据
     */
    IServiceResponse updatePurchaseGame(@QueryParam("id")String id,
                                        @QueryParam("deliveryTypeId")String deliveryTypeId,
                                        @QueryParam("tradeTypeId")String tradeTypeId);

    /**
     * 删除收货商游戏配置数据
     */
    IServiceResponse deletePurchaseGame(@QueryParam("id")String id );
}
