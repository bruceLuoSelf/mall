package com.wzitech.gamegold.common.enums;

/**
 * 出货单状态
 */
public enum SystemConfigEnum {
    /**
     * 采购商余额小于该值提醒
     */
    BALANCE_REMIND_LINE("BALANCE_REMIND_LINE", "采购商余额小于该值提醒"),
    /**
     * 采购商余额小于该值，停止收货
     */
    BALANCE_STOP_LINE("BALANCE_STOP_LINE", "采购商余额小于该值，停止收货"),
    /**
     * 充值单最小充值金额
     */
    RECHARGE_MIN("RECHARGE_MIN", "充值单最小充值金额"),
    /**
     * 机器超时时间自动转人工
     */
    AUTOMATE_TIMEOUT("AUTOMATE_TIMEOUT", "机器超时时间自动转人工"),
    /**
     * 其他厂商的token过期时间
     */
    FIRMS_TIMEOUT("FIRMS_TIMEOUT","其他厂商的token过期时间"),

    /**
     * 收货自动退充值单
     */
    AUTO_TIMEOUT_PAY_ORDER("AUTO_TIMEOUT_PAY_ORDER","收货自动退充值单"),

    /**
     * 是否发短信
     */
    SEND_MSG("SEND_MSG","是否发短信"),

    /**
     * 是否开启环信
     */
    HUANXIN_OPEN("HUANXIN_OPEN","是否开启主站环信"),

    //zhulf  start
    /**
     * 默认通讯方式
     */
    DEFAULT_IM("DEFAULT_IM","默认通讯方式"),
    //end

    /**
     *  订单中心JOB锁定时间
     */
    ORDER_CENTER_DELIVER_TIME("ORDER_CENTER_DELIVER_TIME","订单中心JOB锁定时间"),

    /**
     * 是否启用提供新版接口给M站（新版接口参数多一个IP）
     * 当value值为1的时候 表示新的验证方式 value值为2的时候 表示沿用旧的验证方式
     * */
    OPEN_OR_CLOSE_INTERFACE_FOR_M("OPEN_OR_CLOSE_INTERFACE_FOR_M","是否启用提供新版接口给M站"),

    /**
     * 是否给APP增加商品种类验证
     */
    NEW_ENCRYPT_GOODSTYPENAME_FOR_APP_OR_NOT("NEW_ENCRYPT_GOODSTYPENAME_FOR_APP_OR_NOT","APP是否采用商品种类作验证"),

    /**
     * 邮寄收货阈值
     * */
    MAIL_ROBOT_PURCHASE("MAIL_ROBOT_PURCHASE","邮寄收货误差阈值"),

    /**
     * 收货后台配置的QQ
     * */
    DELIVERY_SERVICE_QQ("DELIVERY_SERVICE_QQ","收货后台配置的客户QQ"),

    /**
     * Order overtime Prohibition of complaints
     * */
    OVER_TIME("OVER_TIME","订单交易结束超过这个时间后禁止申诉"),

    /**
     * 机器收货交易超时阈值
     * */
    TRADE_TIMEOUT("TRADE_TIMEOUT","机器收货交易超时转人工"),


    /**
     * 邮寄收货自动化响应超时时间
     * */
    SELLER_DELIVERY_TIMEOUT("SELLER_DELIVERY_TIMEOUT","邮寄收货自动化响应超时时间"),

    /**
     * m站下单提示信息
     * */
    M_ADD_ORDER_MSG("M_ADD_ORDER_MSG","m站下单提示信息"),

    /**
     * APP新接口切换
     */
    APP_SWITCH("APP_SWITCH","app新接口切换"),

    /**
     * 订单掉单补偿时间
     * */
    COMPENSATE_ORDER_TIME("COMPENSATE_ORDER_TIME","订单掉单补偿时间"),

    /**
     * 接口频率限制
     */
    ACCESS_LIMIT_COUNT("ACCESS_LIMIT_COUNT","接口频率限制"),


    /**
     * 自动化获取查收邮件配置时间
     */

    ROBOT_CONGIGTIME("ROBOT_CONGIGTIME","自动化获取查收邮件配置时间"),

    /**
     * 分仓总开关
     */
    FC_SWITCH("FC_SWITCH","分仓总开关"),

    ;


    private String key;

    private String remark;

    SystemConfigEnum(String key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    /**
     * 根据key值获取对应的枚举
     * @param key
     * @return
     */
    public static SystemConfigEnum getTypeByCode(String key){
        for(SystemConfigEnum status : SystemConfigEnum.values()){
            if(status.getKey().equals(key)){
                return status;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的SystemConfigEnum:" + key);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
