/************************************************************************************
 * Copyright 2013 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		RedisKeyHelper
 * 包	名：		com.wzitech.gamegold.common.utils
 * 项目名称：	gamegold-common
 * 作	者：		HeJian
 * 创建时间：	2014-1-12
 * 描	述：
 * 更新纪录：	1. HeJian 创建于 2014-1-12 下午1:50:28
 ************************************************************************************/
package com.wzitech.gamegold.common.utils;


/**
 * Redis Key帮助类
 *
 * @author HeJian
 *         Update History
 *         Date         Name            Reason For Update
 *         ----------------------------------------------
 *         2017/5/12    wrf              ZW_C_JB_00008商城增加通货
 *         2017/7/07    wrf              ZW_J_JB_00072金币商城对外接口优化
 */
public class RedisKeyHelper {
    /**
     * 获取对应uid的用户信息key
     *
     * @param uid
     * @return
     */
    public static String uid(String uid) {
        return "gamegold:uid:" + uid + ":userInfo";
    }

    /**
     * 获取对应帐号的uid
     *
     * @param loginAccount
     * @return
     */
    public static String account(String loginAccount) {
        return "gamegold:loginAccount:" + loginAccount + ":uid";
    }

    /**
     * 获取uid对应的auth
     *
     * @param uid
     * @return
     */
    public static String auth(String uid) {
        return "gamegold:uid:" + uid + ":auth";
    }

    /**
     * 获取authkey对应的用户id
     *
     * @param authkey
     * @return
     */
    public static String authkey(String authkey) {
        return "gamegold:auth:" + authkey + ":uid";
    }

    /**
     * 获取对应的验证码
     *
     * @param event  事件
     * @param marker 标识
     * @return
     */
    public static String verifyCode(String event, String marker) {
        return "gamegold:marker:" + marker + ":event:" + event + ":verifyCode";
    }

    /**
     * 根据tokenId获取账户信息
     *
     * @return
     */
    public static String tokenAccount(String tokenId) {
        return "gamegold:tokenId:" + tokenId + ":loginAccount";
    }

    /**
     * 游戏对应库存信息
     *
     * @param gameName
     * @param region
     * @param server
     * @param gameRace
     * @return
     */
    public static String gameRepository(String gameName, String region, String server, String gameRace) {
        return "gamegold:gameName:" + gameName + ":region:" + region + ":server:" + server + ":gameRace:" + gameRace + ":repository";
    }

    /**
     * 游戏名、栏目对应的商品列表
     *
     * @param gameName
     * @return
     */
    public static String gameList(String gameName, String goodsTypeName, int goodsCat) {
        return "gamegold:gameName:" + gameName + ":goodsTypeName" + goodsTypeName + ":goodsCat:" + goodsCat + ":goodsList";
    }

    /**
     * 游戏名、区、栏目对应的商品列表
     *
     * @param gameName
     * @param region
     * @param goodsCat
     * @return
     */
    public static String gameList(String gameName, String goodsTypeName, String region, int goodsCat) {
        return "gamegold:gameName:" + gameName + ":goodsTypeName" + goodsTypeName + ":region:" + region + ":goodsCat:" + goodsCat + ":goodsList";
    }

    /**
     * 游戏名、区、服务器、栏目对应的商品列表
     *
     * @param gameName
     * @param region
     * @param server
     * @param goodsCat
     * @return
     */
    public static String gameList(String gameName, String goodsTypeName, String region, String server, int goodsCat) {
        return "gamegold:gameName:" + gameName + ":goodsTypeName" + goodsTypeName + ":region:" + region + ":server:" + server + ":goodsCat:" + goodsCat + ":goodsList";
    }

    /**
     * 游戏名、区、服、阵营对应商品
     *
     * @param gameName
     * @param region
     * @param server
     * @param gameRace
     * @return
     */
    public static String gameList(String gameName, String goodsTypeName, String region, String server, String gameRace) {
        return "gamegold:gameName:" + gameName + ":goodsTypeName" + goodsTypeName + ":region:" + region + ":server:" + server + ":gameRace:" + gameRace + ":goodsList";
    }


    /**
     * 每个用户对应的订单
     *
     * @param uid
     * @return
     */
    public static String orderList(String uid) {
        return "gamegold:uid:" + uid + ":orderList";
    }

