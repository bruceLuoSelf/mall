/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryGameInfoResponse
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.game.dto
 *	项目名称：	    gamegold-facade-frontend
 *	作	者：		yubaihai
 *	创建时间：	    2014-6-28
 *	描	述：		
 *	更新纪录：	1. yubaihai 创建于 2014-6-28 下午3:43:33
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.util.List;


import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wzitech.chaos.framework.server.common.exception.SystemException;

import com.wzitech.gamegold.common.utils.CommissionUtil;
import com.wzitech.gamegold.order.business.ICommissionManager;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.usermgmt.IGameUserManager;
import com.wzitech.gamegold.common.usermgmt.entity.GameUserInfo;
import com.wzitech.gamegold.facade.frontend.service.excel.ExportExcel;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;

/**
 * Servlet implementation class PaymentNotifyServlet
 */
public class ExportOrderServlet extends HttpServlet {
private static final long serialVersionUID = 1L;

    /**
     * 日志记录器
     */
    protected final Logger logger = LoggerFactory.getLogger(ExportOrderServlet.class);

	private IOrderInfoManager orderInfoManager;
	
	
	private IGameUserManager gameUserManager;

//	private ICommissionManager commissionManager;

	/**
	 * 公司得到拥金的比例
	 */
	@Value("${sub_commission.base}")
	private double subCommissionBase = 0.06;
	
	@Override
	public void init() throws ServletException {
		super.init();
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		this.orderInfoManager = (IOrderInfoManager)context.getBean("orderInfoManagerImpl");
		this.gameUserManager = (IGameUserManager)context.getBean("gameUserManagerImpl");
//	    this.commissionManager = (ICommissionManager)context.getBean("commissionManagerImpl");
	}
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 获取位于HTTP HEAD中的cookie
//			String cookie = request.getHeader(ServicesContants.SERVICE_REQUEST_COOKIE);
	        // 从Http Request中获取Cookie
	        String cookie = null;
	        Cookie[] cookies = request.getCookies();

	        if (null != cookies && cookies.length > 0) {
	            for (Cookie acookie : cookies) {
	                if (StringUtils.equals(acookie.getName(), ".5173auth")) {
	                    cookie = acookie.getValue();
	                }
	            }
	        }

	        if (StringUtils.isEmpty(cookie)) {
				cookie = request.getParameter(ServicesContants.SERVICE_REQUEST_COOKIE);
			}
			logger.debug("当前拦截请求的cookie为 {}", cookie);
					
//			// 查找cookie对应用户
			GameUserInfo userInfo = null;
		    userInfo = gameUserManager.analysisCookie(cookie);
//		    userInfo = new GameUserInfo();
//		    userInfo.setUserID("US12090746797191-0101");
//		    userInfo.setUserName("scsxj");

		    //			UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
			request.setCharacterEncoding("utf-8");
			String searchOrderId = request.getParameter("searchOrderId");
			String orderGameName = request.getParameter("orderGameName");
			String orderRegion = request.getParameter("orderRegion");
			String orderServer = request.getParameter("orderServer");
			String goodsTypeName = request.getParameter("goodsTypeName");//导出表格增加商品类型字段
			String orderCreateTime_s = request.getParameter("orderCreateTime_s");
			String orderCreateTime_e = request.getParameter("orderCreateTime_e");
			String ordersState = request.getParameter("ordersState");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startOrderCreate = null;
			Date endOrderCreate = null;
			if(StringUtils.isNotBlank(orderCreateTime_s)){
				orderCreateTime_s += " 00:00:00";
				startOrderCreate  =	(Date) sdf.parse(orderCreateTime_s);
			}   
			if(StringUtils.isNotBlank(orderCreateTime_e)){
				orderCreateTime_e +=" 23:59:59";
				endOrderCreate  =	(Date) sdf.parse(orderCreateTime_e);
			}   
			List<ConfigResultInfoEO> configResultList  = null;
			if(userInfo!=null){
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("accountUid", userInfo.getUserID());
			queryMap.put("loginAccount", userInfo.getUserName());
			if(StringUtils.isNotEmpty(ordersState)){
			queryMap.put("orderState", Integer.parseInt(ordersState));}
			queryMap.put("gameName", orderGameName);
			queryMap.put("region", orderRegion);
			queryMap.put("server", orderServer);
			queryMap.put("goodsTypeName", goodsTypeName);//导出表格增加商品类型字段
		    queryMap.put("startOrderCreate", startOrderCreate);
		    queryMap.put("endOrderCreate", endOrderCreate);
		    queryMap.put("searchOrderId", searchOrderId);
		    configResultList = orderInfoManager.queryExportSellerOrder(queryMap, "CREATE_TIME",false);
			}
			
			HSSFWorkbook wb = new HSSFWorkbook();

			HSSFSheet sheet = wb.createSheet();

