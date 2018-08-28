package com.wzitech.gamegold.facade.service.receiving.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by 339928 on 2018/1/2.
 */
@XmlRootElement(name = "Order")
public class QueryOrderDetailMailDTO {

    private String shYXBMALL = QueryMachineAbnormalTurnManualListItemDTO.SH_YXB_MALL;


    public static final String YXB = "游戏币";

    public static final String YOUJI = "邮寄";
    /**
     * 游戏登录帐号
     */
    private String Account;

    /**
     * 商品编号
     */
    private String bizOfferId;

    /**
     * 对应主订单预计收货数量
     */

    private Long orderCount;

    /**
     * 主订单下所有子订单经全自动确认 非人工完单 的最终状态订单数量
     */

    private BigDecimal autoValidationCount;

    /**
     * 出货地点
     */

    private String tradeAddress;

    /**
     * 收货商角色等级
     */
    private Integer buyerRoleLevel;


    /**
     * 收货商电话
     */
    private String buyerMobile;

    /**
     * 收货地点 给空串
     */

    private String goodsAddress;

    /**
     * 已收货数量
     */

    private BigDecimal haveGoodsCount;


    /**
     * 图片列表 GTR图片
     */

    private Urls autoImageUrls;

    /**
     * 游戏名
     */
    private String gameName;

    /**
     * 游戏区
     */
    private String GameAreaName;

    /**
     * 游戏服
     */
    private String GameAreaServer;

    /**
     * 发布单物品类别（装备，游戏币，其它）
     */
    private String BizOfferTypeName;

    /**
     * 出货单价
     */
    private BigDecimal price;

    /**
     * 交易总价
     */
    private BigDecimal OriginalPrice;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 出货数量
     */
    private Long Quantity;

    /**
     * 实际出货数量
     */
    private Long realCount;

    /**
     * 交易方式
     */
    private String CustomBuyPatterns;

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
    private String OrderStatus;

    /**
     * 卖家发货角色名称
     */
    private String SellerGameRole;

    /**
     * 出货商电话（GTR使用）
     */
    private String SellerMobile;

    /**
     * 出货方5173账号
     */
    private String sellerAccount;

    /**
     * 登录密码（加密）
     */
    private String Password;

    /**
     * 出货单编号
     */
    private String orderId;

    /**
     * 收货方5173账号
     */
    private String buyerAccount;

    /**
     * 买家收货角色名称
     */
    private String BuyerGameRole;

    /**
     * 游戏帐号注册信息
     */
    private String AccountRegInfos;

    /**
     * 接手物服id
     */
    private String takeOverSubjectId;
    /**
     * 接手物服
     */
    private String takeOverSubject;

    /**
     * 全自动的日志
     */
    private String GtrLog;
    /**
     * 主订单号
     */
    private String parentOrderId;
    /**
     * 出货商角色等级
     */
    private String SellerRoleLevel;

    private BigDecimal Rate;


    /**
     * 物服上传的图
     */
    private Urls PicUrls;

    /**
     * gtr转人工的原因
     */
    private String GtrReason;

    @XmlElement(name = "GtrReason")
    public String getGtrReason() {
        return GtrReason;
    }

    public void setGtrReason(String gtrReason) {
        GtrReason = gtrReason;
    }

    @XmlElement(name = "PicUrls")
    public Urls getPicUrls() {
        return PicUrls;
    }

    public void setPicUrls(Urls picUrls) {
        PicUrls = picUrls;
    }

    @XmlElement(name = "Rate")
    public BigDecimal getRate() {
        return Rate;
    }

    public void setRate(BigDecimal Rate) {
        this.Rate = Rate;
    }

    @XmlElement(name = "SellerRoleLevel")
    public String getSellerRoleLevel() {
        return SellerRoleLevel;
    }

    public void setSellerRoleLevel(String sellerRoleLevel) {
        SellerRoleLevel = sellerRoleLevel;
    }


    @XmlElement(name = "ParentOrderId")
    public String getParentOrderId() {
        return parentOrderId;
    }

    public void setParentOrderId(String parentOrderId) {
        this.parentOrderId = parentOrderId;
    }

    @XmlElement(name = "GtrLog")
    public String getGtrLog() {
        return GtrLog;
    }

    public void setGtrLog(String gtrLog) {
        GtrLog = gtrLog;
    }

    @XmlElement(name = "shYXBMALL")
    public String getShYXBMALL() {
        return shYXBMALL;
    }

    public void setShYXBMALL(String shYXBMALL) {
        this.shYXBMALL = shYXBMALL;
    }

    @XmlElement(name = "Account")
    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    @XmlElement(name = "BizOfferId")
    public String getBizOfferId() {
        return bizOfferId;
    }

    public void setBizOfferId(String bizOfferId) {
        this.bizOfferId = bizOfferId;
    }

    @XmlElement(name = "OrderCount")
    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    @XmlElement(name = "AutoValidationCount")
    public BigDecimal getAutoValidationCount() {
        return autoValidationCount;
    }

