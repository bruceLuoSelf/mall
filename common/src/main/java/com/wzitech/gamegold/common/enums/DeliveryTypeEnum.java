package com.wzitech.gamegold.common.enums;

/**
 * Created by 汪俊杰 on 2018/1/5.
 */
public enum DeliveryTypeEnum {
    Robot(1,"机器收货"),

    Manual(2,"手工收货"),

    Stop(3,"暂停收货");

    /**
     * 类型码
     */
    private int code;

    DeliveryTypeEnum(int code, String name){
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
    public static DeliveryTypeEnum getTypeByCode(int code){
        for(DeliveryTypeEnum type : DeliveryTypeEnum.values()){
            if(type.getCode() == code){
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的DeliveryTypeEnum:" + code);
    }

    public static String getNameByCode(Integer code){
        if(code!=null){
            for(DeliveryTypeEnum type : DeliveryTypeEnum.values()){
                if(type.getCode() == code){
                    return type.getName();
                }
            }
        }

        return "未能找到匹配的DeliveryTypeEnum:" + code;
    }
}
