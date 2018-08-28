/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IFileManager
 *	包	名：		com.wzitech.gamegold.filemgmt.business
 *	项目名称：	gamegold-filemgmt
 *	作	者：		HeJian
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-14 下午2:06:27
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.filemgmt.business;

/**
 * 文件操作
 * @author HeJian
 *
 */
public interface IFileManager {
	/**
	 * 保存头像
	 * @param image
	 * @param uid
	 * @return 原头像保存路径及缩略图路径
	 */
	public String[] saveAvatar(byte[] image, String uid);
	
	/**
	 * 保存图片-商品相关
	 * @param image
	 * @param uid
	 * @return 原图片保存路径及缩略图路径
	 */
	public String[] saveGoodsImage(byte[] image, String uid);
	
	/**
	 * <p>保存图片-游戏交易地点相关</p> 
	 * @author Think
	 * @date 2014-2-21 下午1:21:36
	 * @param image
	 * @return
	 * @see
	 */
	public String[] saveTradePlaceImage(byte[] image);

	/**
	 * <p>得到用户头像图片大小信息</p> 
	 * @author Think
	 * @date 2014-2-19 上午11:05:03
	 * @return
	 * @see
	 */
	public String getUserAvatarSizeList();

	/**
	 * <p>得到商品的图片大小信息</p> 
	 * @author Think
	 * @date 2014-2-19 上午11:07:39
	 * @return
	 * @see
	 */
	public String getImageGoodsSizeList();
	
	/**
	 * <p>得到游戏交易地点的图片大小信息</p> 
	 * @author Think
	 * @date 2014-2-21 下午1:22:51
	 * @return
	 * @see
	 */
	public String getImageTradePlaceSizeList();
	
	/**
	 * 保存密保卡
	 * @param file
	 * @param uid
	 * @return 文件保存路径
	 */
	public String[] savePasspod(byte[] file, String uid);
}
