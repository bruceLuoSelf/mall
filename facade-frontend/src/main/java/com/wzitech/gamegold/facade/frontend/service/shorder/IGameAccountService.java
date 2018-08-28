package com.wzitech.gamegold.facade.frontend.service.shorder;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseOrderResponse;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.io.InputStream;

/**
 * 收货角色信息
 */
public interface IGameAccountService {
    PurchaseOrderResponse upload(@Multipart("excelFile") InputStream in,
                                 @Context HttpServletRequest request);

    /**
     * 上传手工收货库存excel文件
     *
     * @param in
     * @param request
     * @return
     */
     PurchaseOrderResponse uploadManual(@Multipart("excelFile") InputStream in,
                                              @Context HttpServletRequest request);

    IServiceResponse queryGameAccountList(@QueryParam("")PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request);

    /**
     * 分页查询账号库存
     * @param purchaseOrderRequest
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    IServiceResponse queryRepositoryGameAccountList(@QueryParam("")PurchaseOrderRequest purchaseOrderRequest,  @QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize, @Context HttpServletRequest request);


    /**
     * 导出数据
     * @param purchaseOrderRequest
     * @param request
     * @return
     */
    void exportOrder(@QueryParam("") PurchaseOrderRequest purchaseOrderRequest,
                     @Context HttpServletRequest request,
                     @Context HttpServletResponse servletRespone);
    /**
     * 修改密码
     */
    IServiceResponse updateGamePwd( PurchaseOrderRequest purchaseOrderRequest,@Context HttpServletRequest request);
    /**
     *  删除
     */
    IServiceResponse deleteGameAccount( PurchaseOrderRequest purchaseOrderRequest,@Context HttpServletRequest request);
}
