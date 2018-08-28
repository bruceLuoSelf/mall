package com.wzitech.gamegold.facade.frontend.service.shorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.SplitRepositoryRequest;
import com.wzitech.gamegold.shorder.entity.SplitRepositorySubRequest;

import java.util.List;

/**
 * @author ljn
 * @date 2018/6/19.
 */
public class SplitRepositoryOrderResponse extends AbstractServiceResponse {

    /**
     * 分仓主订单
     */
    private SplitRepositoryRequest order;

    /**
     * 分仓子订单
     */
    private List<SplitRepositorySubRequest> subOrderList;

    /**
     * 收货订单
     */
    private DeliveryOrder deliveryOrder;

    /**
     * 使用金库数量
     */
    private Long useRepertoryCount;

    public Long getUseRepertoryCount() {
        return useRepertoryCount;
    }

    public void setUseRepertoryCount(Long useRepertoryCount) {
        this.useRepertoryCount = useRepertoryCount;
    }

    public DeliveryOrder getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public List<SplitRepositorySubRequest> getSubOrderList() {
        return subOrderList;
    }

    public void setSubOrderList(List<SplitRepositorySubRequest> subOrderList) {
        this.subOrderList = subOrderList;
    }

    public SplitRepositoryRequest getOrder() {
        return order;
    }

    public void setOrder(SplitRepositoryRequest order) {
        this.order = order;
    }
}
