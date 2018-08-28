package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.shorder.entity.GameAccount;

import java.util.List;
import java.util.Map;

/**
 * 收货角色信息
 */
public interface IGameAccountDBDAO extends IMyBatisBaseDAO<GameAccount, Long> {
    /**
     * 查找对应条件的游戏账号角色信息
     * @param paramMap
     * @return
     */
    List<GameAccount> queryGameAccount(Map<String, Object> paramMap);

    /**
     * 查找对应条件的游戏账号角色信息
     * @param buyerAccount
     * @param gameName
     * @param reion
     * @param server
     * @param gameAccount
     * @param roleName
     * @return
     */
     List<GameAccount> queryGameAccount(String buyerAccount,String gameName,String reion,String server,String gameAccount,String roleName);

    /**
     * 查找对应条件的但不存在于上传的采购单中的游戏账号角色信息
     * @param paramMap
     * @return
     */
    List<GameAccount> queryGameAccountNotInUpload(Map<String, Object> paramMap);

    /**
     * 更新当前区服下所有账号角色的价格
     * @param paramMap
     */
    void updateGameAcountPrice(Map<String, Object> paramMap);

    /**
     * 增加当前区服下所有账号角色的采购数量
     * @param paramMap
     */
    int addGameAcountCount(Map<String, Object> paramMap);

    /**
     * 减去当前区服下所有账号角色的采购数量
     * @param paramMap
     */
    int reduceGameAcountCount(Map<String, Object> paramMap);

    /**
     * 可以收货的账号角色
     * @param paramMap
     * @return
     */
    List<GameAccount> queryGameAccountCanSh(Map<String, Object> paramMap);

    /**
     * 可以收货的账号角色 已有账号查询
     * @param paramMap
     * @return
     */
    Long queryOnlyGameAccountCanSh(Map<String, Object> paramMap);

    /**
     * 可以收货的账号角色
     * @param paramMap
     * @return
     */
    long queryGameAccountCanShCountSum(Map<String, Object> paramMap);

    /**
     * 交易中的账号角色
     * @param paramMap
     * @return
     */
    List<GameAccount> queryGameAccountByReceiving(Map<String, Object> paramMap);

    /**
     * 机器收货使用的查询
     * @param paramMap
     * @return
     */
    List<GameAccount> queryGameAccountCanShByAutomete(Map<String, Object> paramMap);

    /**
     * 根据id更新价格和采购数
     * @param paramMap
     */
    void updateCountAndPriceById(Map<String, Object> paramMap);

    /**
     * 更新账号信息
     * @param paramMap
     * @return
     */
    int updateAccountByMap(Map<String, Object> paramMap);

    int updateAccountByMapNotAspect(Map<String, Object> paramMap);

    /**
     * 分页查询账号库存
     * @param paramMap
     * @param sortFields
     * @param start
     * @param pageSize
     * @return
     */
    GenericPage<GameAccount> selectRepositoryGameAccountList(Map<String, Object> paramMap, List<SortField> sortFields, int start, int pageSize);

    /**
     * 导出
     * @param paramMap
     * @param sortFields
     * @return
     */
    List<GameAccount> selectRepositoryGameAccountList(Map<String, Object> paramMap, List<SortField> sortFields);

    /**
     * 新增帐号角色库存
     *
     * @param paramMap
     * @return
     */
     int addRepositoryCount(Map<String, Object> paramMap);

    int setRepositoryCount(Map<String, Object> paramMap);
    /**
     * 减去帐号角色库存
     *
     * @param paramMap
     * @return
     */
     int reduceRepositoryCount(Map<String, Object> paramMap);

    /**
     * 根据卖家账号、游戏名、区、服删除数据
     * @param list
     */
     void deleteGameAccountByMap(List<Map<String,Object>> list);


    void updateStatus(Map<String, Object> map);

    void incrGameAccountRepositoryCount(Long gameAccountId, Long count);

    void incrGameAccountRepositoryCountByRepositoryId(Long gameAccountId, Long count);

    void updateRepositoryIdById(Long id, Long repositoryId);

    /**
     * 清空今日已售数量
     */
    void emptyTodaySaleCount();

    /**
     * 批量更新冻结数量
     * @param gameAccounts
     */
    void batchAddGameAccountFreezeCount(List<GameAccount> gameAccounts);


//    void reduceFreezeCount(Map<String,Object> reduceMap);
}