    /**
     * 单个客服处理每个游戏类目下订单数
     *
     * @return
     */
    public static String servicerOrder(String gameName, String region, String server, String gameRace) {
        return "gamegold:gameName:" + gameName + ":region:" + region + ":server:" + server + ":gameRace:" + gameRace + ":servicerOrder";
    }

    /**
     * 单个客服处理所有的订单数
     *
     * @return
     */
    public static String serviceAllOrder() {
        return "gamegold:servicerAllOrderNum";
    }

    /**
     * 根据url缓存游戏名，区，服，阵营
     *
     * @param gameName   游戏名
     * @param gameRegion 游戏区
     * @param gameServer 游戏服
     * @param gameRace   游戏阵营
     * @return
     */
    public static String gameUrl(String gameName, String gameRegion, String gameServer, String gameRace) {
        return "gamegold:gameName:" + gameName + ":gameRegion:" + gameRegion + ":gameServer:" + gameServer + ":gameRace:" + gameRace;
    }

    /**
     * 根据currentUrl获取对应的游戏页面信息
     *
     * @param
     * @return
     */
    public static String urlGameInfo(String url) {
        return "gamegold:url:" + url + ":gameInfo";
    }

    /**
     * 根据游戏Id获取游戏信息
     *
     * @param id
     * @return
     */
    public static String gameId(String id) {
        return "gamegold:gameId:" + id;
    }

    /**
     * 保存游戏信息的Key
     *
     * @return
     */
    public static String gameAreaList() {
        return "gamegold:gameAreaList";
    }

    /**
     * 保存游戏阵营的Key
     *
     * @return
     */
    public static String gameRaceList() {
        return "gamegold:gameRaceList";
    }

    /**
     * 保存游戏币信息的key
     *
     * @return
     */
    public static String gameMoneyList() {
        return "gamegold:gameMoneyList";
    }

    /**
     * 保存所有游戏厂商的Key
     *
     * @return
     */
    public static String gameAllCompany() {
        return "gamegold:gameAllCompany";
    }

    /**
     * 根据游戏厂商保存所有游戏
     *
     * @param companyName
     * @return
     */
    public static String companyGame(String companyName) {
        return "gamegold:companyGame:" + companyName;
    }

    /**
     * 保存所有游戏的Key
     *
     * @return
     */
    public static String gameAll() {
        return "gamegold:gameAll";
    }

    /**
     * 保存所有游戏区服id的key
     *
     * @return
     */
    public static String gameAllIds(String gameName, String anyName, Integer type) {
        return "gamegold:gameAllIds:gameName:" + gameName + "name:" + anyName + ":type:" + type;
    }

    /**
     * 重配订单业务锁key
     *
     * @return
     */
    public static String orderConfigLock(String configResultId) {
        return "gamegold:orderConfigResult:" + configResultId;
    }

    /**
     * <p>JOB业务锁key</p>
     *
     * @param jobId
     * @return
     * @author ztjie
     * @date 2014-6-4 上午6:59:26
     * @see
     */
    public static String jobLock(String jobId) {
        return "gamegold:jobLock:" + jobId;
    }

    /**
     * <p>自动化排队job</p>
     *
     * @return
     * @author ztjie
     * @date 2014-6-4 上午6:59:26
     * @see
     */
    public static String queuingredis() {
        return "gamegold:automatic:queuingredis";
    }

    //
    public static String exportStateInfoByToken(String fileToken) {
        return "gamegold:token:" + fileToken + ":exportStateInfo";
    }

    /**
     * 客服投票配置信息key
     *
     * @return
     */
    public static String servicerVoteConfig() {
        return "gamegold:servicerVote:config";
    }

    /**
     * 单个客服处理卖家入驻申请数key
     *
     * @return
     */
    public static String serviceAllEnter() {
        return "gamegold:serviceAllEnterNum";
    }

    /**
     * 安心买热卖商品配置key
     *
     * @param gameName 游戏名称
     * @return
     */
    public static String HotRecommendConfig(String gameName) {
        return "gamegold:hotRecommend:" + gameName;
    }

