package com.wzitech.gamegold.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 签名工具类
 */
public class SignHelper {
    private static final Logger logger = LoggerFactory.getLogger(SignHelper.class);

    public static String sign(Map<String, String> map, String key, String enc) {
        Set<String> sortSet = toSortSet(map, "=", null);
        String result = joinCollectionToString(sortSet, "&");
        result = key + result;
        logger.info("签名字符串：{}", result);
        String md5 = null;
        try {
            md5 = EncryptHelper.md5(result, enc);
        } catch (Exception e) {
            logger.error("MD5签名出错了", e);
        }
        return md5;
    }

    public static String signMd5(TreeSet<String> signMap,String encryptKey){
        StringBuffer entryptString=new StringBuffer();
        for(String sign:signMap){
            entryptString.append(sign).append("_");
        }
        entryptString.append(encryptKey);
        return entryptString.toString();
    }


    public static String sign2(Map<String, String> map, String key, String enc) {
        StringBuilder sb = new StringBuilder();
        for (String keyData : map.keySet()) {
            String value = "";
            Object valueObject = map.get(keyData);
            if (valueObject == null) {
                value = "";
            } else if (valueObject instanceof String[]) {
                String[] values = (String[]) valueObject;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObject.toString();
            }
//
//            if (StringUtils.isNotBlank(enc)) {
//                value = urlEncoding(value, enc);
//            }
            sb.append("_" + keyData + "=" + value);
        }
        String result = key + sb.toString();
        logger.info("签名字符串：{}", result);
        String md5 = null;
        try {
            md5 = EncryptHelper.md5(result, enc);
        } catch (Exception e) {
            logger.error("MD5签名出错了", e);
        }
        return md5;
    }


    public static String sign3(Map<String, String> map, String key, String enc) {
        StringBuilder sb = new StringBuilder();
        for (String keyData : map.keySet()) {
            String value = "";
            Object valueObject = map.get(keyData);
            if (valueObject == null) {
                value = "";
            } else if (valueObject instanceof String[]) {
                String[] values = (String[]) valueObject;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObject.toString();
            }
//
//            if (StringUtils.isNotBlank(enc)) {
//                value = urlEncoding(value, enc);
//            }
            sb.append(value);
        }
        String result = sb.toString() + key;
        logger.info("签名字符串：{}", result);
        String md5 = null;
        try {
            md5 = EncryptHelper.md5(result, enc);
            logger.info("签名后：{}", md5);
        } catch (Exception e) {
            logger.error("MD5签名出错了", e);
        }
        return md5;
    }

    public static String formatURL(String url, Map<String, String> map, String secretKey, String enc) {
        String sign = sign(map, secretKey, enc);
        String paramsURL = joinCollectionToString(toLinkedSet(map, "=", enc), "&");
        return url + "?" + paramsURL + "&sign=" + sign;
    }

    public static String joinCollectionToString(Collection<String> collection, String joinStr) {
        if (CollectionUtils.isEmpty(collection)) return null;
        StringBuffer s = new StringBuffer();
        Iterator<String> iterator = collection.iterator();
        while (iterator.hasNext()) {
            s.append(iterator.next());
            if (iterator.hasNext())
                s.append(joinStr);
        }
        return s.toString();
    }

    public static Set<String> toSortSet(Map<String, String> map, String joinStr, String enc) {
        if (map.isEmpty()) return null;

        Set<String> set = new TreeSet<String>();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = "";
            Object valueObject = map.get(key);
            if (valueObject == null) {
                value = "";
            } else if (valueObject instanceof String[]) {
                String[] values = (String[]) valueObject;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObject.toString();
            }

            if (StringUtils.isNotBlank(enc)) {
                value = urlEncoding(value, enc);
            }
            set.add(key + joinStr + value);
        }
        return set;
    }

    public static Set<String> toLinkedSet(Map<String, String> map, String joinStr, String enc) {
        if (map.isEmpty()) return null;
        Set<String> paramSet = new LinkedHashSet<String>();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entity = iterator.next();
            if (StringUtils.isNotBlank(enc)) {
                paramSet.add(entity.getKey() + joinStr + urlEncoding(entity.getValue(), enc));
            } else {
                paramSet.add(entity.getKey() + joinStr + entity.getValue());
            }
        }
        return paramSet;
    }

    public static String urlEncoding(String content, String enc) {
        if (StringUtils.isBlank(content)) return "";
        try {
            return URLEncoder.encode(content, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }
}
