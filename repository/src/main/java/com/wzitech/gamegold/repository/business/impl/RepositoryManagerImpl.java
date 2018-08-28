/************************************************************************************
 * Copyright 2014 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		GoodsInfoManagerImpl
 * 包	名：		com.wzitech.gamegold.goodsmgmt.business.impl
 * 项目名称：	gamegold-goods
 * 作	者：		SunChengfei
 * 创建时间：	2014-2-15
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014-2-15 上午11:52:17
 ************************************************************************************/
package com.wzitech.gamegold.repository.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.BeanMapper;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.constants.RepositoryConstants;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.common.enums.LogType;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.log.entity.RepositoryLogInfo;
import com.wzitech.gamegold.common.servicer.IServicerOrderManager;
import com.wzitech.gamegold.repository.business.IRepositoryLogManager;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.business.IRepositoryTransfer;
import com.wzitech.gamegold.repository.dao.IRepositoryDBDAO;
import com.wzitech.gamegold.repository.dao.IRepositoryRedisDAO;
import com.wzitech.gamegold.repository.dao.ISellerDBDAO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.shorder.business.IGameAccountManager;
import com.wzitech.gamegold.shorder.business.IShGameConfigManager;
import com.wzitech.gamegold.shorder.business.ISplitRepositoryLogManager;
import com.wzitech.gamegold.shorder.business.impl.ISyncRepositoryManager;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import com.wzitech.gamegold.shorder.entity.SplitRepositoryLog;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 库存管理接口实现
 *
 * @author SunChengfei
 *         Update History
 *         Date        Name                Reason for change
 *         ----------  ----------------    ----------------------
 *         2017/05/19  wubiao              ZW_C_JB_00008 商城增加通货F
 */
@Component
public class RepositoryManagerImpl extends AbstractBusinessObject implements IRepositoryManager {

    protected static final Log log = LogFactory.getLog(RepositoryManagerImpl.class);

    @Autowired
    IRepositoryDBDAO repositoryDBDAO;

    @Autowired
    IRepositoryRedisDAO repositoryRedisDAO;

    @Autowired
    IServicerOrderManager servicerOrderManager;

    /*@Autowired
    ILogManager logManager;*/
    @Autowired
    IRepositoryLogManager repositoryLogManager;

    @Autowired
    ISellerDBDAO sellerDBDAO;

    @Autowired
    IGameAccountManager gameAccountManager;

    @Autowired
    IShGameConfigManager shGameConfigManager;

    @Autowired
    ISplitRepositoryLogManager splitRepositoryLogManager;

    @Autowired
    AysncDBIOImple aysncDBIOImple;

    @Resource(name = "syncRepositoryManager")
    ISyncRepositoryManager syncRepositoryManager;

    @Resource(name = "bnsRepositoryTransferManager")
    IRepositoryTransfer bnsRepositoryTransferManager;

    @Resource(name = "wowRepositoryTransferManager")
    IRepositoryTransfer wowRepositoryTransferManager;

    @Resource(name = "jx3RepositoryTransferManager")
    IRepositoryTransfer jx3RepositoryTransferManager;

    @Resource(name = "jfRepositoryTransferManager")
    IRepositoryTransfer jfRepositoryTransferManager;

    @Resource(name = "xtlbbRepositoryTransferManager")
    IRepositoryTransfer xtlbbRepositoryTransferManager;

    @Resource(name = "dnfRepositoryTransferManager")
    IRepositoryTransfer dnfRepositoryTransferManager;

    private List<RepositoryInfo> addRepositoryInfoList = new ArrayList<RepositoryInfo>();

    private List<RepositoryInfo> updateRepositoryInfoList = new ArrayList<RepositoryInfo>();

    @Override
    public void clearAddRepositoryData() {
        addRepositoryInfoList = new ArrayList<RepositoryInfo>();
        updateRepositoryInfoList = new ArrayList<RepositoryInfo>();
    }

    @Override
    public void commitAddRepositoryData() {
        if (addRepositoryInfoList != null && addRepositoryInfoList.size() > 0)
            repositoryDBDAO.batchInsert(addRepositoryInfoList);
        if (updateRepositoryInfoList != null && updateRepositoryInfoList.size() > 0)
            repositoryDBDAO.batchUpdate(updateRepositoryInfoList);
    }

    @Override
    @Transactional
    public RepositoryInfo addRepositorySingle(RepositoryInfo repositoryInfo) {
        this.clearAddRepositoryData();
        RepositoryInfo retRepositoryInfo = this.addRepository(repositoryInfo);
        this.commitAddRepositoryData();
        return retRepositoryInfo;
    }

    @Override
    @Transactional
    public void addRepositoryList(List<RepositoryInfo> repositoryInfos) {
        this.clearAddRepositoryData();
        for (RepositoryInfo repositoryInfo : repositoryInfos) {
            this.addRepository(repositoryInfo);
        }
        this.commitAddRepositoryData();
    }

    @Override
    @Transactional
    public RepositoryInfo addRepository(RepositoryInfo repositoryInfo) throws SystemException {
        if (repositoryInfo == null) {
            throw new SystemException(ResponseCodes.EmptyRepository.getCode());
        }
        if (StringUtils.isEmpty(repositoryInfo.getGameName())) {
            throw new SystemException(ResponseCodes.EmptyGameName.getCode());
        }
        if (StringUtils.isEmpty(repositoryInfo.getRegion())) {
            throw new SystemException(ResponseCodes.EmptyRegion.getCode());
        }
        if (StringUtils.isEmpty(repositoryInfo.getServer())) {
            throw new SystemException(ResponseCodes.EmptyGameServer.getCode());
        }
        if (StringUtils.isEmpty(repositoryInfo.getGameAccount())) {
            throw new SystemException(ResponseCodes.EmptyGameAccount.getCode());
        }
        if (StringUtils.isEmpty(repositoryInfo.getSellerGameRole())) {
            throw new SystemException(ResponseCodes.EmptySellerGameRole.getCode());
        }
        if (StringUtils.isEmpty(repositoryInfo.getLoginAccount())) {
            throw new SystemException(ResponseCodes.EmptyLoginAccount.getCode());
        }
        if (StringUtils.isEmpty(repositoryInfo.getAccountUid())) {
            throw new SystemException(ResponseCodes.EmptyUid.getCode());
        }
        /***************ZW_C_JB_00008_20170516 START ADD START********************/
        if (StringUtils.isEmpty(repositoryInfo.getGoodsTypeName())) {
            throw new SystemException(ResponseCodes.NoGoodsTypeName.getCode());
        }
        /***************ZW_C_JB_00008_20170516 START ADD END********************/
        // 添加附加信息
        repositoryInfo.setIsDeleted(false);
        repositoryInfo.setLastUpdateTime(new Date());

        // 保存到数据库
        // 查询是否已存在该卖家，相同的库存信息，如果存在，目前按照覆盖的原则，而不是累加
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("loginAccount", repositoryInfo.getLoginAccount());
        queryMap.put("accountUid", repositoryInfo.getAccountUid());
        queryMap.put("backGameName", repositoryInfo.getGameName());
        queryMap.put("backRegion", repositoryInfo.getRegion());
        queryMap.put("backServer", repositoryInfo.getServer());
        queryMap.put("backGameRace", repositoryInfo.getGameRace());
        queryMap.put("gameAccount", repositoryInfo.getGameAccount());
        queryMap.put("sellerGameRole", repositoryInfo.getSellerGameRole());
        queryMap.put("goodsTypeName", repositoryInfo.getGoodsTypeName());//ZW_C_JB_00008_20170517 ADD
        queryMap.put("isDeleted", false);
        queryMap.put("lockMode", true);

        List<RepositoryInfo> repositoryInfoList = repositoryDBDAO.selectByMap(queryMap, "CREATE_TIME", false);
        if (!CollectionUtils.isEmpty(repositoryInfoList)) {
            //先判断上传的密码是否为空，若为空则set入数据库查处的密码
            RepositoryInfo dbExsitRepository = repositoryInfoList.get(0);
            if (StringUtils.isBlank(repositoryInfo.getGamePassWord())) {
                repositoryInfo.setGamePassWord(dbExsitRepository.getGamePassWord());
            }
            if (repositoryInfo.getGoldCount() == null) {
                repositoryInfo.setGoldCount(dbExsitRepository.getGoldCount());
            }
            if (repositoryInfo.getSellableCount() == null) {
                if (StringUtils.equals(repositoryInfo.getGameName(), "地下城与勇士") || StringUtils.equals(repositoryInfo.getGameName(), "魔兽世界(国服)") || StringUtils.equals(repositoryInfo.getGameName(), "新天龙八部")) {
                    repositoryInfo.setSellableCount(dbExsitRepository.getSellableCount());
                } else {
                    repositoryInfo.setSellableCount(repositoryInfo.getGoldCount());
                }
            }
            // 总库存量小于可销售库存量的时候，将总库存设置为可销售库存
            if (repositoryInfo.getGoldCount() != null && repositoryInfo.getSellableCount() != null) {
                if (repositoryInfo.getGoldCount().longValue() < repositoryInfo.getSellableCount().longValue()) {
                    repositoryInfo.setGoldCount(repositoryInfo.getSellableCount());
                }
            }
            if (repositoryInfo.getUnitPrice() == null) {
                repositoryInfo.setUnitPrice(dbExsitRepository.getUnitPrice());
            }
            if (repositoryInfo.getUnitPrice() != null && BigDecimal.ZERO.compareTo(repositoryInfo.getUnitPrice()) == 0) {
                throw new SystemException(ResponseCodes.UnitPriceMustGreaterThanZero.getCode(), ResponseCodes.UnitPriceMustGreaterThanZero.getMessage());
            }
            repositoryInfo.setId(dbExsitRepository.getId());
            repositoryInfo.setIsDeleted(false);
            // 库存数量大于999999999的强制设置为999999999
            if (repositoryInfo.getGoldCount() != null && repositoryInfo.getGoldCount() > 999999999) {
                repositoryInfo.setGoldCount(999999999L);
            }
            if (repositoryInfo.getSellableCount() != null && repositoryInfo.getSellableCount() > 999999999) {
                repositoryInfo.setSellableCount(999999999L);
            }
            // 设置单价保留5位小数
            if (repositoryInfo.getUnitPrice() != null) {
                repositoryInfo.setUnitPrice(repositoryInfo.getUnitPrice());/*******ZW_C_JB_00008_20170522 MODIFY****/
            }
            if (!StringUtils.equals(repositoryInfo.getGameName(), "魔兽世界(国服)")) {
                repositoryInfo.setGameRace(null);
            }
            if (dbExsitRepository.getStockCount() == null || dbExsitRepository.getStockCount() == 0) {
                repositoryInfo.setStockCount(0L);
            } else {
                repositoryInfo.setStockCount(dbExsitRepository.getStockCount());
            }
            // 更新数据，直接覆盖
            updateRepositoryInfoList.add(repositoryInfo);
//            /*logger.debug("卖家{}，添加库存{}，数据库已存在，覆盖原始库存。", new Object[]{
//                    repositoryInfo.getLoginAccount(), repositoryInfo});*/
//            StringBuffer sb = new StringBuffer("可销售库存从");
//            sb.append(dbExsitRepository.getSellableCount()).append("-->").append(repositoryInfo.getSellableCount());
//            sb.append("单价从").append(dbExsitRepository.getUnitPrice()).append("-->").append(repositoryInfo.getUnitPrice());
//
//            RepositoryLogInfo logInfo = new RepositoryLogInfo();
//            logInfo.setLogType(LogType.REPOSITORY_MODIFY);
//            logInfo.setGoodsTypeName(repositoryInfo.getGoodsTypeName());   /**ZW_C_JB_00008_2017/5/15 add **/
//            logInfo.setRemark(sb.toString());
//            repositoryLogManager.add(logInfo, repositoryInfo);
        } else {
            if (StringUtils.isEmpty(repositoryInfo.getGamePassWord())) {
                throw new SystemException(ResponseCodes.EmptyGamePassWord.getCode());
            }
            if (repositoryInfo.getGoldCount() == null || repositoryInfo.getGoldCount().intValue() < 0) {
                throw new SystemException(ResponseCodes.EmptyGoldCount.getCode());
            }
            /**********************ZW_C_JB_00008_20170518 ADD START****************************/
            if (repositoryInfo.getSellableCount() == null) {
                if (StringUtils.equals(repositoryInfo.getGameName(), "地下城与勇士") || StringUtils.equals(repositoryInfo.getGameName(), "魔兽世界(国服)") || StringUtils.equals(repositoryInfo.getGameName(), "新天龙八部")) {
                    repositoryInfo.setSellableCount(repositoryInfo.getSellableCount());
                } else {
                    repositoryInfo.setSellableCount(repositoryInfo.getGoldCount());
                }
            }
            // 总库存量小于可销售库存量的时候，将总库存设置为可销售库存
            if (repositoryInfo.getGoldCount() != null && repositoryInfo.getSellableCount() != null) {
                if (repositoryInfo.getGoldCount().longValue() < repositoryInfo.getSellableCount().longValue()) {
                    repositoryInfo.setGoldCount(repositoryInfo.getSellableCount());
                }
            }
            /**********************ZW_C_JB_00008_20170518 ADD END****************************/
            if (repositoryInfo.getUnitPrice() == null) {
                throw new SystemException(ResponseCodes.EmptyUnitPrice.getCode());
            }
            if (repositoryInfo.getUnitPrice() != null && BigDecimal.ZERO.compareTo(repositoryInfo.getUnitPrice()) == 0) {
                throw new SystemException(ResponseCodes.UnitPriceMustGreaterThanZero.getCode(), ResponseCodes.UnitPriceMustGreaterThanZero.getMessage());
            }
            //重配置单的时候，比较的是可销售库存，这里如果可销售库存为空，默认与库存数量一致
            if (repositoryInfo.getSellableCount() == null) {
                repositoryInfo.setSellableCount(repositoryInfo.getGoldCount());
            }
            if (StringUtils.equals(repositoryInfo.getGameName(), "魔兽世界(国服)") && StringUtils.isEmpty(repositoryInfo.getGameRace())) {
                throw new SystemException(ResponseCodes.EmptyGameRace.getCode());
            }
            // 数据库中不存在相同游戏属性及卖家的信息，直接插入
            repositoryInfo.setCreateTime(new Date());
            // 库存数量大于999999999的强制设置为999999999
            if (repositoryInfo.getGoldCount() != null && repositoryInfo.getGoldCount() > 999999999) {
                repositoryInfo.setGoldCount(999999999L);
            }
            if (repositoryInfo.getSellableCount() != null && repositoryInfo.getSellableCount() > 999999999) {
                repositoryInfo.setSellableCount(999999999L);
            }
            // 设置单价保留5位小数
            if (repositoryInfo.getUnitPrice() != null) {
                repositoryInfo.setUnitPrice(repositoryInfo.getUnitPrice());
            }
            if (!StringUtils.equals(repositoryInfo.getGameName(), "魔兽世界(国服)")) {
                repositoryInfo.setGameRace(null);
            }
            addRepositoryInfoList.add(repositoryInfo);
            /*logger.debug("卖家{}，添加库存{}，数据库未找到，插入新数据。", new Object[]{
                    repositoryInfo.getLoginAccount(), repositoryInfo});
//*/
//            StringBuffer sb = new StringBuffer("添加库存，可销售库存：");
//            sb.append(repositoryInfo.getSellableCount());
//            sb.append("单价：").append(repositoryInfo.getUnitPrice());
//            RepositoryLogInfo logInfo = new RepositoryLogInfo();
//            logInfo.setLogType(LogType.REPOSITORY_ADD);
//            logInfo.setRemark(sb.toString());
//            logInfo.setGoodsTypeName(repositoryInfo.getGoodsTypeName()); /*****ZW_C_JB_00008_2017/5/15 ADD '通货******'*/
//            repositoryLogManager.add(logInfo, repositoryInfo);
        }

//        //收货系统库存
//        gameAccountManager.updateRepositoryCount(repositoryInfo.getLoginAccount(), repositoryInfo.getGameName(), repositoryInfo.getRegion(), repositoryInfo.getServer(),
//                repositoryInfo.getGameRace(), repositoryInfo.getGameAccount(), repositoryInfo.getSellerGameRole(), repositoryInfo.getSellableCount(), repositoryInfo.getGoodsTypeName());


//        //添加收货商日志
//        StringBuffer sb = new StringBuffer("商城添加库存，当前库存：");
//        sb.append(repositoryInfo.getSellableCount());
//        saveLog(repositoryInfo, sb.toString());

        // 客服处理订单数初始化
        // 意思即：一个客服有库存了，即可以开始处理订单了
        /*if (!servicerOrderManager.isInitOrderNum(repositoryInfo.getGameName(),
                repositoryInfo.getRegion(), repositoryInfo.getServer(),
                repositoryInfo.getGameRace(), repositoryInfo.getServicerId())) {
            servicerOrderManager.initOrderNum(repositoryInfo.getGameName(),
                    repositoryInfo.getRegion(), repositoryInfo.getServer(),
                    repositoryInfo.getGameRace(),
                    repositoryInfo.getServicerId());
        }*/

        // 增加日志信息
        /*StringBuffer sb = new StringBuffer("新增库存信息:");
        sb.append("卖家").append(repositoryInfo.getLoginAccount()).append("增加")
				.append(repositoryInfo.getGameName()).append("游戏")
				.append(repositoryInfo.getGoldCount()).append("库存");*/
        //logManager.add(ModuleType.REPOSITORY, sb.toString(), CurrentUserContext.getUser());
        //log.debug(sb.toString());


        // redis中库存总量
        // redis查询速度快，方便前台查询符合条件的客服信息
        /*long goldCount = queryMaxCount(repositoryInfo.getGameName(), repositoryInfo.getRegion(),
                repositoryInfo.getServer(), repositoryInfo.getGameRace(), repositoryInfo.getServicerId());
        repositoryRedisDAO.saveRepositorySum(repositoryInfo, goldCount);*/

        return repositoryInfo;
    }