    public void setAutoValidationCount(BigDecimal autoValidationCount) {
        this.autoValidationCount = autoValidationCount;
    }

    @XmlElement(name = "TradeAddress")
    public String getTradeAddress() {
        return tradeAddress;
    }

    public void setTradeAddress(String tradeAddress) {
        this.tradeAddress = tradeAddress;
    }

    @XmlElement(name = "BuyerRoleLevel")
    public Integer getBuyerRoleLevel() {
        return buyerRoleLevel;
    }

    public void setBuyerRoleLevel(Integer buyerRoleLevel) {
        this.buyerRoleLevel = buyerRoleLevel;
    }

    @XmlElement(name = "BuyerMobile")
    public String getBuyerMobile() {
        return buyerMobile;
    }

    public void setBuyerMobile(String buyerMobile) {
        this.buyerMobile = buyerMobile;
    }

    @XmlElement(name = "GoodsAddress")
    public String getGoodsAddress() {
        return goodsAddress;
    }

    public void setGoodsAddress(String goodsAddress) {
        this.goodsAddress = goodsAddress;
    }

    @XmlElement(name = "HaveGoodsCount")
    public BigDecimal getHaveGoodsCount() {
        return haveGoodsCount;
    }

    public void setHaveGoodsCount(BigDecimal haveGoodsCount) {
        this.haveGoodsCount = haveGoodsCount;
    }

    //   @XmlElementRef(name = "AutoImageUrls")

    @XmlElement(name = "AutoImageUrls")
    public Urls getAutoImageUrls() {
        return autoImageUrls;
    }

    public void setAutoImageUrls(Urls autoImageUrls) {
        this.autoImageUrls = autoImageUrls;
    }

    @XmlElement(name = "GameName")
    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @XmlElement(name = "GameAreaName")
    public String getGameAreaName() {
        return GameAreaName;
    }

    public void setGameAreaName(String gameAreaName) {
        GameAreaName = gameAreaName;
    }

    @XmlElement(name = "GameAreaServer")
    public String getGameAreaServer() {
        return GameAreaServer;
    }

    public void setGameAreaServer(String gameAreaServer) {
        GameAreaServer = gameAreaServer;
    }

    @XmlElement(name = "BizOfferTypeName")
    public String getBizOfferTypeName() {
        return BizOfferTypeName;
    }

    public void setBizOfferTypeName(String bizOfferTypeName) {
        BizOfferTypeName = bizOfferTypeName;
    }

    @XmlElement(name = "Price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @XmlElement(name = "OriginalPrice")
    public BigDecimal getOriginalPrice() {
        return OriginalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        OriginalPrice = originalPrice;
    }

    @XmlElement(name = "CreateDate")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @XmlElement(name = "Quantity")
    public Long getQuantity() {
        return Quantity;
    }

    public void setQuantity(Long quantity) {
        Quantity = quantity;
    }

    @XmlElement(name = "RealCount")
    public Long getRealCount() {
        return realCount;
    }

    public void setRealCount(Long realCount) {
        this.realCount = realCount;
    }

    @XmlElement(name = "CustomBuyPatterns")
    public String getCustomBuyPatterns() {
        return CustomBuyPatterns;
    }

    public void setCustomBuyPatterns(String customBuyPatterns) {
        CustomBuyPatterns = customBuyPatterns;
    }

    @XmlElement(name = "OrderStatus")
    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    @XmlElement(name = "SellerGameRole")
    public String getSellerGameRole() {
        return SellerGameRole;
    }

    public void setSellerGameRole(String sellerGameRole) {
        SellerGameRole = sellerGameRole;
    }

    @XmlElement(name = "SellerMobile")
    public String getSellerMobile() {
        return SellerMobile;
    }

    public void setSellerMobile(String sellerMobile) {
        SellerMobile = sellerMobile;
    }

    @XmlElement(name = "SellerAccount")
    public String getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    @XmlElement(name = "Password")
    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @XmlElement(name = "OrderId")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @XmlElement(name = "BuyerAccount")
    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    @XmlElement(name = "BuyerGameRole")
    public String getBuyerGameRole() {
        return BuyerGameRole;
    }

    public void setBuyerGameRole(String buyerGameRole) {
        BuyerGameRole = buyerGameRole;
    }

    @XmlElement(name = "AccountRegInfo")
    public String getAccountRegInfos() {
        return AccountRegInfos;
    }

    public void setAccountRegInfos(String accountRegInfos) {
        AccountRegInfos = accountRegInfos;
    }

    @XmlElement(name = "takeOverSubjectId")
    public String getTakeOverSubjectId() {
        return takeOverSubjectId;
    }

    public void setTakeOverSubjectId(String takeOverSubjectId) {
        this.takeOverSubjectId = takeOverSubjectId;
    }

    @XmlElement(name = "takeOverSubject")
    public String getTakeOverSubject() {
        return takeOverSubject;
    }

    public void setTakeOverSubject(String takeOverSubject) {
        this.takeOverSubject = takeOverSubject;
    }
}
