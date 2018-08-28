package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.ShGameTrade;
import com.wzitech.gamegold.shorder.entity.Trade;

import java.util.List;
import java.util.Map;

/**
 * Created by ljn on 2018/3/13.
 */
public interface IShGameTradeManager {

    void addShGameTrade(ShGameTrade shGameTrade, List<Long> tradeIds);

    void add(ShGameTrade shGameTrade);

    void deleteById(Long id);

    void updateShGameTrade(ShGameTrade shGameTrade);

    ShGameTrade queryShGameTradeById(Long id);

    GenericPage<ShGameTrade> queryByMap(Map<String,Object> map, int start, int pageSize, String orderBy, boolean isAsc);

    List<Integer> getShModeByGame(Long gameTableId);

    List<Trade> getTradeByGameAndShMode(Map<String,Object> map);


}
