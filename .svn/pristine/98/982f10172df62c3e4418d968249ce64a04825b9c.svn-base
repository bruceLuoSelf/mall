package com.wzitech.gamegold.shorder.enums;

/**
 * Created by 340032 on 2018/5/10.
 */
public enum ShowUserImgEnum {
    SHOW_IMG(1,"展示给用户"),
    NOT_SHOW_IMG(2,"不展示给用户");

    private int code;

    private String message;

    ShowUserImgEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public static ShowUserImgEnum  getMessageByCode(int code){
        for(ShowUserImgEnum type:ShowUserImgEnum.values()){
            if(type.getCode()==code){
                return type;
            }
        }
        throw new IllegalArgumentException("未能找到匹配的type:" + code);
    }

}
