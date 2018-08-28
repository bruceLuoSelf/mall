package com.wzitech.gamegold.goods.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by jhlcitadmin on 2017/3/16.
 */
public class ReferencePrice extends BaseEntity {
    private Long id;
    //游戏名称
    private String gameName;
    //游戏区名称
    private String regionName;
    //游戏服名称
    private String serverName;
    //游戏阵营名称
    private String raceName;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //单价
    private BigDecimal unitPrice;
    //总数量
    private BigInteger totalAccount;
    //最低单价的单位
    private String moneyName;
    //游戏商品类型
    private String goodsTypeName;

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public String getMoneyName() {
        return moneyName;
    }

    public void setMoneyName(String moneyName) {
        this.moneyName = moneyName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigInteger getTotalAccount() {
        return totalAccount;
    }

    public void setTotalAccount(BigInteger totalAccount) {
        this.totalAccount = totalAccount;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
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
}
