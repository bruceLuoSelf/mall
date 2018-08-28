package com.wzitech.gamegold.shrobot.service.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 写订单日志请求
 * @author yemq
 */
public class WriteOrderLogRequest extends AbstractServiceRequest {
    /**
     * 主订单号
     */
    private String orderId;

    /**
     * 子订单号
     */
    private Long id;
    /**
     * 类型
     * 0： 普通log，
     1： 角色名+等级+收货数量
     2： 发送交易地点截图（图片路径保存在img中）
     3：发送附魔师等级截图
     9：其它
     */
    private Integer type;
    /**
     * 角色名
     */
    private String roleName;
    /**
     * 等级
     */
    private String level;
    /**
     * 可交易数量
     */
    private Long count;
    /**
     * 截图
     */
    private String img;
    /**
     * 日志内容
     */
    private String log;

    /**
     * 签名
     */
    private String sign;

    public WriteOrderLogRequest() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
