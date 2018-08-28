package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.util.Date;

/**
 * 分单请求
 */
public class SplitRepositoryRequest extends BaseEntity {
    /**
     * 状态：未推送
     */
    public static final int S_NOT_PUSH = -1;
    /**
     * 状态：待处理
     */
    public static final int S_WAIT_PROCESS = 0;
    /**
     * 状态：分仓中
     */
    public static final int S_SPLIT = 1;
    /**
     * 状态：分仓结束
     */
    public static final int S_FINISH = 2;
    /**
     * 分仓请求订单号
     */
    private String orderId;
    /**
     * 收货方5173账号
     */
    private String buyerAccount;
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
     * 游戏阵营
     */
    private String gameRace;
    /**
     * 游戏账号
     */
    private String gameAccount;
    /**
     * 附魔师角色名
     */
    private String fmsRoleName;
    /**
     * 游戏角色名
     */
    private String gameRole;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 二级密码
     */
    private String secondPwd;
    /**
     * 状态
     * <li>-1：未推送</li>
     * <li>0：待处理</li>
     * <li>1：分仓中 </li>
     * <li>2：分仓结束</li>
     */
    private Integer status;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 计划分仓数量
     */
    private Long count;
    /**
     * 实际分仓数量
     */
    private Long realCount;
    /**
     * 收货主订单号
     */
    private String shOrderId;
    /**
     * 收货子订单号
     */
    private Long shSubOrderId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 自动化原因
     */
    private Integer robotReason;
    /**
     * 其他原因
     */
    private String robotOtherReason;
    /**
     * 分仓原因
     */
    private String splitReason;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getRealCount() {
        return realCount;
    }

    public void setRealCount(Long realCount) {
        this.realCount = realCount;
    }

    public String getShOrderId() {
        return shOrderId;
    }

    public void setShOrderId(String shOrderId) {
        this.shOrderId = shOrderId;
    }

    public Long getShSubOrderId() {
        return shSubOrderId;
    }

    public void setShSubOrderId(Long shSubOrderId) {
        this.shSubOrderId = shSubOrderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getRobotReason() {
        return robotReason;
    }

    public void setRobotReason(Integer robotReason) {
        this.robotReason = robotReason;
    }

    public String getRobotOtherReason() {
        return robotOtherReason;
    }

    public void setRobotOtherReason(String robotOtherReason) {
        this.robotOtherReason = robotOtherReason;
    }

    public String getSplitReason() {
        return splitReason;
    }

    public void setSplitReason(String splitReason) {
        this.splitReason = splitReason;
    }

    public SplitRepositoryRequest() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
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

    public String getGameAccount() {
        return gameAccount;
    }

    public void setGameAccount(String gameAccount) {
        this.gameAccount = gameAccount;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSecondPwd() {
        return secondPwd;
    }

    public void setSecondPwd(String secondPwd) {
        this.secondPwd = secondPwd;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getGameRole() {
        return gameRole;
    }

    public void setGameRole(String gameRole) {
        this.gameRole = gameRole;
    }

    public String getFmsRoleName() {
        return fmsRoleName;
    }

    public void setFmsRoleName(String fmsRoleName) {
        this.fmsRoleName = fmsRoleName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
