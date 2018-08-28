/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		RandomNubUtils
 *	包	名：		com.wzitech.gamegold.common.utils
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-01-11
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-01-11 下午6:11:17
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.utils;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;

/**
 * @author SunChengfei
 *
 */
public class RandomNumUtils {
	/**
	 * 生成随机整数数
	 * @param size 位数
	 * @return
	 */
	public static String geneRandomNum(int size){
		String[] array = {"0","1","2","3","4","5","6","7","8","9"};
		Random rand = new Random();
		for (int i = 10; i > 1; i--) {
		    int index = rand.nextInt(i);
		    String tmp = array[index];
		    array[index] = array[i - 1];
		    array[i - 1] = tmp;
		}
		return StringUtils.join(array, "").substring(0,size);
	}
	
	public static void main(String[] args) throws InterruptedException{
		for (int i = 0; i < 30; i++) {
			System.out.println(RandomNumUtils.geneRandomNum(6));
			Thread.sleep(200);
		}
		
	}
}
