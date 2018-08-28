package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.gamegold.common.enums.DeliveryOrderStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 出货单
 * Created by yemq on 2016/3/15.
 */
@Component
public class DeliveryOrder extends BaseEntity {
    /**
     * 转账状态-未转账
     */
    public static final int NOT_TRANSFER = 0;
    /**
     * 转账状态-等待转账
     */
    public static final int WAIT_TRANSFER = 1;
    /**
     * 转账状态-已转账
     */
    public static final int TRANSFER = 2;
    /**
     * 转账状态-7bao 转账失败
     */
    public static final int FAILD_7BAO = 3;


    public static final Integer OHTER_REASON = 408;
    /**
     * 关联的采购单号
     */
    private Long cgId;
    /**
     * 出货单编号
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
    private String roleName;
    /**
     * 出货单价
     */
    private BigDecimal price;
    /**
     * 出货数量
     */
    private Long count;
    /**
     * 出货金额
     */
    private BigDecimal amount;
    /**
     * 实际出货数量
     */
    private Long realCount;
    /**
     * 实际出货金额
     */
    private BigDecimal realAmount;
    /**
     * 出货方手机号
     */
    private String phone;
    /**
     * 出货方QQ
     */
    private String qq;
    /**
     * 密语
     */
    private String words;
    /**
     * 交易地点
     */
    private String address;
    /**
     * 交易类目
     */
    private Long goodsType;
    private String goodsTypeName;
    /**
     * 聊天室环信id
     * @return
     */
    private String chatroomId;
    /**
     * 状态
     * <li>1：等待交易</li>
     * <li>2：排队中</li>
     * <li>3：交易中</li>
     * <li>4：交易完成</li>
     * <li>5：部分完单</li>
     * <li>6：撤单</li>
     * <li>7：需人工介入</li>
     * <li>8：申请部分完单中</li>
     */
    private Integer status;
    /**
     * 撤单原因
     *
     <pre>
     100，交易完成
     101，更新收货数量
     102, 卖家申请部分完单
     200，需人工介入
     300，其他情况
     301，交易超时
     302，背包已满
     303，不是附魔师
     400, 收货商没来,等待过久
     401, 订单下错区服
     402, 自动收货机器故障(总是登陆异常)
     403, 收货角色名/等级不正确
     404, 收货角色库存上限
     405, 收货角色安全模式
     406, 收货角色频繁更改/上下号
     407, 价格变动不想出了
     408, 其他原因
     501，出货商30分钟内未进行交易
     502，排队超时,
     </pre>
     */
    private Integer reason;
    /**
     * 其他原因
     */
    private String otherReason;
    /**
     * 转账状态
     * <li>0：待初始化</li>
     * <li>1：待转账</li>
     * <li>2：已转账</li>
     */
    private Integer transferStatus;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 开始排队时间
     */
    private Date queueStartTime;
    /**
     * 开始交易时间
     */
    private Date tradeStartTime;
    /**
     * 交易完成时间
     */
    private Date tradeEndTime;
    /**
     * 转账时间
     */
    private Date transferTime;


    private Integer deliveryType; //收货方式
    private Integer tradeType; //交易方式
    private String deliveryTypeName;
    private String tradeTypeName;
    private String buyerPhone;
    private String serviceQq; //客服qq号
    private String serviceNickname; //客服昵称

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

    //出货方环信账号
    private String sellerHxAccount;

    //出货方环信密码

    private String sellerHxPwd;


    //收货方环信id
    private String buyerHxAccount;

    //收货方环信密码

    private String buyerHxPwd;

    //系统管理员环信账号
    private String adminHxAccount;

    private String adminHxPwd;

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
     * 出货商IP
     * */
    private String sellerIp;

    /**
     * 申诉单关联订单号
     */
    private String appealOrder;

    /**
     * 申诉单申诉原因
     */
    private String appealReason;

    /**
     * 原始关联单号
     */
    private String relationOrderId;

    //出货商角色等级
    private Integer sellerRoleLevel;

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

    public Integer getSellerRoleLevel() {
        return sellerRoleLevel;
    }

    public void setSellerRoleLevel(Integer sellerRoleLevel) {
        this.sellerRoleLevel = sellerRoleLevel;
    }

