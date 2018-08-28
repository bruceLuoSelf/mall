package com.wzitech.gamegold.shorder.enums;

/**
 * Created by 339928 on 2018/4/12.
 */
public enum PicSourceEnum {
    GTR(1, "GTR全自动上传"),
    CUSTOMER(2,"用户自上传"),
    RCPIC(3,"物服上传");


    private int code;

    private String message;

    PicSourceEnum(int code, String message) {
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
    public static PicSourceEnum  getMessageByCode(int code){
        for(PicSourceEnum type:PicSourceEnum.values()){
            if(type.getCode()==code){
                return type;
            }
        }
        throw new IllegalArgumentException("未能找到匹配的PicSource:" + code);
    }
}
