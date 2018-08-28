package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.common.utils.ExcelUtils;
import com.wzitech.gamegold.common.utils.UUIDUtils;
import com.wzitech.gamegold.facade.backend.contant.WebServerContants;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.order.business.IOrderConfigManager;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.*;

/**
 * 导出超时的订单
 *
 * @author yemq
 */
@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExportDelayOrderAction extends AbstractAction {
    @Autowired
    private IOrderInfoManager orderInfoManager;
    @Autowired
    private IUserInfoManager userInfoManager;
    @Autowired
    private IOrderConfigManager orderConfigManager;

    /**
     * 订单超时EXCEL模板路径
     */
    private static final String EXCEL_TEMPLATE_PATH = "/META-INF/excel/order_delay_template.xlsx";

    private Date createStartTime;
    private Date createEndTime;
    private String sellerAccount;
    private String buyerAccount;
    private Integer orderState;
    private String gameName;
    private String orderId;
    private String detailServiceAccount;
    private InputStream inputStream;


    /**
     * 查询超时的订单
     *
     * @return
     */
    private List<OrderInfoEO> queryDelayOrder() {
        if (createStartTime == null) {
            Calendar c = Calendar.getInstance();
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 00, 00, 00);
            createStartTime = c.getTime();
        }
        if (createEndTime == null) {
            Calendar c = Calendar.getInstance();
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
            createEndTime = c.getTime();
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        int userType = CurrentUserContext.getUserType();
        if (UserType.SystemManager.getCode() != userType) {
            if (UserType.MakeOrder.getCode() == userType || UserType.RecruitBusiness.getCode() == userType) {
                UserInfoEO user = (UserInfoEO) CurrentUserContext.getUser();
                paramMap.put("servicerId", user.getMainAccountId());
            } else {
                paramMap.put("servicerId", CurrentUserContext.getUid());
            }
        } else {
            if (StringUtils.isNotEmpty(detailServiceAccount)) {
                UserInfoEO servicer = userInfoManager.findUserByAccount(detailServiceAccount);
                if (servicer != null) {
                    paramMap.put("servicerId", servicer.getId());
                } else {
                    paramMap.put("servicerId", 0L);
                }
            }
        }
        paramMap.put("createStartTime", createStartTime);
        paramMap.put("createEndTime", WebServerUtil.oneDateLastTime(createEndTime));
        paramMap.put("sellerAccount", sellerAccount);
        paramMap.put("userAccount", buyerAccount);
        paramMap.put("orderState", orderState);
        paramMap.put("orderId", orderId);
        paramMap.put("orderGameName", gameName);
        paramMap.put("configResultIsDel", false);
        paramMap.put("isDelay", true);
        paramMap.put("goodsTypeName", ServicesContants.TYPE_ALL);
        return orderInfoManager.queryOrderInfo(paramMap, "CREATE_TIME", true);
    }

    /**
     * 导出超时的订单
     *
     * @return
     */
    public String exportDelayOrder() {
        Workbook workbook = null;
        try {
            List<OrderInfoEO> orders = queryDelayOrder();

            workbook = ExcelUtils.getCloneWorkBook(EXCEL_TEMPLATE_PATH);
            Sheet sheet = workbook.getSheetAt(0);

            String startDate = DateFormatUtils.format(createStartTime, "yyyy-MM-dd");
            String endDate = DateFormatUtils.format(createEndTime, "yyyy-MM-dd");
            String date = startDate + "至" + endDate;
            sheet.getRow(0).getCell(1).setCellValue(date);

            if (CollectionUtils.isNotEmpty(orders)) {
                int rowIndex = 3;
                for (int i = 0, j = orders.size(); i < j; i++) {
                    OrderInfoEO order = orders.get(i);
                    Row row = sheet.createRow(rowIndex++);

                    String game = order.getGameName() + "/" + order.getRegion() + "/" + order.getServer();
                    if (StringUtils.isNotBlank(order.getGameRace())) {
                        game += order.getGameRace();
                    }

                    row.createCell(0).setCellValue(i + 1);
                    row.createCell(1).setCellValue(order.getOrderId());
                    row.createCell(2).setCellValue(order.getTotalPrice().doubleValue());
                    row.createCell(3).setCellValue(order.getDeliveryTime());
                    if (order.getSendTime() != null) {
                        Date sendTime = order.getSendTime();
                        Date payTime = order.getPayTime();
                        long time = sendTime.getTime() - payTime.getTime();
                        time = time / 60000; // 发货用时(分钟)
                        row.createCell(4).setCellValue(time);
                    }
                    row.createCell(5).setCellValue(DateFormatUtils.format(order.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
                    if (order.getSendTime() != null) {
                        row.createCell(6).setCellValue(DateFormatUtils.format(order.getSendTime(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    row.createCell(7).setCellValue(game);
                    row.createCell(8).setCellValue(order.getUserAccount());
                    row.createCell(9).setCellValue(order.getQq());
                    row.createCell(10).setCellValue(order.getMobileNumber());

                    String sellerLoginAccounts = "";
                    List<ConfigResultInfoEO> configResultList = orderConfigManager.orderConfigList(order.getOrderId());
                    for (int ii = 0, jj = configResultList.size(); ii < jj; ii++) {
                        ConfigResultInfoEO result = configResultList.get(ii);
                        sellerLoginAccounts += result.getLoginAccount();
                        if (ii != (jj - 1)) {
                            sellerLoginAccounts += ",";
                        }
                    }
                    row.createCell(11).setCellValue(sellerLoginAccounts);
                    row.createCell(12).setCellValue(order.getServicerInfo().getRealName());
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
            logger.error("导出超时的订单出错了", e);
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

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setDetailServiceAccount(String detailServiceAccount) {
        this.detailServiceAccount = detailServiceAccount;
    }
}
