package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.shorder.business.IGameAccountManager;
import com.wzitech.gamegold.shorder.business.ISplitRepositoryRequestManager;
import com.wzitech.gamegold.shorder.dao.IGameAccountDBDAO;
import com.wzitech.gamegold.shorder.dao.ISplitRepoReqOrderIdRedisDao;
import com.wzitech.gamegold.shorder.dao.ISplitRepositoryRequestDao;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import com.wzitech.gamegold.shorder.entity.GameAccount;
import com.wzitech.gamegold.shorder.entity.SplitRepositoryRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分仓请求管理
 */
@Component
public class SplitRepositoryRequestManagerImpl implements ISplitRepositoryRequestManager {
    @Autowired
    private ISplitRepositoryRequestDao splitRepositoryRequestDao;

    @Autowired
    private IGameAccountDBDAO gameAccountDBDAO;


    @Autowired
    private ISplitRepoReqOrderIdRedisDao splitRepoReqOrderIdRedisDao;

    @Autowired
    private IGameAccountManager gameAccountManager;

    protected static final Logger logger = LoggerFactory.getLogger(DeliveryOrderFinishManagerImpl.class);

    /**
     * 创建分仓请求
     *
     * @param deliverySubOrder
     */
    @Transactional
    public void create(DeliverySubOrder deliverySubOrder) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("buyerAccount", deliverySubOrder.getBuyerAccount());
        params.put("gameName", deliverySubOrder.getGameName());
        params.put("region", deliverySubOrder.getRegion());
        params.put("server", deliverySubOrder.getServer());
        params.put("gameRace", deliverySubOrder.getGameRace());
        params.put("gameAccount", deliverySubOrder.getGameAccount());
        List<GameAccount> gameAccountList = gameAccountDBDAO.queryGameAccount(params);
        if (CollectionUtils.isEmpty(gameAccountList)) return;

        logger.info("创建分仓请求接口,orderId:{}, subOrderId:{}, fmsRoleName:{}",
                new Object[]{deliverySubOrder.getOrderId(), deliverySubOrder.getId(), deliverySubOrder.getGameRole()});

