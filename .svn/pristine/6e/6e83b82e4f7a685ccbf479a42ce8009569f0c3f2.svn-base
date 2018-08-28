package com.wzitech.gamegold.common.enums;

/**sunyang
 * Created by Administrator on 2017/2/17.
 */
public enum UserLogType {

    BuyerLogType(1,"收货商记录"),
    SellerLogType(2,"出货商记录"),
    SystemLogType(3,"系统记录");

    /**
     * 用户类型码
     */
    private int code;

    private String name;

    UserLogType(){}

    UserLogType(int code, String name){
        this.name = name;
        this.code = code;
    }

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
    public static UserType getTypeByCode(int code){
        for(UserType type : UserType.values()){
            if(type.getCode() == code){
                return type;
            }
        }

        throw new IllegalArgumentException("未能找到匹配的UserType:" + code);
    }

    /**
     * 获取枚举代码
     *
     * @return
     */
    public String getEnumCode() {
        return String.valueOf(code);
    }

}
