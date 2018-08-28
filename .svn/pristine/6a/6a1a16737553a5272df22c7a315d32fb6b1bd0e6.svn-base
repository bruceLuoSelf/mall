package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.MachineArtificialStatus;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.StockType;
import com.wzitech.gamegold.common.enums.TradeLogoEnum;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderDao;
import com.wzitech.gamegold.shorder.dao.IDeliverySubOrderDao;
import com.wzitech.gamegold.shorder.dao.IRepositorySplitRequestManager;
import com.wzitech.gamegold.shorder.dto.RobotFCRequest;
import com.wzitech.gamegold.shorder.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 340032 on 2018/1/9.
 */
@Component
public class AutomationManagerImpl implements IAutomationManager {
    protected static final Logger logger = LoggerFactory.getLogger(AutomationManagerImpl.class);
    @Autowired
    IDeliverySubOrderDao deliverySubOrderDao;

    @Autowired
    ISystemConfigManager systemConfigManager;

    @Autowired
    private IDeliveryOrderLogManager deliveryOrderLogManager;

    @Autowired
    private IDeliveryOrderManager deliveryOrderManager;

    @Autowired
    private IDeliveryOrderDao deliveryOrderDao;
    @Autowired
    private IShGameConfigManager shGameConfigManager;

    @Resource(name="syncRepositoryManager")
    ISyncRepositoryManager syncRepositoryManager;

    @Autowired
    IRepositorySplitRequestManager repositorySplitRequestManager;

    @Autowired
    ISplitRepositoryLogManager splitRepositoryLogManager;

    @Autowired
    IStockManager stockManager;



    @Override
    @Transactional
    public void automationExceptional(Long id, String OtherReason) {
        DeliverySubOrder subOrder = deliverySubOrderDao.selectByIdForUpdate(id);
        if (subOrder==null){
            throw new SystemException(ResponseCodes.NoSubOrder.getCode(),
                    ResponseCodes.NoSubOrder.getMessage());
        }
        if (subOrder.getMachineArtificialStatus() == null) {
            throw new SystemException(ResponseCodes.EmptyStatus.getCode(), ResponseCodes.EmptyStatus.getMessage());
        }
        if (subOrder.getMachineArtificialStatus() == MachineArtificialStatus.ArtificialTransferSuccess.getCode() || subOrder.getMachineArtificialStatus() == MachineArtificialStatus.ArtificialTransferFailed.getCode()) {
            throw new SystemException(ResponseCodes.MachineArtificial.getCode(), ResponseCodes.MachineArtificial.getMessage());
        }
        if (subOrder.getTradeLogo() == 0 || subOrder.getTradeLogo() == null) {
            throw new SystemException(ResponseCodes.SellerIsNull.getCode(),
                    ResponseCodes.SellerIsNull.getMessage());
        }
        //判断是邮寄还是拍卖
        if (subOrder.getTradeLogo() == TradeLogoEnum.AuctionTrade.getCode()) {
            subOrder.setOtherReason(OtherReason);
            deliverySubOrderDao.update(subOrder);
//            OrderLog orderLog = new OrderLog();
//            StringBuffer sb = new StringBuffer();
//            sb.append("子订单异常，转客服人工处理，请勿关闭本页面。");
//            orderLog.setId(subOrder.getId());
//            orderLog.setOrderId(subOrder.getOrderId());
//            orderLog.setLog(sb.toString());
//            deliveryOrderLogManager.writeLog(orderLog);
            int source = 1;
            deliveryOrderManager.autoDistributionManager(subOrder.getOrderId(),source,id);
        } else if (subOrder.getTradeLogo() == TradeLogoEnum.PostTrade.getCode()) {
            if (OtherReason.equals("未查到金币邮件")) {
                subOrder.setWaitToConfirm(true);
                subOrder.setUpdateTime(new Date());
                subOrder.setRealCount(0L);
                deliverySubOrderDao.update(subOrder);
                OrderLog orderLog = new OrderLog();
                StringBuffer sb = new StringBuffer();
                sb.append("机器已完成邮箱查询，未收到任何游戏币。。点击【无异议】，订单撤单。。。如有异议，请在10分钟点击【转人工】，客服人工介入处理。。。超时未操作将以撤单处理。");
                orderLog.setOrderId(subOrder.getOrderId());
                orderLog.setLog(sb.toString());
                deliveryOrderLogManager.writeLog(orderLog);
            } else {
                subOrder.setOtherReason(OtherReason);
                deliverySubOrderDao.update(subOrder);
                OrderLog orderLog = new OrderLog();
                StringBuffer sb = new StringBuffer();
                sb.append("【" + subOrder.getId() + "号机器人】");
                sb.append("【" + subOrder.getGameRole() + "】");
                sb.append(",子订单异常，转客服人工处理，请勿关闭本页面。");
                orderLog.setOrderId(subOrder.getOrderId());
                orderLog.setLog(sb.toString());
                deliveryOrderLogManager.writeLog(orderLog);
                DeliverySubOrder deliverySubOrder = deliverySubOrderDao.selectById(id);
                int source = 0;
                deliveryOrderManager.autoDistributionManager(deliverySubOrder.getOrderId(), source,id);
            }
        }
    }

