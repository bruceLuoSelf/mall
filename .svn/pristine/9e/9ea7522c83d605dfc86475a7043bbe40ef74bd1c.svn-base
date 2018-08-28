/************************************************************************************
 * Copyright 2014 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		GoodsInfoManagerImpl
 * 包	名：		com.wzitech.gamegold.goodsmgmt.business.impl
 * 项目名称：	gamegold-goods
 * 作	者：		SunChengfei
 * 创建时间：	2014-2-15
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014-2-15 上午11:52:17
 ************************************************************************************/
package com.wzitech.gamegold.goods.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.LogType;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.game.IGameInfoManager;
import com.wzitech.gamegold.common.game.entity.GameNameAndId;
import com.wzitech.gamegold.common.log.entity.GoodsLogInfo;
import com.wzitech.gamegold.common.message.IMobileMsgManager;
import com.wzitech.gamegold.goods.business.IDiscountInfoManager;
import com.wzitech.gamegold.goods.business.IGoodsInfoManager;
import com.wzitech.gamegold.goods.business.IGoodsLogManager;
import com.wzitech.gamegold.goods.dao.IGoodsInfoDBDAO;
import com.wzitech.gamegold.goods.dao.IGoodsInfoRedisDAO;
import com.wzitech.gamegold.goods.entity.DiscountInfo;
import com.wzitech.gamegold.goods.entity.GoodsInfo;
import com.wzitech.gamegold.repository.dao.IRepositoryDBDAO;
import com.wzitech.gamegold.repository.dao.ISellerDBDAO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * 商品管理接口实现
 * @author SunChengfei
 **         Update History
 *         Date         Name            Reason For Update
 *         ----------------------------------------------
 *         2017/5/13    zhujun              ZW_C_JB_00008商城增加通货
 */
@Component
public class GoodsInfoManagerImpl extends AbstractBusinessObject implements IGoodsInfoManager {

    protected static final Log log = LogFactory.getLog(GoodsInfoManagerImpl.class);

    @Autowired
    IGoodsInfoDBDAO goodsInfoDBDAO;

    @Autowired
    IGoodsInfoRedisDAO goodsInfoRedisDAO;

    @Autowired
    IDiscountInfoManager discountInfoManager;

    /*@Autowired
    ILogManager logManager;*/
    @Autowired
    IGoodsLogManager goodsLogManager;

    @Autowired
    IMobileMsgManager mobileMsgManager;

    @Autowired
    IRepositoryDBDAO repositoryDBDAO;

    @Autowired
    ISellerDBDAO sellerDBDAO;

    @Autowired
    IGameInfoManager gameInfoManager;


