package com.wzitech.gamegold.usermgmt.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.gamegold.usermgmt.business.IUsersGameManager;
import com.wzitech.gamegold.usermgmt.dao.rdb.IUsersGameDBDAO;
import com.wzitech.gamegold.usermgmt.dao.redis.IUserInfoRedisDAO;
import com.wzitech.gamegold.usermgmt.entity.UsersGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 用户负责的游戏.
 */
@Component("usersGameManagerImpl")
public class UsersGameManagerImpl extends AbstractBusinessObject implements IUsersGameManager {
    @Autowired
    IUsersGameDBDAO usersGameDBDAO;

    @Autowired
    IUserInfoRedisDAO userInfoRedisDAO;

    @Override
    public List<UsersGame> findUsersGame(Map<String, Object> queryMap){
        return usersGameDBDAO.findUsersGame(queryMap);
    }

    @Override
    @Transactional
    public void saveUser(String games,long userId){
        //当前的配置
        List<String> gameList=new ArrayList<String>();
        if(!games.equals("")){
            gameList= Arrays.asList(games.split(","));
        }

        //获取该用户所有的配置
        Map<String, Object> queryMapExist = new HashMap<String, Object>();
        queryMapExist.put("userId", userId);
        List<UsersGame> gameNamesExistList= findUsersGame(queryMapExist);

        //将获取的用户的配置的游戏名放到集合中，以便于进行包含的判断
        List<String> listExist=new ArrayList<String>();
        for(UsersGame usersGame:gameNamesExistList){
            listExist.add(usersGame.getGameName());
        }

        //删除的参数设定
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("userId",userId);

        //过滤出需要删除掉的配置
        for(String key:listExist){
            if(!gameList.contains(key)) {
                queryMap.put("gameName",key);
                usersGameDBDAO.deleteUsersGames(queryMap);
            }
        }

        //过滤出需要新增的配置
        for(String key:gameList){
            if(!listExist.contains(key)){
                UsersGame u=new UsersGame();
                u.setGameName(key);
                u.setUserId(userId);
                usersGameDBDAO.insert(u);
            }
        }
        //userInfoRedisDAO.removeAssureService();
    }
}
