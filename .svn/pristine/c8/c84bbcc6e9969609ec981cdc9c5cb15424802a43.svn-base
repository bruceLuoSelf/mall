package com.wzitech.gamegold.store.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.StockType;
import com.wzitech.gamegold.repository.dao.IRepositoryDBDAO;
import com.wzitech.gamegold.repository.dao.ISellerDBDAO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.shorder.business.IShGameConfigManager;
import com.wzitech.gamegold.shorder.business.impl.ISyncRepositoryManager;
import com.wzitech.gamegold.shorder.dao.IGameAccountDBDAO;
import com.wzitech.gamegold.shorder.entity.GameAccount;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 340082 on 2018/6/12.
 */
@Component("syncRepositoryManager")
public class SyncRepositoryManagerImpl implements ISyncRepositoryManager {

    /**
     * 日志输出
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IGameAccountDBDAO gameAccountDBDAO;

    @Autowired
    IRepositoryDBDAO repositoryDBDAO;

    @Autowired
    private IShGameConfigManager shGameConfigManager;

    @Autowired
    ISellerDBDAO sellerDBDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncAddRepository(Long gameAccountId, BigDecimal price) {
        //1.查询更新收货角色表
        GameAccount gameAccount = gameAccountDBDAO.selectById(gameAccountId);
        if (gameAccount == null) {
            throw new SystemException(ResponseCodes.EmptyShRoleName.getCode(), ResponseCodes.EmptyShRoleName.getMessage());
        }
        //获取用户对应的客服
        IUser seller = CurrentUserContext.getUser();
        SellerInfo sellerInfo = sellerDBDAO.selectUniqueByProp(
                "loginAccount", seller.getLoginAccount());
        if (sellerInfo == null) {
            throw new SystemException(
                    ResponseCodes.EmptySellerInfo.getCode());
        }
        if(!gameAccount.getBuyerAccount().equals(seller.getLoginAccount())){
            throw new SystemException(
                    ResponseCodes.AccessDeliveryOrderPermissionDenied.getCode(),ResponseCodes.AccessDeliveryOrderPermissionDenied.getMessage());
        }
        //判断用户是否拥有寄售权限，没有的情况下跳出
        if(!sellerInfo.getIsShieldedType()){
            throw new SystemException(
                    ResponseCodes.NotSellerForSync.getCode(),ResponseCodes.NotSellerForSync.getMessage());
        }
        //更新销售状态为true
        gameAccount.setIsSale(true);
        gameAccountDBDAO.update(gameAccount);
        if (price == null) {
            //2.查询销售库存表
            Map<String, Object> selectMap = new HashMap<String, Object>();
            selectMap.put("gameName", gameAccount.getGameName());
            selectMap.put("region", gameAccount.getRegion());
            selectMap.put("server", gameAccount.getServer());
            selectMap.put("loginAccount", CurrentUserContext.getUserLoginAccount());
            List<RepositoryInfo> repositoryInfos = repositoryDBDAO.selectByMap(selectMap);
            price = repositoryInfos.get(0).getUnitPrice();
        }
        //3.将收货角色表字段映射到销售角色表
        RepositoryInfo repositoryInfo = new RepositoryInfo();
        repositoryInfo.setGameName(gameAccount.getGameName());
        repositoryInfo.setRegion(gameAccount.getRegion());
        repositoryInfo.setServer(gameAccount.getServer());
        repositoryInfo.setGameRace(gameAccount.getGameRace());
        ShGameConfig configByGameName = shGameConfigManager.getConfigByGameName(gameAccount.getGameName(), ServicesContants.GOODS_TYPE_GOLD, null, null);
        if(configByGameName == null){
            repositoryInfo.setMoneyName("万金");
        }else {
            repositoryInfo.setMoneyName(configByGameName.getUnitName());
        }

        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("loginAccount",gameAccount.getBuyerAccount());
        queryMap.put("accountUid", gameAccount.getBuyerUid());
        queryMap.put("backGameName",gameAccount.getGameName());
        queryMap.put("backRegion", gameAccount.getRegion());
        queryMap.put("backServer", gameAccount.getServer());
        queryMap.put("backGameRace", gameAccount.getGameRace());
        queryMap.put("gameAccount", gameAccount.getGameAccount());
        queryMap.put("sellerGameRole", gameAccount.getRoleName());
        //ZW_C_JB_00008_20170517 ADD
        queryMap.put("goodsTypeName", ServicesContants.GOODS_TYPE_GOLD);
        queryMap.put("isDeleted", false);
        queryMap.put("lockMode", true);
        List<RepositoryInfo> repositoryInfoList = repositoryDBDAO.selectByMap(queryMap, "CREATE_TIME", false);
        if (!CollectionUtils.isEmpty(repositoryInfoList)) {
            RepositoryInfo dbExsitRepository = repositoryInfoList.get(0);
            dbExsitRepository.setStockCount(gameAccount.getRepositoryCount());
            dbExsitRepository.setIsDeleted(false);
            dbExsitRepository.setUnitPrice(price);
            dbExsitRepository.setLastUpdateTime(new Date());
            repositoryDBDAO.update(dbExsitRepository);
        }else {
            //单价限制小数后5位
            repositoryInfo.setUnitPrice(price.setScale(5, BigDecimal.ROUND_HALF_UP));
            repositoryInfo.setGoldCount(gameAccount.getRepositoryCount() == null ? 0L : gameAccount.getRepositoryCount());
            repositoryInfo.setSellableCount(gameAccount.getRepositoryCount() == null ? 0L : gameAccount.getRepositoryCount());
            repositoryInfo.setStockCount(gameAccount.getRepositoryCount());
            repositoryInfo.setGameAccount(gameAccount.getGameAccount());
            repositoryInfo.setGamePassWord(gameAccount.getGamePwd());
            repositoryInfo.setSellerGameRole(gameAccount.getRoleName());
            repositoryInfo.setSonGamePassWord(gameAccount.getSecondPwd());
            repositoryInfo.setAccountUid(seller.getUid());
            repositoryInfo.setLoginAccount(seller.getLoginAccount());
            repositoryInfo.setGoodsTypeName(ServicesContants.GOODS_TYPE_GOLD);
            repositoryInfo.setGoodsTypeId(ServicesContants.GOODS_TYPE_GOLD_ID);
            repositoryInfo.setCreateTime(new Date());
            repositoryInfo.setShRepositoryId(gameAccountId);
            repositoryInfo.setIsDeleted(false);
            repositoryInfo.setLastUpdateTime(new Date());
            repositoryInfo.setServicerId(sellerInfo.getServicerId());
            repositoryDBDAO.insert(repositoryInfo);
        }
    }

    /**
     * 添加 库存数量 同步使用方法
     *
     * @param repositoryId
     * @param count
     */
    @Override
    public void modifyRepositoryGoldCount(Long repositoryId, Long count) {
        repositoryDBDAO.incrRepositoryCount(repositoryId, count, null);
    }


