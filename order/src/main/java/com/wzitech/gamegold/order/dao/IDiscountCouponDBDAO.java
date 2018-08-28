package com.wzitech.gamegold.order.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.order.entity.DiscountCoupon;

import java.util.Map;

/**
 * 红包
 */
public interface IDiscountCouponDBDAO extends IMyBatisBaseDAO<DiscountCoupon, Long> {
    /**
     * 查找一笔优惠券记录
     * @param queryParam
     * @return
     */
    DiscountCoupon selectUniqueDisCountCoupon(Map<String, Object> queryParam);
}
