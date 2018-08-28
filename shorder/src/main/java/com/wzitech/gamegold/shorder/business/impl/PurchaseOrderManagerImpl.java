package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.ShDeliveryTypeEnum;
import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import com.wzitech.gamegold.common.expection.NotEnoughRepertoryException;
import com.wzitech.gamegold.common.game.IGameInfoManager;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.dao.*;
import com.wzitech.gamegold.shorder.dto.SellerDTO;
import com.wzitech.gamegold.shorder.entity.*;
import com.wzitech.gamegold.shorder.utils.SevenBaoFund;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 收货采购单
 * Created by 335854 on 2016/3/29.
 */
@Component
public class PurchaseOrderManagerImpl extends AbstractBusinessObject implements IPurchaseOrderManager {
    //默认最小收货量
    private final long MinCount = 1000;
    @Autowired
    IPurchaseOrderDBDAO purchaseOrderDBDAO;
    @Autowired
    IGameAccountDBDAO gameAccountDBDAO;
    @Autowired
    IGameInfoManager gameInfoManager;
    @Autowired
    private IShGameConfigManager shGameConfigManager;
    @Autowired
    private IGoodsTypeManager goodsTypeManager;
    @Autowired
    private IPurchaserDataDao purchaserDataDao;
    @Autowired
    private ISystemConfigManager systemConfigManager;
    @Autowired
    private IGameAccountCountRedisDao gameAccountCountRedisDao;
    @Autowired
    private IDeliveryOrderLogDao orderLogDao;
    @Autowired
    private ISellerDTOdao sellerDTOdao;
    @Autowired
    SevenBaoFund sevenBaoFund;
    @Autowired
    private IDeliveryConfigManager deliveryConfigManager;
    @Autowired
    private IConfigManager configManager;
    @Autowired
    private ITradeManager tradeManager;

