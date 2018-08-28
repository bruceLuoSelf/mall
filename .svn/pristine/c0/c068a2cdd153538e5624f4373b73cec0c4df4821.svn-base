package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.UserModifiActionLog;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

import java.util.Map;

/**
 * Created by jhlcitadmin on 2017/3/3.
 */
public interface IUserModifiActionLogManager {

    //添加记录日志
   public void addLog(UserModifiActionLog userModifiActionLog);


    public GenericPage<UserModifiActionLog> queryUser(Map<String, Object> queryMap, int limit, int start,
                                             String sortBy, boolean isAsc);
}
