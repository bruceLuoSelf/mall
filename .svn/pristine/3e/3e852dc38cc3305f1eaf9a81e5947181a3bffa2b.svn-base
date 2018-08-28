package com.wzitech.gamegold.common.paymgmt;

import com.wzitech.gamegold.common.paymgmt.dto.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public interface IPayManager {
    /**
     * 生成支付url
     * params值：所有键值对String类型
     * buyer_id：买家id
     * buyer_name:买家登录名
     * out_trade_no:订单编号
     * order_createdate:订单创建时间
     * game_id:支付物品id
     * is_op:是否为操作员
     * op_name:操作员登录名
     * total_fee:支付总金额
     * goods_title:商品标题
     * price：单价（可选参数）
     * quantity：总数（可选参数）
     * game_account:游戏账号（值可以为空）
     *
     * @param params
     * @return String
     * @throws PayExcepation
     */
    public String getPaymentURL(Map<String, String> params);

    /**
     * 批量p2p转账接口
     *
     * @param orderId       订单Id
     * @param totalPrice    转账总金额
     * @param userId        执行支付用户ID
     * @param userName      执行支付用户注册账户
     * @param getUserId     支付完成售的用户ID
     * @param getUserName   支付完成售的用户注册账户
     * @param subCommission 扣除的佣金
     * @param transferList  获得P2P转账的数据列表
     * @return
     */
    public BatchTransferResponse batchTransfer(String orderId, BigDecimal totalPrice, String userId, String userName,
                                               String getUserId, String getUserName, BigDecimal subCommission, ArrayList<TransferUserInfo> transferList);

    /**
     * 批量p2p转账接口
     *
     * @param orderId       订单Id
     * @param totalPrice    转账总金额
     * @param userId        执行支付用户ID
     * @param userName      执行支付用户注册账户
     * @param subCommission 扣除的佣金
     * @param transferList  获得P2P转账的数据列表
     * @return
     */
    public BatchTransferResponse batchTransfer(String orderId, BigDecimal totalPrice, String userId, String userName,
                                               BigDecimal subCommission, ArrayList<TransferUserInfo> transferList);

//	/**
//	 * 转账接口
//	 * params值：所有键值对String类型
//	 * request_ip：用户ip
//	 * out_trade_no：订单号
//	 * buyer_id：买家id
//	 * buyer_name:买家登录名
//	 * seller_id:卖家id
//	 * seller_name:卖家登录名
//	 * is_op:是否为操作员调用次接口(0:非操作员 1：操作员)操作员为客服
//	 * op_name:操作员登录名(此值可以为空串)
//	 * total_fee：转账总金额（104.5）
//	 * price：商品单价（可选字段，可以不传）
//	 * quantity：商品数量（可选字段，可以不传）
//	 * @param params
//	 * @return TransferResponse
//	 * @throws PayExcepation
//	 */
//	public BatchTransferResponse transfer(Map<String, String> params);
//	

    /**
     * 退款接口
     * params值：所有键值对String类型
     * request_ip：用户ip
     * out_trade_no：订单号
     * buyer_id：退款用户id
     * buyer_name:退款用户登录名
     * is_op:是否为操作员调用次接口(0:非操作员 1：操作员)操作员为客服
     * op_name:操作员登录名(此值可以为空串)
     * total_fee：转账总金额（104.5）
     * price：商品单价（可选字段，可以不传）
     * quantity：商品数量（可选字段，可以不传）
     *
     * @param params
     * @return RefundResponse
     * @throws PayExcepation
     */
    public RefundResponse refund(String orderId, String buyerId, String buyerName,
                                 BigDecimal totalPrice);

    /**
     * 查询资金明细接口
     *
     * @param type    资金类型（13：外部支付；14：外部退款；3：转账）
     * @param orderId 订单号
     * @param uid     用户Id
     * @return
     */
    public QueryDetailResponse queryDetail(String type, String orderId, String uid);

    /**
     * 赔付资金接口
     *
     * @param orderId    订单号
     * @param totalPrice 支付金额
     * @param userName   所得赔付用户登录账号
     * @param uid        所得赔付用户uid
     * @return
     */
    public CompensateResponse compensate(String orderId, BigDecimal totalPrice, String userName, String uid);

    /**
     * 公共资金服务通用付款接口
     *
     * @param id                ID
     * @param orderId           订单号
     * @param sellerId          收款方ID
     * @param sellerName        收款方用户名
     * @param totalFee          付款总金额
     * @param createTime        订单创建时间
     * @param retrospectOrderId 原退可追溯单号，支付时的业务订单号
     * @return
     */
    DirectPayTransferResponse directPayTransfer(String id, String orderId, String sellerId, String sellerName,
                                                BigDecimal totalFee, Date createTime, String retrospectOrderId);

    /**
     * 部分转账
     *
     * @param orderId    订单号
     * @param buyerId    转账用户ID
     * @param buyerName  转账用户名
     * @param sellerId   获得转账的用户ID
     * @param sellerName 获得转账的用户名
     * @param totalFee   转账总金额
     * @param feeDetails 子费用条目
     * @return
     */
    DirectPartialTransferResponse directPartialTransfer(String orderId, String buyerId, String buyerName,
                                                        String sellerId, String sellerName,
                                                        BigDecimal totalFee, String feeDetails);

    /**
     * 部分退款
     *
     * @param orderId    订单号
     * @param buyerId    获得退款的用户ID
     * @param buyerName  获得退款的用户名
     * @param totalFee   退款总金额
     * @param feeDetails 部分退款条目
     * @return
     */
    RefundResponse directPartialRefund(String orderId, String buyerId, String buyerName,
                                       BigDecimal totalFee, String feeDetails);

    /**
     * 获取m站的支付接口
     *
     * @param params
     * @return
     */
    public String getMPaymentURL(Map<String, String> params);

    VaQueryDetailResponse queryWithdrawalsDetail(String type, String orderId, String billId);
}