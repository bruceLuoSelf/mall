package com.wzitech.gamegold.store.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.enums.DeliveryOrderGTRType;
import com.wzitech.gamegold.common.enums.MachineArtificialStatus;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.ShServiceType;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.dao.IDeliverySubOrderDao;
import com.wzitech.gamegold.shorder.entity.*;
import com.wzitech.gamegold.store.business.IShStoreManager;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 库存管理(收货发起)
 * Created by 汪俊杰 on 2017/1/8.
 */
@Component
public class ShStoreManagerImpl implements IShStoreManager {
    private static final Logger logger = LoggerFactory.getLogger(ShStoreManagerImpl.class);
    @Autowired
    private IGameAccountManager gameAccountManager;
    @Autowired
    private IRepositoryManager repositoryManager;
    @Autowired
    private IDeliveryOrderFinishManager deliveryOrderFinishManager;
    @Autowired
    private IDeliveryOrderManager deliveryOrderManager;
    @Autowired
    private ISplitRepositoryRequestManager splitRepositoryRequestManager;
    @Autowired
    private IUserInfoManager userInfoManager;
    @Autowired
    private IDeliverySubOrderDao deliverySubOrderDao;
    @Autowired
    private IMachineArtificialConfigManager machineArtificialConfigManager;
    @Autowired
    private IDeliveryOrderLogManager deliveryOrderLogManager;
    @Autowired
    private IPurchaseOrderManager purchaseOrderManager;

//    /**
//     * 交易完成接口，供收货机器人调用
//     *
//     * @param mainOrderId  主订单号
//     * @param subOrderId   子订单号
//     * @param gtrType      类型
//     *                     <ul>
//     *                     <li>100：交易完成</li>
//     *                     <li>101：修改收货数量</li>
//     *                     <li>200：需人工介入</li>
//     *                     <li>300：其他情况</li>
//     *                     <li>301：交易超时</li>
//     *                     <li>302：背包已满</li>
//     *                     <li>303：不是附魔师</li>
//     *                     </ul>
//     * @param receiveCount 总共收到数量
//     */
//    @Override
//    @Transactional
//    public void complete(String mainOrderId, Long subOrderId, String gtrType, Integer backType, Long receiveCount, String remark, Boolean offline) throws IOException {
//        GameAccount gameAccount = deliveryOrderFinishManager.complete(mainOrderId, subOrderId, gtrType, backType, receiveCount, remark, offline);
//
//        if (gameAccount != null) {
//            //更新商城库存
//            repositoryManager.updateRepositoryCount(gameAccount.getBuyerAccount(), gameAccount.getGameAccount(), gameAccount.getRoleName(), gameAccount.getGameName(), gameAccount.getRegion(), gameAccount.getServer(), gameAccount.getGameRace(), gameAccount.getRepositoryCount(), ServicesContants.GOODS_TYPE_GOLD);
//        }
//    }

    /**
     * 人工介入
     *
     * @param mainOrderId
     * @param subOrderId
     * @param gtrType
     * @param backType
     * @param receiveCount
     * @param remark
     */
    @Override
    @Transactional
    public void manual(String mainOrderId, Long subOrderId, String gtrType, Integer backType, Long receiveCount, String remark, Boolean offline) {
        GameAccount gameAccount = deliveryOrderFinishManager.manual(mainOrderId, subOrderId, gtrType, backType, receiveCount, remark, offline);

        if (gameAccount != null) {
            //更新商城库存
            repositoryManager.updateRepositoryCount(gameAccount.getBuyerAccount(), gameAccount.getGameAccount(), gameAccount.getRoleName(), gameAccount.getGameName(), gameAccount.getRegion(), gameAccount.getServer(), gameAccount.getGameRace(), gameAccount.getRepositoryCount(), ServicesContants.GOODS_TYPE_GOLD);
        }
    }

