package com.wzitech.gamegold.usermgmt.dao.rdb;


import com.wzitech.chaos.framework.server.common.exception.BusinessException;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.usermgmt.entity.UsersGame;

import java.util.List;
import java.util.Map;

public interface IUsersGameDBDAO  extends IMyBatisBaseDAO<UsersGame, Long> {
    /**
     * 查询用户负责的所有游戏
     * @param queryMap
     * @return
     */
    public List<UsersGame> findUsersGame(Map<String, Object> queryMap);

    /**
     * 批量删除
     * @throws BusinessException
     */
    public void deleteUsersGames(Map<String, Object> queryMap);
}