    @Override
    public void addRepository(List<RepositoryInfo> repositoryInfoListUpload, IUser user) {
//        logger.info("当前处理上传库存，repositoryInfoListUpload:" + repositoryInfoListUpload + "，userLoginAccount:" + user.getLoginAccount());
        if (repositoryInfoListUpload == null || repositoryInfoListUpload.size() == 0) {
            return;
        }
        List<RepositoryInfo> updateRepositoryList = new ArrayList<RepositoryInfo>();
        List<RepositoryInfo> insertRepositoryList = new ArrayList<RepositoryInfo>();
        Map<String, RepositoryInfo> unitqueRepositoryMap = new HashedMap();
        for (int i = 0; i < repositoryInfoListUpload.size(); i++) {
//            logger.info("当前处理上传库存:" + repositoryInfoListUpload.get(i).toString());
            RepositoryInfo repositoryInfoNow = repositoryInfoListUpload.get(i);
            if (repositoryInfoNow == null) {
                throw new SystemException(ResponseCodes.EmptyRepository.getCode());
            }
            if (StringUtils.isBlank(repositoryInfoNow.getGameName())) {
                throw new SystemException(ResponseCodes.EmptyGameName.getCode());
            }
            if (StringUtils.isBlank(repositoryInfoNow.getRegion())) {
                throw new SystemException(ResponseCodes.EmptyRegion.getCode());
            }
            if (StringUtils.isBlank(repositoryInfoNow.getServer())) {
                throw new SystemException(ResponseCodes.EmptyGameServer.getCode());
            }
            if (StringUtils.isBlank(repositoryInfoNow.getGameAccount())) {
                throw new SystemException(ResponseCodes.EmptyGameAccount.getCode());
            }
            if (StringUtils.isBlank(repositoryInfoNow.getSellerGameRole())) {
                throw new SystemException(ResponseCodes.EmptySellerGameRole.getCode());
            }
            if (StringUtils.isBlank(repositoryInfoNow.getOriginalGoodsTypeNmae())) {
                throw new SystemException(ResponseCodes.NoGoodsTypeName.getCode());
            }
            if (StringUtils.isBlank(repositoryInfoNow.getMoneyName())) {
                throw new SystemException(ResponseCodes.EmptyMoneyName.getCode(), ResponseCodes.EmptyMoneyName.getMessage());
            }
            if (repositoryInfoNow.getUnitPrice() == null) {
                throw new SystemException(ResponseCodes.EmptyUnitPrice.getCode(), ResponseCodes.EmptyUnitPrice.getMessage());
            }
            if (BigDecimal.ZERO.compareTo(repositoryInfoNow.getUnitPrice()) >= 0) {
                throw new SystemException(ResponseCodes.UnitPriceMustGreaterThanZero.getCode(), ResponseCodes.UnitPriceMustGreaterThanZero.getMessage());
            }
            if (repositoryInfoNow.getGoldCount() == null || repositoryInfoNow.getGoldCount() < 0) {
                throw new SystemException(ResponseCodes.NoGoldCount.getCode(), ResponseCodes.NoGoldCount.getMessage());
            }
            if (repositoryInfoNow.getSellableCount() == null || repositoryInfoNow.getSellableCount() < 0) {
                throw new SystemException(ResponseCodes.EmptySellableGoldCount.getCode(), ResponseCodes.EmptySellableGoldCount.getMessage());
            }
            // 总库存量小于可销售库存量的时候，将总库存设置为可销售库存
            if (repositoryInfoNow.getGoldCount().longValue() < repositoryInfoNow.getSellableCount().longValue()) {
                repositoryInfoNow.setGoldCount(repositoryInfoNow.getSellableCount());
            }
            if (repositoryInfoNow.getGoldCount() != null && repositoryInfoNow.getGoldCount() > 999999999) {
                repositoryInfoListUpload.get(i).setGoldCount(999999999L);
            }
            if (repositoryInfoNow.getSellableCount() != null && repositoryInfoNow.getSellableCount() > 999999999) {
                repositoryInfoListUpload.get(i).setSellableCount(999999999L);
            }
            if (StringUtils.isBlank(repositoryInfoNow.getGamePassWord())) {
                throw new SystemException(ResponseCodes.EmptyGamePassWord.getCode(), ResponseCodes.EmptyGamePassWord.getMessage());
            }

            String popKey = repositoryInfoNow.getGameName() +
                    repositoryInfoNow.getRegion() +
                    repositoryInfoNow.getServer() +
                    repositoryInfoNow.getGameAccount() +
                    repositoryInfoNow.getSellerGameRole() +
                    repositoryInfoNow.getGoodsTypeName();
            if (StringUtils.isNotBlank(repositoryInfoNow.getGameRace())) {
                //有阵营 不是魔兽世界 报错
                if (!StringUtils.equals(repositoryInfoNow.getGameName(), "魔兽世界(国服)")) {
                    throw new SystemException(ResponseCodes.NotMatchRaceWithGame.getCode(), ResponseCodes.NotMatchRaceWithGame.getMessage());
                } else {
                    popKey = popKey + repositoryInfoNow.getGameRace();
                }
            }
            //魔兽世界无阵营报错
            if (StringUtils.equals(repositoryInfoNow.getGameName(), "魔兽世界(国服)") && StringUtils.isBlank(repositoryInfoNow.getGameRace())) {
                throw new SystemException(ResponseCodes.EmptyGameRace.getCode(), ResponseCodes.EmptyGameRace.getMessage());
            }
            //不是魔兽世界 阵营设置为null
            if (!StringUtils.equals(repositoryInfoNow.getGameName(), "魔兽世界(国服)")) {
                repositoryInfoNow.setGameRace(null);
            }
            if (unitqueRepositoryMap.containsKey(popKey)) {
                throw new SystemException(ResponseCodes.ExistSameRepository.getCode(), ResponseCodes.ExistSameRepository.getMessage());
            } else {
                unitqueRepositoryMap.put(popKey, repositoryInfoNow);
            }
            if (user != null) {
                repositoryInfoNow.setLoginAccount(user.getLoginAccount());
                repositoryInfoNow.setAccountUid(user.getUid());
            } else {
                throw new SystemException(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
            }
            if (StringUtils.isEmpty(repositoryInfoNow.getLoginAccount())) {
                throw new SystemException(ResponseCodes.EmptyLoginAccount.getCode());
            }
            if (StringUtils.isEmpty(repositoryInfoNow.getAccountUid())) {
                throw new SystemException(ResponseCodes.EmptyUid.getCode());
            }
            repositoryInfoNow.setIsDeleted(false);
            repositoryInfoNow.setLastUpdateTime(new Date());
            // 添加附加信息
            ShGameConfig shGameConfig = shGameConfigManager.getConfigByGameName(repositoryInfoNow.getGameName(),
                    repositoryInfoNow.getOriginalGoodsTypeNmae(), null, true);
            if (shGameConfig == null) {
                throw new SystemException(ResponseCodes.NotAvailableConfig.getCode(), ResponseCodes.NotAvailableConfig.getMessage());
            }
            if (!StringUtils.equals(shGameConfig.getUnitName(), repositoryInfoNow.getMoneyName())) {
                logger.error("当前配置下货币单位为:" + shGameConfig.getUnitName() + ",上传货币单位为:" + repositoryInfoNow.getMoneyName());
                throw new SystemException(ResponseCodes.IllegalArguments.getCode(), "当前配置货币单位与上传单位不一致");
            }
            repositoryInfoNow.setGoodsTypeId(shGameConfig.getGoodsTypeId());
        }
        aysncDBIOImple.aysnDBIO(repositoryInfoListUpload, user, updateRepositoryList, insertRepositoryList);
    }


    @Override
    @Transactional
    public void updatePasspod(RepositoryInfo repositoryInfo) {
        // 验证参数
        if (repositoryInfo == null) {
            throw new SystemException(ResponseCodes.EmptyRepository.getCode());
        }
        if (StringUtils.isEmpty(repositoryInfo.getAccountUid())) {
            throw new SystemException(ResponseCodes.EmptyUid.getCode());
        }
        if (StringUtils.isEmpty(repositoryInfo.getLoginAccount())) {
            throw new SystemException(ResponseCodes.EmptyLoginAccount.getCode());
        }
        if (StringUtils.isEmpty(repositoryInfo.getGameName())) {
            throw new SystemException(ResponseCodes.EmptyGameName.getCode());
        }
        if (StringUtils.isEmpty(repositoryInfo.getGameAccount())) {
            throw new SystemException(ResponseCodes.EmptyGameAccount.getCode());
        }
        if (StringUtils.isEmpty(repositoryInfo.getPasspodUrl())) {
            throw new SystemException(ResponseCodes.EmptyPasspod.getCode());
        }

        // 修改数据库 where条件游戏名+游戏账号+卖家信息
        repositoryInfo.setLastUpdateTime(new Date());
        repositoryDBDAO.update(repositoryInfo);
    }

    @Override
    @Transactional
    public RepositoryInfo modifyRepository(RepositoryInfo repositoryInfo)
            throws SystemException {
        if (repositoryInfo == null) {
            throw new SystemException(ResponseCodes.EmptyRepository.getCode());
        }
        if (repositoryInfo.getId() == null) {
            throw new SystemException(ResponseCodes.EmptyRepositoryId.getCode());
        }
        // 修改的库存数量不能小于0
        if (repositoryInfo.getGoldCount() != null) {
            if (repositoryInfo.getGoldCount().longValue() < 0) {
                throw new SystemException(ResponseCodes.EmptyRepositoryGold.getCode());
            }
        }
        /**********************ZW_C_JB_00008_20170519 ADD ‘库存数量判断’ START****************************/
        if (repositoryInfo.getGoldCount() != null && repositoryInfo.getSellableCount() != null) {
            if (repositoryInfo.getGoldCount().longValue() < repositoryInfo.getSellableCount().longValue()) {
                throw new SystemException(ResponseCodes.GoldCountLessThanSellenbelCount.getCode());
            }
        }
        /**********************ZW_C_JB_00008_20170519 ADD END****************************/
        // 增加日志信息
        StringBuffer sb = new StringBuffer();

        // 原始该库存信息
        long diffCount = 0;
        RepositoryInfo origdbRepository = repositoryDBDAO.selectById(repositoryInfo.getId());
        if (repositoryInfo.getSellableCount() != null
                && repositoryInfo.getSellableCount().longValue() != origdbRepository.getSellableCount().longValue()) {
            diffCount = repositoryInfo.getSellableCount() - origdbRepository.getSellableCount();
            sb.append("可销售库存从").append(origdbRepository.getSellableCount()).append("-->").append(repositoryInfo.getSellableCount());
        }

        // 库存价格发生改变
        if (repositoryInfo.getUnitPrice() != null) {
            sb.append("单价从").append(origdbRepository.getUnitPrice()).append("-->").append(repositoryInfo.getUnitPrice());
            origdbRepository.setUnitPrice(repositoryInfo.getUnitPrice().setScale(5, BigDecimal.ROUND_HALF_UP));/*******ZW_C_JB_00008_20170522 MODIFY****/
        }

        if (!StringUtils.equals(repositoryInfo.getGameName(), "魔兽世界(国服)")) {
            repositoryInfo.setGameRace(null);
        }

        repositoryInfo.setLastUpdateTime(new Date());
        repositoryDBDAO.update(repositoryInfo);

//        //收货系统库存
//        gameAccountManager.updateRepositoryCount(origdbRepository.getLoginAccount(), origdbRepository.getGameName(), origdbRepository.getRegion(), origdbRepository.getServer(), origdbRepository.getGameRace(), origdbRepository.getGameAccount(), origdbRepository.getSellerGameRole(), repositoryInfo.getSellableCount(), repositoryInfo.getGoodsTypeName());

//        //添加收货商日志
//        StringBuffer sbData = new StringBuffer();
//        sbData.append("商城修改库存，从").append(origdbRepository.getSellableCount()).append("-->").append(repositoryInfo.getSellableCount());
//        saveLog(origdbRepository, sbData.toString());

        // 将库存信息存储到redis
        // redis查询速度快，方便前台查询符合条件的客服信息
        if (repositoryInfo.getGoldCount() != null) {
            origdbRepository.setGoldCount(diffCount);
            repositoryRedisDAO.addRepositorySum(origdbRepository);
        }

        //logManager.add(ModuleType.REPOSITORY, sb.toString(), CurrentUserContext.getUser());
        //log.debug(sb.toString());

        RepositoryLogInfo logInfo = new RepositoryLogInfo();
        logInfo.setLogType(LogType.REPOSITORY_MODIFY);
        logInfo.setRemark(sb.toString());
        logInfo.setGoodsTypeName(origdbRepository.getGoodsTypeName());  /*******ZW_C_JB_00008_2017/5/15 ADD*******/
        repositoryLogManager.add(logInfo, repositoryInfo);

        return repositoryInfo;
    }

    /**
     * 批量更新卖家库存，只修改指定字段的值
     *
     * @param repositoryInfoList
     */
    @Transactional
    public void batchUpdateSellerRepository(List<RepositoryInfo> repositoryInfoList, IUser seller) {
        if (CollectionUtils.isEmpty(repositoryInfoList)) return;
        for (RepositoryInfo repositoryInfo : repositoryInfoList) {
            updateSellerRepository(repositoryInfo, seller);

//            gameAccountManager.updateRepositoryCount(repositoryInfo.getLoginAccount(), repositoryInfo.getGameName(), repositoryInfo.getRegion(), repositoryInfo.getServer(), repositoryInfo.getGameRace(), repositoryInfo.getGameAccount(), repositoryInfo.getSellerGameRole(), repositoryInfo.getSellableCount(), repositoryInfo.getGoodsTypeName());
        }
    }

    /**
     * 修改卖家库存，只修改指定字段的值
     *
     * @param repositoryInfo
     * @return
     */
    @Transactional
    public RepositoryInfo updateSellerRepository(RepositoryInfo repositoryInfo, IUser seller) {
        if (repositoryInfo.getId() == null || repositoryInfo.getId().longValue() <= 0) {
            throw new SystemException(ResponseCodes.EmptyRepositoryId.getCode(),
                    ResponseCodes.EmptyRepositoryId.getMessage());
        }

       /* if (StringUtils.isBlank(repositoryInfo.getMoneyName())) {
            throw new SystemException(ResponseCodes.EmptyMoneyName.getCode(),
                    ResponseCodes.EmptyMoneyName.getMessage());
        }*/

        /*if (repositoryInfo.getUnitPrice() == null) {
            throw new SystemException(ResponseCodes.EmptyUnitPrice.getCode(),
                    ResponseCodes.EmptyUnitPrice.getMessage());
        }*/
        if (repositoryInfo.getUnitPrice() != null) {
            if (repositoryInfo.getUnitPrice().doubleValue() <= 0) {
                throw new SystemException(ResponseCodes.UnitPriceMustGreaterThanZero.getCode(),
                        ResponseCodes.UnitPriceMustGreaterThanZero.getMessage());
            } else {
                repositoryInfo.setUnitPrice(repositoryInfo.getUnitPrice().setScale(RepositoryConstants.MAX_DECIMAL_PLACE,
                        RoundingMode.HALF_UP));
            }
        }

        if (repositoryInfo.getGoldCount() != null) {
            if (repositoryInfo.getGoldCount().longValue() < 0) {
                throw new SystemException(ResponseCodes.EmptyGoldCount.getCode(),
                        ResponseCodes.EmptyGoldCount.getMessage());
            } else if (repositoryInfo.getGoldCount().longValue() > RepositoryConstants.MAX_REPOSITORY_COUNT) {
                repositoryInfo.setGoldCount(RepositoryConstants.MAX_REPOSITORY_COUNT);
            }
        }

        if (repositoryInfo.getSellableCount() != null) {
            if (repositoryInfo.getSellableCount().longValue() < 0) {
                throw new SystemException(ResponseCodes.EmptySellableGoldCount.getCode(),
                        ResponseCodes.EmptySellableGoldCount.getMessage());
            } else if (repositoryInfo.getSellableCount().longValue() > RepositoryConstants.MAX_REPOSITORY_COUNT) {
                repositoryInfo.setSellableCount(RepositoryConstants.MAX_REPOSITORY_COUNT);
            }
        }

        /*if (StringUtils.isBlank(repositoryInfo.getGameAccount())) {
            throw new SystemException(ResponseCodes.EmptyGameAccount.getCode(),
                    ResponseCodes.EmptyGameAccount.getMessage());
        }

        if (StringUtils.isBlank(repositoryInfo.getSellerGameRole())) {
            throw new SystemException(ResponseCodes.EmptySellerGameRole.getCode(),
                    ResponseCodes.EmptySellerGameRole.getMessage());
        }*/

        if (repositoryInfo.getGoldCount() == null && repositoryInfo.getSellableCount() != null) {
            repositoryInfo.setGoldCount(repositoryInfo.getSellableCount());
        } else if (repositoryInfo.getGoldCount() != null && repositoryInfo.getSellableCount() == null) {
            repositoryInfo.setSellableCount(repositoryInfo.getGoldCount());
        }

        // 总库存量小于可销售库存量的时候，将总库存设置为可销售库存
        if (repositoryInfo.getGoldCount() != null && repositoryInfo.getSellableCount() != null) {
            if (repositoryInfo.getGoldCount().longValue() < repositoryInfo.getSellableCount().longValue()) {
                repositoryInfo.setGoldCount(repositoryInfo.getSellableCount());
            }
        }

        // 获取原始该库存信息
        //IUser seller = CurrentUserContext.getUser();
        RepositoryInfo dbRepository = querySellerRepository(seller, repositoryInfo.getId());

        // 增加日志信息
        StringBuffer sb = new StringBuffer();
        if (repositoryInfo.getSellableCount() != null
                && repositoryInfo.getSellableCount().longValue() != dbRepository.getSellableCount().longValue()) {
            sb.append("可销售库存从").append(dbRepository.getSellableCount()).append("-->").append(repositoryInfo.getSellableCount());
        }

        // 库存价格发生改变
        if (repositoryInfo.getUnitPrice() != null) {
            sb.append("单价从").append(dbRepository.getUnitPrice()).append("-->").append(repositoryInfo.getUnitPrice());
        }

        // 修改游戏账号或游戏角色名的，需要判断是否存在相同的记录
        if (StringUtils.isNotBlank(repositoryInfo.getGameAccount()) || StringUtils.isNotBlank(repositoryInfo.getSellerGameRole())) {
            // 查询是否已存在该卖家，相同的库存信息
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("loginAccount", seller.getLoginAccount());
            queryMap.put("accountUid", seller.getUid());
            queryMap.put("backGameName", dbRepository.getGameName());
            queryMap.put("backRegion", dbRepository.getRegion());
            queryMap.put("backServer", dbRepository.getServer());
            queryMap.put("backGameRace", dbRepository.getGameRace());
            queryMap.put("gameAccount", repositoryInfo.getGameAccount());
            queryMap.put("sellerGameRole", repositoryInfo.getSellerGameRole());
            queryMap.put("notEqualId", dbRepository.getId());
            queryMap.put("isDeleted", false);

            List<RepositoryInfo> repositoryInfoList = repositoryDBDAO.selectByMap(queryMap, "CREATE_TIME", false);

            if (!CollectionUtils.isEmpty(repositoryInfoList)) {
                String msg = null;
                if (StringUtils.isNotBlank(repositoryInfo.getGameRace())) {
                    msg = String.format("存在相同的库存记录,游戏：%s,区：%s,服：%s,阵营：%s,游戏账号：%s,游戏角色名：%s",
                            dbRepository.getGameName(), dbRepository.getRegion(), dbRepository.getServer(),
                            dbRepository.getGameRace(), repositoryInfo.getGameAccount(), repositoryInfo.getSellerGameRole());
                } else {
                    msg = String.format("存在相同的库存记录,游戏：%s,区：%s,服：%s,游戏账号：%s,游戏角色名：%s",
                            dbRepository.getGameName(), dbRepository.getRegion(), dbRepository.getServer(),
                            repositoryInfo.getGameAccount(), repositoryInfo.getSellerGameRole());
                }
                throw new SystemException(ResponseCodes.ExistSameRepository.getCode(), msg);
            }
        }

        Long sellableCount = dbRepository.getSellableCount();
        String gamePwd = repositoryInfo.getGamePassWord();//游戏密码
        // 修改库存
        dbRepository.setId(repositoryInfo.getId());
        dbRepository.setMoneyName(repositoryInfo.getMoneyName());
        dbRepository.setUnitPrice(repositoryInfo.getUnitPrice());
        dbRepository.setGoldCount(repositoryInfo.getGoldCount());
        dbRepository.setSellableCount(repositoryInfo.getSellableCount());
        dbRepository.setGameAccount(repositoryInfo.getGameAccount());
        dbRepository.setSellerGameRole(repositoryInfo.getSellerGameRole());
        dbRepository.setLastUpdateTime(new Date());
        repositoryInfo.setGoodsTypeName(dbRepository.getGoodsTypeName()); //ZW_C_JB_00008_20170516 ADD
        if (StringUtils.isNotBlank(gamePwd)) {
            dbRepository.setGamePassWord(gamePwd);
        }
        //

        repositoryDBDAO.updateSellerRepository(repositoryInfo);

//        //收货系统库存
//        gameAccountManager.updateRepositoryCount(repositoryInfo.getLoginAccount(), repositoryInfo.getGameName(), repositoryInfo.getRegion(), repositoryInfo.getServer(), repositoryInfo.getGameRace(), repositoryInfo.getGameAccount(), repositoryInfo.getSellerGameRole(), repositoryInfo.getSellableCount(), repositoryInfo.getGoodsTypeName());

//        //添加收货商日志
//        StringBuffer sbData = new StringBuffer();
//        sbData.append("商城修改库存，从").append(sellableCount).append("-->").append(repositoryInfo.getSellableCount());
//        saveLog(dbRepository, sbData.toString());

        // 写入日志
        repositoryInfo.setGameName(dbRepository.getGameName());
        repositoryInfo.setRegion(dbRepository.getRegion());
        repositoryInfo.setServer(dbRepository.getServer());
        repositoryInfo.setGameRace(dbRepository.getGameRace());
        repositoryInfo.setLoginAccount(dbRepository.getLoginAccount());
        RepositoryLogInfo logInfo = new RepositoryLogInfo();
        logInfo.setLogType(LogType.REPOSITORY_MODIFY);
        logInfo.setRemark(sb.toString());
        logInfo.setGoodsTypeName(dbRepository.getGoodsTypeName());  /******ZW_C_JB_00008_2017/5/15 ADD '增加通货'******/
        repositoryLogManager.add(logInfo, repositoryInfo);

        return repositoryInfo;
    }

    @Override
    @Transactional
    public void deleteRepositorys(List<Long> ids) throws SystemException {
        if (ids == null) {
            throw new SystemException(ResponseCodes.EmptyRepositoryId.getCode());
        }

        // db删除
        repositoryDBDAO.batchDeleteByIds(ids);

        for (Long id : ids) {
            // redis中库存总量添减去该库存
            RepositoryInfo dbRepository = repositoryDBDAO.selectById(id);
            repositoryRedisDAO.subRepositorySum(dbRepository);
            //库存删除同步修改收货角色 是否销售状态 jiyx
            syncRepositoryManager.deleteRepositorysSyncShgameAccountIsSaleFalse(dbRepository.getGameAccount(), dbRepository.getLoginAccount(), dbRepository.getGameName(), dbRepository.getRegion(), dbRepository.getServer(), dbRepository.getGameRace(), dbRepository.getSellerGameRole());
        }

        // 增加日志信息
        StringBuffer sb = new StringBuffer("批量删除库存").append("ID列表为").append(ids).append("的库存");
        //logManager.add(ModuleType.REPOSITORY, sb.toString(), CurrentUserContext.getUser());
        log.debug(sb.toString());

        RepositoryLogInfo logInfo = new RepositoryLogInfo();
        logInfo.setLogType(LogType.REPOSITORY_REMOVE);
        logInfo.setRemark(sb.toString());
        repositoryLogManager.add(logInfo, null);
    }

    /**
     * 删除库存
     *
     * @param ids
     * @param seller
     * @throws SystemException
     */
    @Override
    @Transactional
    public void deleteSellerRepositorys(List<Long> ids, IUser seller) throws SystemException {
        if (ids == null) {
            throw new SystemException(ResponseCodes.EmptyRepositoryId.getCode(), ResponseCodes.EmptyRepositoryId.getMessage());
        }

        if (seller == null) {
            throw new SystemException(ResponseCodes.EmptySellerInfo.getCode(), ResponseCodes.EmptySellerInfo.getMessage());
        }

        // db删除
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("ids", ids);
        queryMap.put("seller", seller);
        repositoryDBDAO.deleteSellerRepository(queryMap);

        /*for(Long id : ids){
            // redis中库存总量添减去该库存
            RepositoryInfo dbRepository = repositoryDBDAO.selectById(id);
            repositoryRedisDAO.subRepositorySum(dbRepository);
        }*/

        // 增加日志信息
        StringBuffer sb = new StringBuffer("批量删除库存").append("ID列表为").append(ids).append("的库存");
        //logManager.add(ModuleType.REPOSITORY, sb.toString(), CurrentUserContext.getUser());
        log.debug(sb.toString());

        RepositoryLogInfo logInfo = new RepositoryLogInfo();
        logInfo.setLogType(LogType.REPOSITORY_REMOVE);
        logInfo.setRemark(sb.toString());
        repositoryLogManager.add(logInfo, null);
    }

    @Override
    public GenericPage<RepositoryInfo> queryRepository(
            Map<String, Object> queryMap, int limit, int start, String sortBy,
            boolean isAsc) {
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "ID";
        }
        queryMap.put("isDeleted", false);
        Boolean isNeed = (Boolean) queryMap.get("isNeed");
        queryMap.remove("isNeed");
        String server = (String) queryMap.get("backServer");
        String region = (String) queryMap.get("backRegion");
        queryMap = processRepositoryTransfer2(queryMap);
        String gameName = (String) queryMap.get("gameName");
        String extServer = (String) queryMap.get("server");
        String goodsTypeName = (String) queryMap.get("goodsTypeName");
        if (StringUtils.isBlank(gameName)) {
            gameName = (String) queryMap.get("backGameName");
        }

        ShGameConfig shGameConfig = shGameConfigManager.getConfigByGameName(gameName, (String) queryMap.get("goodsTypeName"), true, true);
        if (shGameConfig != null && shGameConfig.getMinCount() != null) {
            queryMap.put("sellableCount", shGameConfig.getMinCount());
        }
        GenericPage<RepositoryInfo> genericPage = null;
        if ( goodsTypeName.equals(ServicesContants.GOODS_TYPE_GOLD) && "地下城与勇士".equals(gameName)) {
            if (StringUtils.isBlank(extServer)) {
                queryMap.put("server", server);
            }
            genericPage = repositoryDBDAO.selectSalableRepositoryForPage(queryMap, limit,
                    start, sortBy, isAsc);
        } else if ("地下城与勇士".equals(gameName) && goodsTypeName.equals(ServicesContants.GOODS_TYPE_TZS)) {
            queryMap.put("region", region);
            queryMap.put("server", server);
            queryMap.remove("regions");
            queryMap.remove("servers");
            genericPage = repositoryDBDAO.selectByMap(queryMap, limit,
                    start, sortBy, isAsc);
        } else {
            queryMap.put("region", region);
            genericPage = repositoryDBDAO.selectByMap(queryMap, limit,
                    start, sortBy, isAsc);
        }

        List<RepositoryInfo> repositoryList = genericPage.getData();
        if (repositoryList != null && isNeed != null && isNeed) {
            for (RepositoryInfo repository : repositoryList) {
                SellerInfo sellerInfo = repository.getSellerInfo();
                if (sellerInfo != null) {
                    if (sellerInfo.getIsShielded()) {
                        repository.setIsShield(isNeed);
                    } else if (sellerInfo.getIsHelper() != null && sellerInfo.getIsHelper() == true) {
                        repository.setIsShield(isNeed);
                    }
                }
            }
        }
        return genericPage;
    }

