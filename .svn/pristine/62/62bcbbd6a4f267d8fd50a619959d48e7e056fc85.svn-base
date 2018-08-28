package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.common.utils.ExcelUtils;
import com.wzitech.gamegold.common.utils.UUIDUtils;
import com.wzitech.gamegold.facade.backend.contant.WebServerContants;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.dto.SimpleOrderDTO;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.util.*;

/**
 * 导出已取消、已退款的订单
 */
@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExportOrderCancelAction extends AbstractAction {
    @Autowired
    private IOrderInfoManager orderInfoManager;

    private Date createStartTime;
    private Date createEndTime;
    private Integer orderState;
    private String gameName;
    private InputStream inputStream;

    /**
     * 已取消订单EXCEL模板路径
     */
    private static final String EXCEL_TEMPLATE_PATH = "/META-INF/excel/order_cancel_template.xlsx";

    private List<SimpleOrderDTO> queryOrders() {
        if (createStartTime == null || createEndTime == null) {
            Calendar c = Calendar.getInstance();
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 00, 00, 00);
            createStartTime = c.getTime();

            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
            c.set(Calendar.MILLISECOND, 999);
            createEndTime = c.getTime();
        }

        if (orderState == null) {
            orderState = OrderState.Cancelled.getCode();
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("createStartTime", createStartTime);
        paramMap.put("createEndTime", WebServerUtil.oneDateLastTime(createEndTime));
        paramMap.put("orderState", orderState);
        paramMap.put("gameName", gameName);
        return orderInfoManager.selectOrderByMap(paramMap);
    }


    public String exportOrder() {
        Workbook workbook = null;
        try {
            List<SimpleOrderDTO> orders = queryOrders();

            workbook = ExcelUtils.getCloneWorkBook(EXCEL_TEMPLATE_PATH);
            Sheet sheet = workbook.getSheetAt(0);

            String startDate = DateFormatUtils.format(createStartTime, "yyyy-MM-dd");
            String endDate = DateFormatUtils.format(createEndTime, "yyyy-MM-dd");
            String date = startDate + "至" + endDate;
            sheet.getRow(0).getCell(1).setCellValue(date);

            if (CollectionUtils.isNotEmpty(orders)) {
                int rowIndex = 3;
                for (int i = 0, j = orders.size(); i < j; i++) {
                    SimpleOrderDTO order = orders.get(i);
                    Row row = sheet.createRow(rowIndex++);

                    String game = order.getGameName() + "/" + order.getRegion() + "/" + order.getServer();
                    if (StringUtils.isNotBlank(order.getGameRace())) {
                        game += "/" + order.getGameRace();
                    }

                    row.createCell(0).setCellValue(i + 1);
                    row.createCell(1).setCellValue(order.getOrderId());
                    row.createCell(2).setCellValue(order.getTitle());
                    row.createCell(3).setCellValue(game);
                    row.createCell(4).setCellValue(order.getReceiver());
                    row.createCell(5).setCellValue(order.getGoldCount());
                    row.createCell(6).setCellValue(order.getMoneyName());
                    row.createCell(7).setCellValue(order.getUnitPrice().doubleValue());
                    row.createCell(8).setCellValue(order.getTotalPrice().doubleValue());
                    row.createCell(9).setCellValue(order.getMobileNumber());
                    row.createCell(10).setCellValue(order.getQq());
                    row.createCell(11).setCellValue(order.getBuyerAccount());
                    row.createCell(12).setCellValue(order.getSellerAccount());
                    row.createCell(13).setCellValue(order.getServiceAccount());
                    row.createCell(14).setCellValue(order.getServiceName());
                    row.createCell(15).setCellValue(OrderState.getTypeByCode(order.getOrderState()).getName());
                    row.createCell(16).setCellValue(DateFormatUtils.format(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                    if (order.getPayTime() != null) {
                        row.createCell(17).setCellValue(DateFormatUtils.format(order.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    if (order.getEndTime() != null) {
                        row.createCell(18).setCellValue(DateFormatUtils.format(order.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
                    }
                }
            }

            String path = ServletActionContext.getServletContext().getRealPath(WebServerContants.FILES_EXPORT_PATH);
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
            logger.error("导出取消的订单出错了", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                }
            }
        }
        return returnSuccess();
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

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
