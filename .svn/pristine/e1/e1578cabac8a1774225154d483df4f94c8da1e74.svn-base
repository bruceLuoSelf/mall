package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.IShGameConfigManager;
import com.wzitech.gamegold.shorder.business.IShGameTradeManager;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import com.wzitech.gamegold.shorder.entity.ShGameTrade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ljn on 2018/3/13.
 */
@Scope("prototype")
@ExceptionToJSON
@Controller
public class ShGameTradeAction extends AbstractAction {

    private ShGameTrade shGameTrade;

    private Long id;

    private List<ShGameTrade> shGameTradeList;

    private List<Long> tradeIds;

    private String errorMessage;

    @Autowired
    private IShGameTradeManager shGameTradeManager;

    @Autowired
    private IShGameConfigManager shGameConfigManager;

    /**
     * 查询配置
     * @return
     */
    public String queryShGameTrade() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (shGameTrade != null) {
            if (shGameTrade.getGameTableId() != null) {
                map.put("gameTableId",shGameTrade.getGameTableId());
            }
            if (shGameTrade.getShMode() != null && shGameTrade.getShMode() != 0) {
                map.put("shMode",shGameTrade.getShMode());
            }
        }
        GenericPage<ShGameTrade> page = shGameTradeManager.queryByMap(map, this.limit, this.start, "game_table_id,sh_mode", false);
        shGameTradeList = page.getData();
        if (!CollectionUtils.isEmpty(shGameTradeList)) {
            for (ShGameTrade eo : shGameTradeList) {
                ShGameConfig shGameConfig = shGameConfigManager.selectById(eo.getGameTableId());
                eo.setGameGoodsTypeName(shGameConfig.getGameName()+":"+shGameConfig.getGoodsTypeName());
                eo.setTradeTypeName(eo.getTrade() == null ? "" : eo.getTrade().getName());
            }
        }
        totalCount = page.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 删除配置
     * @return
     */
    public String deleteShGameTrade(){
        try {
            shGameTradeManager.deleteById(id);
            return this.returnSuccess();
        } catch (SystemException e){
            return this.returnError(e);
        }
    }

    /**
     * 增加配置
     * @return
     */
    public String addShGameTrade(){
        try {
            if (CollectionUtils.isEmpty(tradeIds)) {
                throw new SystemException(ResponseCodes.EmptyTradeTypeId.getCode(),ResponseCodes.EmptyTradeTypeId.getMessage());
            }
            if (shGameTrade.getShMode() == null) {
                throw new SystemException(ResponseCodes.EmptyDeliveryTypeId.getCode(),ResponseCodes.EmptyDeliveryTypeId.getMessage());
            }
            if (shGameTrade.getGameTableId() == null) {
                throw new SystemException(ResponseCodes.EmptyGameConfigId.getCode(),ResponseCodes.EmptyGameConfigId.getMessage());
            }
            try {
                shGameTradeManager.addShGameTrade(shGameTrade,tradeIds);
            } catch (RuntimeException e) {
                errorMessage = e.getMessage();
            }
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 修改配置
     * @return
     */
    public String updateShGameTrade(){
        try {
            shGameTradeManager.updateShGameTrade(shGameTrade);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public ShGameTrade getShGameTrade() {
        return shGameTrade;
    }

    public void setShGameTrade(ShGameTrade shGameTrade) {
        this.shGameTrade = shGameTrade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ShGameTrade> getShGameTradeList() {
        return shGameTradeList;
    }

    public void setShGameTradeList(List<ShGameTrade> shGameTradeList) {
        this.shGameTradeList = shGameTradeList;
    }

    public List<Long> getTradeIds() {
        return tradeIds;
    }

    public void setTradeIds(List<Long> tradeIds) {
        this.tradeIds = tradeIds;
    }

    public IShGameTradeManager getShGameTradeManager() {
        return shGameTradeManager;
    }

    public void setShGameTradeManager(IShGameTradeManager shGameTradeManager) {
        this.shGameTradeManager = shGameTradeManager;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