			ExportExcel exportExcel = new ExportExcel(wb, sheet);

			// 创建单元格样式
			HSSFCellStyle cellStyle = wb.createCellStyle();

			// 指定单元格居中对齐
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// 指定单元格垂直居中对齐
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

			// 指定当单元格内容显示不下时自动换行
			cellStyle.setWrapText(true);

			// 设置单元格字体
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontName("宋体");
			font.setFontHeight((short) 200);
			cellStyle.setFont(font);

			// 创建报表头部
			String headString = "我的卖家商城订单";
			int columnSize = 15;
			exportExcel.createNormalHead(0, headString, columnSize-1);

			// 创建报表列
			String[] columHeader = new String[] {  "卖家5173账号" , "卖家角色" ,"订单号", "订单状态" ,"成交游戏账号" , "商品名称", "游戏名称", "所在区", "所在服", "所在阵营","商品类型", "入库单价(1游戏币兑换多少元)" ,"卖出数量", "卖家收益" ,"卖家流水"
					 ,"最后更新时间" };
			exportExcel.createColumHeader(1, columHeader);

//			Map<String, ConfigResultInfoEO> map = new HashMap<String, ConfigResultInfoEO>();
//			
////			for (ConfigResultInfoEO configResultInfoEO : configResultList) {
////				
////				if(!map.containsKey(configResultInfoEO.getOrderId())){
////					map.put(configResultInfoEO.getOrderId(), configResultInfoEO);
////				}else{
////					ConfigResultInfoEO ortherConfigResultInfoEO = map.get(configResultInfoEO.getOrderId());
////					//设置拼单合并成一条的卖出数量
////					ortherConfigResultInfoEO.setConfigGoldCount(configResultInfoEO.getConfigGoldCount()+ortherConfigResultInfoEO.getConfigGoldCount());
////					//设置拼单合并成一条的卖家收益
////					ortherConfigResultInfoEO.setTotalPrice(configResultInfoEO.getTotalPrice().add(ortherConfigResultInfoEO.getTotalPrice()));
////					//设置拼单合并成一条的卖家入库单价
////					ortherConfigResultInfoEO.setRepositoryUnitPrice(ortherConfigResultInfoEO.getTotalPrice().divide(new BigDecimal(ortherConfigResultInfoEO.getConfigGoldCount()), 4, BigDecimal.ROUND_HALF_UP));
////					map.put(configResultInfoEO.getOrderId(), ortherConfigResultInfoEO);
////				}
////			}
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < configResultList.size(); i++) {
				ConfigResultInfoEO configResultInfoEO = configResultList.get(i);
				if(!map.containsKey(configResultInfoEO.getOrderId())){
				   map.put(configResultInfoEO.getOrderId(), i+"A");
			     }else{
			    	 String orderId = configResultInfoEO.getOrderId();
			    	 String flag = map.get(orderId);
			    	 char letter = flag.charAt(flag.length()-1);
			    	if('A' == letter){
			    		int index = Integer.parseInt(flag.substring(0, flag.length()-1));
			    		ConfigResultInfoEO alertconfigResultInfoEOA =  configResultList.get(index);
			    		alertconfigResultInfoEOA.setOrderId(alertconfigResultInfoEOA.getOrderId()+"A");
			    		
			    		configResultInfoEO.setOrderId(configResultInfoEO.getOrderId()+"B");
			    		map.put(orderId, "B");
			    		
			    	}else{
			    		
			    		char addletter = (char) (letter+1);
			    		configResultInfoEO.setOrderId(configResultInfoEO.getOrderId()+addletter);
			    		map.put(orderId, String.valueOf(addletter));
			    	} 
			    }
			}
			
			// 循环创建中间的单元格的各项的值
			if (configResultList != null) {
				int i = 2;

				HSSFCellStyle cellStyle1 = wb.createCellStyle();
				cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);

				for (ConfigResultInfoEO configResultInfoEO : configResultList) {
					HSSFRow row = sheet.createRow(i++);
					/*subCommissionBase = 0.06;
					Date date1=null,date2=null,date3=null;
						date1 = configResultInfoEO.getOrderInfoEO().getEndTime();
						date2 = sdf.parse("2014-08-13 12:00:00");
						date3 = sdf.parse("2014-11-26 12:30:00");
					if(date1 != null && date1.after(date2)){
						if("剑灵".equals(configResultInfoEO.getOrderInfoEO().getGameName())){
							subCommissionBase = 0.05;
							if("电信傲雪区".equals(configResultInfoEO.getOrderInfoEO().getRegion())||"电信传说区".equals(configResultInfoEO.getOrderInfoEO().getRegion())
									||("网通冰魂区".equals(configResultInfoEO.getOrderInfoEO().getRegion())&&date3.after(date1))){
								subCommissionBase = 0.00;
							}
						}
					}
                    if (configResultInfoEO.getOrderInfoEO().getGameName().equals("疾风之刃")) {
                        subCommissionBase = 0.00;
                    }*/

                    Date createTime = configResultInfoEO.getOrderInfoEO().getCreateTime();
                    String gameName = configResultInfoEO.getOrderInfoEO().getGameName();
                    String region = configResultInfoEO.getOrderInfoEO().getRegion();
					Integer goodsCat = configResultInfoEO.getOrderInfoEO().getGoodsCat();
                    //subCommissionBase = CommissionUtil.getCommission(createTime, gameName, region);

					/*BigDecimal commission = commissionManager.getCommission(gameName, region, goodsCat);
					subCommissionBase = commission.doubleValue();*/

					RepositoryInfo repositoryInfo = configResultInfoEO.getRepositoryInfo();
					String loginAccount = configResultInfoEO.getLoginAccount(),
							sellerGameRole = "",
							gameAccount = "";
					if (repositoryInfo != null) {
						sellerGameRole = repositoryInfo.getSellerGameRole();
						gameAccount = repositoryInfo.getGameAccount();
					}

					exportExcel.cteateCell(wb, row, (short) 0,
							cellStyle1, loginAccount);
					exportExcel.cteateCell(wb, row, (short) 1,
							cellStyle1, sellerGameRole);
					exportExcel.cteateCell(wb, row, (short) 2,
							cellStyle1,
							configResultInfoEO.getOrderId());
					exportExcel.cteateCell(wb, row, (short) 3,
							cellStyle1,
							OrderState.getTypeByCode(configResultInfoEO.getOrderInfoEO().getOrderState()).getName());
					exportExcel.cteateCell(wb, row, (short) 4,
							cellStyle1, gameAccount);
					exportExcel.cteateCell(wb, row, (short) 5,
							cellStyle1,
							configResultInfoEO.getOrderInfoEO().getTitle());
					exportExcel.cteateCell(wb, row, (short) 6,
							cellStyle1,
							configResultInfoEO.getOrderInfoEO().getGameName());
					exportExcel.cteateCell(wb, row, (short) 7,
							cellStyle1, configResultInfoEO.getOrderInfoEO().getRegion());
					exportExcel.cteateCell(wb, row, (short) 8,
							cellStyle1, configResultInfoEO.getOrderInfoEO().getServer());
					String mGameRace = "";
					if(configResultInfoEO.getOrderInfoEO().getGameRace()!=null){
						mGameRace = configResultInfoEO.getOrderInfoEO().getGameRace();
					}
					exportExcel.cteateCell(wb, row, (short) 9,
							cellStyle1, mGameRace);
					//新增商品类目字段 by hyl
					exportExcel.cteateCell(wb, row, (short) 10,
							cellStyle1, configResultInfoEO.getOrderInfoEO().getGoodsTypeName());
					exportExcel.cteateCell(wb, row, (short) 11,
							cellStyle1, configResultInfoEO.getRepositoryUnitPrice().toString());
					exportExcel.cteateCell(wb, row, (short) 12,
							cellStyle1, configResultInfoEO.getConfigGoldCount().toString());
					String sellerearnings = "";
					if (configResultInfoEO.getTotalPrice() != null) {
						//sellerearnings = configResultInfoEO.getTotalPrice().multiply(new BigDecimal(1-subCommissionBase)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
						sellerearnings = configResultInfoEO.getIncome().toString();
					}
					exportExcel.cteateCell(wb, row, (short) 13,
							cellStyle1, sellerearnings);
					String totalPrice = "";
					if (configResultInfoEO.getTotalPrice() != null) {
						totalPrice =  configResultInfoEO.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
					}
					exportExcel.cteateCell(wb, row, (short) 14,
							cellStyle1, totalPrice);
					String lastUpdateTime = "";
					if(configResultInfoEO.getLastUpdateTime()!=null){
						lastUpdateTime = sdf.format(configResultInfoEO.getLastUpdateTime()); 
					}
					exportExcel.cteateCell(wb, row, (short) 15,
							cellStyle1,lastUpdateTime);
				}
			}
			String configsellerorder = "";
			if(userInfo != null){
				configsellerorder = userInfo.getUserName(); 
			}
			 configsellerorder += "sellerorder.xls";
			response.setContentType("application/octet-stream");     
			response.addHeader("Content-Type", "text/html; charset=utf-8");
			response.addHeader("Content-Disposition", "attachment; filename=" + configsellerorder);     
		        OutputStream ouputStream = response.getOutputStream();     
		        exportExcel.write(ouputStream);     
		        ouputStream.flush();     
		        ouputStream.close(); 
		} catch (SystemException ex) {
			logger.error("查询卖家订单发生异常:{}", ex);
		}catch (Exception e) {
		logger.error("查询卖家订单发生未知异常:{}", e);
		}
		}
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
