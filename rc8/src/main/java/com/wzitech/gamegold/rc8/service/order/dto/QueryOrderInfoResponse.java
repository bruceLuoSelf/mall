package com.wzitech.gamegold.rc8.service.order.dto;

import com.wzitech.gamegold.rc8.dto.Response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.List;

/**
 * 查询订单详情响应
 *
 * @author yemq
 */
@XmlRootElement(name = "Result")
@XmlType(propOrder = {"order", "subOrderList", "statisticalData"})
public class QueryOrderInfoResponse extends Response {
    /**
     * 主订单信息
     */
    private Order order;
    /**
     * 子订单信息
     */
    private List<SubOrder> subOrderList;
    /**
     * 统计数据
     */
    private StatisticalData statisticalData;

    public QueryOrderInfoResponse() {
    }

    @XmlElement(name = "Order")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @XmlElementWrapper(name = "SubOrderList")
    @XmlElement(name = "SubOrder")
    public List<SubOrder> getSubOrderList() {
        return subOrderList;
    }

    public void setSubOrderList(List<SubOrder> subOrderList) {
        this.subOrderList = subOrderList;
    }

    @XmlElement(name = "StatisticalData")
    public StatisticalData getStatisticalData() {
        return statisticalData;
    }

    public void setStatisticalData(StatisticalData statisticalData) {
        this.statisticalData = statisticalData;
    }

    /**
     * 统计数据
     */
    @XmlRootElement(name = "StatisticalData")
    @XmlType(propOrder = {
        "goldCount","price", "totalPrice","income","commission","orderPrice","orderTotalPrice","balance"
    })
    public static class StatisticalData {
        /**
         * 游戏币数量
         */
        private Long goldCount;
        /**
         * 库存单价
         */
        private BigDecimal price;
        /**
         * 配置入库总额
         */
        private BigDecimal totalPrice;
        /**
         * 卖家收益
         */
        private BigDecimal income;
        /**
         * 卖家佣金
         */
        private BigDecimal commission;
        /**
         * 订单单价
         */
        private BigDecimal orderPrice;
        /**
         * 配置出库总额
         */
        private BigDecimal orderTotalPrice;
        /**
         * 差额收入
         */
        private BigDecimal balance;

        public StatisticalData(){}

        public Long getGoldCount() {
            return goldCount;
        }

        public void setGoldCount(Long goldCount) {
            this.goldCount = goldCount;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
        }

        public BigDecimal getIncome() {
            return income;
        }

        public void setIncome(BigDecimal income) {
            this.income = income;
        }

        public BigDecimal getCommission() {
            return commission;
        }

        public void setCommission(BigDecimal commission) {
            this.commission = commission;
        }

        public BigDecimal getOrderPrice() {
            return orderPrice;
        }

        public void setOrderPrice(BigDecimal orderPrice) {
            this.orderPrice = orderPrice;
        }

        public BigDecimal getOrderTotalPrice() {
            return orderTotalPrice;
        }

        public void setOrderTotalPrice(BigDecimal orderTotalPrice) {
            this.orderTotalPrice = orderTotalPrice;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }
    }

    @XmlRootElement(name = "Order")
    @XmlType(propOrder = {
            "bizOfferTypeName", "title", "orderId", "gameInfo", "buyerInfo", "serviceInfo", "tradeTypeDesc",
            "namedPlacesOfDelivery", "howMuchTimeDelivery", "goldCount", "goldUnit", "price", "totalPrice",
            "orderStatus", "isDelay", "isHaveStore", "createTime", "paidTime", "sendTime", "endTime", "remark","goodsTypeName","goodsTypeId","digitalId","gameGrade","fields"
    })
    public static class Order {
        /**
         * 发布单物品类别（装备，游戏币，其它）
         */
        private String bizOfferTypeName = "游戏币";
        /**
         * 发布单名称
         */
        private String title;
        /**
         * 订单号
         */
        private String orderId;
        /**
         * 游戏信息
         */
        private GameInfo gameInfo;
        /**
         * 买家信息
         */
        private BuyerInfo buyerInfo;
        /**
         * 客服信息
         */
        private CustomerService serviceInfo;
        /**
         * 交易方式（当面，邮寄）
         */
        private String tradeTypeDesc;
        /**
         * 固定交易地点列表（多条以“|”号分隔）
         */
        private String namedPlacesOfDelivery;
        /**
         * 多少时间内发货(单位：分钟)
         */
        private String howMuchTimeDelivery;
        /**
         * 购买的游戏币数量
         */
        private Long goldCount;
        /**
         * 游戏币单位
         */
        private String goldUnit;
        /**
         * 单价
         */
        private BigDecimal price;
        /**
         * 总价
         */
        private BigDecimal totalPrice;
        /**
         * 订单状态
         */
        private Integer orderStatus;
        /**
         * 是否延迟
         */
        private Boolean isDelay;
        /**
         * 是否有货
         */
        private Boolean isHaveStore;
        /**
         * 创建时间
         */
        private String createTime;
        /**
         * 付款时间
         */
        private String paidTime;
        /**
         * 发货时间
         */
        private String sendTime;
        /**
         * 结单时间
         */
        private String endTime;
        /**
         * 备注
         */
        private String remark;

