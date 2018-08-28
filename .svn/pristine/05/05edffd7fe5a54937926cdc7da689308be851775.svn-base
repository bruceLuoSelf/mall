package com.wzitech.gamegold.order.business;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.entity.CurrencyConfigEO;

import java.util.List;
import java.util.Map;

/**
 * 通货配置管理接口
 * Created by 340032 on 2018/3/15.
 */

public interface ICurrencyConfigManager {
    /**
     * 添加通货配置
     */
    void addCurrencyConfig(CurrencyConfigEO currencyConfigEO)throws SystemException;

    /**
     * 修改通货配置
     */
    void modifyCurrencyConfig(CurrencyConfigEO currencyConfigEO)throws SystemException;

    /**
     * 删除通货配置
     */
    void deleteCurrencyConfig(Long id)throws SystemException;

    /**
     * 分页查询数据
     */
    GenericPage<CurrencyConfigEO> queryListInPage(Map<String,Object> map, int start, int pageSize, String orderBy, boolean
            isAsc);

    /**
     * 查询,对应的配置
     * @param gameName
     * @param goodsType
     * @return
     * @throws SystemException
     */
    List<CurrencyConfigEO> queryCurrencyConfig(String gameName,String goodsType)throws SystemException;

    /**
     * 查询配置信息是否已经存在
     */
    CurrencyConfigEO selectById(Long id);

    CurrencyConfigEO selectCurrency(CurrencyConfigEO currencyConfigEO);


    GenericPage<CurrencyConfigEO> queryInPage(Map<String,Object> map, int start, int pageSize, String orderBy, boolean
            isAsc);
}
