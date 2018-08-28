/************************************************************************************
 * Copyright 2014 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		PayHelper
 * 包	名：		com.wzitech.gamegold.common.utils
 * 项目名称：	    gamegold-common
 * 作	者：		SunChengfei
 * 创建时间：	    2014年2月26日
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014年2月26日 上午11:51:57
 ************************************************************************************/
package com.wzitech.gamegold.common.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author SunChegnfei
 */
public class PayHelper {
    /**
     * 转换成 long
     *
     * @param ip
     * @return
     * @throws IllegalArgumentException
     */
    public static long ipToLong(String ip) throws IllegalArgumentException {
        // 使用正则判断是否为ip格式字符
        if (!ip.matches("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$")) {
            throw new IllegalArgumentException("非IP格式:" + ip);
        }
        String[] ipArr = ip.split("\\.");
        long ip_Long = 0;
        long p1 = Long.parseLong(ipArr[0]) * 256 * 256 * 256;
        long p2 = Long.parseLong(ipArr[1]) * 256 * 256;
        long p3 = Long.parseLong(ipArr[2]) * 256;
        long p4 = Long.parseLong(ipArr[3]);
        ip_Long = p1 + p2 + p3 + p4;
        return ip_Long;
    }

    /**
     * 生成支付URL
     *
     * @param url
     * @param map
     * @param secretKey
     * @param
     * @return
     * @throws IOException
     */
    public static String formatURL(String url, Map<String, String> map,
                                   String secretKey, String enc) {

        Set<String> origParamSet;
        try {
            origParamSet = PayHelper.toSortSet(map, "=", null);
            String origParamString = PayHelper.joinCollectionToString(origParamSet);
            String sign = null;
            if (null == secretKey || "".equals(secretKey)) {
                sign = EncryptHelper.md5(origParamString, enc);
            } else {
                sign = EncryptHelper.md5(origParamString + secretKey, enc);
            }
            if (map.containsKey("dep_name")) {
                map.put("dep_name", PayHelper.urlEncoding(map.get("dep_name"), "gb2312"));
            }
            if (map.containsKey("goods_title")) {
                map.put("goods_title", PayHelper.urlEncoding(map.get("goods_title"), "gb2312"));
            }
            if (map.containsKey("buyer_name")) {
                map.put("buyer_name", PayHelper.urlEncoding(map.get("buyer_name"), "gb2312"));
            }
            String paramsURL = PayHelper.joinCollectionToString(PayHelper.toSortSet(map,
                    "=", enc));
            return url + "?" + paramsURL + "&sign=" + sign;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成支付URL
     *
     * @param url
     * @param map
     * @param secretKey
     * @param
     * @return
     * @throws IOException
     */
    public static String formatURL2(String url, Map<String, String> map, String secretKey, String enc) {
        try {
            String sign = SignHelper.sign3(map, secretKey, enc);
            String paramsURL = PayHelper.joinCollectionToString(PayHelper.toSortSet(map, "=", enc));
            return url + "?" + paramsURL + "&Sign=" + sign;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * m站支付的生成
     *
     * @param url
     * @param map
     * @param secretKey
     * @param enc
     * @return
     */
    public static String formatMURL(String url, Map<String, String> map,
                                    String secretKey, String enc) {

        Set<String> origParamSet;
        try {
            origParamSet = PayHelper.toSortSet(map, "=", null);
            String origParamString = PayHelper.joinCollectionToString(origParamSet);
            String sign = null;
            if (null == secretKey || "".equals(secretKey)) {
                sign = EncryptHelper.md5(origParamString, enc);
            } else {
                sign = EncryptHelper.md5(origParamString + secretKey, enc);
            }
            if (map.containsKey("dep_name")) {
                map.put("dep_name", PayHelper.urlEncoding(map.get("dep_name"), "gb2312"));
            }
            if (map.containsKey("buyer_name")) {
                map.put("buyer_name", PayHelper.urlEncoding(map.get("buyer_name"), "gb2312"));
            }
            String paramsURL = PayHelper.joinCollectionToString(PayHelper.toSortSet(map,
                    "=", enc));
            return url + "?" + paramsURL + "&sign=" + sign;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取参数签名字符串
     *
     * @param map
     * @param secretKey
     * @param enc
     * @return
     */
    public static String getSignStr(Map<String, String> map, String secretKey, String enc) {
        Set<String> origParamSet;
        try {
            origParamSet = PayHelper.toSortSet(map, "=", null);
            String origParamString = PayHelper.joinCollectionToString(origParamSet);
            String sign = null;
            if (null == secretKey || "".equals(secretKey)) {
                sign = EncryptHelper.md5(origParamString, enc);
            } else {
                sign = EncryptHelper.md5(origParamString + secretKey, enc);
            }
            return sign;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Set<String> toLinkedSet(Map<String, String> map, String joinStr, String enc) {
        if (map.size() > 0) {
            Set<String> paramSet = new LinkedHashSet<String>();
            Iterator<Map.Entry<String, String>> iterator = map.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entity = iterator.next();
                if (null != enc) {
                    paramSet.add(entity.getKey() + joinStr + urlEncoding(entity.getValue(), enc));
                } else {
                    paramSet.add(entity.getKey() + joinStr + entity.getValue());
                }
            }
            return paramSet;
        }
        return null;
    }

    public static Set<String> toSortSet(Map<String, String> map, String joinStr, String enc)
            throws UnsupportedEncodingException {
        if (map.size() > 0) {
            Set<String> paramSet = new TreeSet<String>();
            Iterator<Map.Entry<String, String>> iterator = map.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entity = iterator.next();
                if (null != enc) {
                    paramSet.add(entity.getKey() + joinStr + urlEncoding(entity.getValue(), enc));
                } else {
                    paramSet.add(entity.getKey() + joinStr + entity.getValue());
                }
            }
            return paramSet;
        }
        return null;
    }

    public static String urlEncoding(String content, String enc) {
        try {
            return URLEncoder.encode(content, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static String joinCollectionToString(Collection<String> collection) {
        StringBuffer stringBuffer = new StringBuffer();
        if (collection.size() > 0) {
            Iterator<String> iterator = collection.iterator();
            while (iterator.hasNext()) {
                String item = iterator.next();
                stringBuffer.append(item);
                if (iterator.hasNext()) {
                    stringBuffer.append("&");
                }
            }
        }
        return stringBuffer.toString();
    }
}
