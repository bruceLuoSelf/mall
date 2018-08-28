package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.business.IUserModifiActionLogManager;
import com.wzitech.gamegold.shorder.dao.UserModifiActionLogDao;
import com.wzitech.gamegold.shorder.entity.UserModifiActionLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by jhlcitadmin on 2017/3/3.
 */
@Component
public class UserModifiActionLogManagerImpl implements IUserModifiActionLogManager {
    @Autowired
    UserModifiActionLogDao userModifiActionLogDao;
    public void addLog(UserModifiActionLog userModifiActionLog){
     userModifiActionLogDao.addLog(userModifiActionLog);
    }

    public GenericPage<UserModifiActionLog> queryUser(Map<String, Object> queryMap, int limit, int start,
                                                      String sortBy, boolean isAsc){
        if(StringUtils.isEmpty(sortBy)){
            sortBy = "id";
        }
        return userModifiActionLogDao.selectByMap(queryMap, limit, start, sortBy, isAsc);
    }
}