        /**
         * 通货类型ID
         * lvchengsheng 5.16新增 ZW_C_JB_00008 商城增加通货
         */
        private Long goodsTypeId;

        /**
         * 通货类型名称
         * lvchengsheng 5.16新增 ZW_C_JB_00008 商城增加通货
         */
        private String goodsTypeName;

        /**
         *商品数字ID
         */
        private String digitalId;

        /**
         * 游戏等级
         */
        private Integer gameGrade;

        /**
         * 动态属性
         */
        private String fields;

        public String getFields() {
            return fields;
        }

        public void setFields(String fields) {
            this.fields = fields;
        }

        public Integer getGameGrade() {
            return gameGrade;
        }

        public void setGameGrade(Integer gameGrade) {
            this.gameGrade = gameGrade;
        }

        public String getDigitalId() {
            return digitalId;
        }

        public void setDigitalId(String digitalId) {
            this.digitalId = digitalId;
        }

        public Long getGoodsTypeId() {
            return goodsTypeId;
        }

        public void setGoodsTypeId(Long goodsTypeId) {
            this.goodsTypeId = goodsTypeId;
        }

        public String getGoodsTypeName() {
            return goodsTypeName;
        }

        public void setGoodsTypeName(String goodsTypeName) {
            this.goodsTypeName = goodsTypeName;
        }

        public Order() {
        }

        public String getBizOfferTypeName() {
            return bizOfferTypeName;
        }

