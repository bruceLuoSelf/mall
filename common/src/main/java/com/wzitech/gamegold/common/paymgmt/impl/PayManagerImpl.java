package com.wzitech.gamegold.common.paymgmt.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.enums.FundsQueryType;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.paymgmt.IPayManager;
import com.wzitech.gamegold.common.paymgmt.config.*;
import com.wzitech.gamegold.common.paymgmt.dto.*;
import com.wzitech.gamegold.common.utils.PayHelper;
import com.wzitech.gamegold.common.utils.SignHelper;
import com.wzitech.gamegold.common.utils.StreamIOHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class PayManagerImpl implements IPayManager {
    /**
     * 日志记录器
     */
    protected final Logger logger = LoggerFactory.getLogger(PayManagerImpl.class);

    protected final DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 设置连接超时时间
     */
    @Value("${httpclient.connection.timeout}")
    private int connectionTimeout = 30000;

    /**
     * 设置读取超时时间
     */
    @Value("${httpclient.connection.sotimeout}")
    private int SoTimeout = 120000;

    /**
     * 设置最大连接数
     */
    @Value("${httpclient.connection.maxconnection}")
    private int maxConnection = 200;

    /**
     * 设置每个路由最大连接数
     */
    @Value("${httpclient.connection.maxrouteconnection}")
    private int maxRouteConneciton = 50;

    /**
     * 设置接收缓冲
     */
    @Value("${httpclient.connection.socketbuffersize}")
    private int sockeBufferSize = 8192;

    /**
     * 设置失败重试次数
     */
    @Value("${httpclient.connection.maxretry}")
    private int maxRetry = 5;

    /**
     * 下载多线程管理器
     */
    private static PoolingClientConnectionManager connMger;

    /**
     * 下载参数
     */
    private static HttpParams connParams;

    /**
     * 售得用户Id
     */
    @Value("${fund.batch.transfer.get.user.id}")
    private String getUserId = "";

    /**
     * 售得用户名称
     */
    @Value("${fund.batch.transfer.get.user.name}")
    private String getUserName = "";

    @Autowired
    PaymentConfig paymentConfig;

    @Autowired
    BatchTransferConfig batchTransferConfig;

    @Autowired
    RefundConfig refundConfig;

    @Autowired
    QueryConfig queryConfig;

    @Autowired
    CompensateConfig compensateConfig;

    @Autowired
    DirectPayTransferConfig directPayTransferConfig;

    @Autowired
    DirectPartialTransferConfig directPartialTransferConfig;

    @Autowired
    DirectPartialRefundConfig directPartialRefundConfig;

    JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

    @PostConstruct
    private void afterInitialization() {
        // 初始化下载线程池
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(
                new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(
                new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

        connMger = new PoolingClientConnectionManager(schemeRegistry);
        connMger.setDefaultMaxPerRoute(maxRouteConneciton);
        connMger.setMaxTotal(maxConnection);

        // 初始化下载参数
        connParams = new BasicHttpParams();
        connParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeout);
        connParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, SoTimeout);
        connParams.setParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, sockeBufferSize);
    }

    public HttpClient getHttpClient() {
        DefaultHttpClient client = new DefaultHttpClient(connMger, connParams);
        client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(maxRetry, false));
        return client;
    }

    @Override
    public String getPaymentURL(Map<String, String> params) {
        Map<String, String> map = paymentConfig.getBaseParams();
        if (params.size() > 0) {
            map.putAll(params);
        }
        String paymentURL = PayHelper.formatURL(paymentConfig.getUrl(), map,
                paymentConfig.getSignSecretKey(),
                paymentConfig.getInputCharset());
        logger.debug("支付网关生成地址:{}", paymentURL);
        return paymentURL;
    }

    @Override
    public String getMPaymentURL(Map<String, String> params) {
        Map<String, String> map = paymentConfig.getMBaseParams();
        if (params.size() > 0) {
            map.putAll(params);
        }
        String paymentURL = PayHelper.formatMURL(paymentConfig.getMurl(), map,
                paymentConfig.getSignSecretKey(),
                paymentConfig.getInputCharset());
        logger.debug("支付网关生成地址:{}", paymentURL);
        return paymentURL;
    }


    @Override
    public BatchTransferResponse batchTransfer(String orderId, BigDecimal totalPrice, String userId, String userName,
                                               String getUserId, String getUserName, BigDecimal subCommission, ArrayList<TransferUserInfo> transferList) {
        BatchTransferResponse response = new BatchTransferResponse();
        response.setResult(false);
        HttpResponse httpResponse = null;

        Map<String, String> map = batchTransferConfig.getBaseParams();
        map.put("out_trade_no", orderId);
        map.put("total_fee", totalPrice.toString());
        map.put("is_op", "0");
        map.put("op_name", "");
        map.put("user_id", userId);
        map.put("user_name", userName);
        map.put("get_user_id", getUserId);
        map.put("get_user_name", getUserName);
        map.put("sub_commission_fee", subCommission.toString());

        if (null == transferList || transferList.size() == 0) {
            throw new SystemException("转账列表不能为空");
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < transferList.size(); i++) {
            TransferUserInfo transferUserInfo = transferList.get(i);

            if (StringUtils.isEmpty(transferUserInfo.getGetUserId())
                    || StringUtils.isEmpty(transferUserInfo.getGetUserName())
                    || null == transferUserInfo.getTransferFee()) {
                throw new SystemException("转账用户参数不能为空");
            }
            stringBuilder.append(String.format("%s#%s#%s", transferUserInfo.getGetUserId(),
                    transferUserInfo.getGetUserName(), transferUserInfo.getTransferFee().toString()));
            if (i < (transferList.size() - 1)) {
                stringBuilder.append(",");
            }
        }
        map.put("transfer_list", stringBuilder.toString());

        /*String transferURI = PayHelper.formatURL(batchTransferConfig.getUrl(), map, batchTransferConfig.getSignSecretKey(), batchTransferConfig.getInputCharset());
        logger.info("{}，订单号为:{}，资金转账请求：{}",new Object[]{new Date(),orderId,transferURI});
        try {
            httpResponse = getHttpClient().execute(new HttpGet(transferURI));
            String respoString = StreamIOHelper.inputStreamToStr(httpResponse.getEntity()
                    .getContent(), "GB2312");

            logger.info("{}，订单号为:{}，资金转账返回：{}",new Object[]{new Date(),orderId,respoString});
            response = this.jsonMapper.fromJson(respoString, BatchTransferResponse.class);
            if (null != response && StringUtils.isNotEmpty(response.getMessage())) {
                response.setMessage(new String(response.getMessage().getBytes("GB2312"), "UTF-8"));
            }
        } catch (Exception e) {
            logger.error("转账时发生异常:{}", ExceptionUtils.getStackTrace(e));
        }*/

        try {
            HttpPost httpPost = new HttpPost(batchTransferConfig.getUrl());
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Iterator<Map.Entry<String, String>> paramIter = map.entrySet().iterator();
            while (paramIter.hasNext()) {
                Map.Entry<String, String> entry = paramIter.next();
                if (entry.getKey().equals("user_name")) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), PayHelper.urlEncoding(PayHelper.urlEncoding(entry.getValue(), batchTransferConfig.getInputCharset()), batchTransferConfig.getInputCharset())));
                    continue;
                }

                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            String sign = PayHelper.getSignStr(map, batchTransferConfig.getSignSecretKey(),
                    batchTransferConfig.getInputCharset());
            nvps.add(new BasicNameValuePair("sign", sign));

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, batchTransferConfig.getInputCharset()));
            httpResponse = getHttpClient().execute(httpPost);

            String respoString = StreamIOHelper.inputStreamToStr(httpResponse.getEntity()
                    .getContent(), "GB2312");

            logger.info("{}，订单号为:{}，资金转账返回：{}", new Object[]{new Date(), orderId, respoString});
            response = this.jsonMapper.fromJson(respoString, BatchTransferResponse.class);
            if (null != response && StringUtils.isNotEmpty(response.getMessage())) {
                response.setMessage(new String(response.getMessage().getBytes("GB2312"), "UTF-8"));
            }
        } catch (Exception e) {
            logger.error("转账时发生异常:{}", ExceptionUtils.getStackTrace(e));
        }

        return response;
    }

    /**
     * 批量转账
     */
    @Override
    public BatchTransferResponse batchTransfer(String orderId, BigDecimal totalPrice, String userId, String userName,
                                               BigDecimal subCommission, ArrayList<TransferUserInfo> transferList) {
        return batchTransfer(orderId, totalPrice, userId, userName, this.getUserId, this.getUserName, subCommission, transferList);
    }

    @Override
    public QueryDetailResponse queryDetail(String type, String orderId,
                                           String uid) {
        QueryDetailResponse response = new QueryDetailResponse();
        Map<String, String> map = queryConfig.getBaseParams();
        map.put("balance_change_type", type);
        map.put("out_trade_no", orderId);
        map.put("req_user_id", uid);
        map.put("out_bill_id", "");

        String queryDetailURI = PayHelper.formatURL(queryConfig.getUrl(), map, queryConfig.getSignSecretKey(), queryConfig.getInputCharset());

        logger.info("{}，查询订单号为:{}，类型为:{}，资金明细请求：{}", new Object[]{new Date(), orderId,
                FundsQueryType.getTypeByCode(type).getName(), queryDetailURI});

        if (null != queryDetailURI) {
            HttpResponse httpResponse;
            try {
                httpResponse = getHttpClient().execute(new HttpGet(queryDetailURI));
                String respoString = StreamIOHelper.inputStreamToStr(httpResponse.getEntity()
                        .getContent(), "GB2312");
                logger.info("{}，查询订单号为:{}，类型为:{}，资金明细返回：{}", new Object[]{new Date(), orderId,
                        FundsQueryType.getTypeByCode(type).getName(), respoString});

                if (StringUtils.isNotBlank(respoString) && !respoString.startsWith("{")) {
                    logger.error("查询订单号为:{}，类型为:{}，资金明细请求：{}", new Object[]{orderId,
                            FundsQueryType.getTypeByCode(type).getName(), queryDetailURI});
                    logger.error("查询订单号为:{}，类型为:{}，资金明细返回：{}", new Object[]{orderId,
                            FundsQueryType.getTypeByCode(type).getName(), respoString});
                }

                response = this.jsonMapper.fromJson(respoString, QueryDetailResponse.class);
                if (null != response && StringUtils.isNotEmpty(response.getMessage())) {
                    response.setMessage(new String(response.getMessage().getBytes("GB2312"), "UTF-8"));
                }
            } catch (Exception e) {
                response = null;
                logger.error("查询资金明细发生异常:{}", ExceptionUtils.getStackTrace(e));
            }
        }
        return response;
    }


    @Override
    public VaQueryDetailResponse queryWithdrawalsDetail(String type, String orderId, String billId) {
        VaQueryDetailResponse response = new VaQueryDetailResponse();
        Map<String, String> map = queryConfig.getBaseWithdrawalsParams();
        map.put("UserId", "");
        map.put("ChangeType", type);
        map.put("OrderId", orderId);
        map.put("BillId", billId);
        map.put("ExtendId", "");

        String queryDetailURI = PayHelper.formatURL2(queryConfig.getVaUrl(), map, queryConfig.getVaSignSecretKey(), queryConfig.getVaInputCharset());

        logger.info("{}，提现7bao，查询订单号为:{}，类型为:{}，资金明细请求：{}", new Object[]{new Date(), orderId,
                FundsQueryType.getTypeByCode(type).getName(), queryDetailURI});

        if (null != queryDetailURI) {
            HttpResponse httpResponse;
            try {
                httpResponse = getHttpClient().execute(new HttpGet(queryDetailURI));
                String respoString = StreamIOHelper.inputStreamToStr(httpResponse.getEntity()
                        .getContent(), queryConfig.getVaInputCharset());
                logger.info("{}，提现7bao，查询订单号为:{}，类型为:{}，资金明细返回：{}", new Object[]{new Date(), orderId,
                        FundsQueryType.getTypeByCode(type).getName(), respoString});

                if (StringUtils.isNotBlank(respoString) && !respoString.startsWith("{")) {
                    logger.error("提现7bao，查询订单号为:{}，类型为:{}，资金明细请求：{}", new Object[]{orderId,
                            FundsQueryType.getTypeByCode(type).getName(), queryDetailURI});
                    logger.error("提现7bao，查询订单号为:{}，类型为:{}，资金明细返回：{}", new Object[]{orderId,
                            FundsQueryType.getTypeByCode(type).getName(), respoString});
                }

                response = this.jsonMapper.fromJson(respoString, VaQueryDetailResponse.class);
                if (null != response && StringUtils.isNotEmpty(response.getMessage())) {
                    response.setMessage(new String(response.getMessage().getBytes(queryConfig.getVaInputCharset()), queryConfig.getVaInputCharset()));
                }
            } catch (Exception e) {
                response = null;
                logger.error("提现7bao，查询资金明细发生异常:{}", ExceptionUtils.getStackTrace(e));
            }
        }
        return response;
    }

    //	@Override
