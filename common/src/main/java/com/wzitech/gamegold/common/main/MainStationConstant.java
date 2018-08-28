package com.wzitech.gamegold.common.main;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by chengXY on 2017/8/29.
 */
public class MainStationConstant {

    /**
     * 测试服务器
     */
    public static String URL = "http://openapi.5173.com";

    /****************************** 2.0之后使用的接口名称 ******************************/
    /**
     * Access接口
     */
    public static String URL_ACCESS = URL + "/access.do";
    /**
     * RequestToken接口
     */
    public static String URL_REQUESTTOKEN = URL + "/request.do";
    /**
     * 一般请求接口
     */
    public static String URL_REST = URL + "/rest.do";

    /**
     * 设置服务器返回数据类型
     */
    public static String RESULT_TYPE = "json";
    /**
     * 设置服务器返回加密类型
     */
    public static String RESULT_MD5 = "md5";
    /**
     * 设置用户版本
     */
    public static String RESULT_VERSION = "1.0";

    /**
     * Fields
     */
    public static String RESULT_FIELDS = "";


    /**
     * 设置版本
     */
    public static String RESULT_AUTHVERS = "1.0";

    /**
     * 请求头
     */
    public static String HEADER_CONTENT_TYPE = "Content-Type";
    public static String HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static String IS_ANDROID = "1";
    public static String CONTANT_CODE = "text/html;UTF-8";
    public static String CONTANT_CONTENT_TYPE = "multipart/form-data";


    /****************************** 主站提供方法名称 ******************************/
    /**
     * 获取用户实名信息
     * */
    public static String METHOD_GET_USER_REALNAME = "kubao.msite.user.getrealnameinfo";

    /**
     * 设置用户为商户
     * */
    public static String SET_USER_TOBE_MERCHANT = "kubao.msite.user.SetMerchantInfo";

    /**
     * 设置用户为商户
     * */
    public static String GET_ID_MAIN = "kubao.id.getversionnumber.get";

    public static <T> T JSONToObj(String jsonStr, Class<T> obj) {
        T t = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            t = objectMapper.readValue(jsonStr,
                    obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    // 将输入流转换成字符串
    public static String inStream2String(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        is.close();
        return new String(baos.toByteArray());
    }
}
