package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.utils.ExcelUtils;
import com.wzitech.gamegold.common.utils.UUIDUtils;
import com.wzitech.gamegold.facade.backend.contant.WebServerContants;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出购买了保险的订单
 * @author yemq
 */
@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExportInsuranceOrder extends AbstractAction {

    @Autowired
    private IOrderInfoManager orderInfoManager;

    /**
     * 购买了保险的订单EXCEL模板路径
     */
    private static final String EXCEL_TEMPLATE_PATH = "/META-INF/excel/insurance_order_template.xlsx";

    private Date createStartTime;
    private Date createEndTime;
    private Integer orderState;
    private String buyerAccount;
    private String gameName;
    private String orderId;
    private String buyerQq;
    private InputStream inputStream;

    private Map<String, Object> getRequestParams() {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("isBuyInsurance", true);
        paramMap.put("orderState", orderState);
        paramMap.put("userAccount", buyerAccount);
        paramMap.put("orderId", orderId);
        paramMap.put("orderGameName", gameName);
        paramMap.put("buyerQq", buyerQq);
        paramMap.put("configResultIsDel", false);
        paramMap.put("isDelay", null);
        paramMap.put("goodsTypeName", ServicesContants.TYPE_ALL);

        if (orderState == null) {
            paramMap.put("sendStartTime", createStartTime);
            paramMap.put("sendEndTime", WebServerUtil.oneDateLastTime(createEndTime));
        } else if (orderState == OrderState.Delivery.getCode()) {
            paramMap.put("sendStartTime", createStartTime);
            paramMap.put("sendEndTime", WebServerUtil.oneDateLastTime(createEndTime));
        } else if (orderState == OrderState.Statement.getCode()) {
            paramMap.put("statementStartTime", createStartTime);
            paramMap.put("statementEndTime", WebServerUtil.oneDateLastTime(createEndTime));
        }

        return paramMap;
    }

    private List<OrderInfoEO> getOrders() {
        List<OrderInfoEO> orders = orderInfoManager.queryOrderInfo(getRequestParams(), "CREATE_TIME", true);
        return orders;
    }

    /**
     * 统计保费金额
     * @return
     */
    public BigDecimal getStatisticPremiums() {
        BigDecimal totalPreminums = orderInfoManager.statisticPremiums(getRequestParams());
        return totalPreminums;
    }

    public String exportInsuranceOrder() {
        Workbook workbook = null;
        try {
            List<OrderInfoEO> orders = getOrders();

            workbook = ExcelUtils.getCloneWorkBook(EXCEL_TEMPLATE_PATH);
            Sheet sheet = workbook.getSheetAt(0);

            String startDate = DateFormatUtils.format(createStartTime, "yyyy-MM-dd");
            String endDate = DateFormatUtils.format(createEndTime, "yyyy-MM-dd");
            String date = startDate + "至" + endDate;
            sheet.getRow(0).getCell(1).setCellValue(date); // 日期
            sheet.getRow(0).getCell(4).setCellValue(getStatisticPremiums().toString()); //保费总金额

            if (CollectionUtils.isNotEmpty(orders)) {
                int rowIndex = 2;
                for (int i = 0, j = orders.size(); i < j; i++) {
                    OrderInfoEO order = orders.get(i);
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(i+1);
                    row.createCell(1).setCellValue(order.getOrderId());
                    row.createCell(2).setCellValue(order.getUserAccount());
                    row.createCell(3).setCellValue(order.getTotalPrice().doubleValue());
                    row.createCell(4).setCellValue(order.getInsuranceAmount().doubleValue());
                    row.createCell(5).setCellValue(DateFormatUtils.format(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                    if (order.getEndTime() != null) {
                        row.createCell(6).setCellValue(DateFormatUtils.format(order.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
                    }
                }
            }

            String path = WebServerContants.FILES_EXPORT_PATH + "insurance";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }

            String filePath = path + "/" + UUIDUtils.getUUID() + ".xlsx";
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            workbook.write(fos);
            fos.close();
            inputStream = new FileInputStream(filePath);
        } catch (Exception e) {
            logger.error("导出购买了保险的订单出错了", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                }
            }
        }

        return this.returnSuccess();
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBuyerQq() {
        return buyerQq;
    }

    public void setBuyerQq(String buyerQq) {
        this.buyerQq = buyerQq;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }
}
