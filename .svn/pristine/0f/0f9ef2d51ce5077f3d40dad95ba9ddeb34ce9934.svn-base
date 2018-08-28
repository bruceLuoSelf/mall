package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 出货子订单
 */
public class DeliverySubOrder extends BaseEntity {
    /**
     * 关联的出货单号
     */
    private Long chId;
    /**
     * 关联的出货单编号
     */
    private String orderId;
    /**
     * 出货方5173账号
     */
    private String sellerAccount;
    /**
     * 出货方5173UID
     */
    private String sellerUid;
    /**
     * 收货方5173账号
     */
    private String buyerAccount;
    /**
     * 收货方5173UID
     */
    private String buyerUid;
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
     * 出货角色名
     */
    private String sellerRoleName;
    /**
     * 密语
     */
    private String words;
    /**
     * 交易地点
     */
    private String address;
    /**
     * 收货的游戏账号
     */
    private String gameAccount;
    /**
     * 游戏密码
     */
    private String gamePwd;
    /**
     * 收货的角色名
     */
    private String gameRole;
    /**
     * 二级密码
     */
    private String secondPwd;
    /**
     * 收货数量
     */
    private Long count;
    /**
     * 实际收货数量
     */
    private Long realCount;
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
     * 撤单原因
     */
    private Integer reason;
    /**
     * 其他原因
     */
    private String otherReason;

    private Integer deliveryType; //收货方式
    private Integer tradeType; //交易方式
    private String deliveryTypeName;
    private String tradeTypeName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后更新时间
     */
    private Date updateTime;
    /**
     * 发货时间
     */
    private Date deliveryTime;
    /**
     * 结束时间，完单或者取消
     */
    private Date finishTime;
    /**
     * 单位
     */
    private String moneyName;

    /**
     * 是否超时
     */
    private Boolean isTimeout;

    /**
     * 收货商电话（GTR使用）
     */
    private String buyerTel;

    /**
     * 接手物服id
     */
    private String takeOverSubjectId;

    /**
     * 接手物服
     */
    private String takeOverSubject;

    /**
     * 机器转人工原因
     */
    private String machineArtificialReason;

    /**
     * 机器转人工时间
     */
    private Date machineArtificialTime;

    /**
     * 机器转人工状态(1.已转人工；2.转人工失败)
     */
    private Integer machineArtificialStatus;

    /**
     * 备注(机器收货异常转人工物服取消订单备注)
     */
    private String remarks;

    /**
     * 出货商填写收货数量
     */
    private Long shInputCount;


    /**
     * 关联申诉单号
     */
    private String appealOrder;

    /**
     * 是否是待确认状态  true代表需要用户确认  false代表不需要用户确认了
     */
    private Boolean waitToConfirm;

    private int roleLevel;

    private Integer appealOrderStatus;

    /**
     * 出货商发货时间
     */
    private Date sellerDeliveryTime;


    /**
     * 邮寄,拍卖类型
     * @return
     */
    private Integer tradeLogo;

    /**
     * 拍卖交易用到
     * @return
     */
    private Integer afterFour;

    /**
     * 角色表id
     */
    private Long gameAccountId;

    /**
     * 图片列表
     */
    private List<RobotImgEO> robotImgList;

    private Boolean splited;

    public Boolean getSplited() {
        return splited;
    }

    public void setSplited(Boolean splited) {
        this.splited = splited;
    }

    public List<RobotImgEO> getRobotImgList() {
        return robotImgList;
    }

    public void setRobotImgList(List<RobotImgEO> robotImgList) {
        this.robotImgList = robotImgList;
    }

    public Integer getAfterFour() {
        return afterFour;
    }

    public void setAfterFour(Integer afterFour) {
        this.afterFour = afterFour;
    }

    public Integer getTradeLogo() {
        return tradeLogo;
    }

    public void setTradeLogo(Integer tradeLogo) {
        this.tradeLogo = tradeLogo;
    }

    public Integer getAppealOrderStatus() {
        return appealOrderStatus;
    }

    public void setAppealOrderStatus(Integer appealOrderStatus) {
        this.appealOrderStatus = appealOrderStatus;
    }

