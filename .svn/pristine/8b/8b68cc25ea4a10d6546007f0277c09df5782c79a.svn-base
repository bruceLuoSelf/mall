/************************************************************************************
 *  Copyright 2012 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		AccountServiceImpl
 *	包	名：		com.wzitech.chinabeauty.facade.service.usermgmt.impl
 *	项目名称：	chinabeauty-facade-frontend
 *	作	者：		SunChengfei
 *	创建时间：	2013-9-26
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2013-9-26 下午2:51:45
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.wzitech.gamegold.common.entity.BaseDTO;
import com.wzitech.gamegold.shorder.business.IGoodsTypeManager;
import com.wzitech.gamegold.shorder.entity.GoodsType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.backend.contant.WebServerContants;
import com.wzitech.gamegold.facade.backend.extjs.AbstractFileUploadAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.filemgmt.business.IFileManager;
import com.wzitech.gamegold.order.business.IGameConfigManager;
import com.wzitech.gamegold.order.entity.GameConfigEO;

@Controller
@Scope("prototype")
@ExceptionToJSON
public class GameConfigAction extends AbstractFileUploadAction {

	private static final long serialVersionUID = 2212456161675686700L;

	private GameConfigEO gameConfig;

	private GoodsType goodsType;

	public GoodsType getGoodsType(){
		return goodsType;
	}
	public void setGoodsType(GoodsType goodsType){
		this.goodsType = goodsType;
	}

	// 封装上传文件域的属性
	private List<File> images;

	// 封装上传文件名的属性
	private List<String> imagesFileName;

	private Long id;

	private List<GameConfigEO> gameConfigList;

	private List<GoodsType> goodsTypeList;

	public void setGoodsTypeList(List<GoodsType> goodsTypeList){
		this.goodsTypeList = goodsTypeList;
	}

	public List<GoodsType> getGoodsTypeList(){
		return goodsTypeList;
	}

    /**
     * 商城的所有游戏名称和ID
     */
    private List<BaseDTO> gameNameIdList;

	private List<BaseDTO> goodsTypeNameIdList;

	@Autowired
	IGameConfigManager gameConfigManager;
    @Autowired
	IGoodsTypeManager goodsTypeManager;
	@Autowired
	IFileManager fileManager;

    /**
     * 获取商城的所有游戏名称和ID
     * @return
     */
    public String queryGameNameIdList() {
        List<GameConfigEO> list = gameConfigManager.queryGameNameIdList();
        if (CollectionUtils.isNotEmpty(list)) {
            gameNameIdList = Lists.newArrayList();
            for (GameConfigEO config : list) {
                gameNameIdList.add(new BaseDTO(config.getId(), config.getGameName()));
            }
               gameNameIdList.add(0,new BaseDTO(list.size(),"全部"));
        }
        return this.returnSuccess();
    }

	public String queryGoodsTypeNameIdList(){
		List<GoodsType> list = goodsTypeManager.queryGoodsTypeNameIdList();
		if (CollectionUtils.isNotEmpty(list)) {
			goodsTypeNameIdList = Lists.newArrayList();
			for (GoodsType config : list) {
				goodsTypeNameIdList.add(new BaseDTO(config.getId(), config.getName()));
			}
		}
		return this.returnSuccess();
	}

	/**
	 * 查询游戏交易地列表
	 * 
	 * @return
	 */
	public String queryGameConfig() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("gameName", gameConfig.getGameName());
		paramMap.put("placeName", gameConfig.getPlaceName());
		/**************ZW_C_JB_00008_20170524 ADD START***********/
		if("游戏币".equals(gameConfig.getGoodsTypeName())){
			paramMap.put("goodsTypeName", "");
		}else{
			paramMap.put("goodsTypeName", gameConfig.getGoodsTypeName());
		}
		/**************ZW_C_JB_00008_20170524 ADD END***********/
		GenericPage<GameConfigEO> genericPage = gameConfigManager
				.queryGameConfig(paramMap, this.limit, this.start, null, true);
		gameConfigList = genericPage.getData();
		totalCount = genericPage.getTotalCount();
		return this.returnSuccess();
	}

	private void createPlaceImageUrl(File placeImage, String placeImageFileName)
			throws IOException {
		if (placeImage != null) {
			if (!FilenameUtils.isExtension(placeImageFileName.toLowerCase(),
					WebServerContants.IMAGE_EXTENSIONS)) {
				throw new SystemException(
						ResponseCodes.ImageTypeWrong.getCode());
			}
			String[] uris = fileManager.saveTradePlaceImage(WebServerUtil
					.changeFileToByteArray(placeImage));
			if (StringUtils.isBlank(uris[0])) {
				throw new SystemException(
						ResponseCodes.UserThumbWrong.getCode());
			}
			gameConfig.setPlaceImage(uris[0]);
		}
	}

	private void createGoodsImageUrl(File goodsImage, String goodsImageFileName)
			throws IOException {
		if (goodsImage != null) {
			if (!FilenameUtils.isExtension(goodsImageFileName.toLowerCase(),
					WebServerContants.IMAGE_EXTENSIONS)) {
				throw new SystemException(
						ResponseCodes.ImageTypeWrong.getCode());
			}
			String[] uris = fileManager.saveGoodsImage(WebServerUtil.changeFileToByteArray(goodsImage), CurrentUserContext.getUid().toString());
			if (StringUtils.isBlank(uris[0])) {
				throw new SystemException(
						ResponseCodes.UserThumbWrong.getCode());
			}
			gameConfig.setGameImage(uris[0]);
		}
	}

	/**
	 * 新增游戏交易地
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addGameConfig() throws Exception {
		try {
			if (images != null) {
				if (StringUtils.isNotBlank(gameConfig.getPlaceImage())&&StringUtils.isNotBlank(gameConfig.getGameImage())) {
					createPlaceImageUrl(images.get(0), imagesFileName.get(0));
					createGoodsImageUrl(images.get(1), imagesFileName.get(1));
				}else if(StringUtils.isNotBlank(gameConfig.getPlaceImage())&&StringUtils.isBlank(gameConfig.getGameImage())){
					createPlaceImageUrl(images.get(0), imagesFileName.get(0));
				}else if(StringUtils.isBlank(gameConfig.getPlaceImage())&&StringUtils.isNotBlank(gameConfig.getGameImage())){
					createGoodsImageUrl(images.get(0), imagesFileName.get(0));
				}
			}
			gameConfigManager.addGameConfig(gameConfig);
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}

	/**
	 * 修改游戏交易地
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyGameConfig() throws Exception {
		try {
			if (images != null) {
				if (StringUtils.isNotBlank(gameConfig.getPlaceImage())&&StringUtils.isNotBlank(gameConfig.getGameImage())) {
					createPlaceImageUrl(images.get(0), imagesFileName.get(0));
					createGoodsImageUrl(images.get(1), imagesFileName.get(1));
				}else if(StringUtils.isNotBlank(gameConfig.getPlaceImage())&&StringUtils.isBlank(gameConfig.getGameImage())){
					createPlaceImageUrl(images.get(0), imagesFileName.get(0));
				}else if(StringUtils.isBlank(gameConfig.getPlaceImage())&&StringUtils.isNotBlank(gameConfig.getGameImage())){
					createGoodsImageUrl(images.get(0), imagesFileName.get(0));
				}
			}
			gameConfigManager.modifyGameConfig(gameConfig);
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}

	/**
	 * 删除游戏交易地
	 * 
	 * @return
	 */
	public String deleteGameConfig() {
		try {
			gameConfigManager.deleteGameConfig(id);
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}

	public GameConfigEO getGameConfig() {
		return gameConfig;
	}

    public List<BaseDTO> getGameNameIdList() {
        return gameNameIdList;
    }


    public List<BaseDTO> getGoodsTypeNameIdList(){return goodsTypeNameIdList;}

    public void setGameConfig(GameConfigEO gameConfig) {
		this.gameConfig = gameConfig;
	}

	public List<GameConfigEO> getGameConfigList() {
		return gameConfigList;
	}

	public void setImages(List<File> images) {
		this.images = images;
	}

	public void setImagesFileName(List<String> imagesFileName) {
		this.imagesFileName = imagesFileName;
	}

	public void setId(Long id) {
		this.id = id;
	}
}