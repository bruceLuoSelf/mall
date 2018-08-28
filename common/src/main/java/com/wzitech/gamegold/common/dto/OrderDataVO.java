package com.wzitech.gamegold.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.sf.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by chengXY on 2017/10/24.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDataVO {
    /**
     * 买家姓名
     */
    @JsonProperty("BuyerName")
    private String buyerName;

    /**
     * 卖家姓名
     */
    @JsonProperty("SellerName")
    private String sellerName;

    /**
     * 商品标题
     */
    @JsonProperty("BizOfferName")
    private String bizOfferName;

    /**
     * 游戏名称
     */
    @JsonProperty("GameName")
    private String gameName;

    /**
     * 游戏区名称
     */
    @JsonProperty("GameAreaName")
    private String gameAreaName;

    /**
     * 游戏服名称
     */
    @JsonProperty("GameServerName")
    private String gameServerName;

    /**
     * 物品种类名称
     */
    @JsonProperty("BizOfferTypeName")
    private String bizOfferTypeName;

    /**
     * 买家游戏角色名
     */
    @JsonProperty("BuyerGameRole")
    private String buyerGameRole;


    /**
     * 买家游戏账号
     */
    @JsonProperty("BuyerGameAccount")
    private String buyerGameAccount;

    /**
     * 买家QQ
     */
    @JsonProperty("BuyerQQ")
    private String buyerQQ;

    /**
     * 买家电话
     */
    @JsonProperty("BuyerMobile")
    private String buyerMobile;

    /**
     * 卖家游戏账号
     */
    @JsonProperty("SellerGameAccount")
    private String sellerGameAccount;

    /**
     * 卖家游戏角色名
     */
    @JsonProperty("SellerGameRole")
    private String sellerGameRole;

    /**
     * 交易取消原因
     * */
    @JsonProperty("CancelReasons")
    private String cancelReasons;

    /**
     * store picture address
     * */
    @JsonProperty("PicUrlManager")
    private String picUrlManager;

    /**
     * machine delivery
     * */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("MachineDelivery")
    private String machineDelivery;

    /**
     * really trade amount
     * */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("RealAmount")
    private BigDecimal realAmount;

    //DEFAULT
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("Evaluation")
    private int evaluation;

    //买家IP
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("BuyerIp")
    private String buyerIp;

    //买家客户端信息
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ClientInfo")
    private JSONObject clientInfo;

    public String getBuyerIp() {
        return buyerIp;
    }

    public void setBuyerIp(String buyerIp) {
        this.buyerIp = buyerIp;
    }

    public JSONObject getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(JSONObject clientInfo) {
        this.clientInfo = clientInfo;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public String getMachineDelivery() {
        return machineDelivery;
    }

    public void setMachineDelivery(String machineDelivery) {
        this.machineDelivery = machineDelivery;
    }

    public String getPicUrlManager() {
        return picUrlManager;
    }

    public void setPicUrlManager(String picUrlManager) {
        this.picUrlManager = picUrlManager;
    }


    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getBizOfferName() {
        return bizOfferName;
    }

    public void setBizOfferName(String bizOfferName) {
        this.bizOfferName = bizOfferName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameAreaName() {
        return gameAreaName;
    }

    public void setGameAreaName(String gameAreaName) {
        this.gameAreaName = gameAreaName;
    }

    public String getGameServerName() {
        return gameServerName;
    }

    public void setGameServerName(String gameServerName) {
        this.gameServerName = gameServerName;
    }

    public String getBizOfferTypeName() {
        return bizOfferTypeName;
    }

    public void setBizOfferTypeName(String bizOfferTypeName) {
        this.bizOfferTypeName = bizOfferTypeName;
    }

    public String getBuyerGameRole() {
        return buyerGameRole;
    }

    public void setBuyerGameRole(String buyerGameRole) {
        this.buyerGameRole = buyerGameRole;
    }

    public String getBuyerGameAccount() {
        return buyerGameAccount;
    }

    public void setBuyerGameAccount(String buyerGameAccount) {
        this.buyerGameAccount = buyerGameAccount;
    }

    public String getBuyerQQ() {
        return buyerQQ;
    }

    public void setBuyerQQ(String buyerQQ) {
        this.buyerQQ = buyerQQ;
    }

    public String getBuyerMobile() {
        return buyerMobile;
    }

    public void setBuyerMobile(String buyerMobile) {
        this.buyerMobile = buyerMobile;
    }

    public String getSellerGameAccount() {
        return sellerGameAccount;
    }

    public void setSellerGameAccount(String sellerGameAccount) {
        this.sellerGameAccount = sellerGameAccount;
    }

    public String getSellerGameRole() {
        return sellerGameRole;
    }

    public void setSellerGameRole(String sellerGameRole) {
        this.sellerGameRole = sellerGameRole;
    }


    public String getCancelReasons() {
        return cancelReasons;
    }

    public void setCancelReasons(String cancelReasons) {
        this.cancelReasons = cancelReasons;
    }
}


