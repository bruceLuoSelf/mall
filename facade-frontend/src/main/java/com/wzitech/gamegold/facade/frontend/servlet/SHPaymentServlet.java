package com.wzitech.gamegold.facade.frontend.servlet;

import com.wzitech.chaos.framework.server.common.CommonServiceResponse;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.paymgmt.IPayManager;
import com.wzitech.gamegold.common.paymgmt.config.PaymentConfig;
import com.wzitech.gamegold.shorder.business.IPayOrderManager;
import com.wzitech.gamegold.shorder.entity.PayOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 收货系统付款
 */
public class SHPaymentServlet extends HttpServlet {
    /**
     * 日志记录器
     */
    protected final Logger logger = LoggerFactory.getLogger(PaymentServlet.class);

    protected static final DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private PaymentConfig paymentConfig;

    private IPayManager payManager;

    private IPayOrderManager payOrderManager;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.paymentConfig = (PaymentConfig) context.getBean("paymentConfig");
        this.payManager = (IPayManager) context.getBean("payManagerImpl");
        this.payOrderManager = (IPayOrderManager) context.getBean("payOrderManagerImpl");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service(req, resp);
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String paymentURL = null;
        CommonServiceResponse commonServiceResponse = null;
        try {
            String orderId = req.getParameter("orderId");
            if (StringUtils.isBlank(orderId)) return;

            PayOrder order = payOrderManager.queryByOrderId(orderId, false);
            if (order == null) return;

            String depName = new String(new String("商城收货系统".getBytes("utf-8"), "utf-8").getBytes("gbk"), "gbk");
            String goodsTitle = new String(new String("收货资金".getBytes("utf-8"), "utf-8").getBytes("gbk"), "gbk");

            Map<String, String> map = new HashMap<String, String>();
            map.put("buyer_id", order.getUid());
            map.put("buyer_name", order.getAccount());
            map.put("out_trade_no", order.getOrderId());
            map.put("is_op", "0");
            map.put("op_name", "");
            map.put("dep_name", depName);
            map.put("game_id", order.getId().toString());
            map.put("order_createdate", DATA_FORMAT.format(order.getCreateTime()));
            map.put("total_fee", new DecimalFormat("0.00").format(order.getAmount()));
            map.put("goods_title", goodsTitle);
            map.put("price", order.getAmount().toString());
            map.put("quantity", "1");
            map.put("game_account", "");
            map.put("notify_url", paymentConfig.getShNotifyURL());
            map.put("return_url", paymentConfig.getShReturnURL());

            paymentURL = this.payManager.getPaymentURL(map);
            logger.info("支付网关生成地址:{}", paymentURL);
        } catch (SystemException ex) {
            // 捕获系统异常
            commonServiceResponse = new CommonServiceResponse(ex.getErrorCode(), ex.getArgs()[0].toString());
            res.getOutputStream().write(JsonMapper.nonEmptyMapper().toJson(commonServiceResponse).getBytes("utf-8"));
            logger.error("支付网关发生异常:{}", ExceptionUtils.getStackTrace(ex));
        } catch (Exception ex) {
            commonServiceResponse = new CommonServiceResponse(ResponseCodes.UnKnownError.getCode(),
                    ResponseCodes.UnKnownError.getMessage());
            res.getOutputStream().write(JsonMapper.nonEmptyMapper().toJson(commonServiceResponse).getBytes("utf-8"));
            logger.error("支付网关发生异常:{}", ExceptionUtils.getStackTrace(ex));
        }

        res.sendRedirect(paymentURL);
    }


}
