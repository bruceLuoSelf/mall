package com.wzitech.gamegold.common.enums;

/**
 * @author 340096
 * @date 2017/11/24.
 */
public enum  MachineOrArtficialEnum {
    //machine delivery
    MACHINE(0,"t"),
    //artfical delivery
    ARTFICIAL(1,"f");

    private int code;

    private String name;

    MachineOrArtficialEnum(int code, String name){
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
    public static MachineOrArtficialEnum getTypeByCode(int code){
        for(MachineOrArtficialEnum type : MachineOrArtficialEnum.values()){
            if(type.getCode() == code){
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的PayType:" + code);
    }
}
