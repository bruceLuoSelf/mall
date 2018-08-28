package com.wzitech.gamegold.order.business.impl;

import com.google.common.collect.Sets;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.order.business.IOrderConfigManager;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.business.IOrderPushMainManager;
import com.wzitech.gamegold.order.business.IServiceEvaluateManager;
import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
import com.wzitech.gamegold.order.dao.IServiceEvaluateDao;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.order.entity.ServiceEvaluate;
import com.wzitech.gamegold.order.entity.ServiceEvaluateStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.xml.ws.Service;
import java.util.*;

/**
 * 客服评价记录Manager
 * @author yemq
 */
@Component
public class ServiceEvaluateManagerImpl implements IServiceEvaluateManager{
    private static final Logger logger = LoggerFactory.getLogger(ServiceEvaluateManagerImpl.class);

    @Autowired
    IServiceEvaluateDao serviceEvaluateDao;
    @Autowired
    IOrderInfoManager orderInfoManager;
    @Autowired
    IOrderConfigManager orderConfigManager;

    @Autowired
    IOrderInfoDBDAO orderInfoDBDAO;

    @Autowired
    IOrderPushMainManager orderPushMainManager;
    /**
     * 添加客服评价记录
     *
     * @param orderId 评价的订单号
     * @param score1  商品数量评分
     * @param score2  服务态度评分
     * @param score3  发货速度评分
     * @param remark  评价内容
     */
    @Override
    @Transactional
    public void add(String orderId, int score1, int score2, int score3, String remark) {
        add(orderId, score1, score2, score3, remark, false);
    }

    /**
     * 添加客服评价记录
     *
     * @param orderId 评价的订单号
     * @param score1  商品数量评分
     * @param score2  服务态度评分
     * @param score3  发货速度评分
     * @param remark  评价内容
     * @param isDefault 是否默认评价
     */
    @Override
    @Transactional
    public void add(String orderId, int score1, int score2, int score3, String remark, boolean isDefault) {
        checkCanEvaluate(orderId);
        checkScore(score1);
        checkScore(score2);
        checkScore(score3);

        // 添加评价记录
        addEvaluate(orderId, score1, score2, score3, remark, isDefault);

        // 更新订单为已评价
        orderInfoManager.updateOrderAlreadyEvaluate(orderId);
        //评价 推送MQ
        OrderInfoEO orderInfo = orderInfoDBDAO.queryOrderId (orderId);
        orderPushMainManager.orderPushMain(orderInfo);

    }

    /**
     * 修改客服评价记录
     *
     * @param orderId 评价的订单号
     * @param score1  商品数量评分
     * @param score2  服务态度评分
     * @param score3  发货速度评分
     * @param remark  评价内容
     */
    @Override
    @Transactional
    public void update(String orderId, int score1, int score2, int score3, String remark) {
        checkCanReEvaluate(orderId);
        checkScore(score1);
        checkScore(score2);
        checkScore(score3);

        // 清空之前对该订单的评价记录
        serviceEvaluateDao.removeByOrderId(orderId);

        // 添加评价记录
        addEvaluate(orderId, score1, score2, score3, remark, false);

        // 更新订单为已追加评价
        orderInfoManager.updateOrderAlreadyReEvaluate(orderId);
        //追加评价再次 推送MQ
        OrderInfoEO orderInfo = orderInfoDBDAO.queryOrderId (orderId);
        orderPushMainManager.orderPushMain(orderInfo);

    }

