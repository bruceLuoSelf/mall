package com.wzitech.gamegold.order.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.MyBatisPostfixConstants;
import com.wzitech.gamegold.order.dao.IDiscountCouponDBDAO;
import com.wzitech.gamegold.order.entity.DiscountCoupon;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 红包
 */
@Repository
public class DiscountCouponDBDAOImpl extends AbstractMyBatisDAO<DiscountCoupon, Long> implements IDiscountCouponDBDAO {
    /**
     * 查找一笔优惠券记录
     * @param queryParam
     * @return
     */
    @Override
    public DiscountCoupon selectUniqueDisCountCoupon(Map<String, Object> queryParam) {
        return this.getSqlSession().selectOne(this.mapperNamespace + MyBatisPostfixConstants.SELECT_UNIQUE_BY_PROP, queryParam);
    }
}
