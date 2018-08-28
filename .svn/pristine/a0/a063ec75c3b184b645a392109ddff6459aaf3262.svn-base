
package com.wzitech.gamegold.shorder.business.impl;

import com.google.common.collect.Maps;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.*;
import com.wzitech.gamegold.common.expection.NotEnoughRepertoryException;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.dao.*;
import com.wzitech.gamegold.shorder.entity.*;
import com.wzitech.gamegold.usermgmt.dao.rdb.IUserInfoDBDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

import static com.wzitech.gamegold.common.enums.TradeLogoEnum.AuctionTrade;

/**
 * 自动配单
 *
 * @author yemq
 */
@Component
public class DeliveryOrderAutoConfigManagerImpl implements IDeliveryOrderAutoConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryOrderAutoConfigManagerImpl.class);

    @Autowired
    private IPurchaseOrderManager purchaseOrderManager;

    @Autowired
    private IGameAccountManager gameAccountManager;

    @Autowired
    private IDeliveryOrderDao deliveryOrderDao;

    @Autowired
    private IDeliverySubOrderDao deliverySubOrderDao;

    @Autowired
    private IDeliveryOrderLogManager deliveryOrderLogManager;

    @Autowired
    private IMachineArtificialConfigDao machineArtificialConfigDao;

    @Autowired
    private IPurchaseOrderDBDAO purchaseOrderDBDAO;


    @Autowired
    private IAutomaticQueuingRedis automaticQueuingRedis;

    @Autowired
    private IRobotImgDAO robotImgDAO;

    @Autowired
    ISystemConfigManager systemConfigManager;

    @Autowired
    private ILevelCarryLimitManager levelCarryLimitManager;

    @Autowired
    private IShGameConfigManager shGameConfigManager;

    @Autowired
    private IUserInfoDBDAO userInfoDBDAO;

    //保存已转人工的账号，在每一次分配账号是初始化
    private Set<String> artificialTransferSet;


    @Autowired
    private ISplitRepositoryRequestDao splitRepositoryRequestDao;

    /**
     * 给订单配单,提供自动配单任务调用
     *
     * @param id
     */
    @Transactional
    public void autoConfigOrderReady(long id) {
        DeliveryOrder order = deliveryOrderDao.selectByIdForUpdate(id);
        logger.info("自动配单开始，订单号:{}", order.getOrderId());
        //当前面有订单在排队状态直接转入排队中
        //申诉单逻辑处理单逻辑
        if (StringUtils.isBlank(order.getAppealOrder())) {
            boolean wait = deliverySubOrderDao.selectByAccountInqueue(order);
            if (wait) {
                //进入排队逻辑 1.更新主订单状态 2.加入排队队列
                changeState(order.getId(), DeliveryOrderStatus.INQUEUE.getCode());
                automaticQueuingRedis.pushOrderId(order.getOrderId());
                return;
            }
            this.autoConfigOrder(order);
        } else {
            this.autoConfigAppealOrder(order);
        }
    }

    /**
     * 配单Appeal提取方法 供job调用
     *
     * @param order
     */
    public void autoConfigAppealOrder(DeliveryOrder order) {
        DeliverySubOrder deliverySubOrder = deliverySubOrderDao.selectById(Long.parseLong(order.getAppealOrder()));

        //步骤 1.查询可以生成的订单账号 2.用账号去创建子订单 3。角色表扣除数量逻辑修改 4。不能生产的情况下加入排队状态
        Boolean canSh = gameAccountManager.queryOnlyGameAccountCanSh(deliverySubOrder);
        if (canSh) {
            //调用胜诉单用的生成订单方法
            configByCreateSubOrderAndGameAccount(order, deliverySubOrder, order.getCount());
            //改变主订单订单号
            changeState(order.getId(), DeliveryOrderStatus.TRADING.getCode());
            return;
        }
        changeState(order.getId(), DeliveryOrderStatus.INQUEUE.getCode());
        automaticQueuingRedis.pushOrderId(order.getOrderId());
    }

    /**
     * 配单提取方法 供job调用
     *
     * @param order
     */
    @Transactional
    public void autoConfigOrder(DeliveryOrder order) {
        order = deliveryOrderDao.selectByOrderIdForUpdate(order.getOrderId());
        if (order.getStatus() != DeliveryOrderStatus.WAIT_TRADE.getCode() && order.getStatus() != DeliveryOrderStatus.INQUEUE.getCode()) {
            return;
        }
        PurchaseOrder purchaseOrder = purchaseOrderDBDAO.selectByIdForUpdate(order.getCgId());
        // 出货数量不能大于采购数量
        if (purchaseOrder != null && order.getCount().compareTo(purchaseOrder.getCount()) > 0) {
            throw new NotEnoughRepertoryException();
        }
        //查询游戏对应的收货空闲账号数量
        long canShcount = gameAccountManager.queryGameAccountCanShCountSum(order);
        if (order.getCount().compareTo(canShcount) > 0) {
            //数量不够进入排队状态
            changeState(order.getId(), DeliveryOrderStatus.INQUEUE.getCode());
            automaticQueuingRedis.pushOrderId(order.getOrderId());
            return;
        }
        // 写入订单日志
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(order.getOrderId());
        orderLog.setType(OrderLog.TYPE_NORMAL);
        if (order.getQueueStartTime() != null) {
            if (TradeLogoEnum.PostTrade.getCode() == order.getTradeLogo()) {
                orderLog.setLog("排队已结束，收货角色信息已显示在下方表格中。请登录游戏，通过" + TradeLogoEnum.getTypeByCode(order.getTradeLogo()).getType() + "方式发货。");
            } else if (TradeLogoEnum.AuctionTrade.getCode() == order.getTradeLogo()) {
                orderLog.setLog("排队已结束，收货角色、等级、拍卖价格已显示在下方表格中。");
            }
            deliveryOrderLogManager.writeLog(orderLog);
        }
        //判出条件处理完 开始配单
        List<GameAccount> gameAccounts = gameAccountManager.queryGameAccountCanSh(order);
        //保存游戏账号 保证唯一性
        //调用游戏配置开关
        List<String> list = new ArrayList<String>();
        this.configGameAccountSwitch(order, gameAccounts,list);
        //用account 批量更新游戏账号状态为交易中xxx yyy
        //扣除purchaseOrder的数量
        purchaseOrder.setCount(purchaseOrder.getCount() - order.getCount());
        purchaseOrderDBDAO.update(purchaseOrder);
        //更新主订单状态
        changeState(order.getId(), DeliveryOrderStatus.TRADING.getCode());

        if (TradeLogoEnum.PostTrade.getCode() == order.getTradeLogo()) {
            orderLog.setLog("请在30分钟内完成邮寄，并点击下方【已发货】按钮，输入实际邮寄数量。超时自动撤单。");
            deliveryOrderLogManager.writeLog(orderLog);
            orderLog.setLog("注：实际收货数量不可超过预计收货数量。邮费由卖家承担。");
            deliveryOrderLogManager.writeLog(orderLog);
        }
        //机器收货，创建订单后，在分仓订单表中查询收货游戏帐号是否在分仓中，如在分仓中，进行提示
        if (order.getDeliveryType() == DeliveryTypeEnum.Robot.getCode() && !CollectionUtils.isEmpty(list)) {
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("gameName",order.getGameName());
            map.put("region",order.getRegion());
            map.put("server",order.getServer());
            if (StringUtils.isNotBlank(gameAccounts.get(0).getBuyerAccount())) {
                map.put("buyerAccount",gameAccounts.get(0).getBuyerAccount());
            }
            map.put("list",list);
            int count = splitRepositoryRequestDao.countByAccountList(map);
            if (count > 0) {
                orderLog.setLog("当前收货商帐号繁忙，等待帐号空闲。");
                deliveryOrderLogManager.writeLog(orderLog);
            }
        }
        return;
    }

    /**
     * 邮寄交易配置游戏账号
     *
     * @param order
     * @param gameAccounts
     */
    private void configGameAccountByPostTrade(DeliveryOrder order, List<GameAccount> gameAccounts,List<String> list) {
        long configCount = order.getCount();
        for (int i = 0; i < gameAccounts.size(); i++) {
            GameAccount gameAccount = gameAccounts.get(i);
            if (gameAccount.getCount() >= configCount) {
                this.configByCreateSubOrderAndGameAccountSubtractCount(order, gameAccount, configCount,list);
                break;
            }
            configCount -= gameAccount.getCount();
            this.configByCreateSubOrderAndGameAccountSubtractCount(order, gameAccount, gameAccount.getCount(),list);
        }
    }

    /**
     * 拍卖交易配置游戏账号
     *
     * @param order
     * @param gameAccounts
     */
    private void configGameAccountByAuctionTrade(DeliveryOrder order, List<GameAccount> gameAccounts,List<String> list) {
        long configCount = order.getCount();
        this.createArtificialTransferList();
        for (int i = 0; i < gameAccounts.size(); i++) {
            GameAccount gameAccount = gameAccounts.get(i);
            if (gameAccount.getCount() >= configCount) {
                this.auctionTradeCreateSubOrder(order, gameAccount, configCount,list);
                break;
            }
            configCount -= gameAccount.getCount();
            this.auctionTradeCreateSubOrder(order, gameAccount, gameAccount.getCount(),list);
        }
    }

    private void createArtificialTransferList() {
        this.artificialTransferSet = new HashSet<String>();
    }

    /**
     * @param order
     * @param gameAccount
     * @param configCount 机器拍卖收货逻辑
     *                    1.判断出货商等级,获取出货上限
     *                    2.取出每一笔收货角色收货数量 除以 出货商出货上限 计算出出货需要的次数
     *                    3.计算出平均每一次出货数量
     *                    4.生成 出货次数笔 子订单
     */
    private void auctionTradeCreateSubOrder(DeliveryOrder order, GameAccount gameAccount, long configCount,List<String> list) {
        //计算出货需要次数
        long canCount = levelCarryLimitManager.selectCarryUpperLimit(order.getSellerRoleLevel(), order.getGameName(), order.getGoodsType());
        //对于收货数量-1万操作 作为 随机数 的保留
        double num = Math.ceil((double) configCount / (canCount - 1L));
        if (num <= 1) {
            configByCreateSubOrderAndGameAccountSubtractCount(order, gameAccount, configCount,list);
        } else {
            //防止有小数的情况下，向下取整
            long avgCount = (long) Math.floor(configCount / num);
            for (; num > 0; num--) {
                configByCreateSubOrderAndGameAccountSubtractCount(order, gameAccount, avgCount,list);
            }
        }
    }

    /**
     * 配置游戏角色
     *
     * @param order
     * @param gameAccounts
     */
    private void configGameAccountSwitch(DeliveryOrder order, List<GameAccount> gameAccounts,List<String> list) {
        switch (TradeLogoEnum.getTypeByCode(order.getTradeLogo())) {
            case AuctionTrade:
                this.configGameAccountByAuctionTrade(order, gameAccounts,list);
                break;
            case PostTrade:
                this.configGameAccountByPostTrade(order, gameAccounts,list);
                break;
        }
    }

    /**
     * 配单
     *
     * @param order
     * @param needCount 需要分配多少数量
     * @return long 返回创建的配单总游戏币数量
     */
    @Transactional
    public long configOrder(DeliveryOrder order, long needCount) {

        // 查询收货方可以分配的游戏角色账号,排序按账号空闲状态，同个游戏账号，收货数量多的靠前规则
        Map<String, Object> params = Maps.newHashMap();
        params.put("buyerAccount", order.getBuyerAccount());
        params.put("gameName", order.getGameName());
        params.put("region", order.getRegion());
        params.put("server", order.getServer());
        params.put("gameRace", order.getGameRace());
        params.put("orderBy", "status asc,game_account asc,count desc");
        params.put("isLock", true);

        List<GameAccount> gameAccountList;
        MachineArtificialConfig machineArtificialConfig = machineArtificialConfigDao.selectMachineArtificialConfigByName(order.getGameName());
        logger.info("配单，machineArtificialConfig:{}", machineArtificialConfig);
        //游戏对应的转人工配置为禁用则走自动化老流程启用则走新流程
        gameAccountList = gameAccountManager.queryGameAccountCanSh(order);
        logger.info("配单老流程，gameAccountList:{}", gameAccountList);
        if (CollectionUtils.isEmpty(gameAccountList)) {
            return 0L;
        }

        //start ZW_C_JB_00004 mj
        // 分配收货角色   若有一个收货角色的数量大于订单所需要数量则直接分配该
        // 收货角色；若都不满足则取出第一个收货角色，将其信息更新到子订单中，订
        // 单需要的收货数量由所有收货角色的数量凑足。
        long configCount = 0; // 总共配置了多少数量
        GameAccount gameAccount = null;
        if (gameAccountList == null || gameAccountList.size() == 0) {
            return configCount;
        }
        for (int i = 0; (i < gameAccountList.size()) && (needCount > 0); i++) {
            GameAccount entity = gameAccountList.get(i);
            if (entity.getCount() == null || entity.getCount().intValue() == 0) {
                continue;
            }
            //判断色的收货数量收货角有大于订单需要的数量则直接取此收货角色
            if (entity.getCount().longValue() >= needCount) {
                configCount = needCount;
                gameAccount = entity;
                break;
            } else {
                configCount += entity.getCount();
            }
        }
//        //判断所有收货角色的收货数量总和必须大于订单需要的数量
//        if (configCount < needCount) {
//            throw new NotEnoughRepertoryException();
//        }
        //收货角色的收货数量没有大于订单需要的数量则取出第一条收货角色
        if (gameAccount == null) {
            gameAccount = gameAccountList.get(0);
        }
        // 创建出货子订单
        if (createSubOrder(order, gameAccount, needCount)) {
            order.setTakeOverSubjectId(DeliveryOrderFinishManagerImpl.RCNAME);
            logger.info("创建出货子订单，account:{}", gameAccount);
            logger.info("创建出货子订单，createTotalCount:{}", needCount);
            return needCount;
        } else {
            configCount = 0;
            return configCount;
        }
    }

    /**
     * 创建子订单，同时预扣采购单收货数量和收货角色的收货数量
     *
     * @param order
     * @param account
     * @param configCount
     * @return boolean 是否创建成功
     */
    public boolean createSubOrder(DeliveryOrder order, GameAccount account, long configCount) {
        logger.info("在订单号:" + order + "中配单：{}", configCount);
        // 构建子订单
        DeliverySubOrder subOrder = new DeliverySubOrder();
        subOrder.setChId(order.getId());
        subOrder.setOrderId(order.getOrderId());
        subOrder.setSellerUid(order.getSellerUid());
        subOrder.setSellerAccount(order.getSellerAccount());
        subOrder.setBuyerAccount(order.getBuyerAccount());
        subOrder.setBuyerUid(order.getBuyerUid());
        subOrder.setGameName(order.getGameName());
        subOrder.setRegion(order.getRegion());
        subOrder.setServer(order.getServer());
        subOrder.setGameRace(order.getGameRace());
        subOrder.setSellerRoleName(order.getRoleName());
        subOrder.setWords(order.getWords());
        subOrder.setAddress(order.getAddress());
        subOrder.setGameAccount(account.getGameAccount());
        subOrder.setGamePwd(account.getGamePwd());
        subOrder.setGameRole(account.getRoleName());
        subOrder.setSecondPwd(account.getSecondPwd());
        subOrder.setCount(configCount);
        subOrder.setRealCount(0L);
        subOrder.setStatus(DeliveryOrderStatus.TRADING.getCode());
        subOrder.setBuyerTel(account.getTel());
        subOrder.setMoneyName(order.getMoneyName());
        subOrder.setCreateTime(new Date());
        subOrder.setTakeOverSubjectId(order.getTakeOverSubjectId());
        subOrder.setTakeOverSubject(order.getTakeOverSubject());
        subOrder.setMachineArtificialTime(order.getMachineArtificialTime());
        subOrder.setMachineArtificialReason(order.getMachineArtificialReason());
        subOrder.setMachineArtificialStatus(order.getMachineArtificialStatus());
        subOrder.setDeliveryType(order.getDeliveryType());
        subOrder.setGameAccountId(account.getId());

        // 获取采购单信息
        PurchaseOrder purchaseOrder = purchaseOrderDBDAO.selectById(order.getCgId());
        // 出货数量不能大于采购数量
        if (purchaseOrder != null && order.getCount() > purchaseOrder.getCount().longValue())
            throw new NotEnoughRepertoryException();

        // 预扣收货角色和采购单收货数量
        boolean isSuccess = purchaseOrderManager.updatePurchaseOrderCount(order.getBuyerAccount(), order.getGameName(),
                order.getRegion(), order.getServer(), order.getGameRace(), subOrder.getGameAccount(),
                subOrder.getGameRole(), subOrder.getCount(), GameAccount.S_RECEIVING, false, order.getOrderId());

        if (isSuccess) {
            // 添加子订单
            deliverySubOrderDao.insert(subOrder);
            return true;
        }
        return false;
    }

    /**
     * 创建子订单，同时预扣采购单收货数量和收货角色的收货数量
     *
     * @param order
     * @param account
     * @param configCount
     * @return boolean 是否创建成功
     */
    public boolean configByCreateSubOrderAndGameAccountSubtractCount(DeliveryOrder order, GameAccount account, long configCount,List<String> list) {
        list.add(account.getGameAccount());
        logger.info("在订单号:" + order + "中配单：{}", configCount);
        // 构建子订单
        DeliverySubOrder subOrder = new DeliverySubOrder();
        subOrder.setChId(order.getId());
        subOrder.setOrderId(order.getOrderId());
        subOrder.setSellerUid(order.getSellerUid());
        subOrder.setSellerAccount(order.getSellerAccount());
        subOrder.setBuyerAccount(order.getBuyerAccount());
        subOrder.setBuyerUid(order.getBuyerUid());
        subOrder.setGameName(order.getGameName());
        subOrder.setRegion(order.getRegion());
        subOrder.setServer(order.getServer());
        subOrder.setGameRace(order.getGameRace());
        subOrder.setSellerRoleName(order.getRoleName());
        subOrder.setWords(order.getWords());
        subOrder.setAddress(order.getAddress());
        subOrder.setGameAccount(account.getGameAccount());
        subOrder.setGamePwd(account.getGamePwd());
        subOrder.setGameRole(account.getRoleName());
        subOrder.setSecondPwd(account.getSecondPwd());
        subOrder.setCount(configCount);
//        subOrder.setRealCount(0L);
        subOrder.setStatus(DeliveryOrderStatus.TRADING.getCode());
        subOrder.setBuyerTel(account.getTel());
        subOrder.setMoneyName(order.getMoneyName());
        subOrder.setCreateTime(new Date());
        subOrder.setTakeOverSubjectId(order.getTakeOverSubjectId());
        subOrder.setTakeOverSubject(order.getTakeOverSubject());
        subOrder.setMachineArtificialTime(order.getMachineArtificialTime());
        subOrder.setMachineArtificialReason(order.getMachineArtificialReason());
        subOrder.setMachineArtificialStatus(order.getMachineArtificialStatus());
        subOrder.setDeliveryType(order.getDeliveryType());
        subOrder.setRoleLevel(account.getLevel());
        subOrder.setTradeType(order.getTradeType());
        subOrder.setTradeLogo(order.getTradeLogo());
        subOrder.setGameAccountId(account.getId());
        //是拍卖交易的情况下需要添加末四位数
        if (subOrder.getTradeLogo() == AuctionTrade.getCode()) {
            //取出1-9999之间
            subOrder.setAfterFour((int) (Math.random() * 8999 + 1000));
            //判断订单是否转人工
            if ((new BigDecimal(1).
                    subtract(shGameConfigManager.getPoundage(order.getGameName(), order.getGoodsType().toString())).
                    multiply(new BigDecimal(configCount))).
                    compareTo(new BigDecimal(levelCarryLimitManager.selectCarryUpperLimit(account.getLevel(), order.getGameName(), order.getGoodsType()))) > 0) {
                logger.info("订单转人工订单号：" + order.getOrderId());
                //将已经转人工的订单设置到set中
                this.configFreeConsignmentService(subOrder, account.getLevel());
                artificialTransferSet.add(account.getGameAccount());
            } else if (artificialTransferSet.contains(account.getGameAccount())) {
                logger.info("订单转人工订单号：" + order.getOrderId());
                this.configFreeConsignmentService(subOrder, account.getLevel());
            } else {
                subOrder.setMachineArtificialStatus(MachineArtificialStatus.ArtificialAuto.getCode());
            }
        }

        account.setStatus(GameAccount.S_RECEIVING);
        account.setCount(account.getCount() - configCount);
        // 添加子订单 更新配单表
        account.setUpdateTime(new Date());
        gameAccountManager.updateGameAccountByConfig(account);
        deliverySubOrderDao.insert(subOrder);

        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(order.getOrderId());
        if (subOrder.getTradeLogo() == AuctionTrade.getCode()) {
            if (subOrder.getMachineArtificialStatus() == MachineArtificialStatus.ArtificialTransferSuccess.getCode()) {
                orderLog.setType(OrderLog.TYPE_NORMAL);
                orderLog.setLog("【" + subOrder.getId() + "号机器人】" + "由于收货商等级为" + account.getLevel() + "级，拍卖成功后数量可能会超过其携带上限，订单转人工处理");
                orderLog.setSubId(subOrder.getId());
                deliveryOrderLogManager.writeLog(orderLog);
            } else {
                orderLog.setType(OrderLog.TYPE_NORMAL);
                orderLog.setLog("【" + subOrder.getId() + "号机器人】" + "触发机器登录收货商账号。请耐心等候机器处理订单。");
                deliveryOrderLogManager.writeLog(orderLog);
            }
        }
        return true;
    }

    /**
     * 为子订单分配免费物服
     * 转人工使用
     *
     * @param subOrder
     */
    private void configFreeConsignmentService(DeliverySubOrder subOrder, Integer level) {
        //查询订单量最少的物服
        List<UserInfoEO> list = userInfoDBDAO.selectFreeConsignmentService();
        if (list == null || list.size() < 1) {
            logger.info("当前没有物服在线，分配失败！");
            throw new SystemException(ResponseCodes.NullConsignmentService.getCode(),
                    ResponseCodes.NullConsignmentService.getMessage());
        }
        UserInfoEO user = list.get(0);
        subOrder.setOtherReason("由于收货商等级为" + level + "级，拍卖成功后数量可能会超过其携带上限，订单转人工处理");
        subOrder.setTakeOverSubjectId(user.getLoginAccount());
        subOrder.setTakeOverSubject(user.getNickName());
        subOrder.setBuyerTel(user.getPhoneNumber() == null ? "" : user.getPhoneNumber());
        subOrder.setMachineArtificialStatus(MachineArtificialStatus.ArtificialTransferSuccess.getCode());
        subOrder.setMachineArtificialTime(new Date());
    }

    /**
     * 创建子订单，同时预扣采购单收货数量和收货角色的收货数量
     *
     * @param order
     * @param configCount
     * @return boolean 是否创建成功
     */
    public boolean configByCreateSubOrderAndGameAccount(DeliveryOrder mainOrder, DeliverySubOrder order, long configCount) {
        logger.info("在订单号:" + order + "中配单：{}", configCount);
        // 构建子订单
        DeliverySubOrder subOrder = new DeliverySubOrder();
        subOrder.setChId(mainOrder.getId());
        subOrder.setOrderId(mainOrder.getOrderId());
        subOrder.setSellerUid(order.getSellerUid());
        subOrder.setSellerAccount(order.getSellerAccount());
        subOrder.setBuyerAccount(order.getBuyerAccount());
        subOrder.setBuyerUid(order.getBuyerUid());
        subOrder.setGameName(order.getGameName());
        subOrder.setRegion(order.getRegion());
        subOrder.setServer(order.getServer());
        subOrder.setGameRace(order.getGameRace());
        subOrder.setSellerRoleName(order.getSellerRoleName());
        subOrder.setWords(order.getWords());
        subOrder.setAddress(order.getAddress());
        subOrder.setGameAccount(order.getGameAccount());
        subOrder.setGamePwd(order.getGamePwd());
        subOrder.setGameRole(order.getGameRole());
        subOrder.setSecondPwd(order.getSecondPwd());
        subOrder.setCount(configCount);
//        subOrder.setRealCount(0L);
        subOrder.setStatus(DeliveryOrderStatus.TRADING.getCode());
        subOrder.setBuyerTel(order.getBuyerTel());
        subOrder.setMoneyName(order.getMoneyName());
        subOrder.setCreateTime(new Date());
        subOrder.setTakeOverSubjectId(mainOrder.getTakeOverSubjectId());
        subOrder.setTakeOverSubject(mainOrder.getTakeOverSubject());
        subOrder.setMachineArtificialTime(mainOrder.getMachineArtificialTime());
        subOrder.setMachineArtificialReason(mainOrder.getMachineArtificialReason());
        subOrder.setMachineArtificialStatus(mainOrder.getMachineArtificialStatus());
        subOrder.setDeliveryType(order.getDeliveryType());
        subOrder.setGameAccountId(order.getGameAccountId());

        Map<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("gameAccount", order.getGameAccount());
        updateMap.put("buyerAccount", order.getBuyerAccount());
        updateMap.put("gameName", order.getGameName());
        updateMap.put("region", order.getRegion());
        updateMap.put("server", order.getServer());
        updateMap.put("gameRace", order.getGameRace());
        updateMap.put("roleName", order.getGameRole());
        updateMap.put("status", GameAccount.S_RECEIVING);
        // 添加子订单 更新配单表
        gameAccountManager.updateStatus(updateMap);
        deliverySubOrderDao.insert(subOrder);

        //批量查询img表测数据
        Map<String, Object> selectMap = new HashMap<String, Object>();
        selectMap.put("subOrderId", order.getId());
        List<RobotImgEO> robotImgEOs = robotImgDAO.selectByMap(selectMap);
        if (robotImgEOs != null && robotImgEOs.size() != 0) {
            for (RobotImgEO img : robotImgEOs) {
                img.setOrderId(mainOrder.getOrderId());
                img.setSubOrderId(subOrder.getChId());
            }
            //修改订单号后插入img表
            robotImgDAO.batchInsert(robotImgEOs);
        }
        return true;
    }

    /**
     * 修改订单状态
     *
     * @param id
     * @param status
     */
    @Transactional
    public void changeState(Long id, int status) {
        DeliveryOrder order = deliveryOrderDao.selectByIdForUpdate(id);
        if (status == DeliveryOrderStatus.TRADING.getCode()) {
            order.setTradeStartTime(new Date());
        }
        order.setStatus(status);
        deliveryOrderDao.update(order);
    }

}