    public String getAppealReason() {
        return appealReason;
    }

    public void setAppealReason(String appealReason) {
        this.appealReason = appealReason;
    }

    public String getAppealOrder() {
        return appealOrder;
    }

    public void setAppealOrder(String appealOrder) {
        this.appealOrder = appealOrder;
    }

    public String getSellerIp() {
        return sellerIp;
    }

    public void setSellerIp(String sellerIp) {
        this.sellerIp = sellerIp;
    }

    public DeliveryOrder() {
    }

    public static int getNotTransfer() {
        return NOT_TRANSFER;
    }

    public String getAdminHxAccount() {
        return adminHxAccount;
    }

    public void setAdminHxAccount(String adminHxAccount) {
        this.adminHxAccount = adminHxAccount;
    }

    public String getAdminHxPwd() {
        return adminHxPwd;
    }

    public void setAdminHxPwd(String adminHxPwd) {
        this.adminHxPwd = adminHxPwd;
    }

    public String getSellerHxAccount() {
        return sellerHxAccount;
    }

    public void setSellerHxAccount(String sellerHxAccount) {
        this.sellerHxAccount = sellerHxAccount;
    }

    public String getSellerHxPwd() {
        return sellerHxPwd;
    }

    public void setSellerHxPwd(String sellerHxPwd) {
        this.sellerHxPwd = sellerHxPwd;
    }

    public String getBuyerHxAccount() {
        return buyerHxAccount;
    }

    public void setBuyerHxAccount(String buyerHxAccount) {
        this.buyerHxAccount = buyerHxAccount;
    }

    public String getBuyerHxPwd() {
        return buyerHxPwd;
    }

    public void setBuyerHxPwd(String buyerHxPwd) {
        this.buyerHxPwd = buyerHxPwd;
    }

    public String getServiceQq() {
        return serviceQq;
    }

    public void setServiceQq(String serviceQq) {
        this.serviceQq = serviceQq;
    }

    public String getServiceNickname() {
        return serviceNickname;
    }

    public void setServiceNickname(String serviceNickname) {
        this.serviceNickname = serviceNickname;
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

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
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

    public Long getCgId() {
        return cgId;
    }

    public void setCgId(Long cgId) {
        this.cgId = cgId;
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

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public String getMoneyName() {
        return moneyName;
    }

    public void setMoneyName(String moneyName) {
        this.moneyName = moneyName;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getRealCount() {
        return realCount;
    }

    public void setRealCount(Long realCount) {
        this.realCount = realCount;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTradeStartTime() {
        return tradeStartTime;
    }

    public void setTradeStartTime(Date tradeStartTime) {
        this.tradeStartTime = tradeStartTime;
    }

    public Date getTradeEndTime() {
        return tradeEndTime;
    }

    public void setTradeEndTime(Date tradeEndTime) {
        this.tradeEndTime = tradeEndTime;
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

    public Date getQueueStartTime() {
        return queueStartTime;
    }

    public void setQueueStartTime(Date queueStartTime) {
        this.queueStartTime = queueStartTime;
    }

    public String getSellerUid() {
        return sellerUid;
    }

    public void setSellerUid(String sellerUid) {
        this.sellerUid = sellerUid;
    }

    public String getBuyerUid() {
        return buyerUid;
    }

    public void setBuyerUid(String buyerUid) {
        this.buyerUid = buyerUid;
    }

    public Integer getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(Integer transferStatus) {
        this.transferStatus = transferStatus;
    }

    public Date getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

    public Boolean getIsTimeout() {
        return isTimeout;
    }

    public void setIsTimeout(Boolean isTimeout) {
        this.isTimeout = isTimeout;
    }

    public String getBuyerTel() {
        return buyerTel;
    }

    public void setBuyerTel(String buyerTel) {
        this.buyerTel = buyerTel;
    }

    public String getOrderStatusStr(){
        return DeliveryOrderStatus.getNameByCode(status);
    }

    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }

    public Long getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Long goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
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

    public String getRelationOrderId() {
        return relationOrderId;
    }

    public void setRelationOrderId(String relationOrderId) {
        this.relationOrderId = relationOrderId;
    }
}
