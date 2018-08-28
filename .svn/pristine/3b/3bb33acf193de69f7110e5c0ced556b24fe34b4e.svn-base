package com.wzitech.gamegold.facade.frontend.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.dto.SubOrderDetailDTO;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;

/**
 * 响应卖家订单信息DTO
 */
public class QuerySellerOrderResponse extends AbstractServiceResponse {
    private GenericPage<SubOrderDetailDTO> result;
    private SubOrderDetailDTO subOrderDetail;

    public QuerySellerOrderResponse() {
    }

    public GenericPage<SubOrderDetailDTO> getResult() {
        return result;
    }

    public void setResult(GenericPage<SubOrderDetailDTO> result) {
        this.result = result;
    }

    public SubOrderDetailDTO getSubOrderDetail() {
        return subOrderDetail;
    }

    public void setSubOrderDetail(SubOrderDetailDTO subOrderDetail) {
        this.subOrderDetail = subOrderDetail;
    }
}
