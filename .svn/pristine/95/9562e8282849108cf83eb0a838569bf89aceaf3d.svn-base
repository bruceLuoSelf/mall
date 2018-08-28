package com.wzitech.gamegold.goods.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.goods.business.IMConnectionManager;
import com.wzitech.gamegold.goods.business.IRepositoryConfineManager;
import com.wzitech.gamegold.goods.dao.IReferencePriceDao;
import com.wzitech.gamegold.goods.dao.IReferencePriceRedisDao;
import com.wzitech.gamegold.goods.entity.ReferencePrice;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jhlcitadmin on 2017/3/16.
 */
@Component
public class MConnectionManagerImpl implements IMConnectionManager {

    @Autowired
    private IReferencePriceRedisDao redisDao;

    @Autowired
    private IReferencePriceDao referencePriceDao;

    @Autowired
    private IRepositoryConfineManager repositoryConfineManager;

    @Override
    public ReferencePrice getReferencePrice(ReferencePrice referencePrice) {
        ReferencePrice redisRfp = redisDao.get(referencePrice);
        //在redis为null的情况下 查数据库
        if (redisRfp == null) {
            //在数据库没有的情况下 计算参考价
            RepositoryInfo repositoryInfo = new RepositoryInfo();
            repositoryInfo.setGameName(referencePrice.getGameName());
            repositoryInfo.setServer(referencePrice.getServerName());
            repositoryInfo.setRegion(referencePrice.getRegionName());
            repositoryInfo.setGoodsTypeName(referencePrice.getGoodsTypeName());
            if (StringUtils.isNotBlank(referencePrice.getRaceName())) {
                repositoryInfo.setGameRace(referencePrice.getRaceName());
            }
            //查询最低参考价
            referencePrice = repositoryConfineManager.selectByMinUnitPrice(repositoryInfo);
            if (referencePrice == null || referencePrice.getUnitPrice() == null) {
                throw new SystemException("没有对应的参考价");
            }
            return referencePrice;
        }
        return redisRfp;
    }
}
