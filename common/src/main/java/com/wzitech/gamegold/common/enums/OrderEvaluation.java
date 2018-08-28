package com.wzitech.gamegold.common.enums;

/**
 * Created by 340032 on 2017/11/28.
 */
public enum OrderEvaluation {
    NO_Evaluation(1,"无评价"),
    Evaluation(2,"已评价"),
    Append_Evaluation(3,"已追加评价");


    private int code;

    private String name;

    OrderEvaluation(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 根据code值获取对应的枚举
     *
     * @param code
     * @return
     */
    public static OrderEvaluation getOrderType(int code) {
        for (OrderEvaluation type : OrderEvaluation.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的PayType:" + code);
    }


}