//	public BatchTransferResponse transfer(Map<String, String> params) {
//		IPayResponse response = null;
//		Map<String, String> map = transferConfig.getBaseParams();
//
//		if (params.size() > 0) {
//			map.putAll(params);
//		}
//		String transferURI = FundsUtils.formatURL(transferConfig.getUrl(), map, transferConfig.getSignSecretKey(), transferConfig.getInputCharset());
//
//		if (null != transferURI) {
//			HttpResponse httpResponse;
//			PayExcepation payExcepation = null;
//			try {
//				httpResponse = httpClient.execute(new HttpGet(transferURI));
//				String respoString = IOUtils.toString(httpResponse.getEntity()
//						.getContent());
//				PayErrorType errorType = PayErrorType.getTypeByCode(respoString);
//				if (null != errorType) {
//					payExcepation = new PayExcepation(errorType.getCode(), errorType.getValue());
//				} else {
//					TransferResponse transferResponse = this.jsonMapper.fromJson(respoString, TransferResponse.class);
//					//由于Message为gb2312格式字符串，需要转换成UTF-8
//					String msg = transferResponse.getMessage();
//					if (!StringUtils.isEmpty(msg)) {
//						transferResponse.setMessage(new String(msg.getBytes("gb2312"), "UTF-8"));
//					}
//					response = transferResponse;
//				}
//			} catch (ClientProtocolException e) {
//				payExcepation = new PayExcepation(PayErrorType.NetWork_Error.getCode(), PayErrorType.NetWork_Error.getValue()+":"+e.getMessage());
//				e.printStackTrace();
//			} catch (IOException e) {
//				payExcepation = new PayExcepation(PayErrorType.Unknown_Response_Data.getCode(), PayErrorType.Unknown_Response_Data.getValue()+":"+e.getMessage());
//				e.printStackTrace();
//			}
//			if (null != payExcepation) {
//				throw payExcepation;
//			}
//		}
//		return response;
//		return null;
//	}
//
//
//
    @Override
    public RefundResponse refund(String orderId, String buyerId, String buyerName,
                                 BigDecimal totalPrice) {
        RefundResponse response = new RefundResponse();
        response.setResult(false);
        Map<String, String> map = refundConfig.getBaseParams();
        map.put("buyer_id", buyerId);
        map.put("buyer_name", buyerName);
        map.put("out_trade_no", orderId);
        map.put("is_op", "0");
        map.put("op_name", "");
        map.put("total_fee", totalPrice.toString());

        HttpResponse httpResponse;
        String refundURI = PayHelper.formatURL(refundConfig.getUrl(), map, refundConfig.getSignSecretKey(), refundConfig.getInputCharset());
        logger.info("{}，订单号为:{}，资金退款请求：{}", new Object[]{new Date(), orderId, refundURI});
        try {
            httpResponse = getHttpClient().execute(new HttpGet(refundURI));
            String respoString = StreamIOHelper.inputStreamToStr(httpResponse.getEntity()
                    .getContent(), "GB2312");
            logger.info("{}，订单号为:{}，资金退款返回：{}", new Object[]{new Date(), orderId, respoString});
            response = this.jsonMapper.fromJson(respoString, RefundResponse.class);
            if (null != response && StringUtils.isNotEmpty(response.getMessage())) {
                response.setMessage(new String(response.getMessage().getBytes("GB2312"), "UTF-8"));
            }
        } catch (Exception e) {
            logger.error("退款时发生异常:{}", ExceptionUtils.getStackTrace(e));
        }

        return response;
    }

    /* (non-Javadoc)
     * @see com.wzitech.gamegold.common.paymgmt.IPayManager#compensate(java.lang.String, java.math.BigDecimal, java.lang.String, java.lang.String)
     */
    @Override
    public CompensateResponse compensate(String orderId, BigDecimal totalPrice,
                                         String userName, String uid) {
        CompensateResponse response = new CompensateResponse();
        response.setResult(false);
        Map<String, String> map = compensateConfig.getBaseParams();
        map.put("out_trade_no", orderId);
        map.put("total_fee", totalPrice.toString());
        map.put("balance_user_name", userName);
        map.put("req_user_id", uid);
        map.put("is_op", "0");
        map.put("op_name", "");

        HttpResponse httpResponse;
        String refundURI = PayHelper.formatURL(compensateConfig.getUrl(), map, compensateConfig.getSignSecretKey(), compensateConfig.getInputCharset());

        try {
            httpResponse = getHttpClient().execute(new HttpGet(refundURI));
            String respoString = StreamIOHelper.inputStreamToStr(httpResponse.getEntity()
                    .getContent(), "GB2312");
            response = this.jsonMapper.fromJson(respoString, CompensateResponse.class);
            if (null != response && StringUtils.isNotEmpty(response.getMessage())) {
                response.setMessage(new String(response.getMessage().getBytes("GB2312"), "UTF-8"));
            }
        } catch (Exception e) {
            logger.error("赔付时发生异常:{}", ExceptionUtils.getStackTrace(e));
        }

        return response;
    }

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
    public DirectPayTransferResponse directPayTransfer(String id, String orderId, String sellerId, String sellerName,
                                                       BigDecimal totalFee, Date createTime, String retrospectOrderId) {
        DirectPayTransferResponse response = new DirectPayTransferResponse();
        response.setResult(false);

        Map<String, String> map = directPayTransferConfig.getBaseParams();
        map.put("out_trade_no", orderId);
        map.put("seller_id", sellerId);
        map.put("seller_name", sellerName);
        map.put("out_bill_id", id);
        map.put("total_fee", totalFee.toString());
        map.put("order_createdate", DATA_FORMAT.format(createTime));
        map.put("create_trade_no_ip", "127.0.0.1");
        map.put("retrospect_trade_no", retrospectOrderId);

        String transferURI = PayHelper.formatURL(directPayTransferConfig.getUrl(), map,
                directPayTransferConfig.getSignSecretKey(), directPayTransferConfig.getInputCharset());

        logger.info("{}，订单号为:{}，资金付款请求：{}", new Object[]{new Date(), orderId, transferURI});

        HttpResponse httpResponse = null;
        try {
            httpResponse = getHttpClient().execute(new HttpGet(transferURI));
            String respoString = StreamIOHelper.inputStreamToStr(httpResponse.getEntity().getContent(), "GB2312");

            logger.info("{}，订单号为:{}，资金付款返回：{}", new Object[]{new Date(), orderId, respoString});

            response = this.jsonMapper.fromJson(respoString, DirectPayTransferResponse.class);
            if (null != response && StringUtils.isNotEmpty(response.getMessage())) {
                response.setMessage(new String(response.getMessage().getBytes("GB2312"), "UTF-8"));
            }
        } catch (Exception e) {
            logger.error("付款时发生异常:{}", ExceptionUtils.getStackTrace(e));
        }

        return response;
    }

    /**
     * 部分转账
     *
     * @param orderId    支付订单号
     * @param buyerId    转账用户ID
     * @param buyerName  转账用户名
     * @param sellerId   获得转账的用户ID
     * @param sellerName 获得转账的用户名
     * @param totalFee   转账总金额
     * @param feeDetails 子费用条目
     * @return
     */
    public DirectPartialTransferResponse directPartialTransfer(String orderId, String buyerId, String buyerName,
                                                               String sellerId, String sellerName,
                                                               BigDecimal totalFee, String feeDetails) {
        DirectPartialTransferResponse response = new DirectPartialTransferResponse();
        response.setResult(false);

        Map<String, String> map = directPartialTransferConfig.getBaseParams();
        map.put("out_trade_no", orderId);
        map.put("buyer_id", buyerId);
        map.put("buyer_name", buyerName);
        map.put("seller_id", sellerId);
        map.put("seller_name", sellerName);
        map.put("request_ip", "127.0.0.1");
        map.put("total_fee", totalFee.toString());
        map.put("fee_details", feeDetails);

        String transferURI = PayHelper.formatURL(directPartialTransferConfig.getUrl(), map,
                directPartialTransferConfig.getSignSecretKey(), directPartialTransferConfig.getInputCharset());

        logger.info("{}，订单号为:{}，资金部分付款请求：{}", new Object[]{new Date(), orderId, transferURI});

        HttpResponse httpResponse = null;
        try {
            httpResponse = getHttpClient().execute(new HttpGet(transferURI));
            String respoString = StreamIOHelper.inputStreamToStr(httpResponse.getEntity().getContent(), "GB2312");

            logger.info("{}，订单号为:{}，资金部分付款返回：{}", new Object[]{new Date(), orderId, respoString});

            response = this.jsonMapper.fromJson(respoString, DirectPartialTransferResponse.class);
            if (null != response && StringUtils.isNotEmpty(response.getMessage())) {
                response.setMessage(new String(response.getMessage().getBytes("GB2312"), "UTF-8"));
            }
        } catch (Exception e) {
            logger.error("付款时发生异常:{}", ExceptionUtils.getStackTrace(e));
        }

        return response;
    }

    /**
     * 部分退款
     *
     * @param orderId    支付订单号
     * @param buyerId    获得退款的用户ID
     * @param buyerName  获得退款的用户名
     * @param totalFee   退款总金额
     * @param feeDetails 部分退款条目
     * @return
     */
    @Override
    public RefundResponse directPartialRefund(String orderId, String buyerId, String buyerName, BigDecimal totalFee,
                                              String feeDetails) {
        RefundResponse response = new RefundResponse();
        response.setResult(false);

        Map<String, String> map = directPartialRefundConfig.getBaseParams();
        map.put("out_trade_no", orderId);
        map.put("buyer_id", buyerId);
        map.put("buyer_name", buyerName);
        map.put("request_ip", "127.0.0.1");
        map.put("total_fee", totalFee.toString());
        map.put("fee_details", feeDetails);

        String transferURI = PayHelper.formatURL(directPartialRefundConfig.getUrl(), map,
                directPartialRefundConfig.getSignSecretKey(), directPartialRefundConfig.getInputCharset());

        logger.info("{}，订单号为:{}，资金部分退款请求：{}", new Object[]{new Date(), orderId, transferURI});

        HttpResponse httpResponse = null;
        try {
            httpResponse = getHttpClient().execute(new HttpGet(transferURI));
            String respoString = StreamIOHelper.inputStreamToStr(httpResponse.getEntity().getContent(), "GB2312");

            logger.info("{}，订单号为:{}，资金部分退款返回：{}", new Object[]{new Date(), orderId, respoString});

            response = this.jsonMapper.fromJson(respoString, RefundResponse.class);
            if (null != response && StringUtils.isNotEmpty(response.getMessage())) {
                response.setMessage(new String(response.getMessage().getBytes("GB2312"), "UTF-8"));
            }
        } catch (Exception e) {
            logger.error("部分退款时发生异常:{}", ExceptionUtils.getStackTrace(e));
        }

        return response;
    }
}