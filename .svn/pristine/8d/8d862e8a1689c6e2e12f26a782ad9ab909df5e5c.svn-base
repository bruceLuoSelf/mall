package com.wzitech.gamegold.order.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.entity.ServiceEvaluate;
import com.wzitech.gamegold.order.entity.ServiceEvaluateStatistics;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 客服评价记录Manager
 * @author yemq
 */
public interface IServiceEvaluateManager {
    /**
     * 添加客服评价记录
     * @param orderId 评价的订单号
     * @param score1 商品数量评分
     * @param score2 服务态度评分
     * @param score3 发货速度评分
     * @param remark 评价内容
     */
    void add(String orderId, int score1, int score2, int score3, String remark);

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
    void add(String orderId, int score1, int score2, int score3, String remark, boolean isDefault);

    /**
     * 修改客服评价记录
     * @param orderId 评价的订单号
     * @param score1 商品数量评分
     * @param score2 服务态度评分
     * @param score3 发货速度评分
     * @param remark 评价内容
     */
    void update(String orderId, int score1, int score2, int score3, String remark);


    /**
     * 判断是否可以对该订单进行评价
     * @param orderId
     */
    void checkCanEvaluate(String orderId);

    /**
     * 判断是否可以对该订单进行追加评价，超过24小时不能追加评价
     * @param orderId
     */
    void checkCanReEvaluate(String orderId);

    /**
     * 根据订单号查询所有的评价记录
     * @param orderId
     * @return
     */
    List<ServiceEvaluate> queryByOrderId(String orderId);

    /**
     * 分页统计评价数据
     * @param params
     * @param pageSize
     * @param startIndex
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<ServiceEvaluateStatistics> statistics(Map<String, Object> params, int pageSize, int startIndex, String orderBy, boolean isAsc);

    /**
     * 分页查询评价数据
     * @param queryMap
     * @param limit
     * @param start
     * @param sortBy
     * @param isAsc
     * @return
     */
    GenericPage<ServiceEvaluate> queryServiceEvaluate(Map<String, Object> queryMap, int limit, int start, String sortBy, boolean isAsc);
}
