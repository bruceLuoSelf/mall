package com.wzitech.gamegold.facade.frontend.service.chorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 创建出货单请求
 */
public class CreateDeliveryOrderRequest extends AbstractServiceRequest {
    /**
     * 采购单ID
     */
    private Long cgId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 出货数量
     */
    private Long count;
    /**
     * 交易地点
     */
    private String address;
    /**
     * 出货方手机
     */
    private String phone;
    /**
     * 出货方QQ
     */
    private String qq;
    /**
     * 密语
     */
    private String words;

    /**
     * 收货模式
     */
    private Integer deliveryType;
    /**
     * 收货方式
     */
    private Integer tradeType;

    //出货商角色等级
    private Integer sellerRoleLevel;

    public Integer getSellerRoleLevel() {
        return sellerRoleLevel;
    }

    public void setSellerRoleLevel(Integer sellerRoleLevel) {
        this.sellerRoleLevel = sellerRoleLevel;
    }
    /**
     * 收货方式name
     * @return
     */
    private String tradeTypeName;

    /**
     * 申诉单申诉单号 子订单单号
     * @return
     */

    private String appealOrder;

    /**
     * 申诉单申诉原因
     */
    private String appealReason;

    /**
     * 收货方式
     */
    private Integer tradeLogo;

    public Integer getTradeLogo() {
        return tradeLogo;
    }

    public void setTradeLogo(Integer tradeLogo) {
        this.tradeLogo = tradeLogo;
    }

    public String getAppealReason() {
        return appealReason;
    }

    public void setAppealReason(String appealReason) {
        this.appealReason = appealReason;
    }

    public String getAppealOrder() {
        return appealOrder;
    }

    public void setAppealOrder(String appealOrder) {
        this.appealOrder = appealOrder;
    }

    public Long getCgId() {
        return cgId;
    }

    public void setCgId(Long cgId) {
        this.cgId = cgId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeTypeName() {
        return tradeTypeName;
    }

    public void setTradeTypeName(String tradeTypeName) {
        this.tradeTypeName = tradeTypeName;
    }
}
