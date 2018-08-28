package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.ShGameTrade;
import com.wzitech.gamegold.shorder.entity.Trade;

import java.util.List;
import java.util.Map;

/**
 * Created by ljn on 2018/3/13.
 */
public interface IShGameTradeDao extends IMyBatisBaseDAO<ShGameTrade,Long>{

    List<Integer> getShModeByGame(Long gameTableId);

    List<Trade> getTradeByGameAndShMode(Map<String,Object> map);
}
