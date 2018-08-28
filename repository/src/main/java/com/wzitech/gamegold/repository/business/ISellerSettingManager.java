package com.wzitech.gamegold.repository.business;

import com.wzitech.chaos.framework.server.common.exception.BusinessException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.repository.entity.SellerSetting;
import java.util.List;
import java.util.Map;

/**
 * 卖家配置信息
 * Created by 335854 on 2015/9/1.
 */
public interface ISellerSettingManager {
    /**
     * 根据条件分页查询配置
     * @param queryMap
     * @param limit
     * @param start
     * @param sortBy
     * @param isAsc
     * @return
     * @throws BusinessException
     */
    GenericPage<SellerSetting> querySellerSetting(Map<String, Object> queryMap, int limit, int start,
                                                String sortBy, boolean isAsc);

    /**
     * 新增店铺配置
     * @param sellerSetting
     * @return
     * @throws BusinessException
     */
    public Boolean addSellerSetting(SellerSetting sellerSetting);

    /**
     * 修改店铺配置
     * @param sellerSetting
     * @return
     * @throws BusinessException
     */
    public Boolean modifySellerSetting(SellerSetting sellerSetting);

    /**
     * 删除店铺配置
     * @param ids
     * @return
     * @throws BusinessException
     */
    public void deleteSellerSetting(List<Long> ids);

    /**
     * 设置店铺配置的可用与否
     * @param ids
     * @param isDeleted
     * @return
     * @throws BusinessException
     */
    public void settingEnable(List<Long> ids,Boolean isDeleted);

    /**
     * 根据游戏和卖家账号查找店铺信息列表
     * @param gameName
     * @param loginAccountSql
     * @return
     */
    public List<SellerSetting> selectByLoginAccountList(String gameName,String loginAccountSql);

    /**
     * 根据游戏名称和卖家5173账号查询配置
     * @param gameName
     * @param loginAccount
     * @return
     */
    SellerSetting querySellSettingByGameNameAndSeller(String gameName, String loginAccount,String goodsTypeName);

    /**
     * 更新店铺排序
     * @param id
     * @param sort
     */
    void updateSort(long id, int sort);
}
