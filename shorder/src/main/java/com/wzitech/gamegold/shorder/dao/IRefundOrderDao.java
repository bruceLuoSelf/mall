package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.RefundOrder;

/**
 * 退款记录
 */
public interface IRefundOrderDao extends IMyBatisBaseDAO<RefundOrder, Long> {
    RefundOrder selectByIdForUpdate(Long id);
}
