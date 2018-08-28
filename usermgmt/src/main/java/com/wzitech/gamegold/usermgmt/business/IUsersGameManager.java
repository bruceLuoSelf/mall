package com.wzitech.gamegold.usermgmt.business;

import com.wzitech.chaos.framework.server.common.exception.BusinessException;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import com.wzitech.gamegold.usermgmt.entity.UsersGame;

import java.util.List;
import java.util.Map;

/**
 * 用户负责的游戏
 */
public interface IUsersGameManager {

    /**
     * 查询用户负责的所有游戏
     * @param queryMap
     * @return
     */
    public List<UsersGame> findUsersGame(Map<String, Object> queryMap);

    /**
     * 保存配置信息
     * @throws BusinessException
     */
    public void saveUser(String games,long userId);
}
