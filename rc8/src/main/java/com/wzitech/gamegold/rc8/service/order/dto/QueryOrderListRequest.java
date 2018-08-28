package com.wzitech.gamegold.rc8.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 查询订单列表请求
 *
 * @author yemq
 */
public class QueryOrderListRequest extends AbstractServiceRequest {
    /**
     * 创建时间
     */
    public static final int TIME_TYPE_CREATE = 0;
    /**
     * 结束时间
     */
    public static final int TIME_TYPE_END = 1;

    /**
     * 客服账号
     */
    private String name;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 时间类型
     * 0-创建时间
     * 1-结束时间
     */
    private Integer timeType;
    /**
     * 开始时间
     */
    private String stime;
    /**
     * 结束时间
     */
    private String etime;
    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 游戏ID
     */
    private String gameId;

    /**
     * 买家账号
     */
    private String buy;

    /**
     * 买家QQ
     */
    private String buyQq;

    /**
     * 卖家账号
     */
    private String sell;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 人工操作
     */
    private String manualOperation;

    /**
     * 签名
     */
    private String sign;
    /**
     * RC客户端版本号
     */
    private String version;

    private Integer pageSize;

    private Integer page;

    /**
     * 增加商品类目 by lvchengsheng
     */
    private String goodsTypeName;

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public QueryOrderListRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getBuyQq() {
        return buyQq;
    }

    public void setBuyQq(String buyQq) {
        this.buyQq = buyQq;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getManualOperation() {
        return manualOperation;
    }

    public void setManualOperation(String manualOperation) {
        this.manualOperation = manualOperation;
    }
}
