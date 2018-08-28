package com.wzitech.gamegold.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

/**
 * 保险功能配置
 */
public class InsuranceSettings extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 游戏名称
	 */
	private String gameName;

	/**
	 * 保费比例
	 */
	private BigDecimal rate;
	
	/**
	 * 最小值
	 */
	private BigDecimal floor;
	
	/**
	 * 最大值
	 */
	private BigDecimal ceiling;
	
	/**
	 * 保障期限
	 */
	private Integer expireDay;
	
	/**
	 * 是否开启
	 */
	private Boolean enabled;
	
	/**
	 * 最后更新时间
	 */
	private Date updateTime;
	
	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getFloor() {
		return floor;
	}

	public void setFloor(BigDecimal floor) {
		this.floor = floor;
	}

	public BigDecimal getCeiling() {
		return ceiling;
	}

	public void setCeiling(BigDecimal ceiling) {
		this.ceiling = ceiling;
	}

	public Integer getExpireDay() {
		return expireDay;
	}

	public void setExpireDay(Integer expireDay) {
		this.expireDay = expireDay;
	}


    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
