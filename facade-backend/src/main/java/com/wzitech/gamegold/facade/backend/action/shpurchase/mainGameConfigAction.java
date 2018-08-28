package com.wzitech.gamegold.facade.backend.action.shpurchase;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.IMainGameConfigManager;
import com.wzitech.gamegold.shorder.dao.IMainGameConfigDao;
import com.wzitech.gamegold.shorder.entity.MainGameConfig;
import com.wzitech.gamegold.shorder.entity.PurchaseGame;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jhlcitadmin on 2017/2/23.
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class mainGameConfigAction extends AbstractAction {

    private MainGameConfig mainGameConfig;

    private List<MainGameConfig> mainGameConfigList;

    private List<MainGameConfig> gameName;

    private String name;

    private String gameId;

    private Long id;

    @Autowired
    IMainGameConfigDao mainGameConfigDao;

    @Autowired
    IMainGameConfigManager mainGameConfigManager;

    /**
     * 根据游戏名称获取游戏配置项
     *
     * @return
     * @throws ParseException
     */
    public String queryGameConfig() throws ParseException {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(mainGameConfig.getGameName()) && !mainGameConfig.getGameName().equals("全部")) {
            queryMap.put("gameName", mainGameConfig.getGameName());
        }
        GenericPage<MainGameConfig> genericPage = mainGameConfigDao.selectByMap(queryMap, this.limit, this.start, "id", false);
        mainGameConfigList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 查询所有的游戏名称并增加全部选项作为动态获取筛选条件
     *
     * @return
     */
    public String queryGameNameForAll() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        GenericPage<MainGameConfig> genericPage = mainGameConfigDao.selectByMap(queryMap, this.limit, this.start, "id", false);
        MainGameConfig all = new MainGameConfig();
        all.setGameName("全部");
        gameName = genericPage.getData();
        if (null != gameName) {
            gameName.add(0, all);
        }
        return this.returnSuccess();
    }


    /**
     * 增加游戏类目配置
     *
     * @return
     */
    public String addMainGameConfig() {
        try {
            String testname = name;
            String testgameId = gameId;
            MainGameConfig mainGameConfig = new MainGameConfig();
            mainGameConfig.setGameName(testname);
            mainGameConfig.setGameId(testgameId);
            mainGameConfigManager.addMainGameConfig(mainGameConfig);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 修改主游戏配置（开启关闭功能）
     *
     * @return
     */
    public String updateMainGameConfig() {
        try {
            mainGameConfigManager.updateMainGameConfig(mainGameConfig);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 删除主游戏配置
     *
     * @return
     */
    public String deleteMainGameConfig() {
        try {
            if (id != null) {
                mainGameConfigManager.deleteConfigById(id);
            }
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 启用收货
     *
     * @return
     */
    public String qyDelivery() {
        try {
            mainGameConfigManager.qyDelivery(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 禁用收货
     *
     * @return
     */
    public String disableDelivery() {
        try {
            mainGameConfigManager.disableDelivery(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 禁用商城出售
     *
     * @return
     */
    public String disableSell() {
        try {
            mainGameConfigManager.disableSell(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 启用商城出售
     *
     * @return
     */
    public String qySell() {
        try {
            mainGameConfigManager.qySell(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public MainGameConfig getMainGameConfig() {
        return mainGameConfig;
    }

    public void setMainGameConfig(MainGameConfig mainGameConfig) {
        this.mainGameConfig = mainGameConfig;
    }

    public List<MainGameConfig> getMainGameConfigList() {
        return mainGameConfigList;
    }

    public void setMainGameConfigList(List<MainGameConfig> mainGameConfigList) {
        this.mainGameConfigList = mainGameConfigList;
    }

    public List<MainGameConfig> getGameName() {
        return gameName;
    }

    public void setGameName(List<MainGameConfig> gameName) {
        this.gameName = gameName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
