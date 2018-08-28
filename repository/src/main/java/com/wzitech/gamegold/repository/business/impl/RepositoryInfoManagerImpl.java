package com.wzitech.gamegold.repository.business.impl;

import com.wzitech.gamegold.repository.dao.IRepositoryDBDAO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.shorder.business.IRepositoryInfoManager;
import com.wzitech.gamegold.shorder.entity.GameAccount;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ljn
 * @date 2018/6/13.
 */
@Component("queryRepositoryInfo")
public class RepositoryInfoManagerImpl implements IRepositoryInfoManager {

    @Autowired
    IRepositoryDBDAO repositoryDBDAO;

    /**
     * 查询此角色是否存在销售库存表中
     *
     * @param account
     * @return
     */
    @Override
    public boolean countByGameAccount(GameAccount account) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loginAccount", account.getBuyerAccount());
        map.put("gameAccount", account.getGameAccount());
        map.put("gameName", account.getGameName());
        map.put("region", account.getRegion());
        map.put("server", account.getServer());
        map.put("gameRace", account.getGameRace());
        map.put("sellerGameRole", account.getRoleName());
        map.put("isDeleted",false);
        return repositoryDBDAO.countByMap(map) > 0;
    }

    /**
     * 删除掉库存数量
     *
     * @param queryMap
     */
    @Override
    public void decreaseSellableCount(Map<String, Object> queryMap) {
        repositoryDBDAO.updateRepositoryCountBySync(queryMap);
    }
//
//    @Override
//    public String selectSecondPassWord(Map<String, Object> queryMap) {
//        List<RepositoryInfo> repositoryInfos = repositoryDBDAO.selectByMap(queryMap);
//        String sonGamePassWord = null;
//        if (CollectionUtils.isNotEmpty(repositoryInfos)) {
//            sonGamePassWord = repositoryInfos.get(0).getSonGamePassWord();
//        }
//        return sonGamePassWord;
//    }
}
