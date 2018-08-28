package com.wzitech.gamegold.rc8.service.game.impl;

import com.google.common.collect.Lists;
import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.order.business.IGameConfigManager;
import com.wzitech.gamegold.order.entity.GameConfigEO;
import com.wzitech.gamegold.rc8.service.game.IGameService;
import com.wzitech.gamegold.rc8.service.game.dto.GameRequest;
import com.wzitech.gamegold.rc8.service.game.dto.GameResponse;
import com.wzitech.gamegold.rc8.utils.DESHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import java.util.List;

/**
 * 获取游戏信息
 * @author yemq
 */
@Service("GameService")
@Path("game")
@Produces({"application/xml;charset=UTF-8", "application/json;charset=UTF-8"})
@Consumes("application/json;charset=UTF-8")
public class GameServiceImpl extends AbstractBaseService implements IGameService {
    @Autowired
    private IGameConfigManager gameConfigManager;

    @Value("${encrypt.5173.key}")
    private String encryptKey="";

    /**
     * 获取商城游戏列表
     *
     * @return
     */
    @Override
    @Path("list")
    @GET
    public GameResponse getGames(@QueryParam("")GameRequest gameRequest) {
        GameResponse response = new GameResponse();
        response.setMsg("失败");
        response.setStatus(false);

        try {
            // 校验MD5
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s", gameRequest.getName(), gameRequest.getVersion(), encryptKey));
            if(!StringUtils.equals(toEncrypt, gameRequest.getSign())){
                response.setMsg(response.getMsg() + " (签名不一致)");
                return response;
            }

            List<GameResponse.Game> games = Lists.newArrayList();
            List<GameConfigEO> gameConfigList = gameConfigManager.queryGameNameIdList();
            for (GameConfigEO config : gameConfigList) {
                if (StringUtils.isNotBlank(config.getGameId())) {
                    GameResponse.Game game = new GameResponse.Game();
                    game.setId(config.getGameId());
                    game.setName(config.getGameName());
                    games.add(game);
                }
            }
            response.setGames(games);
            response.setMsg("成功");
            response.setStatus(true);
        } catch (SystemException e) {
            response.setMsg(e.getArgs()[0]);
            logger.error("查询参数：{}", gameRequest);
            logger.error("获取商城游戏列表出错了", e);
        } catch (Exception e) {
            response.setMsg("系统发生未知异常");
            logger.error("查询参数：{}", gameRequest);
            logger.error("获取商城游戏列表出错了", e);
        }

        return response;
    }
}
