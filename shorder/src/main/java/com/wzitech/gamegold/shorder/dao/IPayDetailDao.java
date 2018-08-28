package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.PayDetail;

import java.util.Map;

/**
 * 付款明细DAO
 */
public interface IPayDetailDao extends IMyBatisBaseDAO<PayDetail, Long> {
    public int countBymap(Map<String,Object> queryParam);
}
