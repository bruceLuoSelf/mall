package com.wzitech.gamegold.facade.backend.action.usermgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractFileUploadAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.usermgmt.business.IUsersGameManager;
import com.wzitech.gamegold.usermgmt.entity.UsersGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.rmi.CORBA.Util;
import java.util.*;

/**
 * 用户负责的游戏
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class UsersGameAction extends AbstractFileUploadAction {

    private UsersGame usersGame;

    private String games;

    private long userId;

    private List<UsersGame> usersGameList;
    @Autowired
    IUsersGameManager usersGameManager;

    /**
     * 查询列表
     * @return
     */
    public String queryUsersGame() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("gameName",usersGame.getGameName());
        queryMap.put("userId",usersGame.getUserId());
        usersGameList = usersGameManager.findUsersGame(queryMap);
        return this.returnSuccess();
    }

    /**
     * 保存
     * @return
     */
    public String saveUser() {
        try {
            usersGameManager.saveUser(games,userId);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public void setUsersGame(UsersGame usersGame) {
        this.usersGame = usersGame;
    }

    public UsersGame getUsersGame() {
        return usersGame;
    }

    public List<UsersGame> getUsersGameList() {
        return usersGameList;
    }

    public void setUsersGameList(List<UsersGame> usersGameList) {
        this.usersGameList = usersGameList;
    }

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