    @Override
    @Transactional
    public GoodsInfo addGoodsInfo(GoodsInfo goodsInfo) throws SystemException {
        if (goodsInfo == null) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsInfo.getCode(), ResponseCodes.EmptyGoodsInfo.getMessage());
        }
        if (StringUtils.isEmpty(goodsInfo.getGameName())) {
            throw new SystemException(
                    ResponseCodes.EmptyGameName.getCode(), ResponseCodes.EmptyGameName.getCode());
        }
        //现在moneyname可以是空了
        if (null == goodsInfo.getMoneyName()) {
            throw new SystemException(
                    ResponseCodes.EmptyMoneyName.getCode(), ResponseCodes.EmptyMoneyName.getCode());
        }
        if (goodsInfo.getGoodsTypeId() == null) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsTypeId.getCode(), ResponseCodes.EmptyGoodsTypeId.getCode());
        }
        if (StringUtils.isEmpty(goodsInfo.getGoodsTypeName())) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsTypeName.getCode(), ResponseCodes.EmptyGoodsTypeName.getCode());
        }
        if (StringUtils.isEmpty(goodsInfo.getRegion())) {
            throw new SystemException(
                    ResponseCodes.EmptyRegion.getCode(), ResponseCodes.EmptyRegion.getCode());
        }
        if (StringUtils.isEmpty(goodsInfo.getServer())) {
            throw new SystemException(
                    ResponseCodes.EmptyGameServer.getCode(), ResponseCodes.EmptyGameServer.getCode());
        }
        if (StringUtils.isEmpty(goodsInfo.getTitle())) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsTitle.getCode(), ResponseCodes.EmptyGoodsTitle.getCode());
        }
        if (goodsInfo.getUnitPrice() == null) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsPrice.getCode(), ResponseCodes.EmptyGoodsPrice.getMessage());
        }
        if (goodsInfo.getGoodsCat() == null) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsCat.getCode(), ResponseCodes.EmptyGoodsCat.getMessage());
        }
        if (checkGoodsCatExist(goodsInfo)) {
            throw new SystemException(
                    ResponseCodes.ExistGoodsCat.getCode(), ResponseCodes.ExistGoodsCat.getMessage());
        }
        goodsInfo = this.setGoodsGameId(goodsInfo);
        /*if(!("魔兽世界(国服)".equals(goodsInfo.getGameName()))){
			goodsInfo.setUnitPrice(goodsInfo.getUnitPrice().setScale(4, BigDecimal.ROUND_HALF_UP));
		}*/
        // 添加附加信息
        goodsInfo.setIsDeleted(false);
        goodsInfo.setSales(0);
        goodsInfo.setLastUpdateTime(new Date());
        goodsInfo.setCreateTime(new Date());

        // 保存db
        goodsInfoDBDAO.insert(goodsInfo);
        // 保存redis
        goodsInfoRedisDAO.saveGoodsInfo(goodsInfo);

        if (goodsInfo.getDiscountList() != null && goodsInfo.getDiscountList().size() > 0) {
            // 保存商品折扣信息
            for (DiscountInfo discountInfo : goodsInfo.getDiscountList()) {
                discountInfo.setGoodsId((Long) goodsInfo.getId());
                discountInfoManager.addDiscount(discountInfo);
            }
        }

        //logManager.add(ModuleType.GOODS, "新增商品ID为"+goodsInfo.getId()+"的商品信息", CurrentUserContext.getUser());
        log.info("新增商品ID为" + goodsInfo.getId() + "的商品信息");

        GoodsLogInfo logInfo = new GoodsLogInfo();
        logInfo.setLogType(LogType.GOODS_ADD);
        logInfo.setRemark("新增商品");
        goodsLogManager.add(logInfo, goodsInfo);

        return goodsInfo;
    }

    /**
     * 获取对应的主站 游戏区服id
     * @param goodsInfo
     * @return
     */
    public GoodsInfo setGoodsGameId(GoodsInfo goodsInfo) {
        try {
            GameNameAndId gameNameAndId1 = gameInfoManager.getIdByProp(goodsInfo.getGameName().trim(), goodsInfo.getGameName().trim(), 1);
            GameNameAndId gameNameAndId2 = gameInfoManager.getIdByProp(goodsInfo.getGameName().trim(), goodsInfo.getRegion().trim(), 2);
            GameNameAndId gameNameAndId3 = gameInfoManager.getIdByProp(goodsInfo.getGameName().trim(), goodsInfo.getServer().trim(), 3);
            if (gameNameAndId1 != null && gameNameAndId2 != null && gameNameAndId3 != null) {
                goodsInfo.setGameId(gameNameAndId1.getAnyId());
                goodsInfo.setRegionId(gameNameAndId2.getAnyId());
                goodsInfo.setServerId(gameNameAndId3.getAnyId());
            } else {
                throw new SystemException(ResponseCodes.EmptyGameId.getCode(),
                        ResponseCodes.EmptyGameId.getCode());
            }
            if (StringUtils.isEmpty(goodsInfo.getGameRace())) {
                goodsInfo.setRaceId("");
            } else {
                GameNameAndId gameNameAndId4 = gameInfoManager.getIdByProp(goodsInfo.getGameName().trim(), goodsInfo.getGameRace().trim(), 4);
                if (gameNameAndId4 != null) {
                    goodsInfo.setRaceId(gameNameAndId4.getAnyId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goodsInfo;
    }

    @Override
    @Transactional
    public GoodsInfo addSales(long goodsId) {
        GoodsInfo dbGoodsInfo = goodsInfoDBDAO.selectById(goodsId);

        if (dbGoodsInfo == null) {
            return null;
        }

        //增加销量的日志
        StringBuffer sb = new StringBuffer();
        sb.append("增加商品名为").append(dbGoodsInfo.getTitle()).append("的销量，增加前的销量为");

        // 更新销量
        if (dbGoodsInfo.getSales() == null) {
            sb.append(0);
            dbGoodsInfo.setSales(1);
        } else {
            sb.append(dbGoodsInfo.getSales());
            dbGoodsInfo.setSales(dbGoodsInfo.getSales() + 1);
        }

        // 更新db
        goodsInfoDBDAO.update(dbGoodsInfo);


        sb.append("，增加后的销量为" + dbGoodsInfo.getSales());
        //logManager.add(ModuleType.GOODS, sb.toString(), CurrentUserContext.getUser());

        GoodsLogInfo logInfo = new GoodsLogInfo();
        logInfo.setLogType(LogType.GOODS_OTHER);
        logInfo.setRemark(sb.toString());
        goodsLogManager.add(logInfo, dbGoodsInfo);

        // 更新redis
        goodsInfoRedisDAO.saveGoodsInfo(dbGoodsInfo);

        return dbGoodsInfo;
    }

    /*
     * 判断商品的类目是否已经存在
     */
    private boolean checkGoodsCatExist(GoodsInfo goodsInfo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("gameName", goodsInfo.getGameName());
        map.put("region", goodsInfo.getRegion());
        map.put("server", goodsInfo.getServer());
        map.put("goodsTypeName", goodsInfo.getGoodsTypeName());
        if (StringUtils.isNotEmpty(goodsInfo.getGameRace())) {
            map.put("gameRace", goodsInfo.getGameRace());
        }
        map.put("isDeleted", false);
        map.put("goodsCat", goodsInfo.getGoodsCat());
        if (goodsInfoDBDAO.checkGoodsCatExist(map)) {
            return true;
        } else {
            return false;
        }
    }

    public void sendMsgByphone(BigDecimal unitPrice, Map<String, Object> map) {
        Calendar cal = Calendar.getInstance();
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        //在凌晨到早上7点之间
        if (true) {
            String server = (String) map.get("server");
            //这几个区服的价格修改，告知三个管理人员
            if (("安徽1区").equals(server) || ("安徽2区").equals(server) || ("安徽3区").equals(server) || ("福建1区").equals(server)
                    || ("福建2区").equals(server) || ("福建3/4区").equals(server)) {
                try {
                    mobileMsgManager.sendMessage("13575902097", map.get("gameName").toString() + "-" +
                            map.get("region").toString() + "-" + map.get("server").toString() + "的商品价格修改为：" + unitPrice + "【5173游戏币商城】");
                    Thread.sleep(1000);
                    mobileMsgManager.sendMessage("15067088329", map.get("gameName").toString() + "-" +
                            map.get("region").toString() + "-" + map.get("server").toString() + "的商品价格修改为：" + unitPrice + "【5173游戏币商城】");
                    Thread.sleep(1000);
                    mobileMsgManager.sendMessage("15958458215", map.get("gameName").toString() + "-" +
                            map.get("region").toString() + "-" + map.get("server").toString() + "的商品价格修改为：" + unitPrice + "【5173游戏币商城】");
                    Thread.sleep(1000);
                    mobileMsgManager.sendMessage("13738947281", map.get("gameName").toString() + "-" +
                            map.get("region").toString() + "-" + map.get("server").toString() + "的商品价格修改为：" + unitPrice + "【5173游戏币商城】");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        List<RepositoryInfo> repositoryInfos = repositoryDBDAO.selectByMap(map, "ID", false);
        if (repositoryInfos == null) {
            throw new SystemException(
                    ResponseCodes.EmptyRepositoryList.getCode(), ResponseCodes.EmptyRepositoryList.getMessage());
        }
        for (RepositoryInfo repositoryInfo : repositoryInfos) {
            SellerInfo sellerInfo = repositoryInfo.getSellerInfo();
            if (sellerInfo == null) {
                continue;
            }
            if (sellerInfo.getMessagePower() != null && sellerInfo.getMessagePower()) {
                try {
                    mobileMsgManager.sendMessage(sellerInfo.getPhoneNumber(), map.get("gameName").toString() + "-" +
                            map.get("region").toString() + "-" + map.get("server").toString() + "的商品价格修改为：" + unitPrice + "【5173游戏币商城】");
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    @Transactional
    public GoodsInfo modifyGoodsInfo(GoodsInfo goodsInfo, String state)
            throws SystemException {
        log.debug("修改商品信息");

        //增加商品修改的日志
        StringBuffer sb = new StringBuffer();

        if (goodsInfo == null) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsInfo.getCode(), ResponseCodes.EmptyGoodsInfo.getMessage());
        }
        if (goodsInfo.getId() == null) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsId.getCode(), ResponseCodes.EmptyGoodsId.getMessage());
        }
        GoodsInfo dbGoods = goodsInfoDBDAO.selectById((Long) (goodsInfo.getId()));
        sb.append("商品:" + dbGoods.getId() + "|" + dbGoods.getGameName() + "|" + dbGoods.getServer() + "|" + dbGoods.getRegion());
        if (null==goodsInfo.getMoneyName()) {
            throw new SystemException(
                    ResponseCodes.EmptyMoneyName.getCode(), ResponseCodes.EmptyMoneyName.getCode());
        }
        if (!StringUtils.equals(dbGoods.getMoneyName(), goodsInfo.getMoneyName())) {
            dbGoods.setMoneyName(goodsInfo.getMoneyName());
            sb.append(" 修改游戏币名称").append(dbGoods.getMoneyName()).append("-->").append(goodsInfo.getMoneyName());
        }

        if (StringUtils.isEmpty(goodsInfo.getTitle())) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsTitle.getCode(), ResponseCodes.EmptyGoodsTitle.getCode());
        }
        if (!StringUtils.equals(dbGoods.getTitle(), goodsInfo.getTitle())) {
            dbGoods.setTitle(goodsInfo.getTitle());
            sb.append(" 修改商品名称").append(dbGoods.getTitle()).append("-->").append(goodsInfo.getTitle());
        }
        if (goodsInfo.getUnitPrice() == null) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsPrice.getCode(), ResponseCodes.EmptyGoodsPrice.getMessage());
        }
        if (dbGoods.getUnitPrice() != goodsInfo.getUnitPrice()) {
            sb.append(" 修改商品单价").append(dbGoods.getUnitPrice()).append("-->").append(goodsInfo.getUnitPrice());

            dbGoods.setUnitPrice(goodsInfo.getUnitPrice());
            if (state.equals("1")) {
                Map<String, Object> _map = new HashMap<String, Object>();
                _map.put("gameName", goodsInfo.getGameName());
                _map.put("region", goodsInfo.getRegion());
                _map.put("server", goodsInfo.getServer());
                sendMsgByphone(goodsInfo.getUnitPrice(), _map);
            }
        }
//		if(StringUtils.isNotEmpty(goodsInfo.getImageUrls())){
//			dbGoods.setImageUrls(goodsInfo.getImageUrls());
//			sb.append(" 修改商品图片").append(dbGoods.getImageUrls()).append("-->").append(goodsInfo.getImageUrls());
//		}
        if (goodsInfo.getDeliveryTime() != null && goodsInfo.getDeliveryTime() != dbGoods.getDeliveryTime()) {
            dbGoods.setDeliveryTime(goodsInfo.getDeliveryTime());
            sb.append(" 修改商品发货时间").append(dbGoods.getDeliveryTime()).append("-->").append(goodsInfo.getDeliveryTime());
        }
        goodsInfo.setIsDeleted(false);
        dbGoods.setLastUpdateTime(new Date());
        goodsInfoDBDAO.update(dbGoods);
        // 保存redis
        goodsInfoRedisDAO.saveGoodsInfo(dbGoods);
        // 删除折扣信息
        discountInfoManager.deleteByGoodsId((Long) dbGoods.getId());
        // 保存新的折扣信息
        for (DiscountInfo discountInfo : goodsInfo.getDiscountList()) {
            discountInfo.setGoodsId((Long) goodsInfo.getId());
            discountInfoManager.addDiscount(discountInfo);
        }

        //logManager.add(ModuleType.GOODS, sb.toString(), CurrentUserContext.getUser());
        log.info(sb.toString());

        GoodsLogInfo logInfo = new GoodsLogInfo();
        logInfo.setLogType(LogType.GOODS_MODIFY);
        logInfo.setRemark(sb.toString());
        goodsLogManager.add(logInfo, dbGoods);

        return goodsInfo;
    }

    @Override
    @Transactional
    public void batchModifyPrice(GoodsInfo goodsInfo, String state)
            throws SystemException {
        log.debug("修改商品信息");

        //增加商品修改的日志
        StringBuffer sb = new StringBuffer();
        if (goodsInfo == null) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsInfo.getCode(), ResponseCodes.EmptyGoodsInfo.getMessage());
        }

        if (goodsInfo.getTitle() == null) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsTitle.getCode(), ResponseCodes.EmptyGoodsTitle.getCode());
        }
        if (goodsInfo.getUnitPrice() == null) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsPrice.getCode(), ResponseCodes.EmptyGoodsPrice.getMessage());
        }
        if (goodsInfo.getGameName() == null) {
            throw new SystemException(
                    ResponseCodes.EmptyGameName.getCode(), ResponseCodes.EmptyGameName.getMessage());
        }
        if (goodsInfo.getRegion() == null) {
            throw new SystemException(
                    ResponseCodes.EmptyRegion.getCode(), ResponseCodes.EmptyRegion.getMessage());
        }
        if (goodsInfo.getServer() == null) {
            throw new SystemException(
                    ResponseCodes.EmptyGameServer.getCode(), ResponseCodes.EmptyGameServer.getMessage());
        }
        if ("魔兽世界(国服)".equals(goodsInfo.getGameName().trim()) && StringUtils.isBlank(goodsInfo.getGameRace())) {
            throw new SystemException(
                    ResponseCodes.EmptyGameRace.getCode(), ResponseCodes.EmptyGameRace.getMessage());
        }
        /**  ZW_C_JB_00008_20170518_START ADD **/
        if (goodsInfo.getGoodsTypeName() == null) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsTypeName.getCode(), ResponseCodes.EmptyGoodsTypeName.getMessage());
        }
        /**  ZW_C_JB_00008_20170518_END **/

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("backGameName", goodsInfo.getGameName().trim());
        map.put("backRegion", goodsInfo.getRegion().trim());
        map.put("backServer", goodsInfo.getServer().trim());
        map.put("title", goodsInfo.getTitle().trim());
        map.put("goodsTypeName", goodsInfo.getGoodsTypeName().trim()); /**  ZW_C_JB_00008_20170518 ADD **/
