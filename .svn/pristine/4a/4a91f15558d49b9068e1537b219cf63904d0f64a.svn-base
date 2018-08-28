package com.wzitech.gamegold.facade.backend.action.repository;

import com.google.common.collect.Lists;
import com.wzitech.gamegold.common.entity.BaseDTO;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.goods.business.IRepositoryConfineManager;
import com.wzitech.gamegold.goods.entity.RepositoryConfine;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by jhlcitadmin on 2017/3/20.
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class RepositoryCountAction extends AbstractAction {

    private List<RepositoryConfine> list;

    private List<BaseDTO> repositoryCountList;

    private BigInteger repositoryCount;

    public BigInteger getRepositoryCount() {
        return repositoryCount;
    }

    public void setRepositoryCount(BigInteger repositoryCount) {
        this.repositoryCount = repositoryCount;
    }

    public List<BaseDTO> getRepositoryCountList() {
        return repositoryCountList;
    }

    public void setRepositoryCountList(List<BaseDTO> repositoryCountList) {
        this.repositoryCountList = repositoryCountList;
    }

    public List<RepositoryConfine> getList() {
        return list;
    }

    public void setList(List<RepositoryConfine> list) {
        this.list = list;
    }
    @Autowired
    IRepositoryConfineManager repositoryConfigManagerImpl;

    public String selectRepositoryCount(){
       List<RepositoryConfine> list = repositoryConfigManagerImpl.selectRepository();
        if (CollectionUtils.isNotEmpty(list)) {
            repositoryCountList = Lists.newArrayList();
            for (RepositoryConfine repositoryConfine : list) {
                repositoryCountList.add(new BaseDTO(Long.valueOf(repositoryConfine.getId()), repositoryConfine.getRepositoryCount().toString()));
            }
        }
        return this.returnSuccess();
    }

    public String update(){
        repositoryConfigManagerImpl.updateRepositoryCount(repositoryCount);
        return this.returnSuccess();
    }
}