    @Override
    public GenericPage<RepositoryInfo> queryStockRepository(
            Map<String, Object> queryMap, int limit, int start, String sortBy,
            boolean isAsc) {
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "ID";
        }
        queryMap.put("isDeleted", false);
        Boolean isNeed = (Boolean) queryMap.get("isNeed");
        queryMap.remove("isNeed");
        String server = (String) queryMap.get("backServer");
        String region = (String) queryMap.get("backRegion");
        queryMap = processRepositoryTransfer(queryMap);
        String gameName = (String) queryMap.get("gameName");
        String extServer = (String) queryMap.get("server");
        String goodsTypeName = (String) queryMap.get("goodsTypeName");
        if (StringUtils.isBlank(gameName)) {
            gameName = (String) queryMap.get("backGameName");
        }
        if (StringUtils.isBlank(extServer)) {
            queryMap.put("server", server);
        }
        ShGameConfig shGameConfig = shGameConfigManager.getConfigByGameName(gameName, (String) queryMap.get("goodsTypeName"), true, true);
        if (shGameConfig != null && shGameConfig.getMinCount() != null) {
            queryMap.put("sellableCount", shGameConfig.getMinCount());
        }
        queryMap.put("region", region);
        GenericPage<RepositoryInfo> genericPage = repositoryDBDAO.selectByMap(queryMap, limit,
                start, sortBy, isAsc);


