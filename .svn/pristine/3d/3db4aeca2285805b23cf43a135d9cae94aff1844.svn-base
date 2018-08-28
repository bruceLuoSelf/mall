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
package com.wzitech.gamegold.facade.backend.action.goodsmgmt;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wzitech.gamegold.common.constants.ServicesContants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractFileUploadAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.filemgmt.business.IFileManager;
import com.wzitech.gamegold.goods.business.IDiscountInfoManager;
import com.wzitech.gamegold.goods.business.IGoodsInfoManager;
import com.wzitech.gamegold.goods.entity.DiscountInfo;
import com.wzitech.gamegold.goods.entity.GoodsInfo;

@Controller
@Scope("prototype")
@ExceptionToJSON
public class GoodsAction extends AbstractFileUploadAction {

	public void setState(String state) {
		this.state = state;
	}

	private static final long serialVersionUID = 2212456161675686700L;

	private GoodsInfo goods;
    
	private BigDecimal differPrice;

	private Long id;

    private List<Long> ids;

	private List<GoodsInfo> goodsList;
	
	private List<DiscountInfo> goodsDiscountList;
	
	private String state;

	@Autowired
	IGoodsInfoManager goodsInfoManager;
	
	@Autowired
	IDiscountInfoManager discountInfoManager;

	@Autowired
	IFileManager fileManager;
	
	/**
	 * 用于form表单提交的时候，把discountList以JSON字符串的形式提交
	 */
	private String discountList;
	
	/**
	 * 批量修改价格
	 * 
	 * @return
	 */
	public String batchModifyPrice() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("gameName", goods.getGameName());
		paramMap.put("region", goods.getRegion());
		paramMap.put("server", goods.getServer());
		paramMap.put("title", goods.getTitle());
		paramMap.put("differPrice", differPrice);
		paramMap.put("state",state);
		if(!ServicesContants.TYPE_ALL.equals(goods.getGoodsTypeName())){
			paramMap.put("goodsTypeName",goods.getGoodsTypeName());
		}
		goodsInfoManager.differPrice(paramMap);
		return this.returnSuccess();
	}

	/**
	 * 查询商品信息列表
	 * 
	 * @return
	 */
	public String queryGoods() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (paramMap != null){
		paramMap.put("gameName", goods.getGameName());
		paramMap.put("region", goods.getRegion());
		paramMap.put("server", goods.getServer());
		paramMap.put("title", goods.getTitle());
		paramMap.put("isDeleted", goods.getIsDeleted());
		if (!("全部").equals(goods.getGoodsTypeName())){
			paramMap.put("goodsTypeName",goods.getGoodsTypeName());
		}
	}
		GenericPage<GoodsInfo> genericPage = goodsInfoManager.queryGoodsInfo(paramMap,
				this.limit, this.start, null, true);
		goodsList = genericPage.getData();
		totalCount = genericPage.getTotalCount();
		return this.returnSuccess();
	}
	
	/**
	 * 查询商品折扣信息列表
	 * 
	 * @return
	 */
	public String queryGoodsDiscount() {
		goodsDiscountList = discountInfoManager.queryDiscountInfos(id);
		return this.returnSuccess();
	}
	
	/*private void createGoodsImageUrl() throws IOException {
		if(imageUrls!=null){
			if(!FilenameUtils.isExtension(imageUrlsFileName.toLowerCase(), WebServerContants.IMAGE_EXTENSIONS)){
				throw new SystemException(ResponseCodes.ImageTypeWrong.getCode());
			}
			String[] uris = fileManager.saveGoodsImage(WebServerUtil.changeFileToByteArray(imageUrls), CurrentUserContext.getUid().toString());
			if(StringUtils.isBlank(uris[0])){
				throw new SystemException(ResponseCodes.UserThumbWrong.getCode());
			}
			goods.setImageUrls(uris[0]);				
		}
	}*/

	/**
	 * 新增商品信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addGoods() throws Exception {
		try {
			//createGoodsImageUrl();
			List<DiscountInfo> discountInfoList = null;
			if(StringUtils.isNotBlank(discountList)){
				DiscountInfo[] discountArray = objectMapper.readValue(discountList, DiscountInfo[].class);
				discountInfoList = Arrays.asList(discountArray);
				goods.setDiscountList(discountInfoList);				
			}
			goodsInfoManager.addGoodsInfo(goods);
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}

	/**
	 * 修改商品信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyGoods() throws Exception {
		try {
			//createGoodsImageUrl();
			List<DiscountInfo> discountInfoList = null;
			if(StringUtils.isNotBlank(discountList)){
				DiscountInfo[] discountArray = objectMapper.readValue(discountList, DiscountInfo[].class);
				discountInfoList = Arrays.asList(discountArray);
				goods.setDiscountList(discountInfoList);				
			}
			goodsInfoManager.modifyGoodsInfo(goods,state);
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}
	/**
	 * 删除商品信息
	 * @return
	 */
	public String delGoods(){
		try{
			goodsInfoManager.delGoods(ids);
			return this.returnSuccess();
		}catch(SystemException e){
			return this.returnError(e);
		}
	}
	/**
	 * 禁用商品信息
	 * 
	 * @return
	 */
	public String disableGoods() {
		try {
			goodsInfoManager.disableGoods(ids);
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}
	
	/**
	 * 启用商品信息
	 * 
	 * @return
	 */
	public String enableGoods() {
		try {
			goodsInfoManager.enableGoods(ids);
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}

	public List<GoodsInfo> getGoodsList() {
		return goodsList;
	}

	public void setGoods(GoodsInfo goods) {
		this.goods = goods;
	}

	public GoodsInfo getGoods() {
		return goods;
	}

	/*public void setImageUrls(File imageUrls) {
		this.imageUrls = imageUrls;
	}

	public void setImageUrlsFileName(String imageUrlsFileName) {
		this.imageUrlsFileName = imageUrlsFileName;
	}*/

	public void setId(Long id) {
		this.id = id;
	}

	public void setDiscountList(String discountList) {
		this.discountList = discountList;
	}

	public List<DiscountInfo> getGoodsDiscountList() {
		return goodsDiscountList;
	}

	public void setDifferPrice(BigDecimal differPrice) {
		this.differPrice = differPrice;
	}

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}