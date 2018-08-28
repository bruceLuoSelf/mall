package com.wzitech.gamegold.shorder.enums;

/**
 * Created by chengXY on 2017/8/28.
 */
public enum OrderPrefix {

    NEWORDERID(1,"SG"),

    OLD_PAY_ID(2,"SHZF"),

    NEW_PAY_ID(3,"SHRE");

    private int code;

    private String name;

    OrderPrefix(int code, String name){
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

        throw new IllegalArgumentException("未能找到匹配的PayType:" + code);
    }
}
