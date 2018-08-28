package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.httpClientPool.HttpClientPool;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.common.utils.MainStationUtil;
import com.wzitech.gamegold.common.utils.StreamIOHelper;
import com.wzitech.gamegold.shorder.business.IAmoutHttp;
import com.wzitech.gamegold.shorder.dto.RequestZBaoDTO;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.utils.HttpToConn;
import com.wzitech.gamegold.shorder.utils.ZBaoPayDetailResponse;
import com.wzitech.gamegold.shorder.utils.ZbaoPayDetail;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by chengXY on 2017/8/21.
 * 调用Http请求主站和调用http请求7BAO
 */
@Component
public class AmoutHttpImpl implements IAmoutHttp {
    protected static final Logger logger = LoggerFactory.getLogger(AmoutHttpImpl.class);

    @Autowired
    private HttpClientPool httpClientPool;
    //访问的服务地址 测试机需要host 7Bao.tomain.conToMain.URl
    @Value("${7Bao.tomain.conToMain.url}")
    private String url = "";
    //接入方的部点ID
    @Value("${7Bao.tomain.conToMain.siteId}")
    private String createInternalSiteId = "";
    //异步通知的回调url
    @Value("${7Bao.tomain.conToMain.notifyUrl}")
    private String notifyUrl = "";
    //加密秘钥
    @Value("${7Bao.tomain.conToMain.serKey}")
    private String serKey = "";
    //测试方法
    @Value("${7Bao.tomain.conToMain.service}")
    private String service = "";
    //使用已经开通了白名单的ip 上线的服务器需要开通
    @Value("${7Bao.tomain.conToMain.clientIp}")
    private String clientIp = "192.168.42.29";
    //测试参数
    private String signType = "MD5";
    //编码格式
    private String inputCharset = "gb2312";

    //版本
    private String version = "1.0";
    //开发语言
    private String platformLang = "Java";
    //交易类型 6: 商场
    private String tradingType = "6";
    private String opName = "";
    private String isOp = "0";
    //部门名称
    private String depName = "金币商城";

    @Value("${7Bao.fund.recharge}")
    private String rechargeKey;  //4Uj3V9%

    @Value("${7Bao.update.reduceUrl}")
    private String reduceUrl;

    @Value("${7Bao.update.mulitPayment}")
    private String mulitPayment;

    @Value("${7Bao.update.addUrl}")
    private String addUrl;

    @Value("${7Bao.update.bigAccountUserId}")
    private String bigAccountUserId;

    @Value("${7Bao.update.bigAccountUserName}")
    private String bigAccountUserName;

    @Value("${7Bao.update.queryFundDetailUrl}")
    private String queryFundDetailUrl;

//    private String bigAccountUserId = "US12072359508001-00D3";
//
//    private String bigAccountUserName = "supmj6";

    public String getTransferist(DeliveryOrder deliveryOrder, String totalFee) {
        //US20019088#test1#10.00,US20139878#test2#23.20
        return deliveryOrder.getSellerUid() + "#" + deliveryOrder.getSellerAccount() + "#" + totalFee;
    }

