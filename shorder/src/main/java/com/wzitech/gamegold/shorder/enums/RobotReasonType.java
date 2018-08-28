package com.wzitech.gamegold.shorder.enums;

/**
 * Created by 340032 on 2018/6/22.
 */
public enum RobotReasonType {
    EdgeOutAccount(1,"分仓顶号"),
    FCFinished(2,"分仓正常完单"),
    RobotAberrant(3,"分仓失败");

    private int code;

    private String name;

    RobotReasonType(int code, String name){
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
