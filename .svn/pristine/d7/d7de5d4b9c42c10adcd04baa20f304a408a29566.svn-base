/************************************************************************************
 * Copyright 2014 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		RepositoryInfo
 * 包	名：		com.wzitech.gamegold.repository.entity
 * 项目名称：	gamegold-repository
 * 作	者：		SunChengfei
 * 创建时间：	2014-2-15
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014-2-15 下午12:55:11
 ************************************************************************************/
package com.wzitech.gamegold.repository.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.gamegold.common.constants.ServicesContants;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存信息EO
 * Created by SunChengfei
 *
 *  Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/12  wubiao           ZW_C_JB_00008 商城增加通货
 */
public class RepositoryInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 卖家登录5173账号
     */
    private String loginAccount;

    /**
     * 卖家登录5173账号uid
     */
    private String accountUid;

    private SellerInfo sellerInfo;

    /**
     * 所属客服id
     */
    private Long servicerId;

    /**
     * 游戏账号
     */
    private String gameAccount;

    /**
     * 游戏密码
     */
    private String gamePassWord;

    /**
     * 子账号密码
     */
    private String sonGamePassWord;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 所在区
     */
    private String region;

    /**
     * 所在服
     */
    private String server;

    @Override
    public String toString() {
        return "RepositoryInfo{" +
                "loginAccount='" + loginAccount + '\'' +
                ", accountUid='" + accountUid + '\'' +
                ", sellerInfo=" + sellerInfo +
                ", servicerId=" + servicerId +
                ", gameAccount='" + gameAccount + '\'' +
                ", gamePassWord='" + gamePassWord + '\'' +
                ", sonGamePassWord='" + sonGamePassWord + '\'' +
                ", gameName='" + gameName + '\'' +
                ", region='" + region + '\'' +
                ", server='" + server + '\'' +
                ", sonAccount='" + sonAccount + '\'' +
                ", sellerGameRole='" + sellerGameRole + '\'' +
                ", moneyName='" + moneyName + '\'' +
                //ZW_C_JB_00008_20170512_START*******/
                ", goodsTypeName='" + goodsTypeName + '\'' +
                ", goodsTypeId='" + goodsTypeId + '\'' +
                //ZW_C_JB_00008_20170512_END*******/
                ", gameRace='" + gameRace + '\'' +
                ", goldCount=" + goldCount +
                ", sellableCount=" + sellableCount +
                ", unitPrice=" + unitPrice +
                ", passpodUrl='" + passpodUrl + '\'' +
                ", rolePassword='" + rolePassword + '\'' +
                ", fundsPassword='" + fundsPassword + '\'' +
                ", housePassword='" + housePassword + '\'' +
                ", lastUpdateTime=" + lastUpdateTime +
                ", createTime=" + createTime +
                ", isDeleted=" + isDeleted +
                ", isShield=" + isShield +
                ", unitPriceSeller=" + unitPriceSeller +
                ", gameId='" + gameId + '\'' +
                ", gameRegionId='" + gameRegionId + '\'' +
                ", gameServerId='" + gameServerId + '\'' +
                ", gameRaceId='" + gameRaceId + '\'' +
                ", isShieldedType=" + isShieldedType +
                '}';
    }

    /**
     * 子账号
     */
    private String sonAccount;

    /**
     * 卖家游戏角色名
     */
    private String sellerGameRole;

    /**
     * 游戏币名
     */
    private String moneyName;

    /**
     * 所在阵营
     */
    private String gameRace;

    /**
     * 游戏币数目
     */
    private Long goldCount;

    /**
     * 可销售库存（目前存在于DNF中）
     */
    private Long sellableCount;

    /**
     * 单价(1游戏币兑换多少元)
     */
    private BigDecimal unitPrice;

    /**
     * 密保卡url
     */
    private String passpodUrl;

    /**
     * 人物密码
     */
    private String rolePassword;

    /**
     * 财产密码
     */
    private String fundsPassword;

    /**
     * 仓库密码
     */
    private String housePassword;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Boolean isDeleted;

    /**
     * 是否屏蔽
     */
    private Boolean isShield;

    private BigDecimal unitPriceSeller;

    private String gameId;
    private String gameRegionId;
    private String gameServerId;
    private String gameRaceId;

    private Boolean isShieldedType;


    /**
     * 通货类目ID
     * wrf 5.11新增
     */
    private Long goodsTypeId;//ZW_C_JB_00008_20170512 MODIFY

    /**
     * 通货类目名称
     * wrf 5.11新增
     */
    private String goodsTypeName;

    /**
     * 增加同步收货时添加的id
     * @return
     */
    private Long shRepositoryId;

    /**
     * 盘库存数
     */
    private Long stockCount;

    public Long getStockCount() {
        return stockCount;
    }

    public void setStockCount(Long stockCount) {
        this.stockCount = stockCount;
    }

    public Long getShRepositoryId() {
        return shRepositoryId;
    }

    public void setShRepositoryId(Long shRepositoryId) {
        this.shRepositoryId = shRepositoryId;
    }

    public Long getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(Long goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    /**
     * 当商品类目为空时，查找类目为游戏币的信息
     * hyl 5.12新增
     */
    public String getGoodsTypeName() {
        goodsTypeName = StringUtils.isBlank(goodsTypeName) ? ServicesContants.GOODS_TYPE_GOLD : goodsTypeName;
        return goodsTypeName;
    }

    public String getOriginalGoodsTypeNmae(){
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }


    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public SellerInfo getSellerInfo() {
        return sellerInfo;
    }

    public void setSellerInfo(SellerInfo sellerInfo) {
        this.sellerInfo = sellerInfo;
    }

    public String getAccountUid() {
        return accountUid;
    }

    public void setAccountUid(String accountUid) {
        this.accountUid = accountUid;
    }

    public Long getServicerId() {
        return servicerId;
    }

    public void setServicerId(Long servicerId) {
        this.servicerId = servicerId;
    }

    public String getGameAccount() {
        return gameAccount;
    }

    public void setGameAccount(String gameAccount) {
        this.gameAccount = gameAccount;
    }

    public String getGamePassWord() {
        return gamePassWord;
    }

    public void setGamePassWord(String gamePassWord) {
        this.gamePassWord = gamePassWord;
    }

    public String getSonGamePassWord() {
        return sonGamePassWord;
    }

    public void setSonGamePassWord(String sonGamePassWord) {
        this.sonGamePassWord = sonGamePassWord;
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

    public String getSonAccount() {
        return sonAccount;
    }

    public void setSonAccount(String sonAccount) {
        this.sonAccount = sonAccount;
    }

    public String getSellerGameRole() {
        return sellerGameRole;
    }

    public void setSellerGameRole(String sellerGameRole) {
        this.sellerGameRole = sellerGameRole;
    }

    public String getMoneyName() {
        if(moneyName!=null){
            moneyName=moneyName.trim();
        }
        return moneyName;
    }

    public void setMoneyName(String moneyName) {
        this.moneyName = moneyName;
    }

    public String getGameRace() {
        return gameRace;
    }

    public void setGameRace(String gameRace) {
        this.gameRace = gameRace;
    }

    public Long getGoldCount() {
        return goldCount;
    }

    public void setGoldCount(Long goldCount) {
        this.goldCount = goldCount;
    }

    public Long getSellableCount() {
        return sellableCount;
    }

    public void setSellableCount(Long sellableCount) {
        this.sellableCount = sellableCount;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the passpodUrl
     */
    public String getPasspodUrl() {
        return passpodUrl;
    }

    /**
     * @param passpodUrl the passpodUrl to set
     */
    public void setPasspodUrl(String passpodUrl) {
        this.passpodUrl = passpodUrl;
    }

    /**
     * @return the rolePassword
     */
    public String getRolePassword() {
        return rolePassword;
    }

    /**
     * @param rolePassword the rolePassword to set
     */
    public void setRolePassword(String rolePassword) {
        this.rolePassword = rolePassword;
    }

    /**
     * @return the fundsPassword
     */
    public String getFundsPassword() {
        return fundsPassword;
    }

    /**
     * @param fundsPassword the fundsPassword to set
     */
    public void setFundsPassword(String fundsPassword) {
        this.fundsPassword = fundsPassword;
    }

    /**
     * @return the housePassword
     */
    public String getHousePassword() {
        return housePassword;
    }

    /**
     * @param housePassword the housePassword to set
     */
    public void setHousePassword(String housePassword) {
        this.housePassword = housePassword;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getIsShield() {
        return isShield;
    }

    public void setIsShield(Boolean isShield) {
        this.isShield = isShield;
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

    public Boolean getIsShieldedType() {
        return isShieldedType;
    }

    public void setIsShieldedType(Boolean isShieldedType) {
        this.isShieldedType = isShieldedType;
    }

    public BigDecimal getUnitPriceSeller() {
        return unitPriceSeller;
    }

    public void setUnitPriceSeller(BigDecimal unitPriceSeller) {
        this.unitPriceSeller = unitPriceSeller;
    }

}
