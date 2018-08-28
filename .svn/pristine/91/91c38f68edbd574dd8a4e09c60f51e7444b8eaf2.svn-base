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
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.paymgmt.config.RefundConfig;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.common.utils.PayHelper;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.entity.OrderInfoEO;

/**
 * 退款通知Servlet
 * @author yunhai.Wu
 *
 */
public class RefundNotifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * 日志记录器
     */
    protected final Logger logger = LoggerFactory.getLogger(RefundNotifyServlet.class);
    
    private IOrderInfoManager orderInfoManager;
    
    private RefundConfig refundConfig;
       
    @Override
	public void init() throws ServletException {
		super.init();
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		this.orderInfoManager = (IOrderInfoManager)context.getBean("orderInfoManagerImpl");
		this.refundConfig = (RefundConfig)context.getBean("refundConfig");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURL = request.getRequestURI().toString();
		logger.info("退款回调地址:{}", requestURL);


        UserInfoEO userInfoEO = new UserInfoEO();
        userInfoEO.setId(-1L);
        userInfoEO.setLoginAccount("RefundNotifyServlet");
        userInfoEO.setUserType(UserType.System.getCode());
        CurrentUserContext.setUser(userInfoEO);
        CurrentIpContext.setIp(request.getLocalAddr());

		boolean isRefund = false;
		
		try{
			String queryString = request.getQueryString();
			logger.info("退款异步回调地址参数:{}", queryString);
			String sign = request.getParameter("sign");
			String outTradeNO = request.getParameter("out_trade_no");
			String tradingType = request.getParameter("trading_type");
			String totalFee = request.getParameter("total_fee");
			
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("out_trade_no", outTradeNO);
			map.put("trading_type", tradingType);
			map.put("total_fee", totalFee);
			String needSignString = PayHelper.joinCollectionToString(PayHelper.toLinkedSet(map, "=", null));
			String tmpSign = EncryptHelper.md5(needSignString + refundConfig.getSignSecretKey(), "gb2312");
			
			if (tmpSign.equals(sign)){
				//签名正确
                logger.info("退款异步回调处理签名成功");
                OrderInfoEO dbOrderInfo = this.orderInfoManager.selectById(outTradeNO);
                if (null != dbOrderInfo && dbOrderInfo.getOrderState().intValue() == OrderState.Refund.getCode()
                		){
                	orderInfoManager.changeOrderState(outTradeNO, OrderState.Refund.getCode(), null);
                	isRefund = true;
                }
			}
                else {
					//签名失败
                	logger.info("退款异步回调处理签名失败");
                	isRefund = false;
				}
			} catch (Exception e) {
				logger.error("退款异步回调时发生异常:{}", ExceptionUtils.getStackTrace(e));
				isRefund = false;
			}
		if (isRefund) {
			response.getWriter().write("true");
		}else{
            response.getWriter().write("false");
        }
		response.flushBuffer();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
