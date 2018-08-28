package com.wzitech.gamegold.order.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.entity.DiscountCoupon;
import com.wzitech.gamegold.order.entity.ServiceEvaluateStatistics;

import java.util.List;
import java.util.Map;

/**
 * 红包
 */
public interface IDiscountCouponManager {
    /**
     * 分页查找红包
     * @param queryMap
     * @param limit
     * @param start
     * @param sortBy
     * @param isAsc
     * @return
     */
     GenericPage<DiscountCoupon> queryDiscountCoupon( Map<String, Object> queryMap, int limit, int start, String sortBy,boolean isAsc);

    /**
     * 根据条件查找所有的优惠券
     * @param queryMap
     * @param sortBy
     * @param isAsc
     * @return
     */
    List<DiscountCoupon> queryDiscountCouponList( Map<String, Object> queryMap, String sortBy,boolean isAsc);

    /**
     * 查找一笔优惠券记录
     * @param code
     * @param pwd
     * @param couponType
     * @param price
     * @return
     */
    DiscountCoupon selectUniqueDisCountCoupon(String code,String pwd,Integer couponType,Double price);

    /**
     * 批量新增
     * @param list
     * @return
     */
    Boolean batchInsert(List<DiscountCoupon> list);

    /**
     * 设置红包为已使用
     * @param code
     * @param orderId
     * @param couponType
     * @return
     */
     Boolean UpdateCouponUsed(String code,String orderId,int couponType);
}
