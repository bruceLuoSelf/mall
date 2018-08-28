package com.wzitech.gamegold.facade.backend.action.hotrecommend;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.goods.business.IHotRecommendConfigManager;
import com.wzitech.gamegold.goods.dao.IHotRecommendConfigDBDAO;
import com.wzitech.gamegold.goods.dao.IHotRecommendConfigRedisDAO;
import com.wzitech.gamegold.goods.entity.HotRecommendConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前台安心买热卖商品配置
 *
 * @author yemq
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class HotRecommendConfigAction extends AbstractAction {
    @Autowired
    private IHotRecommendConfigManager hotRecommendConfigManager;

    @Autowired
    private IHotRecommendConfigDBDAO hotRecommendConfigDBDAO;

    @Autowired
    private IHotRecommendConfigRedisDAO hotRecommendConfigRedisDAO;

    /**
     * 游戏币配置ID列表
     */
    private List<Long> ids;
    /**
     * 游戏币配置对象
     */
    private HotRecommendConfig config;
    /**
     * 游戏币配置列表
     */
    private List<HotRecommendConfig> configList;

    /**
     * 分页查询前台安心买热卖商品配置列表
     *
     * @return
     */
    public String queryConfigList() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("gameName", config.getGameName());
        try {
            GenericPage<HotRecommendConfig> page = hotRecommendConfigManager.queryConfigList(queryMap, limit, start, "GAME_ID", true);
            configList = page.getData();
            totalCount = page.getTotalCount();
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 添加配置
     *
     * @return
     */
    public String addConfig() {
        try {
            hotRecommendConfigManager.add(config);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 更新配置
     *
     * @return
     */
    public String updateConfig() {
        try {
            hotRecommendConfigManager.update(config);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 删除配置
     *
     * @return
     */
    public String deleteConfig() {
        try {
            hotRecommendConfigManager.deleteByIds(ids);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public HotRecommendConfig getConfig() {
        return config;
    }

    public List<HotRecommendConfig> getConfigList() {
        return configList;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public void setConfig(HotRecommendConfig config) {
        this.config = config;
    }
}
