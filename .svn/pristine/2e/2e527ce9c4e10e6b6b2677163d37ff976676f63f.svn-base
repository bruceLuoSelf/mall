/************************************************************************************
 *Copyright 2012 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		AccountServiceImpl
 *	包	名：		com.wzitech.chinabeauty.facade.service.usermgmt.impl
 *	项目名称：	chinabeauty-facade-frontend
 *	作	者：		SunChengfei
 *	创建时间：	2013-9-26
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2013-9-26 下午2:51:45
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.enums.TradeType;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.facade.backend.contant.WebServerContants;
import com.wzitech.gamegold.facade.backend.excel.ExportExcel;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.entity.ShippingInfoEO;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
/**
 * 通货出库订单导出接口
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/17  zhujun           ZW_C_JB_00008 导出增加商品类目
 **/
@Controller
@Scope("prototype")
public class ExportShippingOrderAction extends AbstractAction {

	private Date createStartTime;

	private Date createEndTime;

	private InputStream inputStream;

	/**
	 * 2017/05/17 zhujun  新增商品类型
	 */
	private String goodsTypeName;

	@Autowired
	IOrderInfoManager orderInfoManager;
	
	@Autowired
	ISellerManager sellerManager;

	private List<ShippingInfoEO> queryShippingList() throws UnsupportedEncodingException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		int userType = CurrentUserContext.getUserType();
		if (UserType.SystemManager.getCode() != userType) {
			if (UserType.MakeOrder.getCode() == userType
					|| UserType.RecruitBusiness.getCode() == userType) {
				UserInfoEO user = (UserInfoEO) CurrentUserContext.getUser();
				paramMap.put("servicerId", user.getMainAccountId());
			} else {
				paramMap.put("servicerId", CurrentUserContext.getUid());
			}
		}
		paramMap.put("createStartTime", createStartTime);
		paramMap.put("createEndTime",
				WebServerUtil.oneDateLastTime(createEndTime));
		paramMap.put("orderState", OrderState.WaitDelivery.getCode());
		goodsTypeName = URLDecoder.decode(goodsTypeName, "utf-8");
		logger.info("导出出库订单, 商品类型：" + goodsTypeName);
		paramMap.put("goodsTypeName",goodsTypeName);//ZW_C_JB_00008_20170517 ADD
		List<ShippingInfoEO> list = orderInfoManager.queryShippingList(
				paramMap, "CREATE_TIME", false);
		return list;
	}

	public String exportShippingOrder() throws UnsupportedEncodingException {
        logger.info("导出出库订单开始............");
		List<ShippingInfoEO> shippingList = queryShippingList();
		logger.info("导出出库订单, 记录个数：" + shippingList.size());

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
		String headString = "交易流水列表";
		int columnSize = 11;
		exportExcel.createNormalHead(0, headString, columnSize-1);

		// 创建报表列
		String[] columHeader = new String[] { "订单号", "买家", "卖家", "发布单名称",
				"商品数量","商品类型名称", "订单单价", "订单总额", "选择交易方式", "创建时间", "结束时间", "备注" };//ZW_C_JB_00008_20170512 ADD '商品类型'
		exportExcel.createColumHeader(1, columHeader);

		HSSFCellStyle cellstyle = wb.createCellStyle();
		cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);

		// 循环创建中间的单元格的各项的值
		if (shippingList != null) {
			int i = 2;
			for (ShippingInfoEO shippingInfo : shippingList) {
				HSSFRow row = sheet.createRow((short) i++);
				exportExcel.cteateCell(wb, row, (short) 0,
						cellstyle,
						shippingInfo.getOrderId());
				exportExcel.cteateCell(wb, row, (short) 1,
						cellstyle,
						shippingInfo.getBuyer());
				StringBuilder sellers = new StringBuilder();
				List<SellerInfo> sellerList = sellerManager.querySellerByOrderId(shippingInfo.getOrderId(),shippingInfo.getGoodsTypeName());
				int index = 1;
				for(SellerInfo seller : sellerList){
					sellers.append(seller.getLoginAccount());
					if(sellerList.size()>index){
						sellers.append(" || ");
					}
					index++;
				}
				exportExcel.cteateCell(wb, row, (short) 2,
						cellstyle,
						sellers.toString());
				exportExcel.cteateCell(wb, row, (short) 3,
						cellstyle,
						shippingInfo.getTitle());
				exportExcel.cteateCell(wb, row, (short) 4,
						cellstyle, shippingInfo
								.getGoldCount().toString());
				/**  ZW_C_JB_00008_20170513_START **/
				String goodsTypeName;
				if (StringUtils.isEmpty(shippingInfo.getGoodsTypeName())){
					goodsTypeName = "游戏币";
				}else{
					goodsTypeName = shippingInfo.getGoodsTypeName();
				}
				exportExcel.cteateCell(wb, row, (short) 5,
						cellstyle, goodsTypeName);
				/**  ZW_C_JB_00008_20170513_END **/
				String unitPrice = "";
				if (shippingInfo.getUnitPrice() != null) {
					unitPrice = "￥" + shippingInfo.getUnitPrice().toString();
				}
				exportExcel.cteateCell(wb, row, (short) 6,
						cellstyle, unitPrice);
				String totalPrice = "";
				if (shippingInfo.getTotalPrice() != null) {
					totalPrice = "￥" + shippingInfo.getTotalPrice().toString();
				}
				exportExcel.cteateCell(wb, row, (short) 7,
						cellstyle, totalPrice);
				String tradeType = "";
				if (shippingInfo.getTradeType() != null) {
					tradeType = TradeType.getTypeByCode(
							shippingInfo.getTradeType()).getName();
				}
				exportExcel.cteateCell(wb, row, (short) 8,
						cellstyle, tradeType);
				String createTime = "";
				if(shippingInfo.getCreateTime()!=null){
					createTime = format.format(shippingInfo.getCreateTime());
				}
				exportExcel.cteateCell(wb, row, (short) 9,
						cellstyle,
						createTime);
				String endTime = "";
				if(shippingInfo.getEndTime()!=null){
					endTime = format.format(shippingInfo.getEndTime());
				}
				exportExcel.cteateCell(wb, row, (short) 10,
						cellstyle,
						endTime);
				exportExcel.cteateCell(wb, row, (short) 11,
						cellstyle,
						shippingInfo.getNotes());
			}
		}

		String exportPath = WebServerContants.FILES_EXPORT_PATH;
		String path = ServletActionContext.getServletContext().getRealPath(
				exportPath);
		File file = new File(path);
		file.mkdirs();
		String filePath = path + "/" + UUID.randomUUID().toString() + ".xls";
		logger.info("导出出库订单, 导出目录："+filePath);
		exportExcel.outputExcel(filePath);
		try {
			inputStream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			logger.error("导出出库订单, 服务器目录不存在");
			e.printStackTrace();
		}
		return returnSuccess();
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setCreateStartTime(Date createStartTime) {
		this.createStartTime = createStartTime;
	}

	public void setCreateEndTime(Date createEndTime) {
		this.createEndTime = createEndTime;
	}

	public String getGoodsTypeName() {
		return goodsTypeName;
	}

	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}
}
