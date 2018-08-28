package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.gamegold.shorder.enums.SplitRepositoryStatus;

import java.util.Date;

/**
 * Created by 339928 on 2018/6/19.
 */
public class SplitRepositorySubRequest extends BaseEntity {
    /**
     * 分仓主订单号
     */
    private String orderId;
    /**
     * 被分仓角色
     */
    private String gameRole;
    /**
     * 缺口值
     */
    private Long count;
    /**
     * 实际值
     */
    private Long realCount;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 使用金库金币数量
     */
    private Long  useRepertoryCount;

    /**
     * 自动化异常原因
     * @return
     */
    private String robotOtherReason;

    public String getRobotOtherReason() {
        return robotOtherReason;
    }

    public void setRobotOtherReason(String robotOtherReason) {
        this.robotOtherReason = robotOtherReason;
    }

    public Long getUseRepertoryCount() {
        return useRepertoryCount;
    }

    public void setUseRepertoryCount(Long useRepertoryCount) {
        this.useRepertoryCount = useRepertoryCount;
    }

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public SplitRepositorySubRequest() {
    }

    public SplitRepositorySubRequest(String orderId, String gameRole, Long count, Date createTime) {
        this.orderId = orderId;
        this.gameRole = gameRole;
        this.count = count;
        this.createTime = createTime;
        this.status= SplitRepositoryStatus.SPLITTING.getCode();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGameRole() {
        return gameRole;
    }

    public void setGameRole(String gameRole) {
        this.gameRole = gameRole;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getRealCount() {
        return realCount;
    }

    public void setRealCount(Long realCount) {
        this.realCount = realCount;
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
