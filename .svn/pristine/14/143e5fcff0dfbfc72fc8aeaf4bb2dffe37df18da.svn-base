/************************************************************************************
 * Copyright 2013 WZITech Corporation. All rights reserved.
 * <p/>
 * 模	块：		IOrderService
 * 包	名：		com.wzitech.gamegold.facade.service.order
 * 项目名称：	gamegold-facade
 * 作	者：		SunChengfei
 * 创建时间：	2014-1-14
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014-1-14 下午4:52:59
 ************************************************************************************/
package com.wzitech.gamegold.shrobot.service.order;


import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.shorder.dto.RobotFCRequest;
import com.wzitech.gamegold.shrobot.service.order.dto.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单管理服务端接口
 *
 * @author yemq
 */
public interface IOrderService {

    /**
     * 订单列表
     *
     * @param params
     * @param request
     * @return
     */
    IServiceResponse orderList(OrderListRequest params, HttpServletRequest request);

//    /**
//     * 订单交易完成接口
//     *
//     * @param params
//     * @return
//     */
//    IServiceResponse finish(OrderFinishRequest params);

    /**
     * 订单写日志接口
     *
     * @param params
     * @return
     */
    IServiceResponse writeLog(WriteOrderLogRequest params);


    /**
     * 自动化异常转人工
     */
    IServiceResponse RobotExceptional(RobotRequest params);


    /**
     *自动化图片
     */
    IServiceResponse RobotImg(RobotRequest params);


    /**
     * 全自动查询子订单接口
     */
    IServiceResponse SelectSubOrder(RobotRequest params);

    /**
     * 全自动拉子订单接口
     */
    IServiceResponse orderList(RobotRequest params);

    /**
     * 全自动确认发货(撤单)接口
     */
    IServiceResponse finish(RobotRequest params);

    /**
     * 查询子订单是否处于撤单状态
     */
    IServiceResponse withdrawOrder(RobotRequest params);

    /**
     * 自动化6分钟没有收到货,调撤单接口
     */
    IServiceResponse withdraw(RobotRequest params);

    /**
     * 自动化获取查收邮件配置时间
     */
    IServiceResponse obtainConfigTime();

    /**
     * 盘存数量接口
     * Inventory
     * check
     */
    IServiceResponse modifyInventory(RobotFCRequest params);

    /**
     * 分仓结单
     *
     */
    IServiceResponse finishFC(RobotFCRequest params);
//    /**
//     * 分仓日志
//     */
//    IServiceResponse writeFcLog(FcLogRequest params);

    /**
     * 全自动拉分仓子订单接口
     */
    IServiceResponse orderFCList(RobotFCRequest params);
}
