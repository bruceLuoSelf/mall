/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		StreamIOHelper
 *	包	名：		com.wzitech.gamegold.common.utils
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-21 下午1:02:15
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author SunChengfei
 * 
 */
public class StreamIOHelper {
	/**
	 * 输入流转化为字符串
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String inputStreamToStr(InputStream is, String charset) throws IOException {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			return new String(buffer.toString().getBytes("ISO-8859-1"), charset);
		}finally {
			in.close();
		}
	}
}