    /**
     * 添加 库存数量 同步使用方法
     *
     * @param
     * @param count
     */
    @Override
    public void modifyRepositoryGoldCountByInfo(String loginAccount, String gameAccount, String gameRole, String gameName, String regionName, String serverName, String gameRace, Long count, Long stockCount) {

        Map<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("loginAccount", loginAccount);
        updateMap.put("backGameName", gameName);
        updateMap.put("backRegion", regionName);
        updateMap.put("backServer", serverName);
        updateMap.put("backGameRace", gameRace);
        updateMap.put("gameAccount", gameAccount);
        updateMap.put("sellerGameRole", gameRole);
        updateMap.put("isDeleted", false);
        updateMap.put("lockMode", true);
        updateMap.put("sellableCount", count);
        updateMap.put("goldCount", count);
        if(stockCount != null){
            updateMap.put("stockCount", stockCount);
        }

        repositoryDBDAO.updateRepositoryCountBySync(updateMap);
    }


    /**
     * 添加 库存数量 同步使用方法
     *
     */
    @Override
    public void modifyGameAccountCountByInfo(String gameAccount, String loginAccount, String gameName, String region, String server, String gameRace, String sellerGameRole, Long goldCount) {
        Map<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("gameAccount", gameAccount);
        updateMap.put("buyerAccount", loginAccount);
        updateMap.put("gameName", gameName);
        updateMap.put("region", region);
        updateMap.put("server", server);
        updateMap.put("gameRace", gameRace);
        updateMap.put("roleName", sellerGameRole);
        updateMap.put("repositoryCount", goldCount);

        repositoryDBDAO.updateRepositoryCountBySync(updateMap);
    }


