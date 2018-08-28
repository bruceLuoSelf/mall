/************************************************************************************
 * Copyright 2013 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		ThumbGenerator
 * 包	名：		com.wzitech.gamegold.filemgmt.media
 * 项目名称：	gamegold-filemgmt
 * 作	者：		Shawn
 * 创建时间：	2013-9-26
 * 描	述：
 * 更新纪录：	1. Shawn 创建于 2013-9-26 下午2:51:45
 ************************************************************************************/
package com.wzitech.gamegold.filemgmt.media;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import magick.ImageInfo;
import magick.MagickImage;
import magick.PixelPacket;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Shawn
 *
 */
@Component("thumbGenerator")
public class ThumbGenerator {
    /**
     * 日志记录器
     */
    private final static Logger logger = LoggerFactory.getLogger(ThumbGenerator.class);

    static {
        //不能漏掉这个，不然jmagick.jar的路径找不到
        System.setProperty("jmagick.systemclassloader", "no");
    }

    public String generateThumb(String imageFullName, ThumbGeneratorMode mode, String rootDir, String sizeList) {
        logger.debug("开始为{}生成size list：{}的缩略图", new Object[]{imageFullName, sizeList});

        // 多张图片url间以","分隔
        String generateImageUrls = "";

        if (StringUtils.isNotBlank(sizeList)) {
            for (String thumbSize : sizeList.split("[,:;]")) {
                String[] widthAndHeight = thumbSize.split("x");
                String width = widthAndHeight[0];
                String height = (widthAndHeight.length >= 2) ? widthAndHeight[1] : width;
                String imageUrl = generateUseJMagick(imageFullName, mode, Integer.parseInt(width), Integer.parseInt(height));
                generateImageUrls += FilenameUtils.normalize(imageUrl);
                generateImageUrls += ",";
            }
        }
        return (generateImageUrls == null) ? null : generateImageUrls.substring(0, generateImageUrls.length() - 1);
    }

