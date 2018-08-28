package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.DeliveryTypeEnum;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.ShDeliveryTypeEnum;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.dao.*;
import com.wzitech.gamegold.shorder.entity.PurchaseGame;
import com.wzitech.gamegold.shorder.entity.PurchaserGameTrade;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import com.wzitech.gamegold.shorder.entity.Trade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 收货商游戏配置manager
 */
@Component
public class PurchaseGameManagerImpl implements IPurchaseGameManager {
    @Autowired
    private IGoodsTypeManager goodsTypeManager;
    @Autowired
    private ITradeManager tradeManager;
    @Autowired
    private IPurchaseGameDao purchaseGameDao;
    @Autowired
    private IPurchaseOrderDBDAO purchaseOrderDBDAO;
    @Autowired
    private IPurchaseOrderManager purchaseOrderManager;
    @Autowired
    private IShGameConfigDao shGameConfigDao;
    @Autowired
    private IShGameTradeDao shGameTradeDao;
    @Autowired
    private IPurchaserGameTradeManager purchaserGameTradeManager;
    @Autowired
    private IPurchaserGameTradeRedis purchaserGameTradeRedis;


    /**
     * 分页查询采购商游戏配置管理
     */
    @Override
    public GenericPage<PurchaseGame> queryListInPage(Map<String, Object> map, int start, int pageSize, String orderBy, boolean isAsc) {
        if (null == map) {
            map = new HashMap<String, Object>();
        }

        //检查分页参数
        if (pageSize < 1) {
            throw new IllegalArgumentException("分页pageSize参数必须大于1");
        }

        if (start < 0) {
            throw new IllegalArgumentException("分页startIndex参数必须大于0");
        }
        return purchaseGameDao.selectByMap(map, pageSize, start, orderBy, isAsc);
    }


