package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.Trade;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/4.
 */
public interface ITradeManager {

    /**
     * 添加收货模式配置
     * @param trade
     * @return
     */
    Trade addTrade(Trade trade);

    void deleteTrade(Long id);

    void updateTrade(Trade trade);

    void update(Long id,Integer tradeLogo);

    void enabled(Long id);

    void disabled(Long id);

    GenericPage<Trade> query(Map<String,Object> map,int pageSize,int start,String orderBy,Boolean isAsc);

    //根据ids查询交易方式名称
    String getNamesByIds(String ids);

    /**
     * 获取启动的交易方式
     * @param id
     * @return
     */
     String getById(Long id);

    List<Trade> queryByMap(Map<String,Object> map);

    Trade selectById(Long id);

    List<Trade> selectTradeByGameGoodsTypeAndShMode(Map<String,Object> map);
}