    /**
     * 添加 同步使用方法
     * 同步盘库数方法
     * 自动化使用
     * 1.更新销售库存表
     * 2.更新收货角色表
     * 3.添加分仓日志表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyGameAccountStockCountByInfo(int stockType, String orderId, Integer inComeType, String gameAccount, String loginAccount, String gameName, String region, String server, String gameRace, String sellerGameRole, Long modifyCount, Long stockCount) {
        //1.更新销售库存表
        Map<String, Object> updateMap = new HashMap<String, Object>(8);
        updateMap.put("gameAccount", gameAccount);
        updateMap.put("buyerAccount", loginAccount);
        updateMap.put("gameName", gameName);
        updateMap.put("region", region);
        updateMap.put("server", server);
        updateMap.put("gameRace", gameRace);
        updateMap.put("sellerGameRole", sellerGameRole);
        updateMap.put("stockCount", stockCount);
        repositoryDBDAO.updateRepositoryCountBySync(updateMap);
        //2.更新收货角色表
        Map<String, Object> params = new HashMap<String, Object>(8);
        params.put("buyerAccount", loginAccount);
        params.put("gameName", gameName);
        params.put("region", region);
        params.put("server", server);
        params.put("gameRace", gameRace);
        params.put("gameAccount", gameAccount);
        params.put("roleName", sellerGameRole);
        params.put("repositoryCount", stockCount);
        params.put("updateTime",new Date());
        gameAccountDBDAO.setRepositoryCount(params);
        logger.info("成功更新stockCount数据量"+stockCount+"数据为：{}",updateMap);
//        SplitRepositoryLog splitRepositoryLog = new SplitRepositoryLog();
//        splitRepositoryLog.setBuyerAccount(loginAccount);
//        splitRepositoryLog.setGameName(gameName);
//        splitRepositoryLog.setRegion(region);
//        splitRepositoryLog.setServer(server);
//        splitRepositoryLog.setGameRace(gameRace);
//        splitRepositoryLog.setGameAccount(gameAccount);
//        splitRepositoryLog.setRoleName(sellerGameRole);
//        splitRepositoryLog.setCount(modifyCount);
//        splitRepositoryLog.setFcOrderId(orderId);
//        splitRepositoryLog.setIncomeType(inComeType);
//        if(StockType.Seller.getCode() == stockType){
//            //因为需要repositoryInfo ConfigResultInfoEO orderInfo表数据避免查询只能添加在此 添加销售日志
//            splitRepositoryLog.setLogType(LogTypeEnum.SELLERPAID.getCode());
//        }else if(StockType.Delivery.getCode() == stockType){
//            splitRepositoryLog.setLogType(LogTypeEnum.DELIVERYSALED.getCode());
//        }else if(StockType.SplitRepository.getCode() == stockType && inComeType.equals(IncomeType.INCOME.getCode())){
//            splitRepositoryLog.setLogType(LogTypeEnum.ARRANGEREPOSITORYSALED.getCode());
//        }else if(StockType.SplitRepository.getCode() == stockType && inComeType.equals(IncomeType.SPEND.getCode())){
//            splitRepositoryLog.setLogType(LogTypeEnum.ARRANGEREPOSITORYPAID.getCode());
//        }else {
//            //都不是的情况下跳出
//            return;
//        }
//        splitRepositoryLogManager.saveLog(splitRepositoryLog);

    }

    /**
     * 添加 库存数量 同步使用方法
     *盘库使用方法
     * @param repositoryId
     * @param count
     */
    @Override
    public void modifyRepositoryGoldCount(Long repositoryId, Long count, Long stockCount) {
        repositoryDBDAO.incrRepositoryCount(repositoryId, count, stockCount);
    }

    /**
     * 添加 角色表数量 同步使用方法
     *
     * @param gameAccountId
     * @param count
     */
    @Override
    public void modifyGameAccountGoldCount(Long gameAccountId, Long count) {
        gameAccountDBDAO.incrGameAccountRepositoryCount(gameAccountId, count);
    }


    /**
     * 添加 角色表数量 同步使用方法
     *
     * @param repositoryId 库存表id
     * @param count 不可为null
     */
    @Override
    public void updateStockCountByRepositoryId(Long repositoryId, Long count) {
        repositoryDBDAO.updateStockCountById(repositoryId, count);
    }


    /**
     * 盘库接口 , 参数为gameAccountId
     *
     * @param gameAccountId
     * @param count
     */
    @Override
    public void inventoryStockByGameAccountId(Long gameAccountId, Long count, Long stockCount) {
        GameAccount gameAccount = gameAccountDBDAO.selectById(gameAccountId);
        this.modifyRepositoryGoldCount(gameAccount.getRepositoryId(), count, stockCount);
        this.modifyGameAccountGoldCount(gameAccountId, count);
    }

    /**
     * 盘库接口 , 参数为repositoryId
     *
     * @param repositoryId
     * @param count
     */
    @Override
    public void inventoryStockByRepositoryId(Long repositoryId, Long count, Long stockCount) {
        RepositoryInfo repositoryInfo = repositoryDBDAO.selectById(repositoryId);
        this.modifyRepositoryGoldCount(repositoryId, count, stockCount);
        this.modifyGameAccountGoldCount(repositoryInfo.getShRepositoryId(), count);
    }

    /**
     * 删除库存表数据时同步修改收货角色表IsSale状态为false
     *
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRepositorysSyncShgameAccountIsSaleFalse(String gameAccount, String loginAccount, String gameName, String region, String server, String gameRace, String sellerGameRole) {
        Map<String, Object> updateMap = new HashMap<String, Object>(8);
        updateMap.put("gameAccount", gameAccount);
        updateMap.put("buyerAccount", loginAccount);
        updateMap.put("gameName", gameName);
        updateMap.put("region", region);
        updateMap.put("server", server);
        updateMap.put("gameRace", gameRace);
        updateMap.put("roleName", sellerGameRole);
        updateMap.put("isSale",false);
        int i = gameAccountDBDAO.updateAccountByMap(updateMap);
        logger.info("成功更新isSale数据量：{},更新Map为：{}",i,updateMap);
    }
}
