/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IImageManager
 *	包	名：		com.wzitech.gamegold.filemgmt.media
 *	项目名称：	gamegold-filemgmt
 *	作	者：		Shawn
 *	创建时间：	2013-9-26
 *	描	述：		
 *	更新纪录：	1. Shawn 创建于 2013-9-26 下午2:51:45
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.filemgmt.media;

/**
 * @author Shawn
 *
 */
public interface IImageManager {

	/**
	 * 返回生成缩略图的路径
	 * 为指定路径图片生成缩略图
	 * @param imageLocalPath
	 */
	String generateThumb(String imageLocalPath, ThumbGeneratorMode mode, String rootDir, String sizeList);

	/**
	 * 剪切图片
	 * @param imageLocalPath
	 * @param cropStartX
	 * @param cropStartY
	 * @param cropWidth
	 * @param cropHeight
	 */
	String crop(String imageLocalPath, int cropStartX, int cropStartY,
			int cropWidth, int cropHeight);
}
