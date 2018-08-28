package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IGoodsTypeDao;
import com.wzitech.gamegold.shorder.entity.GoodsType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 交易类目Dao实现类
 */
@Repository
public class GoodsTypeDaoImpl extends AbstractMyBatisDAO<GoodsType,Long> implements IGoodsTypeDao{
   public List<GoodsType> queryGoodsTypeNameIdList(){
        return this.getSqlSession().selectList(this.getMapperNamespace() + ".queryGoodsTypeNameIdList");
    }

    public Long queryGoodsTypeIdByName(String name){
        return this.getSqlSession().selectOne(this.getMapperNamespace() +".queryGoodsTypeIdByName",name);
    }
}
