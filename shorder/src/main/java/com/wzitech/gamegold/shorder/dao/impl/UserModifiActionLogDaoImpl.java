package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.UserModifiActionLogDao;
import com.wzitech.gamegold.shorder.entity.MainGameConfig;
import com.wzitech.gamegold.shorder.entity.UserModifiActionLog;
import org.springframework.stereotype.Repository;

/**
 * Created by jhlcitadmin on 2017/3/3.
 */
@Repository
public class UserModifiActionLogDaoImpl extends AbstractMyBatisDAO<UserModifiActionLog,Long> implements UserModifiActionLogDao{
   public void addLog(UserModifiActionLog userModifiActionLog){
       this.getSqlSession().insert(getMapperNamespace() + ".insertLog",userModifiActionLog);
   }
}
