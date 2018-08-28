package com.wzitech.gamegold.common.main;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.dto.GoodsInfo;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.game.IGameInfoManager;
import com.wzitech.gamegold.common.game.entity.GameInfo;
import com.wzitech.gamegold.common.game.entity.GameNameAndId;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by chengXY on 2017/10/24.
 */
@Component
public class GetGameIdFromMain implements IGetGameIdFromMain{
    @Autowired
    IGameInfoManager gameInfoManager;

    public GoodsInfo getGameAndServer(GoodsInfo goodsInfo){
        try {
            GameNameAndId gameNameAndIdTypeOfOne = gameInfoManager.getIdByProp(goodsInfo.getGameName().trim(), goodsInfo.getGameName().trim(), 1);
            GameNameAndId gameNameAndIdTypeOfTwo = gameInfoManager.getIdByProp(goodsInfo.getGameName().trim(), goodsInfo.getRegion().trim(), 2);
            GameNameAndId gameNameAndIdTypeOfThree = gameInfoManager.getIdByProp(goodsInfo.getGameName().trim(), goodsInfo.getServer().trim(), 3);
            if (gameNameAndIdTypeOfOne != null && gameNameAndIdTypeOfTwo != null && gameNameAndIdTypeOfThree != null) {
                goodsInfo.setGameId(gameNameAndIdTypeOfOne.getAnyId());
                goodsInfo.setServerId(gameNameAndIdTypeOfThree.getAnyId());
                goodsInfo.setRegionId(gameNameAndIdTypeOfTwo.getAnyId());

            } else {
                throw new SystemException(ResponseCodes.EmptyGameId.getCode(),
                        ResponseCodes.EmptyGameId.getCode());
            }
            if (StringUtils.isEmpty(goodsInfo.getGameRace())) {
                goodsInfo.setRaceId("");
            } else {
                GameNameAndId gameNameAndIdTypeOfFour = gameInfoManager.getIdByProp(goodsInfo.getGameName().trim(), goodsInfo.getGameRace().trim(), 4);
                if (gameNameAndIdTypeOfFour != null) {
                    goodsInfo.setRaceId(gameNameAndIdTypeOfFour.getAnyId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goodsInfo;
    }
}
