package com.wzitech.gamegold.facade.frontend.service.repository.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

public class SellerRepositoryRequest extends AbstractServiceRequest {
    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 大区
     */
    private String regionName;

    /**
     * 服务器
     */
    private String serverName;

    private String raceName;

    /**
     * 排序字段
     */
    private String filedName;

    /**
     * 排序
     */
    private String sort;

    private int startIndex;

    private int pageCount;

    /**
     * 商品类型
     */
    private String goodsTypeName;

    private String goodsTypeId;

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }
}
