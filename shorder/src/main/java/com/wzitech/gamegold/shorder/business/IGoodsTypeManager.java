package com.wzitech.gamegold.shorder.business;

import com.wzitech.gamegold.shorder.entity.GoodsType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 交易类目manager
 */
public interface IGoodsTypeManager {
    /**
     * 根据id查询交易类目名称
     *
     * @param id
     * @return
     */
    String getNameById(Long id);

    /**
     * 根据id查询启用的交易类目名称
     *
     * @param id
     * @return
     */
    String getEnableNameById(Long id);

    //獲取所有交易類目名稱和id
    List<GoodsType> queryGoodsTypeNameIdList();

    //根据游戏类目名称查询id
    Long queryGoodsTypeIdByName(String name);
}
