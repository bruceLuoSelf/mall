package com.wzitech.gamegold.shorder.dao.impl;

import com.google.common.collect.Maps;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.shorder.dao.IGameAccountDBDAO;
import com.wzitech.gamegold.shorder.entity.GameAccount;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收货角色信息
 */
@Repository
public class GameAccountDBDAOImpl extends AbstractMyBatisDAO<GameAccount, Long> implements IGameAccountDBDAO {
    /**
     * 查找对应条件的游戏账号角色信息
     * @param paramMap
     * @return
     */
    @Override
    public List<GameAccount> queryGameAccount(Map<String, Object> paramMap)
    {
        return this.getSqlSession().selectList(getMapperNamespace() + ".queryGameAccount", paramMap);
    }

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
    @Override
    public List<GameAccount> queryGameAccount(String buyerAccount,String gameName,String reion,String server,String gameAccount,String roleName)
    {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("buyerAccount", buyerAccount);
        queryMap.put("gameName", gameName);
        queryMap.put("region", reion);
        queryMap.put("server", server);
        queryMap.put("gameAccount", gameAccount);
        queryMap.put("roleName", roleName);
        return this.getSqlSession().selectList(getMapperNamespace() + ".queryGameAccount", queryMap);
    }

    /**
     * 查找对应条件的但不存在于上传的采购单中的游戏账号角色信息
     * @param paramMap
     * @return
     */
    @Override
    public List<GameAccount> queryGameAccountNotInUpload(Map<String, Object> paramMap){
        return this.getSqlSession().selectList(getMapperNamespace() + ".queryGameAccountNotInUpload", paramMap);
    }

    /**
     * 更新当前区服下所有账号角色的价格
     * @param paramMap
     */
    @Override
    public void updateGameAcountPrice(Map<String, Object> paramMap) {
        this.getSqlSession().update(getMapperNamespace() + ".updateGameAcountPrice", paramMap);
    }

    /**
     * 增加当前区服下所有账号角色的采购数量
     * @param paramMap
     */
    @Override
    public int addGameAcountCount(Map<String, Object> paramMap) {
        return this.getSqlSession().update(getMapperNamespace() + ".addGameAcountCount", paramMap);
    }

    /**
     * 减去当前区服下所有账号角色的采购数量
     * @param paramMap
     */
    @Override
    public int reduceGameAcountCount(Map<String, Object> paramMap) {
        return this.getSqlSession().update(getMapperNamespace() + ".reduceGameAcountCount", paramMap);
    }

    /**
     * 可以收货的账号角色
     * @param paramMap
     * @return
     */
    @Override
    public List<GameAccount> queryGameAccountCanSh(Map<String, Object> paramMap) {
        return this.getSqlSession().selectList(getMapperNamespace() + ".queryGameAccountCanSh", paramMap);
    }

    /**
     * 可以收货的账号角色 已有账号查询
     * @param paramMap
     * @return
     */
    @Override
    public Long queryOnlyGameAccountCanSh(Map<String, Object> paramMap) {
        return this.getSqlSession().selectOne(getMapperNamespace() + ".queryOnlyGameAccountCanSh", paramMap);
    }

    /**
     * 可以收货的账号角色
     * @param paramMap
     * @return
     */
    @Override
    public long queryGameAccountCanShCountSum(Map<String, Object> paramMap) {
        Long sum = this.getSqlSession().selectOne(getMapperNamespace() + ".queryGameAccountCanShCountSum", paramMap);
        return sum == null ? 0 : sum;
    }

    /**
     * 正在交易中的账号角色
     * @param paramMap
     * @return
     */
    @Override
    public List<GameAccount> queryGameAccountByReceiving(Map<String, Object> paramMap) {
        return this.getSqlSession().selectList(getMapperNamespace() + ".queryGameAccountByReceiving", paramMap);
    }

    /**
     * 可以收货的账号角色 机器收货使用
     * @param paramMap
     * @return
     */
    @Override
    public List<GameAccount> queryGameAccountCanShByAutomete(Map<String, Object> paramMap) {
        return this.getSqlSession().selectList(getMapperNamespace() + ".queryGameAccountCanShByAutomete", paramMap);
    }

    /**
     * 根据id更新价格和采购数
     * @param paramMap
     */
    @Override
    public void updateCountAndPriceById(Map<String, Object> paramMap) {
        this.getSqlSession().update(getMapperNamespace() + ".updateCountAndPriceById", paramMap);
    }

    /**
     * 更新库存数量和等级
     *
     * @param paramMap
     * @return
     */
    @Override
    public int updateAccountByMap(Map<String, Object> paramMap) {
        return getSqlSession().update(getMapperNamespace() + ".updateAccountByMap", paramMap);
    }

    /**
     * 更新库存数量和等级
     *
     * @param paramMap
     * @return
     */
    @Override
    public int updateAccountByMapNotAspect(Map<String, Object> paramMap) {
        return getSqlSession().update(getMapperNamespace() + ".updateAccountByMap", paramMap);
    }

