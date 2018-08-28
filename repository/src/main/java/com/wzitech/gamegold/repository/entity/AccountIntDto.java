package com.wzitech.gamegold.repository.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by 340032 on 2017/8/26.
 */
@Component
public class AccountIntDto extends BaseEntity {
    /**
     * 登录账号
     * */
    private String loginAccount;


    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * qq
     * */
    private Long qq;


    /**
     * 姓名
     * */
    private  String name;

    /**
     * 金币用户绑定
     */
    private Boolean isUserBind;


    /**
     * 7bao账户Id
     * @return
     */
    private String uid;
    /**
     * sign
     * */
    private String sign;
    /**
     * 7bao总金额
     */
    private BigDecimal totalAmountBao;

    private BigDecimal freezeAmountBao;
    /**
     * 可用金额
     */
    private BigDecimal availableAmountBao;

    /**
     * 申请时间
     * */
    private Long applyTime;


    /**
     * 资金绑定
     * @return
     */
    private Boolean isShBind;


    /**
     * 订单列表
     * */
    private JSONArray dataJson;

    public JSONArray getDataJson() {
        return dataJson;
    }

    public void setDataJson(JSONArray dataJson) {
        this.dataJson = dataJson;
    }

    public Boolean getIsShBind() {
        return isShBind;
    }

    public void setIsShBind(Boolean shBind) {
        isShBind = shBind;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public BigDecimal getTotalAmountBao() {
        return totalAmountBao;
    }

    public void setTotalAmountBao(BigDecimal totalAmountBao) {
        this.totalAmountBao = totalAmountBao;
    }

    public BigDecimal getFreezeAmountBao() {
        return freezeAmountBao;
    }

    public void setFreezeAmountBao(BigDecimal freezeAmountBao) {
        this.freezeAmountBao = freezeAmountBao;
    }

    public BigDecimal getAvailableAmountBao() {
        return availableAmountBao;
    }

    public void setAvailableAmountBao(BigDecimal availableAmountBao) {
        this.availableAmountBao = availableAmountBao;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getQq() {
        return qq;
    }

    public void setQq(Long qq) {
        this.qq = qq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getUserBind() {
        return isUserBind;
    }

    public void setUserBind(Boolean userBind) {
        isUserBind = userBind;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
