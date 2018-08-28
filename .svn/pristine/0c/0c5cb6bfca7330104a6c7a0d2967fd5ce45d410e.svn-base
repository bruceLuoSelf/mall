package com.wzitech.gamegold.facade.backend.action.shpurchase;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.action.ordermgmt.GameConfigAction;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.IGameCategoryConfigManager;
import com.wzitech.gamegold.shorder.entity.GameCategoryConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏类目配置
 * 孙杨  2017/2/14
 */

@Controller
@Scope("prototype")
@ExceptionToJSON
public class GameCategoryConfigAction extends AbstractAction {

    private GameCategoryConfig gameCategoryConfig;

    @Autowired
    private IGameCategoryConfigManager gameCategoryConfigManager;

    @Autowired
    private GameConfigAction gameConfigAction;

    private List<GameCategoryConfig> gameCategoryConfigList;

    private String name;

    private Long id;

    //------------------------------------------------------------------

    /**
     * 查询游戏类目配置列表
     *
     * @return
     */
    public String queryGameCategoryConfig() {
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            if (gameCategoryConfig != null && StringUtils.isNotBlank(gameCategoryConfig.getName())) {
                queryMap.put("name", gameCategoryConfig.getName());
            }
            GenericPage<GameCategoryConfig> genericPage = gameCategoryConfigManager.
                    queryPage(queryMap, this.limit, this.start, "id", false);
            gameCategoryConfigList = genericPage.getData();
            totalCount = genericPage.getTotalCount();
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 添加游戏类目配置
     *
     * @return
     */
    public String addGameCategoryConfig() {
        try {
            gameCategoryConfig.setName(name);
            gameCategoryConfigManager.addConfig(gameCategoryConfig);
            //收货游戏配置马上重新查询
          //  gameConfigAction.getGoodsTypeNameIdList();
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 修改游戏类目配置
     *
     * @return
     */
    public String updateGameCategoryConfig() {
        try {
            gameCategoryConfigManager.updateConfig(gameCategoryConfig);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 删除游戏类目配置
     *
     * @return
     */
    public String deleteGameCategoryConfig() {
        try {
            if (id != null) {
                gameCategoryConfigManager.deleteConfigById(id);
            }
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 启用配置
     *
     * @return
     */
    public String qyGameCategoryConfig() {
        try {
            gameCategoryConfigManager.qyConfig(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 禁用配置
     *
     * @return
     */
    public String disableGameCategory() {
        try {
            gameCategoryConfigManager.disableConfig(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }


    public GameCategoryConfig getGameCategoryConfig() {
        return gameCategoryConfig;
    }

    public void setGameCategoryConfig(GameCategoryConfig gameCategoryConfig) {
        this.gameCategoryConfig = gameCategoryConfig;
    }

    public List<GameCategoryConfig> getGameCategoryConfigList() {
        return gameCategoryConfigList;
    }

    public void setGameCategoryConfigList(List<GameCategoryConfig> gameCategoryConfigList) {
        this.gameCategoryConfigList = gameCategoryConfigList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
