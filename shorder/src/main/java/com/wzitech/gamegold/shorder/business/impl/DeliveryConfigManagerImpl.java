package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.DeliveryOrderGTRType;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.ShDeliveryTypeEnum;
import com.wzitech.gamegold.shorder.business.IDeliveryConfigManager;
import com.wzitech.gamegold.shorder.business.IGoodsTypeManager;
import com.wzitech.gamegold.shorder.business.ITradeManager;
import com.wzitech.gamegold.shorder.dao.IDeliveryConfigDao;
import com.wzitech.gamegold.shorder.entity.DeliveryConfig;
import com.wzitech.gamegold.shorder.entity.Trade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收货系统配置
 * Created by 汪俊杰 on 2018/1/4.
 */
@Component
public class DeliveryConfigManagerImpl extends AbstractBusinessObject implements IDeliveryConfigManager {
    @Autowired
    private IDeliveryConfigDao deliveryConfigDao;
    @Autowired
    private IGoodsTypeManager goodsTypeManager;
    @Autowired
    private ITradeManager tradeManager;

    /**
     * 根据条件获取分页数据
     *
     * @param paramMap
     * @param pageSize
     * @param start
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<DeliveryConfig> queryPage(Map<String, Object> paramMap, int pageSize, int start, String orderBy, Boolean isAsc) {
        return deliveryConfigDao.selectByMap(paramMap, pageSize, start, orderBy, isAsc);
    }

    /**
     * 根据id删除记录
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new SystemException(ResponseCodes.EmptyConfigResultId.getCode());
        }
        deliveryConfigDao.deleteById(id);
    }

    /**
     * 添加记录
     *
     * @param deliveryConfig
     */
    @Override
    public void insert(DeliveryConfig deliveryConfig) {
        if (StringUtils.isBlank(deliveryConfig.getGameName())) {
            throw new SystemException(ResponseCodes.EmptyGameName.getCode());
        }
        if (deliveryConfig.getDeliveryTypeId() == null) {
            throw new SystemException(ResponseCodes.EmptyDeliveryTypeName.getCode());
        }
        if (deliveryConfig.getGoodsTypeId() == null) {
            throw new SystemException(ResponseCodes.EmptyGoodsType.getCode());
        }
        if (deliveryConfig.getTradeTypeId() == null) {
            throw new SystemException(ResponseCodes.EmptyTradeType.getCode());
        }

        //验证新增合法性
        boolean isExsit = validInsertDeliveryConfig(deliveryConfig);
        if (isExsit) {
            throw new SystemException(ResponseCodes.ExsitDeliveryConfig.getCode());
        }

        //收货模式 1,机器收货 2,手工收货
        String deliveryTypeName = ShDeliveryTypeEnum.getNameByCode(deliveryConfig.getDeliveryTypeId());
        deliveryConfig.setDeliveryTypeName(deliveryTypeName);

        //商品类型 如：游戏币
        String goodsTypeName = goodsTypeManager.getEnableNameById(deliveryConfig.getGoodsTypeId().longValue());
        deliveryConfig.setGoodsTypeName(goodsTypeName);

        //交易方式  如：邮寄交易、附魔交易
        String tradeTypeName = tradeManager.getById(deliveryConfig.getTradeTypeId().longValue());
        deliveryConfig.setTradeTypeName(tradeTypeName);

        deliveryConfig.setCreateTime(new Date());
        deliveryConfigDao.insert(deliveryConfig);
    }

    /**
     * 修改记录
     *
     * @param deliveryConfig
     */
    @Override
    public void update(DeliveryConfig deliveryConfig) {
        if (deliveryConfig.getId() == null) {
            throw new SystemException(ResponseCodes.EmptyConfigResultId.getCode());
        }

        DeliveryConfig deliveryConfigDb = deliveryConfigDao.selectById(deliveryConfig.getId());
        if (deliveryConfigDb == null) {
            throw new SystemException(ResponseCodes.NoConfig.getCode());
        }

        deliveryConfig.setGameName(deliveryConfigDb.getGameName());
        deliveryConfig.setDeliveryTypeId(deliveryConfigDb.getDeliveryTypeId());
        deliveryConfig.setTradeTypeId(deliveryConfigDb.getTradeTypeId());
        deliveryConfig.setGoodsTypeId(deliveryConfigDb.getGoodsTypeId());

        //验证新增合法性
        boolean isExsit = validUpdateDeliveryConfig(deliveryConfig);
        if (isExsit) {
            throw new SystemException(ResponseCodes.ExsitDeliveryConfig.getCode());
        }
        deliveryConfigDao.update(deliveryConfig);
    }

    /**
     * 根据多条件查询当前收货配置
     *
     * @param gameName
     * @param goodsTypeId
     * @param deliveryTypId
     * @param tradeTypeId
     * @return
     */
    @Override
    public DeliveryConfig getDeliveryConfigByCondition(String gameName, Integer goodsTypeId, Integer deliveryTypId, Integer tradeTypeId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("gameName", gameName);
        map.put("goodsTypeId", goodsTypeId);
        map.put("deliveryTypeId", deliveryTypId);
        map.put("tradeTypeId", tradeTypeId);
        List<DeliveryConfig> list = deliveryConfigDao.selectByMap(map);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 验证新增合法性
     *
     * @param deliveryConfig
     * @return
     */
    private boolean validInsertDeliveryConfig(DeliveryConfig deliveryConfig) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("gameName", deliveryConfig.getGameName());
        map.put("goodsTypeId", deliveryConfig.getGoodsTypeId());
        map.put("deliveryTypeId", deliveryConfig.getDeliveryTypeId());
        map.put("tradeTypeId", deliveryConfig.getTradeTypeId());
        return deliveryConfigDao.countByMap(map) > 0;
    }

    /**
     * 验证修改合法性
     *
     * @param deliveryConfig
     * @return
     */
    private boolean validUpdateDeliveryConfig(DeliveryConfig deliveryConfig) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("notInId", deliveryConfig.getId());
        map.put("gameName", deliveryConfig.getGameName());
        map.put("goodsTypeId", deliveryConfig.getGoodsTypeId());
        map.put("deliveryTypeId", deliveryConfig.getDeliveryTypeId());
        map.put("tradeTypeId", deliveryConfig.getTradeTypeId());
        return deliveryConfigDao.countByMap(map) > 0;
    }
}
