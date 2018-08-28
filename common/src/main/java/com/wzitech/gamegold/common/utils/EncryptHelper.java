/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		EncryptHelper
 *	包	名：		com.wzitech.gamegold.common.utils
 *	项目名称：	    gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	    2014年2月25日
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014年2月25日 下午4:27:27
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.wzitech.chaos.framework.server.common.security.Digests;
import com.wzitech.chaos.framework.server.common.security.HexBinrary;

/**
 * @author SunChegnfei
 *
 */
public class EncryptHelper {
	/**
	 * MD5签名
	 * @throws IOException 
	 */
	public static String md5(String content) throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
		byte[] byteArray;
		try {
			byteArray = Digests.md5(inputStream);
		}finally {
			if (inputStream!=null){
				inputStream.close();
			}
		}
		return HexBinrary.encodeHexBinrary(byteArray).toLowerCase();
	}

	/**
	 * MD5 加密
	 * 
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public static String md5(String content, String enc) throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				content.getBytes(enc));
		byte[] byteArray;
		try {
			byteArray = Digests.md5(inputStream);
		}finally {
			if (inputStream!=null){
				inputStream.close();
			}
		}
		return HexBinrary.encodeHexBinrary(byteArray).toLowerCase();
	}
}
