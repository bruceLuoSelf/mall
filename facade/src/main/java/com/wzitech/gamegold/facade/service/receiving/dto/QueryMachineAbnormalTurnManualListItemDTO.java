/************************************************************************************
 * Copyright 2013 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		OrderServiceImpl
 * 包	名：		com.wzitech.gamegold.facade.service.order.impl
 * 项目名称：	gamegold-facade
 * 作	者：		SunChengfei
 * 创建时间：	2014-2-24
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014-2-24 下午5:32:28
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.receiving.dto;

import javax.xml.bind.annotation.XmlElement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author SunChengfei
 * ZW_C_JB_00004 yexiaokang
 */
public class QueryMachineAbnormalTurnManualListItemDTO {

    //收货游戏币商城标识
    public static final String SH_YXB_MALL = "1";

    private String id;
    /**
     * 游戏名
     */
    private String gn;
    /**
     * 游戏区
     */
    private String gsn;
    /**
     * 游戏服
     */
    private String gan;
    /**
     * 固定标识 (默认为 1)
     */
    private String yxbMall;

    /**
     * 接手物服id
     */
    private String takeOverSubjectId;

    /**
     * 接手物服
     */
    private String takeOverSubject;
    /**
     * 机器转人工原因
     */
    private String MAR;
    /**
     * 机器转人工时间
     */
    private Date MAT;

    /**
     * 交易方式
     */
    private String CustomBuyPatterns;

    @XmlElement(name = "CustomBuyPatterns")
    public String getCustomBuyPatterns() {
        return CustomBuyPatterns;
    }

    public void setCustomBuyPatterns(String customBuyPatterns) {
        CustomBuyPatterns = customBuyPatterns;
    }

    /**
     * @return the id
     */
    @XmlElement(name = "Id")
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the gn
     */
    @XmlElement(name = "GN")
    public String getGn() {
        return gn;
    }

    /**
     * @param gn the gn to set
     */
    public void setGn(String gn) {
        this.gn = gn;
    }

    /**
     * @return the gsn
     */
    @XmlElement(name = "GSN")
    public String getGsn() {
        return gsn;
    }

    /**
     * @param gsn the gsn to set
     */
    public void setGsn(String gsn) {
        this.gsn = gsn;
    }

    /**
     * @return the ganString
     */
    @XmlElement(name = "GAN")
    public String getGan() {
        return gan;
    }

    /**
     * @param gan the ganString to set
     */
    public void setGan(String gan) {
        this.gan = gan;
    }

    /**
     * @return the yxbMall
     */
    @XmlElement(name = "shYXBMALL")
    public String getYxbMall() {
        return yxbMall;
    }

    /**
     * @param yxbMall
     */
    public void setYxbMall(String yxbMall) {
        this.yxbMall = yxbMall;
    }

    /**
     * @return
     */
    @XmlElement(name = "MAR")
    public String getMAR() {
        return MAR;
    }

    /**
     * @param MAR
     */
    public void setMAR(String MAR) {
        this.MAR = MAR;
    }

    /**
     * @return
     */
    @XmlElement(name = "MAT")
    public String getMAT() {
        if (MAT != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.format(MAT);
            return simpleDateFormat.format(MAT);
        }
        return null;
    }

    /**
     * @param MAT
     */
    public void setMAT(Date MAT) {
        this.MAT = MAT;
    }

    @XmlElement(name = "OpName")
    public String getTakeOverSubject() {
        return takeOverSubject;
    }

    public void setTakeOverSubject(String takeOverSubject) {
        this.takeOverSubject = takeOverSubject;
    }

    @XmlElement(name = "OpId")
    public String getTakeOverSubjectId() {
        return takeOverSubjectId;
    }

    public void setTakeOverSubjectId(String takeOverSubjectId) {
        this.takeOverSubjectId = takeOverSubjectId;
    }
}
