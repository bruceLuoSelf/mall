package com.wzitech.gamegold.repository.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.repository.dao.ISellerGameDBDAO;
import com.wzitech.gamegold.repository.entity.SellerGame;
import org.springframework.stereotype.Repository;

/**
 * 卖家入驻选择的游戏DB实现
 *
 * @author yemq
 */
@Repository
public class SellerGameDBDAOImpl extends AbstractMyBatisDAO<SellerGame, Long> implements ISellerGameDBDAO {

    @Override
    public void deleteBySellerId(long sellerId) {
        this.getSqlSession().delete(getMapperNamespace() + ".deleteBySellerId", sellerId);
    }
}
