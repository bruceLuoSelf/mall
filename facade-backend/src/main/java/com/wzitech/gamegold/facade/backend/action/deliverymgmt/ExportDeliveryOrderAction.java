package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.gamegold.common.enums.DeliveryOrderStatus;
import com.wzitech.gamegold.common.enums.ShDeliveryTypeEnum;
import com.wzitech.gamegold.facade.backend.contant.WebServerContants;
import com.wzitech.gamegold.facade.backend.excel.ExportExcel;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/1/18.
 */
@Controller
@Scope("prototype")
public class ExportDeliveryOrderAction extends AbstractAction {

    private DeliveryOrder deliveryOrder;

    private String isTimeOut;

    private Date startTime;

    private Date endTime;

    private InputStream inputStream;

    @Autowired
    IDeliveryOrderManager deliveryOrderManager;

    private Map<String, Object> getQueryMap() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if(deliveryOrder!=null) {
            if(StringUtils.isNotBlank(deliveryOrder.getBuyerAccount())){
                queryMap.put("buyerAccount", deliveryOrder.getBuyerAccount().trim());
            }
            if(StringUtils.isNotBlank(deliveryOrder.getSellerAccount())){
                queryMap.put("sellerAccount", deliveryOrder.getSellerAccount().trim());
            }
            if(StringUtils.isNotBlank(deliveryOrder.getGameName())){
                queryMap.put("gameName", deliveryOrder.getGameName().trim());
            }
            if(StringUtils.isNotBlank(deliveryOrder.getRegion())){
                queryMap.put("region", deliveryOrder.getRegion().trim());
            }
            if(StringUtils.isNotBlank(deliveryOrder.getServer())){
                queryMap.put("server", deliveryOrder.getServer().trim());
            }
            if(StringUtils.isNotBlank(deliveryOrder.getGameRace())){
                queryMap.put("gameRace", deliveryOrder.getGameRace().trim());
            }
            if(StringUtils.isNotBlank(deliveryOrder.getOrderId())){
                queryMap.put("orderId", deliveryOrder.getOrderId().trim());
            }
            if(deliveryOrder.getStatus()!=null && deliveryOrder.getStatus()!=0){
                queryMap.put("status", deliveryOrder.getStatus());
            }
            if(deliveryOrder.getTransferStatus()!=null && deliveryOrder.getTransferStatus()!=-1){
                queryMap.put("transferStatus", deliveryOrder.getTransferStatus());
            }
            if(deliveryOrder.getDeliveryType()!=null && deliveryOrder.getDeliveryType()!=0){
                queryMap.put("deliveryType",deliveryOrder.getDeliveryType());
            }
            if(StringUtils.isNotBlank(deliveryOrder.getTradeTypeName())&&!deliveryOrder.getTradeTypeName().equals("全部")){
                queryMap.put("tradeTypeName",deliveryOrder.getTradeTypeName());
            }
            if(isTimeOut!=null&&!isTimeOut.equals("-1")){
                queryMap.put("isTimeout",isTimeOut.equals("true"));
            }
            queryMap.put("createStartTime",startTime);
            queryMap.put("createEndTime", WebServerUtil.oneDateLastTime(endTime));
            queryMap.put("ORDERBY", "create_time");
            queryMap.put("ORDER", "DESC");

        }

