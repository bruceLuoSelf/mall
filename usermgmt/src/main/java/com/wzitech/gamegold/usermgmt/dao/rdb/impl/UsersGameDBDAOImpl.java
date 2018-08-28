package com.wzitech.gamegold.usermgmt.dao.rdb.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.usermgmt.dao.rdb.IUsersGameDBDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import com.wzitech.gamegold.usermgmt.entity.UsersGame;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 用户负责的游戏
 */
@Repository("usersGameDBDAOImpl")
public class UsersGameDBDAOImpl extends AbstractMyBatisDAO<UsersGame, Long> implements IUsersGameDBDAO {
    @Override
    public List<UsersGame> findUsersGame(Map<String, Object> queryMap){
        return this.selectByMap(queryMap);
    }

    @Override
    public void deleteUsersGames(Map<String, Object> queryMap) throws SystemException {
        this.getSqlSession().delete(this.mapperNamespace + ".deleteUsersGames", queryMap);
    }
}
