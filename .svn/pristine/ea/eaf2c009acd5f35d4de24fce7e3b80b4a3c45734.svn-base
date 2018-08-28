package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IBalckListDao;
import com.wzitech.gamegold.shorder.entity.BlackListEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 340032 on 2017/12/25.
 */

@Repository("BlackListDaoImpl")
public class BlackListDaoImpl extends AbstractMyBatisDAO<BlackListEO,Long> implements IBalckListDao {


    @Override
    public BlackListEO findUserByLoginAccount(String loginAccount) {
        if(StringUtils.isEmpty(loginAccount)){
            return null;
        }
        return selectUniqueByProp("loginAccount", loginAccount);
    }

    @Override
    public BlackListEO findUserById(Long  id) {
        if(id==null){
            return null;
        }
        return selectUniqueByProp("id",id);
    }

    @Override
    public BlackListEO blackListAccount(String loginAccount, Boolean enable) {
        Map<String,Object> map=new HashMap<String, Object>();
        if (StringUtils.isEmpty(loginAccount)){
            return null;
        }
        map.put("loginAccount",loginAccount);
        map.put("enable",enable);
        return this.getSqlSession().selectOne(getMapperNamespace()+".selectByEnable",map);
    }


}
