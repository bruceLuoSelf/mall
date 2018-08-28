package com.wzitech.gamegold.order.business.impl;

import com.google.common.collect.Lists;
import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.common.enums.*;
import com.wzitech.gamegold.common.log.entity.OrderLogInfo;
import com.wzitech.gamegold.order.business.*;
import com.wzitech.gamegold.order.dao.IConfigPowerDBDAO;
import com.wzitech.gamegold.order.dao.IConfigResultInfoDBDAO;
import com.wzitech.gamegold.order.dao.IOrderConfigLockRedisDAO;
import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
import com.wzitech.gamegold.order.entity.ConfigPowerEO;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.business.ISellerSettingManager;
import com.wzitech.gamegold.repository.dao.IRepositoryDBDAO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 自动配单管理实现类
 */
@Component
public class AutoConfigManagerImpl extends AbstractBusinessObject implements IAutoConfigManager {
    protected static final Log log = LogFactory.getLog(AutoConfigManagerImpl.class);

    @Autowired
    IConfigPowerDBDAO configPowerDBDAO;

    @Autowired
    IRepositoryDBDAO repositoryDBDAO;

    @Autowired
    IOrderInfoDBDAO orderInfoDBDAO;

    @Autowired
    IConfigResultInfoDBDAO configResultInfoDBDAO;

    @Autowired
    IOrderConfigLockRedisDAO orderConfigLockRedisDAO;

    @Autowired
    IRepositoryManager repositoryManager;

    @Autowired
    IOrderLogManager orderLogManager;

    @Autowired
    IOrderConfigManager orderConfigManager;

    @Autowired
    IAutoPayManager autoPayManager;

    @Autowired
    IArrangeTraderManager arrangeTraderManager;

    @Autowired
    ISellerSettingManager sellerSettingManager;

    @Autowired
    IGameConfigManager gameConfigManager;

    @Autowired
    ICommissionManager commissionManager;

