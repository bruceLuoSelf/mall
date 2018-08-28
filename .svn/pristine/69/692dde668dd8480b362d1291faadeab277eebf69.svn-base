package com.wzitech.gamegold.facade.frontend.service.shorder;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PayOrderRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import java.math.BigDecimal;

/**
 * 支付单
 */
public interface IPayOrderService {
    /**
     * 分页查询数据
     * @param payOrderRequest
     * @param request
     * @return
     */
    IServiceResponse queryPayOrderList(PayOrderRequest payOrderRequest, HttpServletRequest request);

    /**
     * 新增支付单
     * @param payOrderRequest
     * @param request
     * @return
     */
    IServiceResponse addPayOrder(PayOrderRequest payOrderRequest,HttpServletRequest request);


    /**
     * 提供给7bao的查询详细资金接口
     * @param loginAccount 登录账号
     * @param sign 加密密匙
     * @return
     */
    IServiceResponse queryPurchaseData(String loginAccount,String sign);


    /**
     *
     * @param loginAccount 操作账户
     * @param sign    md5加密
     * @param avaliableFee  需要更新的金额
     * @return
     */
    IServiceResponse updatePurchaseData(String loginAccount, String sign, BigDecimal avaliableFee,String orderId);

    /**
     *
     * @param loginAccount 变动账号
     * @param changeAmount 变动资金
     * @param sign 对应MD5加密
     * @return
     */
    IServiceResponse deletePayOrder(String loginAccount,BigDecimal changeAmount,String sign);

    /**
     * 根据单号查询支付单信息
     *
     * @param orderId
     * @return
     */
     IServiceResponse queryPayOrderById(@QueryParam("orderId") String orderId, @QueryParam("sign") String sign);
}
