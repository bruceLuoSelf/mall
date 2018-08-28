package com.wzitech.gamegold.order.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.order.entity.CurrencyConfigEO;

import java.util.List;
import java.util.Map;

/**
 * Created by 340032 on 2018/3/15.
 */
public interface ICurrencyConfigDBDAO extends IMyBatisBaseDAO<CurrencyConfigEO,Long> {
    /**
     * 根据订单号删除评价记录
     * @param id
     */
    void removeByid(Long id);

    List<CurrencyConfigEO> queryCurrencyConfig(String gameName,String goodsType);

    CurrencyConfigEO selectConfig(Long id);
    CurrencyConfigEO selectCurrency(CurrencyConfigEO currencyConfigEO);
}
