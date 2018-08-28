package com.wzitech.gamegold.store.business.impl;

import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.repository.dao.IRepositoryDBDAO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.shorder.business.ISplitRepositoryLogManager;
import com.wzitech.gamegold.shorder.dao.IGameAccountDBDAO;
import com.wzitech.gamegold.shorder.entity.SplitRepositoryLog;
import com.wzitech.gamegold.shorder.enums.LogTypeEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 340082 on 2018/6/14.
 */
@Aspect
@Component
public class RepositoryCountAspect {
    /**
     * 日志输出
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IGameAccountDBDAO gameAccountDBDAO;

    @Autowired
    private IRepositoryDBDAO repositoryDBDAO;

    @After("execution(* com.wzitech.gamegold.repository.business.impl.RepositoryManagerImpl.incrRepositoryCount(..)))")
    public void afterCountSyncShGameAccountIncr(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        RepositoryInfo repository = (RepositoryInfo) args[0];
        Long count = (Long) args[1];

        //根据区服角色进行更新
        Map<String, Object> params = new HashMap<String, Object>(8);
        params.put("buyerAccount", repository.getLoginAccount());
        params.put("gameName", repository.getGameName());
        params.put("region", repository.getRegion());
        params.put("server", repository.getServer());
        params.put("gameRace", repository.getGameRace());
        params.put("gameAccount", repository.getGameAccount());
        params.put("roleName", repository.getSellerGameRole());
        params.put("repositoryCount", count);

        gameAccountDBDAO.addRepositoryCount(params);
        logger.info("incrRepositoryCount切入执行，更新数据：{}",params);
    }

    @After("execution(* com.wzitech.gamegold.repository.business.impl.RepositoryManagerImpl.decrRepositoryCount(..)))")
    public void afterCountSyncShGameAccountDecr(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        RepositoryInfo repository = (RepositoryInfo) args[0];
        //在sql中做负数操作
        Long count = (Long) args[1];
        //根据区服角色进行更新
        Map<String, Object> params = new HashMap<String, Object>(8);
        params.put("buyerAccount", repository.getLoginAccount());
        params.put("gameName", repository.getGameName());
        params.put("region", repository.getRegion());
        params.put("server", repository.getServer());
        params.put("gameRace", repository.getGameRace());
        params.put("gameAccount", repository.getGameAccount());
        params.put("roleName", repository.getSellerGameRole());
        params.put("repositoryCount", count);
        gameAccountDBDAO.reduceRepositoryCount(params);
        logger.info("decrRepositoryCount切入执行，更新数据：{}",params);
    }


    @After("execution(* com.wzitech.gamegold.shorder.business.impl.GameAccountManagerImpl.updateStatus(..)))")
    public void afterCountSyncRepositoryIncr(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Map<String, Object> map = (Map<String, Object>) args[0];
        if (map.containsKey("addRepositoryCount")) {
            Map<String, Object> updateMap = new HashMap<String, Object>();
            updateMap.put("loginAccount", map.get("buyerAccount"));
            updateMap.put("backGameName", map.get("gameName"));
            updateMap.put("backRegion", map.get("region"));
            updateMap.put("backServer", map.get("server"));
            updateMap.put("backGameRace", map.get("gameRace"));
            updateMap.put("gameAccount", map.get("gameAccount"));
            updateMap.put("sellerGameRole", map.get("roleName"));
            updateMap.put("isDeleted", false);
            updateMap.put("lockMode", true);
            updateMap.put("modifySellableCount", map.get("addRepositoryCount"));
            updateMap.put("modifyGoldCount", map.get("addRepositoryCount"));
            updateMap.put("goodsTypeName", ServicesContants.GOODS_TYPE_GOLD);
            repositoryDBDAO.updateRepositoryCountBySync(updateMap);
            logger.info("updateStatus切入执行，更新数据：{}",map);
        }
    }

    @After("execution(* com.wzitech.gamegold.shorder.dao.impl.GameAccountDBDAOImpl.updateAccountByMap(..)))")
    public void afterCountSyncUpdateAccountByMap(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Map<String, Object> map = (Map<String, Object>) args[0];
        Map<String, Object> updateMap = new HashMap<String, Object>();
        Object repositoryCount = null;
        if (map.containsKey("repositoryCount")) {
            updateMap.put("sellableCount", map.get("repositoryCount"));
            updateMap.put("goldCount", map.get("repositoryCount"));
        }else if(map.containsKey("modifyRepositoryCount")){
            updateMap.put("modifySellableCount", map.get("modifyRepositoryCount"));
            updateMap.put("modifyGoldCount", map.get("modifyRepositoryCount"));
        }else {
            return;
        }
        updateMap.put("loginAccount", map.get("buyerAccount"));
        updateMap.put("backGameName", map.get("gameName"));
        updateMap.put("backRegion", map.get("region"));
        updateMap.put("backServer", map.get("server"));
        updateMap.put("backGameRace", map.get("gameRace"));
        updateMap.put("gameAccount", map.get("gameAccount"));
        updateMap.put("sellerGameRole", map.get("roleName"));
        updateMap.put("goodsTypeName", ServicesContants.GOODS_TYPE_GOLD);
        updateMap.put("isDeleted", false);
        updateMap.put("lastUpdateTime", new Date());
        repositoryDBDAO.updateRepositoryCountBySync(updateMap);
        logger.info("updateAccountByMap切入执行，更新数据：{}",map);
    }

}
