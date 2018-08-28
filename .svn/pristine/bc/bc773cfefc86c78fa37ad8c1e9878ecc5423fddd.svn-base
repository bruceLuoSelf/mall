package com.wzitech.gamegold.common.main;

/**
 * Created by chengXY on 2017/8/29.
 */
public interface IMainStationManager {
    /**
     * 步骤1、获取授权
     * */
    SRRequsestTokenResponse GetRequestToken2();

    /**
     * 步骤2、获取令牌
     * */
    SRRequsestTokenResponse GetAccess();

    /**
     * 请求主站
     * */
    String GetMainRest(String uri, String authVers, String format, String method, String jsonParaInfo
            , String signMethod, String token, String vers);
}
