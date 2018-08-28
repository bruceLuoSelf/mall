/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ComplaintServiceImpl
 *	包	名：		com.wzitech.gamegold.facade.service.complaint.impl
 *	项目名称：	gamegold-facade
 *	作	者：		HeJian
 *	创建时间：	2014-2-25
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-25 下午5:51:09
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.complaint.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import com.wzitech.gamegold.common.enums.OrderState;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.TradeType;
import com.wzitech.gamegold.facade.service.complaint.IComplaintService;
import com.wzitech.gamegold.facade.service.complaint.dto.OrderInfoItem;
import com.wzitech.gamegold.facade.service.complaint.dto.QueryBuyerListRequest;
import com.wzitech.gamegold.facade.service.complaint.dto.QueryBuyerListResponse;
import com.wzitech.gamegold.facade.service.complaint.dto.QueryInfoByOrderIdRequest;
import com.wzitech.gamegold.facade.service.complaint.dto.QueryInfoByOrderIdResponse;
import com.wzitech.gamegold.facade.service.complaint.dto.QueryOrdersByUidRequest;
import com.wzitech.gamegold.facade.service.complaint.dto.QueryOrdersByUidResponse;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.entity.OrderInfoEO;

/**
 * Created by Administrator on 2017/1/4.
 *
 *  Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/16  lvchengsheng           ZW_C_JB_00008 商城增加通货
 */

/**
 * 投诉服务实现
 * @author HeJian
 *
 */
@Service("ComplaintService")
@Path("complaint")
@Produces("application/json; charset=UTF-8")
@Consumes("application/json; charset=UTF-8")
public class ComplaintServiceImpl extends AbstractBaseService implements IComplaintService{
	/**
	 * 客服名称
	 */
	@Value("${KFName}")
	private String kfName;
	
	/**
	 * 客服部门
	 */
	@Value("${KFWorkDeptName}")
	private String kfWorkDeptName;
	
	/**
	 * 客服组
	 */
	@Value("${KFWorkGroupName}")
	private String kfWorkGroupName;
	
	/**
	 * 客服真实姓名
	 */
	@Value("${KFRealName}")
	private String kfRealName;
	
	/**
	 * 客服所属业务站点
	 */
	@Value("${KFTradeSite}")
	private String kfTradeSite;
	
	@Autowired
	IOrderInfoManager orderInfoManager;
	
