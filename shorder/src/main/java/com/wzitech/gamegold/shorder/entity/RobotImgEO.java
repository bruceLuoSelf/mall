package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.util.Date;

/**
 * Created by 340032 on 2017/12/29.
 */
public class RobotImgEO extends BaseEntity {
    /**
     * 图片路劲
     */
    private String imgSrc;

    private int imgSource;

    /**
     * 主订单id
     */
    private String orderId;

    /**
     * 子订单id
     */
    private Long subOrderId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *是否展示给用户
     */
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getImgSource() {
        return imgSource;
    }

    public void setImgSource(int imgSource) {
        this.imgSource = imgSource;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(Long subOrderId) {
        this.subOrderId = subOrderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
