package com.wzitech.gamegold.goods.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.goods.business.IGameRepositoryManager;
import com.wzitech.gamegold.goods.dao.IGameRepositoryDao;
import com.wzitech.gamegold.goods.dao.IRepositoryConfineInfoRedisDao;
import com.wzitech.gamegold.goods.entity.RepositoryConfineInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${SunYang} on 2017/3/27.
 */
@Component
public class GameRepositoryManagerImpl extends AbstractBusinessObject implements IGameRepositoryManager {

    @Autowired
    IGameRepositoryDao gameRepositoryDao;
    @Autowired
    IRepositoryConfineInfoRedisDao repositoryConfineInfoRedisDao;

    /**
     * 分页查询
     *
     * @param queryMap
     * @param limit
     * @param start
     * @param sortBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<RepositoryConfineInfo> queryGameRepositoryList(Map<String, Object> queryMap,
                                                                      int limit, int start, String sortBy, boolean isAsc) {
        return gameRepositoryDao.selectByMap(queryMap, limit, start, sortBy, isAsc);
    }

    /**
     * 按游戏名查询
     *
     * @param gameName
     * @return
     */
    public RepositoryConfineInfo selectRepositoryByName(String gameName) {
        return gameRepositoryDao.selectRepositoryByName(gameName);
    }

    /**
     * 添加
     *
     * @param repositoryConfineInfo
     */
    public void addGameRepository(RepositoryConfineInfo repositoryConfineInfo) {
        if (repositoryConfineInfo != null) {
            gameRepositoryDao.insert(repositoryConfineInfo);
            //存入redis中
            repositoryConfineInfoRedisDao.addRepositoryConfineInfo(repositoryConfineInfo);
        }
    }

    /**
     * 修改
     *
     * @param repositoryConfine
     */
    public void update(RepositoryConfineInfo repositoryConfine) {
        gameRepositoryDao.update(repositoryConfine);
        repositoryConfineInfoRedisDao.addRepositoryConfineInfo(repositoryConfine);
    }


    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    @Override
    public RepositoryConfineInfo selectById(Long id) {
        return gameRepositoryDao.selectById(id);
    }

    /**
     * 开启
     *
     * @param repositoryConfine
     */
    public void enabledConfig(RepositoryConfineInfo repositoryConfine) {
        if (repositoryConfine.getId() == null) {
            throw new SystemException(ResponseCodes.EmptyId.getCode());
        }
        RepositoryConfineInfo repositoryConfig = gameRepositoryDao.selectById(repositoryConfine.getId());
        if (repositoryConfig != null) {
            if (repositoryConfig.getEnabled()) {
                throw new SystemException(ResponseCodes.RepositoryIsEnableWrong.getCode());
            }
            if (StringUtils.isBlank(repositoryConfig.getRaceName())) {
                repositoryConfig.setRaceName("");
            }
            repositoryConfig.setEnabled(true);
            gameRepositoryDao.update(repositoryConfig);
            repositoryConfineInfoRedisDao.addRepositoryConfineInfo(repositoryConfig);
        }
    }

    /**
     * 关闭
     *
     * @param repositoryConfine
     */
    public void disableConfig(RepositoryConfineInfo repositoryConfine) {
        if (repositoryConfine.getId() == null) {
            throw new SystemException(ResponseCodes.EmptyId.getCode());
        }
        RepositoryConfineInfo repositoryConfig = gameRepositoryDao.selectById(repositoryConfine.getId());
        if (repositoryConfig != null) {
            if (!repositoryConfig.getEnabled()) {
                throw new SystemException(ResponseCodes.RepositoryIsDisableWrong.getCode());
            }
            if (StringUtils.isBlank(repositoryConfig.getRaceName())) {
                repositoryConfig.setRaceName("");
            }
            repositoryConfig.setEnabled(false);
            gameRepositoryDao.update(repositoryConfig);
            repositoryConfineInfoRedisDao.deleteRepositoryConfineInfo(repositoryConfig);
        }
    }

    /**
     * 删除
     *
     * @param repositoryConfine
     */
    public void deleteConfig(RepositoryConfineInfo repositoryConfine) {
        //根据id查询名称
        RepositoryConfineInfo repositoryconfine = gameRepositoryDao.selectById(repositoryConfine.getId());
        //根据名称主键删除redis中的数据
        //start 交易流水统计 by汪俊杰 20170515
        if (StringUtils.isBlank(repositoryconfine.getRaceName())) {
            repositoryconfine.setRaceName("");
        }
        //end
        gameRepositoryDao.delete(repositoryconfine);
        repositoryConfineInfoRedisDao.deleteRepositoryConfineInfo(repositoryconfine);
    }

    /**
     * 查询全部
     */
    public GenericPage<RepositoryConfineInfo> selectAllForUpdate(int count, int startIndex) {

        Map<String, Object> queryMap = new HashMap<String, Object>();
        GenericPage<RepositoryConfineInfo> genericPage = gameRepositoryDao.selectByMap(queryMap, count, startIndex, "id", false);
        return genericPage;
    }
}