    /**
     * 判断是否可以对该订单进行评价
     *
     * @param orderId
     */
    @Override
    public void checkCanEvaluate(String orderId) {
        OrderInfoEO order = orderInfoManager.selectById(orderId);
        if (order.getOrderState() != OrderState.Delivery.getCode()
                && order.getOrderState() != OrderState.Statement.getCode()
                && order.getOrderState() != OrderState.Refund.getCode()) {
            throw new SystemException(ResponseCodes.IllegalOrderStateCantEvaluate.getCode(),
                    ResponseCodes.IllegalOrderStateCantEvaluate.getMessage());
        }

        // 只能对自己的订单进行评价
        String evaluatorAccount = CurrentUserContext.getUserLoginAccount();
        int userType = CurrentUserContext.getUserType();
        if (!order.getUserAccount().equals(evaluatorAccount)) {
            if (UserType.System.getCode() == userType && "auto_evaluate_job".equals(evaluatorAccount)) {
                // 如果是系统默认评价job的用户名，则允许评价
            } else {
                throw new SystemException(ResponseCodes.NotYourOrder.getCode(), ResponseCodes.NotYourOrder.getMessage());
            }
        }


        // 没有评价的才可以评价,不允许重复评价
        if (order.getIsEvaluate() != null && order.getIsEvaluate()) {
            throw new SystemException(ResponseCodes.AlreadyEvaluate.getCode(), ResponseCodes.AlreadyEvaluate.getMessage());
        }

        if (UserType.System.getCode() == userType && "auto_evaluate_job".equals(evaluatorAccount)) {
            // 如果是系统默认评价job的用户名，则允许评价
        } else {
            // 已经发货超过4小时，不能进行评价
            Date sendTime = order.getSendTime();
            if (sendTime == null) return;
            Calendar c = Calendar.getInstance();
            c.setTime(sendTime);
            c.add(Calendar.HOUR_OF_DAY, 4);
            long after4Hour= c.getTimeInMillis();
            Date now = new Date();
            if (now.getTime() > after4Hour) {
                throw new SystemException(ResponseCodes.MoreThan4HourCantEvaluate.getCode(),
                        ResponseCodes.MoreThan4HourCantEvaluate.getMessage());
            }
        }
    }

    /**
     * 判断是否可以对该订单进行追加评价，超过24小时不能追加评价
     *
     * @param orderId
     * @return
     */
    @Override
    public void checkCanReEvaluate(String orderId) {
        OrderInfoEO order = orderInfoManager.selectById(orderId);

        if (order.getOrderState() != OrderState.Delivery.getCode()
                && order.getOrderState() != OrderState.Statement.getCode()
                && order.getOrderState() != OrderState.Refund.getCode()) {
            throw new SystemException(ResponseCodes.IllegalOrderStateCantEvaluate.getCode(),
                    ResponseCodes.IllegalOrderStateCantEvaluate.getMessage());
        }

        // 只能对自己的订单进行评价
        String evaluatorAccount = CurrentUserContext.getUserLoginAccount();
        if (!order.getUserAccount().equals(evaluatorAccount)) {
            throw new SystemException(ResponseCodes.NotYourOrder.getCode(), ResponseCodes.NotYourOrder.getMessage());
        }

        // 没有评价的，不可以追加评价
        /*if (order.getIsEvaluate() == null || !order.getIsEvaluate()) {
            throw new SystemException(ResponseCodes.CantReEvaluate.getCode(), ResponseCodes.CantReEvaluate.getMessage());
        }*/

        // 已经追加评价的，不允许重复评价
        if (order.getIsReEvaluate() != null && order.getIsReEvaluate()) {
            throw new SystemException(ResponseCodes.AlreadyReEvaluate.getCode(), ResponseCodes.AlreadyReEvaluate.getMessage());
        }

        // 已经发货超过24小时，不能追加评价
        Date sendTime = order.getSendTime();
        if (sendTime == null) return;
        Calendar c = Calendar.getInstance();
        c.setTime(sendTime);
        c.add(Calendar.HOUR_OF_DAY, 24);
        long after24Hour= c.getTimeInMillis();
        Date now = new Date();
        if (now.getTime() > after24Hour) {
            throw new SystemException(ResponseCodes.MoreThan24HourCantReEvaluate.getCode(),
                    ResponseCodes.MoreThan24HourCantReEvaluate.getMessage());
        }
    }

    /**
     * 根据订单号查询所有的评价记录
     *
     * @param orderId
     * @return
     */
    @Override
    public List<ServiceEvaluate> queryByOrderId(String orderId) {
        return serviceEvaluateDao.queryByOrderId(orderId);
    }

