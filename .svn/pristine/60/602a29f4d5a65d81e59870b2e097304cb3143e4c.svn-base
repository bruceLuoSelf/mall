/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		FileManagerImpl
 *	包	名：		com.wzitech.gamegold.filemgmt.business.impl
 *	项目名称：	gamegold-filemgmt
 *	作	者：		HeJian
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-14 下午2:05:42
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.filemgmt.business.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.gamegold.filemgmt.business.IFileManager;
import com.wzitech.gamegold.filemgmt.media.IImageManager;
import com.wzitech.gamegold.filemgmt.media.ThumbGeneratorMode;

/**
 * @author HeJian
 *
 */
@Component("fileManagerImpl")
public class FileManagerImpl extends AbstractBusinessObject implements IFileManager{
	/**
	 * 日志记录器
	 */
	private final static Logger logger = LoggerFactory.getLogger(FileManagerImpl.class);
	
	@Autowired
	IImageManager imageMger;
	
	/**
	 * 头像存放根路径
	 */
	@Value("${image.avatar.rootdir}")
	private String avatarRootDir = "/srv/gamegold/userfile";
	
	/**
	 * 头像缩略图规格
	 */
	@Value("${image.avatar.thumb.gensizelist}")
	private String avatarSizeList="58";
	
	/**
	 * 商品图片存放根路径
	 */
	@Value("${image.goods.rootdir}")
	private String imageGoodsRootDir="/srv/gamegold/goodsfile";
	
	/**
	 * 商品图片缩略图规格
	 */
	@Value("${image.goods.thumb.gensizelist}")
	private String imageGoodsSizeList="200";
	
	/**
	 * 游戏交易地点图片存放根路径
	 */
	@Value("${image.tradePlace.rootdir}")
	private String imageTradePlaceRootDir="/srv/gamegold/tradePlaceFile";
	
	/**
	 * 游戏交易地点缩略图规格
	 */
	@Value("${image.tradePlace.thumb.gensizelist}")
	private String imageTradePlaceSizeList="";
	
	/**
	 * 密保卡存放根路径
	 */
	@Value("${image.passpod.rootdir}")
	private String passpodRootDir="/srv/gamegold/passpod";
	
	/**
	 * 密保卡缩略图规格
	 */
	@Value("${image.passpod.thumb.gensizelist}")
	private String passpodSizeList="";
	
