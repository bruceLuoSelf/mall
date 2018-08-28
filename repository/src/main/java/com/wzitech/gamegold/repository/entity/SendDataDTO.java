package com.wzitech.gamegold.repository.entity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by chengXY on 2017/8/17.
 */
public class SendDataDTO {
    /**
     * 登录账号
     * */
    private String loginAccount;

    /**
     * 金币用户绑定
     */
    private Boolean isUserBind;

    /**
     * sign
     * */
    private String sign;

    /**
     * 是否开通收货
     * */
    private Boolean isShBind;

    /**
     * 总金额
     **/
    private BigDecimal totalAmount;

    /**
     * 冻结金额
     * */
    private BigDecimal freezeAmount;

    /**
     * 可用金额
     * */
    private BigDecimal availableAmount;

    /**
     * 订单列表
     * */
    private JSONArray dataJson;

    /**
     * 7bao总资金
     */
    private BigDecimal CheckTotalAmount;

    /**
     * 7bao可用金额
     * @return
     */
    private BigDecimal CheckAvailableAmount;

    /**
     * 7bao冻结金额
     * @return
     */
    private BigDecimal CheckFreezeAmount;

    public BigDecimal getCheckTotalAmount() {
        return CheckTotalAmount;
    }

    public void setCheckTotalAmount(BigDecimal checkTotalAmount) {
        CheckTotalAmount = checkTotalAmount;
    }

    public BigDecimal getCheckAvailableAmount() {
        return CheckAvailableAmount;
    }

    public void setCheckAvailableAmount(BigDecimal checkAvailableAmount) {
        CheckAvailableAmount = checkAvailableAmount;
    }

    public BigDecimal getCheckFreezeAmount() {
        return CheckFreezeAmount;
    }

    public void setCheckFreezeAmount(BigDecimal checkFreezeAmount) {
        CheckFreezeAmount = checkFreezeAmount;
    }

    public JSONArray getDataJson() {
        return dataJson;
    }

    public void setDataJson(JSONArray dataJson) {
        this.dataJson = dataJson;
    }

    private int yesOrNo;

    public int getYesOrNo() {
        return yesOrNo;
    }

    public void setYesOrNo(int yesOrNo) {
        this.yesOrNo = yesOrNo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public Boolean getIsShBind() {
        return isShBind;
    }

    public void setIsShBind(Boolean shBind) {
        isShBind = shBind;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public Boolean getUserBind() {
        return isUserBind;
    }

    public void setUserBind(Boolean userBind) {
        isUserBind = userBind;
    }



}