    /**
     * 分页统计评价数据
     *
     * @param params
     * @param pageSize
     * @param startIndex
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<ServiceEvaluateStatistics> statistics(Map<String, Object> params, int pageSize, int startIndex, String orderBy, boolean isAsc) {
        return serviceEvaluateDao.statistics(params, pageSize, startIndex, orderBy, isAsc);
    }

    /**
     * 添加评价记录
     * @param orderId
     * @param score1
     * @param score2
     * @param score3
     * @param remark
     * @param isDefault 是否默认评价
     */
    private void addEvaluate(String orderId, int score1, int score2, int score3, String remark, boolean isDefault) {
        Date now = new Date();
        OrderInfoEO order = orderInfoManager.selectById(orderId);
        String evaluatorAccount = CurrentUserContext.getUserLoginAccount();

        //start by汪俊杰 20170517
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId);
        paramMap.put("serviceId", order.getServicerId());
        int countDb = serviceEvaluateDao.countByMap(paramMap);
        if (countDb > 0) {
            throw new SystemException(ResponseCodes.AlreadyEvaluate.getCode(), ResponseCodes.AlreadyEvaluate.getMessage());
        }
        //end

        // 对担保客服的评价
        ServiceEvaluate evaluate = new ServiceEvaluate();
        evaluate.setOrderId(orderId);
        evaluate.setServiceId(order.getServicerId());
        evaluate.setScore(score2);
        evaluate.setRemark(remark);
        evaluate.setOrderState(order.getOrderState());
        evaluate.setIsDefault(isDefault);
        evaluate.setEvaluatorAccount(evaluatorAccount);
        evaluate.setCreateTime(now);
        serviceEvaluateDao.insert(evaluate);

        // 对寄售物服的评价
        List<ConfigResultInfoEO> configResults = orderConfigManager.orderConfigList(orderId, false);
        if (!CollectionUtils.isEmpty(configResults)) {
            // 物服取评价最小的分数为准
            int _score = Math.min(score1, score3);
            Set<Long> serviceIds = Sets.newHashSet();
            for (ConfigResultInfoEO result : configResults) {
                // 不是寄售的忽略
                if (result.getIsConsignment() == null || !result.getIsConsignment()) {
                    continue;
                }
                // 寄售机器人的忽略
                if (result.getIsJsRobot() != null && result.getIsJsRobot()) {
                    continue;
                }
                // 已经对该物服进行评价了，就忽略，避免一笔订单多次评价
                if (serviceIds.contains(result.getOptionId())) {
                    continue;
                }
                serviceIds.add(result.getOptionId());

                //start by汪俊杰 20170517
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("orderId", orderId);
                map.put("serviceId", result.getOptionId());
                int countWf = serviceEvaluateDao.countByMap(map);
                if (countWf > 0) {
                    throw new SystemException(ResponseCodes.AlreadyEvaluate.getCode(), ResponseCodes.AlreadyEvaluate.getMessage());
                }
                //end

                ServiceEvaluate e = new ServiceEvaluate();
                e.setOrderId(orderId);
                e.setServiceId(result.getOptionId());
                e.setScore(_score);
                e.setRemark(remark);
                e.setOrderState(order.getOrderState());
                e.setIsDefault(isDefault);
                e.setEvaluatorAccount(evaluatorAccount);
                e.setCreateTime(now);
                serviceEvaluateDao.insert(e);
            }
        }
    }

    private void checkScore(int score) {
        if (score > 5 || score < 1)
            throw new SystemException(ResponseCodes.IllegalEvaluateScore.getCode(), ResponseCodes.IllegalEvaluateScore.getMessage());
    }

    @Override
    public GenericPage<ServiceEvaluate> queryServiceEvaluate(Map<String, Object> queryMap, int limit, int start, String sortBy, boolean isAsc) throws SystemException {
        return serviceEvaluateDao.selectByMap(queryMap, limit, start, sortBy, isAsc);
    }
}
