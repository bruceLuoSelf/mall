package com.wzitech.gamegold.facade.service.receiving.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;

/**ZW_C_JB_00004 sunyang
 * Created by sunyang on 2017/5/7.
 */
@XmlRootElement(name="Order")
public class QueryOrderDetaileDTO {

    public static  final  String FUMO = "附魔";

    public static final String YXB="游戏币";

    private String shYXBMALL = QueryMachineAbnormalTurnManualListItemDTO.SH_YXB_MALL;
    /**
     * 出货单编号
     */
    private String orderId;
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
     * 出货数量
     */
    private Long Quantity;
    /**
     * 出货方5173账号
     */
    private String sellerAccount;
    /**
     * 收货方5173账号
     */
    private String buyerAccount;
    /**
     * 出货方手机号
     */
    private String phone;
    /**
     * 出货单价
     */
    private BigDecimal price;
    /**
     * 交易总价
     */
    private double OriginalPrice;
    /**
     * 交易地点
     */
    private String address;
    /**
     * 实际出货数量
     */
    private Long realCount;
    /**
     * 收货数量
     */
    private Long purchCount;
    /**
     * 实际收货数量
     */
    private Long realpurchCount;
    /**
     * 实际出货金额
     */
    private BigDecimal realAmount;
    /**
     * 交易方式
     */
    private String CustomBuyPatterns;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 收货角色名
     */
    private String roleName;
    /**
     * 游戏登录帐号
     */
    private String Account;
    /**
     * 登录密码（加密）
     */
    private String Password;
    /**
     * 游戏帐号注册信息
     */
    private String AccountRegInfos;
    /**
     * 发布单物品类别（装备，游戏币，其它）
     */
    private String BizOfferTypeName;
    /**
     * 卖家发货角色名称
     */
    private String SellerGameRole;
    /**
     * 买家收货角色名称
     */
    private String BuyerGameRole;
    /**
     * 机器转人工原因
     */
    private String GtrReason;
    /**
     * 收货商电话（GTR使用）
     */
    private String SellerMobile;
    /**
     * 接手物服id
     */
    private String takeOverSubjectId;
    /**
     * 接手物服
     */
    private String takeOverSubject;

    @XmlElement(name = "shYXBMALL")
    public String getShYXBMALL() {
        return shYXBMALL;
    }

    public void setShYXBMALL(String shYXBMALL) {
        this.shYXBMALL = shYXBMALL;
    }

    @XmlElement(name = "OpId")
    public String getTakeOverSubjectId() {
        return takeOverSubjectId;
    }

    public void setTakeOverSubjectId(String takeOverSubjectId) {
        this.takeOverSubjectId = takeOverSubjectId;
    }

    @XmlElement(name = "OpName")
    public String getTakeOverSubject() {
        return takeOverSubject;
    }

    public void setTakeOverSubject(String takeOverSubject) {
        this.takeOverSubject = takeOverSubject;
    }

    @XmlElement(name = "PurchCount")
    public Long getPurchCount() {
        return purchCount;
    }

    public void setPurchCount(Long purchCount) {
        this.purchCount = purchCount;
    }

    @XmlElement(name = "RealpurchCount")
    public Long getRealpurchCount() {
        return realpurchCount;
    }

    public void setRealpurchCount(Long realpurchCount) {
        this.realpurchCount = realpurchCount;
    }

    @XmlElement(name = "OrderId")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @XmlElement(name = "GtrReason")
    public String getGtrReason() {
        return GtrReason;
    }

    public void setGtrReason(String gtrReason) {
        GtrReason = gtrReason;
    }

    @XmlElement(name = "SellerMobile")
    public String getSellerMobile() {
        return SellerMobile;
    }

    public void setSellerMobile(String sellerMobile) {
        SellerMobile = sellerMobile;
    }

    @XmlElement(name = "AccountRegInfo")
    public String getAccountRegInfos() {
        return AccountRegInfos;
    }

    public void setAccountRegInfos(String accountRegInfos) {
        AccountRegInfos = accountRegInfos;
    }

    @XmlElement(name = "BizOfferTypeName")
    public String getBizOfferTypeName() {
        return BizOfferTypeName;
    }

    public void setBizOfferTypeName(String bizOfferTypeName) {
        BizOfferTypeName = bizOfferTypeName;
    }

    @XmlElement(name = "SellerGameRole")
    public String getSellerGameRole() {
        return SellerGameRole;
    }

    public void setSellerGameRole(String sellerGameRole) {
        SellerGameRole = sellerGameRole;
    }

    @XmlElement(name = "BuyerGameRole")
    public String getBuyerGameRole() {
        return BuyerGameRole;
    }

    public void setBuyerGameRole(String buyerGameRole) {
        BuyerGameRole = buyerGameRole;
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

    @XmlElement(name = "OrderStatus")
    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    @XmlElement(name = "Quantity")
    public Long getQuantity() {
        return Quantity;
    }

    public void setQuantity(Long quantity) {
        Quantity = quantity;
    }

    @XmlElement(name = "SellerAccount")
    public String getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    @XmlElement(name = "BuyerAccount")
    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    @XmlElement(name = "Phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @XmlElement(name = "Price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @XmlElement(name = "OriginalPrice")
    public double getOriginalPrice() {
        return OriginalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        OriginalPrice = originalPrice;
    }

    @XmlElement(name = "Address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @XmlElement(name = "RealCount")
    public Long getRealCount() {
        return realCount;
    }

    public void setRealCount(Long realCount) {
        this.realCount = realCount;
    }

    @XmlElement(name = "RealAmount")
    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    @XmlElement(name = "CustomBuyPatterns")
    public String getCustomBuyPatterns() {
        return CustomBuyPatterns;
    }

    public void setCustomBuyPatterns(String customBuyPatterns) {
        CustomBuyPatterns = customBuyPatterns;
    }

    @XmlElement(name = "CreateDate")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @XmlElement(name = "RoleName")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @XmlElement(name = "Account")
    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    @XmlElement(name = "Password")
    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
