package com.wzitech.gamegold.goods.business;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.goods.entity.FirmInfo;
import com.wzitech.gamegold.goods.entity.FirmsAccount;

import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/7/5
 */
public interface IFirmInfoManager {

    /**
     * 增加信息
     *
     * @param firmInfo
     * @return
     * @throws SystemException
     */
    FirmInfo addFirmInfo(FirmInfo firmInfo) throws SystemException;

    /**
     * 修改
     *
     * @param firmInfo
     * @return
     * @throws SystemException
     */
    FirmInfo modifyFirmInfo(FirmInfo firmInfo) throws SystemException;

    /**
     * 新增条件查询
     *
     * @param firmInfo
     * @return
     * @throws SystemException
     */
    FirmInfo selectByMap(FirmInfo firmInfo) throws SystemException;


    /**
     * Id查询
     *
     * @param id
     * @return
     * @throws SystemException
     */
    FirmInfo selectById(Long id) throws SystemException;

    /**
     * 删除
     *
     * @throws SystemException
     */
    void delFirmInfo(Long id) throws SystemException;

    /**
     * 分页查询
     *
     * @param map
     * @param pageSize
     * @param start
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<FirmInfo> selectFirms(Map<String, Object> map, int pageSize, int start, String orderBy, Boolean isAsc);


    /**
     * 根据secretName更新工作室密匙
     */
    void refreshSecret(FirmInfo firmInfo);

    /**
     * 获取工作室密匙
     * @param id
     */
    FirmInfo getSecret(Long id);

    public void updateFimrs(Long id,String firmsName);

}
