package com.wzitech.gamegold.facade.backend.action.repository;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.goods.business.IGameRepositoryManager;
import com.wzitech.gamegold.goods.business.IRepositoryConfineManager;
import com.wzitech.gamegold.goods.dao.IGameRepositoryDao;
import com.wzitech.gamegold.goods.entity.RepositoryConfineInfo;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * Created by ${SunYang} on 2017/3/27.
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class GameRepositoryAction extends AbstractAction {

    @Autowired
    IGameRepositoryManager gameRepositoryManager;
    @Autowired
    IRepositoryConfineManager repositoryConfineManager;

    private RepositoryConfineInfo repositoryConfineInfo;

    private List<RepositoryConfineInfo> repositoryConfineInfos;

    private String gameName;

    private Long id;


    //添加库存限制配置信息
    public String addRepositoryConfig() {
        RepositoryConfineInfo repositoryConfineInfoNew = null;
        try {
            //根据游戏名称查询，如果存在不允许添加
            if (StringUtils.isNotBlank(gameName)) {
                repositoryConfineInfoNew = gameRepositoryManager.selectRepositoryByName(gameName);
            }

            if (repositoryConfineInfoNew != null) {
                return this.returnError("该游戏详细库存限制配置已经存在！");
            }
            repositoryConfineInfo.setCreateTime(new Date());
            repositoryConfineInfo.setUpdateTime(new Date());
            gameRepositoryManager.addGameRepository(repositoryConfineInfo);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }


    //查询
    public String queryGameRepositoryList() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (repositoryConfineInfo != null) {
            if (StringUtils.isNotBlank(repositoryConfineInfo.getGameName())) {
                queryMap.put("gameName", repositoryConfineInfo.getGameName());
            }
            if (StringUtils.isNotBlank(repositoryConfineInfo.getRegionName())) {
                queryMap.put("regionName", repositoryConfineInfo.getRegionName());
            }
            if (StringUtils.isNotBlank(repositoryConfineInfo.getServerName())) {
                queryMap.put("serverName", repositoryConfineInfo.getServerName());
            }
            if (StringUtils.isNotBlank(repositoryConfineInfo.getRaceName())) {
                queryMap.put("raceName", repositoryConfineInfo.getRaceName());
            }
            if (StringUtils.isNotBlank(repositoryConfineInfo.getGoodsTypeName())) {
                queryMap.put("goodsTypeName", repositoryConfineInfo.getGoodsTypeName());
            }
        }
        GenericPage<RepositoryConfineInfo> genericPage = gameRepositoryManager.queryGameRepositoryList
                (queryMap, this.limit, this.start, "id", true);
        repositoryConfineInfos = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    //修改
    public String updateGameRepository() {
        if (repositoryConfineInfo != null) {
            RepositoryConfineInfo repositoryConfineInfoOne = gameRepositoryManager.selectById(repositoryConfineInfo.getId());
            if (repositoryConfineInfoOne != null) {
                if (StringUtils.isBlank(repositoryConfineInfoOne.getRaceName())) {
                    repositoryConfineInfoOne.setRaceName("");
                }
                repositoryConfineInfoOne.setRepositoryCount(repositoryConfineInfo.getRepositoryCount());
                repositoryConfineInfoOne.setEnabled(repositoryConfineInfo.getEnabled());
                gameRepositoryManager.update(repositoryConfineInfoOne);
            }
        }
        return this.returnSuccess();
    }

    //删除
    public String deleteGameRepository() {
        try {
            gameRepositoryManager.deleteConfig(repositoryConfineInfo);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    //启用
    public String enableRepository() {
        try {
            gameRepositoryManager.enabledConfig(repositoryConfineInfo);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    //禁用
    public String disableRepository() {
        try {
            gameRepositoryManager.disableConfig(repositoryConfineInfo);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    //全局更新
    public String updateAll() {

        List<RepositoryConfineInfo> list = new ArrayList<RepositoryConfineInfo>();
        int startIndex = 0;
        int count = 75;
        //先分页查询一次获取总条目数, 这里设置一次取75条
        GenericPage<RepositoryConfineInfo> genericPage = gameRepositoryManager.selectAllForUpdate(count, startIndex);
        Long totalCount = genericPage.getTotalCount();
        //当startIndex=0 count*startIndex=0 取出来的是1-75条 startIndex=1 count*startIndex=75 取出来的 76-150
        //以此类推 当count*startIndex<总条目数时 去进行分页查询 并将数据置入一个list<RepositoryConfineInfo>,反之就不查了,如果count*startIndex = 总条目数
        //实际上在之前的一次分页查询中已经将最后的一批查出来了
        while (count * startIndex < totalCount) {
            GenericPage<RepositoryConfineInfo> genericPage1 = gameRepositoryManager.selectAllForUpdate(count, startIndex * count);
            startIndex++;
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                return this.returnError("更新时发生异常,请重试");
            }
            List<RepositoryConfineInfo> list1 = genericPage1.getData();
            list.addAll(list1);
        }
        for (RepositoryConfineInfo repositoryConfineInfo : list) {
            RepositoryInfo repositoryInfo = new RepositoryInfo();
            repositoryInfo.setGameName(repositoryConfineInfo.getGameName());
            repositoryInfo.setServer(repositoryConfineInfo.getServerName());
            repositoryInfo.setRegion(repositoryConfineInfo.getRegionName());
            repositoryInfo.setGoodsTypeName(repositoryConfineInfo.getGoodsTypeName());
            if (StringUtils.isNotBlank(repositoryConfineInfo.getRaceName())) {
                repositoryInfo.setGameRace(repositoryConfineInfo.getRaceName());
            }
            try {
                repositoryConfineManager.selectByMinUnitPrice(repositoryInfo);
                Thread.sleep(300);
            } catch (InterruptedException e) {
                return this.returnError("更新时发生异常,请重试");
            }
        }
        return this.returnSuccess();
    }

    public RepositoryConfineInfo getRepositoryConfineInfo() {
        return repositoryConfineInfo;
    }

    public void setRepositoryConfineInfo(RepositoryConfineInfo repositoryConfineInfo) {
        this.repositoryConfineInfo = repositoryConfineInfo;
    }

    public List<RepositoryConfineInfo> getRepositoryConfineInfos() {
        return repositoryConfineInfos;
    }

    public void setRepositoryConfineInfos(List<RepositoryConfineInfo> repositoryConfineInfos) {
        this.repositoryConfineInfos = repositoryConfineInfos;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
