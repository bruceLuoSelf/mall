package com.wzitech.gamegold.goods.business;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.goods.entity.FirmInfo;
import com.wzitech.gamegold.goods.entity.FirmsAccount;

import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/7/5
 */
public interface IFirmsAccountManager {
    /**
     * 增加信息
     *
     * @param firmsAccount
     * @return
     * @throws SystemException
     */
    void addFirmsAccount(FirmsAccount firmsAccount) throws SystemException;


    /**
     * 修改厂商信息
     *
     * @param firmsAccount
     * @return
     * @throws SystemException
     */
    void modifyFirmsAccount(FirmsAccount firmsAccount) throws SystemException;


    /**
     * 修改用户信息
     *
     * @param firmInfo
     * @throws SystemException
     */
    void modifyFirmsNames(FirmInfo firmInfo) throws SystemException;

    /**
     * 删除
     *
     * @throws SystemException
     */
    void delFirmsAccount(Long id) throws SystemException;

    /**
     * 查询
     *
     * @param map
     * @param pageSize
     * @param start
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<FirmsAccount> selectFirmsAccount(Map<String, Object> map, int pageSize, int start, String orderBy, Boolean isAsc);

    /**
     * 条件查询
     *
     * @param firmsAccount
     * @return
     */
    FirmsAccount selectByMap(FirmsAccount firmsAccount);


    /**
     * 删除跟厂商级联的账号
     */
    void delAccountByfirmsSecret(String firmsSecret);

    /**
     * 厂商查询方法
     * Author：wubiao
     * 20170713
     * @param map
     * @return
     */
    FirmsAccount getFirmsAccountByMap(Map<String, Object> map);

    /**
     * 根据id查询厂商用户
     * @param id
     * @return
     */
    FirmsAccount selectById(Long id);

    /**
     * 更新厂商用户密钥
     */
    void refreshSecretKey(Long id);

    FirmsAccount getFirmsAccountSecretKey();

    FirmsAccount refreshFirmsAccountSecretKey();

    FirmsAccount setFirmsAccountSecretKey(String key);

    String queryFirmsAccountByLoginAccount(String loginAccount);
}
