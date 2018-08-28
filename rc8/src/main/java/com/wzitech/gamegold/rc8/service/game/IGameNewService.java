package com.wzitech.gamegold.rc8.service.game;

import com.wzitech.gamegold.rc8.service.game.dto.GameRequest;
import com.wzitech.gamegold.rc8.service.game.dto.GameResponse;

/**
 * Created by 340032 on 2018/3/6.
 */
public interface IGameNewService {
    /**
     * 获取商城游戏列表
     * @return
     */
    GameResponse getGames(GameRequest gameRequest);
}
