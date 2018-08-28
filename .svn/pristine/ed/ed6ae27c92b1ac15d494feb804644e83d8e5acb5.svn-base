package com.wzitech.gamegold.facade.backend.action.fundmgmt;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.enums.RefererType;
import com.wzitech.gamegold.common.utils.DateUtil;
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

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.facade.backend.contant.WebServerContants;
import com.wzitech.gamegold.facade.backend.excel.ExportExcel;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.funds.business.ITradingStatisticsManager;
import com.wzitech.gamegold.funds.business.ITransactionInfoManager;
import com.wzitech.gamegold.funds.dao.ITransactionInfoDBDAO;
import com.wzitech.gamegold.funds.entity.TradingStatisticsEO;
import com.wzitech.gamegold.funds.entity.TransactionInfo;
import com.wzitech.gamegold.funds.entity.gameTradingEO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

@Controller
@Scope("prototype")
@ExceptionToJSON
public class TransactionInfoAction extends AbstractAction {

    private List<TransactionInfo> transactionInfoList;

    private List<gameTradingEO> gameTradingList;

    private Integer orderState;

    private BigDecimal disCount;

    private String loginAccount;

    private Long servicerId;

    private Date statementStartTime;

    private Date statementEndTime;

    private String gameName;

    private String region;

    private String server;

    private Integer refererType;

    private TradingStatisticsEO tradingStatistics;

    /**
     * 总金额
     */
    private BigDecimal totalAmount = BigDecimal.ZERO;
    /**
     * 总笔数
     */
    private Long totalCounts = 0L;
    /**
     * 总收入
     */
    private BigDecimal income = BigDecimal.ZERO;

    /**
     * 使用红包数量
     */
    private Long redEnvelope = 0L;

    /**
     * 使用的红包金额汇总
     */
    private BigDecimal redEnvelopeAccount = BigDecimal.ZERO;

    /**
     * 使用店铺券数量
     */
    private Long shopCouponCount = 0L;

    /**
     * 使用的店铺券金额汇总
     */
    private BigDecimal shopCouponAccount = BigDecimal.ZERO;

    /**
     * 商品类目id
     */
    private Integer goodsTypeId;

    /**
     * 商品类目名称
     */
    private String goodsTypeName;

    @Autowired
    ITransactionInfoManager transactionInfoManager;

    @Autowired
    ITransactionInfoDBDAO transactionInfoDBDAO;

    @Autowired
    ITradingStatisticsManager tradingStatisticsManager;

