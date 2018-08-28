package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IQqOnLineDAO;
import com.wzitech.gamegold.shorder.entity.QqOnLineEO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/1/11.
 */
@Repository
public class QqOnLineDAOImpl extends AbstractMyBatisDAO<QqOnLineEO,Long> implements IQqOnLineDAO {

//    @Override
//    @Cacheable(value="default",key="'id_5'+#id")
//    public QqOnLineEO selectByid(Long id){
//        return this.selectById(id);
//    }

}
