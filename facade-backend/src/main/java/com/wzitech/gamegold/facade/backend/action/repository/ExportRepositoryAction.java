package com.wzitech.gamegold.facade.backend.action.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.facade.backend.contant.WebServerContants;
import com.wzitech.gamegold.facade.backend.excel.ExportExcel;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

@Controller
@Scope("prototype")
/**
 * 通货库存导出接口
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/12  wubiao           ZW_C_JB_00008 商城增加通货
**/
public class ExportRepositoryAction extends AbstractAction {

	private RepositoryInfo repository;
	
	private InputStream inputStream;
	
	@Autowired
	IRepositoryManager repositoryManager;

	private List<RepositoryInfo> queryRepository() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		int userType = CurrentUserContext.getUserType();
		if(UserType.SystemManager.getCode()!=userType){
			if(UserType.MakeOrder.getCode()==userType||UserType.RecruitBusiness.getCode()==userType){
				UserInfoEO user = (UserInfoEO)CurrentUserContext.getUser();
				paramMap.put("servicerId", user.getMainAccountId());
			}else{
				paramMap.put("servicerId", CurrentUserContext.getUid());
			}
		}
		if(repository!=null){
			paramMap.put("sellableCount", repository.getGoldCount());
			paramMap.put("orderUnitPrice", repository.getUnitPrice());
			paramMap.put("loginAccount", repository.getLoginAccount());
			paramMap.put("gameName", repository.getGameName());
			paramMap.put("region", repository.getRegion());
			paramMap.put("server", repository.getServer());
			paramMap.put("gameRace", repository.getGameRace());
			paramMap.put("backSellerGameRole", repository.getSellerGameRole());
			paramMap.put("goodsTypeName", repository.getGoodsTypeName());//ZW_C_JB_00008_20170512 ADD
		}
		logger.info("订单配单查询参数：{}",paramMap);
		List<RepositoryInfo> repositoryList = repositoryManager.queryRepository(paramMap, "UNIT_PRICE", true);
		return repositoryList;
	}

	public String exportRepository() {

		List<RepositoryInfo> repositoryList = queryRepository();
		
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
		int columnSize = 12;
		exportExcel.createNormalHead(0, headString, columnSize-1);

		// 创建报表列
		String[] columHeader = new String[] { "卖家5173账号", "游戏账号", "卖家游戏角色名", "游戏名称", "类目","单位", "所在区", "所在服", "所在阵营", "单价",
				"通货数目", "可销售库存" };//ZW_C_JB_00008_20170512 ADD '商品类目'
		exportExcel.createColumHeader(1, columHeader);

		HSSFCellStyle cellstyle = wb.createCellStyle();
		cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);

		// 循环创建中间的单元格的各项的值
		if (repositoryList != null) {
			int i = 2;
			for (RepositoryInfo repositoryInfo : repositoryList) {
				HSSFRow row = sheet.createRow((short) i++);
				exportExcel.cteateCell(wb, row, (short) 0,
						cellstyle,
						repositoryInfo.getLoginAccount());
				exportExcel.cteateCell(wb, row, (short) 1,
						cellstyle,
						repositoryInfo.getGameAccount());
				exportExcel.cteateCell(wb, row, (short) 2,
						cellstyle,
						repositoryInfo.getSellerGameRole());
				exportExcel.cteateCell(wb, row, (short) 3,
						cellstyle,
						repositoryInfo.getGameName());
				/*******ZW_C_JB_00008_20170512_START******/
				exportExcel.cteateCell(wb, row, (short) 4,
						cellstyle,
						repositoryInfo.getGoodsTypeName());
				exportExcel.cteateCell(wb, row, (short) 5,
						cellstyle,
						repositoryInfo.getMoneyName());
				/*******ZW_C_JB_00008_20170512_END*****/
				exportExcel.cteateCell(wb, row, (short) 6,
						cellstyle, repositoryInfo.getRegion());
				exportExcel.cteateCell(wb, row, (short) 7,
						cellstyle, repositoryInfo.getServer());
				exportExcel.cteateCell(wb, row, (short) 8,
						cellstyle, repositoryInfo.getGameRace());
				String unitPrice = "";
				if (repositoryInfo.getUnitPrice() != null) {
					unitPrice = "￥" + repositoryInfo.getUnitPrice().toString();
				}
				exportExcel.cteateCell(wb, row, (short) 9,
						cellstyle, unitPrice);
				exportExcel.cteateCell(wb, row, (short) 10,
						cellstyle, repositoryInfo.getGoldCount().toString());
				exportExcel.cteateCell(wb, row, (short) 11,
						cellstyle, repositoryInfo.getSellableCount().toString());
			}
		}

		String exportPath = WebServerContants.FILES_EXPORT_PATH;
		String path = ServletActionContext.getServletContext().getRealPath(
				exportPath);
		File file = new File(path);
		file.mkdirs();
		String filePath = path + "/" + UUID.randomUUID().toString() + ".xls";
		exportExcel.outputExcel(filePath);
		try {
			inputStream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return returnSuccess();
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public RepositoryInfo getRepository() {
		return repository;
	}

	public void setRepository(RepositoryInfo repository) {
		this.repository = repository;
	}
	
}