    /**
     * 人工完单
     *
     * @param mainOrderId             出货主订单号
     * @param subOrderReceiveCountMap 每个子订单收到的实际数量
     *                                <p>格式：</p>
     *                                <li>key=子订单ID</li>
     *                                <li>value=实际收到的数量</li>
     */
    @Override
    @Transactional
    public void manualFinishOrder(String mainOrderId, Map<Long, Long> subOrderReceiveCountMap) throws IOException {
        List<GameAccount> gameAccountList = deliveryOrderManager.manualFinishOrder(mainOrderId, subOrderReceiveCountMap, false);

//        if (gameAccountList != null && gameAccountList.size() > 0) {
//            for (GameAccount gameAccount : gameAccountList) {
//                //更新商城库存
//                repositoryManager.updateRepositoryCount(gameAccount.getBuyerAccount(), gameAccount.getGameAccount(), gameAccount.getRoleName(), gameAccount.getGameName(), gameAccount.getRegion(), gameAccount.getServer(), gameAccount.getGameRace(), gameAccount.getRepositoryCount(), ServicesContants.GOODS_TYPE_GOLD);
//            }
//        }
    }

    /**
     * 分仓结果
     *
     * @param orderId
     * @param repsitoryCount
     * @param level
     * @param fmRepsitoryCount
     * @param fmLevel
     */
    @Override
    @Transactional
    public void spliteResult(String orderId, Long repsitoryCount, Integer level, Long fmRepsitoryCount, Integer fmLevel) {
        SplitRepositoryRequest order = splitRepositoryRequestManager.spliteResult(orderId, repsitoryCount, level, fmRepsitoryCount, fmLevel);

        if (order != null) {
            //更新商城库存
            if (repsitoryCount > 0) {
                repositoryManager.updateRepositoryCount(order.getBuyerAccount(), order.getGameAccount(), order.getGameRole(), order.getGameName(), order.getRegion(), order.getServer(), order.getGameRace(), repsitoryCount, ServicesContants.GOODS_TYPE_GOLD);
            }

            if (fmRepsitoryCount > 0) {
                repositoryManager.updateRepositoryCount(order.getBuyerAccount(), order.getGameAccount(), order.getFmsRoleName(), order.getGameName(), order.getRegion(), order.getServer(), order.getGameRace(), fmRepsitoryCount, ServicesContants.GOODS_TYPE_GOLD);
            }
        }
    }