    /**
     * 分页查询账号库存
     * @param paramMap
     * @param sortFields
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public GenericPage<GameAccount> selectRepositoryGameAccountList(Map<String, Object> paramMap, List<SortField> sortFields, int start, int pageSize) {
        if(null == paramMap){
            paramMap = new HashMap<String, Object>();
        }

        //检查分页参数
        if(pageSize < 1){
            throw new IllegalArgumentException("分页pageSize参数必须大于1");
        }

        if(start < 0){
            throw new IllegalArgumentException("分页startIndex参数必须大于0");
        }

        String orderByStr = "";
        if(sortFields!=null){
            for (int i = 0, j = sortFields.size(); i < j; i++) {
                SortField field = sortFields.get(i);
                orderByStr += "\"" + field.getField() + "\" " + field.getSort();
                if (i != (j - 1)) {
                    orderByStr += ",";
                }
            }
        }

        paramMap.put("orderBy", orderByStr);

        int totalSize = countByMap(paramMap);

        // 如果数据Count为0，则直接返回
        if(totalSize == 0){
            return new GenericPage<GameAccount>(start, totalSize, pageSize, null);
        }

        List<GameAccount> pagedData = this.getSqlSession().selectList(this.mapperNamespace + ".selectByMap",
                paramMap, new RowBounds(start, pageSize));

        return new GenericPage<GameAccount>(start, totalSize, pageSize, pagedData);
    }

    /**
     * 导出
     * @param paramMap
     * @param sortFields
     * @return
     */
    @Override
    public List<GameAccount> selectRepositoryGameAccountList(Map<String, Object> paramMap, List<SortField> sortFields) {
        if(null == paramMap){
            paramMap = new HashMap<String, Object>();
        }

        String orderByStr = "";
        if(sortFields!=null){
            for (int i = 0, j = sortFields.size(); i < j; i++) {
                SortField field = sortFields.get(i);
                orderByStr += "\"" + field.getField() + "\" " + field.getSort();
                if (i != (j - 1)) {
                    orderByStr += ",";
                }
            }
        }

        paramMap.put("orderBy", orderByStr);

        List<GameAccount> list = this.getSqlSession().selectList(this.mapperNamespace + ".selectByMap",paramMap);

        return list;
    }

    /**
     * 新增帐号角色库存
     *
     * @param paramMap
     * @return
     */
    @Override
    public int addRepositoryCount(Map<String, Object> paramMap) {
        return getSqlSession().update(getMapperNamespace() + ".addRepositoryCount", paramMap);
    }

    /**
     * 设置帐号角色库存
     *
     * @param paramMap
     * @return
     */
    @Override
    public int setRepositoryCount(Map<String, Object> paramMap) {
        return getSqlSession().update(getMapperNamespace() + ".setRepositoryCount", paramMap);
    }

    /**
     * 减去帐号角色库存
     *
     * @param paramMap
     * @return
     */
    @Override
    public int reduceRepositoryCount(Map<String, Object> paramMap) {
        return getSqlSession().update(getMapperNamespace() + ".reduceRepositoryCount", paramMap);
    }

    /**
     * 根据卖家账号、游戏名、区、服删除数据
     *
     * @param list
     */
    @Override
    public void deleteGameAccountByMap(List<Map<String, Object>> list) {
        this.getSqlSession().delete(getMapperNamespace()+".batchDeleteByMap",list);
    }

    /**
     * 根据游戏状态修改
     *
     */
    @Override
    public void updateStatus(Map<String, Object> map) {
        this.getSqlSession().delete(getMapperNamespace()+".updateStatus",map);
    }

    /**
     * 增加库存数量
     *
     */
    @Override
    public void incrGameAccountRepositoryCount(Long gameAccountId, Long count) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id", gameAccountId);
        map.put("repositoryCount", count);
        this.getSqlSession().update(getMapperNamespace()+".incrGameAccountRepositoryCount",map);
    }

    /**
     * 增加库存数量
     *
     */
    @Override
    public void incrGameAccountRepositoryCountByRepositoryId(Long gameAccountId, Long count) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("repositoryId", gameAccountId);
        map.put("repositoryCount", count);
        this.getSqlSession().update(getMapperNamespace()+".incrGameAccountRepositoryCountByRepositoryId",map);
    }

    @Override
    public void updateRepositoryIdById(Long id, Long repositoryId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id",id);
        map.put("repositoryId",repositoryId);
        this.getSqlSession().update(getMapperNamespace()+".updateRepositoryIdById",map);
    }

//    @Override
//    public void reduceFreezeCount(Map<String,Object> reduceMap) {
//        this.getSqlSession().update(getMapperNamespace()+".reduceFreezeCount",reduceMap);
//    }

    @Override
    public void emptyTodaySaleCount() {
        this.getSqlSession().update(getMapperNamespace() + ".emptyTodaySaleCount");
    }

    @Override
    public void batchAddGameAccountFreezeCount(List<GameAccount> gameAccounts) {
        this.getSqlSession().update(getMapperNamespace()+".batchAddGameAccountFreezeCount",gameAccounts);
    }
}
