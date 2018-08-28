package com.wzitech.gamegold.common.enums;

/**
 * 收货模式
 * Created by 汪俊杰 on 2017/1/5.
 */
public enum ShDeliveryTypeEnum {
    Robot(1,"机器收货"),

    Manual(2,"手工收货"),

    Close(3,"关闭收货"),

    Pause(4,"暂停收货");

    /**
     * 类型码
     */
    private int code;

    ShDeliveryTypeEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * 根据code值获取对应的枚举
     * @param code
     * @return
     */
    public static ShDeliveryTypeEnum getTypeByCode(int code){
        for(ShDeliveryTypeEnum type : ShDeliveryTypeEnum.values()){
            if(type.getCode() == code){
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的ShDeliveryTypeEnum:" + code);
    }

    public static String getNameByCode(Integer code){
        if(code!=null){
            for(ShDeliveryTypeEnum type : ShDeliveryTypeEnum.values()){
                if(type.getCode() == code){
                    return type.getName();
                }
            }
        }

        return "未能找到匹配的ShDeliveryTypeEnum:" + code;
    }
}
