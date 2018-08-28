package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/4.
 */
public interface IShGameConfigManager {
    /**
     * 分页查询
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<ShGameConfig> queryByMap(Map<String,Object> map, int start, int pageSize, String orderBy, boolean
            isAsc);

    /**
     * 不分页查询
     * @param map
     * @param orderBy
     * @param isAsc
     * @return
     */
    List<ShGameConfig> queryByMap(Map<String, Object> map, String orderBy, boolean isAsc);
    /**
     * 删除收货游戏配置
     * @param id
     */
    public int deleteGameConfig(Long id);

    int updateShGameConfig(ShGameConfig shGameConfig);

    void addShGameConfig(ShGameConfig shGameConfig);
    /**
     * 禁用配置
     * @param id
     */
    int disableUser(Long id);
    /**
     * 通过游戏名称，商品名称获取收货游戏配置
     * 增加通货类型 update by lsy
     * @param gameName
     * @return
     */
    ShGameConfig getConfigByGameName(String gameName, String goodsTypeName, Boolean isEnabled, Boolean enableMall);

    /**
     * 通过游戏名称，查询所有的商品类型
     * 增加通货类型 update by lsy
     * @param gameName
     * @return
     */
    List<ShGameConfig> getConfigByGameNameAndSwitch(String gameName, Boolean isEnabled, Boolean enableMall);

    /**
     * 启用配置
     * @param id
     */
     int qyUser(Long id);

    /**
     * 商城禁用
     * 增加通货类型 add by lvchengsheng
     * @param id
     * @return
     */
     int disableMall(Long id);

    /**
     * 禁用九宫格数据
     */
    int disableNineBlock(Long id);

    /**
     * 启用九宫格数据
     */
    int enableNineBlock(Long id);

    /**
     * 启用分仓配置
     */
    int isSplitWarehouse(Long id);

    /**
     * 禁用分仓配置
     */
    int disableWarehouse(Long id);
    /**
     * 商城启用
     * 增加通货类型 add by lvchengsheng
     * @param id
     * @return
     */
     int enableMall(Long id);
    /**
     * 查询所有启用状态的游戏配置
     */
    List<ShGameConfig> selectEnableConfig();

    ShGameConfig selectShGameConfig(ShGameConfig shGameConfig);

    ShGameConfig selectById(Long id);

    List<ShGameConfig> selectGoodsTypeByGameAndShMode(Map<String,Object> map);

    BigDecimal getPoundage(String gameName,String goodsTypeId);

}
