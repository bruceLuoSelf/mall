package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.util.Date;

/**
 * @author ljn
 * @date 2018/6/20.
 * 盘库
 */
public class Stock extends BaseEntity {

    /**
     * 库存类型(1：销售；2：收货；3：分仓)
     */
    private Integer stockType;
    /**
     * 背包数量
     */
    private Long packageCount;
    /**
     * 金库数量
     */
    private Long repertoryCount;
    /**
     * 游戏名称
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
     * 阵营名
     */
    private String raceName;
    /**
     * 游戏账号
     */
    private String gameAccount;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 关联(销售、收货、分仓)主订单号
     */
    private String orderId;
    /**
     * 关联(销售、收货、分仓)子订单号
     */
    private Long subOrderId;

    public Integer getStockType() {
        return stockType;
    }

    public void setStockType(Integer stockType) {
        this.stockType = stockType;
    }

    public Long getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(Long packageCount) {
        this.packageCount = packageCount;
    }

    public Long getRepertoryCount() {
        return repertoryCount;
    }

    public void setRepertoryCount(Long repertoryCount) {
        this.repertoryCount = repertoryCount;
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

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(Long subOrderId) {
        this.subOrderId = subOrderId;
    }
}
