package com.wzitech.gamegold.facade.frontend.service.repository.dto;

/**
 * Created by 339928 on 2018/5/31.
 */
public class OuterWebSiteReposityDTO extends RepositoryDTO {
    /**
     *  登录账号
     */
    private String loginAccount;

    /**
     * 厂商token
     */
    private String token;

    /**
     * 页码
     */
    private Integer page;


    /**
     * 页码容量
     */
    private Integer pageSize;

    /**
     * 排序依据
     */
    private String sortByField;

    /**
     * 正序或倒序
     */
    private Boolean isAsc;

    public OuterWebSiteReposityDTO() {
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortByField() {
        return sortByField;
    }

    public void setSortByField(String sortByField) {
        this.sortByField = sortByField;
    }

    public Boolean getisAsc() {
        return isAsc;
    }

    public void setisAsc(Boolean asc) {
        isAsc = asc;
    }

}
