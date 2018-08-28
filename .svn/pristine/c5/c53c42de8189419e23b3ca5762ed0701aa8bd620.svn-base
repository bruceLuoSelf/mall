package com.wzitech.gamegold.common.enums;


/**
 * 用户类型枚举
 * @author SunChengfei
 *
 */
public enum UserType implements GenericEnum<UserType> {
    /**
     * 系统
     */
    System(-1, "系统"),
	/**
	 * 担保客服
	 */
	AssureService(1, "担保客服"),
	
	/**
	 * 管理员
	 */
	NomalManager(2, "管理员"),
	
	/**
	 * 系统管理员
	 */
	SystemManager(3, "系统管理员"),
	
	/**
	 * 前台5173注册用户
	 */
	FrontUser(4, "5173注册用户"),
	
	/**
	 * 客服的子账号类型-招商
	 */
	RecruitBusiness(5, "招商"),
	
	/**
	 * 客服的子账号类型-做单
	 */
	MakeOrder(6, "做单"),

	/**
	 * 卖家入驻审核客服
	 */
	EnterService(7, "入驻客服"),

    ConsignmentService(8, "寄售客服");
	
	/**
	 * 用户类型码
	 */
	private int code;
	
	private String name;

    UserType(){}
	
	UserType(int code, String name){
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
    @Override
    public String getEnumCode() {
        return String.valueOf(code);
    }
}
