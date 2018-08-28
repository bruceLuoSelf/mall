package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.LogType;
import com.wzitech.gamegold.shorder.business.ISplitRepositoryLogManager;
import com.wzitech.gamegold.shorder.dao.ISplitRepositoryLogDao;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import com.wzitech.gamegold.shorder.entity.SplitRepositoryLog;
import com.wzitech.gamegold.shorder.enums.LogTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 分仓日志
 */
@Component
public class SplitRepositoryLogManagerImpl implements ISplitRepositoryLogManager {
    @Autowired
    ISplitRepositoryLogDao splitRepositoryLogDao;

    /**
     * 分页查找分仓日志
     *
     * @param map
     * @param orderBy
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public GenericPage<SplitRepositoryLog> queryListInPage(Map<String, Object> map, int start, int pageSize, String orderBy, boolean isAsc) {
        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "id";
        }

        GenericPage<SplitRepositoryLog> genericPage = splitRepositoryLogDao.selectByMap(map, pageSize,
                start, orderBy, isAsc);
        return genericPage;
    }

    /**
     * 保存库存变化日志
     *
     * @param log
     */
    @Override
    @Transactional
    public void saveLog(SplitRepositoryLog log) {
        if(log.getLogType()== LogTypeEnum.ARRANGEREPOSITORYSALED.getCode()){
            log.setLog(String.format("因分仓增加库存%s",log.getCount()));
        }
        if(log.getLogType()==LogTypeEnum.DELIVERYSALED.getCode()){
            log.setLog(String.format("因收货增加库存%s",log.getCount()));
        }
        if(log.getLogType()==LogTypeEnum.ARRANGEREPOSITORYPAID.getCode()){
            log.setLog(String.format("因分仓减少库存%s",log.getCount()));
        }
        if(log.getLogType()==LogTypeEnum.SELLERPAID.getCode()){
            log.setLog(String.format("因销售减少库存%s",log.getCount()));
        }
        splitRepositoryLogDao.insert(log);
    }

    @Override
    public void saveLogs(List<SplitRepositoryLog> splitRepositoryLogs) {
        for(SplitRepositoryLog splitRepositoryLog:splitRepositoryLogs){
            this.saveLog(splitRepositoryLog);
        }
    }

    /**
     * 保存收货日志
     * @param deliverySubOrder
     * @param logContent
     */
    @Override
    public void saveLog(DeliverySubOrder deliverySubOrder, String logContent) {
        if (deliverySubOrder == null) {
            return;
        }

        SplitRepositoryLog log = new SplitRepositoryLog();
        log.setFcId(deliverySubOrder.getId());
        log.setBuyerAccount(deliverySubOrder.getBuyerAccount());
        log.setGameName(deliverySubOrder.getGameName());
        log.setRegion(deliverySubOrder.getRegion());
        log.setServer(deliverySubOrder.getServer());
        log.setGameRace(deliverySubOrder.getGameRace());
        log.setGameAccount(deliverySubOrder.getGameAccount());
        log.setLog(logContent);
        log.setCreateTime(new Date());
        splitRepositoryLogDao.insert(log);
    }
}
