package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.shorder.business.IGoodsTypeManager;
import com.wzitech.gamegold.shorder.dao.IGoodsTypeDao;
import com.wzitech.gamegold.shorder.entity.GoodsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jhlcitadmin on 2017/2/15.
 */
@Component
public class GoodsTypeManagerImpl implements IGoodsTypeManager {
    @Autowired
    private IGoodsTypeDao goodsTypeDao;

    /**
     * 根据id查询交易类目名称
     *
     * @param id
     * @return
     */
    @Override
    public String getNameById(Long id) {
        GoodsType goodsType = goodsTypeDao.selectById(id);
        String name = "";
        if (goodsType != null) {
            name = goodsType.getName();
        }
        return name;
    }

    /**
     * 根据id查询启用的交易类目名称
     *
     * @param id
     * @return
     */
    @Override
    public String getEnableNameById(Long id) {
        GoodsType goodsType = goodsTypeDao.selectById(id);
        if (goodsType == null || !goodsType.getEnabled()) {
            throw new SystemException(ResponseCodes.NoEnableGoodsType.getCode());
        }
        return goodsType.getName();
    }

    public List<GoodsType> queryGoodsTypeNameIdList() {
        return goodsTypeDao.queryGoodsTypeNameIdList();
    }

    public Long queryGoodsTypeIdByName(String name) {
        return goodsTypeDao.queryGoodsTypeIdByName(name);
    }
}
