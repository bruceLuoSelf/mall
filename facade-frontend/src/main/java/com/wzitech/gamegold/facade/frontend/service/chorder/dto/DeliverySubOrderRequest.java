package com.wzitech.gamegold.facade.frontend.service.chorder.dto;

import com.wzitech.gamegold.shorder.entity.RobotImgEO;

import java.util.List;

/**
 * 子订单
 * Created by jhlcitadmin on 2017/1/6.
 */
public class DeliverySubOrderRequest {
    /**
     * 子订单id
     */
    private Long id;
    /**
     * 子订单orderId
     */
    private String orderId;

    /**
     * 收货角色
     */

    private String gameRole;
    /**
     * 预计收货数量
     */
    private Long count;

    /**
     * 实际收货数量
     */
    private Long realCount;

    /**
     *状态
     */
    private  String status;

    /**
     * 子订单状态
     */
    private Integer subOrderStatus;

    /**
     * seller input count
     * */
    private Long sellerInputCount;

    /**
     * The buyer's game role level
     * */
    private int roleLevel;

    /**
     * Do or not need user confirmation
     * */
    private Boolean waitToConfirm;

    /**
     * 关联申诉单号
     */
    private String appealOrder;

    /**
     * The status of a child order allowed to be operated
     * 1：Order exceed the appeal time and prohibit the complaint
     * */
    private Integer appealOrderStatus;

    /**
     * 子订单对应的申诉单的订单id
     * */
    private String subOrderCorrespondingAppealOrderId;

    private List<RobotImgEO> robotImgList;

    private Integer afterFour;


    /**
     * 机器转人工状态(1.已转人工；2.转人工失败 3.机器上号交易)
     */
    private Integer machineArtificialStatus;

    public Integer getMachineArtificialStatus() {
        return machineArtificialStatus;
    }

    public void setMachineArtificialStatus(Integer machineArtificialStatus) {
        this.machineArtificialStatus = machineArtificialStatus;
    }

    public Integer getAfterFour() {
        return afterFour;
    }

    public void setAfterFour(Integer afterFour) {
        this.afterFour = afterFour;
    }

    public List<RobotImgEO> getRobotImgList() {
        return robotImgList;
    }

    public void setRobotImgList(List<RobotImgEO> robotImgList) {
        this.robotImgList = robotImgList;
    }

    public String getSubOrderCorrespondingAppealOrderId() {
        return subOrderCorrespondingAppealOrderId;
    }

    public void setSubOrderCorrespondingAppealOrderId(String subOrderCorrespondingAppealOrderId) {
        this.subOrderCorrespondingAppealOrderId = subOrderCorrespondingAppealOrderId;
    }

    public Integer getAppealOrderStatus() {
        return appealOrderStatus;
    }

    public void setAppealOrderStatus(Integer appealOrderStatus) {
        this.appealOrderStatus = appealOrderStatus;
    }

    public String getAppealOrder() {
        return appealOrder;
    }

    public void setAppealOrder(String appealOrder) {
        this.appealOrder = appealOrder;
    }

    public Boolean getWaitToConfirm() {
        return waitToConfirm;
    }

    public void setWaitToConfirm(Boolean waitToConfirm) {
        this.waitToConfirm = waitToConfirm;
    }

    public int getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    public Long getSellerInputCount() {
        return sellerInputCount;
    }

    public void setSellerInputCount(Long sellerInputCount) {
        this.sellerInputCount = sellerInputCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSubOrderStatus() {
        return subOrderStatus;
    }

    public void setSubOrderStatus(Integer subOrderStatus) {
        this.subOrderStatus = subOrderStatus;
    }
}