        public void setBizOfferTypeName(String bizOfferTypeName) {
            this.bizOfferTypeName = bizOfferTypeName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public GameInfo getGameInfo() {
            return gameInfo;
        }

        public void setGameInfo(GameInfo gameInfo) {
            this.gameInfo = gameInfo;
        }

        public BuyerInfo getBuyerInfo() {
            return buyerInfo;
        }

        public void setBuyerInfo(BuyerInfo buyerInfo) {
            this.buyerInfo = buyerInfo;
        }

        public CustomerService getServiceInfo() {
            return serviceInfo;
        }

        public void setServiceInfo(CustomerService serviceInfo) {
            this.serviceInfo = serviceInfo;
        }

        public String getTradeTypeDesc() {
            return tradeTypeDesc;
        }

        public void setTradeTypeDesc(String tradeTypeDesc) {
            this.tradeTypeDesc = tradeTypeDesc;
        }

        public String getNamedPlacesOfDelivery() {
            return namedPlacesOfDelivery;
        }

        public void setNamedPlacesOfDelivery(String namedPlacesOfDelivery) {
            this.namedPlacesOfDelivery = namedPlacesOfDelivery;
        }

        public String getHowMuchTimeDelivery() {
            return howMuchTimeDelivery;
        }

        public void setHowMuchTimeDelivery(String howMuchTimeDelivery) {
            this.howMuchTimeDelivery = howMuchTimeDelivery;
        }

        public Long getGoldCount() {
            return goldCount;
        }

        public void setGoldCount(Long goldCount) {
            this.goldCount = goldCount;
        }

        public String getGoldUnit() {
            return goldUnit;
        }

        public void setGoldUnit(String goldUnit) {
            this.goldUnit = goldUnit;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
        }

        public Integer getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(Integer orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPaidTime() {
            return paidTime;
        }

        public void setPaidTime(String paidTime) {
            this.paidTime = paidTime;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public Boolean getIsDelay() {
            return isDelay;
        }

        public void setIsDelay(Boolean isDelay) {
            this.isDelay = isDelay;
        }

        public Boolean getIsHaveStore() {
            return isHaveStore;
        }

        public void setIsHaveStore(Boolean isHaveStore) {
            this.isHaveStore = isHaveStore;
        }
    }

    /**
     * 订单详情
     */
    @XmlRootElement(name = "SubOrder")
    @XmlType(propOrder = {
            "id", "configGoldCount", "orderUnitPrice", "repositoryUnitPrice", "totalPrice", "orderTotalPrice",
            "orderStatus", "isConsignment", "isHelper", "trader", "sellerInfo", "income", "commission", "balance",
            "createTime"
    })
    public static class SubOrder {
        private Long id;
        /**
         * 购买的游戏币数量
         */
        private Long configGoldCount;
        /**
         * 订单单价
         */
        private BigDecimal orderUnitPrice;
        /**
         * 库存单价
         */
        private BigDecimal repositoryUnitPrice;
        /**
         * 配置入库总额
         */
        private BigDecimal totalPrice;
        /**
         * 配置出库总额
         */
        private BigDecimal orderTotalPrice;
        /**
         * 订单状态
         */
        private Integer orderStatus;
        /**
         * 是否寄售订单
         */
        private Boolean isConsignment;
        /**
         * 是否小助手订单
         */
        private Boolean isHelper;
        /**
         * 交易员信息
         */
        private TraderInfo trader;
        /**
         * 卖家信息
         */
        private SellerInfo sellerInfo;
        /**
         * 卖家收益
         */
        private BigDecimal income;
        /**
         * 卖家佣金
         *
         * @return
         */
        private BigDecimal commission;
        /**
         * 差额
         *
         * @return
         */
        private BigDecimal balance;

        /**
         * 创建时间
         */
        private String createTime;

        public SubOrder() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getConfigGoldCount() {
            return configGoldCount;
        }

        public void setConfigGoldCount(Long configGoldCount) {
            this.configGoldCount = configGoldCount;
        }

        public BigDecimal getRepositoryUnitPrice() {
            return repositoryUnitPrice;
        }

        public void setRepositoryUnitPrice(BigDecimal repositoryUnitPrice) {
            this.repositoryUnitPrice = repositoryUnitPrice;
        }

        public BigDecimal getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
        }

        public Integer getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(Integer orderStatus) {
            this.orderStatus = orderStatus;
        }

        public SellerInfo getSellerInfo() {
            return sellerInfo;
        }

        public void setSellerInfo(SellerInfo sellerInfo) {
            this.sellerInfo = sellerInfo;
        }

        public BigDecimal getIncome() {
            return income;
        }

        public void setIncome(BigDecimal income) {
            this.income = income;
        }

        public BigDecimal getCommission() {
            return commission;
        }

        public void setCommission(BigDecimal commission) {
            this.commission = commission;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public BigDecimal getOrderUnitPrice() {
            return orderUnitPrice;
        }

        public void setOrderUnitPrice(BigDecimal orderUnitPrice) {
            this.orderUnitPrice = orderUnitPrice;
        }

        public BigDecimal getOrderTotalPrice() {
            return orderTotalPrice;
        }

        public void setOrderTotalPrice(BigDecimal orderTotalPrice) {
            this.orderTotalPrice = orderTotalPrice;
        }

        public Boolean getIsConsignment() {
            return isConsignment;
        }

        public void setIsConsignment(Boolean isConsignment) {
            this.isConsignment = isConsignment;
        }

        public Boolean getIsHelper() {
            return isHelper;
        }

        public void setIsHelper(Boolean isHelper) {
            this.isHelper = isHelper;
        }

        public TraderInfo getTrader() {
            return trader;
        }

        public void setTrader(TraderInfo trader) {
            this.trader = trader;
        }
    }

    /**
     * 游戏信息
     */
    @XmlRootElement(name = "GameInfo")
    public static class GameInfo {
        /**
         * 游戏名
         */
        private String game;
        private String gameId;
        /**
         * 游戏区
         */
        private String gameRegion;
        private String gameRegionId;
        /**
         * 游戏服务器
         */
        private String gameServer;
        private String gameServerId;
        /**
         * 游戏阵营
         */
        private String gameRace;
        private String gameRaceId;



        public GameInfo() {
        }

        public String getGame() {
            return game;
        }

        public void setGame(String game) {
            this.game = game;
        }

        public String getGameRegion() {
            return gameRegion;
        }

        public void setGameRegion(String gameRegion) {
            this.gameRegion = gameRegion;
        }

        public String getGameServer() {
            return gameServer;
        }

        public void setGameServer(String gameServer) {
            this.gameServer = gameServer;
        }

        public String getGameRace() {
            return gameRace;
        }

        public void setGameRace(String gameRace) {
            this.gameRace = gameRace;
        }

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }

        public String getGameRegionId() {
            return gameRegionId;
        }

        public void setGameRegionId(String gameRegionId) {
            this.gameRegionId = gameRegionId;
        }

        public String getGameServerId() {
            return gameServerId;
        }

        public void setGameServerId(String gameServerId) {
            this.gameServerId = gameServerId;
        }

        public String getGameRaceId() {
            return gameRaceId;
        }

        public void setGameRaceId(String gameRaceId) {
            this.gameRaceId = gameRaceId;
        }
    }

    /**
     * 客服信息
     */
    @XmlRootElement(name = "CustomerService")
    public static class CustomerService {
        /**
         * 客服ID
         */
        private Long id;
        /**
         * 客服昵称
         */
        private String name;
        /**
         * 客服真实名称
         */
        private String realName;
        /**
         * 客服账号
         */
        private String account;

        public CustomerService() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }
    }

