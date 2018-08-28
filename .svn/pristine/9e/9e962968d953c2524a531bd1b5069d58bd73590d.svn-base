package com.wzitech.gamegold.repository.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.util.Date;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/20  wangmin           ZW_J_JB_00073 金币商城增加区服互通功能
 */

public class TransferFile extends BaseEntity{

    /**
     * 新增gameSize_20170721
     */
    public static Long GAMESIZE = 4L;

    //游戏名称
    private String gameName;
    //关键字符串
    private String jsonString;
    //更新时间
    private Date updateTime;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName == null ? null : gameName.trim();
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString == null ? null : jsonString.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}