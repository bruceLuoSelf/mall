package com.wzitech.gamegold.goods.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by jhlcitadmin on 2017/3/27.
 */
public class RepositoryConfineInfo extends BaseEntity {
    private Long id;
    //游戏名称
    private String gameName;
    //库存数量
    private BigInteger repositoryCount;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //是否可用
    private Boolean enabled;
    //区信息
    private String regionName;
    //服信息
    private String serverName;
    //正营信息
    private String raceName;

    /**
     * 库存限制类型
     * @return
     */
    private String goodsTypeName;

    //-----------------------------------------------------------------------------------------


    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public BigInteger getRepositoryCount() {
        return repositoryCount;
    }

    public void setRepositoryCount(BigInteger repositoryCount) {
        this.repositoryCount = repositoryCount;
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
}
