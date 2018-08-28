package com.wzitech.gamegold.repository.business.impl;

import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.repository.dao.IRepositoryDBDAO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.shorder.business.IGameAccountManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 339928 on 2018/5/23.
 */
@Repository
public class AysncDBIOImple {
    @Autowired
    private IRepositoryDBDAO repositoryDBDAO;
    @Autowired
    private IGameAccountManager gameAccountManager;

    protected static final Log logger = LogFactory.getLog(AysncDBIOImple.class);

    @Async
    public void aysnDBIO(List<RepositoryInfo> repositoryInfoListUpload, IUser user, List<RepositoryInfo> updateRepositoryList, List<RepositoryInfo> insertRepositoryList) {
//        logger.info("异步更新库存信息，"+repositoryInfoListUpload);
        for (RepositoryInfo repositoryInfo : repositoryInfoListUpload) {
            // 保存到数据库
            // 查询是否已存在该卖家，相同的库存信息，如果存在，目前按照覆盖的原则，而不是累加
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("loginAccount", repositoryInfo.getLoginAccount()); //
            queryMap.put("accountUid", repositoryInfo.getAccountUid());//
            queryMap.put("backGameName", repositoryInfo.getGameName());//
            queryMap.put("backRegion", repositoryInfo.getRegion());//
            queryMap.put("backServer", repositoryInfo.getServer());//
            queryMap.put("backGameRace", repositoryInfo.getGameRace());//
            queryMap.put("gameAccount", repositoryInfo.getGameAccount());//
            queryMap.put("sellerGameRole", repositoryInfo.getSellerGameRole());//
            queryMap.put("goodsTypeName", repositoryInfo.getGoodsTypeName());//ZW_C_JB_00008_20170517 ADD
            queryMap.put("isDeleted", false);
            queryMap.put("lockMode", true);
            List<RepositoryInfo> repositoryInfoList = repositoryDBDAO.selectByMap(queryMap, "CREATE_TIME", false);
            if (!CollectionUtils.isEmpty(repositoryInfoList)) {
                RepositoryInfo dbExsitRepository = repositoryInfoList.get(0);
                repositoryInfo.setId(dbExsitRepository.getId());
                repositoryInfo.setIsDeleted(false);
//                logger.info("更新库存信息:" + "登录名:" + user.getLoginAccount() + "游戏名:" + repositoryInfo.getGameName() + ",区:" + repositoryInfo.getRegion() + "服务器:" + repositoryInfo.getServer() +
//                        "商品种类:" + repositoryInfo.getGoodsTypeName() + ",货币单位" + repositoryInfo.getMoneyName() + ",货币单价:" + repositoryInfo.getUnitPrice() +
//                        ",库存数量" + repositoryInfo.getGoldCount() + ",可销售库存:" + repositoryInfo.getSellableCount() + ",游戏账号:" + repositoryInfo.getGameAccount() +
//                        ",游戏账号密码:" + repositoryInfo.getGamePassWord());
                updateRepositoryList.add(repositoryInfo);
            } else {
                repositoryInfo.setCreateTime(new Date());
//                logger.info("增加库存信息:" + "登录名:" + user.getLoginAccount() + ",游戏名:" + repositoryInfo.getGameName() + ",区:" + repositoryInfo.getRegion() + "服务器:" + repositoryInfo.getServer() +
//                        "商品种类:" + repositoryInfo.getGoodsTypeName() + ",货币单位" + repositoryInfo.getMoneyName() + ",货币单价:" + repositoryInfo.getUnitPrice() +
//                        ",库存数量" + repositoryInfo.getGoldCount() + ",可销售库存:" + repositoryInfo.getSellableCount() + ",游戏账号:" + repositoryInfo.getGameAccount() +
//                        ",游戏账号密码:" + repositoryInfo.getGamePassWord());
                insertRepositoryList.add(repositoryInfo);
            }
//            //收货系统库存
//            gameAccountManager.updateRepositoryCount(repositoryInfo.getLoginAccount(), repositoryInfo.getGameName(), repositoryInfo.getRegion(), repositoryInfo.getServer(),
//                    repositoryInfo.getGameRace(), repositoryInfo.getGameAccount(), repositoryInfo.getSellerGameRole(), repositoryInfo.getSellableCount(), repositoryInfo.getGoodsTypeName());
        }
        if (!CollectionUtils.isEmpty(updateRepositoryList)) {
            repositoryDBDAO.batchUpdate(updateRepositoryList);
        }
        if (!CollectionUtils.isEmpty(insertRepositoryList)) {
            repositoryDBDAO.batchInsert(insertRepositoryList);
        }
    }
}