    /**
     * 查询商品订单分成信息列表
     *
     * @return
     */
    public String queryTransaction() {
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            String orderBy = "END_TIME";
            int userType = CurrentUserContext.getUserType();
            if (UserType.SystemManager.getCode() != userType) {
                if (UserType.MakeOrder.getCode() == userType || UserType.RecruitBusiness.getCode() == userType) {
                    UserInfoEO user = (UserInfoEO) CurrentUserContext.getUser();
                    paramMap.put("servicerId", user.getMainAccountId());
                } else {
                    paramMap.put("servicerId", CurrentUserContext.getUid());
                }
            } else {
                paramMap.put("servicerId", servicerId);
            }
            if (userType == UserType.NomalManager.getCode()) {
                paramMap.put("servicerId", servicerId);
            }
            paramMap.put("loginAccount", loginAccount);
            if (orderState != null && orderState == 4) {
                paramMap.put("createStartTime", statementStartTime);
                paramMap.put("createEndTime",
                        WebServerUtil.oneDateLastTime(statementEndTime));
                orderBy = "CREATE_TIME";
            } else {
                paramMap.put("statementStartTime", statementStartTime);
                paramMap.put("statementEndTime",
                        WebServerUtil.oneDateLastTime(statementEndTime));
            }
            paramMap.put("orderState", orderState);
            if(refererType != null){
                if (refererType == 3) {
                    paramMap.put("refererTypeBygoldOrder","'1','2','3'");
                } else {
                    paramMap.put("refererType", refererType);
                }
            }
            paramMap.put("goodsTypeId", goodsTypeId);//ZW_C_JB_00008 add
            paramMap.put("goodsTypeName", goodsTypeName);//ZW_C_JB_00008 add

            GenericPage<TransactionInfo> genericPage = transactionInfoManager.queryTransactionList(paramMap, orderBy, false, this.limit, this.start);
            transactionInfoList = genericPage.getData();
            totalCount = genericPage.getTotalCount();
            return this.returnSuccess();
        } catch (SystemException e) {
            return returnError(e);
        }
    }

    /**
     * 汇总订单信息
     *
     * @return
     */
    public String statisticOrderInfo() {
        // 查询统计数据
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orderState", orderState);
        paramMap.put("loginAccount", loginAccount);
        paramMap.put("statementStartTime", statementStartTime);
        paramMap.put("statementEndTime", WebServerUtil.oneDateLastTime(statementEndTime));
        if(refererType != null){
            if (refererType == 3) {
                paramMap.put("refererTypeBygoldOrder","'1','2','3'");
            } else {
                paramMap.put("refererType", refererType);
            }
        }

        // 查询订单总笔数和总金额
        Map<String, Object> result = tradingStatisticsManager.queryOrderTotalCountAndAmount(paramMap);
        if (result != null) {
            totalCounts = (Long) result.get("TOTAL_COUNT");
            totalAmount = (BigDecimal) result.get("TOTAL_AMOUNT");
        }

        // 查询订单总收入(佣金+差价)
        if (orderState == null || orderState != OrderState.Statement.getCode()) {
            income = BigDecimal.ZERO;
        } else {
            Map<String, BigDecimal> map = tradingStatisticsManager.countCommissionAndPriceDiff(paramMap);
            if (map != null) {
                BigDecimal commission = map.get("commission");
                BigDecimal priceDiff = map.get("price_diff");
                if (commission != null && priceDiff != null) {
                    income = commission.add(priceDiff);
                } else {
                    income = BigDecimal.ZERO;
                }
            } else {
                income = BigDecimal.ZERO;
            }
            income = income.setScale(2, BigDecimal.ROUND_HALF_UP);

            Map<String, Object> resultYhq = tradingStatisticsManager.queryYhqCountAndAmount(paramMap);
            if (resultYhq != null) {
                redEnvelope = (Long) resultYhq.get("RED_ENVELOPE_COUNT");
                redEnvelopeAccount = (BigDecimal) resultYhq.get("RED_ENVELOPE");
                shopCouponCount = (Long) resultYhq.get("SHOP_COUPON_COUNT");
                shopCouponAccount = (BigDecimal) resultYhq.get("SHOP_COUPON");
            }
        }
        return this.returnSuccess();
    }

    /**
     * 查询区服交易信息列表
     *
     * @return
     */
    public String queryGameTradingList() {
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("disCount", disCount);
            paramMap.put("statementStartTime", statementStartTime);
            paramMap.put("statementEndTime", WebServerUtil.oneDateLastTime(statementEndTime));
            paramMap.put("orderState", 5);
            paramMap.put("gameName", gameName);
            paramMap.put("region", region);
            paramMap.put("server", server);
            GenericPage<gameTradingEO> genericPage = transactionInfoDBDAO.selectTradingByGame(paramMap, this.limit, this.start, "TRADING_NUM", false);
            gameTradingList = genericPage.getData();
            totalCount = genericPage.getTotalCount();
            return this.returnSuccess();
        } catch (SystemException e) {
            return returnError(e);
        }
    }

    //单独查出差额，然后遍历list把差额赋值
    public List<gameTradingEO> setListDifference(List<gameTradingEO> list) {
        for (gameTradingEO gameTradingInfo : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("gameName", gameTradingInfo.getGameName());
            map.put("region", gameTradingInfo.getRegion());
            map.put("server", gameTradingInfo.getServer());
            map.put("statementStartTime", this.statementStartTime);
            map.put("statementEndTime", WebServerUtil.oneDateLastTime(this.statementEndTime));
            BigDecimal difference;

            String differenceStr = transactionInfoDBDAO.selectDifference(map);
            if (StringUtils.isEmpty(differenceStr)) {
                difference = new BigDecimal("0.00");
            } else {
                difference = new BigDecimal(differenceStr);
            }
            gameTradingInfo.setDifference(difference);
            BigDecimal total = gameTradingInfo.getTotal();
            if ((total.doubleValue() - difference.doubleValue()) * 0.06 != 0) {
                BigDecimal commission = new BigDecimal((total.doubleValue() - difference.doubleValue()) * 0.06);
                commission = commission.setScale(4, BigDecimal.ROUND_HALF_UP);
                gameTradingInfo.setCommission(commission);
            } else {
                gameTradingInfo.setCommission(new BigDecimal("0"));
            }
        }
        return list;
    }

    /**
     * 查询区服交易信息列表,不是分页对象,是为了得到导出数据
     *
     * @return
     */
    public List<gameTradingEO> queryGameTrading() {
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("disCount", disCount);
            paramMap.put("statementStartTime", statementStartTime);
            paramMap.put("statementEndTime", WebServerUtil.oneDateLastTime(statementEndTime));
            paramMap.put("orderState", 5);
            paramMap.put("gameName", gameName);
            paramMap.put("region", region);
            paramMap.put("server", server);
            return setListDifference(transactionInfoDBDAO.selectTradingByGame1(paramMap));
        } catch (SystemException e) {
            return null;
        }
    }

    /**
     * <p>交易统计</p>
     *
     * @return
     * @author Think
     * @date 2014-2-25 下午9:11:02
     * @see
     */
    public String tradingStatistics() {
        try {
            tradingStatistics = tradingStatisticsManager.queryTradingStatistics();
            return this.returnSuccess();
        } catch (SystemException e) {
            return returnError(e);
        }
    }

    //导出区服交易信息
    public String exportGameTradingFlow() {
        gameTradingList = queryGameTrading();

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

        sheet.setColumnWidth(0, 20 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 20 * 256);

        // 设置单元格字体
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);

        // 创建报表头部
        String headString = gameName + "区服交易流水列表   从" +
                format.format(statementStartTime) + "到" + format.format(WebServerUtil.oneDateLastTime(statementEndTime));
        int columnSize = 7;
        exportExcel.createNormalHead(0, headString, columnSize - 1);

        // 创建报表列
        String[] columHeader = new String[]{"游戏名称", "所在区", "所在服", "成交笔数", "金币总数",
                "订单总金额", "佣金收入", "差额收入"};
        exportExcel.createColumHeader(1, columHeader);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("statementStartTime", statementStartTime);
        map.put("statementEndTime", WebServerUtil.oneDateLastTime(statementEndTime));


        HSSFCellStyle cellstyle = wb.createCellStyle();
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);

        // 循环创建中间的单元格的各项的值
        if (gameTradingList != null) {
            int i = 2;
            for (gameTradingEO gameTradingInfo : gameTradingList) {
                HSSFRow row = sheet.createRow((short) i++);
                //8月13剑灵佣金条件，所以对佣金处理
                BigDecimal commission = gameTradingInfo.getCommission();
                Date date = null, date1 = null, date2 = null;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    date = sdf.parse("2014-08-13 12:00:00");
                    date1 = sdf.parse("2014-11-26 12:30:00");
                    date2 = sdf.parse("2015-01-04 11:59:59");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (gameTradingInfo.getGameName().equals("剑灵") && WebServerUtil.oneDateLastTime(statementEndTime).after(date)) {
                    map.put("gameName", gameTradingInfo.getGameName());
                    map.put("commission", commission);
                    map.put("region", gameTradingInfo.getRegion());
                    map.put("server", gameTradingInfo.getServer());
                    map.put("date", date);
                    map.put("date1", date1);
                    map.put("date2", date2);
                    commission = new BigDecimal(transactionInfoDBDAO.countCommission(map)).setScale(4, BigDecimal.ROUND_HALF_UP);
                    ;
                }
                exportExcel.cteateCell(wb, row, (short) 0,
                        cellstyle,
                        gameTradingInfo.getGameName());
                exportExcel.cteateCell(wb, row, (short) 1,
                        cellstyle,
                        gameTradingInfo.getRegion());
                exportExcel.cteateCell(wb, row, (short) 2,
                        cellstyle,
                        gameTradingInfo.getServer());
                exportExcel.cteateCell(wb, row, (short) 3,
                        cellstyle,
                        gameTradingInfo.getTradingNum() + "");
                exportExcel.cteateCell(wb, row, (short) 4,
                        cellstyle,
                        gameTradingInfo.getGoldCount() + "");
                exportExcel.cteateCell(wb, row, (short) 5,
                        cellstyle,
                        "￥" + gameTradingInfo.getTotal());
                exportExcel.cteateCell(wb, row, (short) 6,
                        cellstyle,
                        "￥" + commission + "");
                exportExcel.cteateCell(wb, row, (short) 7,
                        cellstyle,
                        "￥" + gameTradingInfo.getDifference() + "");
            }
        }

        String exportPath = WebServerContants.FILES_EXPORT_PATH;
        String path = ServletActionContext.getServletContext().getRealPath(exportPath);
        File file = new File(path);
        file.mkdirs();
        String filePath = path + "/" + UUID.randomUUID().toString() + ".xls";
        exportExcel.outputExcel(filePath);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                if(inputStream!= null)
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return returnSuccess();
    }

    public List<TransactionInfo> getTransactionInfoList() {
        return transactionInfoList;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public void setStatementStartTime(Date statementStartTime) {
        this.statementStartTime = statementStartTime;
    }

    public void setStatementEndTime(Date statementEndTime) {
        this.statementEndTime = statementEndTime;
    }

    public TradingStatisticsEO getTradingStatistics() {
        return tradingStatistics;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public void setServicerId(Long servicerId) {
        this.servicerId = servicerId;
    }

    public List<gameTradingEO> getGameTradingList() {
        return gameTradingList;
    }

    public void setGameTradingList(List<gameTradingEO> gameTradingList) {
        this.gameTradingList = gameTradingList;
    }

    public void setDisCount(BigDecimal disCount) {
        this.disCount = disCount;
    }

    public BigDecimal getDisCount() {
        return disCount;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public Long getTotalCounts() {
        return totalCounts;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTransactionInfoList(List<TransactionInfo> transactionInfoList) {
        this.transactionInfoList = transactionInfoList;
    }

    public Long getShopCouponCount() {
        return shopCouponCount;
    }

    public void setShopCouponCount(Long shopCouponCount) {
        this.shopCouponCount = shopCouponCount;
    }

    public BigDecimal getShopCouponAccount() {
        return shopCouponAccount;
    }

    public void setShopCouponAccount(BigDecimal shopCouponAccount) {
        this.shopCouponAccount = shopCouponAccount;
    }

    public BigDecimal getRedEnvelopeAccount() {
        return redEnvelopeAccount;
    }

    public void setRedEnvelopeAccount(BigDecimal redEnvelopeAccount) {
        this.redEnvelopeAccount = redEnvelopeAccount;
    }

    public Long getRedEnvelope() {
        return redEnvelope;
    }

    public void setRedEnvelope(Long redEnvelope) {
        this.redEnvelope = redEnvelope;
    }

    public Integer getRefererType() {
        return refererType;
    }

    public void setRefererType(Integer refererType) {
        this.refererType = refererType;
    }

    public Integer getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(Integer goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }


    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }
}