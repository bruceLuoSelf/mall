package com.wzitech.gamegold.facade.frontend.servlet;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.paymgmt.config.PaymentConfig;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.common.utils.PayHelper;
import com.wzitech.gamegold.order.business.IAutoPayManager;
import com.wzitech.gamegold.shorder.business.IFundManager;
import com.wzitech.gamegold.shorder.business.IPayOrderManager;
import com.wzitech.gamegold.shorder.entity.PayOrder;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 收货方支付成功跳转页面
 */
public class SHPaymentReturnServlet extends HttpServlet {
    /**
     * 日志记录器
     */
    protected final Logger logger = LoggerFactory.getLogger(PaymentServlet.class);

    private IPayOrderManager payOrderManager;
    private PaymentConfig paymentConfig;
    private IAutoPayManager autoPayManager;
    private IFundManager fundManager;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.payOrderManager = (IPayOrderManager) context.getBean("payOrderManagerImpl");
        this.paymentConfig = (PaymentConfig) context.getBean("paymentConfig");
        this.autoPayManager = (IAutoPayManager)context.getBean("autoPayManagerImpl");
        this.fundManager = (IFundManager)context.getBean("fundManagerImpl");
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
        String requestURL = req.getRequestURL().toString();
        logger.info("支付同步回掉地址:{}", requestURL);

        boolean isPaid = false;

        try {
            String queryString = req.getQueryString();
            logger.info("支付同步回掉地址参数:{}", queryString);
            String sign = req.getParameter("sign");
            String outTradeNO = req.getParameter("out_trade_no");
            String tradingType = req.getParameter("trading_type");
            String totalFee = req.getParameter("total_fee");

            logger.info("支付同步回调，订单号:{}", outTradeNO);

            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("out_trade_no", outTradeNO);
            map.put("trading_type", tradingType);
            map.put("total_fee", totalFee);
            String needSignString = PayHelper.joinCollectionToString(PayHelper.toLinkedSet(map, "=", null));
            String tmpSign = EncryptHelper.md5(needSignString + paymentConfig.getSingEncretKey(), "gb2312");

            if (tmpSign.equals(sign)) {
                //签名正确
                logger.info("支付同步回掉处理签名成功");
                PayOrder order = payOrderManager.queryByOrderId(outTradeNO, false);
                if (null != order && order.getStatus().intValue() == PayOrder.WAIT_PAYMENT) {
                    // 查询资金明细，确认已支付
                    Boolean result = autoPayManager.queryPaymentDetail(order.getOrderId(), order.getUid());
                    if (result == null) {
                        //未返回标准结果，不做操作
                        logger.info("支付同步回调，查询资金明细，查询无结果，返回false");
                        isPaid = false;
                    } else if (result) {
                        //已支付，修改订单状态
                        logger.info("支付同步回调，查询资金明细，查询已付款，修改订单状态，返回true");
                        BigDecimal amount = new BigDecimal(totalFee);
                        amount = amount.setScale(2, RoundingMode.HALF_DOWN);
                        fundManager.processRechargeSuccessNotify(order.getOrderId(), amount);
                        isPaid = true;
                    } else if (!result) {
                        //未支付，修改订单状态
                        logger.info("支付同步回调，查询资金明细，查询未支付，返回true");
                        isPaid = true;
                    }
                }
            } else {
                //签名失败
                logger.info("支付同步回掉处理签名失败");
                isPaid = false;
            }
        } catch (SystemException e) {
            if (ResponseCodes.OrderStatusHasChangedError.getCode().equals(e.getErrorCode())) {
                isPaid = true;
            } else {
                isPaid = false;
                logger.error("支付同步回掉时发生异常:{}", ExceptionUtils.getStackTrace(e));
            }
        } catch (Exception e) {
            logger.error("支付同步回掉时发生异常:{}", ExceptionUtils.getStackTrace(e));
            isPaid = false;
        }

        if (isPaid) {
            res.getWriter().write("true");
        } else {
            res.getWriter().write("false");
        }

        // 重定向
        String orderId = req.getParameter("out_trade_no");
        String url = String.format(paymentConfig.getShRedirectURL(), orderId);
        res.sendRedirect(url);
    }
}
