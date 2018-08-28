/************************************************************************************
 * Copyright 2014 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		IGoodsInfoRedisDAOImpl
 * 包	名：		com.wzitech.gamegold.goods.dao.impl
 * 项目名称：	gamegold-goods
 * 作	者：		SunChengfei
 * 创建时间：	2014-2-21
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014-2-21 下午6:55:21
 ************************************************************************************/
package com.wzitech.gamegold.goods.dao.impl;

import java.util.ArrayList;
import java.util.Set;

import com.wzitech.gamegold.common.constants.ServicesContants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.goods.dao.IGoodsInfoRedisDAO;
import com.wzitech.gamegold.goods.entity.GoodsInfo;

/**
 * @author SunChengfei
 *         *         Update History
 *         Date         Name            Reason For Update
 *         ----------------------------------------------
 *         2017/5/12    wrf              ZW_C_JB_00008商城增加通货
 */
@Repository
public class GoodsInfoRedisDAOImpl extends AbstractRedisDAO<GoodsInfo> implements IGoodsInfoRedisDAO {
    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    /* (non-Javadoc)
     * @see com.wzitech.gamegold.goods.dao.IGoodsInfoRedisDAO#saveGoodsInfo(com.wzitech.gamegold.goods.entity.GoodsInfo)
     */
    @Override
    public void saveGoodsInfo(GoodsInfo goodsInfo) {
        String goodsJson = jsonMapper.toJson(goodsInfo);
        //新建的结构匹配之前的代码
        if(StringUtils.isBlank(goodsInfo.getGoodsTypeName())){
            goodsInfo.setGoodsTypeName(ServicesContants.GOODS_TYPE_GOLD);
        }
        // 根据游戏名称+商品类型+区+服+阵营保存商品信息
        BoundHashOperations<String, String, String> goodsOpe = template
                .boundHashOps(RedisKeyHelper.gameList(goodsInfo.getGameName(), goodsInfo.getGoodsTypeName(), /**ZW_C_JB_00008_20170512 mod**/
                        goodsInfo.getRegion(), goodsInfo.getServer(), goodsInfo.getGameRace()));
        goodsOpe.put(goodsInfo.getGoodsCat().toString(), goodsJson);

        // 根据游戏名称+商品类型+商品栏目保存
        zSetOps.add(RedisKeyHelper.gameList(goodsInfo.getGameName(), goodsInfo.getGoodsTypeName(), goodsInfo.getGoodsCat()),/**ZW_C_JB_00008_20170512 mod**/
                "region:" + goodsInfo.getRegion() + ":server:" + goodsInfo.getServer() + ":gameRace:" + goodsInfo.getGameRace(), (goodsInfo.getSales() == null) ? 0 : goodsInfo.getSales());

        // 根据游戏名称+商品类型+游戏区+商品栏目保存
        zSetOps.add(RedisKeyHelper.gameList(goodsInfo.getGameName(), goodsInfo.getGoodsTypeName(), goodsInfo.getRegion(), goodsInfo.getGoodsCat()),/**ZW_C_JB_00008_20170512 mod**/
                "server:" + goodsInfo.getServer() + ":gameRace:" + goodsInfo.getGameRace(), (goodsInfo.getSales() == null) ? 0 : goodsInfo.getSales());

        // 根据游戏名称+商品类型+游戏区+游戏服+商品栏目保存
        zSetOps.add(RedisKeyHelper.gameList(goodsInfo.getGameName(), goodsInfo.getGoodsTypeName(), goodsInfo.getRegion(), goodsInfo.getServer(), goodsInfo.getGoodsCat()),/**ZW_C_JB_00008_20170512 mod**/
                "gameRace:" + goodsInfo.getGameRace(), (goodsInfo.getSales() == null) ? 0 : goodsInfo.getSales());
    }

