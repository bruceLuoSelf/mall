package com.wzitech.gamegold.goods.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.goods.entity.ReferencePrice;
import com.wzitech.gamegold.goods.entity.RepositoryConfine;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * Created by jhlcitadmin on 2017/3/20.
 */
public interface IRepositoryConfineManager {

    /**
     * 查询游戏库存中的最低价
     *
     */
    ReferencePrice selectByMinUnitPrice(RepositoryInfo info);

    //添加库存参考价游戏配置
    public void addRepositoryConfig(RepositoryConfine repositoryConfine);


    public GenericPage<RepositoryConfine> queryRepositoryList(Map<String, Object> queryMap, int limit, int start,
                                                              String sortBy, boolean isAsc);


    public int deleteConfig(RepositoryConfine repositoryConfine);

    public void enabledConfig(RepositoryConfine repositoryConfine);

    public void disableConfig(RepositoryConfine repositoryConfine);

    //根据游戏名称查询
    public RepositoryConfine selectRepositoryByMap(String gameName,String goodsTypeName);

    //修改
    public void updateConfig(RepositoryConfine repositoryConfine);

    //
    public List<RepositoryConfine> selectRepository();

    public void updateRepositoryCount(BigInteger repositoryCount);
}
