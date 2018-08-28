package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.utils.ParamUtils;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.dao.IPurchaserGameTradeRedis;
import com.wzitech.gamegold.shorder.dao.IShGameTradeDao;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import com.wzitech.gamegold.shorder.entity.ShGameTrade;
import com.wzitech.gamegold.shorder.entity.Trade;
import com.wzitech.gamegold.shorder.enums.ShType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ljn on 2018/3/13.
 */
@Service
public class ShGameTradeManagerImpl implements IShGameTradeManager{

    @Autowired
    private IShGameTradeDao shGameTradeDao;

    @Autowired
    private IShGameConfigManager shGameConfigManager;

    @Autowired
    private ITradeManager tradeManager;

    @Autowired
    private IPurchaserGameTradeManager purchaserGameTradeManager;
    @Autowired
    private IPurchaserGameTradeRedis purchaserGameTradeRedis;


    /**
     * 分页查询
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<ShGameTrade> queryByMap(Map<String, Object> map, int pageSize, int start, String orderBy, boolean isAsc) {
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        return shGameTradeDao.selectByMap(map,pageSize,start,orderBy,isAsc);
    }

    /**
     * 通过id查询配置
     * @param id
     * @return
     */
    @Override
    public ShGameTrade queryShGameTradeById(Long id) {
        return shGameTradeDao.selectById(id);
    }

    /**
     * 修改配置
     * @param shGameTrade
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShGameTrade(ShGameTrade shGameTrade) {
        checkGameTradeConfig(shGameTrade);
        shGameTradeDao.update(shGameTrade);
    }

    /**
     * 删除配置
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        ShGameTrade shGameTrade = shGameTradeDao.selectById(id);
        if (shGameTrade == null) {
            throw new SystemException(ResponseCodes.EmptyShGameTrade.getCode(),
                    ResponseCodes.EmptyShGameTrade.getMessage());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("gameTableId",shGameTrade.getGameTableId());
        map.put("tradeTableId",shGameTrade.getTradeTableId());
        map.put("deliveryTypeId",shGameTrade.getShMode());
        purchaserGameTradeManager.deleteByGameTrade(map);
        shGameTradeDao.deleteById(id);
        purchaserGameTradeRedis.deleteAll();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addShGameTrade(ShGameTrade shGameTrade, List<Long> tradeIds) {
        for (Long id : tradeIds) {
            shGameTrade.setTradeTableId(id);
            this.add(shGameTrade);
        }
    }

    /**
     * 增加配置
     * @param shGameTrade
     */
    @Override
    public void add(ShGameTrade shGameTrade) {
        checkGameTradeConfig(shGameTrade);
        shGameTradeDao.insert(shGameTrade);
    }

    /**
     * 检查是否有相同的配置
     * @param shGameTrade
     */
    private void checkGameTradeConfig(ShGameTrade shGameTrade) {
        Map<String, Object> map = ParamUtils.convertMap(shGameTrade);
        int i = shGameTradeDao.countByMap(map);
        if (i > 0) {
            ShGameConfig shGameConfig = shGameConfigManager.selectById(shGameTrade.getGameTableId());
            Trade trade = tradeManager.selectById(shGameTrade.getTradeTableId());
            String message=String.format(ResponseCodes.ExistGameTradeConfig.getMessage(),
                    shGameConfig.getGameName()+":"+shGameConfig.getGoodsTypeName()+":"+ ShType.getTypeByCode(shGameTrade.getShMode()).getType()+":"+trade.getName());
            throw new RuntimeException(message);
        }
    }

    @Override
    public List<Integer> getShModeByGame(Long gameTableId) {
        return shGameTradeDao.getShModeByGame(gameTableId);
    }

    /**
     * 根据配置游戏id和收货模式查询交易方式
     * @param map
     * @return
     */
    @Override
    public List<Trade> getTradeByGameAndShMode(Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<String,Object>();
        }
        return shGameTradeDao.getTradeByGameAndShMode(map);
    }
}
