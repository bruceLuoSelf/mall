/************************************************************************************
 * Copyright 2014 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		IGoodsInfoManager
 * 包	名：		com.wzitech.gamegold.goodsmgmt.business
 * 项目名称：	gamegold-goods
 * 作	者：		SunChengfei
 * 创建时间：	2014-2-15
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014-2-15 上午11:48:26
 ************************************************************************************/
package com.wzitech.gamegold.goods.business;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.wzitech.chaos.framework.server.common.exception.BusinessException;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.goods.entity.GoodsInfo;

/**
 * 商品管理接口
 * @author SunChengfei
 **         Update History
 *         Date         Name            Reason For Update
 *         ----------------------------------------------
 *         2017/5/12    wrf              ZW_C_JB_00008商城增加通货
 */

public interface IGoodsInfoManager {
    /**
     * 添加商品
     * @param goodsInfo
     * @return
     * @throws BusinessException
     */
    GoodsInfo addGoodsInfo(GoodsInfo goodsInfo) throws SystemException;

    /**
     * 添加商品销量
     * @param goodsId
     * @return
     */
    GoodsInfo addSales(long goodsId);

    /**
     * 后台添加价格信息
     * @param pricefile[]
     * @return
     * @throws SystemException
     */
    void mdPriceByExcel(byte[] file, String state) throws SystemException, IOException;

    /**
     * 修改商品
     * @param goodsInfo
     * @return
     * @throws BusinessException
     */
    GoodsInfo modifyGoodsInfo(GoodsInfo goodsInfo, String state) throws SystemException;

    /**
     * 根据条件分页查询商品
     * @param queryMap
     * @param limit
     * @param start
     * @param sortBy
     * @param isAsc
     * @return
     * @throws BusinessException
     */
    GenericPage<GoodsInfo> queryGoodsInfo(Map<String, Object> queryMap, int limit, int start,
                                          String sortBy, boolean isAsc);

    /**
     * 根据条件查询商品单价
     * @param queryMap
     * @param orderBy
     * @param isAsc
     * @return
     */
    List<GoodsInfo> queryUnitPrice(String gameID, String regionID, String serverID, String raceID,String goodsTypeName, String orderBy, boolean isAsc);

    /**
     * 条件查询指定栏目商品
     * @param gameName
     * @param region
     * @param server
     * @param gameRace
     * @goodsCat 商品栏目
     * @return
     *
     */
    public GoodsInfo getMaxSaleGoods(String gameName, String goodsTypeName, String region,
                                     String server, String gameRace, int goodsCat);/**ZW_C_JB_00008_20170512 add**/


    /**
     *
     * <p>批量修改价格</p>
     * @author ztjie
     * @date 2014-3-27 下午8:05:35
     * @param paramMap
     * @see
     */
    void differPrice(Map<String, Object> paramMap);

    /**
     * 批量禁用商品
     * @param ids
     * @throws SystemException
     */
    void disableGoods(List<Long> ids) throws SystemException;

    /**
     * 禁用商品
     * @param id
     * @return
     * @throws BusinessException
     */
    GoodsInfo disableGoods(Long id) throws SystemException;

    /**
     * 批量启用商品
     * @param ids
     * @throws SystemException
     */
    void enableGoods(List<Long> ids) throws SystemException;

    /**
     * 启用商品
     * @param id
     * @return
     * @throws BusinessException
     */
    GoodsInfo enableGoods(Long id) throws SystemException;

    void delGoods(List<Long> ids) throws SystemException;

    void delGoods(Long id) throws SystemException;

    void batchModifyPrice(GoodsInfo goodsInfo, String state) throws SystemException;

    /**
     * 根据ID获取商品对象
     * @param id
     * @return
     */
    GoodsInfo selectById(Long id);

    /**
     * 获取主站对应的游戏区服id
     * @param goodsInfo
     * @return
     */
    GoodsInfo setGoodsGameId(GoodsInfo goodsInfo);
}

