/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 * <p>
 *	模	块：		UserInfoEO
 *	包	名：		com.wzitech.gamegold.usermgmt.entity
 *	项目名称：	gamegold-usermgmt
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午12:29:41
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.gamegold.common.entity.IUser;

/**
 * 用户信息
 * @author SunChengfei
 *
 */
public class UserInfoEO extends BaseEntity  implements IUser{
	private static final long serialVersionUID = 1L;

	/**
	 * 主帐号ID
	 */
	private Long mainAccountId;
	
	/**
	 * 用户类型
	 */
	private Integer userType;
	
	/**
	 * 5173注册用户账号
	 */
	private String uid;
	
	/**
	 * 登录帐号
	 */
	private String loginAccount;
	
	/**
	 * 登录密码
	 */
	@JsonIgnore
	private String password;
	
	/**
	 * 客服资金账号
	 * 业务流程：买家资金先打给客服，然后客服打给卖家
	 */
	private String fundsAccount;
	
	/**
	 * 客服资金账号Id
	 */
	private String fundsAccountId;
	
	/**
	 * 姓名
	 */
	private String realName;
	
	/**
	 * 昵称
	 */
	private String nickName;
	
	/**
	 * 姓别
	 */
	private String sex;
	
	/**
	 * QQ
	 */
	private String qq;

	/**
	 * QQ密码
	 */
	private String qqPwd;
	
	/**
	 * 微信
	 */
	private String weiXin;
	
	/**
	 * 手机号码
	 */
	private String phoneNumber;
	
	/**
	 * 签名
	 */
	private String sign;
	
	/**
	 * 头像地址
	 */
	private String avatarUrl;
	
	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 是否删除
	 */
	private Boolean isDeleted;
	
	/**
	 * 客服当前处理的订单数
	 */
	private int orderNum;

	/**
	 * 客服投票数
	 */
	private int vote;

	/**
	 * YY
	 */
	private String yy;

	/**
	 * 服务费
	 */
	private int serviceCharge;

	/*
	客服星级
	 */
	private int star;

	/**
	 * 好评率
	 */
	private Double goodRate;

	private String hxAccount;

	private String hxPwd;

    private String hxAppAccount;

    private String hxAppPwd;

	//云信账号
	private String yxAccount;

	//云信密码
    private String yxPwd;

	/*//通讯工具管理
	private String communicationTools;*/

	public String getYxAccount() {
		return yxAccount;
	}

	public void setYxAccount(String yxAccount) {
		this.yxAccount = yxAccount;
	}

	public String getYxPwd() {
		return yxPwd;
	}

	public void setYxPwd(String yxPwd) {
		this.yxPwd = yxPwd;
	}

	/*public String getCommunicationTools() {
		return communicationTools;
	}

	public void setCommunicationTools(String communicationTools) {
		this.communicationTools = communicationTools;
	}*/

	public String getQqPwd() {
		return qqPwd;
	}

	public void setQqPwd(String qqPwd) {
		this.qqPwd = qqPwd;
	}

	public String getHxAppAccount() {
        return hxAppAccount;
    }

    public void setHxAppAccount(String hxAppAccount) {
        this.hxAppAccount = hxAppAccount;
    }

    public String getHxAppPwd() {
        return hxAppPwd;
    }

    public void setHxAppPwd(String hxAppPwd) {
        this.hxAppPwd = hxAppPwd;
    }


    public String getHxAccount() {
        return hxAccount;
    }

	public void setHxAccount(String hxAccount) {
		this.hxAccount = hxAccount;
	}

	public String getHxPwd() {
		return hxPwd;
	}

	public void setHxPwd(String hxPwd) {
		this.hxPwd = hxPwd;
	}

	/**
	 * @return the userType
	 */
	@Override
	public Integer getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the loginAccount
	 */
	@Override
	public String getLoginAccount() {
		return loginAccount;
	}

	/**
	 * @param loginAccount the loginAccount to set
	 */
	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	/**
	 * @return the fundsAccount
	 */
	public String getFundsAccount() {
		return fundsAccount;
	}

	/**
	 * @param fundsAccount the fundsAccount to set
	 */
	public void setFundsAccount(String fundsAccount) {
		this.fundsAccount = fundsAccount;
	}

	public String getFundsAccountId() {
		return fundsAccountId;
	}

	public void setFundsAccountId(String fundsAccountId) {
		this.fundsAccountId = fundsAccountId;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * @param realName the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @return the qq
	 */
	public String getQq() {
		return qq;
	}

	/**
	 * @param qq the qq to set
	 */
	public void setQq(String qq) {
		this.qq = qq;
	}

	/**
	 * @return the weiXin
	 */
	public String getWeiXin() {
		return weiXin;
	}

	/**
	 * @param weiXin the weiXin to set
	 */
	public void setWeiXin(String weiXin) {
		this.weiXin = weiXin;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * @return the avatarUrl
	 */
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
	 * @param avatarUrl the avatarUrl to set
	 */
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	/**
	 * @return the lastUpdateTime
	 */
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * @param lastUpdateTime the lastUpdateTime to set
	 */
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the isDeleted
	 */
	public Boolean getIsDeleted() {
		if(isDeleted==null){
			return true;
		}else{
			return isDeleted;			
		}
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	@JsonProperty("isDeleted") 
	public void setIsDeleted(Boolean isDeleted) {
		if(isDeleted==null){
			this.isDeleted = true;
		}else{
			this.isDeleted = isDeleted;			
		}
	}

	@Override
	public String getUid() {
		return uid;
	}

	public Long getMainAccountId() {
		return mainAccountId;
	}

	public void setMainAccountId(Long mainAccountId) {
		this.mainAccountId = mainAccountId;
	}

	/**
	 * @return the orderNum
	 */
	public int getOrderNum() {
		return orderNum;
	}

	/**
	 * @param orderNum the orderNum to set
	 */
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}

	public String getYy() {
		return yy;
	}

	public void setYy(String yy) {
		this.yy = yy;
	}

	public int getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(int serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public Double getGoodRate() {
		return goodRate;
	}

	public void setGoodRate(Double goodRate) {
		this.goodRate = goodRate;
	}

	public int getStar() {
		return star;
	}

    public void setStar(int star) {
        this.star = star;
    }


}
