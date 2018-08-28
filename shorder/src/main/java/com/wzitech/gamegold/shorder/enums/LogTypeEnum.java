package com.wzitech.gamegold.shorder.enums;

/**
 * Created by 339928 on 2018/6/13.
 */
public enum LogTypeEnum {


    ARRANGEREPOSITORYSALED(1,"分仓所增"),

    DELIVERYSALED(2,"收货所增"),

    ARRANGEREPOSITORYPAID(3,"分仓所减"),

    SELLERPAID(4,"销售所减");

    private int code;

    private String name;

    LogTypeEnum(int code, String name){
        this.code = code;
        this.name = name;
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
    public static OrderPrefix getTypeByCode(int code){
        for(OrderPrefix type : OrderPrefix.values()){
            if(type.getCode() == code){
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的日志类型:" + code);
    }
}
