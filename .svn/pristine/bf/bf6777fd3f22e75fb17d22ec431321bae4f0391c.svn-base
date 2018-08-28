package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.enums.DeliveryOrderStatus;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.StockType;
import com.wzitech.gamegold.shorder.business.IRepositoryInfoManager;
import com.wzitech.gamegold.shorder.business.IShGameConfigManager;
import com.wzitech.gamegold.shorder.business.ISplitRepositoryLogManager;
import com.wzitech.gamegold.shorder.dao.*;
import com.wzitech.gamegold.shorder.entity.*;
import com.wzitech.gamegold.shorder.enums.IncomeType;
import com.wzitech.gamegold.shorder.enums.RobotReasonType;
import com.wzitech.gamegold.shorder.enums.SplitRepositoryLogType;
import com.wzitech.gamegold.shorder.enums.SplitRepositoryStatus;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by 339928 on 2018/6/14.
 */
@Repository
public class RepositorySplitRequestManagerImple implements IRepositorySplitRequestManager {

    private static final Logger logger = LoggerFactory.getLogger(RepositorySplitRequestManagerImple.class);
    @Autowired
    IPurchaserDataDao purchaserDataDao;

    @Autowired
    IGameAccountDBDAO gameAccountDBDAO;

    @Autowired
    IShGameConfigManager shGameConfigManager;

    @Autowired
    ISplitRepositoryRequestDao splitRepositoryRequestDao;

    @Autowired
    ISplitRepositorySubRequestDao splitRepositorySubRequestDao;

    @Autowired
    private ISplitRepoReqOrderIdRedisDao splitRepoReqOrderIdRedisDao;

    @Autowired
    ISplitRepositoryLogManager spl;

    @Resource(name = "queryRepositoryInfo")
    IRepositoryInfoManager queryRepositoryInfo;

    @Autowired
    IStockDao stockDao;

    @Autowired
    IDeliveryOrderDao deliveryOrderDao;

