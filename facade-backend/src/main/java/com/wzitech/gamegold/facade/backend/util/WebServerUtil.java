package com.wzitech.gamegold.facade.backend.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import com.wzitech.gamegold.common.utils.DateUtil;

/**
 * WEB服务工具类
 * 
 * @author ztjie
 * 
 */
public class WebServerUtil {

	/**
	 * 调整时间为当前天的最后一秒
	 * @param date
	 * @return
	 */
	public static Date oneDateLastTime(Date date){
		return DateUtil.oneDateLastTime(date);
	}
	
	/**
	 * 转换文件为byte数组
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] changeFileToByteArray(File file) throws IOException{
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
    	ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
    	byte[] temp = new byte[1024];
    	int size = 0;
    	while ((size = in.read(temp)) != -1) {
    		out.write(temp, 0, size);
    	}
    	in.close();
    	byte[] content = out.toByteArray();
    	return content;
	}
}