    /**
     * 自动分配物服
     * <p>
     * ZW_C_JB_00004 sunyang
     *
     * @param orderId
     */
    /*@Override
    @Transactional
    public void voluntarilyRC(String orderId, Long id, String serviceId, String remark, Boolean offline, Integer type, Long receiveCount) {

        //根据订单id取查询出货单
        DeliveryOrder deliveryOrder = deliveryOrderManager.selectByOrderIdForUpdate(orderId);

        logger.info("自动分配物服DeliveryOrder:{}", deliveryOrder);
        if (deliveryOrder == null) {
            throw new SystemException(ResponseCodes.OrderLogIdInvalid.getCode()
                    , ResponseCodes.OrderLogIdInvalid.getMessage());
        }

        //根据出货单中的游戏名取获取分配物服开关
        if (StringUtils.isNotBlank(deliveryOrder.getGameName())) {
            Boolean isEnable = machineArtificialConfigManager.queryenableByGameName
                    (deliveryOrder.getGameName());
            logger.info("自动分配物服流程开关isEnable:{}", isEnable);
            if (!isEnable) {
                if (serviceId.equals(ShServiceType.CANCEL.getCode())) {
                    //没有启用开关  走老流程撤单
                    deliveryOrderFinishManager.cancel(orderId, id, serviceId,
                            type, remark, offline);
                } else if (serviceId.equals(ShServiceType.MANUAL_INTERVENTION.getCode())) {
                    manual(orderId, id, serviceId,
                            type, receiveCount, remark, offline);
                }
                return;
            }
        }
        //获取单前订单量最小的物服信息
        UserInfoEO userInfoEO = null;
        List<UserInfoEO> userInfoEOs = userInfoManager.selectFreeConsignmentService();
        if (CollectionUtils.isNotEmpty(userInfoEOs)) {
            userInfoEO = userInfoEOs.get(0);
        } else {
            //设置出货单 转人工状态(失败)
            throw new SystemException(ResponseCodes.GetRcUserLose.getCode(),
                    ResponseCodes.GetRcUserLose.getMessage());
        }
        //设置接手物服 id
        deliveryOrder.setTakeOverSubjectId(userInfoEO.getLoginAccount());
        //设置接手物服姓名
        deliveryOrder.setTakeOverSubject(userInfoEO.getRealName());
        //机器转物服的原因
        deliveryOrder.setMachineArtificialReason(DeliveryOrderGTRType.getTypeByCode(type).getMessage());
        //机器转物服的时间
        deliveryOrder.setMachineArtificialTime(new Date());
        //机器转人工状态
        deliveryOrder.setMachineArtificialStatus(MachineArtificialStatus.ArtificialTransferSuccess.getCode());
        deliveryOrderManager.update(deliveryOrder);
        logger.info("自动分配物服流程主订单更新成功:{}", deliveryOrder);

        List<DeliverySubOrder> deliverySubOrders = deliverySubOrderDao.queryWaitForTradeOrders(deliveryOrder.getCgId(), true);
        DeliverySubOrder deliverySubOrderNew = null;
        if (CollectionUtils.isNotEmpty(deliverySubOrders)) {
            deliverySubOrderNew = deliverySubOrders.get(0);
            *//**//*//**//*//*判断子订单与主订单订单数的配单逻辑
            if (deliveryOrder.getCount() > deliverySubOrderNew.getCount()) {
                //子订单数与主订单数不一样
                boolean b = purchaseOrderManager.updatePurchaseOrderCount(deliverySubOrderNew.getBuyerAccount(), deliverySubOrderNew.getGameName(), deliverySubOrderNew.getRegion(), deliverySubOrderNew.getServer(), deliverySubOrderNew.getGameRace(), deliverySubOrderNew.getGameAccount(), deliverySubOrderNew.getGameRole(), deliveryOrder.getCount() - deliverySubOrderNew.getCount(), GameAccount.S_RECEIVING, false, deliveryOrder.getOrderId());
                if(b){
                    deliverySubOrderNew.setCount(deliveryOrder.getCount());
                }
            }*//**//*

            OrderLog orderLog = new OrderLog();
            orderLog.setCreateTime(new Date());
            orderLog.setOrderId(deliveryOrder.getOrderId());
            orderLog.setType(OrderLog.TYPE_INNER);
            orderLog.setLog("系统将该异常收货订单分配给物服【" + userInfoEO.getLoginAccount() + "】");
            deliveryOrderLogManager.writeLog(orderLog);
            orderLog.setType(OrderLog.TYPE_NORMAL);
            orderLog.setLog("由于" + (DeliveryOrderGTRType.getTypeByCode(type).getMessage() == null ? "其他原因" : DeliveryOrderGTRType.getTypeByCode(type).getMessage()) + "，无法完成自动收货。5173客服即将登录收货账号手动手货，请勿关闭本页面。刷新页面显示最新出货提示。");
            deliveryOrderLogManager.writeLog(orderLog);
            //设置接手物服 id
            deliverySubOrderNew.setTakeOverSubjectId(userInfoEO.getLoginAccount());
            //设置接手物服姓名
            deliverySubOrderNew.setTakeOverSubject(userInfoEO.getRealName());
            //机器转物服的原因
            deliverySubOrderNew.setMachineArtificialReason(DeliveryOrderGTRType.getTypeByCode(type).getMessage());
            //机器转物服的时间
            deliverySubOrderNew.setMachineArtificialTime(new Date());
            //机器转人工状态
            deliverySubOrderNew.setMachineArtificialStatus(MachineArtificialStatus.ArtificialTransferSuccess.getCode());
            deliverySubOrderDao.update(deliverySubOrderNew);

            logger.info("自动分配物服流程子订单更新成功:{}", deliverySubOrderNew);
        } else {
            //手动生成子订单
            deliveryOrderManager.createSubOrderByAutoMateError(deliveryOrder.getId(), null);
        }
    }*/
}

