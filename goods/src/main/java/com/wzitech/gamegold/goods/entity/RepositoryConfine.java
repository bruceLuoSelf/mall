package com.wzitech.gamegold.goods.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by jhlcitadmin on 2017/3/16.
 */
public class RepositoryConfine extends BaseEntity {
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
    private Boolean isEnabled;

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

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}
