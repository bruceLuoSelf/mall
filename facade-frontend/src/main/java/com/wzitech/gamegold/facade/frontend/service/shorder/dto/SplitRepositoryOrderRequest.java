package com.wzitech.gamegold.facade.frontend.service.shorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * @author ljn
 * @date 2018/6/12.
 * 分仓单请求
 */
public class SplitRepositoryOrderRequest extends AbstractServiceRequest {

    /**
     * 创建开始时间
     */
    private String createStartTime;

    /**
     * 创建结束时间
     */
    private String createEndTime;

    /**
     * 游戏名
     */
    private String gameName;

    /**
     * 区名
     */
    private String regionName;

    /**
     * 服名
     */
    private String serverName;

    /**
     * 分仓订单号
     */
    private String splitRepositoryOrderNo;

    /**
     * 收货主订单号
     */
    private String shOrderId;

    /**
     * 收货子订单号
     */
    private String shSubOrderId;

    /**
     * 分仓游戏帐号
     */
    private String gameAccount;

    /**
     * 收货角色
     */
    private String gameRole;

    /**
     * 被分仓角色
     */
    private String splitRepositoryRole;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 第几页
     */
    private Integer page;

    /**
     * 一页多少条
     */
    private Integer pageSize;

    public String getShOrderId() {
        return shOrderId;
    }

    public void setShOrderId(String shOrderId) {
        this.shOrderId = shOrderId;
    }

    public String getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(String createStartTime) {
        this.createStartTime = createStartTime;
    }

    public String getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(String createEndTime) {
        this.createEndTime = createEndTime;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getSplitRepositoryOrderNo() {
        return splitRepositoryOrderNo;
    }

    public void setSplitRepositoryOrderNo(String splitRepositoryOrderNo) {
        this.splitRepositoryOrderNo = splitRepositoryOrderNo;
    }

    public String getShSubOrderId() {
        return shSubOrderId;
    }

    public void setShSubOrderId(String shSubOrderId) {
        this.shSubOrderId = shSubOrderId;
    }

    public String getGameAccount() {
        return gameAccount;
    }

    public void setGameAccount(String gameAccount) {
        this.gameAccount = gameAccount;
    }

    public String getGameRole() {
        return gameRole;
    }

    public void setGameRole(String gameRole) {
        this.gameRole = gameRole;
    }

    public String getSplitRepositoryRole() {
        return splitRepositoryRole;
    }

    public void setSplitRepositoryRole(String splitRepositoryRole) {
        this.splitRepositoryRole = splitRepositoryRole;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
