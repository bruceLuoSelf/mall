package com.wzitech.gamegold.common.enums;

/**
 * Created by chengXY on 2017/10/24.
 */
public enum OrderCenterOrderStatus {
    /**
     * 待付款
     * */
    WAIT_PAY(0,"待付款"),

    /**
     * 待发货
     * */
    WAIT_SEND(1,"待发货"),

    ALREADY_SEND(2,"已发货"),

    SUCCESS_TRADE(3,"交易成功"),

    FAILD_TRADE(4,"交易取消"),

    SERVICE_FOR_AFTER_TRADE(5,"售后");

    private int code;

    private String name;

    OrderCenterOrderStatus(int code, String name) {
        this.code = code;
        this.name=name;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 根据code值获取对应的枚举
     * @param code
     * @return
     */
    public static OrderCenterOrderStatus getTypeByCode(int code){
        for(OrderCenterOrderStatus type : OrderCenterOrderStatus.values()){
            if(type.getCode() == code){
                return type;
            }
        }

        throw new IllegalArgumentException("OrderCenterOrderStatus:" + code);
    }
}
