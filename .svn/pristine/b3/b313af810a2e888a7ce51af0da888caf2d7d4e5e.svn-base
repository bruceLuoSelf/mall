package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.common.exception.BusinessException;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.BlackListEO;

import java.util.Map;

/**
 * 黑名单用户管里
 * Created by 340032 on 2017/12/25.
 */
public interface IBlackListManager {
    /**
     * 查询用户信息
     * @param queryMap
     * @param limit
     * @param start
     * @param sortBy
     * @param isAsc
     * @return
     */
    public GenericPage<BlackListEO> queryUser(Map<String, Object> queryMap, int limit, int start,
                                              String sortBy, boolean isAsc);



    /**
     * 添加用户
     * @throws BusinessException
     */
    public void addUser1(BlackListEO blackListeo) throws SystemException;


    /**
     * 通过ID删除用户
     * @param id
     */
    public void deleteUser1(Long id) throws SystemException;


    /**
     * 通过ID启用用户
     * @param id
     */
    public void enableUser1(Long id) throws SystemException;

    /**
     * 通过ID禁用用户
     * @param id
     */
    public void disableUser1(Long id) throws SystemException;
}
