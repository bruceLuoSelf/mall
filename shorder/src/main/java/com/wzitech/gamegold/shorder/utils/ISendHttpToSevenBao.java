package com.wzitech.gamegold.shorder.utils;

import net.sf.json.JSONObject;

/**
 * Created by chengXY on 2017/8/21.
 */
public interface ISendHttpToSevenBao {
    JSONObject httpPost(String url, JSONObject jsonParam);


    /**
     * post请求
     * 参数：url请求地址
     * jsonParam
     */
    String sendHttpPost(String url, JSONObject jsonParam);
}