	@Path("querybuyerlist")
	@GET
	@Override
	public QueryBuyerListResponse queryBuyerList(@QueryParam("") QueryBuyerListRequest queryBuyerListRequest,
			@Context HttpServletRequest request) {
		logger.debug("当前查询订单:{}", queryBuyerListRequest);
		// 初始化返回数据
		QueryBuyerListResponse response = new QueryBuyerListResponse();
		try {
//			if(StringUtils.equals("seller", queryBuyerListRequest.getType())){
//				// 类型为卖家，直接返回
//				response.setTotalCount((long) 0);
//				response.setCurrentPage(0);
//				response.setCurrentPageSize(0);
//				response.setTotalPage((long) 0);
//				
//				return response;
//			}
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("uid", queryBuyerListRequest.getUid());
            queryMap.put("createStartTime", dateFormat.parse(queryBuyerListRequest.getMinDate() + " 00:00:00.000"));
            queryMap.put("createEndTime", dateFormat.parse(queryBuyerListRequest.getMaxDate() + " 23:59:59.999"));
			//增加按商品类目查询条件 by lvchengsheng 2017.5.15 add ZW_C_JB_00008
			if (StringUtils.isBlank(queryBuyerListRequest.getGoodsTypeName())){
				queryMap.put("goodsTypeName","全部");
			}else {
				queryMap.put("goodsTypeName",queryBuyerListRequest.getGoodsTypeName());
			}
            int pageSize = queryBuyerListRequest.getPs();
			int pageNub = queryBuyerListRequest.getP();
			if(pageSize == 0){
				pageSize = 10;
			}
			if(pageNub == 0){
				pageNub = 1;
			}
			GenericPage<OrderInfoEO> orderGenePage = orderInfoManager.queryOrderInfo(queryMap, "CREATE_TIME", false, pageSize, (pageNub-1)*pageSize);
			response.setTotalCount(orderGenePage.getTotalCount());
			response.setCurrentPage((int) orderGenePage.getCurrentPageNo());
			response.setCurrentPageSize((int) orderGenePage.getPageSize());
			response.setTotalPage(orderGenePage.getTotalPageCount());

			if(response.getTotalCount() == null){
				response.setTotalCount((long) 0);
			}
			if(response.getTotalPage() == null){
				response.setTotalPage((long) 0);
			}
			
			List<OrderInfoEO> orderInfoList = orderGenePage.getData();
			
			if(orderInfoList == null || orderInfoList.size() == 0){
				return response;
			}
			
			List<OrderInfoItem> responseOrder = new ArrayList<OrderInfoItem>();
			for (OrderInfoEO orderInfoEO : orderInfoList) {
				OrderInfoItem item = new OrderInfoItem();
				item.setAreaName(orderInfoEO.getRegion());
				item.setBasicType("1");
				item.setCreateTime(orderInfoEO.getCreateTime());
				item.setGameName(orderInfoEO.getGameName());
				item.setId(orderInfoEO.getOrderId());
				item.setRawSum("￥"+orderInfoEO.getTotalPrice().toString());
				//增加通货返回值类型 add by lvchengsheng 2017.5.16  ZW_C_JB_00008
				item.setGoodsTypeName(orderInfoEO.getGoodsTypeName());
				item.setGoodsTypeId(orderInfoEO.getGoodsTypeId());
                item.setTitle(orderInfoEO.getTitle() + "/" + orderInfoEO.getGoldCount() + orderInfoEO.getMoneyName());
                item.setServerName(orderInfoEO.getServer());
                if (orderInfoEO.getOrderState() == OrderState.Cancelled.getCode()) { // 撤单
                    item.setOrderPayStatusValue(2);
                } else if (orderInfoEO.getOrderState() == OrderState.Statement.getCode()) { // 交易成功
                    item.setOrderPayStatusValue(5);
                } else if (orderInfoEO.getOrderState() == OrderState.Delivery.getCode()) { // 移交给买家
                    item.setOrderPayStatusValue(4);
                } else { // 交易中
                    item.setOrderPayStatusValue(0);
                }

				responseOrder.add(item);
			}
			response.setOrderList(responseOrder);
		}catch (SystemException ex) {
			// 捕获系统异常
			logger.error("当前查询订单发生异常:{}", ex);
		} catch (Exception ex) {
			// 捕获未知异常
			logger.error("当前查询订单发生未知异常:{}", ex);
		}
		logger.debug("当前查询订单响应信息:{}", response);
		return response;
	}

	@Path("queryinfobyorderid")
	@GET
	@Override
	public QueryInfoByOrderIdResponse queryInfoByOrderId(@QueryParam("")QueryInfoByOrderIdRequest queryInfoByOrderIdRequest,
			@Context HttpServletRequest request) {
		logger.debug("当前根据订单号查询信息:{}", queryInfoByOrderIdRequest);
		// 初始化返回数据
		QueryInfoByOrderIdResponse response = new QueryInfoByOrderIdResponse();
		response.setKfName(kfName);
		response.setKfWorkDeptName(kfWorkDeptName);
		response.setKfWorkGroupName(kfWorkGroupName);
		response.setKfRealName(kfRealName);
		response.setKfTradeSite(kfTradeSite);
		try {
			OrderInfoEO orderInfo = orderInfoManager.selectById(queryInfoByOrderIdRequest.getOrderId());
			if(orderInfo == null){
				return response;
			}
			response.setGameAreaName(orderInfo.getRegion());//游戏区
			response.setGameServerName(orderInfo.getServer());//游戏服
			response.setGameName(orderInfo.getGameName());//游戏名
			//增加通货返回值类型 add by lvchengsheng 2017.5.16 ZW_C_JB_00008
			response.setGoodsTypeName(orderInfo.getGoodsTypeName());
			response.setGoodsTypeId(orderInfo.getGoodsTypeId());
			response.setTradingServiceTypeName(TradeType.getTypeByCode(orderInfo.getTradeType()).getName());//交易类型
		} catch (SystemException ex) {
			// 捕获系统异常
			logger.error("根据订单号查询信息发生异常:{}", ex);
		} catch (Exception ex) {
			// 捕获未知异常
			logger.error("根据订单号查询信息发生未知异常:{}", ex);
		}
		logger.debug("根据订单号查询信息响应信息:{}", response);
		return response;
	}

