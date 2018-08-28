package com.wzitech.gamegold.gamegold.app.service.order.impl;

import com.google.common.collect.Lists;
import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.BeanMapper;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.enums.GoodsCat;
import com.wzitech.gamegold.common.enums.ResponseCodes;

import com.wzitech.gamegold.gamegold.app.service.order.IGoodsService;
import com.wzitech.gamegold.gamegold.app.service.order.dto.GoodsDTO;
import com.wzitech.gamegold.gamegold.app.service.order.dto.QueryGoodsInfoResponse;
import com.wzitech.gamegold.goods.business.IGoodsInfoManager;
import com.wzitech.gamegold.goods.business.IHotRecommendConfigManager;
import com.wzitech.gamegold.goods.entity.GoodsInfo;
import com.wzitech.gamegold.goods.entity.HotRecommendConfig;
import com.wzitech.gamegold.order.business.IGameConfigManager;
import com.wzitech.gamegold.order.entity.GameConfigEO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.wzitech.gamegold.repository.business.ICategoryRepositoryManager;

import javax.ws.rs.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by lingchengshu on 2017/2/27.
 */
@Service("GoodsService")
@Path("goods")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class GoodsServiceImpl extends AbstractBaseService implements IGoodsService {


    @Autowired
    IHotRecommendConfigManager hotRecommendConfigManager;

    @Autowired
    IGameConfigManager gameConfigManager;

    @Autowired
    IGoodsInfoManager goodsInfoManager;

    @Autowired
    ICategoryRepositoryManager categoryRepositoryManager;

    /**
     * 查询所有栏目商品
     *
     * @Path("token")
     */
    @Path("QueryAllCategoryGoods")
    @GET
    @Override
    public IServiceResponse queryAllCategoryGoods(@QueryParam("gameName") String gameName, @QueryParam("goodsTypeName") String goodsTypeName,
                                                  @QueryParam("gameRegion") String gameRegion,
                                                  @QueryParam("gameServer") String gameServer, @QueryParam("gameRace") String gameRace) {
        // 初始化返回数据
        QueryGoodsInfoResponse response = new QueryGoodsInfoResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            if (StringUtils.isEmpty(gameName) || StringUtils.isEmpty(gameRegion) || StringUtils.isEmpty(gameServer)) {
                response.setGoodsList(new ArrayList<GoodsDTO>(0));
            } else {
                List<GoodsDTO> goodsList = new ArrayList<GoodsDTO>();

                // 获取该游戏安心买热卖商品配置,外部接口只提供游戏币
                if (StringUtils.isBlank(goodsTypeName)) {
                    goodsTypeName = ServicesContants.GOODS_TYPE_GOLD;
                }
                HotRecommendConfig hotRecommendConfig = hotRecommendConfigManager.getConfigFromRedis(gameName, goodsTypeName);/**ZW_C_JB_00008_20170512 add**/
                if (hotRecommendConfig != null) {
                    // 获取游戏配置信息,只查询游戏币的
                    GameConfigEO gameConfigEO = gameConfigManager.selectGameConfig(gameName, goodsTypeName);

                    // 获取栏目2的自定义商品信息
                    GoodsInfo goods = null;
                    String gameId = null, regionId = null, serverId = null, raceId = null;
                    for (int i = 1; i < 4; i++) {
                        // 获取栏目2商品,外部接口只提供游戏币的
                        goods = goodsInfoManager.getMaxSaleGoods(gameName, goodsTypeName, gameRegion, gameServer, gameRace, i);/**ZW_C_JB_00008_20170512 add**/
                        if (goods != null && goods.getIsDeleted() == false) {
                            // 只获取一条
                            goods.setGoodsCat(GoodsCat.Cat2.getCode()); // 强制设置为栏目2
                            goods.setImageUrls(gameConfigEO.getGameImage());
                            gameId = goods.getGameId();
                            regionId = goods.getRegionId();
                            serverId = goods.getServerId();
                            raceId = goods.getRaceId();

                            break;
                        }
                    }


                    // 是否显示栏目1的最低价商品
                    if (hotRecommendConfig.isShowCategory23()) {
                        // 栏目2单价
                        BigDecimal biggestUnitPrice = (goods != null && goods.getIsDeleted() == false) ? goods.getUnitPrice() : null;

                        // 获取栏目1的最低价商品
                        Map<String, Object> queryMap = new HashMap<String, Object>();
                        queryMap.put("backGameName", gameName);
                        queryMap.put("backRegion", gameRegion);
                        queryMap.put("backServer", gameServer);
                        queryMap.put("gameRace", gameRace);
                        //外部接口只提供游戏币
                        queryMap.put("goodsTypeName", goodsTypeName); /**ZW_C_JB_00008_20170512 add**/
                        List<RepositoryInfo> repositoryInfos = categoryRepositoryManager.queryCategoryRepository(queryMap, biggestUnitPrice);
                        if (!CollectionUtils.isEmpty(repositoryInfos)) {
                            int goodsCat = GoodsCat.Cat1.getCode(); // 最低价设置为栏目1
                            String title = "";
                            if (StringUtils.equals(gameName, "天涯明月刀")) {
                                title = "当前最低价<br/>购买零风险<br/>交易无上限";
                            } else {
                                title = "当前最低价";
                            }
                            List<GoodsDTO> goodsInfoList = convertToGoods(repositoryInfos, gameConfigEO, goodsCat, title, gameId, regionId, serverId, raceId);
                            if (!CollectionUtils.isEmpty(goodsInfoList)) {
                                goodsList.add(goodsInfoList.get(0));
                            }
                        }
                    }

                    if (goods != null) {
                        GoodsDTO goodsDTO = BeanMapper.map(goods, GoodsDTO.class);
                        goodsList.add(goodsDTO);
                    }

                    // 查询一条单价最低库存最多的卖家店铺库存
                    Map<String, Object> queryMap = new HashMap<String, Object>();
                    queryMap.put("backGameName", gameName);
                    queryMap.put("backRegion", gameRegion);
                    queryMap.put("backServer", gameServer);
                    queryMap.put("gameRace", gameRace);
                    queryMap.put("onShowShopRepository", true);// 只显示店铺库存
                    //外部接口只提供游戏币
                    queryMap.put("goodsTypeName", goodsTypeName);/**ZW_C_JB_00008_20170512 add**/
                    List<RepositoryInfo> repositoryInfos = categoryRepositoryManager.queryCategoryRepository(queryMap, null);
                    if (!CollectionUtils.isEmpty(repositoryInfos)) {
                        int goodsCat = GoodsCat.Cat3.getCode(); // 店铺设置为栏目3

                        String title = "金牌店铺";
                        if (StringUtils.equals(gameName, "天涯明月刀")) {
                            title += "<br/>提供等价物";
                        }

                        List<GoodsDTO> goodsInfoList = convertToGoods(repositoryInfos, gameConfigEO, goodsCat,
                                title, gameId, regionId, serverId, raceId);
                        if (!CollectionUtils.isEmpty(goodsInfoList)) {
                            goodsList.add(goodsInfoList.get(0));
                        }
                    }
                }

                response.setGameName(gameName);
                response.setGameServer(gameServer);
                response.setGameRegion(gameRegion);
                response.setGameRace(gameRace);
                response.setGoodsList(goodsList);
                response.setHotRecommendConfig(hotRecommendConfig);
            }
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("当前查询所有栏目商品发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("当前查询所有栏目商品发生未知异常:{}", ex);
        }
        logger.debug("当前查询所有栏目商品响应信息:{}", response);
        return response;
    }


    private List<GoodsDTO> convertToGoods(List<RepositoryInfo> repositoryList, GameConfigEO gameConfig, int goodsCat,
                                          String title, String gameId, String regionId, String serverId, String raceId) {
        List<GoodsDTO> goods = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(repositoryList)) {
            Random rand = new Random();
            for (int i = 0; i < repositoryList.size(); i++) {
                RepositoryInfo repository = repositoryList.get(i);
                GoodsDTO dto = new GoodsDTO();
                dto.setId(repository.getId());
                dto.setImageUrls(gameConfig.getGameImage());
                dto.setGoodsCat(goodsCat);
                dto.setTitle(title);

                if (gameConfig != null) {
                    dto.setDeliveryTime(gameConfig.getMailTime());
                } else {
                    dto.setDeliveryTime(20);
                }

                dto.setMoneyName(repository.getMoneyName());
                dto.setGameName(repository.getGameName());
                dto.setRegion(repository.getRegion());
                dto.setServer(repository.getServer());
                dto.setGameRace(repository.getGameRace());
                dto.setUnitPrice(repository.getUnitPrice());
                dto.setSellableCount(repository.getSellableCount());
                dto.setSellerLoginAccount(repository.getLoginAccount());
                dto.setIsDeleted(false);
                dto.setGameId(gameId);
                dto.setRegionId(regionId);
                dto.setServerId(serverId);
                dto.setRaceId(raceId);

                goods.add(dto);
            }
        }

        return goods;
    }


}