    /**
     * 获取总表中的交易方式并置入用户个人游戏配置条目中
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PurchaseGame> getPublicTradeTypeForUpdateData(String loginAccount) {
        if (null == loginAccount) {
            throw new SystemException(ResponseCodes.InvalidAuthkey.getCode(),
                    ResponseCodes.InvalidAuthkey.getMessage());
        }
        Map<String, Object> loginAcc = new HashMap<String, Object>();
        loginAcc.put("purchaseAccount", loginAccount);
        //先将对应账户下所有的配置查询出来
        List<PurchaseGame> list = purchaseGameDao.selectByMap(loginAcc);
        return list;
    }

    /**
     * 获取收货模式获取收货配置
     *
     * @param loginAccount
     * @return
     */
    @Override
    public List<PurchaseGame> getGameTradeConfig(String loginAccount) {
        List<PurchaseGame> list = this.getPublicTradeTypeForUpdateData(loginAccount);
        //从收货商交易方式配置表中读取收货商的交易方式配置
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        for (PurchaseGame purchaseGame : list) {
            List<PurchaserGameTrade> purchaserGameTrades = purchaserGameTradeManager.selectByPurchaserId(purchaseGame.getId());
            purchaseGame.setConfigList(purchaserGameTrades);
            map.clear();
            map.put("goodsTypeId", purchaseGame.getGoodsTypeId() + "");
            map.put("gameName", purchaseGame.getGameName());
            map.put("shMode", DeliveryTypeEnum.Robot.getCode());
            List<Trade> robotList = tradeManager.selectTradeByGameGoodsTypeAndShMode(map);
            purchaseGame.setRobotList(robotList);
            map.put("shMode", DeliveryTypeEnum.Manual.getCode());
            List<Trade> manualList = tradeManager.selectTradeByGameGoodsTypeAndShMode(map);
            purchaseGame.setManualList(manualList);
            purchaseGame.setTradeTypeId(null);
            purchaseGame.setTradeTypeName(null);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPurchaseGame(PurchaseGame purchaseGame) {
        if (StringUtils.isBlank(purchaseGame.getPurchaseAccount())) {
            throw new SystemException(ResponseCodes.EmptyPurchaseAccount.getCode(),
                    ResponseCodes.EmptyPurchaseAccount.getMessage());
        }
        if (StringUtils.isBlank(purchaseGame.getGameName())) {
            throw new SystemException(ResponseCodes.EmptyGameName.getCode(),
                    ResponseCodes.EmptyGameName.getMessage());
        }
        if (purchaseGame.getGoodsTypeId() == null) {
            throw new SystemException(ResponseCodes.EmptyGoodsTypeId.getCode(),
                    ResponseCodes.EmptyGoodsTypeId.getMessage());
        }
        if (purchaseGame.getDeliveryTypeId() == null) {
            throw new SystemException(ResponseCodes.EmptyDeliveryTypeId.getCode(),
                    ResponseCodes.EmptyDeliveryTypeId.getMessage());
        }
        //todo  临时做法，需要扩展
        //目前游戏类目仅支持游戏币
        String goodsTypeName = "游戏币";
        //根据游戏币获取对应的id
        Long goodsTypeId = goodsTypeManager.queryGoodsTypeIdByName(goodsTypeName);
        if (goodsTypeId == null) {
            throw new SystemException(ResponseCodes.EmptyGoodsTypeId.getCode(), ResponseCodes.EmptyGoodsTypeId.getMessage());
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("purchaseAccount", purchaseGame.getPurchaseAccount());
        map.put("gameName", purchaseGame.getGameName());
        map.put("goodsTypeId", goodsTypeId);
        List<PurchaseGame> list = purchaseGameDao.selectByMap(map);
        if (list != null && list.size() > 0) {
            throw new SystemException(ResponseCodes.ExistPurchaserGameConfig.getCode(),
                    ResponseCodes.ExistPurchaserGameConfig.getMessage());
        }
        if (StringUtils.isBlank(purchaseGame.getTradeTypeId())) {
            throw new SystemException(ResponseCodes.EmptyTradeTypeId.getCode(),
                    ResponseCodes.EmptyTradeTypeId.getMessage());
        }
        //根据tradeId查询交易方式名称
//        String tradeName = tradeManager.getNamesByIds(purchaseGame.getTradeTypeId());
//        purchaseGame.setTradeTypeName(tradeName == null ? "" : tradeName);
        purchaseGame.setDeliveryTypeName(ShDeliveryTypeEnum.getNameByCode(new Integer(purchaseGame.getDeliveryTypeId())));
//        //根据goodsTypeId查询交易类目名称
//        String goodsTypeName = goodsTypeManager.getNameById(new Long(purchaseGame.getGoodsTypeId()));
        purchaseGame.setGoodsTypeName(goodsTypeName);
        purchaseGame.setGoodsTypeId((goodsTypeId).intValue());
        //保存新增收获商游戏配置
        purchaseGameDao.insert(purchaseGame);
        batchAddPurchaserTrade(purchaseGame);
    }

    /**
     * 修改收货商游戏配置
     *
     * @param map
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePurchaseGame(Map<String, String> map) {
        if (StringUtils.isBlank(map.get("id"))) {
            throw new SystemException(ResponseCodes.EmptyPurchaseAccount.getCode(),
                    ResponseCodes.EmptyPurchaseAccount.getMessage());
        }
        PurchaseGame entity = purchaseGameDao.selectById(new Long(map.get("id").trim()));
        if (entity == null) {
            throw new SystemException(ResponseCodes.NotAvailableConfig.getCode(),
                    ResponseCodes.NotAvailableConfig.getMessage());
        }
        if (StringUtils.isBlank(map.get("deliveryTypeId"))) {
            throw new SystemException(ResponseCodes.EmptyDeliveryTypeId.getCode(),
                    ResponseCodes.EmptyDeliveryTypeId.getMessage());
        }
        if (ShDeliveryTypeEnum.Manual.getCode() == new Integer(map.get("deliveryTypeId").trim()) ||
                ShDeliveryTypeEnum.Robot.getCode() == new Integer(map.get("deliveryTypeId").trim())) {
            //手工收货需要验证交易方式非空
            if (StringUtils.isBlank(map.get("tradeTypeId"))) {
                throw new SystemException(ResponseCodes.EmptyTradeTypeId.getCode(),
                        ResponseCodes.EmptyTradeTypeId.getMessage());
            }
            entity.setTradeTypeId(map.get("tradeTypeId"));
            //根据tradeId查询交易方式名称
//            String tradeName = tradeManager.getNamesByIds(map.get("tradeTypeId").trim());
//            entity.setTradeTypeName(tradeName == null ? "" : tradeName);
        }
        entity.setDeliveryTypeId(new Integer(map.get("deliveryTypeId")));
        //根据收货模式id查询收货名称
        entity.setDeliveryTypeName(ShDeliveryTypeEnum.getNameByCode(new Integer(map.get("deliveryTypeId").trim())));
        //先删除采购商配置的交易方式
        purchaserGameTradeManager.deleteByPurchaserId(entity.getId());
        List<PurchaserGameTrade> purchaserList = this.batchAddPurchaserTrade(entity);
        purchaseGameDao.update(entity);
        purchaserGameTradeRedis.delete(entity.getId());
        if (CollectionUtils.isNotEmpty(purchaserList)) {
            purchaserGameTradeRedis.add(entity.getId(), purchaserList);
        }

    }

    /**
     * 根据交易方式字符串插入交易方式配置表
     *
     * @param entity
     */
    @Transactional(rollbackFor = Exception.class)
    private List<PurchaserGameTrade> batchAddPurchaserTrade(PurchaseGame entity) {
        if (entity.getDeliveryTypeId() == ShDeliveryTypeEnum.Close.getCode()) {
            return null;
        }
        List<PurchaserGameTrade> list = new ArrayList<PurchaserGameTrade>();
        if (StringUtils.isNotBlank(entity.getTradeTypeId())) {
            List<String> ids = Arrays.asList(entity.getTradeTypeId().trim().split(","));
            for (String id : ids) {
                Long tradeId = Long.parseLong(id);
                Trade trade = tradeManager.selectById(tradeId);
                if (trade == null) {
                    continue;
                }
                PurchaserGameTrade purchaserGameTrade = new PurchaserGameTrade();
                purchaserGameTrade.setTradeTypeName(trade.getName());
                purchaserGameTrade.setTradeLogo(trade.getTradeLogo());
                purchaserGameTrade.setTradeTypeId(tradeId);
                purchaserGameTrade.setPurchaserId(entity.getId());
                list.add(purchaserGameTrade);
                purchaserGameTradeManager.addPurchaserGameTrade(purchaserGameTrade);
            }
        }
        return list;
    }

    /**
     * 删除收货商游戏配置
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePurchaseGame(Long id) {
        if (id == null) {
            throw new SystemException(ResponseCodes.EmptyPurchaseAccount.getCode(),
                    ResponseCodes.EmptyPurchaseAccount.getMessage());
        }
        PurchaseGame purchaseGame = purchaseGameDao.selectById(id);
        if (StringUtils.isBlank(purchaseGame.getPurchaseAccount())) {
            throw new SystemException(ResponseCodes.EmptyPurchaseAccount.getCode(),
                    ResponseCodes.EmptyPurchaseAccount.getMessage());
        }
        if (StringUtils.isBlank(purchaseGame.getGameName())) {
            throw new SystemException(ResponseCodes.EmptyGameName.getCode(),
                    ResponseCodes.EmptyGameName.getMessage());
        }
        if (purchaseGame.getDeliveryTypeId() == null) {
            throw new SystemException(ResponseCodes.EmptyDeliveryTypeId.getCode(),
                    ResponseCodes.EmptyDeliveryTypeId.getMessage());
        }
        if (purchaseGame.getGoodsTypeId() == null) {
            throw new SystemException(ResponseCodes.EmptyGoodsTypeId.getCode(),
                    ResponseCodes.EmptyGoodsTypeId.getMessage());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerAccount", purchaseGame.getPurchaseAccount());
        map.put("gameName", purchaseGame.getGameName());
        map.put("goodsType", purchaseGame.getGoodsTypeId());
        map.put("deliveryType", new Integer(purchaseGame.getDeliveryTypeId()));
        map.put("isOnline", false);
        //下架全部采购单
        purchaseOrderManager.onlineAll(map);
        //删除采购商配置的交易方式
        purchaserGameTradeManager.deleteByPurchaserId(id);
        purchaseGameDao.deleteById(id);
        purchaserGameTradeRedis.delete(id);
    }

    @Override
    public List selectPurchaseGameByGameNameAndGoodsTypeName(PurchaseGame purchaseGame) {
        return purchaseGameDao.selectPurchaseGameByGameNameAndGoodsTypeName(purchaseGame);
    }


    @Override
    public void update(ShGameConfig shGameConfig) {
        PurchaseGame purchaseGame = new PurchaseGame();
        purchaseGame.setGoodsTypeName(shGameConfig.getGoodsTypeName());
        purchaseGame.setGameName(shGameConfig.getGameName());
//        //获取当前提交上来的交易方式,取出和收货配置属性表相同的字段
//        List<PurchaseGame> list = purchaseGameDao.selectPurchaseGameByGameNameAndGoodsTypeName(purchaseGame);
//        String[] str = shGameConfig.getTradeTypeId().split(",");
//        String[] s = shGameConfig.getTradeType().split(",");
//        for (PurchaseGame purchasegame : list) {
//            String tradetypeId = "";
//            String tradetype = "";
//            /**********新增非空判断_ADD_20170607_新增最低购买金额_START*****************/
//            if (StringUtils.isBlank(purchasegame.getTradeTypeId()) && StringUtils.isBlank(purchasegame.getTradeTypeName())) {
//                continue;
//            }
//            /**********新增非空判断_ADD_20170607_新增最低购买金额_END*****************/
//            String[] st = purchasegame.getTradeTypeId().split(",");
//            String[] ss = purchasegame.getTradeTypeName().split(",");
//            for (int i = 0; i < str.length; i++) {
//                for (int j = 0; j < st.length; j++) {
//                    if (s[i].equals(ss[j])) {
//                        tradetypeId = tradetypeId + str[i] + ",";
//                        tradetype = tradetype + s[i] + ",";
//                        break;
//                    }
//                }
//            }
//            if (StringUtils.isBlank(tradetypeId)) {
//                deletePurchaseGame(purchasegame.getId());
//                continue;
//            } else {
//                purchasegame.setTradeTypeId(tradetypeId.substring(0, tradetypeId.length() - 1));
//            }
//
//            if (StringUtils.isBlank(tradetype)) {
//                deletePurchaseGame(purchasegame.getId());
//                continue;
//            } else {
//                purchasegame.setTradeTypeName(tradetype.substring(0, tradetype.length() - 1));
//            }
//            purchaseGameDao.updatePurchaseGameByGameNameAndGoodsTypeName(purchasegame);
//        }
        //当一个游戏对应的类目不支持机器收货时，关联收货商游戏属性配置表将收货方式为机器收货修改为暂停收货
        if (!shGameConfig.getEnableRobot()) {
            purchaseGameDao.updatePurchaseGameByEnrobot(purchaseGame);
        }
    }

    /**
     * 给旧的收货商配置增加交易方式标识(拍卖交易上线时调用)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTradeLogoData() {
        purchaserGameTradeRedis.deleteAll();
        List<PurchaseGame> purchaseGames = purchaseGameDao.selectAll();
        if (CollectionUtils.isEmpty(purchaseGames)) {
            return;
        }
        List<PurchaserGameTrade> list = new ArrayList<PurchaserGameTrade>();
        for (PurchaseGame purchase : purchaseGames) {
            if (StringUtils.isBlank(purchase.getTradeTypeId())) {
                continue;
            }
            String[] tradeTypeIds = purchase.getTradeTypeId().split(",");
            List<String> tradeList = Arrays.asList(tradeTypeIds);
            for (String id : tradeList) {
                Long tradeId = Long.parseLong(id);
                Trade trade = tradeManager.selectById(tradeId);
                if (trade == null) {
                    continue;
                }
                PurchaserGameTrade purchaserGame = new PurchaserGameTrade();
                purchaserGame.setTradeTypeName(trade.getName());
                purchaserGame.setTradeLogo(trade.getTradeLogo());
                purchaserGame.setPurchaserId(purchase.getId());
                purchaserGame.setTradeTypeId(tradeId);
                list.add(purchaserGame);
            }
        }
        purchaserGameTradeManager.batchInsert(list);
    }

    @Override
    public PurchaseGame selectByPurchaseAccount(String purchaseAccount, String gameName, String goodsTypeName) {
        return purchaseGameDao.selectByPurchaseAccount(purchaseAccount, gameName, goodsTypeName);
    }
}
