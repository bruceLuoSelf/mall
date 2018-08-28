/**
 * 
 */
package com.wzitech.gamegold.facade.service.servicer.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author wzitech
 *
 */
@XmlRootElement(name="Result")
public class LoginResponse {
	private String msg;

	private boolean status;
	
	private String yxbMall;
	
	/**
	 * @return the yxbMall
	 */
	@XmlElement(name = "YXBMALL")
	public String getYxbMall() {
		return yxbMall;
	}

	/**
	 * @param yxbMall
	 *            the yxbMall to set
	 */
	public void setYxbMall(String yxbMall) {
		this.yxbMall = yxbMall;
	}

	/**
	 * @return the msg
	 */
	@XmlElement(name = "Msg")
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the status
	 */
	@XmlElement(name = "Status")
	public boolean isStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
