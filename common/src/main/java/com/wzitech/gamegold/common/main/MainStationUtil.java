package com.wzitech.gamegold.common.main;

import com.wzitech.gamegold.common.utils.EncryptHelper;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chengXY on 2017/8/29.
 */
public class MainStationUtil {
    private final static String ENCODE = "UTF-8";
    /**
     * URL 解码
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:09:51
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String ComputeSignature(String baseSign, String consumerSecret, String tokenSecret)
    {
        String base64="";
        try {
            String sign= EncryptHelper.md5(baseSign);
            String  consumerSecret1 =new String(EncodeToBytes(consumerSecret),"ASCII");
            String  tokenSecret1 =new String(EncodeToBytes(tokenSecret),"ASCII");
            String key =consumerSecret1+"&"+sign+"&" +tokenSecret1 ;
            BASE64Encoder encode = new BASE64Encoder();
            base64 = encode.encode(key.getBytes("ASCII"));
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(base64);
            base64 = m.replaceAll("");
            //base64=base64.replace("\r\n","");
            System.out.println(base64);
            //900150983cd24fb0d6963f7d28e17f72
        }catch (Exception e) {

            System.out.println(e.getMessage());
        }
        return base64;
    }

    private static byte[] EncodeToBytes(String input) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(input))
            return new byte[0];
        byte[] inbytes = input.getBytes("utf-8");
        int unsafeChars = 0;
        char c;
        for (byte b : inbytes)
        {
            c = (char)b;
            if (NeedsEscaping(c))
                unsafeChars++;
        }
        if (unsafeChars == 0)
            return inbytes;
        byte[] outbytes = new byte[inbytes.length + (unsafeChars * 2)];
        int pos = 0;
        for (int i = 0; i < inbytes.length; i++)
        {
            byte b = inbytes[i];
            if (NeedsEscaping((char)b))
            {
                outbytes[pos++] = (byte)'%';
                outbytes[pos++] = (byte)IntToHex((b >> 4) & 0xf);
                outbytes[pos++] = (byte)IntToHex(b & 0x0f);
            }
            else
                outbytes[pos++] = b;
        }
        return outbytes;
    }

    private static boolean NeedsEscaping(char c)
    {
        return !((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')
                || c == '-' || c == '_' || c == '.' || c == '~');
    }

    private static char IntToHex(int n)
    {
        //if (n < 0 || n >= 16)
            //throw new AccountException("n必须大于0小于16");

        if (n <= 9)
            return (char)(n + (int)'0');
        else
            return (char)(n - 10 + (int)'A');
    }


//    /**
//     * 一般加密 sign get
//     */
//    public static Map<String, Object> SignBase64( String method, String paraInfo, long timeNow, String accessToken,
//                                                 String accessSrcet, String token, String resultVers) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("method", method);
//        map.put("paraInfo", paraInfo);
//        map.put("format", MainStationConstant.RESULT_TYPE);
//        map.put("accesstoken", accessToken);
//        map.put("authVers", MainStationConstant.RESULT_VERSION);
//        map.put("signMethod", MainStationConstant.RESULT_MD5);
//        map.put("timestamp", timeNow);
//        map.put("token", token);
//        map.put("fields", "");
//        map.put("vers", resultVers);
//        map.put("clientip", getLocalIpAddress());
//        map.put("appid", MainStationConstant.APP_ID);
//        setSign(map, accessSrcet);
//        return map;
//
//    }




//    // 封装sign签名
//    public static void setSign(Map<String, Object> map, String accessserect) {
//        StringBuffer buff2 = new StringBuffer();
//        buff2.append(map.get("accesstoken")).append("&").append(map.get("appid")).append("&").append(map.get("authVers")).append("&")
//                .append(map.get("clientip")).append("&").append(map.get("fields")).append("&").append(map.get("format")).append("&")
//                .append(map.get("method")).append("&").append(URLDecoder.decode(map.get("paraInfo").toString())).append("&").append(map.get("signMethod")).append("&")
//                .append(map.get("timestamp")).append("&").append(map.get("token")).append("&").append("GET:" + MainStationConstant.URL_REST)
//                .append("&").append(map.get("vers"));
//
//        String sign = MainStationConstant.KAYVALUE + "&" + CipherUtil.generatePassword(buff2.toString()).toLowerCase() + "&" + accessserect;
//        map.put("sign", Base64.encodeBytes(sign.getBytes()));
//    }


    // 获取本地ip
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
//            Log.e("WifiPreference IpAddress", ex.toString());
        }
        return null;
    }


    public static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle =new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader( yc.getInputStream()));
            String inputLine = null;
            while ( (inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return json.toString();
    }

    public static String keyValueJoin(String url, Map<String, String> model, String split){
        StringBuilder modelStr = new StringBuilder();
        if(!StringUtils.isEmpty(url)){
            modelStr.append(url);
        }
        Iterator<Map.Entry<String, String>> iter = model.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            modelStr.append(entry.getKey()).append("=").append(entry.getValue()).append(split);
        }
        if (modelStr.length() > 0) {
            modelStr.deleteCharAt(modelStr.length() - 1);
        }
        return modelStr.toString();
    }

}