	@Override
	public String[] saveAvatar(byte[] image, String uid) {
		avatarRootDir = FilenameUtils.normalize(avatarRootDir);
		// 原始图片全路径
		String imageFullUrl = FilenameUtils.normalize(avatarRootDir + System.getProperty("file.separator")
				 + uid + System.getProperty("file.separator") + "avatar.jpg");
		// 生成路径(包含原图片路径及缩略图路径)
		String[] geneUrlArray = new String[avatarSizeList.split("[,;]").length + 1];
		// 赋值原图片路径
		int index = imageFullUrl.lastIndexOf("gamegold");
		if(index!=-1){
			geneUrlArray[0] = imageFullUrl.substring(index-1);// 原始图片路径
			//geneUrlArray[0] = imageFullUrl.replace(FilenameUtils.normalize(System.getProperty("file.separator")+"srv"), ""); // 原始图片路径
		}
		
		logger.debug("生成完整的文件名:{}", imageFullUrl);
		
		OutputStream outputStream = null;
		try {
			File imagePreFix = new File(FilenameUtils.getFullPathNoEndSeparator(imageFullUrl));
			FileUtils.forceMkdir(imagePreFix);// 生成目录如果文件目录不存在
			
			outputStream = new FileOutputStream(new File(imageFullUrl));
			outputStream.write(image);
			outputStream.close();
			
			// 生成缩略图，多个缩略图路径之间用","分割
			String thumbUrls = imageMger.generateThumb(imageFullUrl, ThumbGeneratorMode.ScaleByWidthAndHeight, avatarRootDir, avatarSizeList);
			if(StringUtils.isNotEmpty(thumbUrls)){
				String[] thumbUrlArray = thumbUrls.split("[,;]");
				for (int i=0; i<thumbUrlArray.length; i++) {
					// 将缩略图路径赋值到geneUrlArray中
					index = thumbUrlArray[i].lastIndexOf("gamegold");
					if(index!=-1){
						geneUrlArray[i+1] = thumbUrlArray[i].substring(index-1);
						//geneUrlArray[i+1] = thumbUrlArray[i].replace(FilenameUtils.normalize(System.getProperty("file.separator")+"srv"), "");
					}
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			logger.debug("文件{}没有找到{}",new Object[]{imageFullUrl,e1});
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug("生成文件{}出错{}",new Object[]{imageFullUrl,e});
		}
		return geneUrlArray;
	}
	
	@Override
	public String[] saveTradePlaceImage(byte[] image) {
		imageTradePlaceRootDir = FilenameUtils.normalize(imageTradePlaceRootDir);
		// 原始图片全路径
		String imageFullUrl = FilenameUtils.normalize(imageTradePlaceRootDir + System.getProperty("file.separator")
				 + java.util.UUID.randomUUID().toString().replace("-", "")+ ".jpg");
		// 生成路径(包含原图片路径及缩略图路径)
		String[] geneUrlArray = new String[imageTradePlaceSizeList.split("[,;]").length + 1];
		// 赋值原图片路径
		int index = imageFullUrl.lastIndexOf("gamegold");
		if(index!=-1){
			geneUrlArray[0] = imageFullUrl.substring(index-1);// 原始图片路径
			//geneUrlArray[0] = imageFullUrl.replace(FilenameUtils.normalize(System.getProperty("file.separator")+"srv"), "");			
		}
		
		logger.debug("生成完整的文件名:{}", imageFullUrl);
		
		OutputStream outputStream = null;
		try {
			File imagePreFix = new File(FilenameUtils.getFullPathNoEndSeparator(imageFullUrl));
			FileUtils.forceMkdir(imagePreFix);// 生成目录如果文件目录不存在
			
			outputStream = new FileOutputStream(new File(imageFullUrl));
			outputStream.write(image);
			outputStream.close();
			
			// 生成缩略图，多个缩略图路径之间用","分割
			String thumbUrls = imageMger.generateThumb(imageFullUrl, ThumbGeneratorMode.ScaleByWidthAndHeight, imageTradePlaceRootDir, imageTradePlaceSizeList);
			if(StringUtils.isNotEmpty(thumbUrls)){
				String[] thumbUrlArray = thumbUrls.split("[,;]");
				for (int i=0; i<thumbUrlArray.length; i++) {
					// 将缩略图路径赋值到geneUrlArray中
					index = thumbUrlArray[i].lastIndexOf("gamegold");
					if(index!=-1){
						geneUrlArray[i+1] = thumbUrlArray[i].substring(index-1);
						//geneUrlArray[i+1] = thumbUrlArray[i].replace(FilenameUtils.normalize(System.getProperty("file.separator")+"srv"), "");
					}
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			logger.debug("文件{}没有找到{}",new Object[]{imageFullUrl,e1});
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug("生成文件{}出错{}",new Object[]{imageFullUrl,e});
		}
		return geneUrlArray;
	}
	
	@Override
	public String[] saveGoodsImage(byte[] image, String uid) {
		imageGoodsRootDir = FilenameUtils.normalize(imageGoodsRootDir);
		// 原始图片全路径
		String imageFullUrl = FilenameUtils.normalize(imageGoodsRootDir + System.getProperty("file.separator")
				 + uid + System.getProperty("file.separator")
				 + java.util.UUID.randomUUID().toString().replace("-", "")+ ".jpg");
		// 生成路径(包含原图片路径及缩略图路径)
		String[] geneUrlArray = new String[imageGoodsSizeList.split("[,;]").length + 1];
		// 赋值原图片路径
		int index = imageFullUrl.lastIndexOf("gamegold");
		if(index!=-1){
			geneUrlArray[0] = imageFullUrl.substring(index-1);// 原始图片路径
			//geneUrlArray[0] = imageFullUrl.replace(FilenameUtils.normalize(System.getProperty("file.separator")+"srv"), "");			
		}
		
		logger.debug("生成完整的文件名:{}", imageFullUrl);
		
		OutputStream outputStream = null;
		try {
			File imagePreFix = new File(FilenameUtils.getFullPathNoEndSeparator(imageFullUrl));
			FileUtils.forceMkdir(imagePreFix);// 生成目录如果文件目录不存在
			
			outputStream = new FileOutputStream(new File(imageFullUrl));
			outputStream.write(image);
			outputStream.close();
			
			// 生成缩略图，多个缩略图路径之间用","分割
			String thumbUrls = imageMger.generateThumb(imageFullUrl, ThumbGeneratorMode.ScaleByWidthAndHeight, imageGoodsRootDir, imageGoodsSizeList);
			if(StringUtils.isNotEmpty(thumbUrls)){
				String[] thumbUrlArray = thumbUrls.split("[,;]");
				for (int i=0; i<thumbUrlArray.length; i++) {
					// 将缩略图路径赋值到geneUrlArray中
					index = thumbUrlArray[i].lastIndexOf("gamegold");
					if(index!=-1){
						geneUrlArray[i+1] = thumbUrlArray[i].substring(index-1);
						//geneUrlArray[i+1] = thumbUrlArray[i].replace(FilenameUtils.normalize(System.getProperty("file.separator")+"srv"), "");
					}
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			logger.debug("文件{}没有找到{}",new Object[]{imageFullUrl,e1});
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug("生成文件{}出错{}",new Object[]{imageFullUrl,e});
		}
		return geneUrlArray;
	}

	@Override
	public String getUserAvatarSizeList() {
		return this.avatarSizeList;
	}

	@Override
	public String getImageGoodsSizeList() {
		return this.imageGoodsSizeList;
	}

	@Override
	public String getImageTradePlaceSizeList() {
		return this.imageTradePlaceSizeList;
	}

	@Override
	public String[] savePasspod(byte[] file, String uid) {
		passpodRootDir = FilenameUtils.normalize(passpodRootDir);
		// 原始图片全路径
		String imageFullUrl = FilenameUtils.normalize(passpodRootDir + System.getProperty("file.separator")
				 + uid + System.getProperty("file.separator")
				 + java.util.UUID.randomUUID().toString().replace("-", "")+ ".jpg");
		// 生成路径(包含原图片路径及缩略图路径)
		String[] geneUrlArray = new String[passpodSizeList.split("[,;]").length + 1];
		// 赋值原图片路径
		int index = imageFullUrl.lastIndexOf("gamegold");
		if(index!=-1){
			geneUrlArray[0] = imageFullUrl.substring(index-1);// 原始图片路径
			//geneUrlArray[0] = imageFullUrl.replace(FilenameUtils.normalize(System.getProperty("file.separator")+"srv"), "");			
		}
		
		logger.debug("生成完整的文件名:{}", imageFullUrl);
		
		OutputStream outputStream = null;
		try {
			File imagePreFix = new File(FilenameUtils.getFullPathNoEndSeparator(imageFullUrl));
			FileUtils.forceMkdir(imagePreFix);// 生成目录如果文件目录不存在
			
			outputStream = new FileOutputStream(new File(imageFullUrl));
			outputStream.write(file);
			outputStream.close();
			
			// 生成缩略图，多个缩略图路径之间用","分割
			String thumbUrls = imageMger.generateThumb(imageFullUrl, ThumbGeneratorMode.ScaleByWidthAndHeight, passpodRootDir, passpodSizeList);
			if(StringUtils.isNotEmpty(thumbUrls)){
				String[] thumbUrlArray = thumbUrls.split("[,;]");
				for (int i=0; i<thumbUrlArray.length; i++) {
					// 将缩略图路径赋值到geneUrlArray中
					index = thumbUrlArray[i].lastIndexOf("gamegold");
					if(index!=-1){
						geneUrlArray[i+1] = thumbUrlArray[i].substring(index-1);
						//geneUrlArray[i+1] = thumbUrlArray[i].replace(FilenameUtils.normalize(System.getProperty("file.separator")+"srv"), "");
					}
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			logger.debug("文件{}没有找到{}",new Object[]{imageFullUrl,e1});
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug("生成文件{}出错{}",new Object[]{imageFullUrl,e});
		}
		return geneUrlArray;
	}

}