        for (GameAccount gameAccount : gameAccountList) {
            // 是收货角色或背包已满的不需要生成分仓请求
            if (gameAccount.getIsShRole() || gameAccount.getIsPackFull()) continue;

            String splitRepoOrderId = splitRepoReqOrderIdRedisDao.getOrderId();
            SplitRepositoryRequest splitRepositoryReq = new SplitRepositoryRequest();
            splitRepositoryReq.setOrderId(splitRepoOrderId);
            splitRepositoryReq.setGameName(gameAccount.getGameName());
            splitRepositoryReq.setRegion(gameAccount.getRegion());
            splitRepositoryReq.setServer(gameAccount.getServer());
            splitRepositoryReq.setGameRace(gameAccount.getGameRace());
            splitRepositoryReq.setGameAccount(gameAccount.getGameAccount());
            splitRepositoryReq.setFmsRoleName(deliverySubOrder.getGameRole());
            splitRepositoryReq.setGameRole(gameAccount.getRoleName());
            splitRepositoryReq.setPwd(gameAccount.getGamePwd());
            splitRepositoryReq.setSecondPwd(gameAccount.getSecondPwd());
            splitRepositoryReq.setStatus(SplitRepositoryRequest.S_WAIT_PROCESS);
            splitRepositoryReq.setBuyerAccount(gameAccount.getBuyerAccount());
            splitRepositoryReq.setTel(gameAccount.getTel());
            splitRepositoryReq.setUpdateTime(new Date());
            splitRepositoryRequestDao.insert(splitRepositoryReq);
        }
    }

    /**
     * 根据订单ID查询分仓请求
     *
     * @param orderId
     * @return
     */
    @Override
    @Transactional
    public SplitRepositoryRequest queryByOrderId(String orderId) {
        return splitRepositoryRequestDao.selectByOrderId(orderId, false);
    }

    /**
     * 分仓结果
     * @param orderId
     * @param repsitoryCount
     * @param level
     * @param fmRepsitoryCount
     * @param fmLevel
     */
    @Override
    @Transactional
    public SplitRepositoryRequest spliteResult(String orderId,Long repsitoryCount,Integer level,Long fmRepsitoryCount,Integer fmLevel){
        // 查询分仓订单
        SplitRepositoryRequest order = queryByOrderId(orderId);
        if (order == null)
            throw new SystemException(ResponseCodes.NotExistSplitRepoOrder.getCode(),
                    ResponseCodes.NotExistSplitRepoOrder.getMessage());

        // 更新被分仓角色库存和等级
        gameAccountManager.updateRepositoryCountAndLevel(order.getBuyerAccount(), order.getGameName(), order.getRegion(),
                order.getServer(), order.getGameRace(), order.getGameAccount(), order.getGameRole(),
                repsitoryCount, level);

        // 更新附魔师角色库存和等级
        gameAccountManager.updateRepositoryCountAndLevel(order.getBuyerAccount(), order.getGameName(), order.getRegion(),
                order.getServer(), order.getGameRace(), order.getGameAccount(), order.getFmsRoleName(),
                fmRepsitoryCount, fmLevel);

        return order;
    }

    /**
     * 更改分仓订单状态
     *
     * @param orderId
     * @param status
     */
    @Override
    @Transactional
    public void changeState(String orderId, int status) {
        if (StringUtils.isBlank(orderId))
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());

        if (status != SplitRepositoryRequest.S_SPLIT && status != SplitRepositoryRequest.S_FINISH) {
            throw new SystemException(ResponseCodes.IllegalArguments.getCode(), "不是一个有效的状态值");
        }

        SplitRepositoryRequest order = splitRepositoryRequestDao.selectByOrderId(orderId, true);
        if (order == null)
            throw new SystemException(ResponseCodes.NotExistSplitRepoOrder.getCode(),
                    ResponseCodes.NotExistSplitRepoOrder.getMessage());

        // 更新游戏角色状态
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("buyerAccount", order.getBuyerAccount());
        params.put("gameName", order.getGameName());
        params.put("region", order.getRegion());
        params.put("server", order.getServer());
        params.put("gameRace", order.getGameRace());
        params.put("gameAccount", order.getGameRace());
        if (status == SplitRepositoryRequest.S_SPLIT) {
            params.put("status", GameAccount.S_SPLIT);
        } else if (status == SplitRepositoryRequest.S_FINISH) {
            params.put("status", GameAccount.S_FREE);
        }
        gameAccountDBDAO.updateAccountByMap(params);
        // 更新分仓订单状态
        order.setStatus(status);
        splitRepositoryRequestDao.update(order);
    }

    /**
     * 分页查找分仓订单
     *
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<SplitRepositoryRequest> queryListInPage(Map<String, Object> map, int pageSize, int start, String orderBy, boolean isAsc) {
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
        return splitRepositoryRequestDao.selectByMap(map, pageSize, start, orderBy, isAsc);
    }

    /**
     * 新增分仓请求
     *
     * @param entity
     */
    @Override
    public void addSplitRepositoryRequest(SplitRepositoryRequest entity) {
        if (entity == null) {
            throw new SystemException("新增数据异常");
        }
        if (entity.getUpdateTime() == null) {
            entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
        splitRepositoryRequestDao.insert(entity);
    }

    @Override
    public List<SplitRepositoryRequest> selectByMap(Map<String, Object> map,String orderBy,Boolean isAsc) {
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        return splitRepositoryRequestDao.selectByMap(map, orderBy, isAsc);
    }

    /**
     * 查出所有，导出表格用
     * @param queryParam
     * @return
     */
    @Override
    public List<SplitRepositoryRequest> queryAllForExport(Map<String, Object> queryParam){
        return  splitRepositoryRequestDao.selectByMap(queryParam);
    }

}
