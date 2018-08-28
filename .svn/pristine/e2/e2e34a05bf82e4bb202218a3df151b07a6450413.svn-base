package com.wzitech.gamegold.facade.frontend.service.shorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;

import java.util.List;

/**
 * Created by Administrator on 2017/1/6.
 */
public class ShGameConfigResponse extends AbstractServiceResponse {
    /*
    收货游戏配置集合
     */
    private ShGameConfig shGameConfig;

    private List<ShGameConfig> shGameConfigList;



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

    public ShGameConfig getShGameConfig() {
        return shGameConfig;
    }

    public void setShGameConfig(ShGameConfig shGameConfig) {
        this.shGameConfig = shGameConfig;
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
    public List<ShGameConfig> getShGameConfigList() {
        return shGameConfigList;
    }

    public void setShGameConfigList(List<ShGameConfig> shGameConfigList) {
        this.shGameConfigList = shGameConfigList;
    }
}