        return queryMap;
    }

    public String exportDeliveryOrderList() {

        Map<String, Object> queryMap = getQueryMap();
        List<DeliveryOrder> deliveryOrderList = deliveryOrderManager.selectAllorderByMap(queryMap);
        deliveryOrder= deliveryOrderManager.statisAmount(queryMap);

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
        // 创建报表列
        String[] columHeader = new String[] { "游戏", "游戏区", "游戏服", "订单号",
                "收购方", "出货方", "单价", "计划收购数量", "计划收购金额", "实际收购数量", "实际收购金额",
                "订单状态", "收货方式","交易方式", "是否超时", "转账状态", "创建时间" };

        String headString = "出货订单列表";
        exportExcel.createNormalHead(0, headString, columHeader.length-1);
        exportExcel.createNormalHead(1, "计划出货金额汇总：￥"+deliveryOrder.getAmount()+" 实际出货金额汇总：￥"+deliveryOrder.getRealAmount()+" 笔数："+deliveryOrder.getCount()+" 佣金：0",columHeader.length-1);

        exportExcel.createColumHeader(2, columHeader);

        HSSFCellStyle cellstyle = wb.createCellStyle();
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);

        // 循环创建中间的单元格的各项的值
        if (deliveryOrderList != null) {
            int i = 3;
            for (DeliveryOrder deliveryOrder : deliveryOrderList) {
                HSSFRow row = sheet.createRow((short) i++);
                exportExcel.cteateCell(wb, row, (short) 0,
                        cellstyle,
                        deliveryOrder.getGameName());
                exportExcel.cteateCell(wb, row, (short) 1,
                        cellstyle,
                        deliveryOrder.getRegion());
                exportExcel.cteateCell(wb, row, (short) 2,
                        cellstyle,
                        deliveryOrder.getServer());
                exportExcel.cteateCell(wb, row, (short) 3,
                        cellstyle,
                        deliveryOrder.getOrderId());
                exportExcel.cteateCell(wb, row, (short) 4,
                        cellstyle, deliveryOrder.getBuyerAccount()
                                );
                exportExcel.cteateCell(wb, row, (short) 5,
                        cellstyle, deliveryOrder.getSellerAccount()
                );

                String Price = "";
                if (deliveryOrder.getPrice() != null) {
                    Price =   deliveryOrder.getPrice().toString();
                }
                exportExcel.cteateCell(wb, row, (short) 6,
                        cellstyle, Price);
                exportExcel.cteateCell(wb, row, (short) 7,
                        cellstyle, deliveryOrder.getCount().toString());
                exportExcel.cteateCell(wb, row, (short) 8,
                        cellstyle, deliveryOrder.getAmount().toString());
                exportExcel.cteateCell(wb, row, (short) 9,
                        cellstyle, deliveryOrder.getRealCount().toString());
                exportExcel.cteateCell(wb, row, (short) 10,
                        cellstyle, deliveryOrder.getRealAmount().toString());

                exportExcel.cteateCell(wb, row, (short) 11,
                        cellstyle, DeliveryOrderStatus.getTypeByCode(deliveryOrder.getStatus()).getName());
                exportExcel.cteateCell(wb, row, (short) 12,
                        cellstyle, ShDeliveryTypeEnum.getNameByCode(deliveryOrder.getDeliveryType()));
                exportExcel.cteateCell(wb, row, (short) 13,
                        cellstyle, deliveryOrder.getTradeTypeName());
                exportExcel.cteateCell(wb, row, (short) 14,
                            cellstyle,deliveryOrder.getIsTimeout()!=null&&deliveryOrder.getIsTimeout()?"已超时":"" );
    String transferStatusStr = deliveryOrder.getTransferStatus()==DeliveryOrder.NOT_TRANSFER?"未转账":
            (deliveryOrder.getTransferStatus()==DeliveryOrder.WAIT_TRANSFER?"等待转账":"已转账");
                exportExcel.cteateCell(wb, row, (short) 15,
                        cellstyle, transferStatusStr);


                String createTime = "";
                if(deliveryOrder.getCreateTime()!=null){
                    createTime = format.format(deliveryOrder.getCreateTime());
                }
                exportExcel.cteateCell(wb, row, (short) 16,
                        cellstyle,
                        createTime);


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

    public DeliveryOrder getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public String getIsTimeOut() {
        return isTimeOut;
    }

    public void setIsTimeOut(String isTimeOut) {
        this.isTimeOut = isTimeOut;
    }
}
