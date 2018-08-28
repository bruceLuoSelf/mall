package com.wzitech.gamegold.common.utils;

import com.wzitech.chaos.framework.server.common.security.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 模    块：DESHelper
 * 包    名：com.wzitech.gamegold.facade.utils
 * 项目名称：dada
 * 作    者：Shawn
 * 创建时间：2/26/14 7:08 PM
 * 描    述：
 * 更新纪录：1. Shawn 创建于 2/26/14 7:08 PM
 */
public class DESHelper {
    public static String decrypt(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(getIv2());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        return new String(cipher.doFinal(Base64.decodeBase64Binrary(message)));
    }

    public static String encrypt(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(getIv2());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        return Base64.encodeBase64Binrary(cipher.doFinal(message.getBytes("UTF-8")));
    }

    public static String encryptDesEcb(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "DES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(message.getBytes("UTF-8"));
        return Hex.encodeHexString(encryptedData);
    }

    public static String decryptDesEcb(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "DES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(message.getBytes("UTF-8"));
        return new String(encryptedData, "UTF-8");
    }

    private static byte[] getIv2(){
        byte ivBytes[] = new byte[8];
        ivBytes[0]	= 0x18;
        ivBytes[1]	= 0x37;
        ivBytes[2]	= 0x56;
        ivBytes[3]	= 0x68;
        ivBytes[4]	= (byte) 0x99;
        ivBytes[5]	= (byte) 0xAB;
        ivBytes[6]	= (byte) 0xCD;
        ivBytes[7]	= (byte) 0xEF;

        return ivBytes;
    }

//    private static byte[] getIv() {
////        String iv = "55,103,246,79,36,99,167,3";
//        String iv = "18, 37, 56, 68, 99, AB, CD, EF";
//
//        String[] ivArray = iv.split(",");
//        byte[] iv_64 = new byte[8];
//        for (int i = 0; i < ivArray.length; i++) {
//            iv_64[i] = Byte.parseByte(ivArray[i], 16);
//        }
//        return iv_64;
//    }
//
//    private static byte[] getKey() {
//        String key = "42,16,93,156,78,4,218,32";
//        String[] keyArray = key.split(",");
//        byte[] key_64 = new byte[8];
//        for (int i = 0; i < keyArray.length; i++) {
//            key_64[i] = (byte) Integer.parseInt(keyArray[i], 10);
//        }
//        return key_64;
//    }
}
