package com.wzitech.gamegold.common.enums;

/**
 * 收货开通状态
 * Created by 汪俊杰 on 2016/12/13.
 */
public enum ShOpenState {

    WAIT_OPEN(0,"待开通"),

    OPEN(1,"已开通"),

    CLOSE(2,"已关闭");

    private int code;

    private String name;

    ShOpenState(int code, String name){
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
    public static ShOpenState getTypeByCode(int code){
        for(ShOpenState type : ShOpenState.values()){
            if(type.getCode() == code){
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的ShOpenState:" + code);
    }
}
