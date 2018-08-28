package com.wzitech.gamegold.goods.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.goods.entity.RepositoryConfineInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by ${SunYang} on 2017/3/27.
 */
public interface IGameRepositoryManager {

    //分页
    public GenericPage<RepositoryConfineInfo> queryGameRepositoryList(Map<String, Object> queryMap, int limit, int start,
                                                                      String sortBy, boolean isAsc);

    //根据游戏名称查询
    public RepositoryConfineInfo selectRepositoryByName(String GameName);

    //添加库存参考价游戏配置
    public void addGameRepository(RepositoryConfineInfo repositoryConfine);

    //修改
    public void update(RepositoryConfineInfo repositoryConfine);

    //让id查询
    public RepositoryConfineInfo selectById(Long id);

    //删除
    public void deleteConfig(RepositoryConfineInfo repositoryConfine);

    //启用
    public void enabledConfig(RepositoryConfineInfo repositoryConfine);

    //禁用
    public void disableConfig(RepositoryConfineInfo repositoryConfine);

    //查询所有
    public GenericPage<RepositoryConfineInfo> selectAllForUpdate(int count, int startIndex);


}
