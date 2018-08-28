package com.wzitech.gamegold.facade.frontend.service.chorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 子订单
 * Created by jhlcitadmin on 2017/1/6.
 */
public class DeliverySubOrderResponse extends AbstractServiceResponse {
    /**
     * 主订单
     */
    private DeliveryOrder deliveryOrder;

    /**
     * 子订单列表
     */
    private List<DeliverySubOrderRequest> subOrderList;

    /**
     * 主订单状态
     */
    private String orderStatus;

    /**
     * 收货角色
     */
    private Set<String> roleNames;

    /**
     * 单位
     */
    private String unitName;

    private String goodsTypeName;

    /**
     * 最小申诉金额
     * */
    private BigDecimal leastAmount;

    /**
     * 黑名单用户
     */
    private String blackUser;
    /**
     * The status of a child order allowed to be operated
     * 1：Order exceed the appeal time and prohibit the complaint
     * */
    private Integer appealOrderStatus;

    public String getBlackUser() {
        return blackUser;
    }

    public void setBlackUser(String blackUser) {
        this.blackUser = blackUser;
    }

    public Integer getAppealOrderStatus() {
        return appealOrderStatus;
    }

    public void setAppealOrderStatus(Integer appealOrderStatus) {
        this.appealOrderStatus = appealOrderStatus;
    }

    public BigDecimal getLeastAmount() {
        return leastAmount;
    }

    public void setLeastAmount(BigDecimal leastAmount) {
        this.leastAmount = leastAmount;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public DeliveryOrder getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public List<DeliverySubOrderRequest> getSubOrderList() {
        return subOrderList;
    }

    public void setSubOrderList(List<DeliverySubOrderRequest> subOrderList) {
        this.subOrderList = subOrderList;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Set<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(Set<String> roleNames) {
        this.roleNames = roleNames;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
