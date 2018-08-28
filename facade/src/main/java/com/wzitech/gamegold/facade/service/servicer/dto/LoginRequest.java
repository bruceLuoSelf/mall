/**
 * 
 */
package com.wzitech.gamegold.facade.service.servicer.dto;

/**
 * @author wzitech
 *
 */
public class LoginRequest {
	/**
	 * 登陆用户名
	 */
	private String loginId;
	
	/**
	 * 登陆密码
	 */
	private String userPwd;
	
	/**
	 * 移动密保
	 */
	private String passpod;
	
	/**
	 * 签名
	 */
	private String sign;

	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}

	/**
	 * @param loginId the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	/**
	 * @return the userPwd
	 */
	public String getUserPwd() {
		return userPwd;
	}

	/**
	 * @param userPwd the userPwd to set
	 */
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	/**
	 * @return the passpod
	 */
	public String getPasspod() {
		return passpod;
	}

	/**
	 * @param passpod the passpod to set
	 */
	public void setPasspod(String passpod) {
		this.passpod = passpod;
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
}
