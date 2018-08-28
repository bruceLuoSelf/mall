package com.wzitech.gamegold.facade.frontend.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

public class EvaluateRequest  extends AbstractServiceResponse {
    /**
     * 订单号
     */
    private String orderId;

    /**
     * 商品数量评分
     */
    private int score1;

    /**
     * 服务态度评分
     */
    private int score2;

    /**
     * 发货速度评分
     */
    private int score3;

    /**
     * 评价内容
     */
    private String remark;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public int getScore3() {
        return score3;
    }

    public void setScore3(int score3) {
        this.score3 = score3;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
