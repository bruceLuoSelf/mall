package com.wzitech.gamegold.shorder.entity;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.util.Date;

/**
 * 交易类目
 */


public class GoodsType extends BaseEntity {

    /**
     * 交易类目id
     */
    public Long id;
    /**
     * 交易类目名
     */
    public String name;
    /**
     * 是否可用
     */
    public Boolean enabled;
    /**
     * 创建时间
     */
    public Date createTime;
    /**
     * 更新时间
     */
    public Date updateTime;

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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
