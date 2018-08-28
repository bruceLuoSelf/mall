package com.wzitech.gamegold.facade.frontend.service.repository;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.order.dto.OuterWebSiteLoginFreeRequest;
import com.wzitech.gamegold.facade.frontend.service.order.dto.QuerySellerOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.CheckRepositoryManageResponse;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.OuterWebSiteReposityDTO;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.RepositoryDTO;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.RepositoryResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;


/**
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/05  wubiao              解密请求路径获取用户账号
 */
public interface INewCheckRepositoryService {
    /**
     * @param firmSecret    厂商秘钥
     * @param appid 授权码
     * @param loginAccount  登录账号
     * @param sign          MD5加密
     * @return
     */
    public CheckRepositoryManageResponse accessToken(OuterWebSiteLoginFreeRequest request,HttpServletRequest httpServletRequest);

    /**
     * 批量上传库存
     *
     * @param jsonData 上传库存json数据
     * @return
     */
    RepositoryResponse uploadRepositoryWithJsonData(OuterWebSiteLoginFreeRequest request);

    /**
     * 分页查询库存
     *
     * @param repositoryDTO 库存信息
     * @param request
     * @return
     */
    RepositoryResponse queryRepository(OuterWebSiteReposityDTO repositoryDTO, HttpServletRequest request);


    /**
     * 分页查询卖家订单数据, 返回少量字段
     *
     * @param params
     * @return
     */
    IServiceResponse queryOrderList(QuerySellerOrderRequest params, HttpServletRequest request);

    /**
     * 查询订单详情
     * @return
     */
    IServiceResponse queryOrderDetail(OuterWebSiteLoginFreeRequest outerWebSiteLoginFreeRequest, HttpServletRequest request);


    /**
     * 移交订单
     * @return
     */
    IServiceResponse transferOrderMethod(OuterWebSiteLoginFreeRequest outerWebSiteLoginFreeRequest,HttpServletRequest request);

}
