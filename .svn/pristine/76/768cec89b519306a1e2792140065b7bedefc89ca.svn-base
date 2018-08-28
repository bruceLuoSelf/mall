package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.dto.BaseResponse;
import com.wzitech.gamegold.facade.frontend.service.excel.ExportExcel;
import com.wzitech.gamegold.facade.frontend.service.shorder.ISplitRepositoryService;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.SplitRepositoryOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.SplitRepositoryOrderResponse;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.entity.*;
import com.wzitech.gamegold.shorder.enums.SplitRepositoryStatus;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ljn
 * @date 2018/6/12.
 * 分仓
 */
@Service("SplitRepositoryService")
@Path("splitRepository")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class SplitRepositoryServiceImpl extends AbstractBaseService implements ISplitRepositoryService {

    @Autowired
    private ISplitRepositoryRequestManager splitRepositoryRequestManager;

    @Autowired
    private IDeliveryOrderManager deliveryOrderManager;

    @Autowired
    private ISplitRepositorySubRequestManager splitRepositorySubRequestManager;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String FILES_EXPORT_PATH = "/srv/export/";

    @Path("queryOrderList")
    @POST
    @Override
    public IServiceResponse queryOrderList(@RequestBody SplitRepositoryOrderRequest repositoryRequest,@Context HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try{
            if (repositoryRequest.getPage() == null) {
                repositoryRequest.setPage(1);
            }
            if (repositoryRequest.getPageSize() == null) {
                repositoryRequest.setPageSize(10);
            }
            int start = (repositoryRequest.getPage()-1) * repositoryRequest.getPageSize();
            Map<String, Object> map = dealParams(repositoryRequest);
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }
            map.put("buyerAccount",userInfo.getLoginAccount());
            GenericPage<SplitRepositoryRequest> page = splitRepositoryRequestManager.queryListInPage(map, repositoryRequest.getPageSize(), start, "create_time", false);
            List<SplitRepositoryRequest> data = page.getData();
            emptyPwd(data);
            response.setTotalCount(page.getTotalCount());
            response.setPageIndex(repositoryRequest.getPage());
            response.setPageSize(repositoryRequest.getPageSize());
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
            response.setData(data);
        }catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("查询分仓订单列表发生异常:{}", e);
        }catch (Exception e){
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("查询分仓订单列表发生未知异常:{}", e);
        }
        return response;
    }

    private List<SplitRepositoryRequest> emptyPwd(List<SplitRepositoryRequest> data) {
        if (CollectionUtils.isNotEmpty(data)) {
            for (SplitRepositoryRequest order : data) {
                order.setFmsRoleName(null);
                order.setBuyerAccount(null);
                order.setGameAccount(null);
                order.setPwd(null);
                order.setRobotOtherReason(null);
                order.setRobotReason(null);
                order.setSecondPwd(null);
                order.setSplitReason(null);
                order.setTel(null);
            }
        }
        return data;
    }

    private Map<String, Object> dealParams(SplitRepositoryOrderRequest repositoryRequest) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        if (StringUtils.isNotBlank(repositoryRequest.getCreateStartTime())) {
            map.put("createStartTime",sdf.parse(repositoryRequest.getCreateStartTime()));
        }
        if (StringUtils.isNotBlank(repositoryRequest.getCreateEndTime())) {
            map.put("createEndTime",sdf.parse(repositoryRequest.getCreateEndTime()));
        }
        map.put("gameName",repositoryRequest.getGameName());
        map.put("region",repositoryRequest.getRegionName());
        map.put("server",repositoryRequest.getServerName());
        map.put("orderId",repositoryRequest.getSplitRepositoryOrderNo());
        map.put("shOrderId",repositoryRequest.getShOrderId());
        String status = repositoryRequest.getStatus();
        if (StringUtils.isNotBlank(status) && !status.equals("不限")) {
            map.put("status",Integer.valueOf(status));
        }
        map.put("gameAccount",repositoryRequest.getGameAccount());
        map.put("gameRole",repositoryRequest.getGameRole());
        map.put("splitRepositoryRole",repositoryRequest.getSplitRepositoryRole());
        return map;
    }

    @Path("queryOrderDetails")
    @GET
    @Override
    public IServiceResponse queryOrderDetails(@QueryParam("orderNo")String orderNo) {
        SplitRepositoryOrderResponse response = new SplitRepositoryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try{
            String loginAccount = CurrentUserContext.getUser().getLoginAccount();
            if (StringUtils.isBlank(loginAccount)) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
            }
            if (StringUtils.isBlank(orderNo)) {
                throw new SystemException(ResponseCodes.EmptyOrderId.getCode(),ResponseCodes.EmptyOrderId.getMessage());
            }
            //分仓订单
            SplitRepositoryRequest order = splitRepositoryRequestManager.queryByOrderId(orderNo);
            if (order == null) {
                throw new SystemException(ResponseCodes.OrderLogIdInvalid.getCode(),ResponseCodes.OrderLogIdInvalid.getMessage());
            }
            List<SplitRepositorySubRequest> subOrderList = splitRepositorySubRequestManager.getSubOrderList(order.getOrderId());
            Long useRepertoryCount = 0L;
            if (CollectionUtils.isNotEmpty(subOrderList)) {
                for (SplitRepositorySubRequest subOrder : subOrderList) {
                    useRepertoryCount += subOrder.getUseRepertoryCount() == null ? 0 : subOrder.getUseRepertoryCount();
                }
            }
            response.setUseRepertoryCount(useRepertoryCount);
            response.setOrder(order);
            response.setSubOrderList(subOrderList);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        }catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("查询分仓订单详情发生异常:{}", e);
        }catch (Exception e){
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("查询分仓订单详情发生未知异常:{}", e);
        }
        return response;
    }

    /**
     * 导出分仓订单列表
     * @param repositoryRequest
     * @param request
     * @return
     */
    @Path("export")
    @GET
    @Override
    public IServiceResponse export(@QueryParam("") SplitRepositoryOrderRequest repositoryRequest, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        BaseResponse baseResponse = new BaseResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        baseResponse.setResponseStatus(responseStatus);
        try{
            Map<String, Object> map = dealParams(repositoryRequest);
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return baseResponse;
            }
            map.put("buyerAccount",userInfo.getLoginAccount());
            List<SplitRepositoryRequest> list = splitRepositoryRequestManager.selectByMap(map,"create_time",false);
            emptyPwd(list);
            String path = exportOrder(list);
            download(response,path);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        }catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("导出分仓订单列表发生异常:{}", e);
        }catch (Exception e){
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("导出分仓订单列表发生未知异常:{}", e);
        }
        return baseResponse;
    }

    private String exportOrder(List<SplitRepositoryRequest> list) {
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
        String headString = "分仓订单列表";
        int columnSize = 5;
        exportExcel.createNormalHead(0, headString, columnSize - 1);

        String[] columnHeader = new String[]{
                "创建时间",
                "分仓订单号",
                "状态",
                "分仓数量（万金）",
                "关联收获订单"
        };
        exportExcel.createColumHeader(1, columnHeader);

        HSSFCellStyle cellstyle = wb.createCellStyle();
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);

        //设置列的宽度
        sheet.setColumnWidth(0,columnSize*10*100);
        sheet.setColumnWidth(1,columnSize*10*90);
        sheet.setColumnWidth(2,columnSize*10*80);
        sheet.setColumnWidth(3,columnSize*10*80);
        sheet.setColumnWidth(4,columnSize*10*90);

        // 循环创建中间的单元格的各项的值

        if (CollectionUtils.isNotEmpty(list)) {
            int i = 2;
            for (SplitRepositoryRequest order : list) {
                HSSFRow row = sheet.createRow((short) i++);
                //创建时间
                String createTime = order.getCreateTime() == null ? "" : sdf.format(order.getCreateTime());
                //分仓订单号
                String orderId = order.getOrderId();
                //状态
                String status = SplitRepositoryStatus.getSplitRepositoryStatus(order.getStatus()).getStatus();
                //实际分仓数量（万金）
                String count = order.getRealCount() == null ? "" :order.getRealCount().toString();
                //关联收获订单
                String shOrderId = order.getShOrderId();

                exportExcel.createCell(wb, row, 0, CellStyle.ALIGN_CENTER, createTime);
                exportExcel.createCell(wb, row, 1, CellStyle.ALIGN_CENTER, orderId);
                exportExcel.createCell(wb, row, 2, CellStyle.ALIGN_CENTER, status);
                exportExcel.createCell(wb, row, 3, CellStyle.ALIGN_CENTER, count);
                exportExcel.createCell(wb, row, 4, CellStyle.ALIGN_CENTER, shOrderId);

            }
        }

        File file = new File(FILES_EXPORT_PATH);
        file.mkdirs();
        String filePath = FILES_EXPORT_PATH + "/" + UUID.randomUUID().toString() + ".xls";
        exportExcel.outputExcel(filePath);
        return filePath;
    }

    private void download(HttpServletResponse response ,String path) throws IOException {
        InputStream in = new FileInputStream(path);
        response.reset();
        response.setContentType("application/msexcel");
        response.addHeader("Content-Disposition","attachment;fileName="+new String("分仓订单表".getBytes(),"iso-8859-1")+".xls");
        OutputStream out = response.getOutputStream();
        try {
            byte[] b = new byte[1024];
            int len ;
            while ((len = in.read(b)) > 0) {
                out.write(b,0,len);
            }
        } finally {
            out.flush();
            out.close();
            if (in != null) {
                in.close();
            }
        }
    }
}
