package com.wzitech.gamegold.facade.backend.action.goodsmgmt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.goods.business.IWarningManager;
import com.wzitech.gamegold.goods.entity.Warning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;


/**
 * Created by Administrator on 2017/5/15.
 * <p>
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/15 liuchanghua           ZW_C_JB_00008 商城增加通货
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class WarningAction extends AbstractAction {

    private List<Long> ids;

    private List<Warning> warningList;

    private Warning warning;

    private Long id;

    private String gameName;

    private String goodsTypeName;

    private Integer tradeType;

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    @Autowired
    IWarningManager warningManager;

    /**
     * 新增友情提示
     *
     * @return
     */
    public String addWarning() {
        try {
            warningManager.insert(warning);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /*
     * 修改友情提示
     */
    public String modifyWarning() {
        try {
            Warning newWarning = warningManager.findById(id);
            newWarning.setWarning(warning.getWarning());
            newWarning.setTradeType(warning.getTradeType());
            warningManager.update(newWarning);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 删除友情提示
     *
     * @return
     */
    public String deleteWarning() {
        try {
            warningManager.deleteWarning(ids);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 查询商品信息列表
     *
     * @return
     */
    public String queryWarning() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("gameName", gameName);
        paramMap.put("goodsTypeName", goodsTypeName);
        if (tradeType != null) {
            paramMap.put("tradeType", tradeType);
        }
        GenericPage<Warning> genericPage = warningManager.selectByMap(paramMap,
                this.limit, this.start, null, true);
        warningList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }


    public List<Warning> getWarningList() {
        return warningList;
    }

    public void setWarningList(List<Warning> warningList) {
        this.warningList = warningList;
    }

    public Warning getWarning() {
        return warning;
    }

    public void setWarning(Warning warning) {
        this.warning = warning;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}