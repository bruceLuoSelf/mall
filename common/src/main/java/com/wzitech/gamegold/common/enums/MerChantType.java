package com.wzitech.gamegold.common.enums;

/**
 * Created by chengXY on 2017/8/30.
 * 商户类型枚举
 */
public enum MerChantType {

    None(0,"非商户用户"),

    QiBao(1,"7bao");

    private int code;

    private String name;

    MerChantType(int code, String name){
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
    public static MerChantType getTypeByCode(int code){
        for(MerChantType type : MerChantType.values()){
            if(type.getCode() == code){
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的PayType:" + code);
    }
}
