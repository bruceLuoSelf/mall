package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.common.utils.BeanMapper;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.excel.ExportExcel;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.shorder.business.ISplitRepositoryRequestManager;
import com.wzitech.gamegold.shorder.entity.SplitRepositoryRequest;
import com.wzitech.gamegold.shorder.enums.SplitRepositoryStatus;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 分仓请求
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class SplitRepositoryRequestAction extends AbstractAction{
    private SplitRepositoryRequest splitRepositoryRequest;
    private List<SplitRepositoryRequest> splitRepositoryRequestList;
    private Date startTime;
    private Date endTime;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private  ISplitRepositoryRequestManager splitRepositoryRequestManager;

    public String querySplitRepositoryRequestList(){
        Map<String,Object> queryMap=new HashMap<String, Object>();
        if(splitRepositoryRequest!=null){
            if(StringUtils.isNotBlank(splitRepositoryRequest.getOrderId())){
                queryMap.put("orderId",splitRepositoryRequest.getOrderId());
            }
            if(StringUtils.isNotBlank(splitRepositoryRequest.getBuyerAccount())){
                queryMap.put("buyerAccount",splitRepositoryRequest.getBuyerAccount());
            }
            if(StringUtils.isNotBlank(splitRepositoryRequest.getGameAccount())){
                queryMap.put("gameAccount",splitRepositoryRequest.getGameAccount());
            }
            if(StringUtils.isNotBlank(splitRepositoryRequest.getGameRole())){
                queryMap.put("gameRole",splitRepositoryRequest.getGameRole());
            }
            if(splitRepositoryRequest.getStatus()!=null && splitRepositoryRequest.getStatus()!=-2){
                queryMap.put("status",splitRepositoryRequest.getStatus());
            }
            queryMap.put("createStartTime",startTime);
            queryMap.put("createEndTime", WebServerUtil.oneDateLastTime(endTime));
        }
        GenericPage<SplitRepositoryRequest> genericPage=splitRepositoryRequestManager.queryListInPage(queryMap,
                                          this.limit,this.start,"create_time",false);
        splitRepositoryRequestList=genericPage.getData();

        if(splitRepositoryRequestList!=null){
            for(SplitRepositoryRequest request:splitRepositoryRequestList){
                request.setPwd(null);
                request.setSecondPwd(null);
            }
        }

        totalCount=genericPage.getTotalCount();
        return this.returnSuccess();
    }


    /**
     * 导出
     * */
    public void exportFundExcel(){
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            Map<String, Object> queryParam = new HashMap<String, Object>();
            if(StringUtils.isNotBlank(splitRepositoryRequest.getOrderId())){
                queryParam.put("orderId",splitRepositoryRequest.getOrderId());
            }
            if(StringUtils.isNotBlank(splitRepositoryRequest.getBuyerAccount())){
                queryParam.put("buyerAccount",splitRepositoryRequest.getBuyerAccount());
            }
            if(StringUtils.isNotBlank(splitRepositoryRequest.getGameAccount())){
                queryParam.put("gameAccount",splitRepositoryRequest.getGameAccount());
            }
            if(StringUtils.isNotBlank(splitRepositoryRequest.getGameRole())){
                queryParam.put("gameRole",splitRepositoryRequest.getGameRole());
            }
            if(splitRepositoryRequest.getStatus()!=null && splitRepositoryRequest.getStatus()!=-2){
                queryParam.put("status",splitRepositoryRequest.getStatus());
            }
            queryParam.put("createStartTime", startTime);
            queryParam.put("createEndTime", WebServerUtil.oneDateLastTime(endTime));
            List<SplitRepositoryRequest> eoList = splitRepositoryRequestManager.queryAllForExport(queryParam);
//            List<SplitRepositoryRequest> voList = new ArrayList<SplitRepositoryRequest>();
//            if (CollectionUtils.isNotEmpty(eoList)) {
//                for (SplitRepositoryRequest entityEO : eoList) {
//                    SplitRepositoryRequest entityVO = BeanMapper.map(entityEO, SplitRepositoryRequest.class);
//                    entityVO.setOrderId(entityEO.getOrderId());
//                    entityVO.setBuyerAccount(entityEO.getBuyerAccount());
//                    entityVO.setStatus(entityEO.getStatus());
//                    entityVO.setGameName(entityEO.getGameName());
//                    entityVO.setRegion(entityEO.getRegion());
//                    entityVO.setServer(entityEO.getServer());
//                    entityVO.setGameRace(entityEO.getGameRace());
//                    entityVO.setGameAccount(entityEO.getGameAccount());
//                    entityVO.setGameRole(entityEO.getGameRole());
//                    entityVO.setRealCount(entityEO.getRealCount());
//                    entityVO.setCreateTime(entityEO.getCreateTime());
//                    entityVO.setUpdateTime(entityEO.getUpdateTime());
//                    voList.add(entityVO);
//                }
                export(eoList,response);
//                download(response, path);
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void export(List<SplitRepositoryRequest> voList,HttpServletResponse response) {
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
        font.setFontHeight((short) 400);
        cellStyle.setFont(font);

        // 创建报表头部
        String headString = "分仓主订单表";
        int columnSize = 10;
        exportExcel.createNormalHead(0, headString, columnSize - 1);

        // 创建报表列
        String[] columnHeader = new String[]{"订单号", "收货方账号", "状态", "游戏名", "游戏区",
                "游戏服", "游戏阵营", "游戏账号", "游戏角色名", "分仓总数量(万金)","创建时间","更新时间"};
        exportExcel.createColumHeader(1, columnHeader);

        HSSFCellStyle cellstyle = wb.createCellStyle();
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);

        // 循环创建中间的单元格的各项的值
        if (CollectionUtils.isNotEmpty(voList)) {
            int i = 2;
            for (SplitRepositoryRequest fundVO : voList) {
                HSSFRow row = sheet.createRow((short) i++);
                exportExcel.cteateCell(wb, row, 0,cellstyle, fundVO.getOrderId());
                exportExcel.cteateCell(wb, row, 1, cellstyle, fundVO.getBuyerAccount());
                if (fundVO.getStatus() ==1){
                    exportExcel.cteateCell(wb, row, 2, cellstyle,SplitRepositoryStatus.SPLITTING.getStatus());
                }else if (fundVO.getStatus()==2){
                    exportExcel.cteateCell(wb, row, 2, cellstyle, SplitRepositoryStatus.SUCCESS.getStatus());
                }else if (fundVO.getStatus()==3){
                    exportExcel.cteateCell(wb, row, 2, cellstyle, SplitRepositoryStatus.FAIL.getStatus());
                }else if (fundVO.getStatus()==4){
                    exportExcel.cteateCell(wb, row, 2, cellstyle, SplitRepositoryStatus.PART_SPLIT.getStatus());
                }
                exportExcel.cteateCell(wb, row, 3, cellstyle, fundVO.getGameName());
                exportExcel.cteateCell(wb, row, 4, cellstyle, fundVO.getRegion());
                exportExcel.cteateCell(wb, row, 5, cellstyle, fundVO.getServer());
                exportExcel.cteateCell(wb, row, 6, cellstyle, fundVO.getGameRace());
                exportExcel.cteateCell(wb, row, 7, cellstyle, fundVO.getGameAccount());
                exportExcel.cteateCell(wb, row, 8, cellstyle, fundVO.getGameRole());
                exportExcel.cteateCell(wb, row, 9, cellstyle, String.valueOf(fundVO.getRealCount()));
                if (fundVO.getCreateTime() != null)
                    exportExcel.cteateCell(wb, row, 10, cellstyle, String.valueOf(dataFormat.format(fundVO.getCreateTime())));
                if (fundVO.getUpdateTime() != null)
                    exportExcel.cteateCell(wb, row, 11, cellstyle, String.valueOf(dataFormat.format(fundVO.getUpdateTime())));
            }
        }
        response.setContentType("application/msexcel");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date());
        response.addHeader("Content-Disposition", "attachment; filename=\"FundStatisticsExcel" + format + ".xls\"");
        try {
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public SplitRepositoryRequest getSplitRepositoryRequest() {
        return splitRepositoryRequest;
    }

    public void setSplitRepositoryRequest(SplitRepositoryRequest splitRepositoryRequest) {
        this.splitRepositoryRequest = splitRepositoryRequest;
    }

    public List<SplitRepositoryRequest> getSplitRepositoryRequestList() {
        return splitRepositoryRequestList;
    }

    public void setSplitRepositoryRequestList(List<SplitRepositoryRequest> splitRepositoryRequestList) {
        this.splitRepositoryRequestList = splitRepositoryRequestList;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