    /**
     * 请求主站
     */
    public Boolean conToMain(DeliveryOrder deliveryOrder, BigDecimal totalFee, int orderSubfix) {
        CloseableHttpResponse execute = null;
        Map<String, String> map = new TreeMap<String, String>();
        try {
            map.put("service", service);
            map.put("input_charset", inputCharset);
            map.put("sign_type", signType);
            map.put("client_ip", clientIp);
            map.put("version", version);
            if (orderSubfix > 0) {
                map.put("out_trade_no", deliveryOrder.getOrderId() + "_" + orderSubfix);
            } else {
                map.put("out_trade_no", deliveryOrder.getOrderId());
            }
            map.put("platform_lang", platformLang);
            map.put("total_fee", totalFee.toString());
            map.put("trading_type", tradingType);
            map.put("user_id", bigAccountUserId);
            map.put("user_name", bigAccountUserName);
            map.put("transfer_list", getTransferist(deliveryOrder, totalFee.toString()));
            map.put("is_op", isOp);
            map.put("op_name", opName);
            map.put("dep_name", depName);
            map.put("create_internal_site_id", createInternalSiteId);
            InetAddress ia = null;
            ia = InetAddress.getLocalHost();

            String clientIP = ia.getHostAddress();
            map.put("request_ip", clientIP/*"192.168.42.196"*/);
            map.put("notify_url", notifyUrl);
            String md5Str = MainStationUtil.keyValueJoin(null, map, "&", false, "");
            String md5 = "&sign=" + EncryptHelper.md5(md5Str + serKey, inputCharset);
            String getUrl = String.format("%s%s%s", url, MainStationUtil.keyValueJoin(null, map, "&", true, inputCharset), md5);
            CloseableHttpClient httpClient = httpClientPool.getHttpClient();
            logger.info("调用主站接口url：{}", getUrl);
            HttpGet method = new HttpGet(getUrl);
            HttpClientPool.config(method);
            execute = httpClient.execute(method);
            String res = StreamIOHelper.inputStreamToStr(execute.getEntity().getContent(), "utf-8");
            logger.info("调用主站接口返回接口参数：{}", res);
            //判断请求结果是否是true响应成功
            if (JSONObject.fromObject(res).getBoolean("Result") == true) return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (execute != null) {
                try {
                    execute.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 请求7bao给出货单买家减款
     */
    public Boolean conToZBao(DeliveryOrder deliveryOrder, BigDecimal bigDecimal, int orderSubfix) throws IOException {
        RequestZBaoDTO requestZBaoDTO = new RequestZBaoDTO();
        String sign = "";
        requestZBaoDTO.setUserId(deliveryOrder.getBuyerUid());
        if (orderSubfix > 0) {
            requestZBaoDTO.setOrderId(deliveryOrder.getOrderId() + "_" + orderSubfix);
        } else {
            requestZBaoDTO.setOrderId(deliveryOrder.getOrderId());
        }
        requestZBaoDTO.setOrderMoney(deliveryOrder.getAmount());
        requestZBaoDTO.setRealMoney(bigDecimal);
        requestZBaoDTO.setRemark("0");

        try {
            String format = String.format("%s_%s_%s_%s_%s", rechargeKey, requestZBaoDTO.getUserId(), requestZBaoDTO.getOrderId(),
                    requestZBaoDTO.getRealMoney(), requestZBaoDTO.getRemark());
            logger.info("请求7bao减款签名{}：", format);
            sign = EncryptHelper.md5(format);
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestZBaoDTO.setSign(sign);


        String params = "sign=" + sign + "&" + "userId=" + requestZBaoDTO.getUserId() + "&" + "orderId=" + requestZBaoDTO.getOrderId() + "&" +
                "money=" + requestZBaoDTO.getRealMoney() + "&" + "remark=" + requestZBaoDTO.getRemark();


        //调用http请求7bao
        HttpToConn httpToZBao = new HttpToConn();
        JSONObject jsonParam = JSONObject.fromObject(requestZBaoDTO);
        //调用 cxymark00
        httpToZBao.httpPostReduce(reduceUrl, params);
        return true;
    }

    /**
     * 请求7bao给出货单买家减款
     */
    @Override
    public Boolean mulitCreatTransferDeduction(String uid, String transferList, String loginAccount, BigDecimal amount, int yesOrNo, String orderId, BigDecimal totalAmount, BigDecimal availableAmount, BigDecimal freezeAmount) throws IOException {
        RequestZBaoDTO requestZBaoDTO = new RequestZBaoDTO();
        String sign = "";
        requestZBaoDTO.setUserId(uid);
        requestZBaoDTO.setTransferList(transferList);

        String format = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", rechargeKey, requestZBaoDTO.getUserId(), requestZBaoDTO.getTransferList(), loginAccount, amount, yesOrNo, orderId, totalAmount, availableAmount, freezeAmount);
        logger.info("请求7bao批量减款签名{}：", format);
        sign = EncryptHelper.md5(format);
        requestZBaoDTO.setSign(sign);

        String params = "sign=" + sign + "&userId=" + requestZBaoDTO.getUserId() + "&transferList=" + transferList + "&loginAccount=" + loginAccount + "&freezeFund=" + amount + "&yesOrNo=" + yesOrNo + "&orderId=" + orderId + "&totalAmount=" + totalAmount + "&availableAmount=" + availableAmount + "&freezeAmount=" + freezeAmount;

        //调用http请求7bao
        HttpToConn httpToZBao = new HttpToConn();
        //调用 cxymark00
        httpToZBao.httpPostReduce(mulitPayment, params);
        return true;
    }

    /**
     * 查询资金明细
     *
     * @param orderId
     * @param loginAccount
     * @param amount
     * @return
     * @throws IOException
     */
    @Override
    public boolean queryFundDetail(String orderId, String loginAccount, BigDecimal amount) throws IOException {
        String format = String.format("%s_%s", rechargeKey, orderId);
        logger.info("请求7bao查询资金明细签名：{}", format);
        String sign = EncryptHelper.md5(format);
        String params = "sign=" + sign + "&orderId=" + orderId;
        //调用http请求7bao
        HttpToConn httpToZBao = new HttpToConn();
        //调用 cxymark00
        String response = httpToZBao.sendHttpPost(queryFundDetailUrl, params);
        JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
        ZBaoPayDetailResponse payDetailResponse = jsonMapper.fromJson(response, ZBaoPayDetailResponse.class);
        logger.info("payDetailResponse,{}", JsonMapper.nonDefaultMapper().toJson(payDetailResponse));
        if (payDetailResponse == null) {
            return false;
        }
        ResponseStatus responseStatus = payDetailResponse.getResponseStatus();
        ZbaoPayDetail payDetail = payDetailResponse.getPayDetail();
        if (responseStatus == null || !responseStatus.getCode().equals(ResponseCodes.Success.getCode())) {
            return false;
        }
        if (payDetail == null || !orderId.equals(payDetail.getOrderId()) || !loginAccount.equals(payDetail.getLoginAccount()) ||
                payDetail.getAmount() == null || payDetail.getAmount().compareTo(amount) != 0) {
            return false;
        }
        return true;
    }

}