    /**
     * 通过上传采购单新增/修改采购单
     */
    @Override
    @Transactional
    public void addPurchaseOrderInUpload(List<GameAccount> gameAccountList, Integer deliveryType) throws Exception {
        //对上传的采购单进行处理
        Map<String, PurchaseOrder> map = new HashMap<String, PurchaseOrder>();//暂存处理对象
        Map<String, ShGameConfig> configMap = new HashMap<String, ShGameConfig>();//存游戏配置数据
        for (GameAccount gameAccount : gameAccountList) {
            //如果不是收货角色，则不进行处理采购数量和单价的操作
//            if (!gameAccount.getIsShRole()) {
//                continue;
//            }
//
//            //判断是否存在当前游戏
//            GameNameAndId gameData = gameInfoManager.getIdByProp(gameAccount.getGameName().trim(),
//                    gameAccount.getGameName().trim(), 1);
//            if (gameData == null) {
//                throw new SystemException(ResponseCodes.NoGame.getCode(), "游戏【" + gameAccount.getGameName() + "】不存在");
//            }
//
//            //判断是否存在当前游戏区
//            GameNameAndId regionData = gameInfoManager.getIdByProp(gameAccount.getGameName().trim(),
//                    gameAccount.getRegion().trim(), 2);
//            if (regionData == null) {
//                throw new SystemException(ResponseCodes.NoRegion.getCode(), "游戏区【" + gameAccount.getRegion() + "】不存在");
//            }
//
//            //判断是否存在当前游戏服
//            GameNameAndId serverData = gameInfoManager.getIdByProp(gameAccount.getGameName().trim(),
//                    gameAccount.getServer().trim(), 3);
//            if (serverData == null) {
//                throw new SystemException(ResponseCodes.NoServer.getCode(), "游戏服【" + gameAccount.getServer() + "】不存在");
//            }

            //获取单位
            String moneyName = "";
            ShGameConfig shGameConfig = configMap.get(gameAccount.getGameName());
            if (shGameConfig == null) {
                shGameConfig = shGameConfigManager.getConfigByGameName(gameAccount.getGameName(), ServicesContants.GOODS_TYPE_GOLD, true, null);
                configMap.put(gameAccount.getGameName(), shGameConfig);
            }
            if (shGameConfig != null) {
                moneyName = shGameConfig.getUnitName();
            }
            ;

            String key = gameAccount.getBuyerAccount() + "_" + gameAccount.getGameName() + "_" + gameAccount.getRegion()
                    + "_" + gameAccount.getServer() + "_" + gameAccount.getGameRace();
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setMoneyName(moneyName);
            if (map.containsKey(key)) {
                //集合中存在，则进行采购数量累计，以及获取最大采购单价
                purchaseOrder = map.get(key);
                //同区服下的采购数量进行累加
                purchaseOrder.setCount(purchaseOrder.getCount().longValue() + gameAccount.getCount().longValue());
                //同区服下的采购单价进行比较，取最大采购单价
                purchaseOrder.setPrice(gameAccount.getPrice().compareTo(purchaseOrder.getPrice()) > 0 ? gameAccount.getPrice() : purchaseOrder.getPrice());
                purchaseOrder.setMinCount(gameAccount.getMinCount().compareTo(purchaseOrder.getMinCount()) > 0 ? gameAccount.getMinCount() : purchaseOrder.getMinCount());
            } else {
                //集合中不存在则复制gameAccount中的值到purchaseOrder
                purchaseOrder.setGameName(gameAccount.getGameName());
                purchaseOrder.setRegion(gameAccount.getRegion());
                purchaseOrder.setServer(gameAccount.getServer());
                purchaseOrder.setGameRace(gameAccount.getGameRace());
                purchaseOrder.setBuyerAccount(gameAccount.getBuyerAccount());
                purchaseOrder.setBuyerUid(gameAccount.getBuyerUid());
                purchaseOrder.setCount(gameAccount.getCount());
                purchaseOrder.setMinCount(gameAccount.getMinCount());
                purchaseOrder.setPrice(gameAccount.getPrice());
                map.put(key, purchaseOrder);
            }
        }
        //todo  临时做法，需要扩展
        //目前游戏类目仅支持游戏币
        String goodsTypeName = "游戏币";
        //根据游戏币获取对应的id
        Long goodsTypeId = goodsTypeManager.queryGoodsTypeIdByName(goodsTypeName);
        if (goodsTypeId == null) {
            throw new SystemException(ResponseCodes.EmptyGoodsTypeId.getCode(), ResponseCodes.EmptyGoodsTypeId.getMessage());
        }
        SystemConfig systemConfig = sevenBaoFund.createFund();
        if (systemConfig == null || StringUtils.isBlank(systemConfig.getConfigValue()) || StringUtils.isBlank(systemConfig.getAvailableFundValue())) {
            throw new SystemException(ResponseCodes.ErrorGetSystemConfigFromZ7Bao.getCode(), ResponseCodes.ErrorGetSystemConfigFromZ7Bao.getMessage());
        }
        //获取收货商账号与数据 新增的都是当前登录的
        String loginAccount = gameAccountList.get(0).getBuyerAccount();
        if (StringUtils.isBlank(loginAccount)) {
            throw new SystemException(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
        }
        PurchaserData purchaserData = purchaserDataDao.selectUniqueByProp("loginAccount", loginAccount);
        if (null == purchaserData) {
            throw new SystemException(ResponseCodes.NoPurchaseData.getCode(), ResponseCodes.NoPurchaseData.getMessage());
        }
        //保存数据库，如果数据库中不存在则新增，如果存在则修改
        for (String key : map.keySet()) {
            PurchaseOrder purchaseOrder = map.get(key);

            //查找符合条件的数据看数据库中是否存在当前采购单
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("buyerAccount", purchaseOrder.getBuyerAccount());
            queryMap.put("gameName", purchaseOrder.getGameName());
            queryMap.put("region", purchaseOrder.getRegion());
            queryMap.put("server", purchaseOrder.getServer());
            queryMap.put("deliveryType", deliveryType);
            List<PurchaseOrder> purchaseOrderList = purchaseOrderDBDAO.queryPurchaseOrder(queryMap);

            if (CollectionUtils.isEmpty(purchaseOrderList)) {
                purchaseOrder.setDeliveryType(deliveryType);
                purchaseOrder.setIsOnline(false);
                purchaseOrder.setUpdateTime(new Date());
                purchaseOrder.setGoodsType(goodsTypeId);
                purchaseOrder.setGoodsTypeName(goodsTypeName);
                purchaseOrderDBDAO.insert(purchaseOrder);
            } else {
                purchaseOrder.setId(purchaseOrderList.get(0).getId());
                purchaseOrder.setUpdateTime(new Date());
                purchaseOrder.setPrice(purchaseOrder.getPrice());
                purchaseOrder.setMinCount(purchaseOrder.getMinCount());
                purchaseOrder.setGoodsType(goodsTypeId);
                purchaseOrder.setGoodsTypeName(goodsTypeName);
                purchaseOrderDBDAO.update(purchaseOrder);
            }
            BigDecimal availableAmount = purchaserData.getAvailableAmountZBao();
            if (null == availableAmount) {
                availableAmount = BigDecimal.ZERO;
            }
            //只有可用余额大于押金加上上线金额之间的走SQL语句，否则直接按照老流程下架
            if (availableAmount.compareTo(new BigDecimal(systemConfig.getConfigValue()).add(new BigDecimal(systemConfig.getAvailableFundValue()))) >= 0) {
                //数据库中去统计对应的该区服下所有收货角色的采购数量，并更新到采购单表中。采用这种方式，是因为上传的采购单中会出现数据库中不存在的账号角色，程序逻辑中统计则会忽略掉数据库中存在的但不存在采购单中的采购数量，造成采购数量有出入。
                if (deliveryType == ShDeliveryTypeEnum.Robot.getCode()) {
                    purchaseOrderDBDAO.updatePurchaseOrderCount(queryMap);
                } else {
                    queryMap.put("count", 0);
                    purchaseOrderDBDAO.updateManualPurchaseOrderCount(queryMap);
                }
            }
        }
    }

    /**
     * 分页查询收货单
     *
     * @param queryMap
     * @param limit
     * @param start
     * @param sortBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<PurchaseOrder> queryPurchaseOrder(Map<String, Object> queryMap, int limit, int start, String sortBy, boolean isAsc) {
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "id";
        }

        GenericPage<PurchaseOrder> genericPage = purchaseOrderDBDAO.selectByMap(queryMap, limit,
                start, sortBy, isAsc);
        return genericPage;
    }


    /**
     * 批量设置采购单的上下架
     *
     * @param ids
     * @param isOnline
     */
    @Override
    public String setPurchaseOrderOnline(List<Long> ids, Boolean isOnline, String buyerAccount) {
        List<PurchaseOrder> list = new ArrayList<PurchaseOrder>();
        Map<String, Object> queryMapPurchaseOrder = new HashMap<String, Object>();
        queryMapPurchaseOrder.put("buyerAccount", buyerAccount);
        String message = "";
        for (Long id : ids) {
            queryMapPurchaseOrder.put("id", id);
            PurchaseOrder purchaseOrder = purchaseOrderDBDAO.selectByIdAndBuyerAccount(queryMapPurchaseOrder);
            if (purchaseOrder == null) {
                throw new SystemException(ResponseCodes.EmptyShPurchaseOrder.getCode());
            }
            if (isOnline != null && isOnline && (purchaseOrder.getCount() == 0 || purchaseOrder.getCount() < purchaseOrder.getMinCount())) {
                message += purchaseOrder.getGameName() + "/" + purchaseOrder.getRegion() + "/" + purchaseOrder.getServer() + "，收货数量小于最小收货量，上架失败\r\n";
                purchaseOrder.setIsOnline(false);
            } else {
                purchaseOrder.setIsOnline(isOnline);
            }

            purchaseOrder.setUpdateTime(new Date());
            list.add(purchaseOrder);
        }
        purchaseOrderDBDAO.batchUpdate(list);
        if (StringUtils.isBlank(message)) {
            message = "上架成功";
        }
        return message;
    }

    /**
     * 全部上架或者下架
     *
     * @param paramMap
     */
    @Override
    public void onlineAll(Map<String, Object> paramMap) {
        purchaseOrderDBDAO.onlineAll(paramMap);
    }

    /**
     * 发布单上架
     */
    @Override
    public void online(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderDBDAO.selectById(id);
        //设置上架
        purchaseOrder.setIsOnline(true);
        purchaseOrder.setUpdateTime(new Date());
        purchaseOrderDBDAO.update(purchaseOrder);
    }

    /**
     * 发布单下架
     */
    @Override
    public void offline(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderDBDAO.selectById(id);
        //设置下架
        purchaseOrder.setIsOnline(false);
        purchaseOrder.setIsRobotDown(false);
        purchaseOrder.setUpdateTime(new Date());
        purchaseOrderDBDAO.update(purchaseOrder);
    }

    /**
     * 发布单下架
     */
    @Override
    public void oldOffline(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderDBDAO.selectById(id);
        //设置下架
        purchaseOrder.setIsOnline(false);
        purchaseOrder.setUpdateTime(new Date());
        purchaseOrderDBDAO.update(purchaseOrder);
    }

    /**
     * 自动上架
     */
    @Override
    public void grounding(String account, BigDecimal availableAmount) {
        //调用7bao配置保证金，可以金额接口
        SystemConfig systemConfig = sevenBaoFund.createFund();
        //判断充值可用金额是否大于配置可用金额
        if (availableAmount.compareTo(new BigDecimal(systemConfig.getAvailableFundValue())) >= 0) {
            purchaseOrderDBDAO.updateLoginAccount(account);
        }
    }

    /**
     * 修改当前采购单的采购单价和采购量
     *
     * @param id
     * @param price
     * @param count
     */
    @Override
    @Transactional
    public Boolean updatePurchaseOrderPriceAndCount(Long id, BigDecimal price, Long count, Map<String, Long>
            gameAccountMap) {
        //根据采购单id和采购方的5173账号查找采购单信息
        Map<String, Object> queryMapPurchaseOrder = new HashMap<String, Object>();
        queryMapPurchaseOrder.put("id", id);
        //queryMapPurchaseOrder.put("buyerAccount",buyerAccount);

        if (BigDecimal.ZERO.compareTo(price) >= 0) {
            throw new SystemException(ResponseCodes.ShUnitPriceMustGreaterThanZero.getCode());
        }
        if (count.longValue() < 0) {
            throw new SystemException(ResponseCodes.EmptyShCount.getCode());
        }
//        else if (count.longValue() % 1000 != 0) {
//            throw new SystemException(ResponseCodes.IntMultiple.getCode());
//        }

        PurchaseOrder purchaseOrder = purchaseOrderDBDAO.selectByIdForUpdate(id);
        if (purchaseOrder == null) {
            logger.error("该采购单不存在！");
            return false;
        }

        Long countAll = 0L;
        //更新当前采购方该区服下所有账号角色的价格
        if (gameAccountMap != null && gameAccountMap.size() > 0) {
            for (String key : gameAccountMap.keySet()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", Long.parseLong(key));
                map.put("price", price);
                map.put("count", gameAccountMap.get(key));

                if (gameAccountMap.get(key).longValue() < 0) {
                    throw new SystemException(ResponseCodes.EmptyShCount.getCode());
                }
//                else if (gameAccountMap.get(key).longValue() % 1000 != 0) {
//                    throw new SystemException(ResponseCodes.IntMultiple.getCode());
//                }
                gameAccountDBDAO.updateCountAndPriceById(map);

                countAll += gameAccountMap.get(key);
            }

            //判断采购单中总的采购数量是否跟账号角色中的采购数量一致
            if (count.longValue() == countAll.longValue()) {
                //更新采购单的价格和采购数量
                purchaseOrder.setPrice(price);
                purchaseOrder.setCount(countAll);
                purchaseOrder.setMinCount(1000L);
                purchaseOrder.setUpdateTime(new Date());
                purchaseOrderDBDAO.update(purchaseOrder);
            } else {
                logger.error("总的采购数量与账号中采购数量不一致");
                return false;
            }
        } else {
            //如果没有账号角色，或者没有传入账号角色的时候，只需要更新对应该采购单下的账号角色的单价
            //更新当前采购方该区服下所有账号角色的价格
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("buyerAccount", purchaseOrder.getBuyerAccount());
            queryMap.put("gameName", purchaseOrder.getGameName());
            queryMap.put("region", purchaseOrder.getRegion());
            queryMap.put("server", purchaseOrder.getServer());
            queryMap.put("gameRace", purchaseOrder.getGameRace());
            queryMap.put("price", price);
            gameAccountDBDAO.updateGameAcountPrice(queryMap);

            //更新采购单的价格和采购数量
            purchaseOrder.setPrice(price);
//            purchaseOrder.setCount(count);
            purchaseOrder.setMinCount(1000L);
            purchaseOrder.setUpdateTime(new Date());
            purchaseOrderDBDAO.update(purchaseOrder);
        }

        return true;
    }

    /**
     * 修改当前采购单的采购单价和采购量和最低采购数量
     *
     * @param id
     * @param price
     * @param count
     */
    @Override
    public Boolean updatePurchaseOrderPriceAndCountAndNum(Long id, BigDecimal price, Long count, Long minCount) {
        //根据采购单id和采购方的5173账号查找采购单信息
        Map<String, Object> queryMapPurchaseOrder = new HashMap<String, Object>();
        queryMapPurchaseOrder.put("id", id);
        //queryMapPurchaseOrder.put("buyerAccount",buyerAccount);

        if (count == null || count < 0) {
            throw new SystemException(ResponseCodes.ShGoldCount.getCode(),
                    ResponseCodes.ShGoldCount.getMessage());
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new SystemException(ResponseCodes.ShUnitPriceMustGreaterThanZero.getCode(),
                    ResponseCodes.ShUnitPriceMustGreaterThanZero.getMessage());
        }

        if (minCount == null || minCount < 0) {
            throw new SystemException(ResponseCodes.ShGoldCount.getCode(),
                    "最小收货量不能小于0");
        }

        //如果收货数量大于0且单价=0就返回
        if (count > 0 && price.compareTo(BigDecimal.ZERO) == 0) {
            throw new SystemException(ResponseCodes.ShUnitPriceMustGreaterThanZero.getCode(),
                    ResponseCodes.ShUnitPriceMustGreaterThanZero.getMessage());
        }

        if (count.longValue() < 0) {
            throw new SystemException(ResponseCodes.EmptyShCount.getCode());
        }

        PurchaseOrder purchaseOrder = purchaseOrderDBDAO.selectByIdForUpdate(id);
        if (purchaseOrder == null) {
            throw new SystemException(ResponseCodes.EmptyPurchaseOrder.getCode(),
                    ResponseCodes.EmptyPurchaseOrder.getMessage());
        }

        purchaseOrder.setCount(count);
        purchaseOrder.setPrice(price);
        purchaseOrder.setMinCount(minCount);

        //如果收货数量等于0,就设置为下架状态
        if (count == 0 || count < minCount) {
            purchaseOrder.setIsOnline(false);
        }

        purchaseOrderDBDAO.update(purchaseOrder);
        return true;
    }

    /**
     * 更新采购单和对应账号角色的采购数量
     *
     * @param buyerAccount
     * @param gameName
     * @param region
     * @param server
     * @param gameRace
     * @param gameAccount
     * @param roleName
     * @param count
     * @param status
     * @param isAdd
     * @return boolean 是否更新成功
     * ZW_C_JB_00004 jiyx 修改
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePurchaseOrderCount(String buyerAccount, String gameName, String region, String server, String gameRace, String gameAccount, String roleName, Long count, int status, Boolean isAdd, String orderId) {

        //更新当前采购方该区服下所有账号角色的采购数量
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("buyerAccount", buyerAccount);
        queryMap.put("gameName", gameName);
        queryMap.put("region", region);
        queryMap.put("server", server);
        queryMap.put("gameRace", gameRace);
//        queryMap.put("gameAccount", gameAccount);
//        queryMap.put("roleName", roleName);
//        queryMap.put("count", count);
//        queryMap.put("status", status);
        Long shCount = count;
        int countResult = 0;
        if (isAdd) {
            //读取redis 遍历 将原来的取出来
            //执行增加操作
            Map<Long, String> map = (Map<Long, String>) gameAccountCountRedisDao.selectByOrderId(orderId);
            logger.info("在订单号:" + orderId + "中配单查询配单角色扣除信息：{}", map);
            if (map == null || map.size() == 0 || count == 0) {
                queryMap.put("gameAccount", gameAccount);
                queryMap.put("roleName", roleName);
                queryMap.put("status", status);
                queryMap.put("count", count);
                countResult = gameAccountDBDAO.addGameAcountCount(queryMap);
            } else {
                for (Map.Entry<Long, String> m : map.entrySet()) {
                    String[] split = m.getValue().split("\\,\\!\\@\\;");
                    queryMap.put("status", split[0].equals(gameAccount) && split[1].equals(roleName) ? status : null);
                    if (count > Long.parseLong(split[2])) {
                        queryMap.put("gameAccount", split[0]);
                        queryMap.put("roleName", split[1]);
                        queryMap.put("count", Long.parseLong(split[2]));
                        countResult += gameAccountDBDAO.addGameAcountCount(queryMap);
                        count -= Long.parseLong(split[2]);
                    } else {
                        queryMap.put("gameAccount", split[0]);
                        queryMap.put("roleName", split[1]);
                        queryMap.put("count", count);
                        countResult += gameAccountDBDAO.addGameAcountCount(queryMap);
                        count = 0L;
//                        break;
                    }
                }
            }
        } else {
//            OrderLog orderLog = new OrderLog();
//            orderLog.setOrderId(orderId);
//            orderLog.setType(OrderLog.TYPE_INNER);
//            orderLog.setCreateTime(new Date());
            //处理之前的逻辑
            queryMap.put("gameAccount", gameAccount);
            queryMap.put("roleName", roleName);
            queryMap.put("count", count);
            queryMap.put("status", status);
            countResult = gameAccountDBDAO.reduceGameAcountCount(queryMap);
            //执行减去操作
            if (countResult == 0) {
                queryMap.put("lockMode", true);
                queryMap.put("gameAccount", null);
                queryMap.put("roleName", null);
                queryMap.put("count", null);
                queryMap.put("status", null);
                List<GameAccount> gameAccounts = gameAccountDBDAO.selectByMap(queryMap);
                Map<Long, String> gameCountMap = new HashMap<Long, String>();
                for (GameAccount g : gameAccounts) {
                    queryMap.put("gameAccount", g.getGameAccount());
                    queryMap.put("roleName", g.getRoleName());
                    queryMap.put("status", g.getGameAccount().equals(gameAccount) && g.getRoleName().equals(roleName) ? status : null);
//                    if (g.getCount() == 0) {
//                        continue;
//                    }
                    if (count > g.getCount()) {
                        count = count - g.getCount();
                        queryMap.put("count", g.getCount());
                        gameCountMap.put(g.getId(), g.getGameAccount() + ",!@;" + g.getRoleName() + ",!@;" + g.getCount());
                        countResult += gameAccountDBDAO.reduceGameAcountCount(queryMap);
                    } else {
                        queryMap.put("count", count);
                        gameCountMap.put(g.getId(), g.getGameAccount() + ",!@;" + g.getRoleName() + ",!@;" + count);
                        countResult += gameAccountDBDAO.reduceGameAcountCount(queryMap);
                        count = 0L;
//                        break;
                    }
                }
                if (count > 0) {
                    throw new NotEnoughRepertoryException();
                }
                logger.info("在订单号" + orderId + "中配单：{}", gameCountMap);
                //redis 存储 key:value 格式
                gameAccountCountRedisDao.save(orderId, gameCountMap);
            }
        }

        if (countResult == 0) return false;

        //如果更新数据，则调用采购单更新统计方法进行同步采购数量
        queryMap.put("count", shCount);
        logger.info("在订单号:" + orderId + "更新数据：{}", queryMap);
        purchaseOrderDBDAO.updatePurchaseOrderCount(queryMap);

        return true;
    }

    /**
     * 申诉单撤单仅更新角色状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePurchaseOrderCountForAppealOrder(String buyerAccount, String gameName, String region, String server, String gameRace,
                                                          String gameAccount, String roleName, int status) {
        Map<String, Object> queryMap = new HashMap<String, Object>(9);
        queryMap.put("buyerAccount", buyerAccount);
        queryMap.put("gameName", gameName);
        queryMap.put("region", region);
        queryMap.put("server", server);
        queryMap.put("gameRace", gameRace);
        queryMap.put("gameAccount", gameAccount);
        queryMap.put("roleName", roleName);
        queryMap.put("status", status);
        queryMap.put("count", 0);
        logger.info("申诉单撤单释放对应角色");
        gameAccountDBDAO.addGameAcountCount(queryMap);
        return true;
    }

    /**
     * 批量修改
     *
     * @param orderList
     * @return
     */
    @Transactional
    public boolean batchUpdate(List<PurchaseOrder> orderList) {
        purchaseOrderDBDAO.batchUpdate(orderList);
        return true;
    }

    /**
     * 分页查找采购单列表用以前台列表页展示
     *
     * @param paramMap
     * @param sortFields
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public GenericPage<PurchaseOrder> selectOrderList(Map<String, Object> paramMap, List<SortField> sortFields, int start, int pageSize) {
        return purchaseOrderDBDAO.selectOrderList(paramMap, sortFields, start, pageSize);
    }

    /**
     * 根据采购单id查找采购单以及采购商信息
     * ZW_C_JB_00004 mj
     *
     * @param id
     * @return
     */
    @Override
    public PurchaseOrder selectPurchaseOrderAndCgDataById(Long id) {
        String loginAccount = CurrentUserContext.getUserLoginAccount();
        if (id == null) {
            throw new SystemException(ResponseCodes.EmptyChId.getCode(),
                    ResponseCodes.EmptyChId.getMessage());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        List<PurchaseOrder> purchaseOrderList = purchaseOrderDBDAO.selectOrderList(map, null, 0, 1).getData();
        if (purchaseOrderList == null || purchaseOrderList.size() < 1) {
            throw new SystemException(ResponseCodes.EmptyPurchaseOrder.getCode(),
                    ResponseCodes.EmptyPurchaseOrder.getMessage());
        }
        PurchaseOrder purchaseOrder = purchaseOrderList.get(0);
        SystemConfig systemConfig = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.BALANCE_STOP_LINE.getKey());
        if (systemConfig == null) {
            throw new SystemException(ResponseCodes.EmptySystemConfig.getCode(),
                    ResponseCodes.EmptySystemConfig.getMessage());
        }
        if (StringUtils.isBlank(systemConfig.getConfigValue())) {
            throw new SystemException(ResponseCodes.NullConfigValue.getCode(),
                    ResponseCodes.NullConfigValue.getMessage());
        }
        //获取采购商限制余额最小值
        BigDecimal limitAmount = new BigDecimal(systemConfig.getConfigValue());

        if (StringUtils.isBlank(purchaseOrder.getBuyerAccount())) {
            throw new SystemException(ResponseCodes.EmptyPurchaseAccount.getCode(),
                    ResponseCodes.EmptyPurchaseAccount.getMessage());
        }
        if (purchaseOrder.getBuyerAccount().equals(loginAccount)) {
            throw new SystemException(ResponseCodes.CanNotSellToYourself.getCode(), ResponseCodes.CanNotSellToYourself.getMessage());
        }
        PurchaserData purchaserData = purchaserDataDao.selectByAccount(purchaseOrder.getBuyerAccount());
        if (purchaserData == null) {
            throw new SystemException(ResponseCodes.EmptyPurchaseOrder.getCode(),
                    ResponseCodes.EmptyPurchaseOrder.getMessage());
        }
        //获取可用金额
        BigDecimal availableAmount = purchaserData.getAvailableAmount();
        //获取7bao可用金额
        BigDecimal availableAmountZBao = purchaserData.getAvailableAmountZBao();
        //收货商可用余额为0或者不大于收货商余额限制则下架该采购单
        List<Long> ids = new ArrayList<Long>();
        SellerDTO byAccount = sellerDTOdao.findByAccount(purchaseOrder.getBuyerAccount());
        //新流程
        systemConfig = sevenBaoFund.createFund();
        if (systemConfig == null) {
            logger.info("可用收货金配置不能为空:{}", systemConfig);
            throw new SystemException(ResponseCodes.Configuration.getCode(), ResponseCodes.Configuration.getMessage());
        }
        String configValue = systemConfig.getConfigValue();
        String availableFundValue = systemConfig.getAvailableFundValue();
        if (byAccount.getIsNewFund() != null && byAccount.getIsNewFund() == true && byAccount.getisAgree() != null && byAccount.getisAgree() == true) {
            if (new BigDecimal(configValue).add(new BigDecimal(availableFundValue)).compareTo(availableAmountZBao) >= 0) {
                offline(purchaseOrder.getId());
                logger.info("7bao新流程下架采购单:{}", purchaseOrder);
                throw new SystemException(ResponseCodes.SellerWarningMoney.getCode(),
                        ResponseCodes.SellerWarningMoney.getMessage());
            }
        } else if (availableAmount == null || availableAmount.compareTo(BigDecimal.ZERO) < 1 || limitAmount.compareTo(BigDecimal.ZERO) < 1
                || availableAmount.compareTo(limitAmount) < 1) {
            oldOffline(purchaseOrder.getId());
            logger.info("下架采购单:{}", purchaseOrder);
            throw new SystemException(ResponseCodes.SellerWarningMoney.getCode(),
                    ResponseCodes.SellerWarningMoney.getMessage());
        }
        if (purchaseOrder.getPrice() == null || purchaseOrder.getPrice().compareTo(BigDecimal.ZERO) < 1) {
            throw new SystemException(ResponseCodes.EmptyUnitPrice.getCode(),
                    ResponseCodes.EmptyUnitPrice.getMessage());
        }

        //根据余额计算出最大收货数量
        //新出货流程和老出货流程判断
        Long count = null;
        if (byAccount.getIsNewFund() != null && byAccount.getisAgree() != null && byAccount.getIsNewFund() == true && byAccount.getisAgree() == true) {
            availableAmountZBao = availableAmountZBao.subtract(new BigDecimal(configValue)).divide(purchaseOrder.getPrice(), 0, BigDecimal.ROUND_DOWN);
            count = Long.parseLong(availableAmountZBao.toString());
        } else {
            availableAmount = availableAmount.subtract(limitAmount).divide(purchaseOrder.getPrice(), 0, BigDecimal.ROUND_DOWN);
            count = Long.parseLong(availableAmount.toString());
        }

//        if (purchaseOrder.getDeliveryType().intValue() == ShDeliveryTypeEnum.Robot.getCode()) {
//            //采购单的收货数量取1000的整数倍
//            long purchaseOrderCount = purchaseOrder.getCount();
//            purchaseOrderCount /= 1000;
//            purchaseOrderCount *= 1000;
//            purchaseOrder.setCount(purchaseOrderCount);
//            //机器收货最大收货量取1000的整数倍
//            count /= 1000;
//            count *= 1000;
//        }
        //比较余额算出的收货数量与采购单的最小收货数量，若最大收货数量小于最小收货数量则下架该采购单
        if (count.compareTo(purchaseOrder.getMinCount()) != -1) {
            //若余额算出的收货数量大于采购单的最大收货数量，则显示采购单的最大收货数量
            if (count.compareTo(purchaseOrder.getCount()) == -1) {
                purchaseOrder.setCount(count);
            }
        } else {
            logger.info("下架采购单:{}", purchaseOrder);
            offline(purchaseOrder.getId());
            throw new SystemException(ResponseCodes.EmptyPurchaseOrder.getCode(),
                    ResponseCodes.EmptyPurchaseOrder.getMessage());
        }
        return purchaseOrder;
    }

    /**
     * 获取采购商数据，并锁定
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public PurchaseOrder selectByIdForUpdate(Long id) {
        return purchaseOrderDBDAO.selectByIdForUpdate(id);
    }

    /**
     * 更新采购单采购数量
     *
     * @param buyerAccount
     * @param gameName
     * @param region
     * @param server
     * @param gameRace
     * @param count
     * @return
     */
    public boolean updatePurchaseOrderCount(String buyerAccount, String gameName, String region, String server, String gameRace, Long count) {
        //更新当前采购方该区服下所有账号角色的采购数量
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("buyerAccount", buyerAccount);
        queryMap.put("gameName", gameName);
        queryMap.put("region", region);
        queryMap.put("server", server);
        queryMap.put("gameRace", gameRace);
        queryMap.put("count", count);
        purchaseOrderDBDAO.updateManualPurchaseOrderCount(queryMap);
        return true;
    }


    /**
     * 获取交易信息
     *
     * @param gameName
     * @param goodsTypeId
     * @param deliveryTypeId
     * @return
     */
    @Override
    public PurchaseConfig getPurchaseConfig(String gameName, int goodsTypeId, int deliveryTypeId) {
        PurchaseConfig purchaseConfig = new PurchaseConfig();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodsTypeId", goodsTypeId + "");
        map.put("gameName", gameName);
        map.put("shMode", deliveryTypeId);
        List<Trade> tradeList = tradeManager.selectTradeByGameGoodsTypeAndShMode(map);
        if (!CollectionUtils.isEmpty(tradeList)) {
            List<DeliveryConfig> deliveryConfigList = new ArrayList<DeliveryConfig>();
            for (Trade trade : tradeList) {
                //游戏交易说明信息
                DeliveryConfig deliveryConfig = deliveryConfigManager.getDeliveryConfigByCondition(gameName, goodsTypeId, deliveryTypeId, new Long(trade.getId()).intValue());
                logger.info("获取交易信息,deliveryConfig:{}", deliveryConfig);
                if (deliveryConfig == null) {
                    continue;
                }
                deliveryConfigList.add(deliveryConfig);
            }
            purchaseConfig.setTradeList(tradeList);
            purchaseConfig.setDeliveryConfigList(deliveryConfigList);
        }
        //收货游戏配置
        ShGameConfig shGameConfig = shGameConfigManager.getConfigByGameName(gameName, ServicesContants.GOODS_TYPE_GOLD, true, null);
        logger.info("获取交易信息,shGameConfig:{}", shGameConfig);
        if (shGameConfig != null) {
            shGameConfig.setTradeTypeId(null);
            shGameConfig.setTradeType(null);
            purchaseConfig.setGameConfig(shGameConfig);
        }
        //交易地址信息列表
        List<Config> configList = configManager.getConfigByGameName(gameName);
        if (configList != null && configList.size() > 0) {
            logger.info("获取交易信息,configList:{}", configList);
            purchaseConfig.setConfigList(configList);
        }
        return purchaseConfig;
    }

    /**
     * 更新采购单采购数量
     *
     * @param id
     * @param count
     * @return
     */
    @Override
    public boolean updatePurchaseOrderCountById(Long id, Long count) {
        purchaseOrderDBDAO.updatePurchaseOrderCountById(id, count);
        return true;
    }
}
