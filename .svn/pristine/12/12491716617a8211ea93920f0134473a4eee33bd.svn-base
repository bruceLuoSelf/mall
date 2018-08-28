package com.wzitech.gamegold.shorder.business;

import com.wzitech.gamegold.shorder.dto.RobotFCRequest;

import java.io.IOException;

/**
 * 自动化 异常  发货  更新数据库逻辑实现
 * Created by 340032 on 2018/1/8.
 */
public interface IAutomationManager {

    /**
     * 自动化异常实现
     * @param id
     */
    void automationExceptional(Long id,String OtherReason);

    /**
     * 发货逻辑实现
     */
    void automationFinish(Long id,Long realCount,Integer tradeLogo) throws IOException;


    void modifyInventory(RobotFCRequest params);
}