//    	map.put("unitPrice", goodsInfo.getUnitPrice());  /**  ZW_C_JB_00008_20170518 DEL **/
//    	map.put("lastUpdateTime",new Date());    /**  ZW_C_JB_00008_20170518 DEL **/
        if (StringUtils.isNotEmpty(goodsInfo.getGameRace())) {
            map.put("backGameRace", goodsInfo.getGameRace().trim());
        }
        List<GoodsInfo> dbGoodsInfolist = goodsInfoDBDAO.selectByMap(map, "ID", false);
        if (CollectionUtils.isEmpty(dbGoodsInfolist)) {
            StringBuilder s = new StringBuilder();
            s.append(goodsInfo.getGameName()).append("/").append(goodsInfo.getRegion()).append("/")
                    .append(goodsInfo.getServer()).append("/").append(goodsInfo.getGameRace())
                    .append(",标题：").append(goodsInfo.getTitle())
                    .append(",单价：").append(goodsInfo.getUnitPrice());
            logger.info("没有找到这个商品记录,{}", s.toString());
            throw new SystemException(
                    ResponseCodes.EmptyGoodsInfo.getCode(), ResponseCodes.EmptyGoodsInfo.getMessage());
        }
        GoodsInfo dbGoodInfo = dbGoodsInfolist.get(0);
        sb.append("商品:" + dbGoodInfo.getId() + "|" + dbGoodInfo.getGameName() + "|" + dbGoodInfo.getServer() + "|" + dbGoodInfo.getRegion());

        if (goodsInfo.getMoneyName() == "close") {
            dbGoodInfo.setIsDeleted(true);
        } else {
            if (dbGoodInfo.getUnitPrice() != goodsInfo.getUnitPrice()) {
                if (state.equals("1")) {
                    Map<String, Object> _map = new HashMap<String, Object>();
                    _map.put("gameName", goodsInfo.getGameName());
                    _map.put("region", goodsInfo.getRegion());
                    _map.put("server", goodsInfo.getServer());
                    sendMsgByphone(goodsInfo.getUnitPrice(), _map);
                }
                if ("魔兽世界(国服)".equals(dbGoodInfo.getGameName().trim())
                        || "剑侠情缘Ⅲ".equals(dbGoodInfo.getGameName().trim())
                        || "疾风之刃".equals(dbGoodInfo.getGameName().trim())
                        || "天涯明月刀".equals(dbGoodInfo.getGameName().trim())) {
                    dbGoodInfo.setIsDeleted(false);
                    //sb.append(" 修改商品单价").append(dbGoodInfo.getUnitPrice().setScale(5, BigDecimal.ROUND_HALF_UP)).append("-->").append(goodsInfo.getUnitPrice().setScale(5, BigDecimal.ROUND_HALF_UP));
                }/*else{
					sb.append(" 修改商品单价").append(dbGoodInfo.getUnitPrice().setScale(4, BigDecimal.ROUND_HALF_UP)).append("-->").append(goodsInfo.getUnitPrice().setScale(4, BigDecimal.ROUND_HALF_UP));
				}*/
                sb.append(" 修改商品单价").append(dbGoodInfo.getUnitPrice()).append("-->").append(goodsInfo.getUnitPrice());
                dbGoodInfo.setUnitPrice(goodsInfo.getUnitPrice());
            }
        }
        dbGoodInfo.setLastUpdateTime(new Date());
        goodsInfoDBDAO.update(dbGoodInfo);
        // 保存redis
        goodsInfoRedisDAO.saveGoodsInfo(dbGoodInfo);
        //logManager.add(ModuleType.GOODS, sb.toString(), CurrentUserContext.getUser());
        log.info(sb.toString());

        GoodsLogInfo logInfo = new GoodsLogInfo();
        logInfo.setLogType(LogType.GOODS_MODIFY);
        logInfo.setRemark(sb.toString());
        goodsLogManager.add(logInfo, dbGoodInfo);
    }

    /**
     * 根据ID获取商品对象
     *
     * @param id
     * @return
     */
    @Override
    public GoodsInfo selectById(Long id) {
        return goodsInfoDBDAO.selectById(id);
    }

    /**
     * 批量启用商品
     *
     * @param ids
     * @throws com.wzitech.chaos.framework.server.common.exception.SystemException
     */
    @Override
    public void enableGoods(List<Long> ids) throws SystemException {
        for (Long id : ids) {
            enableGoods(id);
        }
    }

    @Override
    @Transactional
    public GoodsInfo enableGoods(Long id) throws SystemException {
        if (id == null) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsId.getCode(), ResponseCodes.EmptyGoodsId.getMessage());
        }

        GoodsInfo dbGoods = goodsInfoDBDAO.selectById(id);
        if (dbGoods == null) {
            return null;
        }
        //判断一个id为空，其他就是空
        if (StringUtils.isEmpty(dbGoods.getGameId())) {
            dbGoods = this.setGoodsGameId(dbGoods);
        }
        dbGoods.setLastUpdateTime(new Date());
        dbGoods.setIsDeleted(false);
        goodsInfoDBDAO.update(dbGoods);
        // 保存redis
        // 保存数据库商品记录
        goodsInfoRedisDAO.saveGoodsInfo(dbGoods);

        //logManager.add(ModuleType.GOODS, "启用商品ID为"+id+"的商品信息", CurrentUserContext.getUser());
        log.info("启用商品ID为" + id + "的商品信息");

        GoodsLogInfo logInfo = new GoodsLogInfo();
        logInfo.setLogType(LogType.GOODS_ENABLE);
        logInfo.setRemark("启用商品ID为" + id + "的商品信息");
        goodsLogManager.add(logInfo, dbGoods);

        return dbGoods;
    }

    /**
     * 批量禁用商品
     * @param ids
     * @throws SystemException
     */
    public void disableGoods(List<Long> ids) throws SystemException {
        for (Long id : ids) {
            disableGoods(id);
        }
    }

    @Override
    @Transactional
    public GoodsInfo disableGoods(Long id) throws SystemException {
        if (id == null) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsId.getCode(), ResponseCodes.EmptyGoodsId.getMessage());
        }

        GoodsInfo dbGoods = goodsInfoDBDAO.selectById(id);
        if (dbGoods == null) {
            return null;
        }

        dbGoods.setLastUpdateTime(new Date());
        dbGoods.setIsDeleted(true);
        goodsInfoDBDAO.update(dbGoods);

        // 删除redis
        // 禁用数据库商品记录
        goodsInfoRedisDAO.deleteGoods(dbGoods);

        //logManager.add(ModuleType.GOODS, "禁用商品ID为"+id+"的商品信息", CurrentUserContext.getUser());
        log.info("禁用商品ID为" + id + "的商品信息");

        GoodsLogInfo logInfo = new GoodsLogInfo();
        logInfo.setLogType(LogType.GOODS_DISABLE);
        logInfo.setRemark("禁用商品ID为" + id + "的商品信息");
        goodsLogManager.add(logInfo, dbGoods);

        return dbGoods;
    }

    @Override
    public GenericPage<GoodsInfo> queryGoodsInfo(Map<String, Object> queryMap, int limit, int start,
                                                 String sortBy, boolean isAsc) {
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "ID";
        }
        return goodsInfoDBDAO.selectByMap(queryMap, limit, start, sortBy, isAsc);
    }

    @Override
    public GoodsInfo getMaxSaleGoods(String gameName, String goodsTypeName, String region, String server, String gameRace, int goodsCat) {
        GoodsInfo goodsInfo = goodsInfoRedisDAO.getMaxSaleGoods(gameName, goodsTypeName, region, server, gameRace, goodsCat);
        if (goodsInfo == null) {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("backGameName", gameName);
            queryMap.put("backRegion", region);
            queryMap.put("backServer", server);
            queryMap.put("backGameRace", gameRace);
            queryMap.put("goodsCat", goodsCat);
            queryMap.put("goodsTypeName", goodsTypeName);
            queryMap.put("isDeleted", false);
            GenericPage<GoodsInfo> goodsResult = queryGoodsInfo(queryMap, 1, 0, "SALES", false);
            if (goodsResult.getData() != null) {
                goodsInfo = goodsResult.getData().get(0);
                goodsInfo = this.setGoodsGameId(goodsInfo);
                goodsInfoRedisDAO.saveGoodsInfo(goodsInfo);
                goodsInfoDBDAO.update(goodsInfo);
            }
        }
        return goodsInfo;
    }

    @Override
    @Transactional
    public void differPrice(Map<String, Object> paramMap) {
//		paramMap.put("isDeleted", false);
        goodsInfoDBDAO.updatePrice(paramMap);

        // 修改redis中商品价格
        List<GoodsInfo> goodList = goodsInfoDBDAO.selectByMap(paramMap, "ID", false);
        if (goodList != null && goodList.size() > 0) {
            for (GoodsInfo goodsInfo : goodList) {
                goodsInfoRedisDAO.saveGoodsInfo(goodsInfo);
                if (paramMap.get("state").equals("1")) {
                    Map<String, Object> _map = new HashMap<String, Object>();
                    _map.put("gameName", goodsInfo.getGameName());
                    _map.put("region", goodsInfo.getRegion());
                    _map.put("server", goodsInfo.getServer());
                    sendMsgByphone(goodsInfo.getUnitPrice(), _map);
                }
            }
        }

        StringBuilder sb = new StringBuilder("批量修改商品价格，");
        sb.append("差价:").append(paramMap.get("differPrice"));

        String gameName = (String) paramMap.get("gameName");
        String region = (String) paramMap.get("region");
        String server = (String) paramMap.get("server");
        if (StringUtils.isNotBlank(gameName)) {
            sb.append("，区服:").append(gameName).append("/").append(region).append("/").append(server);
        }

        String title = (String) paramMap.get("title");
        if (StringUtils.isNotBlank(title)) {
            sb.append("，标题中带：'").append(paramMap.get("title")).append("'的商品");
        }

        GoodsLogInfo logInfo = new GoodsLogInfo();
        logInfo.setLogType(LogType.GOODS_MODIFY);
        logInfo.setRemark(sb.toString());
        goodsLogManager.add(logInfo, null);
    }

    @Override
    public void mdPriceByExcel(byte[] file, String state) throws SystemException, IOException {
        // 读excel
        InputStream inp = new ByteArrayInputStream(file);
        HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));
        HSSFSheet sheet = wb.getSheetAt(0);

        if (sheet.getLastRowNum() <= 0) {
            throw new SystemException(ResponseCodes.EmptyUploadFile.getCode());
        }
        HSSFRow row = null;
        List<GoodsInfo> goodsInfos = new ArrayList<GoodsInfo>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            List<String> itemList = new ArrayList<String>();
            row = sheet.getRow(i);
            if (null == row) {
                continue;
            }
            String data = null;
            for (int j = 1; j < row.getLastCellNum(); j++) {
                data = getStringCellValue(row.getCell(j));
                itemList.add(data);
            }

            GoodsInfo goodsItem = new GoodsInfo();
            if (itemList.size() >= 1) {
                goodsItem.setGameName(itemList.get(0)); // 游戏名称
            }
            if (itemList.size() >= 2) {
                goodsItem.setTitle(itemList.get(1)); // 商品名称
            }
            if (itemList.size() >= 3) {
                goodsItem.setRegion(itemList.get(2)); // 游戏区
            }
            if (itemList.size() >= 4) {
                goodsItem.setServer(itemList.get(3)); // 游戏服
            }
            if (itemList.size() >= 5) {
                if (itemList.get(4) == null || itemList.get(4) == "") {
                    String gameName = goodsItem.getGameName().trim();
                    if ("魔兽世界(国服)".equals(gameName) || "地下城与勇士".equals(gameName)) {
                        goodsItem.setUnitPrice(new BigDecimal("0")); // 单价
                        goodsItem.setMoneyName("close");
                    } else {
                        continue;
                    }
                } else {
                    goodsItem.setUnitPrice(new BigDecimal(itemList.get(4))); // 单价
                }
            } else {
                if ("剑侠情缘Ⅲ".equals(goodsItem.getGameName().trim())
                        || "疾风之刃".equals(goodsItem.getGameName().trim())
                        || "天涯明月刀".equals(goodsItem.getGameName().trim())) {
                    goodsItem.setUnitPrice(new BigDecimal("0")); // 单价
                    goodsItem.setMoneyName("close");
                }
            }
            /**  ZW_C_JB_00008_20170518_START ADD **/
            if (itemList.size() >= 6) {
                if (StringUtils.isBlank(itemList.get(5))) {
                    continue;
                }
                goodsItem.setGoodsTypeName(itemList.get(5)); // 商品类型
            }
            /**  ZW_C_JB_00008_20170518_END **/

            if (itemList.size() >= 7) {
                if (itemList.get(6) != null && itemList.get(6) != "") {
                    goodsItem.setGameRace(itemList.get(6)); // 阵营
                }
            }

            goodsInfos.add(goodsItem);
        }
        for (GoodsInfo goodsItem : goodsInfos) {
            this.batchModifyPrice(goodsItem, state);
        }

    }

    private String getStringCellValue(HSSFCell cell) {
        if (cell == null) {
            return null;
        }
        String strCell = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }

    @Override
    public List<GoodsInfo> queryUnitPrice(String gameID, String regionID, String serverID, String raceID, String goodsTypeName,
                                          String orderBy, boolean isAsc) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("gameId", gameID);
        queryMap.put("regionId", regionID);
        queryMap.put("serverId", serverID);
        queryMap.put("raceId", raceID);
        queryMap.put("goodsTypeName", goodsTypeName);  /**  ZW_C_JB_00008_20170518 ADD **/
        queryMap.put("isDeleted", false);
        return goodsInfoDBDAO.selectByMap(queryMap, orderBy, isAsc);
    }

    @Override
    public void delGoods(List<Long> ids) throws SystemException {
        for (Long id : ids) {
            delGoods(id);
        }
    }

    @Override
    public void delGoods(Long id) throws SystemException {
        if (id == null) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsId.getCode(), ResponseCodes.EmptyGoodsId.getMessage());
        }

        GoodsInfo dbGoods = goodsInfoDBDAO.selectById(id);
        if (dbGoods == null) {
            return;
        }
        goodsInfoDBDAO.deleteById(id);
        goodsInfoRedisDAO.deleteGoods(dbGoods);

        GoodsLogInfo logInfo = new GoodsLogInfo();
        logInfo.setLogType(LogType.GOODS_REMOVE);
        logInfo.setRemark("删除商品ID为" + id + "的商品信息");
        goodsLogManager.add(logInfo, dbGoods);
    }

}