    /**
     * 返回生成缩略图的路径
     * 使用JMagick
     * @param imageFullName
     * @param mode
     * @param width
     * @param height
     * @return
     */
    public static String generateUseJMagick(String imageFullName,
                                            ThumbGeneratorMode mode, int width, int height) {
        try {
//            File fi = new File(imageFullName); // 大图文件
            String fileSuffix = FilenameUtils.getExtension(imageFullName);
//
//            // 如果要生成的原图的不存在则直接返回
//            if (!fi.exists()) {
//                logger.error("图片{}不存在，无法生成缩略图. ", imageFullName);
//                return null;
//            }

            File imgFile = new File(imageFullName);
            if (!imgFile.exists()) {
                logger.error("图片{}不存在，无法生成缩略图. ", imageFullName);
                return null;
            }
            Image originalImageDimension = ImageIO.read(imgFile);
//			ImageInfo originalImageInfo = new ImageInfo(imageFullName);
//			MagickImage originalImage = new MagickImage(originalImageInfo);
//			Dimension originalImageDimension = originalImage.getDimension();

            int oWidth = originalImageDimension.getWidth(null); // 得到源图宽
            int oHeight = originalImageDimension.getHeight(null); // 得到源图长
            int scaleWidth = 0;
            int scaleHeight = 0;
//            int startX = 0;
//            int startY = 0;

            double scaling = (double) oWidth / (double) oHeight;

            if (mode == ThumbGeneratorMode.ScaleByWidthAndHeight) {
                double newScaling = (double) width / (double) height;

                // 根据生成图片长宽比例及原始长宽比例缩小图片，
                // 并是图片居中放置
                if (scaling >= newScaling) {
                    // 如果原始图片宽度大于长度，依据长度调整宽度
                    scaleWidth = width;
                    scaleHeight = (int) (scaleWidth / scaling);
//					startX = 0;
//					startY = (int) ((height - scaleHeight) / 2);
                } else {
                    scaleHeight = height;
                    scaleWidth = (int) (scaleHeight * scaling);
//					startX = (int) ((width - scaleWidth) / 2);
//					startY = 0;
                }
            } else if (mode == ThumbGeneratorMode.ScaleByWidth) {
                scaleWidth = width;
                scaleHeight = (int) (scaleWidth / scaling);
                height = scaleHeight;
//				startX = 0;
//				startY = 0;
            } else if (mode == ThumbGeneratorMode.ScaleByHeight) {
                scaleHeight = height;
                scaleWidth = (int) (scaleHeight * scaling);
                width = scaleWidth;
//				startX = 0;
//				startY = 0;
            }


            // 设置缩略图文件名
            // 如原图为afcdeftgh1234321.jpg
            // 则缩略图格式为afcdeftgh1234321_width*height.jpg
            String thumbFileName = FilenameUtils.getBaseName(imageFullName) + "_" + width + "x" + height + "." + fileSuffix;
            // 设置缩略图带完整路径的文件名
            String thumbFullName = FilenameUtils.getFullPath(imageFullName) + thumbFileName;
            logger.info("缩略图完整文件名：{}", thumbFullName);

//			File fo = new File(thumbFullName); // 将要转换出的小图文件
//			// 直接返回如果缩略图已存在.
//			if (fo.exists()) {
//				return;
//			}

//			MagickImage thumbImage = originalImage.scaleImage(scaleWidth, scaleHeight);
            BufferedImage bi = new BufferedImage(scaleWidth, scaleHeight, BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.getGraphics();
            g.drawImage(originalImageDimension, 0, 0, scaleWidth, scaleHeight, Color.LIGHT_GRAY, null);
            g.dispose();
            // 将图片保存在原文件夹并加上前缀
            ImageIO.write(bi, fileSuffix, new File(thumbFullName));

//			thumbImage.setBorderColor(PixelPacket.queryColorDatabase("white"));
//			MagickImage borderedThumbImage = thumbImage.borderImage(new Rectangle(0, 0, startX, startY));
//
//			borderedThumbImage.setFileName(thumbFullName);
//			borderedThumbImage.writeImage(originalImageInfo);

            logger.info("成功为{}生成缩略图{}", new Object[]{imageFullName, thumbFullName});
            return thumbFullName;
        } catch (Exception e) {
            logger.error("为{}生成缩略图时发生错误：{}",
                    new Object[]{imageFullName, e});
            return null;
        }
    }

    /**
     * 返回生成缩略图的路径
     * 使用Pure Java
     * @param imageFullName
     * @param mode
     * @param width
     * @param height
     * @return
     */
    public static String generateUseJava(String imageFullName,
                                         ThumbGeneratorMode mode, int width, int height) {
        try {
            File fi = new File(imageFullName); // 大图文件
            String fileSuffix = FilenameUtils.getExtension(imageFullName);

            // 如果要生成的原图的不存在则直接返回
            if (!fi.exists()) {
                logger.error("图片{}不存在，无法生成缩略图. ", imageFullName);
                return null;
            }

            BufferedImage originalImage = ImageIO.read(fi); // 读入文件
            int oWidth = originalImage.getWidth(); // 得到源图宽
            int oHeight = originalImage.getHeight(); // 得到源图长
            int scaleWidth = 0;
            int scaleHeight = 0;
            int startX = 0;
            int startY = 0;

            double scaling = (double) oWidth / (double) oHeight;

            if (mode == ThumbGeneratorMode.ScaleByWidthAndHeight) {
                double newScaling = (double) width / (double) height;

                // 根据生成图片长宽比例及原始长宽比例缩小图片，
                // 并是图片居中放置
                if (scaling >= newScaling) {
                    // 如果原始图片宽度大于长度，依据长度调整宽度
                    scaleWidth = width;
                    scaleHeight = (int) (scaleWidth / scaling);
                    startX = 0;
                    startY = (int) ((height - scaleHeight) / 2);
                } else {
                    scaleHeight = height;
                    scaleWidth = (int) (scaleHeight * scaling);
                    startX = (int) ((width - scaleWidth) / 2);
                    startY = 0;
                }
            } else if (mode == ThumbGeneratorMode.ScaleByWidth) {
                scaleWidth = width;
                scaleHeight = (int) (scaleWidth / scaling);
                height = scaleHeight;
                startX = 0;
                startY = 0;
            } else if (mode == ThumbGeneratorMode.ScaleByHeight) {
                scaleHeight = height;
                scaleWidth = (int) (scaleHeight * scaling);
                width = scaleWidth;
                startX = 0;
                startY = 0;
            }


            // 设置缩略图文件名
            // 如原图为afcdeftgh1234321.jpg
            // 则缩略图格式为afcdeftgh1234321_width*height.jpg
            String thumbFileName = FilenameUtils.getBaseName(imageFullName)
                    + "_" + width + "x" + height + "." + fileSuffix;
            // 设置缩略图带完整路径的文件名
            String thumbFullName = FilenameUtils.getFullPath(imageFullName)
                    + thumbFileName;

            logger.debug("缩略图完整文件名：{}", thumbFullName);


            File fo = new File(thumbFullName); // 将要转换出的小图文件
            // 直接返回如果缩略图已存在.
            if (fo.exists()) {
                return thumbFullName;
            }

            Image image = originalImage.getScaledInstance(scaleWidth,
                    scaleHeight, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, width, height);
            g.drawImage(image, startX, startY, null); // 绘制缩小后的图
            g.dispose();
            ImageIO.write(tag, fileSuffix, fo);// 输出到文件流

            logger.debug("成功为{}生成缩略图{}", new Object[]{imageFullName, thumbFullName});
            return thumbFullName;
        } catch (Exception e) {
            logger.error("为{}生成缩略图时发生错误：{}",
                    new Object[]{imageFullName, e});
            return null;
        }
    }
}
