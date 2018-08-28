package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.PurchaserData;

import java.util.List;
import java.util.Map;

/**
 * 采购商数据管理dao
 */
public interface IPurchaserDataDao extends IMyBatisBaseDAO<PurchaserData, Long> {
    PurchaserData selectByIdForUpdate(long id);

    /**
     * 根据采购商账号进行查找
     * @param account
     * @return
     */
    PurchaserData selectByAccountForUpdate(String account);

    /**
     * 找到有trade的字符串的PurchaserData
     * @return
     */
    List<PurchaserData> selectByLikeTrade(Map<String, Object> paramMap);

    /**
     * 防止锁表 查询无锁
     * @param account
     * @return
     */
    PurchaserData selectByAccount(String account);
}
