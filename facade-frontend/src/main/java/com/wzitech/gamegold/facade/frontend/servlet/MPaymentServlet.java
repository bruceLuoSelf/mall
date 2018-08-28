package com.wzitech.gamegold.facade.frontend.servlet;

import com.wzitech.chaos.framework.server.common.CommonServiceResponse;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.paymgmt.IPayManager;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.wsdl.extensions.http.HTTPUrlEncoded;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 340082 on 2017/3/29.
 * m站支付servlet
 */
public class MPaymentServlet extends HttpServlet {
    /**
     * 日志记录器
     */
    protected final Logger logger = LoggerFactory.getLogger(PaymentServlet.class);

    private static final long serialVersionUID = 1L;

    private IPayManager payManager;

    private IOrderInfoManager orderInfoManager;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String paymentURL = null;
        CommonServiceResponse commonServiceResponse = null;
        try {
            OrderInfoEO orderInfoEO = orderInfoManager.selectById(req.getParameter("orderId"));
//            String goodsTitle = new String(new String("收货资金".getBytes("utf-8"), "utf-8").getBytes("gbk"), "gbk");
            Map<String, String> map = new HashMap<String, String>();
            map.put("buyer_id", orderInfoEO.getUid());
            map.put("buyer_name",orderInfoEO.getUserAccount());
            map.put("out_trade_no", orderInfoEO.getOrderId());
            map.put("is_op", "0");
            map.put("op_name", "");
            map.put("game_id", orderInfoEO.getGameId());
            map.put("order_createdate", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(orderInfoEO.getCreateTime()));
            map.put("total_fee", new DecimalFormat("0.00").format(orderInfoEO.getTotalPrice()));
            map.put("goods_title","");
            map.put("price", orderInfoEO.getUnitPrice().toString());
            map.put("quantity", orderInfoEO.getGoldCount().toString());
            map.put("game_account", "");
            paymentURL = this.payManager.getMPaymentURL(map);
            logger.info("支付网关生成地址:{}", paymentURL);
        } catch (SystemException ex) {
            // 捕获系统异常
            commonServiceResponse = new CommonServiceResponse(ex.getErrorCode(), ex.getArgs()[0].toString());
            resp.getOutputStream().write(JsonMapper.nonEmptyMapper().toJson(commonServiceResponse).getBytes("utf-8"));
            logger.error("支付网关发生异常:{}", ExceptionUtils.getStackTrace(ex));
        } catch (Exception ex) {
            commonServiceResponse = new CommonServiceResponse(ResponseCodes.UnKnownError.getCode(),
                    ResponseCodes.UnKnownError.getMessage());
            resp.getOutputStream().write(JsonMapper.nonEmptyMapper().toJson(commonServiceResponse).getBytes("utf-8"));
            logger.error("支付网关发生异常:{}", ExceptionUtils.getStackTrace(ex));
        }
        resp.sendRedirect(paymentURL);
    }
    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.payManager= (IPayManager)context.getBean("payManagerImpl");
        this.orderInfoManager = (IOrderInfoManager)context.getBean("orderInfoManagerImpl");
    }
}
