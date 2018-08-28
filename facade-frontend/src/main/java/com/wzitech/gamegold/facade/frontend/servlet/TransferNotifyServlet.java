package com.wzitech.gamegold.facade.frontend.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.paymgmt.config.TransferConfig;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.common.utils.PayHelper;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.entity.OrderInfoEO;

/**
 * 转账通知Servlet
 * 
 * @author yunhai.Wu
 * 
 */
public class TransferNotifyServlet extends HttpServlet {

    /**
     * 日志记录器
     */
    protected final Logger logger = LoggerFactory.getLogger(TransferNotifyServlet.class);
    
	private static final long serialVersionUID = 1L;
	
	private IOrderInfoManager orderInfoManager;
	
	private TransferConfig transferConfig;
	
	@Override
	public void init() throws ServletException {
		super.init();
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		this.orderInfoManager = (IOrderInfoManager)context.getBean("orderInfoManagerImpl");
		this.transferConfig = (TransferConfig)context.getBean("transferConfig");
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String requestURL = request.getRequestURL().toString();
        logger.info("转账异步回调地址:{}", requestURL);
        
        boolean isTransfer = false;
        
        try{
			String queryString = request.getQueryString();
			logger.info("转账异步回调地址参数:{}", queryString);
			String sign = request.getParameter("sign");
			String outTradeNO = request.getParameter("out_trade_no");
            String transferNoList = request.getParameter("transfer_no_list");
			
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("out_trade_no", outTradeNO);
			map.put("transfer_no_list", transferNoList);
			String needSignString = PayHelper.joinCollectionToString(PayHelper.toLinkedSet(map, "=", null));
			String tmpSign = EncryptHelper.md5(needSignString + transferConfig.getSignSecretKey(), "gb2312");
			
			if (tmpSign.equals(sign)){
				//签名正确
                logger.info("转账异步回调处理签名成功");
                OrderInfoEO dbOrderInfo = this.orderInfoManager.selectById(outTradeNO);
                if (null != dbOrderInfo && dbOrderInfo.getOrderState().intValue() == OrderState.Statement.getCode()
                		){
                	orderInfoManager.changeOrderState(outTradeNO, OrderState.Statement.getCode(), null);
                	isTransfer = true;
                }
			}
                else {
					//签名失败
                	logger.info("转账异步回调处理签名失败");
                	isTransfer = false;
				}
			} catch (Exception e) {
				logger.error("转账异步回调时发生异常:{}", ExceptionUtils.getStackTrace(e));
				isTransfer = false;
			}
		if (isTransfer) {
			response.getWriter().write("true");
		}else{
            response.getWriter().write("false");
        }
		response.flushBuffer();
        
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}