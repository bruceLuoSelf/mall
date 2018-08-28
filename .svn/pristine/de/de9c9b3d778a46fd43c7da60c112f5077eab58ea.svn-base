package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.ISellerDTOdao;
import com.wzitech.gamegold.shorder.dto.SellerDTO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chengXY on 2017/8/24.
 */
@Repository
public class SellerDTODao extends AbstractMyBatisDAO<SellerDTO, Long> implements ISellerDTOdao {
    //查询该出货单的卖家信息
    public com.wzitech.gamegold.shorder.dto.SellerDTO findByAccountAndUid(String account, String uid){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("account",account);
        paramMap.put("uid",uid);
        return  this.getSqlSession().selectOne(getMapperNamespace() + ".findSellerByAccountAndUid", paramMap);
    }

    public com.wzitech.gamegold.shorder.dto.SellerDTO findById(Long id){
        return this.getSqlSession().selectOne(getMapperNamespace()+".findById",id);
    }

    public  SellerDTO findByAccount(String account){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("account",account);
        return  this.getSqlSession().selectOne(getMapperNamespace() + ".findSellerByAccount", paramMap);
    }
}