    /**
     * 安心买热卖商品配置key
     * @param gameName  游戏名称
     * @param goodsTypeName  商品类型
     * @return
     */
    /**
     * ZW_C_JB_00008_20170512 start
     **/
    public static String HotRecommendConfig(String gameName, String goodsTypeName) {
        return "gamegold:hotRecommend:" + gameName + ":goodsTypeName" + goodsTypeName;
    }
    /**ZW_C_JB_00008_20170512 end**/

    /**
     * 客服已处理的订单数量key
     *
     * @param servicerId
     * @return
     */
    public static String serviceOrderProcessedCount(Long servicerId) {
        return "gamegold:service:processedOrderCount:" + servicerId;
    }

    public static String repositoryPrice(String gameName, String region, String server, String gameRace, String cacheKey) {
        return "gamegold:gameName:" + gameName + ":region:" + region + ":server:" + server + ":gameRace:" + gameRace + ":" + cacheKey;
    }

    /**
     * 卖家使用下线功能key
     *
     * @param account
     * @param uid
     * @return
     */
    public static String useOffline(String account, String uid) {
        return "gamegold:seller:offline:account:" + account + ":uid:" + uid;
    }

    /**
     * 根据游戏名称获取担保客服
     *
     * @param gameName
     * @return
     */
    public static String assureServiceByGameName(String gameName) {
        return "gamegold:serviceList:" + gameName;
    }

    /**
     * 根据UUID生成key
     */
    public static String uuidKey(String uuid){
        return "gamegold:uuid:"+uuid;
    } //ZW_J_JB_00073 2017/07/07 wrf adds

    /**
     *  construct key of time limited token  for 1771
     * @param loginAccount 5713 user's loginAccount
     * @return
     */
    public static String tokenKeyForOuterWebSite(String loginAccount){
        return "gamegold:firmAccountToken:"+loginAccount;
    }


    /**
     * 保存更新 最低单价查看表 ReferencePrice
     *
     * @param gameName
     * @return
     */
    public static String serviceReferencePrice(String gameName, String regionName, String serverName, String raceName,String goodsTypeName) {
        return "gamegold:serviceReferencePrice:" + gameName + ":" + goodsTypeName + ":" + regionName + ":" + serverName + ":" + raceName;
    }

    /**
     * 根据游戏名称、区、服、阵营key
     *
     * @param gameName
     * @param regionName
     * @param serverName
     * @param raceName
     * @return
     */
    public static String serviceRepositoryConfineInfo(String gameName, String regionName, String serverName, String raceName,String goodsTypeName) {
        return "gamegold:serviceRepositoryConfineInfo:" + gameName + ":" + goodsTypeName + ":" + regionName + ":" + serverName + ":" + raceName;
    }

    /**
     * Update History
     * Date        Name                Reason for change
     * ----------  ----------------    ----------------------
     * 2017/07/20  wangmin           ZW_J_JB_00073 金币商城增加区服互通功能（根据游戏名 返回json串）
     */
    public static String transferGmeNameJsonString(){
        return "gamegold:repository:JsonTransfer";
    }

    public static String saveOrderList(){return "gamegold:orderCenter:order";}

    public static String ImageUrl(String gameId){
        return "gamegold:orderCenter:ImageUrl:"+gameId;
    }

    public static String getMainAccessTokenKeyUserThough(){
        return "gamegold:MainStationKeyUserThough";
    }

    public static String hxChatRoom(){return "gamegold:orderCenter:hxChatRoom";}

    public static String getLevelCarryUpperLimit(String gameName,Long goodsTypeId) {
        return "gamegold:shorder:levelCarryUpperLimit:gameName:" + gameName + ":goodsTypeId:" + goodsTypeId;
    }

    public static String levelCarryUpperLimitForDelete() {
        return "gamegold:shorder:levelCarryUpperLimit:gameName:*";
    }

    public static String getPurchaserGameTrade(Long id) {
        return "gamegold:shorder:purchaserGameTrade:purchaserId:" + id;
    }

    public static String purchaserGameTradeForDelete() {
        return "gamegold:shorder:purchaserGameTrade:purchaserId:*";
    }

    /**
     * 自动化日志是否存在

     */
    public static String ExistLog(long id) {
        return "gamegold:shorder:robot:existLog:subOrderId:" + id ;
    }

}