    @Override
    @Transactional
    public void automationFinish(Long id, Long realCount,Integer tradeLogo) throws IOException {
        DeliverySubOrder subOrder =deliverySubOrderDao.selectByUniqueId(id);
        if (subOrder==null){
            throw new SystemException(ResponseCodes.NoSubOrder.getCode(),
                    ResponseCodes.NoSubOrder.getMessage());
        }
        //判断是否拍卖交易  tradeLogo=5 是拍卖交易
        if (subOrder.getTradeLogo() == TradeLogoEnum.AuctionTrade.getCode()){
            logger.info("自动化拍卖完单子订单信息:{}", id,realCount);
            DeliveryOrder deliveryOrder = deliveryOrderDao.selectByOrderId(subOrder.getOrderId());
            if (deliveryOrder==null){
                throw new SystemException(ResponseCodes.NoSubOrder.getCode(),
                        ResponseCodes.NoSubOrder.getMessage());
            }
            ShGameConfig shGameConfig = shGameConfigManager.getConfigByGameName(deliveryOrder.getGameName(), deliveryOrder.getGoodsTypeName(), null, null);
//            实际收货数量存数据库  (根据子订单预计收获数量乘与配置比例)
//            subOrder.setRealCount(shGameConfig.getPoundage().multiply(new BigDecimal(subOrder.getCount())).setScale(2, RoundingMode.DOWN).longValue());
//            //自动化传实际收货数量不做结单处理,所以重新存一个参考价字段(auctionRealCount)
//            subOrder.setAuctionRealCount(realCount);
//            deliverySubOrderDao.update(subOrder);
            Map<Long, Long> subOrdersInfos = new HashMap<Long, Long>();
            subOrdersInfos.put(id,(new BigDecimal(subOrder.getCount()).subtract(shGameConfig.getPoundage().multiply(new BigDecimal(subOrder.getCount())).setScale(2, RoundingMode.DOWN))).longValue());
            String mainOrderId = subOrder.getOrderId();
            logger.info("拍卖完单子订单移交信息:{}", subOrdersInfos,mainOrderId);
            //没有问题  直接完单
            deliveryOrderManager.handleOrderForMailDeliveryOrderMax(subOrdersInfos, mainOrderId, null, null,null);
        }else if (subOrder.getTradeLogo() == TradeLogoEnum.PostTrade.getCode()){
            logger.info("自动化邮寄完单子订单信息:{}", id,realCount);
            //获取填写数量
            if (subOrder.getShInputCount() ==0 && subOrder.getShInputCount() ==null){
                throw new SystemException(ResponseCodes.ShInputCountNotNull.getCode(),
                        ResponseCodes.ShInputCountNotNull.getMessage());
            }
//            //实际收货数量存数据库
//            subOrder.setRealCount(realCount);
//            deliverySubOrderDao.update(subOrder);
            // 阈值范围外直接转人工   阈值内 前端点击有异议(转人工)与无异议(结单(部分结单))直接跳转  waitToConfirm(true   )并存数据库一个状态job跑结单,
            BigDecimal mailRobotCriticalValue = systemConfigManager.getMailRobotCriticalValue();
            //填写数量 减 实际收货数量
            BigDecimal shInputCount=new BigDecimal(subOrder.getShInputCount());
            BigDecimal realNumber = new BigDecimal(realCount);
//        //预计收货数量
//        BigDecimal count= new BigDecimal((deliverySubOrder.getCount()));
            //阈值金额  divide除法
            DeliveryOrder deliveryOrder = deliveryOrderDao.selectByOrderIdForUpdate(subOrder.getOrderId());
            if (deliveryOrder==null){
                throw new SystemException(ResponseCodes.NoSubOrder.getCode(),
                        ResponseCodes.NoSubOrder.getMessage());
            }
            BigDecimal goldNumber = mailRobotCriticalValue.divide(deliveryOrder.getPrice(),2, RoundingMode.HALF_DOWN);
            //填写数量加阈值
            BigDecimal count = shInputCount.add(goldNumber);
            if (realNumber.compareTo(shInputCount)>=0 && realNumber.compareTo(count)<=0) {
                Map<Long, Long> subOrdersInfos = new HashMap<Long, Long>();
                subOrdersInfos.put(id, realCount);
                String mainOrderId = subOrder.getOrderId();
                //没有问题  直接完单
                deliveryOrderManager.handleOrderForMailDeliveryOrderMax(subOrdersInfos, mainOrderId, null, null,null);
                if (realNumber.compareTo(shInputCount)==0) {
                    //日志信息
                    OrderLog orderLog = new OrderLog();
                    StringBuffer sb = new StringBuffer();
                    sb.append("【" + subOrder.getId() + "号机器人"+"】");
                    sb.append("机器查收数量=您所填写的数量，且全部发货，");
                    sb.append("【" + subOrder.getGameRole() + "】");
                    sb.append("子订单交易完成。");
                    orderLog.setOrderId(subOrder.getOrderId());
                    orderLog.setLog(sb.toString());
                    deliveryOrderLogManager.writeLog(orderLog);
                }else {
                    OrderLog orderLog = new OrderLog();
                    StringBuffer sb = new StringBuffer();
                    sb.append("【" + subOrder.getId() + "号机器人"+"】");
                    sb.append("【" + subOrder.getGameRole() + "】");
                    sb.append("子订单部分结单，当前订单部分结单。");
                    orderLog.setOrderId(subOrder.getOrderId());
                    orderLog.setLog(sb.toString());
                }
            }
            //判断是否在阈值在外或者内
            else if (realNumber.compareTo(count) > 0) {
                subOrder.setOtherReason("实际收货数量大于阈值,需转人工处理!");
                deliverySubOrderDao.updateById(subOrder);
                OrderLog orderLog = new OrderLog();
                StringBuffer sb = new StringBuffer();
                sb.append("【" + subOrder.getId() + "号机器人"+"】");
                sb.append("【" + subOrder.getGameRole()+ "】");
                sb.append("，");
                sb.append("机器查收数量 > 您所填写的数量，且多余数量超出误差允许范围。");
                orderLog.setOrderId(subOrder.getOrderId());
                orderLog.setLog(sb.toString());
                deliveryOrderLogManager.writeLog(orderLog);
                Integer exceptionFromAuto = 2;
                deliveryOrderManager.subOrderAutoDistributionManager(id,exceptionFromAuto,null);
            } else {
                //如果在阈值范围内 waitToConfirm 存入一个true代表需要用户确认  false代表不需要用户确认了
                subOrder.setWaitToConfirm(true);
                subOrder.setUpdateTime(new Date());
                subOrder.setRealCount(realCount);
                deliverySubOrderDao.updateById(subOrder);
                logger.info("有异议无异议订单当前数据:{}",subOrder);
                if (realCount == 0) {
                    //【乙乙乙乙】1个子订单交易异常，你可以点击【无异议】或【转人工】，超时撤单
                    OrderLog orderLog = new OrderLog();
                    StringBuffer sb = new StringBuffer();
                    sb.append("【" + subOrder.getId() + "号机器人" + "】");
                    sb.append("【" + subOrder.getGameRole() + "】");
                    sb.append("机器已经完成邮箱查询,未收到任何游戏币。。点击下方【无异议】，子订单撤单；如有异议，请在10分钟点击下方【转人工】，客服人工介入处理。。。超时未操将以撤单处理。");
                    orderLog.setOrderId(subOrder.getOrderId());
                    orderLog.setLog(sb.toString());
                    deliveryOrderLogManager.writeLog(orderLog);
                } else {
                //【乙乙乙乙】1个子订单交易异常，你可以点击【无异议】或【转人工】，超时部分部分结单
                OrderLog orderLog = new OrderLog();
                StringBuffer sb = new StringBuffer();
                sb.append("【" + subOrder.getId() + "号机器人" + "】");
                sb.append("【" + subOrder.getGameRole() + "】");
                sb.append("机器查收数量 < 您所填写的数量。。点击下方【无异议】，以" + realCount + "万金部分结单；如有异议，请在10分钟点击下方【转人工】，客服人工介入处理。。。超时未操作以" + realCount + "万金部分结单。");
                orderLog.setOrderId(subOrder.getOrderId());
                orderLog.setLog(sb.toString());
                deliveryOrderLogManager.writeLog(orderLog);
            }

            }
        }


    }

