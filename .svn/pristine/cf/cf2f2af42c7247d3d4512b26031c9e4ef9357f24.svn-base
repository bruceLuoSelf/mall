package com.wzitech.gamegold.facade.frontend.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wzitech.gamegold.common.context.CurrentIpContext;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.order.business.IAutoPayManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.paymgmt.config.PaymentConfig;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.common.utils.PayHelper;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.entity.OrderInfoEO;

/**
 * Servlet implementation class PaymentNotifyServlet
 */
public class PaymentNotifyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * 日志记录器
     */
    protected final Logger logger = LoggerFactory.getLogger(PaymentNotifyServlet.class);

    private IOrderInfoManager orderInfoManager;

    private PaymentConfig paymentConfig;

    private IAutoPayManager autoPayManager;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.orderInfoManager = (IOrderInfoManager) context.getBean("orderInfoManagerImpl");
        this.paymentConfig = (PaymentConfig) context.getBean("paymentConfig");
        this.autoPayManager = (IAutoPayManager) context.getBean("autoPayManagerImpl");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURL = request.getRequestURL().toString();
        logger.info("支付异步回掉地址:{}", requestURL);


        UserInfoEO userInfoEO = new UserInfoEO();
        userInfoEO.setId(-1L);
        userInfoEO.setLoginAccount("PaymentNotifyServlet");
        userInfoEO.setUserType(UserType.System.getCode());
        CurrentUserContext.setUser(userInfoEO);
        CurrentIpContext.setIp(request.getLocalAddr());

        boolean isPaid = false;

        try {
            String queryString = request.getQueryString();
            logger.info("支付异步回掉地址参数:{}", queryString);
            String sign = request.getParameter("sign");
            String outTradeNO = request.getParameter("out_trade_no");
            String tradingType = request.getParameter("trading_type");
            String totalFee = request.getParameter("total_fee");

            logger.info("支付异步回调，订单号:{}", outTradeNO);

            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("out_trade_no", outTradeNO);
            map.put("trading_type", tradingType);
            map.put("total_fee", totalFee);
            String needSignString = PayHelper.joinCollectionToString(PayHelper.toLinkedSet(map, "=", null));
            String tmpSign = EncryptHelper.md5(needSignString + paymentConfig.getSingEncretKey(), "gb2312");

            if (tmpSign.equals(sign)) {
                //签名正确
                logger.info("支付异步回掉处理签名成功");
                OrderInfoEO dbOrderInfo = this.orderInfoManager.selectById(outTradeNO);
                if (null != dbOrderInfo && (dbOrderInfo.getOrderState().intValue() == OrderState.WaitPayment.getCode()
                        || dbOrderInfo.getOrderState().intValue() == OrderState.Cancelled.getCode())) {
                    // 查询资金明细，确认已支付
                    Boolean result = autoPayManager.queryPaymentDetail(dbOrderInfo);
                    if (result == null) {
                        //未返回标准结果，不做操作
                        logger.info("支付异步回调，查询资金明细，查询无结果，返回false");
                        isPaid = false;
                    } else if (result) {
                        //已支付，修改订单状态
                        logger.info("支付异步回调，查询资金明细，查询已付款，修改订单状态，返回true");
                        orderInfoManager.changeOrderState(dbOrderInfo.getOrderId(), OrderState.Paid.getCode(), null);
                        isPaid = true;
                    } else if (!result) {
                        //未支付，修改订单状态
                        logger.info("支付异步回调，查询资金明细，查询未支付，返回true");
                        isPaid = true;
                    }
                } else {
                    //订单已支付，或订单不存在，都返回true
                    logger.info("支付异步回调，查询资金明细，订单已支付，或订单不存在，返回true");
                    isPaid = true;
                }
            } else {
                //签名失败
                logger.info("支付异步回掉处理签名失败");
                isPaid = false;
            }
        } catch (Exception e) {
            logger.error("支付异步回掉时发生异常:{}", ExceptionUtils.getStackTrace(e));
            isPaid = false;
        }

        if (isPaid) {
            response.getWriter().write("true");
        } else {
            response.getWriter().write("false");
        }
        response.flushBuffer();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
