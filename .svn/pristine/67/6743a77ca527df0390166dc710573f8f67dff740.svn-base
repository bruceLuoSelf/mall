package com.wzitech.gamegold.repository.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.repository.dao.ISellerSettingDBDAO;
import com.wzitech.gamegold.repository.entity.SellerSetting;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺配置信息
 */
@Repository
public class SellerSettingDBDAOImpl extends AbstractMyBatisDAO<SellerSetting, Long> implements ISellerSettingDBDAO {
    @Override
    public SellerSetting findById(Long  uid) {
        if(uid==null){
            return null;
        }
        return selectUniqueByProp("id",uid);
    }

    @Override
    public Boolean IsExsitGameAndRegion(Map<String, Object> paramMap) {
        return countByMap(paramMap)>=1;
    }

    @Override
    public SellerSetting selectShopName(Map<String, Object> paramMap) {
        return this.getSqlSession().selectOne(getMapperNamespace() + ".selectShopName", paramMap);
    }

    @Override
    public List<SellerSetting> selectByLoginAccountList(Map<String, Object> paramMap)
    {
        return this.getSqlSession().selectList(getMapperNamespace() + ".selectByLoginAccountList", paramMap);
    }

    /**
     * 更新店铺排序
     *
     * @param id
     * @param sort
     */
    @Override
    public void updateSort(long id, int sort) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", id);
        paramMap.put("sort", sort);
        this.getSqlSession().update(getMapperNamespace() + ".updateSort", paramMap);
    }
}