    @Override
    @Transactional
    public void modifyInventory(RobotFCRequest params){
        //自动化传金转为万金
        Long packageCount = params.getPackageCount() / 10000L;
        Long repertoryCount = params.getRepertoryCount() / 10000L;
        Stock skock=new Stock();
        skock.setStockType(params.getStockType());
        skock.setPackageCount(packageCount);
        skock.setRepertoryCount(repertoryCount);
        skock.setGameName(params.getGameName());
        skock.setRegionName(params.getRegionName());
        skock.setServerName(params.getServerName());
        skock.setRaceName(params.getRaceName());
        skock.setRoleName(params.getRoleName());
        skock.setGameAccount(params.getGameAccount());
        skock.setCreateTime(new Date());
        skock.setOrderId(params.getOrderId());
        skock.setSubOrderId(params.getSubOrderId());
        //插入盘库存
        stockManager.add(skock);
        syncRepositoryManager.modifyGameAccountStockCountByInfo(params.getStockType(),params.getOrderId(),null,params.getGameAccount(),params.getLoginAccount(),params.getGameName(),params.getRegionName(),params.getServerName(),null,params.getRoleName(),null,packageCount);
        if (params.getStockType()== StockType.Delivery.getCode()){
            //调生成子订单方法
            repositorySplitRequestManager.createRepositorySplitOrder(params.getOrderId(),params.getSubOrderId());
        }

    }
}
