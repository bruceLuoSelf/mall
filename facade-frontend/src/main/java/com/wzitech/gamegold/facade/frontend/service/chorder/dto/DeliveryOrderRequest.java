package com.wzitech.gamegold.facade.frontend.service.chorder.dto;

import java.util.Date;

/**
 * 订单
 * Created by 335854 on 2016/4/11.
 */
public class DeliveryOrderRequest {
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

    private String roleName;
    private Long count;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 交易类型
     * @return
     */
    private String goodsTypeName;

    private Long goodsType;

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public Long getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Long goodsTypeId) {
        this.goodsType = goodsType;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
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

    public String getGameRace() {
        return gameRace;
    }

    public void setGameRace(String gameRace) {
        this.gameRace = gameRace;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