    //自动配单
    @Override
    @Transactional
    public void autoConfigOrder(OrderInfoEO orderInfo) {
        if (orderInfo == null) {
            throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(), ResponseCodes.EmptyOrderInfo.getMessage());
        }
        //如果是人工操作，直接退出
        if (orderInfo.getManualOperation()) {
            return;
        }
        //验证是否满足自动配单要求
        Boolean result = confirmAutoConfigPower(orderInfo);
        //自动配单开始,否则订单转人工操作
        if (result != null && result) {
            try {
                autoConfiging(orderInfo);
            } catch (SystemException e) {
                if (ResponseCodes.OrderIsEditor.getCode().equals(e.getErrorCode())) {
                    // 订单已在配置中的，直接抛异常，避免多次执行
                    throw e;
                } else {
                    // 其他错误转手动配单
                    orderInfo = orderInfoDBDAO.selectById(orderInfo.getId());
                    orderInfo.setManualOperation(true);
                    orderInfoDBDAO.update(orderInfo);
                    logger.error("转手动配单,orderId:{},exception:{}", orderInfo.getOrderId(), e);
                }
            } catch (Exception e) {
                orderInfo = orderInfoDBDAO.selectById(orderInfo.getId());
                orderInfo.setManualOperation(true);
                orderInfoDBDAO.update(orderInfo);
                logger.error("转手动配单,orderId:{},exception:{}", orderInfo.getOrderId(), e);
            }
        } else {
            orderInfo.setManualOperation(true);
            orderInfoDBDAO.update(orderInfo);
        }
    }

    //验证是否满足自动配单要求
    public Boolean confirmAutoConfigPower(OrderInfoEO orderInfo) {
        /*// 如果是栏目1的商品，必须是自动配单
        if (orderInfo.getGoodsCat() != null) {
            if (orderInfo.getGoodsCat() == 1)
                return true;
        }*/

        String gameName = orderInfo.getGameName();
        ConfigPowerEO configPowerInfo = configPowerDBDAO.getByGameName(gameName, orderInfo.getGoodsTypeName());
        if (orderInfo.getTradeType() == null) {
            // 订单没有设置交易方式的，先更新订单交易方式
            if (configPowerInfo != null) {
                if (configPowerInfo.getTradeType() == TradeType.Divided.getCode()) {// 邮寄交易
                    orderInfo.setTradeType(TradeType.Divided.getCode());
                } else if (configPowerInfo.getTradeType() == TradeType.UnionTrading.getCode()) {// 公会交易
                    orderInfo.setTradeType(TradeType.UnionTrading.getCode());
                } else { // 当面交易
                    orderInfo.setTradeType(TradeType.NoDivid.getCode());
                }
            } else {
                orderInfo.setTradeType(TradeType.NoDivid.getCode()); // 默认当面交易
            }
            orderInfoDBDAO.update(orderInfo);
        }

        if (configPowerInfo != null && configPowerInfo.getConfigPower() == false) {
            return false;
        }
        if (orderInfo.getGoldCount() == null || orderInfo.getGoldCount() <= 0) {
            throw new SystemException(ResponseCodes.EmptyGoldCount.getCode(), ResponseCodes.EmptyGoldCount.getMessage());
        }
        if (orderInfo.getGoldCount() > configPowerInfo.getConfigMaxCount()) {
            return false;
        }
        return true;
    }

    //自动配单
    private void autoConfiging(OrderInfoEO orderInfo) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("backGameName", orderInfo.getGameName());
        queryMap.put("backRegion", orderInfo.getRegion());
        queryMap.put("backServer", orderInfo.getServer());
        queryMap.put("gameRace", orderInfo.getGameRace());
        queryMap.put("orderUnitPrice", orderInfo.getUnitPrice());
        queryMap.put("sellableCount", orderInfo.getGoldCount());
        queryMap.put("isOnline", true); // 只显示卖家在线的库存
        queryMap.put("goodsTypeName", orderInfo.getGoodsTypeName());
        //queryMap.put("sellableCountMax", orderInfo.getGoldCount() + 500);

        // 卖家店铺商品，只配指定卖家的库存
        if (orderInfo.getGoodsCat() == GoodsCat.Cat3.getCode()) {
            if (StringUtils.isNotBlank(orderInfo.getSellerLoginAccount())) {
                queryMap.put("loginAccount", orderInfo.getSellerLoginAccount());
            } else {
                orderInfo.setGoodsCat(GoodsCat.Cat1.getCode());
                orderInfoDBDAO.update(orderInfo);
            }
        }

        List<SortField> sortFields = Lists.newArrayList();
        sortFields.add(new SortField("UNIT_PRICE", SortField.ASC));
        sortFields.add(new SortField("SELLABLE_COUNT", SortField.ASC));
        List<RepositoryInfo> repositoryLists = repositoryManager.queryRepository(queryMap, sortFields);

        if (!CollectionUtils.isEmpty(repositoryLists)) {
            // 删除所有担保和人工库存记录,但不删除开通小助手的卖家库存
            int max_size = repositoryLists.size();
            for (int i = max_size - 1; i >= 0; i--) {
                SellerInfo seller = repositoryLists.get(i).getSellerInfo();
                if (seller != null) {
                    if (seller.getIsShielded() != true) {
                        if (seller.getIsHelper() == null || seller.getIsHelper() != true) {
                            repositoryLists.remove(i);
                        }
                    }

                    /*if (seller.getIsShielded() != true) {
                        repositoryLists.remove(i);
                    } else if (seller.getManualStatus() == true) {
                        repositoryLists.remove(i);
                    }*/
                }
            }

            /*//对金币数判断，是否大于购买量
            int goldCount = orderInfo.getGoldCount();
            int repGoldCount = 0;
            for (RepositoryInfo repositoryInfo : repositoryLists) {
                repGoldCount += repositoryInfo.getSellableCount();
            }
            if (repGoldCount < goldCount) {
                repositoryLists = null;
            }*/
        }
        /*if (CollectionUtils.isEmpty(repositoryLists)){
            orderInfo.setManualOperation(true);
            orderInfoDBDAO.update(orderInfo);
            return;
        }*/

        if (CollectionUtils.isEmpty(repositoryLists)) {
            //queryMap.remove("sellableCountMax");
            queryMap.put("sellableCount", 500);
            sortFields = Lists.newArrayList();
            sortFields.add(new SortField("UNIT_PRICE", SortField.ASC));
            sortFields.add(new SortField("SELLABLE_COUNT", SortField.DESC));
            repositoryLists = repositoryManager.queryRepository(queryMap, sortFields);

            // 删除所有担保和人工库存记录,但不删除开通小助手的卖家库存
            int max_size = repositoryLists.size();
            for (int i = max_size - 1; i >= 0; i--) {
                SellerInfo seller = repositoryLists.get(i).getSellerInfo();
                if (seller != null) {
                    if (seller.getIsShielded() != true) {
                        if (seller.getIsHelper() == null || seller.getIsHelper() != true) {
                            repositoryLists.remove(i);
                        }
                    }
                    /*if (seller.getManualStatus() == true) {
                        repositoryLists.remove(i);
                    }*/
                }
            }

            if (CollectionUtils.isEmpty(repositoryLists)) {
                orderInfo.setManualOperation(true);
                orderInfoDBDAO.update(orderInfo);
                return;
            }

            /*List<RepositoryInfo> repositoryLists1 = new ArrayList<RepositoryInfo>();
            max_size = repositoryLists.size();
            for (int i = max_size - 1; i >= 0; i--) {
                SellerInfo seller = repositoryLists.get(i).getSellerInfo();
                if (seller != null) {
                    if (seller.getIsShielded() != true) {
                        repositoryLists1.add(repositoryLists.get(i));
                        repositoryLists.remove(i);
                    }
                }
            }

            if (!CollectionUtils.isEmpty(repositoryLists1)) {
                //Collections.reverse(repositoryLists1);
                repositoryLists.addAll(repositoryLists1);
            }*/
        }

        //对金币数判断，是否大于购买量
        long goldCount = orderInfo.getGoldCount();
        long repGoldCount = 0;
        for (RepositoryInfo repositoryInfo : repositoryLists) {
            repGoldCount += repositoryInfo.getSellableCount();
        }
        if (repGoldCount < goldCount) {
            orderInfo.setManualOperation(true);
            orderInfoDBDAO.update(orderInfo);
            return;
        }

        //根据购买量，确定配置单数的要求。
        int limitNum;
        if (goldCount > 12000) {
            limitNum = 6;
        } else if (goldCount > 8000) {
            limitNum = 5;
        } else if (goldCount > 4000) {
            limitNum = 4;
        } else {
            limitNum = 3;
        }
        long needGoldCount = goldCount;//需要金币数
        int configTimes = 0;

        //得到配置结果
        List<ConfigResultInfoEO> configInfoList = new ArrayList<ConfigResultInfoEO>();
        List<RepositoryInfo> repositorys = new ArrayList<RepositoryInfo>();

        //配单超过最多单数限制，或者配单完成，退出循环
        for (RepositoryInfo repositoryInfo : repositoryLists) {
            //库存数大于还需的金币数
            if (repositoryInfo.getSellableCount() >= needGoldCount) {
                ConfigResultInfoEO configResultInfo = new ConfigResultInfoEO();
                configResultInfo.setRepositoryId(repositoryInfo.getId());
                //配置还需的数量
                configResultInfo.setConfigGoldCount(needGoldCount);
                configResultInfo.setGameName(repositoryInfo.getGameName());
                configResultInfo.setRegion(repositoryInfo.getRegion());
                configResultInfo.setServer(repositoryInfo.getServer());
                configResultInfo.setGameId(repositoryInfo.getGameId());
                configResultInfo.setRegionId(repositoryInfo.getGameRegionId());
                configResultInfo.setServerId(repositoryInfo.getGameServerId());
                configResultInfo.setRaceId(repositoryInfo.getGameRaceId());
                configResultInfo.setGameRace(repositoryInfo.getGameRace());
                configInfoList.add(configResultInfo);
                repositorys.add(repositoryInfo);
                configTimes++;
                needGoldCount = 0;
                break;
            } else {
                //库存数小于还需的金币数
                ConfigResultInfoEO configResultInfo = new ConfigResultInfoEO();
                configResultInfo.setRepositoryId(repositoryInfo.getId());
                //全部库存的金币数全部配置
                configResultInfo.setConfigGoldCount(repositoryInfo.getSellableCount());
                configResultInfo.setGameName(repositoryInfo.getGameName());
                configResultInfo.setRegion(repositoryInfo.getRegion());
                configResultInfo.setServer(repositoryInfo.getServer());
                configResultInfo.setGameId(repositoryInfo.getGameId());
                configResultInfo.setRegionId(repositoryInfo.getGameRegionId());
                configResultInfo.setServerId(repositoryInfo.getGameServerId());
                configResultInfo.setRaceId(repositoryInfo.getGameRaceId());
                configResultInfo.setGameRace(repositoryInfo.getGameRace());
                configInfoList.add(configResultInfo);
                repositorys.add(repositoryInfo);
                configTimes++;
                needGoldCount -= repositoryInfo.getSellableCount();
            }

            if (configTimes >= limitNum || needGoldCount <= 0) {
                break;
            }
        }

        String gameName = orderInfo.getGameName().trim();
        if (!"地下城与勇士".equals(gameName)) {

            // 配出超过1单的，直接转人工配单
            if (configInfoList.size() > 1) {
                orderInfo.setManualOperation(true);
                orderInfoDBDAO.update(orderInfo);
            }
        }



        //判断是否配单完成
        if (needGoldCount > 0) {
            orderInfo.setManualOperation(true);
            orderInfoDBDAO.update(orderInfo);
        } else {
            if (configInfoList.size() != 0) {
                boolean success = true;
                if (configInfoList.size() > 1) {
                    // 有多单的情况下判断配单数量是否有小于数量100的，有的话转人工配单
                    Iterator<ConfigResultInfoEO> iterator = configInfoList.iterator();
                    while (iterator.hasNext()) {
                        ConfigResultInfoEO result = iterator.next();
                        if (result.getConfigGoldCount() <= 100) {
                            orderInfo.setManualOperation(true);
                            orderInfoDBDAO.update(orderInfo);
                            success = false;
                            break;
                        }
                    }
                }

                if (success) {
                    //处理配置结果
                    handleResult(orderInfo, repositorys, configInfoList);
                }
            }
        }
    }

    //保存配置结果
    @Transactional(propagation = Propagation.NESTED)
    public void handleResult(OrderInfoEO orderInfo, List<RepositoryInfo> repositorys, List<ConfigResultInfoEO> configInfoList) {
        if (repositorys == null) {
            throw new SystemException(ResponseCodes.EmptyRepositoryList.getCode(), ResponseCodes.EmptyRepositoryList.getMessage());
        }
        //锁定子订单
        Boolean locked = orderConfigLockRedisDAO.lock(String.valueOf(orderInfo.getOrderId()));
        if (!locked) {
            throw new SystemException(ResponseCodes.OrderIsEditor.getCode(), ResponseCodes.OrderIsEditor.getMessage());
        }
        try {
            //为自动配单增加一个用户
            UserInfoEO user = new UserInfoEO();
            user.setId(-1L);
            user.setLoginAccount("autoConfig");
            user.setUserType(UserType.System.getCode());
            CurrentUserContext.setUser(user);

            //配置的库存游戏币数量总和
            Map<Long, ConfigResultInfoEO> configMap = new HashMap<Long, ConfigResultInfoEO>();
            long countGold = 0;
            for (ConfigResultInfoEO config : configInfoList) {
                countGold += config.getConfigGoldCount();
                configMap.put(config.getRepositoryId(), config);
                for (RepositoryInfo repInfo : repositorys) {
                    if (repInfo.getId() == config.getRepositoryId()) {
                        if (repInfo.getSellableCount().longValue() < config.getConfigGoldCount().longValue()) {
                            throw new SystemException(ResponseCodes.RepositoryGoldLessConfigGold.getCode(), ResponseCodes.RepositoryGoldLessConfigGold.getMessage());
                        }
                        break;
                    }
                }
            }

            if (countGold != orderInfo.getGoldCount().longValue()) {
                throw new SystemException(ResponseCodes.ConfigGoldNotEqOrderGold.getCode(), ResponseCodes.ConfigGoldNotEqOrderGold.getMessage());
            }

            // 查询出之前对订单配置的库存信息，并修改之前的库存信息
            List<ConfigResultInfoEO> oldConfigs = configResultInfoDBDAO.selectByOrderId(orderInfo.getOrderId());
            if (!CollectionUtils.isEmpty(oldConfigs)) {
                for (ConfigResultInfoEO config : oldConfigs) {
                    RepositoryInfo repositoryInfo = config.getRepositoryInfo();
                    if (repositoryInfo != null) {
                        /*repositoryInfo.setGoldCount(repositoryInfo.getGoldCount() + config.getConfigGoldCount());
                        repositoryInfo.setSellableCount(repositoryInfo.getSellableCount() + config.getConfigGoldCount());
                        repositoryManager.modifyRepository(repositoryInfo);*/

                        repositoryManager.incrRepositoryCount(repositoryInfo, config.getConfigGoldCount().longValue(), orderInfo.getOrderId());
                    }
                }

                // 删除之前对订单配置过的库存信息
                configResultInfoDBDAO.deleteByOrderId(orderInfo.getOrderId());
            }

            // 获取佣金比率
            BigDecimal commissionBase = commissionManager.getCommission(orderInfo.getGameName(), orderInfo.getSellerLoginAccount(),
                    orderInfo.getGoodsCat(), orderInfo.getGoodsTypeName());

            // 红包平均值，将红包金额平分到多个子订单
            double averageRedEnvelopeVal = 0;
            // 最后一笔子订单的红包金额，等于总的红包金额-前面几笔子订单平分的红包之和
            double lastRedEnvelopeVal = 0;
            // 判断是否有使用红包
            if (orderInfo.getRedEnvelope() != null && orderInfo.getRedEnvelope() != 0) {
                BigDecimal amount = new BigDecimal(orderInfo.getRedEnvelope().toString());
                BigDecimal count = new BigDecimal(repositorys.size());
                averageRedEnvelopeVal = amount.divide(count, 2, RoundingMode.HALF_UP).doubleValue();
                lastRedEnvelopeVal = orderInfo.getRedEnvelope() - averageRedEnvelopeVal * (repositorys.size() - 1);
            }

            // 保存订单配置结果对象
            for (int index = 0, len = repositorys.size(); index < len; index++) {
                RepositoryInfo repositoryInfo = repositorys.get(index);
                if (repositoryInfo == null) {
                    throw new SystemException(ResponseCodes.EmptyRepository.getCode(), ResponseCodes.EmptyRepository.getMessage());
                }
                if (repositoryInfo.getId() == null) {
                    throw new SystemException(ResponseCodes.EmptyRepositoryId.getCode(), ResponseCodes.EmptyRepositoryId.getMessage());
                }
                ConfigResultInfoEO configResult = configMap.get(repositoryInfo.getId());
                configResult.setOrderInfoEO(orderInfo);
                configResult.setAccountUid(repositoryInfo.getAccountUid());
                configResult.setLoginAccount(repositoryInfo.getLoginAccount());
                configResult.setCreateTime(new Date());
                configResult.setLastUpdateTime(new Date());
                configResult.setIsDeleted(false);
                configResult.setOrderId(orderInfo.getOrderId());
                configResult.setRepositoryId(repositoryInfo.getId());
                configResult.setServicerId(orderInfo.getServicerId());
                configResult.setTradeType(orderInfo.getTradeType());
                configResult.setOrderUnitPrice(orderInfo.getUnitPrice());
                configResult.setRepositoryUnitPrice(repositoryInfo.getUnitPrice());
                configResult.setState(OrderState.WaitDelivery.getCode());
                configResult.setOptionId(CurrentUserContext.getUid());
                configResult.setCreatorId(CurrentUserContext.getUid());

                // 总金额 = 库存的单价*购买数量
                BigDecimal goldCount = new BigDecimal(configResult.getConfigGoldCount());
                BigDecimal totalPrice = repositoryInfo.getUnitPrice().multiply(goldCount);
                totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_DOWN);

                // 卖家佣金
                BigDecimal commission = totalPrice.multiply(commissionBase);
                commission = commission.setScale(2, BigDecimal.ROUND_HALF_UP);
                // 卖家收益 = 配单总价-佣金
                BigDecimal income = totalPrice.subtract(commission);

                // 判断是否有使用红包
                if (orderInfo.getRedEnvelope() != null && orderInfo.getRedEnvelope() != 0) {
                    if (index != (len - 1)) {
                        // 设置前几笔子订单的红包平均值
                        configResult.setRedEnvelope(averageRedEnvelopeVal);
                    } else {
                        // 设置最后一笔子订单的红包值
                        configResult.setRedEnvelope(lastRedEnvelopeVal);
                    }
                }

                // 店铺商品，判断是否有使用店铺券,店铺券只记录到卖家第一笔配单记录
                if (orderInfo.getGoodsCat() == GoodsCat.Cat3.getCode()
                        && orderInfo.getShopCoupon() != null
                        && orderInfo.getShopCoupon() != 0
                        && index == 0) {
                    configResult.setShopCoupon(orderInfo.getShopCoupon());

                    // 卖家收益= 卖家收益-店铺券金额
                    income = income.subtract(new BigDecimal(orderInfo.getShopCoupon()));
                }

                // 订单价格
                BigDecimal orderAmount = orderInfo.getUnitPrice().multiply(goldCount);
                // 库存价格
                BigDecimal repositoryAmount = configResult.getRepositoryUnitPrice().multiply(goldCount);
                // 差价 = 订单价格(订单单价*购买数量)-库存价格(库存单价*购买数量)
                BigDecimal balance = orderAmount.subtract(repositoryAmount).setScale(2, BigDecimal.ROUND_HALF_UP);

                configResult.setTotalPrice(totalPrice);
                configResult.setIncome(income);
                configResult.setCommission(commission);
                configResult.setBalance(balance);
                configResult.setGoodsTypeId(orderInfo.getGoodsTypeId()); //ZW_C_JB_00008_20170523 ADD
                configResult.setGoodsTypeName(orderInfo.getGoodsTypeName());//ZW_C_JB_00008_20170523 ADD

                // 安排交易员
                arrangeTraderManager.arrangeTrader(configResult, repositoryInfo.getLoginAccount(), orderInfo.getServicerId(),
                        orderInfo.getGameName(), orderInfo.getRegion());

                // 新增配置的库存信息
                configResultInfoDBDAO.insert(configResult);

                // 更新库存量
                //原来的库存减去配置的库存
                /*repositoryInfo.setGoldCount(repositoryInfo.getGoldCount() - configResult.getConfigGoldCount());
				repositoryInfo.setSellableCount(repositoryInfo.getSellableCount() - configResult.getConfigGoldCount());
				repositoryManager.modifyRepository(repositoryInfo);*/

                repositoryManager.decrRepositoryCount(repositoryInfo, configResult.getConfigGoldCount().longValue(), orderInfo.getOrderId());
            }

            //修改订单状态为待发货
            // 订单状态先前状态必须是已付款
            if (orderInfo.getOrderState() == null) {
                throw new SystemException(ResponseCodes.EmptyOrderState.getCode(), ResponseCodes.EmptyOrderState.getMessage());
            }
            if (orderInfo.getOrderState() != OrderState.Paid.getCode()) {
                throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(), "订单先前状态必须是已付款");
            }

            // 增加订单修改日志
            StringBuffer sb = new StringBuffer();
            sb.append("订单号：").append(orderInfo.getOrderId()).append("，订单状态从");
            sb.append(OrderState.getTypeByCode(orderInfo.getOrderState()).getName());

            //修改订单状态
            if (OrderState.Paid.getCode() == orderInfo.getOrderState()) {
                orderInfo.setOrderState(OrderState.WaitDelivery.getCode());
                orderInfoDBDAO.updateOrderState(orderInfo);
            }

            sb.append("变成").append(OrderState.getTypeByCode(orderInfo.getOrderState()).getName());

            //logManager.add(ModuleType.ORDER, sb.toString(), CurrentUserContext.getUser());
            //log.info(sb.toString());

            OrderLogInfo log = new OrderLogInfo();
            log.setLogType(LogType.ORDER_OTHER);
            log.setOrderId(orderInfo.getOrderId());
            log.setRemark(sb.toString());
            orderLogManager.add(log);

            // 增加订单修改日志
			/*StringBuffer sb1 = new StringBuffer();
			sb1.append("为订单号为").append(orderInfo.getOrderId()).append("的订单配置库存成功！");
			logManager.add(ModuleType.ORDER, sb1.toString(), CurrentUserContext.getUser());
			log.info(sb1.toString());*/

            log = new OrderLogInfo();
            log.setLogType(LogType.ORDER_DISTRIBUTION);
            log.setOrderId(orderInfo.getOrderId());
            log.setRemark("订单号：" + orderInfo.getOrderId() + "，自动配置库存成功");
            orderLogManager.add(log);

        } finally {
            //解锁子订单
            orderConfigLockRedisDAO.unlock(String.valueOf(orderInfo.getId()));
        }
    }

    @Override
    public void autoTrans(Integer time) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("orderState", OrderState.Paid.getCode());
        Date now = new Date();
        Date TransTime = DateUtils.addSeconds(now, -time);
        queryMap.put("createEndTime", TransTime);
        queryMap.put("manualOperation", false);
        queryMap.put("goodsTypeName", ServicesContants.TYPE_ALL);
        List<OrderInfoEO> transList = orderInfoDBDAO.selectByMap(queryMap, "ID", false);
        if (null == transList) {
            return;
        }
        List<String> orderIdList = new ArrayList<String>();
        for (OrderInfoEO orderInfo : transList) {
            Boolean result = autoPayManager.queryPaymentDetail(orderInfo);
            if (result != null && result) {
                orderInfo.setManualOperation(true);
                orderInfoDBDAO.update(orderInfo);
                orderIdList.add(orderInfo.getOrderId());
            } else return;
        }

        // 增加转人工操作的订单日志
        if (orderIdList.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            StringBuffer sb = new StringBuffer();
            sb.append("将当前系统时间").append(sdf.format(now)).append("之前").append(time)
                    .append("秒的已付款的订单转人工操作。").append("订单号：").append(orderIdList.toString());
			/*logManager.add(ModuleType.ORDER, sb.toString(),
					CurrentUserContext.getUser());
			log.info(sb.toString());*/

            OrderLogInfo log = new OrderLogInfo();
            log.setLogType(LogType.ORDER_OTHER);
            log.setRemark(sb.toString());
            orderLogManager.add(log);
        }
    }
}
