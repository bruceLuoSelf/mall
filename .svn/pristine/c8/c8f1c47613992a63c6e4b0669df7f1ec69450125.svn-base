package com.wzitech.gamegold.goods.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.goods.dao.IRepositoryConfineDao;
import com.wzitech.gamegold.goods.entity.RepositoryConfine;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jhlcitadmin on 2017/3/17.
 */
@Repository
public class RepositoryConfineDaoImpl extends AbstractMyBatisDAO<RepositoryConfine, Long> implements IRepositoryConfineDao {


    public RepositoryConfine selectRepositoryByMap(String gameName,String goodsTypeName){
        Map<String, Object> selectMap = new HashMap<String, Object>();
        selectMap.put("gameName",gameName);
        selectMap.put("goodsTypeName",goodsTypeName);
        return this.getSqlSession().selectOne(this.getMapperNamespace()+".selectRepositoryByMap",selectMap);
    }

    public List<RepositoryConfine> selectRepository(){
        return this.getSqlSession().selectList(this.getMapperNamespace()+".selectRepository");
    }
}
