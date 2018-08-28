package com.wzitech.gamegold.common.main;

import java.net.URLEncoder;

/**
 * Created by chengXY on 2017/8/29.
 */
public class SpitConstant {
    public static String getRequestStr(String appId){
        String requestStr="?appid="+ URLEncoder.encode(appId)+"&authVers="+URLEncoder.encode(MainStationConstant.RESULT_AUTHVERS)+
                "&format="+URLEncoder.encode(MainStationConstant.RESULT_TYPE)+"&signmethod="+URLEncoder.encode(MainStationConstant.RESULT_MD5);
        return requestStr;
    }

    public static String getBaseSign(String appId){
        String baseSign = appId + "&" + MainStationConstant.RESULT_AUTHVERS + "&" +MainStationConstant.RESULT_TYPE ;
        return baseSign;
    }
}
