package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.shorder.business.ILevelCarryLimitManager;
import com.wzitech.gamegold.shorder.dao.ILevelCarryLimitDao;
import com.wzitech.gamegold.shorder.dao.ILevelCarryLimitRedisDao;
import com.wzitech.gamegold.shorder.entity.LevelCarryLimitEO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ljn on 2018/4/3.
 * 等级携带上限
 */
@Service
public class LevelCarryLimitManagerImpl implements ILevelCarryLimitManager {

    @Autowired
    private ILevelCarryLimitDao levelCarryLimitDao;

    @Autowired
    private ILevelCarryLimitRedisDao levelCarryLimitRedisDao;

    /**
     * 增加等级携带上限配置
     * @param levelCarryLimitEO
     */
    @Override
    @Transactional
    public void add(LevelCarryLimitEO levelCarryLimitEO) {
        checkParams(levelCarryLimitEO);
        levelCarryLimitDao.insert(levelCarryLimitEO);
        levelCarryLimitRedisDao.addLevelCarryLimit(levelCarryLimitEO);
    }

    /**
     * 检查参数
     * @param levelCarryLimitEO
     */
    private void checkParams(LevelCarryLimitEO levelCarryLimitEO) {
        if (levelCarryLimitEO.getMinLevel() == null) {
            throw new SystemException(ResponseCodes.EmptyMinLevel.getCode(),
                    ResponseCodes.EmptyMinLevel.getMessage());
        }
        if (levelCarryLimitEO.getMaxLevel() == null) {
            throw new SystemException(ResponseCodes.EmptyMaxLevel.getCode(),
                    ResponseCodes.EmptyMaxLevel.getMessage());
        }
        if (levelCarryLimitEO.getCarryUpperLimit() == null) {
            throw new SystemException(ResponseCodes.EmptyCarryUpperLimit.getCode(),
                    ResponseCodes.EmptyCarryUpperLimit.getMessage());
        }
        if (StringUtils.isBlank(levelCarryLimitEO.getGameName())) {
            throw new SystemException(ResponseCodes.EmptyGameName.getCode(),
                    ResponseCodes.EmptyGameName.getMessage());
        }
        if (levelCarryLimitEO.getGoodsTypeId() == null) {
                throw new SystemException(ResponseCodes.NullGoodsTypeId.getCode(),
                        ResponseCodes.NullGoodsTypeId.getMessage());
        }
        if (StringUtils.isBlank(levelCarryLimitEO.getGoodsTypeName())) {
            throw new SystemException(ResponseCodes.EmptyGoodsTypeName.getCode(),
                    ResponseCodes.EmptyGoodsTypeName.getMessage());
        }

    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        LevelCarryLimitEO limit = levelCarryLimitDao.selectById(id);
        levelCarryLimitDao.deleteById(id);
        levelCarryLimitRedisDao.deleteLevelCarryLimit(limit);
    }

    @Override
    @Transactional
    public void updateLevelCarryLimit(LevelCarryLimitEO levelCarryLimitEO) {
        checkParams(levelCarryLimitEO);
        levelCarryLimitDao.update(levelCarryLimitEO);
        LevelCarryLimitEO limit = levelCarryLimitDao.selectById(levelCarryLimitEO.getId());
        levelCarryLimitRedisDao.deleteLevelCarryLimit(limit);
        levelCarryLimitRedisDao.addLevelCarryLimit(levelCarryLimitEO);
    }

    @Override
    public GenericPage<LevelCarryLimitEO> selectByMap(Map<String, Object> map, int pageSize, int start, String orderBy, boolean isAsc) {
        if (map == null) {
            map = new HashMap<String,Object>();
        }
        return levelCarryLimitDao.selectByMap(map,pageSize,start,orderBy,isAsc);
    }

    @Override
    public LevelCarryLimitEO selectById(Long id) {
        return levelCarryLimitDao.selectById(id);
    }

    @Override
    public Long selectCarryUpperLimit(int level,String gameName,Long goodsTypeId) {
        LevelCarryLimitEO levelCarryLimit = levelCarryLimitRedisDao.getLevelCarryLimit(level, gameName, goodsTypeId);
        if (levelCarryLimit == null) {
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("level",level);
            map.put("gameName",gameName);
            map.put("goodsTypeId",goodsTypeId);
            levelCarryLimit = levelCarryLimitDao.selectCarryUpperLimit(map);
            if (levelCarryLimit == null) {
                throw new SystemException(ResponseCodes.EmptyCarryUpperLimitConfig.getCode(),
                        ResponseCodes.EmptyCarryUpperLimitConfig.getMessage());
            }
            levelCarryLimitRedisDao.addLevelCarryLimit(levelCarryLimit);
        }
        return levelCarryLimit.getCarryUpperLimit();
    }

    /**
     * 同步redis
     */
    @Override
    public void buildRedisData() {
        levelCarryLimitRedisDao.deleteAll();
        List<LevelCarryLimitEO> limitList = levelCarryLimitDao.selectAll();
        if (CollectionUtils.isEmpty(limitList)) {
            return;
        }
        for (LevelCarryLimitEO level : limitList) {
            levelCarryLimitRedisDao.addLevelCarryLimit(level);
        }
    }
}