        List<RepositoryInfo> repositoryList = genericPage.getData();
        if (repositoryList != null && isNeed != null && isNeed) {
            for (RepositoryInfo repository : repositoryList) {
                SellerInfo sellerInfo = repository.getSellerInfo();
                if (sellerInfo != null) {
                    if (sellerInfo.getIsShielded()) {
                        repository.setIsShield(isNeed);
                    } else if (sellerInfo.getIsHelper() != null && sellerInfo.getIsHelper() == true) {
                        repository.setIsShield(isNeed);
                    }
                }
            }
        }
        return genericPage;
    }

    @Override
    public List<RepositoryInfo> queryRepository(Map<String, Object> paramMap,
                                                String sortBy, boolean isAsc) {
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "ID";
        }
        paramMap.put("isDeleted", false);

        paramMap = processRepositoryTransfer(paramMap);

        return repositoryDBDAO.selectByMap(paramMap, sortBy,
                isAsc);
    }

    /**
     * 根据查询条件，查询库存信息列表
     *
     * @param paramMap
     * @param sortFields
     * @return
     */
    public List<RepositoryInfo> queryRepository(Map<String, Object> paramMap, List<SortField> sortFields) {
        if (CollectionUtils.isEmpty(sortFields)) {
            sortFields = new ArrayList<SortField>();
            sortFields.add(new SortField("ID", SortField.DESC));
        }
        paramMap.put("isDeleted", false);
        String server = paramMap.get("backServer") == null ? null : (String) paramMap.get("backServer");
        paramMap = processRepositoryTransfer2(paramMap);
        paramMap.put("server", server);
        //只有dnf游戏币合区合服
        String goodsTypeName = paramMap.get("goodsTypeName") == null ? null : (String) paramMap.get("goodsTypeName");
        String gameName = paramMap.get("backGameName") == null ? null : (String) paramMap.get("backGameName");
        if (StringUtils.isNotBlank(goodsTypeName) && goodsTypeName.equals("游戏币") && "地下城与勇士".equals(gameName)) {
            return repositoryDBDAO.selectSalableRepository(paramMap, sortFields);
        }
        paramMap.remove("server");
        //dnf的其他商品类型不考虑合区合服
        if ("地下城与勇士".equals(gameName)) {
            paramMap.remove("servers");
        }
        //其他游戏如果没有合区合服的配置，就用原来的区服
        if (paramMap.get("servers") == null) {
            paramMap.put("backServer", server);
        }
        return repositoryDBDAO.selectRepositoryList(paramMap, sortFields);
    }

    /**
     * 根据查询条件，查询库存信息列表
     *
     * @param paramMap
     * @param sortFields
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<RepositoryInfo> queryRepository(Map<String, Object> paramMap, List<SortField> sortFields, int start, int pageSize) {
        if (CollectionUtils.isEmpty(sortFields)) {
            sortFields = new ArrayList<SortField>();
            sortFields.add(new SortField("ID", SortField.DESC));
        }
        paramMap.put("isDeleted", false);

        paramMap = processRepositoryTransfer(paramMap);

        return repositoryDBDAO.selectRepositoryList(paramMap, sortFields, start, pageSize);
    }

    /**
     * 分页查询库存数据
     *
     * @param paramMap
     * @param sortFields
     * @param start
     * @param pageSize
     * @return
     */
    public GenericPage<RepositoryInfo> queryPageRepository(Map<String, Object> paramMap, List<SortField> sortFields,
                                                           int start, int pageSize) {
        if (CollectionUtils.isEmpty(sortFields)) {
            sortFields = new ArrayList<SortField>();
            sortFields.add(new SortField("ID", SortField.DESC));
        }
        paramMap.put("isDeleted", false);

        paramMap = processRepositoryTransfer(paramMap);
        return repositoryDBDAO.queryPageRepository(paramMap, sortFields, start, pageSize);
    }

    @Override
    public RepositoryInfo queryById(Long repositoryId) {
        if (repositoryId == null) {
            throw new SystemException(ResponseCodes.EmptyRepositoryId.getCode());
        }
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("isDeleted", false);
        queryMap.put("id", repositoryId);
        List<RepositoryInfo> repositoryInfos = repositoryDBDAO.selectByMap(
                queryMap, "ID", true);
        if (repositoryInfos.size() > 0) {
            return repositoryInfos.get(0);
        } else {
            throw new SystemException(ResponseCodes.EmptyRepository.getCode(), "没有找到库存信息");
        }
    }

    /**
     * 通过ID查询库存信息，不管有没有被删除
     *
     * @param id
     * @return
     */
    @Override
    public RepositoryInfo selectById(Long id) {
        return repositoryDBDAO.selectById(id);
    }

    /**
     * 查询卖家库存
     *
     * @param seller
     * @param repositoryId
     * @return
     */
    public RepositoryInfo querySellerRepository(IUser seller, Long repositoryId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("id", repositoryId);
        queryMap.put("loginAccount", seller.getLoginAccount());
        queryMap.put("accountUid", seller.getUid());
        queryMap.put("goodsTypeName", ServicesContants.TYPE_ALL);
        List<RepositoryInfo> repositoryInfos = repositoryDBDAO.selectByMap(queryMap, "ID", true);
        if (repositoryInfos.size() > 0) {
            return repositoryInfos.get(0);
        } else {
            throw new SystemException(ResponseCodes.EmptyRepository.getCode(), "没有找到库存信息");
        }
    }

    @Override
    public List<RepositoryInfo> queryRepositoryInfos(String gameUid,
                                                     String loginAccount) throws SystemException {
        if (StringUtils.isEmpty(gameUid)) {
            throw new SystemException(ResponseCodes.EmptyUid.getCode());
        }
        if (StringUtils.isEmpty(loginAccount)) {
            throw new SystemException(ResponseCodes.EmptyLoginAccount.getCode());
        }
        List<RepositoryInfo> repositoryInfos = repositoryDBDAO.queryGameInfo(
                loginAccount, gameUid);
        if (repositoryInfos != null && repositoryInfos.size() > 0) {
            return repositoryInfos;
        }

        return null;
    }

    /**
     * ZW_C_JB_00008_20170513 ADD '通货上传'
     *
     * @param file
     * @param goodsTypeName
     * @return
     * @throws SystemException
     * @throws IOException
     */
    @Override
    public List<RepositoryInfo> batchAddRepository(byte[] file, String goodsTypeName) throws SystemException, IOException {
        // 读取excel
        InputStream inp = new ByteArrayInputStream(file);
        HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));
        HSSFSheet sheet = wb.getSheetAt(0);

        if (StringUtils.isBlank(goodsTypeName)) {
            goodsTypeName = "游戏币";
        }

        if (sheet.getLastRowNum() <= 0) {
            throw new SystemException(ResponseCodes.EmptyUploadFile.getCode());
        }

        HSSFRow row = null;
        List<Integer> errorMsgs = new ArrayList<Integer>();
        List<RepositoryInfo> repositoryInfos = new ArrayList<RepositoryInfo>();
        Map<String, ShGameConfig> shGameConfigMap = new HashMap();
        for (int i = 1; i < sheet.getLastRowNum() - 2; i++) {// 循环读取每一行
            List<String> itemList = new ArrayList<String>();
            row = sheet.getRow(i);
            if (null == row) {
                continue;
            }
            String data = null;
            for (int j = 0; j < row.getLastCellNum(); j++) {// 循环读取每一列
                data = getStringCellValue(row.getCell(j));
                itemList.add(data);
            }
            RepositoryInfo repositoryItem = new RepositoryInfo();
            if (itemList.size() >= 1) {
                repositoryItem.setGameName(itemList.get(0)); // 游戏名称
            }
            /*************************ZW_C_JB_00008_20170518 ADD START*********************************/
            String key = repositoryItem.getGameName() + ":" + goodsTypeName;
            ShGameConfig shGameConfig = shGameConfigMap.get(key);
            if (!shGameConfigMap.containsKey(key)) {
                shGameConfig = shGameConfigManager.getConfigByGameName(repositoryItem.getGameName(), goodsTypeName, null, true);
                shGameConfigMap.put(key, shGameConfig);
            }
            if (shGameConfig == null) {
                String msg = String.format("第%s行，没有找到与当前游戏匹配的商品类目", i);
                throw new SystemException(ResponseCodes.IllegalArguments.getCode(), msg);
            }
            repositoryItem.setGoodsTypeId(shGameConfig.getGoodsTypeId());
            /*************************ZW_C_JB_00008_20170518 ADD END*********************************/
            repositoryItem.setGoodsTypeName(goodsTypeName);//增加商品类目
            if (itemList.size() >= 2) {
                repositoryItem.setRegion(itemList.get(1)); // 游戏区
            }
            if (itemList.size() >= 3) {
                repositoryItem.setServer(itemList.get(2)); // 游戏服
            }
            if (itemList.size() >= 4) {
                repositoryItem.setMoneyName(itemList.get(3));// 游戏币名
            }
            if (itemList.size() >= 5) { //上传库存单价小数点后5位有效
                repositoryItem.setUnitPrice(new BigDecimal(itemList.get(4)).setScale(5, BigDecimal.ROUND_HALF_UP));//ZW_C_JB_00008_20170522 MODIFY
            }
            if (itemList.size() >= 6) {
                Long goldCount = null;
                if (itemList.get(5) != null && itemList.get(5) != "") {
                    goldCount = new BigDecimal(itemList.get(5)).longValue();
                    if (goldCount < 0) {
                        errorMsgs.add(i);
                        continue;
                    }
                }
                repositoryItem.setGoldCount(goldCount);// 库存数量
            }
            if ("地下城与勇士".equals(repositoryItem.getGameName()) || "魔兽世界(国服)".equals(repositoryItem.getGameName()) || "新天龙八部".equals(repositoryItem.getGameName())) {
                if (itemList.size() >= 7) {
                    Long sellableCount = null;
                    if (itemList.get(6) != null && itemList.get(6) != "") {
                        sellableCount = new BigDecimal(itemList.get(6)).longValue();
                        if (sellableCount < 0) {
                            errorMsgs.add(i);
                            continue;
                        }
                    }
                    repositoryItem.setSellableCount(sellableCount);// 可销售库存
                }
                if (itemList.size() >= 8) {
                    repositoryItem.setGameAccount(itemList.get(7));// 对应游戏账号
                }
                if (itemList.size() >= 9) {
                    repositoryItem.setGamePassWord(itemList.get(8));// 游戏密码
                }
                if (itemList.size() >= 10) {
                    repositoryItem.setSellerGameRole(itemList.get(9));// 游戏角色名
                }
                if (itemList.size() >= 11) {
                    repositoryItem.setSonGamePassWord(itemList.get(10));// 二级密码
                }
                if (itemList.size() >= 12) {
                    repositoryItem.setGameRace(itemList.get(11));// 阵营
                }
                if (itemList.size() >= 13) {
                    repositoryItem.setRolePassword(itemList.get(12));// 人物密码
                }
                if (itemList.size() >= 14) {
                    repositoryItem.setFundsPassword(itemList.get(13));// 财产密码
                }
                if (itemList.size() >= 15) {
                    repositoryItem.setHousePassword(itemList.get(14));// 仓库密码
                }
                if (itemList.size() >= 16) {
                    repositoryItem.setLoginAccount(itemList.get(15));// 卖家账号
                }
            } else {
                if (itemList.size() >= 7) {
                    repositoryItem.setGameAccount(itemList.get(6));// 对应游戏账号
                }
                if (itemList.size() >= 8) {
                    repositoryItem.setGamePassWord(itemList.get(7));// 游戏密码
                }
                if (itemList.size() >= 9) {
                    repositoryItem.setSellerGameRole(itemList.get(8));// 游戏角色名
                }
                if (itemList.size() >= 10) {
                    repositoryItem.setSonGamePassWord(itemList.get(9));// 二级密码
                }
                if (itemList.size() >= 11) {
                    repositoryItem.setGameRace(itemList.get(10));// 阵营
                }
                if (itemList.size() >= 12) {
                    repositoryItem.setRolePassword(itemList.get(11));// 人物密码
                }
                if (itemList.size() >= 13) {
                    repositoryItem.setFundsPassword(itemList.get(12));// 财产密码
                }
                if (itemList.size() >= 14) {
                    repositoryItem.setHousePassword(itemList.get(13));// 仓库密码
                }
                if (itemList.size() >= 15) {
                    repositoryItem.setLoginAccount(itemList.get(14));// 卖家账号
                }
            }
            String loginAccount = repositoryItem.getLoginAccount();
            SellerInfo sellerInfo = sellerDBDAO.selectUniqueByProp(
                    "loginAccount", loginAccount);
            if (sellerInfo == null) {
                throw new SystemException(
                        ResponseCodes.EmptySellerInfo.getCode());
            }
            repositoryItem.setAccountUid(String.valueOf(sellerInfo.getId()));
            repositoryItem.setServicerId(sellerInfo.getServicerId());
            /*********************ZW_C_JB_00008_20170518 ADD ATART***********************/
            if (repositoryItem.getSellableCount() == null) {
                repositoryItem.setSellableCount(repositoryItem.getGoldCount());
            }
            /**********************ZW_C_JB_00008_20170518 ADD END**********************/
            repositoryInfos.add(repositoryItem);

//            gameAccountManager.updateRepositoryCount(repositoryItem.getLoginAccount(), repositoryItem.getGameName(), repositoryItem.getRegion(), repositoryItem.getServer(), repositoryItem.getGameRace(), repositoryItem.getGameAccount(), repositoryItem.getSellerGameRole(), repositoryItem.getSellableCount(), repositoryItem.getGoodsTypeName());

//            //添加收货商日志
//            StringBuffer sbData = new StringBuffer();
//            sbData.append("商城批量新增库存，当前库存：").append(repositoryItem.getSellableCount());
//            saveLog(repositoryItem, sbData.toString());
        }
        if (errorMsgs.size() > 0) {
            throw new SystemException(
                    ResponseCodes.RepositoryGoldLessZero.getCode(),
                    errorMsgs.toString());
        }
        this.clearAddRepositoryData();
        for (RepositoryInfo repositoryItem : repositoryInfos) {
            this.addRepository(repositoryItem);
        }
        this.commitAddRepositoryData();
        return repositoryInfos;
    }

    private String getStringCellValue(HSSFCell cell) {
        if (cell == null) {
            return null;
        }
        String strCell = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }

    @Override
    public long queryMaxCount(String gameName, String region, String server, String gameRace, Long servicerId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("gameName", gameName);
        queryMap.put("region", region);
        queryMap.put("server", server);
        queryMap.put("gameRace", gameRace);
        queryMap.put("servicerId", servicerId);
        queryMap.put("isDeleted", false);

        queryMap = processRepositoryTransfer(queryMap);

        return repositoryDBDAO.selectGoldCount(queryMap);
    }

    /**
     * 根据游戏区服阵营单价卖家查询最大库存数量，卖家账号可以为空
     * <p>
     * ZW_C_JB_00008_20170512 增加通货类型 update by hyl
     *
     * @param gameName
     * @param region
     * @param server
     * @param gameRace
     * @param goodsTypeName
     * @param sellerLoginAccount 卖家5173账号
     * @param unitPrice          库存单价
     * @return
     */
    @Override
    public long queryRepositoryMaxCount(String gameName, String goodsTypeName, String region, String server, String gameRace,
                                        String sellerLoginAccount, BigDecimal unitPrice) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("gameName", gameName);
        queryMap.put("region", region);
        queryMap.put("server", server);
        queryMap.put("gameRace", gameRace);
        queryMap.put("loginAccount", sellerLoginAccount);
        queryMap.put("orderUnitPrice", unitPrice);
        queryMap.put("isDeleted", false);
        queryMap.put("goodsTypeName", goodsTypeName);

        queryMap = processRepositoryTransfer2(queryMap);
        queryMap.put("server", server);
        //只有dnf游戏币合区合服
        if (StringUtils.isNotBlank(goodsTypeName) && goodsTypeName.equals("游戏币") && "地下城与勇士".equals(gameName)) {
            return repositoryDBDAO.selectSalableCount(queryMap);
        }
        //dnf的其他商品类型不考虑合区合服
        if ("地下城与勇士".equals(gameName)) {
            queryMap.remove("servers");
        }
        //其他游戏如果有合区合服的配置，就用合区合服的配置
        if (queryMap.get("servers") != null) {
            queryMap.remove("server");
        }
        return repositoryDBDAO.selectGoldCount(queryMap);
    }

    /**
     * 对合服服务器库存互通查询参数进行处理
     *
     * @param queryMap
     * @return
     */
    @Override
    public Map<String, Object> processRepositoryTransfer(Map<String, Object> queryMap) {
        String gameName = (String) queryMap.get("gameName");
        String backGameName = (String) queryMap.get("backGameName");

        if (StringUtils.equals(gameName, "剑灵") || StringUtils.equals(backGameName, "剑灵")) {
            queryMap.remove("backRegion");
            queryMap.remove("region");
            return bnsRepositoryTransferManager.process(queryMap);
        } else if (StringUtils.equals(gameName, "魔兽世界(国服)") || StringUtils.equals(backGameName, "魔兽世界(国服)")) {
            return wowRepositoryTransferManager.process(queryMap);
        } else if (StringUtils.equals(gameName, "剑侠情缘Ⅲ") || StringUtils.equals(backGameName, "剑侠情缘Ⅲ")) {
            return jx3RepositoryTransferManager.process(queryMap);
        } else if (StringUtils.equals(gameName, "疾风之刃") || StringUtils.equals(backGameName, "疾风之刃")) {
            return jfRepositoryTransferManager.process(queryMap);
        } else if (StringUtils.equals(gameName, "新天龙八部") || StringUtils.equals(backGameName, "新天龙八部")) {
            return xtlbbRepositoryTransferManager.process(queryMap);
        }

        return queryMap;
    }

    /**
     * 对合服服务器库存互通查询参数进行处理
     *
     * @param queryMap
     * @return
     */
    @Override
    public Map<String, Object> processRepositoryTransfer2(Map<String, Object> queryMap) {
        String gameName = (String) queryMap.get("gameName");
        String backGameName = (String) queryMap.get("backGameName");

        if (StringUtils.equals(gameName, "剑灵") || StringUtils.equals(backGameName, "剑灵")) {
            queryMap.remove("backRegion");
            queryMap.remove("region");
            return bnsRepositoryTransferManager.process(queryMap);
        } else if (StringUtils.equals(gameName, "魔兽世界(国服)") || StringUtils.equals(backGameName, "魔兽世界(国服)")) {
            return wowRepositoryTransferManager.process(queryMap);
        } else if (StringUtils.equals(gameName, "剑侠情缘Ⅲ") || StringUtils.equals(backGameName, "剑侠情缘Ⅲ")) {
            return jx3RepositoryTransferManager.process(queryMap);
        } else if (StringUtils.equals(gameName, "疾风之刃") || StringUtils.equals(backGameName, "疾风之刃")) {
            return jfRepositoryTransferManager.process(queryMap);
        } else if (StringUtils.equals(gameName, "新天龙八部") || StringUtils.equals(backGameName, "新天龙八部")) {
            return xtlbbRepositoryTransferManager.process(queryMap);
        } else if (StringUtils.equals(gameName, "地下城与勇士") || StringUtils.equals(backGameName, "地下城与勇士")) {
            queryMap.remove("backRegion");
            queryMap.remove("region");
            return dnfRepositoryTransferManager.process(queryMap);
        }

        return queryMap;
    }

    /**
     * 对合服服务器库存互通查询参数进行处理
     *
     * @param queryMap
     * @return
     */
    @Override
    public Map<String, Object> processRepositoryTransfer(Map<String, Object> queryMap, String goodsTypeName) {
        String gameName = (String) queryMap.get("gameName");
        String backGameName = (String) queryMap.get("backGameName");

        if (StringUtils.equals(gameName, "剑灵") || StringUtils.equals(backGameName, "剑灵")) {
            queryMap.remove("backRegion");
            queryMap.remove("region");
            return bnsRepositoryTransferManager.process(queryMap);
        } else if (StringUtils.equals(gameName, "魔兽世界(国服)") || StringUtils.equals(backGameName, "魔兽世界(国服)")) {
            return wowRepositoryTransferManager.process(queryMap);
        } else if (StringUtils.equals(gameName, "剑侠情缘Ⅲ") || StringUtils.equals(backGameName, "剑侠情缘Ⅲ")) {
            return jx3RepositoryTransferManager.process(queryMap);
        } else if (StringUtils.equals(gameName, "疾风之刃") || StringUtils.equals(backGameName, "疾风之刃")) {
            return jfRepositoryTransferManager.process(queryMap);
        } else if (StringUtils.equals(gameName, "新天龙八部") || StringUtils.equals(backGameName, "新天龙八部")) {
            return xtlbbRepositoryTransferManager.process(queryMap);
        } else if ((StringUtils.equals(gameName, "地下城与勇士") || StringUtils.equals(backGameName, "地下城与勇士")) && ServicesContants.GOODS_TYPE_GOLD.equals(goodsTypeName)) {
            queryMap.remove("backRegion");
            queryMap.remove("region");
            return dnfRepositoryTransferManager.process(queryMap);
        }

        return queryMap;
    }

    /**
     * 最低价最大库存数据
     *
     * @param gameName,region,gameRace
     * @return
     */
    public List<RepositoryInfo> queryLowerPriceList(String gameName, String region, String gameRace) {
        List<RepositoryInfo> listRepositoryInfo = new ArrayList<RepositoryInfo>();//暂存最低价最大库存数据

        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("gameName", gameName);
        queryMap.put("region", region);
        queryMap.put("race", gameRace == null ? "" : gameRace);

        /*****************新增根据游戏名称和商品类型查询最低购买金额_20170607_MODIFY_START***********/
        if (StringUtils.isNotBlank(gameName)) { //新增参数判断
            ShGameConfig config = shGameConfigManager.getConfigByGameName(gameName, ServicesContants.GOODS_TYPE_GOLD, null, null);
            if (config != null && config.getMinBuyAmount() != null) {
                queryMap.put("unitPrice", config.getMinBuyAmount());
            }
        }
        /****************新增根据游戏名称和商品ID查询最低购买金额_20170607_MODIFY_END*************/

        List<RepositoryInfo> serverList = repositoryDBDAO.queryServerList(queryMap);//获取当前大区下的所有服务器
        Map<String, Object> mapTemp = new HashMap<String, Object>();

        //循环获取所有合并的服务器并保存到map中
        for (RepositoryInfo server : serverList) {
            String serverName = server.getServer();
            queryMap.put("server", serverName);
            Map<String, Object> map = processRepositoryTransfer(queryMap);

            //过滤合并的区服，只取其一
            if (map.get("servers") != null && map.get("servers") != "") {
                if (!mapTemp.containsValue(map.get("servers"))) {
                    mapTemp.put(serverName, map.get("servers"));
                }
            } else {
                if (map.get("server") == null) {
                    mapTemp.put(serverName, serverName);
                } else {
                    if (!mapTemp.containsValue(map.get("server"))) {
                        mapTemp.put(serverName, map.get("server"));
                    }
                }
            }
        }

        //实现合并服务器的拆分数据
        for (String key : mapTemp.keySet()) {
            queryMap.put("server", key);
            List<RepositoryInfo> list = null;
            List<String> serverTempList = new ArrayList<String>();
            Map<String, Object> map = processRepositoryTransfer(queryMap);

            if (map.get("servers") != null && map.get("servers") != "") {
                serverTempList = Arrays.asList(mapTemp.get(key).toString().split(","));
                list = repositoryDBDAO.queryLowerPriceList(processRepositoryTransfer(queryMap));
                queryMap.remove("servers");
            } else {
                serverTempList.add(mapTemp.get(key).toString());
                queryMap.put("servers", "'" + mapTemp.get(key).toString() + "'");
                list = repositoryDBDAO.queryLowerPriceList(queryMap);
                queryMap.remove("servers");
            }
            //List<String> serverTempList=Arrays.asList(mapTemp.get(key).toString().split(","));
            if (list.size() > 0) {
                for (String serverName : serverTempList) {
                    RepositoryInfo repositoryInfo = new RepositoryInfo();
                    RepositoryInfo repositoryInfoTemp = list.get(0);
                    repositoryInfoTemp.setServer(serverName.replace("'", ""));
                    repositoryInfoTemp.setGameRace(gameRace);
                    BeanMapper.copy(repositoryInfoTemp, repositoryInfo);
                    listRepositoryInfo.add(repositoryInfo);
                }
            }
        }
        return listRepositoryInfo;
    }

    /**
     * 增加库存数量
     *
     * @param repository
     * @param count
     * @param orderId    订单号
     */
    @Override
    @Transactional
    public void incrRepositoryCount(RepositoryInfo repository, Long count, String orderId) {
        repositoryDBDAO.incrRepositoryCount(repository.getId(), count, null);

//        //增加收货系统角色库存
//        gameAccountManager.updateRepositoryCount(repository.getLoginAccount(), repository.getGameName(), repository.getRegion(), repository.getServer(), repository.getGameRace(), repository.getGameAccount(), repository.getSellerGameRole(), repository.getSellableCount().longValue() + count, repository.getGoodsTypeName());

//        //添加收货商日志
//        StringBuffer sbData = new StringBuffer();
//        sbData.append("商城增加库存，当前库存：").append(repository.getSellableCount()).append("+").append(count);
//        saveLog(repository, sbData.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("当前可销售库存:").append(repository.getSellableCount()).append("+").append(count);
        sb.append(",单价:").append(repository.getUnitPrice());

        if (StringUtils.isNotBlank(orderId)) {
            sb.append(",订单号:").append(orderId);
        }

        RepositoryLogInfo logInfo = new RepositoryLogInfo();
        logInfo.setLogType(LogType.REPOSITORY_MODIFY);
        logInfo.setRemark(sb.toString());
        logInfo.setGoodsTypeName(repository.getGoodsTypeName());  /**ZW_C_JB_00008_2017/5/15 add **/
        repositoryLogManager.add(logInfo, repository);
    }

    /**
     * 减少库存数量
     *
     * @param repository
     * @param count
     * @param orderId    订单号
     */
    @Override
    @Transactional
    public void decrRepositoryCount(RepositoryInfo repository, Long count, String orderId) {
        try {
            repositoryDBDAO.decrRepositoryCount(repository.getId(), count);

//            //减少收货系统角色库存
//            gameAccountManager.updateRepositoryCount(repository.getLoginAccount(), repository.getGameName(), repository.getRegion(), repository.getServer(), repository.getGameRace(), repository.getGameAccount(), repository.getSellerGameRole(), repository.getSellableCount().longValue() - count, repository.getGoodsTypeName());

//            //添加收货商日志
//            StringBuffer sbData = new StringBuffer();
//            sbData.append("商城减少库存，当前库存：").append(repository.getSellableCount()).append("-").append(count);
//            saveLog(repository, sbData.toString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SystemException(ResponseCodes.InventoryShortage.getCode(), ResponseCodes.InventoryShortage.getMessage());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("当前可销售库存:").append(repository.getSellableCount()).append("-").append(count);
        sb.append(",单价:").append(repository.getUnitPrice());

        if (StringUtils.isNotBlank(orderId)) {
            sb.append(",订单号:").append(orderId);
        }

        RepositoryLogInfo logInfo = new RepositoryLogInfo();
        logInfo.setLogType(LogType.REPOSITORY_MODIFY);
        logInfo.setRemark(sb.toString());
        logInfo.setGoodsTypeName(repository.getGoodsTypeName());  /**ZW_C_JB_00008_2017/5/15 add **/
        repositoryLogManager.add(logInfo, repository);
    }

    /**
     * 店铺订单列表
     *
     * @param gameName
     * @param region
     * @param serverName
     * @param gameRace
     * @return
     */
    @Override
    @Transactional
    public GenericPage<RepositoryInfo> querySellerRepositoryList(String category, String gameName, String region, String serverName, String gameRace, int pageSize, int startIndex, String orderBy, boolean isAsc) {
        int count = 0;
        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "UNIT_PRICE";
        }
        Map<String, Object> queryMap = new HashMap<String, Object>();
//        if (StringUtils.isBlank(category) || "全部类型".equals(category)) {
//            List<ShGameConfig> configList = shGameConfigManager.getConfigByGameNameAndSwitch(gameName, null, true);
//            for (ShGameConfig config : configList) {
//                goodsTypeNameList.add(config.getGoodsTypeName());
//            }
//            queryMap.put("goodsTypeNameList", goodsTypeNameList);
//        } else
        if (StringUtils.isNotBlank(category) && !"全部类型".equals(category)) {
            queryMap.put("goodsTypeName", category);
        }
        queryMap.put("gameName", gameName);
        queryMap.put("region", region);
        queryMap.put("gameRace", gameRace == null ? "" : gameRace);
        queryMap.put("server", serverName);
        List<RepositoryInfo> list;
        //增加regions servers
        Map<String, Object> map = processRepositoryTransfer2(queryMap);
        map.put("backRegion", region);
        map.put("region", region);
        if (map.get("servers") != null && !map.get("servers").equals("")) {
            if (isAsc) {
                map.put("ORDERBY", "\"SORT\" DESC,\"" + orderBy + "\" ASC");
            } else {
                map.put("ORDERBY", "\"SORT\" DESC,\"" + orderBy + "\" DESC");
            }
            map.put("limit", pageSize);
            map.put("start", startIndex * pageSize);
            if ("地下城与勇士".equals(gameName) && category.equals(ServicesContants.GOODS_TYPE_GOLD)) {
                //地下城与勇士+游戏币的 用新sql
                map.put("server", serverName);
                list = repositoryDBDAO.querySellerRepositoryList(map);
            } else if ((gameName.equals("地下城与勇士") && !category.equals(ServicesContants.GOODS_TYPE_GOLD))) {
                //地下城勇士 除游戏币之外的  servers替换成单个的 还是用旧sql
                map.put("servers", "'" + serverName + "'");
                list = repositoryDBDAO.queryUniqueAndOldSellerRepository(map);
            } else {
                //旧的还是用旧sql 参数及属性不做替换
                list = repositoryDBDAO.queryUniqueAndOldSellerRepository(map);
            }
            System.out.println(list.toString());
        } else if (StringUtils.isBlank(serverName)) {
            //服务器为空 即不做跨区服查询  沿用旧sql
            SetMap(queryMap, pageSize, startIndex, orderBy, isAsc, serverName);
            map.put("servers", null);
            list = repositoryDBDAO.queryUniqueAndOldSellerRepository(queryMap);
        } else {
            //servers 为空沿用旧sql 表示没有跨区服信息  按照单一的来
            SetMap(queryMap, pageSize, startIndex, orderBy, isAsc, serverName);
            queryMap.put("servers", "'" + serverName + "'");
            list = repositoryDBDAO.queryUniqueAndOldSellerRepository(queryMap);
        }
        return new GenericPage<RepositoryInfo>(startIndex, count, pageSize, list);
    }

    /**
     * 店铺订单笔数
     *
     * @param gameName
     * @param region
     * @param serverName
     * @param gameRace
     * @return
     */
    @Override
    public int countSellerGoodslist(String gameName, String region, String serverName, String gameRace, int pageSize, String goodsTypeName) {
        int count = 0;
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("gameName", gameName);
        queryMap.put("region", region);
        queryMap.put("gameRace", gameRace == null ? "" : gameRace);
        queryMap.put("goodsTypeName", goodsTypeName);
        queryMap.put("server", serverName);
        //增加了servers 与regions
        Map<String, Object> map = processRepositoryTransfer2(queryMap);
        map.put("backRegion", region);
        map.put("region", region);
        if (map.get("servers") != null && !map.get("servers").equals("")) {
            if ("地下城与勇士".equals(gameName) && goodsTypeName.equals(ServicesContants.GOODS_TYPE_GOLD)) {
                //地下城与勇士+游戏币的 用新sql
                map.put("server", serverName);
                count = repositoryDBDAO.countSellerRepository(map);
            } else if ((gameName.equals("地下城与勇士") && !goodsTypeName.equals(ServicesContants.GOODS_TYPE_GOLD))) {
                //地下城勇士 除游戏币之外的  servers替换成单个的 还是用旧sql
                map.put("servers", "'" + serverName + "'");
                count = repositoryDBDAO.countUniqueAndOldSellerRepository(map);
            } else {
                //旧的还是用旧sql 参数及属性不做替换
                count = repositoryDBDAO.countUniqueAndOldSellerRepository(map);
            }
        } else if (StringUtils.isBlank(serverName)) {
            map.put("servers", null);
            count = repositoryDBDAO.countUniqueAndOldSellerRepository(map);
        } else {
            queryMap.put("servers", "'" + serverName + "'");
            count = repositoryDBDAO.countUniqueAndOldSellerRepository(queryMap);
        }
        if (count % pageSize == 0)
            return count / pageSize;
        else
            return count / pageSize + 1;
    }

    private void SetMap(Map<String, Object> queryMap, int pageSize, int startIndex, String orderBy, boolean isAsc,
                        String serverName) {
        //queryMap.put("ORDERBY", orderBy);
        if (StringUtils.isBlank(serverName)) {
            if (isAsc) {
                queryMap.put("ORDERBY", "\"" + orderBy + "\" ASC");
            } else {
                queryMap.put("ORDERBY", "\"" + orderBy + "\" DESC");
            }
        } else {
            if (isAsc) {
                queryMap.put("ORDERBY", "\"SORT\" DESC,\"" + orderBy + "\" ASC");
            } else {
                queryMap.put("ORDERBY", "\"SORT\" DESC,\"" + orderBy + "\" DESC");
            }
        }

//        if(isAsc){
//            queryMap.put("ORDER", "ASC");
//        }else{
//            queryMap.put("ORDER", "DESC");
//        }
        queryMap.put("limit", pageSize);
        queryMap.put("start", startIndex * pageSize);
    }

    /**
     * 前台最低价格列表
     *
     * @param gameName
     * @param region
     * @param server
     * @param race
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<RepositoryInfo> queryPriceRepository(String gameName, String region, String server, String race, int start, int pageSize) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("backGameName", gameName);
        queryMap.put("backRegion", region);
        queryMap.put("backServer", server);
        queryMap.put("gameRace", race);
        queryMap.put("isOnline", true);

        /*****根据游戏名称和商品类型查询最低购买金额_20170607_ADD_'新增最低购买金额'_START********/
        if (StringUtils.isNotBlank(gameName)) { //新增参数判断
            ShGameConfig config = shGameConfigManager.getConfigByGameName(gameName, ServicesContants.GOODS_TYPE_GOLD, null, null);
            if (config != null && config.getMinBuyAmount() != null) {
                queryMap.put("unitPrice", config.getMinBuyAmount());
            }
        }
        /*****根据游戏名称和商品类型查询最低购买金额_20170607_ADD_'新增最低购买金额'_END********/

        List<SortField> sortFields = new ArrayList<SortField>();
        sortFields.add(new SortField("UNIT_PRICE", SortField.ASC));
        sortFields.add(new SortField("SELLABLE_COUNT", SortField.DESC));

        queryMap = processRepositoryTransfer(queryMap);

        //取缓存数据
        List<RepositoryInfo> list = repositoryRedisDAO.queryRepositoryPrice(gameName, region, server, race, "lowerPrice");
        if (list == null) {
            list = repositoryDBDAO.queryPriceRepository(queryMap, sortFields, start, pageSize);
            repositoryRedisDAO.addRepositoryPrice(gameName, region, server, race, list, "lowerPrice", 5, TimeUnit.MINUTES);
        }
        return list;
    }

    /**
     * 分页获取所有区服的最低价
     *
     * @param gameName
     * @param region
     * @param server
     * @param race
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public GenericPage<RepositoryInfo> selectLowestList(String loginAccount, String gameName, String region, String server, String race, List<SortField> sortFields, int start, int pageSize) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("backGameName", gameName);
        queryMap.put("backRegion", region);
        queryMap.put("backServer", server);
        queryMap.put("gameRace", race);
        queryMap.put("isOnline", true);
        queryMap.put("loginAccount", loginAccount);

        if (StringUtils.isNotBlank(gameName)) { //新增参数判断
            ShGameConfig config = shGameConfigManager.getConfigByGameName(gameName, ServicesContants.GOODS_TYPE_GOLD, null, null);
            if (config != null && config.getMinBuyAmount() != null) {
                queryMap.put("minBuyAmount", config.getMinBuyAmount());
            }
        }

        queryMap = processRepositoryTransfer(queryMap);

        return repositoryDBDAO.selectLowestList(queryMap, sortFields, start, pageSize);
    }

    /**
     * 店铺卖家平均价查询
     *
     * @param gameName
     * @param region
     * @param serverName
     * @param gameRace
     * @return
     */
    @Override
    @Transactional
    public List<RepositoryInfo> querySellerAvgPriceRepository(String gameName, String region, String serverName, String gameRace, String goodsTypeName) {
        logger.info("店铺卖家平均价查询条件,gameName:{}, region:{}, serverName:{}, gameRace:{}",
                new Object[]{gameName, region, serverName, gameRace});
        int count = 0;
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("gameName", gameName);
        queryMap.put("region", region);
        queryMap.put("gameRace", gameRace == null ? "" : gameRace);
        if (StringUtils.isBlank(gameName)) {
            throw new SystemException(ResponseCodes.EmptyGameName.getCode(), "游戏名称不能为空");
        }

        /*****************新增根据游戏名称和商品类型查询最低购买金额_20170607_MODIFY_START***********/
        if (StringUtils.isNotBlank(gameName)) { //新增参数判断
            ShGameConfig config = shGameConfigManager.getConfigByGameName(gameName, StringUtils.isBlank(goodsTypeName) ? ServicesContants.GOODS_TYPE_GOLD : goodsTypeName, null, null);
            if (config != null && config.getMinBuyAmount() != null) {
                queryMap.put("unitPrice", config.getMinBuyAmount());
            }
        }
        /****************新增根据游戏名称和商品ID查询最低购买金额_20170607_MODIFY_END*************/

        queryMap.put("server", serverName);
        /** ZW_C_JB_00008_20170518_START ADD **/
        if (StringUtils.isNotBlank(goodsTypeName)) {
            queryMap.put("goodsTypeName", goodsTypeName);
        } else {
            queryMap.put("goodsTypeName", ServicesContants.GOODS_TYPE_GOLD);
        }
        /** ZW_C_JB_00008_20170518_END **/
        List<RepositoryInfo> list;
        Map<String, Object> map = processRepositoryTransfer(queryMap);

        if (map.get("servers") != null && !map.get("servers").equals("")) {
            list = repositoryDBDAO.querySellerAvgPriceRepository(map);
        } else if (map.get("server") == null || map.get("server").equals("")) {
            queryMap.remove("server");
            queryMap.put("server", "");
            list = repositoryDBDAO.querySellerAvgPriceRepository(queryMap);
        } else {
            queryMap.put("servers", "'" + serverName + "'");
            list = repositoryDBDAO.querySellerAvgPriceRepository(queryMap);
        }
        logger.info("店铺卖家平均价查询结果,list:{}", list);
        return list;
    }


    /**
     * 更新库存
     *
     * @param loginAccount
     * @param gameAccount
     * @param gameRole
     * @param gameName
     * @param regionName
     * @param serverName
     * @param gameRace
     * @param count
     */
    @Override
    @Transactional
    public void updateRepositoryCount(String loginAccount, String gameAccount, String gameRole, String gameName, String regionName, String serverName, String gameRace, Long count, String goodsTypeName) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("loginAccount", loginAccount);
        queryMap.put("backGameName", gameName);
        queryMap.put("backRegion", regionName);
        queryMap.put("backServer", serverName);
        queryMap.put("backGameRace", gameRace);
        queryMap.put("gameAccount", gameAccount);
        queryMap.put("sellerGameRole", gameRole);
        queryMap.put("isDeleted", false);
        queryMap.put("lockMode", true);
        queryMap.put("goodsTypeName", goodsTypeName);

        List<RepositoryInfo> repositoryInfoList = repositoryDBDAO.selectByMap(queryMap, "CREATE_TIME", false);
        if (repositoryInfoList != null && repositoryInfoList.size() > 0) {
            RepositoryInfo dbExsitRepository = repositoryInfoList.get(0);
            dbExsitRepository.setSellableCount(count);
            dbExsitRepository.setGoldCount(count);
            dbExsitRepository.setLastUpdateTime(new Date());
            repositoryDBDAO.update(dbExsitRepository);
        }
    }

    /**
     * 更新库存
     *
     * @param loginAccount
     * @param gameAccount
     * @param gameRole
     * @param gameName
     * @param regionName
     * @param serverName
     * @param gameRace
     * @param count
     * @param isAdd
     */
    @Override
    @Transactional
    public void updateRepositoryCount(String loginAccount, String gameAccount, String gameRole, String gameName, String regionName, String serverName, String gameRace, Long count, Boolean isAdd) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("loginAccount", loginAccount);
        queryMap.put("backGameName", gameName);
        queryMap.put("backRegion", regionName);
        queryMap.put("backServer", serverName);
        queryMap.put("backGameRace", gameRace);
        queryMap.put("gameAccount", gameAccount);
        queryMap.put("sellerGameRole", gameRole);
        queryMap.put("isDeleted", false);
        queryMap.put("lockMode", true);

        List<RepositoryInfo> repositoryInfoList = repositoryDBDAO.selectByMap(queryMap, "CREATE_TIME", false);
        if (repositoryInfoList != null && repositoryInfoList.size() > 0) {
            RepositoryInfo dbExsitRepository = repositoryInfoList.get(0);
            if (isAdd) {
                dbExsitRepository.setSellableCount(dbExsitRepository.getSellableCount().longValue() + count);
                dbExsitRepository.setGoldCount(dbExsitRepository.getSellableCount().longValue() + count);
            } else {
                dbExsitRepository.setSellableCount(dbExsitRepository.getSellableCount().longValue() - count);
                dbExsitRepository.setGoldCount(dbExsitRepository.getSellableCount().longValue() - count);
            }
            dbExsitRepository.setLastUpdateTime(new Date());
            repositoryDBDAO.update(dbExsitRepository);
        }
    }

    /**
     * 写入收货商日志
     *
     * @param repositoryInfo
     * @param logData
     */
    private void saveLog(RepositoryInfo repositoryInfo, String logData) {
        if (!ServicesContants.GOODS_TYPE_GOLD.equals(repositoryInfo.getGoodsTypeName())) {
            return;
        }

        SplitRepositoryLog splitLog = new SplitRepositoryLog();
        splitLog.setBuyerAccount(repositoryInfo.getLoginAccount());
        splitLog.setGameName(repositoryInfo.getGameName());
        splitLog.setRegion(repositoryInfo.getRegion());
        splitLog.setServer(repositoryInfo.getServer());
        splitLog.setGameRace(repositoryInfo.getGameRace());
        splitLog.setGameAccount(repositoryInfo.getGameAccount());
        splitLog.setLog(logData);
        splitLog.setCreateTime(new Date());
        splitRepositoryLogManager.saveLog(splitLog);
    }


    /**
     * map查询
     *
     * @param queryMap
     */
    @Override
    public List<RepositoryInfo> selectRepositoryByMap(Map queryMap) {
        //获取用户对应的客服
        IUser seller = CurrentUserContext.getUser();
        SellerInfo sellerInfo = sellerDBDAO.selectUniqueByProp(
                "loginAccount", seller.getLoginAccount());
        if (sellerInfo == null) {
            throw new SystemException(
                    ResponseCodes.EmptySellerInfo.getCode());
        }
        //判断用户是否拥有寄售权限，没有的情况下跳出
        if (!sellerInfo.getIsShieldedType()) {
            throw new SystemException(
                    ResponseCodes.NotSellerForSync.getCode(), ResponseCodes.NotSellerForSync.getMessage());
        }
        return repositoryDBDAO.selectByMap(queryMap, "CREATE_TIME", false);
    }
}