    public int getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    public Boolean getWaitToConfirm() {
        return waitToConfirm;
    }

    public void setWaitToConfirm(Boolean waitToConfirm) {
        this.waitToConfirm = waitToConfirm;
    }


    public String getAppealOrder() {
        return appealOrder;
    }

    public void setAppealOrder(String appealOrder) {
        this.appealOrder = appealOrder;
    }

    public Long getShInputCount() {
        return shInputCount;
    }

    public void setShInputCount(Long shInputCount) {
        this.shInputCount = shInputCount;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public String getDeliveryTypeName() {
        return deliveryTypeName;
    }

    public void setDeliveryTypeName(String deliveryTypeName) {
        this.deliveryTypeName = deliveryTypeName;
    }

    public String getTradeTypeName() {
        return tradeTypeName;
    }

    public void setTradeTypeName(String tradeTypeName) {
        this.tradeTypeName = tradeTypeName;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public DeliverySubOrder() {
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public Long getChId() {
        return chId;
    }

    public void setChId(Long chId) {
        this.chId = chId;
    }

    public String getGameAccount() {
        return gameAccount;
    }

    public void setGameAccount(String gameAccount) {
        this.gameAccount = gameAccount;
    }

    public String getGamePwd() {
        return gamePwd;
    }

    public void setGamePwd(String gamePwd) {
        this.gamePwd = gamePwd;
    }

    public String getGameRole() {
        return gameRole;
    }

    public void setGameRole(String gameRole) {
        this.gameRole = gameRole;
    }

    public String getSecondPwd() {
        return secondPwd;
    }

    public void setSecondPwd(String secondPwd) {
        this.secondPwd = secondPwd;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getReason() {
        return reason;
    }

    public void setReason(Integer reason) {
        this.reason = reason;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    public String getSellerUid() {
        return sellerUid;
    }

    public void setSellerUid(String sellerUid) {
        this.sellerUid = sellerUid;
    }

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public String getBuyerUid() {
        return buyerUid;
    }

    public void setBuyerUid(String buyerUid) {
        this.buyerUid = buyerUid;
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

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSellerRoleName() {
        return sellerRoleName;
    }

    public void setSellerRoleName(String sellerRoleName) {
        this.sellerRoleName = sellerRoleName;
    }

    public Boolean getIsTimeout() {
        return isTimeout;
    }

    public void setIsTimeout(Boolean isTimeout) {
        this.isTimeout = isTimeout;
    }

    public String getMoneyName() {
        return moneyName;
    }

    public void setMoneyName(String moneyName) {
        this.moneyName = moneyName;
    }

    public String getBuyerTel() {
        return buyerTel;
    }

    public void setBuyerTel(String buyerTel) {
        this.buyerTel = buyerTel;
    }

    public String getTakeOverSubjectId() {
        return takeOverSubjectId;
    }

    public void setTakeOverSubjectId(String takeOverSubjectId) {
        this.takeOverSubjectId = takeOverSubjectId;
    }

    public String getTakeOverSubject() {
        return takeOverSubject;
    }

    public void setTakeOverSubject(String takeOverSubject) {
        this.takeOverSubject = takeOverSubject;
    }

    public String getMachineArtificialReason() {
        return machineArtificialReason;
    }

    public void setMachineArtificialReason(String machineArtificialReason) {
        this.machineArtificialReason = machineArtificialReason;
    }

    public Date getMachineArtificialTime() {
        return machineArtificialTime;
    }

    public void setMachineArtificialTime(Date machineArtificialTime) {
        this.machineArtificialTime = machineArtificialTime;
    }

    public Integer getMachineArtificialStatus() {
        return machineArtificialStatus;
    }

    public void setMachineArtificialStatus(Integer machineArtificialStatus) {
        this.machineArtificialStatus = machineArtificialStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getSellerDeliveryTime() {
        return sellerDeliveryTime;
    }

    public void setSellerDeliveryTime(Date sellerDeliveryTime) {
        this.sellerDeliveryTime = sellerDeliveryTime;
    }

    public Long getGameAccountId() {
        return gameAccountId;
    }

    public void setGameAccountId(Long gameAccountId) {
        this.gameAccountId = gameAccountId;
    }
}
