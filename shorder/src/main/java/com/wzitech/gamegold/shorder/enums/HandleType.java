package com.wzitech.gamegold.shorder.enums;

/**
 * Created by 340082 on 2017/8/18.
 */
public enum HandleType {
    addFund(1,"addFund"),
    subFund(2,"subFund");



    private int code;

    private String name;

    HandleType(int code, String name){
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
    public static HandleType getTypeByCode(int code){
        for(HandleType type : HandleType.values()){
            if(type.getCode() == code){
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的PayType:" + code);
    }
}
