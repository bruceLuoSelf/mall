package com.wzitech.gamegold.facade.frontend.service.shorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 收货角色操作请求
 */
public class PurchaseOrderRequest extends AbstractServiceRequest {

    /*
    游戏名称
     */
    private String gameName;

    /*
    游戏大区
     */
    private String region;

    /*
    游戏服
     */
    private String server;

    /*
    阵营
     */
    private String gameRace;

    /*
    页码
     */
    private Integer page;

    /*
    每页数据量
     */
    private Integer pageSize;

    /*
    采购单的id集合
     */
    private List<Long> ids;

    /**
     * 是否上架
     */
    private boolean isOnline;

    /*
    采购单价
     */
    private BigDecimal price;

    /*
    采购单的id
     */
    private Long id;

    /**
     * 账号集合
     */
    private Map<String,Long> gameAccountMap;

    /*
    最小采购数量
     */
    private Long minCount;

    /**
     * 采购量
     * @return
     */
    private Long count;

    /**
     * 账号
     */
    private String gameAccount;

    /**
     * 角色
     */
    private String roleName;

    /**
     * 账号密码
     * @return
     */
    private String gamePwd;
    /**
     * 二级密码
     */
    private  String secondPwd;

    private Integer deliveryType;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 1：收入
     * 2：支出
     */
    private String incomeType;

    /**
     * 日志类型
     */
    private String logType;

    private Boolean Sale;

    public Boolean getSale() {
        return Sale;
    }

    public void setSale(Boolean sale) {
        this.Sale = sale;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getGamePwd() {
        return gamePwd;
    }

    public void setGamePwd(String gamePwd) {
        this.gamePwd = gamePwd;
    }

    public String getSecondPwd() {
        return secondPwd;
    }

    public void setSecondPwd(String secondPwd) {
        this.secondPwd = secondPwd;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getGameRace() {
        return gameRace;
    }

    public void setGameRace(String gameRace) {
        this.gameRace = gameRace;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getMinCount() {
        return minCount;
    }

    public void setMinCount(Long minCount) {
        this.minCount = minCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, Long> getGameAccountMap() {
        return gameAccountMap;
    }

    public void setGameAccountMap(Map<String, Long> gameAccountMap) {
        this.gameAccountMap = gameAccountMap;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }


    public String getGameAccount() {
        return gameAccount;
    }

    public void setGameAccount(String gameAccount) {
        this.gameAccount = gameAccount;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
