package com.wzitech.gamegold.common.enums;

/**
 * Created by zhulf on 2018/1/3.
 */
public enum DefaultIMEnum {
    QQ_COMMUNICATION_TOOL(0,"QQ"),
    HX_COMMUNICATION_TOOL(1,"环信"),
    YX_COMMUNICATION_TOOL(2,"云信");

    private Integer code;

    private String name;
    DefaultIMEnum(int code, String name) {
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
    public static DefaultIMEnum getTypeByCode(int code){
        for(DefaultIMEnum type : DefaultIMEnum.values()){
            if(type.getCode() == code){
                return type;
            }
        }

        throw new IllegalArgumentException("DefaultIMEnum:" + code);
    }
}
