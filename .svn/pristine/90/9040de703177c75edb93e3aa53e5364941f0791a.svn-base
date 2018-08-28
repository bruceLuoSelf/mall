package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.DeliveryTypeEnum;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.IDeliveryConfigManager;
import com.wzitech.gamegold.shorder.entity.CommonType;
import com.wzitech.gamegold.shorder.entity.DeliveryConfig;
import com.wzitech.gamegold.shorder.entity.FundDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 汪俊杰 on 2018/1/5.
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class DeliveryConfigAction extends AbstractAction {
    @Autowired
    private IDeliveryConfigManager deliveryConfigManager;

    private DeliveryConfig deliveryConfig;
    private List<CommonType> deliveryTypeList;
    private List<DeliveryConfig> deliveryConfigList;

    /**
     * 获取所有的收货模式的列表
     *
     * @return
     */
    public String queryDeliveryTypeList() {
        deliveryTypeList = new ArrayList<CommonType>();
        for (DeliveryTypeEnum deliveryTypeEnum : DeliveryTypeEnum.values()) {
            deliveryTypeList.add(new CommonType(deliveryTypeEnum.getCode(), deliveryTypeEnum.getName()));
        }
        return this.returnSuccess();
    }

    /**
     * 新增
     *
     * @return
     */
    public String addDeliveryCofig() {
        try {
            deliveryConfigManager.insert(deliveryConfig);
        } catch (SystemException ex) {
            return this.returnError(ex);
        } catch (Exception ex) {
            return this.returnError(ex.toString());
        }
        return this.returnSuccess();
    }

    /**
     * 新增
     *
     * @return
     */
    public String modifyDeliveryCofig() {
        try {
            deliveryConfigManager.update(deliveryConfig);
        } catch (SystemException ex) {
            return this.returnError(ex);
        } catch (Exception ex) {
            return this.returnError(ex.toString());
        }
        return this.returnSuccess();
    }

    /**
     * 根据条件查询
     *
     * @return
     */
    public String queryDeliveryCofig() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (deliveryConfig != null) {
            if (StringUtils.isNotBlank(deliveryConfig.getGameName())) {
                map.put("gameName", deliveryConfig.getGameName());
            }
            if (deliveryConfig.getDeliveryTypeId() != null) {
                map.put("deliveryTypeId", deliveryConfig.getDeliveryTypeId());
            }
            if (deliveryConfig.getGoodsTypeId() != null) {
                map.put("goodsTypeId", deliveryConfig.getGoodsTypeId());
            }
            if (deliveryConfig.getTradeTypeId() != null) {
                map.put("tradeTypeId", deliveryConfig.getTradeTypeId());
            }
        }

        GenericPage<DeliveryConfig> genericPage = deliveryConfigManager.queryPage(map, this.limit, this.start, "create_time", false);
        deliveryConfigList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    public String deleteDeliveryConfig() {
        try {
            deliveryConfigManager.delete(deliveryConfig.getId());
        } catch (SystemException ex) {
            return this.returnError(ex);
        } catch (Exception ex) {
            return this.returnError(ex.toString());
        }
        return this.returnSuccess();
    }

    public List<CommonType> getDeliveryTypeList() {
        return deliveryTypeList;
    }

    public void setDeliveryTypeList(List<CommonType> deliveryTypeList) {
        this.deliveryTypeList = deliveryTypeList;
    }


    public DeliveryConfig getDeliveryConfig() {
        return deliveryConfig;
    }

    public void setDeliveryConfig(DeliveryConfig deliveryConfig) {
        this.deliveryConfig = deliveryConfig;
    }

    public List<DeliveryConfig> getDeliveryConfigList() {
        return deliveryConfigList;
    }

    public void setDeliveryConfigList(List<DeliveryConfig> deliveryConfigList) {
        this.deliveryConfigList = deliveryConfigList;
    }
}
