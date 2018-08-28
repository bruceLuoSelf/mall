package com.wzitech.gamegold.shorder.entity;

import java.util.Date;

/**
 * Created by 340032 on 2018/1/2.
 */
public class RoboutOrder {

    /**
     * 子订单id
     */
    private Long id;

    /**
     * 主订单id
     */
    private String orderId;

    /**
     * 游戏名
     */
    private String gameName;

    /**
     * 游戏区
     */
    private String region;
    /**
     * 游戏服
     */
    private String server;

    /**
     * 游戏密码
     */
    private String gamePwd;

    /**
     * 二级密码
     */
    private String secondPwd;

    /**
     * 游戏阵营
     */
    private String gameRace;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 收货方5173账号
     */
    private String buyerAccount;

    /**
     * 收货的游戏账号
     */
    private String gameAccount;


    /**
     * 状态
     * <li>1：待处理</li>
     * <li>3：交易中</li>
     * <li>4：交易完成</li>
     * <li>5：部分完单</li>
     * <li>6：撤单</li>
     * <li>7：需人工介入</li>
     */
    private Integer status;

    /**
     * 收货角色名
     * @return
     */
    private String gameRole;

    //出货商角色等级
    private Integer sellerRoleLevel;

    /**
     * 收货数量
     */
    private Long count;

    /**
     * 拍卖交易用到  (后4位小数)
     * @return
     */
    private Integer afterFour;

    /**
     * 邮寄,拍卖类型
     * @return
     */
    private Integer tradeLogo;

    public Integer getTradeLogo() {
        return tradeLogo;
    }

    public void setTradeLogo(Integer tradeLogo) {
        this.tradeLogo = tradeLogo;
    }

    public Integer getAfterFour() {
        return afterFour;
    }

    public void setAfterFour(Integer afterFour) {
        this.afterFour = afterFour;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Integer getSellerRoleLevel() {
        return sellerRoleLevel;
    }

    public void setSellerRoleLevel(Integer sellerRoleLevel) {
        this.sellerRoleLevel = sellerRoleLevel;
    }

    public String getGameRole() {
        return gameRole;
    }

    public void setGameRole(String gameRole) {
        this.gameRole = gameRole;
    }

    public String getGameAccount() {
        return gameAccount;
    }

    public void setGameAccount(String gameAccount) {
        this.gameAccount = gameAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getGameRace() {
        return gameRace;
    }

    public void setGameRace(String gameRace) {
        this.gameRace = gameRace;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
