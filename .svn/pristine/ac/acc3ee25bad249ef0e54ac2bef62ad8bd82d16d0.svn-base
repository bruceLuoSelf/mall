package com.wzitech.gamegold.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.sf.json.JSONArray;

import java.math.BigDecimal;

/**
 * Created by chengXY on 2017/10/24.
 * 商城订单推送给主站V0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class orderPushVo {

    /**
     * 子订单id
     */
    @JsonProperty("SubId")
    private String subid;


    /**
     * 订单主键
     */
    @JsonProperty("Id")
    private String id;


    /**
     * 游戏ID
     */
    @JsonProperty("GameId")
    private String gameId;

    /**
     * 游戏区ID
     */
    @JsonProperty("GameAreaId")
    private String gameAreaId;

    /**
     * 游戏服ID
     */
    @JsonProperty("GameServerId")
    private String gameServerId;

    /**
     * 物品种类ID
     */
    @JsonProperty("BizOfferTypeId")
    private String bizOfferTypeId;

    /**
     * 卖家ID
     */
    @JsonProperty("SellerId")
    private String sellerId;

    /**
     * 买家ID
     */
    @JsonProperty("BuyerId")
    private String buyerId;

    /**
     * 订单来源
     */
    @JsonProperty("OrderSource")
    private String orderSource;

    /**
     * 交易线
     */
    @JsonProperty("CQTradingType")
    private Integer cqtradingType;

    /**
     * 客户端类型
     */
    @JsonProperty("ClientType")
    private Integer clientType;

    /**
     * 订单创建时间
     */
    @JsonProperty("OrderCreateDate")
    private Long orderCreateDate;



    /**
     * 订单原始状态
     */
    @JsonProperty("OriginOderStatus")
    private Integer originOderStatus;

    /**
     * 业务站点自定义订单数据 （内容为动态性）
     */
    @JsonProperty("JsonData")
    private OrderDataVO jsonData;

    /**
     * 操作员数据 （内容为动态性）
     */
    @JsonProperty("OpJsonData")
    private OrderOpData opJsonData;

    /**
     * 	备注信息
     */
    @JsonProperty("Remarks")
    private String remarks;

    /**
     * 	版本号
     */
    @JsonProperty("CurrentVersion")
    private Integer currentVersion;


    /**
     * 买家服务列表 BuyerSigns
     * */
    @JsonProperty("BuyerSigns")
    private JSONArray newBuyerSigns;

    public JSONArray getNewBuyerSigns() {
        return newBuyerSigns;
    }

    public void setNewBuyerSigns(JSONArray newBuyerSigns) {
        this.newBuyerSigns = newBuyerSigns;
    }

    /**
     * 是否已经支付
     * */
    @JsonProperty("IsPaid")
    private Boolean isPaid;

    @JsonProperty("BizOfferId")
    private String bizOfferId;

    /**
     * 商品单价
     */
    @JsonProperty("Price")
    private BigDecimal price;

    /**
     * 支付价格
     */
    @JsonProperty("PayPrice")
    private BigDecimal payPrice;

    /**
     * 物品销售数量
     * @return
     */
    @JsonProperty("Quantity")
    private String goodsQuantity;

    public String getSubid() {
        return subid;
    }

    public void setSubid(String subid) {
        this.subid = subid;
    }

    public String getGoodsQuantity() {
        return goodsQuantity;
    }

    public void setGoodsQuantity(String goodsQuantity) {
        this.goodsQuantity = goodsQuantity;
    }
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }


    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public String getBizOfferId() {
        return bizOfferId;
    }

    public void setBizOfferId(String bizOfferId) {
        this.bizOfferId = bizOfferId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameServerId() {
        return gameServerId;
    }

    public void setGameServerId(String gameServerId) {
        this.gameServerId = gameServerId;
    }

    public String getBizOfferTypeId() {
        return bizOfferTypeId;
    }

    public void setBizOfferTypeId(String bizOfferTypeId) {
        this.bizOfferTypeId = bizOfferTypeId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public Long getOrderCreateDate() {
        return orderCreateDate;
    }

    public void setOrderCreateDate(Long orderCreateDate) {
        this.orderCreateDate = orderCreateDate;
    }


    public Integer getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(Integer currentVersion) {
        this.currentVersion = currentVersion;
    }

    public OrderDataVO getJsonData() {
        return jsonData;
    }

    public void setJsonData(OrderDataVO jsonData) {
        this.jsonData = jsonData;
    }

    public OrderOpData getOpJsonData() {
        return opJsonData;
    }

    public void setOpJsonData(OrderOpData opJsonData) {
        this.opJsonData = opJsonData;
    }

    public String getGameAreaId() {
        return gameAreaId;
    }

    public void setGameAreaId(String gameAreaId) {
        this.gameAreaId = gameAreaId;
    }

    public Integer getCqtradingType() {
        return cqtradingType;
    }

    public void setCqtradingType(Integer cqtradingType) {
        this.cqtradingType = cqtradingType;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public Integer getOriginOderStatus() {
        return originOderStatus;
    }

    public void setOriginOderStatus(Integer originOderStatus) {
        this.originOderStatus = originOderStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

