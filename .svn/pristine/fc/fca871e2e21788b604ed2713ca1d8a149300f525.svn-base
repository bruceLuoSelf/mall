package com.wzitech.gamegold.facade.frontend.service.repository.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 库存请求返回对象
 * @author yemq
 */
@XmlRootElement
public class RepositoryResponse extends AbstractServiceResponse {
    private RepositoryDTO repository;
    private List<RepositoryDTO> repositoryList;
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

    private Boolean isJiShou;

    public Boolean getIsJiShou() {
        return isJiShou;
    }

    public void setIsJiShou(Boolean jiShou) {
        isJiShou = jiShou;
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

    public RepositoryDTO getRepository() {
        return repository;
    }

    public void setRepository(RepositoryDTO repository) {
        this.repository = repository;
    }

    public List<RepositoryDTO> getRepositoryList() {
        return repositoryList;
    }

    public void setRepositoryList(List<RepositoryDTO> repositoryList) {
        this.repositoryList = repositoryList;
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
}