    @Autowired
    IDeliverySubOrderDao deliverySubOrderDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRepositorySplitOrder(String orderId, Long id) {
        try {
            //必须主订单完单才能生成分仓单  单角色的生成一笔分仓单  多角色的生成多笔分仓单 主单结单后根据最新盘库表
            DeliveryOrder deliveryOrder = deliveryOrderDao.selectByOrderId(orderId);
            logger.info(String.format("生成分仓单信息,当前收货单%s,收货子单%s,状态为%s", orderId, id, DeliveryOrderStatus.getTypeByCode(deliveryOrder.getStatus())));
            if (deliveryOrder.getStatus() == DeliveryOrderStatus.COMPLETE.getCode() || deliveryOrder.getStatus() == DeliveryOrderStatus.COMPLETE_PART.getCode()) {
                List<DeliverySubOrder> deliverySubOrders = deliverySubOrderDao.queryUniqueMessageOrdersCount(orderId);
                for (DeliverySubOrder subOrder : deliverySubOrders) {
                    doCreateSplitRepositoryOrder(deliveryOrder, subOrder);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("生成分仓单错误信息,生成分仓单发生未知异常:{}", e);
        }
    }

    private boolean doCreateSplitRepositoryOrder(DeliveryOrder deliveryOrder, DeliverySubOrder deliverySubOrder) {
        logger.info(String.format("生成分仓单流程开始,收货单号%s,收货子单号%s", deliveryOrder.getOrderId(), deliverySubOrder.getId()));
        if (deliverySubOrder.getSplited() != null && deliverySubOrder.getSplited()) {
            logger.info(String.format("生成分仓单错误信息,主订单号%s,子订单号%s,已执行过分仓", deliverySubOrder.getOrderId(), deliveryOrder.getId()));
            return true;
        }
        //先要查询有没有开启分仓总开关  --需要当前登录账号 在purchaseData表中找到
        PurchaserData purchaserData = purchaserDataDao.selectByAccount(deliverySubOrder.getBuyerAccount());
        if (purchaserData == null || purchaserData.getIsSplit() == null || !purchaserData.getIsSplit()) {
            logger.info(String.format("生成分仓单错误信息,单号%s,当前用户%s,未开启分仓功能,不执行分仓", deliveryOrder.getOrderId(), deliverySubOrder.getBuyerAccount()));
            return true;
        }
        //查询收货号库存金币数量是否满足分仓阈值  先取设置 再取默认 --需要当前这个游戏名 游戏区 游戏服 登录账号 角色 5173账号用以确定角色唯一性
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("gameName", deliverySubOrder.getGameName());
        hashMap.put("region", deliverySubOrder.getRegion());
        hashMap.put("server", deliverySubOrder.getServer());
        hashMap.put("gameAccount", deliverySubOrder.getGameAccount());
        if (StringUtils.isNotBlank(deliverySubOrder.getGameRace())) {
            hashMap.put("gameRace", deliverySubOrder.getGameRace());
        }
        hashMap.put("buyerAccount", deliverySubOrder.getBuyerAccount());
        //查询收货账号下所有角色信息  按照库存从大到小排
        List<GameAccount> gameAccounts = gameAccountDBDAO.selectByMap(hashMap, "repository_count", false);
        GameAccount accountInfoNeed = new GameAccount();
        for (GameAccount gameRole : gameAccounts) {
            if ((deliverySubOrder.getGameRole()).equals(gameRole.getRoleName())) {
                accountInfoNeed = gameRole;
            }
        }
        //取配置
        ShGameConfig shGameConfig = shGameConfigManager.getConfigByGameName(deliverySubOrder.getGameName(), ServicesContants.GOODS_TYPE_GOLD, null, null);
        if (shGameConfig == null || !shGameConfig.getIsSplit()) {
            logger.info(String.format("生成分仓单错误信息,当前游戏%s,商品种类%s,未开启分仓功能,不执行分仓", deliverySubOrder.getGameName(), ServicesContants.GOODS_TYPE_GOLD));
            return true;
        }
        //库存上限
        Long edgePointRepository = purchaserData.getRepositoryCount() == null ? shGameConfig.getRepositoryCount() : purchaserData.getRepositoryCount();
        //缺口上限
        Long lessGapsRespository = purchaserData.getNeedCount() == null ? shGameConfig.getNeedCount() : purchaserData.getNeedCount();
        String splitRepoOrderId = splitRepoReqOrderIdRedisDao.getOrderId();
        //分仓角色盘库信息
        Stock stockMainSplitRepository = stockDao.queryByOrderIdAnSubId(deliveryOrder.getOrderId(), deliverySubOrder.getId());
        if (stockMainSplitRepository == null) {
            logger.info(String.format("生成分仓单错误信息,单号%s,子单号%s,当前账号%s,无盘库信息,不执行分仓", deliveryOrder.getOrderId(), deliverySubOrder.getId(), deliverySubOrder.getGameAccount()));
            return true;
        }
        if (stockMainSplitRepository.getStockType() != StockType.Delivery.getCode()) {
            logger.info(String.format("生成分仓单错误信息,单号%s,当前账号%s,角色%s,盘库信息非收货单,不执行分仓", deliveryOrder.getOrderId(), deliverySubOrder.getGameAccount(), deliverySubOrder.getGameRole()));
            return true;
        }
        //分仓账号库存基准 背包加仓库
        Long compareCount = stockMainSplitRepository.getPackageCount() + stockMainSplitRepository.getRepertoryCount();
        List<SplitRepositorySubRequest> subRequestList = new ArrayList<SplitRepositorySubRequest>();
        List<GameAccount> listForFreeze = new ArrayList<GameAccount>();
        int splitableRole = 0;
        if (accountInfoNeed.getLevel() == null) {
            logger.info(String.format("生成分仓单错误信息,单号%s,当前账号%s,角色信息%s,等级为空,不执行分仓", deliveryOrder.getOrderId(), deliverySubOrder.getGameAccount(), deliverySubOrder.getGameRole()));
            return true;
        }
        //需要分出的库存量  库存基准-
        Long muchCount = compareCount - (long) ((accountInfoNeed.getLevel() * accountInfoNeed.getLevel()) * (1 + shGameConfig.getMailFee()));
        Long oldMuchCount = muchCount;
        logger.info(String.format("单号%s,角色%s,需分仓数量为%s", deliveryOrder.getOrderId(), deliverySubOrder.getGameRole(), oldMuchCount));
        if (oldMuchCount <= 0) {
            logger.info(String.format("生成分仓单错误信息,单号%s,当前账号%s,角色信息%s,缺口为%s错误,不执行分仓", deliveryOrder.getOrderId(), deliverySubOrder.getGameAccount(), deliverySubOrder.getGameRole(), oldMuchCount));
            return true;
        }
        long finalCount = 0L;
        List<GameAccount> freezeGameAccountList = new ArrayList<GameAccount>();
        for (GameAccount gameAccountEo : gameAccounts) {
            //角色下 库存减去上线小于分仓阈值的 不参与分仓 结单一定会有角色的
            if (gameAccountEo.getRoleName().equals(deliverySubOrder.getGameRole())) {
                logger.info(String.format("当前阈值为%s", edgePointRepository));
                if (oldMuchCount.compareTo(edgePointRepository) < 0) {
                    logger.info(String.format("生成分仓单错误信息,单号%s,当前账号%s,角色%s,盘库库存为%s,库存上限%s,未超过当前阈值%s,不执行分仓",
                            deliveryOrder.getOrderId(), deliverySubOrder.getBuyerAccount(), deliverySubOrder.getGameRole(), compareCount,
                            (gameAccountEo.getLevel() * gameAccountEo.getLevel()) * (1 + shGameConfig.getMailFee()),
                            edgePointRepository));
                    return true;
                }
            } else {
                if (!gameAccountEo.getIsShRole()) {
                    //非收货角色才可以进行被分仓
                    if (gameAccountEo.getTodaySaleCount() == null) {
                        gameAccountEo.setTodaySaleCount(0L);
                    }
                    if (gameAccountEo.getRepositoryCount() == null) {
                        gameAccountEo.setRepositoryCount(0L);
                    }
                    if (gameAccountEo.getFreezeNeedCount() == null) {
                        gameAccountEo.setFreezeNeedCount(0L);
                    }
                    //当前 收货角色（被分仓角色）库存缺口(真实缺口)
                    long repositoryGap = (long) ((gameAccountEo.getLevel() * gameAccountEo.getLevel()) * (1 + shGameConfig.getMailFee()))
                            - gameAccountEo.getRepositoryCount() - gameAccountEo.getTodaySaleCount() - gameAccountEo.getFreezeNeedCount();
                    //缺口大于最低库存阈值
                    if (repositoryGap >= lessGapsRespository) {
                        splitableRole++;
                        if (muchCount > repositoryGap) {
                            subRequestList.add(new SplitRepositorySubRequest(splitRepoOrderId,
                                    gameAccountEo.getRoleName(), repositoryGap, new Date()));
                            logger.info(String.format("可执行分仓,当前被分仓角色%s,库存缺口%s,分配数量%s", gameAccountEo.getRoleName(), repositoryGap, repositoryGap));
                            gameAccountEo.setFreezeNeedCount(repositoryGap);
                            freezeGameAccountList.add(gameAccountEo);
                            muchCount -= repositoryGap;
                            finalCount += repositoryGap;
                        } else if (muchCount < repositoryGap && muchCount != 0) {
                            subRequestList.add(new SplitRepositorySubRequest(splitRepoOrderId, gameAccountEo.getRoleName(), muchCount, new Date()));
                            logger.info(String.format("可执行分仓,当前被分仓角色%s,库存缺口%s,分配数量%s", gameAccountEo.getRoleName(), repositoryGap, muchCount));
                            //角色表  分仓只冻结 不减去库存量
                            gameAccountEo.setFreezeNeedCount(muchCount);
                            freezeGameAccountList.add(gameAccountEo);
                            finalCount += muchCount;
                            muchCount = 0L;
                        }
                    } else {
                        logger.info(String.format("未达成被分仓信息,当前角色%s,真实库存缺口%s,最低库存阈值%s", gameAccountEo.getRoleName(), repositoryGap, lessGapsRespository));
                    }
                } else {
                    logger.info(String.format("未达成被分仓信息,当前角色%s,是收货角色,不参与被分仓", gameAccountEo.getRoleName()));
                }
            }
        }
        if (splitableRole == 0) {
            logger.info(String.format("生成分仓单错误信息,单号%s,当前账号%s,角色%s,因当前不存在库存缺口小于库存缺口阈值%s的被分仓角色,不执行分仓",
                    deliveryOrder.getOrderId(), deliverySubOrder.getBuyerAccount(), deliverySubOrder.getGameRole(), lessGapsRespository));
            return true;
        }
        SplitRepositoryRequest splitRepositoryRequest = new SplitRepositoryRequest();
        splitRepositoryRequest.setCount(finalCount);
        splitRepositoryRequest.setOrderId(splitRepoOrderId);
        splitRepositoryRequest.setBuyerAccount(deliverySubOrder.getBuyerAccount());
        splitRepositoryRequest.setGameName(deliverySubOrder.getGameName());
        splitRepositoryRequest.setGameRole(deliverySubOrder.getGameRole());
        splitRepositoryRequest.setRegion(deliverySubOrder.getRegion());
        //二级密码存repository表里的
        if (StringUtils.isNotBlank(accountInfoNeed.getSecondPwd())) {
            splitRepositoryRequest.setSecondPwd(accountInfoNeed.getSecondPwd());
        }
        splitRepositoryRequest.setServer(deliverySubOrder.getServer());
        if (StringUtils.isNotBlank(deliverySubOrder.getGameRace())) {
            splitRepositoryRequest.setGameRace(deliverySubOrder.getGameRace());
        }
        splitRepositoryRequest.setGameAccount(deliverySubOrder.getGameAccount());
        if (StringUtils.isNotBlank(accountInfoNeed.getGamePwd())) {
            splitRepositoryRequest.setPwd(accountInfoNeed.getGamePwd());
        }
        splitRepositoryRequest.setStatus(SplitRepositoryStatus.SPLITTING.getCode());
        splitRepositoryRequest.setRealCount(0L);
        splitRepositoryRequest.setShOrderId(deliveryOrder.getOrderId());
        splitRepositoryRequest.setShSubOrderId(deliverySubOrder.getId());
        splitRepositoryRequest.setCreateTime(new Date());
        splitRepositoryRequest.setSplitReason(String.format("超交易上限%s万金,且存在库存缺口超过%s万金的角色,开始分仓", edgePointRepository, lessGapsRespository));
        deliverySubOrder.setSplited(true);
        deliverySubOrderDao.updateSplited(deliverySubOrder);
        gameAccountDBDAO.batchAddGameAccountFreezeCount(freezeGameAccountList);
        splitRepositoryRequestDao.insert(splitRepositoryRequest);
        splitRepositorySubRequestDao.batchInsert(subRequestList);
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishSplitRepositoryOrder(Long subSplitId, Long realCount, int robotReason, Long useRepertoryCount, String robotOtherReason) {
        logger.info(String.format("分仓单完单,当前子分仓单号%s,完单数量%s", subSplitId, realCount));
        //分仓原因1是顶号了
        if (subSplitId == null) {
            throw new SystemException(ResponseCodes.SplitRespositorySubOrderIdNull.getCode(), ResponseCodes.SplitRespositorySubOrderIdNull.getMessage());
        }
        if (realCount == null || realCount < 0) {
            throw new SystemException(ResponseCodes.SplitRepositorySubOrderCountNull.getCode(), ResponseCodes.SplitRepositorySubOrderCountNull.getMessage());
        }
        SplitRepositorySubRequest splitRepositorySubRequest = splitRepositorySubRequestDao.selectById(subSplitId);
        if (splitRepositorySubRequest.getStatus() != SplitRepositoryStatus.SPLITTING.getCode()) {
            throw new SystemException(ResponseCodes.OrderStatusHasChangedError.getCode(), ResponseCodes.OrderStatusHasChangedError.getMessage());
        }
        SplitRepositoryRequest splitRepositoryRequest = splitRepositoryRequestDao.selectByOrderId(splitRepositorySubRequest.getOrderId(), true);
        //不是顶号的 直接一个一个结
        if (robotReason == RobotReasonType.FCFinished.getCode()) {
            if (useRepertoryCount != null) {
                splitRepositorySubRequest.setUseRepertoryCount(useRepertoryCount);
            }
            if (splitRepositorySubRequest.getCount() == 0L) {
                splitRepositorySubRequest.setStatus(SplitRepositoryStatus.FAIL.getCode());
                splitRepositorySubRequest.setRealCount(0L);
            } else if (splitRepositorySubRequest.getCount() <= realCount) {
                splitRepositorySubRequest.setRealCount(splitRepositorySubRequest.getCount());
                splitRepositorySubRequest.setStatus(SplitRepositoryStatus.SUCCESS.getCode());
            } else if (splitRepositorySubRequest.getCount() > realCount && realCount > 0) {
                splitRepositorySubRequest.setStatus(SplitRepositoryStatus.PART_SPLIT.getCode());
                splitRepositorySubRequest.setRealCount(realCount);
            }
            if (StringUtils.isNotBlank(robotOtherReason)) {
                splitRepositorySubRequest.setRobotOtherReason(robotOtherReason);
            }
            splitRepositorySubRequest.setUpdateTime(new Date());
            splitRepositorySubRequestDao.update(splitRepositorySubRequest);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("buyerAccount", splitRepositoryRequest.getBuyerAccount());
            params.put("gameName", splitRepositoryRequest.getGameName());
            params.put("region", splitRepositoryRequest.getRegion());
            params.put("server", splitRepositoryRequest.getServer());
            params.put("gameRace", splitRepositoryRequest.getGameRace());
            params.put("gameAccount", splitRepositoryRequest.getGameAccount());
            params.put("roleName", splitRepositorySubRequest.getGameRole());
            params.put("updateTime", new Date());
            params.put("freezeNeedCount", 0);
            if (realCount != 0) {
                //收到是0不记录日志
                SplitRepositoryLog splitRepositoryLog = new SplitRepositoryLog();
                splitRepositoryLog.setFcId(subSplitId);
                splitRepositoryLog.setGameName(splitRepositoryRequest.getGameName());
                splitRepositoryLog.setRegion(splitRepositoryRequest.getRegion());
                splitRepositoryLog.setServer(splitRepositoryRequest.getServer());
                splitRepositoryLog.setBuyerAccount(splitRepositoryRequest.getBuyerAccount());
                if (StringUtils.isNotBlank(splitRepositoryRequest.getGameRace())) {
                    splitRepositoryLog.setGameRace(splitRepositoryRequest.getGameRace());
                }
                splitRepositoryLog.setGameAccount(splitRepositoryRequest.getGameAccount());
                splitRepositoryLog.setCreateTime(new Date());
                //子单是备份仓角色  属于入方
                splitRepositoryLog.setRoleName(splitRepositorySubRequest.getGameRole());
                splitRepositoryLog.setLogType(SplitRepositoryLogType.SPLIT_REPOSITORY_GET.getCode());
                splitRepositoryLog.setFcOrderId(splitRepositorySubRequest.getOrderId());
                splitRepositoryLog.setCount(splitRepositorySubRequest.getRealCount());
                splitRepositoryLog.setIncomeType(IncomeType.INCOME.getCode());
                spl.saveLog(splitRepositoryLog);
                //同时需要添加收货角色表库存数量   添加销售库存 可销售库存
                params.put("modifyRepositoryCount", realCount);
            }
            gameAccountDBDAO.updateAccountByMap(params);
        }
        //顶号的全部都结掉
        List<GameAccount> gameAccounts = new ArrayList<GameAccount>();
        if (robotReason == RobotReasonType.EdgeOutAccount.getCode() || robotReason == RobotReasonType.RobotAberrant.getCode()) {
            List<SplitRepositorySubRequest> subRequestList = splitRepositorySubRequestDao.selectRealCountZeroWithId(subSplitId);
            List<SplitRepositoryLog> list = new ArrayList<SplitRepositoryLog>();
            for (SplitRepositorySubRequest spR : subRequestList) {
                //未处理的其他所有子分仓单 全部归为分仓失败  这里遍历改值而已 也包括了当前这笔
                if (spR.getStatus() == SplitRepositoryStatus.SPLITTING.getCode()) {
                    spR.setRealCount(0L);
                    spR.setStatus(SplitRepositoryStatus.FAIL.getCode());
                    spR.setUpdateTime(new Date());
                    //对应的冻结数量改掉
                    GameAccount gameAccount = new GameAccount();
                    gameAccount.setBuyerAccount(splitRepositoryRequest.getBuyerAccount());
                    gameAccount.setGameName(splitRepositoryRequest.getGameName());
                    gameAccount.setRegion(splitRepositoryRequest.getRegion());
                    gameAccount.setServer(splitRepositoryRequest.getServer());
                    gameAccount.setGameRace(splitRepositoryRequest.getGameRace());
                    gameAccount.setGameAccount(splitRepositoryRequest.getGameAccount());
                    gameAccount.setRoleName(spR.getGameRole());
                    gameAccount.setFreezeNeedCount(0L);
                    gameAccounts.add(gameAccount);
                }
                //当前处理的子分仓单
                if (spR.getId().compareTo(subSplitId) == 0) {
                    if (useRepertoryCount != null) {
                        spR.setUseRepertoryCount(useRepertoryCount);
                    }
                    if (StringUtils.isNotBlank(robotOtherReason)) {
                        spR.setRobotOtherReason(robotOtherReason);
                    }
                }
            }
            gameAccountDBDAO.batchUpdate(gameAccounts);
            splitRepositorySubRequestDao.batchUpdate(subRequestList);
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orderId", splitRepositorySubRequest.getOrderId());
        List<SplitRepositorySubRequest> subRequestList = splitRepositorySubRequestDao.selectByMap(param);
        int successCount = 0;
        long successAmount = 0L;
        //通过遍历 获取子订单成功笔数以及数量
        for (SplitRepositorySubRequest subOrder : subRequestList) {
            if (subOrder.getStatus() == SplitRepositoryStatus.FAIL.getCode() || subOrder.getStatus() == SplitRepositoryStatus.PART_SPLIT.getCode() ||
                    subOrder.getStatus() == SplitRepositoryStatus.SUCCESS.getCode()) {
                successCount++;
                successAmount += subOrder.getRealCount();
            }
        }
        //主分仓单结单 以及记录日志 此日志应该是支出
        if (successCount == subRequestList.size()) {
            splitRepositoryRequest.setRealCount(successAmount);
            splitRepositoryRequest.setUpdateTime(new Date());
            if (splitRepositoryRequest.getCount() == successAmount) {
                splitRepositoryRequest.setStatus(SplitRepositoryStatus.SUCCESS.getCode());
            } else if (splitRepositoryRequest.getCount() > successAmount && successAmount != 0L) {
                splitRepositoryRequest.setStatus(SplitRepositoryStatus.PART_SPLIT.getCode());
            } else if (successAmount == 0L) {
                splitRepositoryRequest.setStatus(SplitRepositoryStatus.FAIL.getCode());
            }
            if (robotReason == RobotReasonType.EdgeOutAccount.getCode()) {
                splitRepositoryRequest.setRobotReason(RobotReasonType.EdgeOutAccount.getCode());
            } else {
                splitRepositoryRequest.setRobotReason(RobotReasonType.FCFinished.getCode());
            }
            splitRepositoryRequestDao.update(splitRepositoryRequest);
            if (successAmount != 0) {
                //收到数量不为0才记录日志
                SplitRepositoryLog splitRepositoryLog = new SplitRepositoryLog();
                splitRepositoryLog.setBuyerAccount(splitRepositoryRequest.getBuyerAccount());
                splitRepositoryLog.setGameName(splitRepositoryRequest.getGameName());
                splitRepositoryLog.setRegion(splitRepositoryRequest.getRegion());
                splitRepositoryLog.setServer(splitRepositoryRequest.getServer());
                if (StringUtils.isNotBlank(splitRepositoryRequest.getGameRace())) {
                    splitRepositoryLog.setGameRace(splitRepositoryRequest.getGameRace());
                }
                splitRepositoryLog.setGameAccount(splitRepositoryRequest.getGameAccount());
                splitRepositoryLog.setCreateTime(new Date());
                splitRepositoryLog.setRoleName(splitRepositoryRequest.getGameRole());
                splitRepositoryLog.setLogType(SplitRepositoryLogType.SPLIT_REPOSITORY_SPEND.getCode());
                splitRepositoryLog.setFcOrderId(splitRepositoryRequest.getOrderId());
                splitRepositoryLog.setCount(splitRepositoryRequest.getRealCount());
                splitRepositoryLog.setIncomeType(IncomeType.SPEND.getCode());
                spl.saveLog(splitRepositoryLog);
            }
        }
        param.remove("orderId");
        param.put("gameName", splitRepositoryRequest.getGameName());
        param.put("region", splitRepositoryRequest.getRegion());
        param.put("server", splitRepositoryRequest.getServer());
        param.put("gameRace", splitRepositoryRequest.getGameRace());
        param.put("gameAccount", splitRepositoryRequest.getGameAccount());
        //更新库存repository  这里要减去的是主分仓角色的库存
        param.put("loginAccount", splitRepositoryRequest.getBuyerAccount());
        param.put("sellerGameRole", splitRepositoryRequest.getGameRole());
        param.put("modifySellableCount", -realCount);
        param.put("modifyGoldCount", -realCount);
        param.put("goodsTypeName", ServicesContants.GOODS_TYPE_GOLD);
        queryRepositoryInfo.decreaseSellableCount(param);
    }
}
