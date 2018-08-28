/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ImageCropper
 *	包	名：		com.wzitech.gamegold.filemgmt.media
 *	项目名称：	gamegold-filemgmt
 *	作	者：		Shawn
 *	创建时间：	2013-9-26
 *	描	述：		
 *	更新纪录：	1. Shawn 创建于 2013-9-26 下午2:51:45
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.filemgmt.media;

import java.awt.Rectangle;
import java.io.File;

import magick.ImageInfo;
import magick.MagickImage;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Shawn
 *
 */
@Component("imageCropper")
public class ImageCropper {

	/**
	 * 日志记录器
	 */
	private final static Logger logger = LoggerFactory.getLogger(ImageCropper.class);
	
	/**
	 * 剪切图片
	 * @param imageLocalPath
	 * @param cropStartX
	 * @param cropStartY
	 * @param cropWidth
	 * @param cropHeight
	 */
	public String crop(String imageLocalPath, int cropStartX, int cropStartY,
			int cropWidth, int cropHeight) {
		logger.debug("开始剪裁图片{}", new Object[]{ imageLocalPath });
		try{
			// 获取图片类型
//			String fileSuffix = FilenameUtils.getExtension(imageLocalPath);
			
			// 检查图片是否存在
			File fi = new File(imageLocalPath); 
			if(!fi.exists()){
				logger.error("图片{}不存在，剪切图片失败. ", imageLocalPath);
				return null;
			}
			
//			BufferedImage originalImage = ImageIO.read(fi); // 读入文件
//			ImageFilter cropFilter = new CropImageFilter(cropStartX, cropStartY, cropWidth, cropHeight);
//			Image croppedImage = Toolkit.getDefaultToolkit().createImage(
//					new FilteredImageSource(originalImage.getSource(), cropFilter));
//			BufferedImage tag = new BufferedImage(cropWidth, cropHeight,
//					BufferedImage.TYPE_INT_RGB);
//			Graphics g = tag.getGraphics();
//			g.setColor(Color.white);
//			g.fillRect(0, 0, cropWidth, cropHeight);
//			g.drawImage(croppedImage, 0, 0, null); // 绘制剪切后的图
//			g.dispose();
//			ImageIO.write(tag, fileSuffix, new File(imageLocalPath));
			
			// 使用JMagick进行图片剪裁
			ImageInfo originalImageInfo = new ImageInfo(imageLocalPath);
			MagickImage originalImage = new MagickImage(originalImageInfo);
			
			Rectangle croppedRect = new Rectangle(cropStartX, cropStartY, cropWidth, cropHeight);
					
			MagickImage croppedImage = originalImage.cropImage(croppedRect);
			
			// 计算Cropped图片名称
			String fileSuffix = FilenameUtils.getExtension(imageLocalPath);
			String cropperdFileName = FilenameUtils.getBaseName(imageLocalPath)
					+ "-cropped." + fileSuffix;
			// 设置缩略图带完整路径的文件名
			String cropperdFullFileName = FilenameUtils.getFullPath(imageLocalPath)
					+ cropperdFileName;
			
			croppedImage.setFileName(cropperdFullFileName);
			croppedImage.writeImage(originalImageInfo);
			
			logger.debug("成功剪切图片{}, 剪裁后文件名为{}.", new Object[]{imageLocalPath, cropperdFullFileName});
			
			return cropperdFullFileName;
		} catch (Exception e) {
			logger.error("剪切图片{}时发生错误：{}", 
					new Object[]{imageLocalPath, e});
			
			return imageLocalPath;
		}
		
	}

}
