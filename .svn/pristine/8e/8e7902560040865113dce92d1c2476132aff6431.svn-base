package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.ILevelCarryLimitManager;
import com.wzitech.gamegold.shorder.entity.LevelCarryLimitEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ljn on 2018/4/3.
 */
@Scope("prototype")
@ExceptionToJSON
@Controller
public class LevelCarryLimitAction extends AbstractAction {

    private Long id;

    private LevelCarryLimitEO levelCarryLimitEO;

    private List<LevelCarryLimitEO> levelCarryLimitEOList;

    @Autowired
    private ILevelCarryLimitManager levelCarryLimitManager;

    /**
     * 分页查询
     * @return
     */
    public String queryLevelCarryLimit(){
        Map<String, Object> map = new HashMap<String, Object>();
        if (levelCarryLimitEO != null) {
            if (levelCarryLimitEO.getMinLevel() != null) {
                map.put("minLevel",levelCarryLimitEO.getMinLevel());
            }
            if (levelCarryLimitEO.getMaxLevel() != null) {
                map.put("maxLevel",levelCarryLimitEO.getMaxLevel());
            }
            if (levelCarryLimitEO.getCarryUpperLimit() != null) {
                map.put("carryUpperLimit",levelCarryLimitEO.getCarryUpperLimit());
            }
            if (StringUtils.isNotBlank(levelCarryLimitEO.getGameName())) {
                map.put("gameName",levelCarryLimitEO.getGameName());
            }
            if (levelCarryLimitEO.getGoodsTypeId() != null) {
                map.put("goodsTypeId",levelCarryLimitEO.getGoodsTypeId());
            }
        }
        GenericPage<LevelCarryLimitEO> page = levelCarryLimitManager.selectByMap(map, this.limit, this.start, "game_name,goods_type_id,min_level", true);
        levelCarryLimitEOList = page.getData();
        totalCount = page.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 删除
     * @return
     */
    public String deleteById() {
        try {
            levelCarryLimitManager.deleteById(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 增加等级携带上限配置
     * @return
     */
    public String addLevelCarryLimit() {
        try {
            levelCarryLimitManager.add(levelCarryLimitEO);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 修改等级携带上限配置
     * @return
     */
    public String updateLevelCarryLimit(){
        try {
            levelCarryLimitManager.updateLevelCarryLimit(levelCarryLimitEO);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String buildRedisData() {
        try{
            levelCarryLimitManager.buildRedisData();
            return this.returnSuccess();
        }catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LevelCarryLimitEO getLevelCarryLimitEO() {
        return levelCarryLimitEO;
    }

    public void setLevelCarryLimitEO(LevelCarryLimitEO levelCarryLimitEO) {
        this.levelCarryLimitEO = levelCarryLimitEO;
    }

    public List<LevelCarryLimitEO> getLevelCarryLimitEOList() {
        return levelCarryLimitEOList;
    }

    public void setLevelCarryLimitEOList(List<LevelCarryLimitEO> levelCarryLimitEOList) {
        this.levelCarryLimitEOList = levelCarryLimitEOList;
    }
}
