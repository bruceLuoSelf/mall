package com.wzitech.gamegold.facade.frontend.service.shorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.shorder.entity.GameAccount;
import com.wzitech.gamegold.shorder.entity.PurchaseOrder;

import java.util.List;

/**
 * 收货角色操作响应
 */
public class PurchaseOrderResponse  extends AbstractServiceResponse {
    /*
    账号角色集合
     */
    private List<GameAccount> gameAccountList;
    /*
    采购单集合
     */
    private List<PurchaseOrder> purchaseOrderList;
    /**
     * 每页记录数
     */
    private Integer pageSize;
    /**
     * 当前是第几页
     */
    private Integer currPage;
    /**
     * 总记录数
     */
    private Long totalCount;
    /**
     * 总页数
     */
    private Long totalPage;

    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 总页数
     * @return
     */
    public Long getTotalPage() {
        if (totalCount == null || pageSize == null)
            return 0L;
        if (totalCount % pageSize == 0)
            totalPage= totalCount / pageSize;
        else
            totalPage= totalCount / pageSize + 1;
        return totalPage;
    }

    public List<GameAccount> getGameAccountList() {
        return gameAccountList;
    }

    public void setGameAccountList(List<GameAccount> gameAccountList) {
        this.gameAccountList = gameAccountList;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrPage() {
        return currPage;
    }

    public void setCurrPage(Integer currPage) {
        this.currPage = currPage;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public List<PurchaseOrder> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public void setPurchaseOrderList(List<PurchaseOrder> purchaseOrderList) {
        this.purchaseOrderList = purchaseOrderList;
    }
}
