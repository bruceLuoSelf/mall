/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ImageManager
 *	包	名：		com.wzitech.gamegold.filemgmt.media
 *	项目名称：	gamegold-filemgmt
 *	作	者：		Shawn
 *	创建时间：	2013-9-26
 *	描	述：		
 *	更新纪录：	1. Shawn 创建于 2013-9-26 下午2:51:45
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.filemgmt.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Shawn
 *
 */
@Component("imageManager")
public class ImageManager implements IImageManager {
	@Autowired
	private ThumbGenerator thumbGenerator;
	
	@Autowired
	private ImageCropper imageCropper;
	
	/* (non-Javadoc)
	 * @see com.wzitech.chaos.framework.server.common.media.IImageManager#generateThumb(java.lang.String)
	 */
	@Override
	public String generateThumb(String imageLocalPath, ThumbGeneratorMode mode, String rootDir, String sizeList) {
		return thumbGenerator.generateThumb(imageLocalPath, mode, rootDir, sizeList);
	}

	/* (non-Javadoc)
	 * @see com.wzitech.chaos.framework.server.common.media.IImageManager#crop(java.lang.String, int, int, int, int)
	 */
	@Override
	public String crop(String imageLocalPath, int cropStartX, int cropStartY,
			int cropWidth, int cropHeight) {
		return imageCropper.crop(imageLocalPath, cropStartX, cropStartY, cropWidth, cropHeight);
	}

}
