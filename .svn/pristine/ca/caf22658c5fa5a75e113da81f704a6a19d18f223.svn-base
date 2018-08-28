package com.wzitech.gamegold.common.enums;

/**
 * Created by chengXY on 2017/11/20.
 */
public enum ClientTypeEnum {
    PC_CLIENT(1,"PC端"),
    M_CLIENT(2,"手机M端"),
    APP_CLIENT(3,"APP端");

    private int code;

    private String name;
    ClientTypeEnum(int code, String name) {
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
    public static ClientTypeEnum getTypeByCode(int code){
        for(ClientTypeEnum type : ClientTypeEnum.values()){
            if(type.getCode() == code){
                return type;
            }
        }

        throw new IllegalArgumentException("ClientTypeEnum:" + code);
    }
}
