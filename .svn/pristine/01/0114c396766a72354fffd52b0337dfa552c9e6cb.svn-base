package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.google.common.collect.Lists;
import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.ShDeliveryTypeEnum;
import com.wzitech.gamegold.facade.frontend.accessLimit.AccessLimit;
import com.wzitech.gamegold.facade.frontend.service.chorder.dto.DeliveryOrderResponse;
import com.wzitech.gamegold.facade.frontend.service.excel.ExportExcel;
import com.wzitech.gamegold.facade.frontend.service.shorder.IGameAccountService;
import com.wzitech.gamegold.facade.frontend.service.shorder.ISellerData;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseOrderResponse;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.entity.GameAccount;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 收货角色信息
 */
@Service("GameAccountService")
@Path("gameAccount")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class GameAccountServiceImpl extends AbstractBaseService implements IGameAccountService {
    @Autowired
    IGameAccountManager gameAccountManager;

    @Autowired
    IPurchaseOrderManager purchaseOrderManager;

    @Autowired
    ISellerManager sellerManager;

    @Autowired
    IGameAccountProcessor gameAccountProcessor;

    @Autowired
    ISystemConfigManager systemConfigManager;

    @Autowired
    IPurchaserDataManager purchaserDataManager;

    @Autowired
    IShGameConfigManager shGameConfigManager;


    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private InputStream inputStream;

    @Autowired
    ISellerData sellerData;


    @Path("queryGameAccountList")
    @GET
    @Override
    public IServiceResponse queryGameAccountList(@QueryParam("") PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {
            // 获取当前用户
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }

            // 检查卖家信息
            if (!sellerData.checkSeller(response)) {
                return response;
            }

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("gameName", purchaseOrderRequest.getGameName());
            paramMap.put("region", purchaseOrderRequest.getRegion());
            paramMap.put("server", purchaseOrderRequest.getServer());
            paramMap.put("gameRace", purchaseOrderRequest.getGameRace());
            paramMap.put("buyerAccount", userInfo.getLoginAccount());

            List<GameAccount> list = gameAccountManager.queryGameAccount(paramMap);
            response.setGameAccountList(list);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取收货的账号角色发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取收货的账号角色发生未知异常:{}", e);
        }

        return response;
    }

    /**
     * 上传机器收货库存excel文件
     *
     * @param in
     * @param request
     * @return
     */
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(value = {"text/html;charset=UTF-8", "application/json;charset=UTF-8"})
    @AccessLimit
    @POST
    @Override
    public PurchaseOrderResponse upload(@Multipart("excelFile") InputStream in,
                                        @Context HttpServletRequest request) {
        logger.info("开始处理上传采购单excel文件");
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        //检查收货商资金
        if (!sellerData.checkSellerAmount(response)) {
            return response;
        }
        // 检查卖家信息
        if (!sellerData.checkSeller(response)) {
            return response;
        }

        if (in == null) {
            responseStatus.setStatus(ResponseCodes.EmptyUploadFile.getCode(), ResponseCodes.EmptyUploadFile.getMessage());
            return response;
        }

        try {
            gameAccountProcessor.process(in, ShDeliveryTypeEnum.Robot.getCode());
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            //捕获系统异常
            String msg = null;
            if (e.getArgs() == null || e.getArgs().length == 0) {
                msg = ResponseCodes.getResponseByCode(e.getErrorCode()).getMessage();
            } else {
                msg = e.getArgs()[0];
            }
            responseStatus.setStatus(e.getErrorCode(), msg);
            logger.error("处理上传的采购单excel文件出错了", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("处理上传的采购单excel文件出错了", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("结束处理上传的采购单excel文件");
        return response;
    }

    /**
     * 上传手工收货库存excel文件
     *
     * @param in
     * @param request
     * @return
     */
    @Path("uploadManual")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(value = {"text/html;charset=UTF-8", "application/json;charset=UTF-8"})
    @AccessLimit
    @POST
    @Override
    public PurchaseOrderResponse uploadManual(@Multipart("excelFile") InputStream in,
                                              @Context HttpServletRequest request) {
        logger.info("开始处理上传采购单excel文件");
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        //检查收货商资金
        if (!sellerData.checkSellerAmount(response)) {
            return response;
        }
        // 检查卖家信息
        if (!sellerData.checkSeller(response)) {
            return response;
        }

        if (in == null) {
            responseStatus.setStatus(ResponseCodes.EmptyUploadFile.getCode(), ResponseCodes.EmptyUploadFile.getMessage());
            return response;
        }

        try {
            gameAccountProcessor.process(in, ShDeliveryTypeEnum.Manual.getCode());
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            //捕获系统异常
            String msg = null;
            if (e.getArgs() == null || e.getArgs().length == 0) {
                msg = ResponseCodes.getResponseByCode(e.getErrorCode()).getMessage();
            } else {
                msg = e.getArgs()[0];
            }
            responseStatus.setStatus(e.getErrorCode(), msg);
            logger.error("处理上传的采购单excel文件出错了", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("处理上传的采购单excel文件出错了", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("结束处理上传的采购单excel文件");
        return response;
    }

    /**
     * 分页查询账号库存
     *
     * @param purchaseOrderRequest
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @Path("queryRepositoryGameAccountList")
    @GET
    @Override
    public IServiceResponse queryRepositoryGameAccountList(@QueryParam("") PurchaseOrderRequest purchaseOrderRequest, @QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize, @Context HttpServletRequest request) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {
            // 获取当前用户
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }
            // 检查卖家信息
            // 检查卖家信息
            if (!sellerData.checkSellerForRecharge(response)) {
                return response;
            }

            if (page == null || page <= 0) {
                page = 1;
            }
            if (pageSize == null || pageSize <= 0) {
                pageSize = 25;
            }
//            else if (pageSize > 100) {
//                //防止大批量的查询
//                pageSize = 100;
//            }
            int start = (page - 1) * pageSize;

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("gameName", purchaseOrderRequest.getGameName());
            paramMap.put("region", purchaseOrderRequest.getRegion());
            paramMap.put("server", purchaseOrderRequest.getServer());
            paramMap.put("gameRace", purchaseOrderRequest.getGameRace());
            paramMap.put("buyerAccount", userInfo.getLoginAccount());
            paramMap.put("gameAccount", purchaseOrderRequest.getGameAccount());
            paramMap.put("roleName", purchaseOrderRequest.getRoleName());
            paramMap.put("isSale",purchaseOrderRequest.getSale());

            List<SortField> sortFields = Lists.newArrayList();
            sortFields.add(new SortField("game_name", SortField.ASC));
            sortFields.add(new SortField("region", SortField.ASC));
            sortFields.add(new SortField("server", SortField.ASC));

            ShGameConfig shGameConfig = shGameConfigManager.getConfigByGameName(purchaseOrderRequest.getGameName(), ServicesContants.GOODS_TYPE_GOLD, true, true);
            if (shGameConfig == null || shGameConfig.getMailFee() == null) {
                throw new SystemException(ResponseCodes.NotAvailableConfig.getCode(), ResponseCodes.NotAvailableConfig.getMessage());
            }
            GenericPage<GameAccount> genericPage = gameAccountManager.queryListInPage(paramMap, start, pageSize, sortFields);
            List<GameAccount> list = genericPage.getData();
            if (list != null && list.size() > 0) {
                for (GameAccount gameAccount : list) {
                    Long tradeQuota = (long) gameAccount.getLevel() * gameAccount.getLevel();
                    gameAccount.setTradeQuota(tradeQuota);
                    Long repositoryQuota = (long) (tradeQuota * (1 + shGameConfig.getMailFee()));
                    gameAccount.setRepositoryQuota(repositoryQuota);
                    if (null == gameAccount.getTodaySaleCount()) {
                        gameAccount.setTodaySaleCount(0L);
                    }
                    if (null == gameAccount.getRepositoryCount()) {
                        gameAccount.setRepositoryCount(0L);
                    }
                    Long repositoryGaps = repositoryQuota - gameAccount.getRepositoryCount() - gameAccount.getTodaySaleCount();
                    if (repositoryGaps < 0) {
                        repositoryGaps = 0L;
                    }
                    gameAccount.setRepositoryGaps(repositoryGaps);
                    gameAccount.setGamePwd(null);
                    gameAccount.setSecondPwd(null);
                    if (gameAccount.getIsSale() == null) {
                        gameAccount.setIsSale(false);
                    }
                }
            }
            response.setGameAccountList(list);
            response.setTotalCount(genericPage.getTotalCount());
            response.setPageSize(pageSize);
            response.setCurrPage(page);
            response.setTotalPage(genericPage.getTotalPageCount());
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("分页查询账号库存发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("分页查询账号库存发生未知异常:{}", e);
        }

        return response;
    }

    @Path("updategamePwd")
    @POST
    @Override
    public IServiceResponse updateGamePwd(PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            // 获取当前用户
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }
            // 检查卖家信息
            if (!sellerData.checkSeller(response)) {
                return response;
            }
            Long id = purchaseOrderRequest.getId();

            //游戏密码
            String gamePwd = "";
            String secondPwd = "";
            if (StringUtils.isNotBlank(purchaseOrderRequest.getGamePwd()) && !purchaseOrderRequest.getGamePwd().equals("******")) {
                gamePwd = purchaseOrderRequest.getGamePwd();
            }
            if (StringUtils.isNotBlank(purchaseOrderRequest.getSecondPwd()) && !purchaseOrderRequest.getSecondPwd().equals("******")) {
                secondPwd = purchaseOrderRequest.getSecondPwd();
            }
//            String gamePwd = purchaseOrderRequest.getGamePwd();
//            String secondPwd = purchaseOrderRequest.getSecondPwd();

            gameAccountManager.updateGamePwd(id, gamePwd, secondPwd);


            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("修改密码发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("修改密码发生未知异常:{}", e);
        }
        return response;
    }

    @Path("deleteGameAccount")
    @GET
    @Override
    public IServiceResponse deleteGameAccount(@QueryParam("") PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            //获取当前用户
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }
            Long id = purchaseOrderRequest.getId();
            gameAccountManager.deleteGameAccount(id);

            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("删除库存发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("删除库存发生未知异常:{}", e);
        }

        return response;
    }

    @Path("exportOrder")
    @GET
    @Override
    public void exportOrder(@QueryParam("") PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request, @Context HttpServletResponse servletResponse) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            //获取当前用户
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return;
            }

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("gameName", purchaseOrderRequest.getGameName());
            paramMap.put("region", purchaseOrderRequest.getRegion());
            paramMap.put("server", purchaseOrderRequest.getServer());
            paramMap.put("gameRace", purchaseOrderRequest.getGameRace());
            paramMap.put("buyerAccount", userInfo.getLoginAccount());
            paramMap.put("roleName", purchaseOrderRequest.getRoleName());
            paramMap.put("gameAccount",purchaseOrderRequest.getGameAccount());
            List<SortField> sortFields = Lists.newArrayList();
            sortFields.add(new SortField("game_name", SortField.ASC));
            sortFields.add(new SortField("region", SortField.ASC));
            sortFields.add(new SortField("server", SortField.ASC));
            List<GameAccount> list = gameAccountManager.queryListInPage(paramMap, sortFields);
            exportToExcel(list, request, servletResponse);
        } catch (SystemException ex) {
            // 捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            logger.error("导出库存明细发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            logger.error("导出库存明细发生未知异常:{}", ex);
        }
    }


    private void exportToExcel(List<GameAccount> list,
                               HttpServletRequest request,
                               HttpServletResponse servletRespone) {
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
        String headString = "库存明细列表";
        int columnSize = 9;
        exportExcel.createNormalHead(0, headString, columnSize - 1);

        // 创建报表列
        String[] columHeader = new String[]{
                "游戏名称",
                "游戏区服",
                "游戏帐号",
                "角色名",
                "交易上限（万金）",
                "库存上限（万金）",
                "最新库存（万金）",
                "今日已售（万金）",
                "库存缺口（万金）"
        };
        exportExcel.createColumHeader(1, columHeader);

        HSSFCellStyle cellstyle = wb.createCellStyle();
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);

        //设置列的宽度
        sheet.setColumnWidth(0, columnSize * 10 * 60);
        sheet.setColumnWidth(1, columnSize * 10 * 60);
        sheet.setColumnWidth(2, columnSize * 10 * 60);
        sheet.setColumnWidth(3, columnSize * 10 * 60);
        sheet.setColumnWidth(4, columnSize * 10 * 60);
        sheet.setColumnWidth(5, columnSize * 10 * 60);
        sheet.setColumnWidth(6, columnSize * 10 * 60);
        sheet.setColumnWidth(7, columnSize * 10 * 60);
        sheet.setColumnWidth(8, columnSize * 10 * 60);

        Map<String, Double> map = new HashMap<String, Double>();

        // 循环创建中间的单元格的各项的值
        if (list != null) {
            int i = 2;
            for (GameAccount gameAccount : list) {
                Boolean isShRole = gameAccount.getIsShRole() == null ? false : gameAccount.getIsShRole();
                String gameName = gameAccount.getGameName();
                String regionServer = gameAccount.getRegion() + "/" + gameAccount.getServer();
                String account = gameAccount.getGameAccount();
                String roleName = gameAccount.getRoleName();
                //交易上限
                int tradeUpperLimit = gameAccount.getLevel() * gameAccount.getLevel();

                Double poundage = map.get(gameName + "游戏币");
                if (poundage == null) {
                    ShGameConfig shGameConfig = shGameConfigManager.getConfigByGameName(gameName, "游戏币", null, null);
                    if (shGameConfig == null) {
                        throw new SystemException(ResponseCodes.NotAvailableConfig.getCode(), ResponseCodes.NotAvailableConfig.getMessage());
                    }
                    poundage = shGameConfig.getMailFee();
                    if (poundage == null) {
                        poundage = 0.0d;
                    }
                    map.put(gameName + "游戏币", poundage);
                    poundage = map.get(gameName + "游戏币");
                }
                String p = poundage + "";
                //库存上限
                BigDecimal repositoryUpperLimit = new BigDecimal(tradeUpperLimit).multiply(new BigDecimal(1).add(new BigDecimal(p)));
                long upperLimit = repositoryUpperLimit.longValue();
                //最新库存
                Long latestRepository = gameAccount.getRepositoryCount() == null ? 0 : gameAccount.getRepositoryCount();
                //今日已售
                Long todaySaleCount = gameAccount.getTodaySaleCount() == null ? 0 : gameAccount.getTodaySaleCount();
                //库存缺口
                long repositoryGaps = upperLimit - latestRepository - todaySaleCount;
                if (repositoryGaps < 0) {
                    repositoryGaps = 0;
                }
                HSSFRow row = sheet.createRow((short) i++);
                exportExcel.cteateCell(wb, row, (short) 0, cellstyle, gameName);
                exportExcel.cteateCell(wb, row, (short) 1, cellstyle, regionServer);
                exportExcel.cteateCell(wb, row, (short) 2, cellstyle, account);
                exportExcel.cteateCell(wb, row, (short) 3, cellstyle, roleName);
                exportExcel.cteateCell(wb, row, (short) 4, cellstyle, tradeUpperLimit + "");
                exportExcel.cteateCell(wb, row, (short) 5, cellstyle, upperLimit + "");
                exportExcel.cteateCell(wb, row, (short) 6, cellstyle, latestRepository + "");
                exportExcel.cteateCell(wb, row, (short) 7, cellstyle, todaySaleCount + "");
                exportExcel.cteateCell(wb, row, (short) 8, cellstyle, isShRole ? "-" : repositoryGaps + "");
            }
        }
        //String exportPath = WebServerContants.FILES_EXPORT_PATH;
        String path = "/srv/export";
        String fileName = UUID.randomUUID().toString() + ".xls";
        File file = new File(path);
        file.mkdirs();
        String filePath = path + "/" + fileName;
        exportExcel.outputExcel(filePath);
        try {
            servletRespone.setContentType("text/plain");
            servletRespone.setHeader("Location", fileName);
            servletRespone.setHeader("Content-Disposition", "attachment; filename=" + new String("库存明细表".getBytes(),"iso-8859-1")+".xls");

            inputStream = new FileInputStream(filePath);
            ServletOutputStream outputStream = servletRespone.getOutputStream();
            byte[] buffer = new byte[1024 * 2];
            int i = -1;
            while ((i = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
