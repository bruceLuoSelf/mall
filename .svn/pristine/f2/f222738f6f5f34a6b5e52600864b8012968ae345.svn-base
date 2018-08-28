package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/4.
 */
public interface IShGameConfigDao extends IMyBatisBaseDAO<ShGameConfig, Long> {

    //根据trade在数据库查询
    List<ShGameConfig> selectByLikeTrade(Map<String, Object> paramMap);

    //满足查询条件为null或者''的查询
    List<ShGameConfig> selectForAllCondition(Map<String,Object> paramMap);

    ShGameConfig selectShGame(ShGameConfig s);

    List<ShGameConfig> selectGoodsTypeByGameAndShMode(Map<String,Object> map);

    BigDecimal getPoundage(Map<String,Object> map);
}