    /* (non-Javadoc)
     * @see com.wzitech.gamegold.goods.dao.IGoodsInfoRedisDAO#deleteGoods(com.wzitech.gamegold.goods.entity.GoodsInfo)
     */
    @Override
    public void deleteGoods(GoodsInfo goodsInfo) {
        if (goodsInfo == null) {
            return;
        }

        // redis中，发现key中有gameRace="";gameRace=null两种值，这里各删除一次
        int executeCount = 1;
        if (StringUtils.isEmpty(goodsInfo.getGameRace())) {
            executeCount = 2;
            goodsInfo.setGameRace("");
        }

        for (int i = 0; i < executeCount; i++) {
            BoundHashOperations<String, String, String> goodsOpe = template
                    .boundHashOps(RedisKeyHelper.gameList(goodsInfo.getGameName(), goodsInfo.getGoodsTypeName(),/**ZW_C_JB_00008_20170512 mod**/
                            goodsInfo.getRegion(), goodsInfo.getServer(), goodsInfo.getGameRace()));
            goodsOpe.delete(goodsInfo.getGoodsCat().toString());

            zSetOps.remove(RedisKeyHelper.gameList(goodsInfo.getGameName(), goodsInfo.getGoodsTypeName(), goodsInfo.getGoodsCat()),/**ZW_C_JB_00008_20170512 mod**/
                    "region:" + goodsInfo.getRegion() + ":server:" + goodsInfo.getServer() + ":gameRace:" + goodsInfo.getGameRace());

            zSetOps.remove(RedisKeyHelper.gameList(goodsInfo.getGameName(), goodsInfo.getGoodsTypeName(), goodsInfo.getRegion(), goodsInfo.getGoodsCat()),/**ZW_C_JB_00008_20170512 mod**/
                    "server:" + goodsInfo.getServer() + ":gameRace:" + goodsInfo.getGameRace());

            zSetOps.remove(RedisKeyHelper.gameList(goodsInfo.getGameName(), goodsInfo.getGoodsTypeName(), goodsInfo.getRegion(), goodsInfo.getServer(), goodsInfo.getGoodsCat()),/**ZW_C_JB_00008_20170512 mod**/
                    "gameRace:" + goodsInfo.getGameRace());

            goodsInfo.setGameRace(null);
        }
    }

    /* (non-Javadoc)
     * @see com.wzitech.gamegold.goods.dao.IGoodsInfoRedisDAO#getMaxSaleGoods(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public GoodsInfo getMaxSaleGoods(String gameName, String goodsTypeName, String region,
                                     String server, String gameRace, int goodsCat) {
        // 由于游戏存在层级关系，gameName(游戏名)-region(游戏区)-server(游戏服务器)-gameRace(游戏阵营)
        // 指定子层级，它的父层级是不为空的
        String key = "";
        String detailPrefixKey = "";
        if (StringUtils.isEmpty(region)) {
            key = RedisKeyHelper.gameList(gameName, goodsTypeName, goodsCat);/**ZW_C_JB_00008_20170512 mod**/
            detailPrefixKey = "gamegold:gameName:" + gameName+":goodsTypeName"+goodsTypeName;
        } else if (StringUtils.isEmpty(server)) {
            key = RedisKeyHelper.gameList(gameName, goodsTypeName, region, goodsCat);/**ZW_C_JB_00008_20170512 mod**/
            detailPrefixKey = "gamegold:gameName:" + gameName +":goodsTypeName"+goodsTypeName+ ":region:" + region;
        } else if (StringUtils.isEmpty(gameRace)) {
            key = RedisKeyHelper.gameList(gameName, goodsTypeName, region, server, goodsCat);/**ZW_C_JB_00008_20170512 mod**/
            detailPrefixKey = "gamegold:gameName:" + gameName+":goodsTypeName"+goodsTypeName + ":region:" + region + ":server:" + server;
        } else if (StringUtils.isNotEmpty(gameRace)) {
            key = RedisKeyHelper.gameList(gameName, goodsTypeName, region, server, gameRace);/**ZW_C_JB_00008_20170512 mod**/
            BoundHashOperations<String, String, String> goodsOpe = template.boundHashOps(key);
            String goodsJson = goodsOpe.get(String.valueOf(goodsCat));
            return jsonMapper.fromJson(goodsJson, GoodsInfo.class);
        }
        // 查找指定游戏条件下，指定栏目销量最高的商品
        Set<String> set = zSetOps.range(key, 0, zSetOps.size(key));
        if (set == null || set.size() == 0) {
            return null;
        }
        String value = new ArrayList<String>(set).get(set.size() - 1);

        BoundHashOperations<String, String, String> goodsOpe = template
                .boundHashOps(detailPrefixKey + ":" + value + ":goodsList");

        String goodsJson = goodsOpe.get(String.valueOf(goodsCat));

        return jsonMapper.fromJson(goodsJson, GoodsInfo.class);
    }
}