    /**
     * 买家信息
     */
    @XmlRootElement(name = "BuyerInfo")
    public static class BuyerInfo {
        /**
         * 买家账号
         */
        private String account;
        /**
         * 买家电话
         */
        private String phone;
        /**
         * 买家QQ
         */
        private String qq;

        /**
         * 买家游戏角色名
         */
        private String gameRole;
        /**
         * 买家游戏角色等级
         */
        private Integer gameLevel;

        public BuyerInfo() {
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
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

        public String getGameRole() {
            return gameRole;
        }

        public void setGameRole(String gameRole) {
            this.gameRole = gameRole;
        }

        public Integer getGameLevel() {
            return gameLevel;
        }

        public void setGameLevel(Integer gameLevel) {
            this.gameLevel = gameLevel;
        }
    }

    /**
     * 卖家信息
     */
    @XmlRootElement(name = "SellerInfo")
    public static class SellerInfo {
        /**
         * 5173账号
         */
        private String account;
        /**
         * 卖家
         */
        private String name;
        /**
         * 卖家电话
         */
        private String phone;
        /**
         * 卖家QQ
         */
        private String qq;
        /**
         * 卖家游戏账号
         */
        private String gameAccount;
        /**
         * 卖家游戏密码
         */
        private String gamePassword;
        /**
         * 卖家游戏二级密码
         */
        private String gameSecondaryPassword;
        /**
         * 卖家游戏角色名
         */
        private String gameRole;

        /**
         * 是否有密保卡
         */
        private Boolean hasPassport;

        public SellerInfo() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getGameAccount() {
            return gameAccount;
        }

        public void setGameAccount(String gameAccount) {
            this.gameAccount = gameAccount;
        }

        public String getGamePassword() {
            return gamePassword;
        }

        public void setGamePassword(String gamePassword) {
            this.gamePassword = gamePassword;
        }

        public String getGameSecondaryPassword() {
            return gameSecondaryPassword;
        }

        public void setGameSecondaryPassword(String gameSecondaryPassword) {
            this.gameSecondaryPassword = gameSecondaryPassword;
        }

        public String getGameRole() {
            return gameRole;
        }

        public void setGameRole(String gameRole) {
            this.gameRole = gameRole;
        }

        public Boolean getHasPassport() {
            return hasPassport;
        }

        public void setHasPassport(Boolean hasPassport) {
            this.hasPassport = hasPassport;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }

    /**
     * 交易员信息
     */
    @XmlRootElement(name = "TraderInfo")
    public static class TraderInfo {
        /**
         * 交易员ID
         */
        private Long id;
        /**
         * 交易员账号
         */
        private String account;
        /**
         * 交易员姓名
         */
        private String name;

        public TraderInfo() {}

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
