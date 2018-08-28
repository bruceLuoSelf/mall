package com.wzitech.gamegold.shrobot.service.repository.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 修改库存请求
 */
public class ModifyRepositoryRequest extends AbstractServiceRequest{

    /**
     * 分仓订单号
     */
    private String orderId;
    /**
     * 角色名
     */
    private String roleName;
    /**
     * 角色等级
     */
    private Integer roleLevel;
    /**
     * 库存数量
     */
    private Long count;

    /**
     * 附魔师等级
     */
    private Integer fmRoleLevel;

    /**
     * 附魔师库存数量
     */
    private Long fmCount;
    /**
     * 签名
     */
    private String sign;

    public ModifyRepositoryRequest() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }

    public Integer getFmRoleLevel() {
        return fmRoleLevel;
    }

    public void setFmRoleLevel(Integer fmRoleLevel) {
        this.fmRoleLevel = fmRoleLevel;
    }

    public Long getFmCount() {
        return fmCount;
    }

    public void setFmCount(Long fmCount) {
        this.fmCount = fmCount;
    }
}
