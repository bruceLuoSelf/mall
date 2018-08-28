package com.wzitech.gamegold.facade.frontend.service.game.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.common.game.entity.GameRaceInfo;

import java.util.List;

/**
 *
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/14  wubiao           ZW_C_JB_00008 商城增加通货
 */
public class QueryGameRaceInfoResponse extends AbstractServiceResponse {

    private List<GameRaceInfo> gameRaceInfoLsit;

    private GameRaceInfo gameRaceInfo;

    public List<GameRaceInfo> getGameRaceInfoLsit() {
        return gameRaceInfoLsit;
    }

    public void setGameRaceInfoLsit(List<GameRaceInfo> gameRaceInfoLsit) {
        this.gameRaceInfoLsit = gameRaceInfoLsit;
    }

    public GameRaceInfo getGameRaceInfo() {
        return gameRaceInfo;
    }

    public void setGameRaceInfo(GameRaceInfo gameRaceInfo) {
        this.gameRaceInfo = gameRaceInfo;
    }
}