	@Path("queryordersbyuid")
	@GET
	@Override
	public QueryOrdersByUidResponse queryOrdersByUid(@QueryParam("")QueryOrdersByUidRequest queryOrdersByUidRequest,
			@Context HttpServletRequest request) {
		logger.debug("当前查询订单:{}", queryOrdersByUidRequest);
		// 初始化返回数据
		QueryOrdersByUidResponse response = new QueryOrdersByUidResponse();
		try {
			if(StringUtils.equals("seller", queryOrdersByUidRequest.getType())){
				// 类型为买家，直接返回
				response.setTotalCount((long) 0);
				response.setCurrentPage(0);
				response.setCurrentPageSize(0);
				response.setTotalPage((long) 0);
				
				return response;
			}
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("uid", queryOrdersByUidRequest.getUid());
            queryMap.put("createStartTime", dateFormat.parse(queryOrdersByUidRequest.getMinDate() + " 00:00:00.000"));
            queryMap.put("createEndTime", dateFormat.parse(queryOrdersByUidRequest.getMaxDate() + " 23:59:59.999"));
			//增加通货入参类型 add by lvchengsheng 2017.5.16 ZW_C_JB_00008
			if (StringUtils.isBlank(queryOrdersByUidRequest.getGoodsTypeName())){
				queryMap.put("goodsTypeName","全部");
			}else {
				queryMap.put("goodsTypeName",queryOrdersByUidRequest.getGoodsTypeName());
			}
			int pageSize = queryOrdersByUidRequest.getPs();
			int pageNub = queryOrdersByUidRequest.getP();
			if(pageSize == 0){
				pageSize = 10;
			}
			if(pageNub == 0){
				pageNub = 1;
			}
			GenericPage<OrderInfoEO> orderGenePage = orderInfoManager.queryOrderInfo(queryMap, "CREATE_TIME", false, pageSize, (pageNub-1)*pageSize);
			response.setTotalCount(orderGenePage.getTotalCount());
			response.setCurrentPage((int) orderGenePage.getCurrentPageNo());
			response.setCurrentPageSize((int) orderGenePage.getPageSize());
			response.setTotalPage(orderGenePage.getTotalPageCount());
			
			if(response.getTotalCount() == null){
				response.setTotalCount((long) 0);
			}
			if(response.getTotalPage() == null){
				response.setTotalPage((long) 0);
			}
			
			List<OrderInfoEO> orderInfoList = orderGenePage.getData();
			
			if(orderInfoList == null || orderInfoList.size() == 0){
				return response;
			}
			
			List<OrderInfoItem> responseOrder = new ArrayList<OrderInfoItem>();
			for (OrderInfoEO orderInfoEO : orderInfoList) {
				OrderInfoItem item = new OrderInfoItem();
				item.setAreaName(orderInfoEO.getRegion());
				item.setBasicType("1");
				item.setCreateTime(orderInfoEO.getCreateTime());
				item.setGameName(orderInfoEO.getGameName());
				item.setId(orderInfoEO.getOrderId());
				item.setRawSum("￥"+orderInfoEO.getTotalPrice().toString());
				//增加通货返回值类型 add by lvchengsheng 2017.5.16 ZW_C_JB_00008
				item.setGoodsTypeName(orderInfoEO.getGoodsTypeName());
				item.setGoodsTypeId(orderInfoEO.getGoodsTypeId());
                item.setTitle(orderInfoEO.getTitle() + "/" + orderInfoEO.getGoldCount() + orderInfoEO.getMoneyName());
                item.setServerName(orderInfoEO.getServer());
                if (orderInfoEO.getOrderState() == OrderState.Cancelled.getCode()) { // 撤单
                    item.setOrderPayStatusValue(2);
                } else if (orderInfoEO.getOrderState() == OrderState.Statement.getCode()) { // 交易成功
                    item.setOrderPayStatusValue(5);
                } else if (orderInfoEO.getOrderState() == OrderState.Delivery.getCode()) { // 移交给买家
                    item.setOrderPayStatusValue(4);
                } else { // 交易中
                    item.setOrderPayStatusValue(0);
                }

				responseOrder.add(item);
			}
			response.setOrderList(responseOrder);
			
		} catch (SystemException ex) {
			// 捕获系统异常
			logger.error("当前查询订单发生异常:{}", ex);
		} catch (Exception ex) {
			// 捕获未知异常
			logger.error("当前查询订单发生未知异常:{}", ex);
		}
		logger.debug("当前查询订单响应信息:{}", response);
		return response;
	}

	@GET
	@Path("orderstate")
	@Override
	public String queryOrderState(@QueryParam("id")String orderId) {
		OrderInfoEO orderInfoEO = orderInfoManager.selectById(orderId);
		if(orderInfoEO == null){
			return "0";
		}
		return "1";
	}

}
