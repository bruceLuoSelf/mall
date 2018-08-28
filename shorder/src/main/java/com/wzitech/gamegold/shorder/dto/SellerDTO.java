package com.wzitech.gamegold.shorder.dto;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

import java.util.Date;

/**
 * Created by chengXY on 2017/8/22.
 */
public class SellerDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 审核状态
     */
    private Integer checkState;

    /**
     * 审核备注信息
     */
    private String notes;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Boolean isDeleted;

    /**
     * 有否具有短信权限
     */
    private Boolean messagePower;

    /**
     * 是否屏蔽卖家
     */
    private Boolean isShielded;
    /**
     * 是否人工处理库存
     */
    private Boolean manualStatus;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

    /**
     * 用户账号(5173的用户账号)
     */
    private String loginAccount;

    /**
     * 联系人
     */
    private String name;

    /**
     * 是否寄售
     */
    private Boolean isShieldedType;

    /**
     * 是否小助手
     */
    private Boolean isHelper;

    /**
     * 店铺名称
     */
    private String shopName;
//	/**
//	 * 游戏账号
//	 */
//	private String gameAccount;
//
//	/**
//	 * 游戏密码
//	 */
//	private String password;
//
//	/**
//	 * 游戏名
//	 */
//	private String gameName;

//	/**
//	 * 游戏区
//	 */
//	private String region;
//
//	/**
//	 * 游戏服
//	 */
//	private String server;
//
//	/**
//	 * 所属阵营
//	 */
//	private String gameRace;

    /**
     * 联系电话
     */
    private String phoneNumber;

    /**
     * QQ
     */
    private String qq;

    /**
     * 卖家类型
     */
    private Integer sellerType;
    /**
     * 客服ID
     */
    private Long servicerId;

    /**
     * 客服信息
     */
    private UserInfoEO servicerInfo;

    /**
     * 用户id(5173的用户id)
     */
    private String uid;

    /**
     * 关联的游戏
     */
    private String games;

    /**
     * 是否在线
     */
    private Boolean isOnline;

    /*
    收货开通状态
     */
    private Integer openShState;

    /**
     * 是否开通自动更新价格
     */
    private boolean isPriceRob;

    /**
     * 环信账号
     */
    private String hxAccount;

    /**
     * 环信密码
     */
    private String hxPwd;

    //ZW_C_JB_00021 strat 340096
    /**
     *  是否绑定7bao账号
     * */
    private Boolean isBind;

    /**
     *  卖家7bao账号
     * */
    private String sevenBaoAccount;

    /**
     * 是否同意资金托管协议
     * */
    private Boolean isAgree;

    /**
     * 是否开通新资金
     */
    private Boolean isNewFund;
    //ZW_C_JB_00021 end

    public Boolean getIsNewFund() {
        return isNewFund;
    }

    public void setIsNewFund(Boolean newFund) {
        isNewFund = newFund;
    }

    public Boolean getisAgree() {
        return isAgree;
    }

    public void setisAgree(Boolean agree) {
        isAgree = agree;
    }

    public String getSevenBaoAccount() {
        return sevenBaoAccount;
    }

    public void setSevenBaoAccount(String sevenBaoAccount) {
        this.sevenBaoAccount = sevenBaoAccount;
    }

    public Boolean getisBind() {
        return isBind;
    }

    public void setisBind(Boolean bind) {
        isBind = bind;
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
     * @return the checkState
     */
    public Integer getCheckState() {
        return checkState;
    }

    public String getNotes() {
        return notes;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * @return the lastUpdateTime
     */
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @return the loginAccount
     */
    public String getLoginAccount() {
        return loginAccount;
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return the qq
     */
    public String getQq() {
        return qq;
    }

    /**
     * @return the servicerId
     */
    public Long getServicerId() {
        return servicerId;
    }

    /**
     * @return the servicerInfo
     */
    public UserInfoEO getServicerInfo() {
        return servicerInfo;
    }

    public String getUid() {
        return uid;
    }

//	/**
//	 * @return the gameAccount
//	 */
//	public String getGameAccount() {
//		return gameAccount;
//	}
//
//	/**
//	 * @param gameAccount the gameAccount to set
//	 */
//	public void setGameAccount(String gameAccount) {
//		this.gameAccount = gameAccount;
//	}
//
//	/**
//	 * @return the password
//	 */
//	public String getPassword() {
//		return password;
//	}
//
//	/**
//	 * @param password the password to set
//	 */
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	/**
//	 * @return the gameName
//	 */
//	public String getGameName() {
//		return gameName;
//	}
//
//	/**
//	 * @param gameName the gameName to set
//	 */
//	public void setGameName(String gameName) {
//		this.gameName = gameName;
//	}

//	/**
//	 * @return the region
//	 */
//	public String getRegion() {
//		return region;
//	}
//
//	/**
//	 * @param region the region to set
//	 */
//	public void setRegion(String region) {
//		this.region = region;
//	}
//
//	/**
//	 * @return the server
//	 */
//	public String getServer() {
//		return server;
//	}
//
//	/**
//	 * @param server the server to set
//	 */
//	public void setServer(String server) {
//		this.server = server;
//	}
//
//	/**
//	 * @return the gameRace
//	 */
//	public String getGameRace() {
//		return gameRace;
//	}
//
//	/**
//	 * @param gameRace the gameRace to set
//	 */
//	public void setGameRace(String gameRace) {
//		this.gameRace = gameRace;
//	}

    /**
     * @param checkState the checkState to set
     */
    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getMessagePower() {
        return messagePower;
    }

    public void setMessagePower(Boolean messagePower) {
        this.messagePower = messagePower;
    }

    public Boolean getIsShielded() {
        return isShielded;
    }

    public void setIsShielded(Boolean isShielded) {
        this.isShielded = isShielded;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * @param lastUpdateTime the lastUpdateTime to set
     */
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @param loginAccount the loginAccount to set
     */
    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @param qq the qq to set
     */
    public void setQq(String qq) {
        this.qq = qq;
    }

    public Integer getSellerType() {
        return sellerType;
    }

    public void setSellerType(Integer sellerType) {
        this.sellerType = sellerType;
    }

    /**
     * @param servicerId the servicerId to set
     */
    public void setServicerId(Long servicerId) {
        this.servicerId = servicerId;
    }

    /**
     * @param servicerInfo the servicerInfo to set
     */
    public void setServicerInfo(UserInfoEO servicerInfo) {
        this.servicerInfo = servicerInfo;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getManualStatus() {
        return manualStatus;
    }

    public void setManualStatus(Boolean manualStatus) {
        this.manualStatus = manualStatus;
    }

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }

    public Boolean getIsShieldedType() {
        return isShieldedType;
    }

    public void setIsShieldedType(Boolean isShieldedType) {
        this.isShieldedType = isShieldedType;
    }

    public Boolean getIsHelper() {
        return isHelper;
    }

    public void setIsHelper(Boolean isHelper) {
        this.isHelper = isHelper;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean getIsPriceRob() {
        return isPriceRob;
    }

    public void setIsPriceRob(boolean isPriceRob) {
        this.isPriceRob = isPriceRob;
    }

    public Integer getOpenShState() {
        return openShState;
    }

    public void setOpenShState(Integer openShState) {
        this.openShState = openShState;
    }
}
