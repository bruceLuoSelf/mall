package com.wzitech.gamegold.facade.backend.action.repository;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.goods.business.IRepositoryConfineManager;
import com.wzitech.gamegold.goods.dao.IRepositoryConfigRedisDAO;
import com.wzitech.gamegold.goods.entity.RepositoryConfine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jhlcitadmin on 2017/3/17.
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class RepositoryConfigAction extends AbstractAction {

    @Autowired
    IRepositoryConfineManager repositoryConfigManagerImpl;

    @Autowired
    IRepositoryConfigRedisDAO repositoryConfigRedisDAO;

    private RepositoryConfine repositoryConfine;

    private List<RepositoryConfine> repositoryConfigList;
    //private Boolean isEnabled;

    private Long id;

    public RepositoryConfine getRepositoryConfine() {
        return repositoryConfine;
    }

    public void setRepositoryConfine(RepositoryConfine repositoryConfine) {
        this.repositoryConfine = repositoryConfine;
    }

    public List<RepositoryConfine> getRepositoryConfigList() {
        return repositoryConfigList;
    }

    public void setRepositoryConfigList(List<RepositoryConfine> repositoryConfigList) {
        this.repositoryConfigList = repositoryConfigList;
    }



    //添加库存限制配置信息
    public  String addRepositoryConfig(){
        try{
            //根据游戏名称查询，如果存在不允许添加
           RepositoryConfine repositoryConfig = repositoryConfigManagerImpl.selectRepositoryByMap(repositoryConfine.getGameName(),repositoryConfine.getGoodsTypeName());
            if(repositoryConfig!=null){
                return this.returnError("该游戏库存限制配置已经存在！");
            }
            repositoryConfine.setCreateTime(new Date());
            repositoryConfine.setUpdateTime(new Date());
            repositoryConfigManagerImpl.addRepositoryConfig(repositoryConfine);
            return this.returnSuccess();
        }catch (SystemException e){
            return this.returnError(e);
        }
    }

    //查询
    public String queryRepositoryConfigList(){
            Map<String, Object> queryMap = new HashMap<String, Object>();
            if (repositoryConfine != null) {
                if(StringUtils.isNotBlank(repositoryConfine.getGameName())){
                    if(!repositoryConfine.getGameName().equals("全部")){
                        queryMap.put("gameName",repositoryConfine.getGameName());
                    }
                }
            }
            GenericPage<RepositoryConfine> genericPage = repositoryConfigManagerImpl.queryRepositoryList(queryMap, this.limit, this.start, "id", true);
            repositoryConfigList = genericPage.getData();
            totalCount = genericPage.getTotalCount();
            return this.returnSuccess();
        }

        //删除
    public String deleteConfig(){
        try {
            repositoryConfigManagerImpl.deleteConfig(repositoryConfine);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    //启用
    public String enabledConfig(){
        try {
            repositoryConfigManagerImpl.enabledConfig(repositoryConfine);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    //禁用
    public String disableConfig(){
        try {
            repositoryConfigManagerImpl.disableConfig(repositoryConfine);
            return this.returnSuccess();
        }catch (SystemException e){
            return this.returnError(e);
        }

    }

    //修改
    public String modifyRepositoryConfig(){
        if(repositoryConfine!=null){
            repositoryConfigManagerImpl.updateConfig(repositoryConfine);
        }
        return this.returnSuccess();
    }




}
