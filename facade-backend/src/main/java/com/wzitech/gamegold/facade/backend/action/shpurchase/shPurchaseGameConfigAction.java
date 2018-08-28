package com.wzitech.gamegold.facade.backend.action.shpurchase;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.IPurchaseGameManager;
import com.wzitech.gamegold.shorder.business.IPurchaserGameTradeManager;
import com.wzitech.gamegold.shorder.dao.IGameCategoryConfigDao;
import com.wzitech.gamegold.shorder.dao.ITradeDao;
import com.wzitech.gamegold.shorder.entity.GameCategoryConfig;
import com.wzitech.gamegold.shorder.entity.PurchaseGame;
import com.wzitech.gamegold.shorder.entity.PurchaserGameTrade;
import com.wzitech.gamegold.shorder.entity.Trade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/15.
 */

@Controller
@Scope("prototype")
@ExceptionToJSON
public class shPurchaseGameConfigAction extends AbstractAction {

    public PurchaseGame purchaseGame;

    public List<PurchaseGame> purchaseGameList;


    public List<GameCategoryConfig> gameCategoryConfigList;

    public List<Trade> tradeList;

    @Autowired
    private IPurchaseGameManager purchaseGameManager;

    @Autowired
    private IGameCategoryConfigDao gameCategoryConfigDao;

    @Autowired
    private ITradeDao tradeDao;

    @Autowired
    private IPurchaserGameTradeManager purchaserGameTradeManager;

    /**
     * 根据条件查询收货商游戏配置
     *
     * @return
     * @throws ParseException
     */
    public String queryPurchase() throws ParseException {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (null != purchaseGame) {
            if (null != purchaseGame.getPurchaseId()) {
                queryMap.put("purchaseId", purchaseGame.getPurchaseId());
            }
            if (StringUtils.isNotBlank(purchaseGame.getPurchaseAccount())) {
                queryMap.put("purchaseAccount", purchaseGame.getPurchaseAccount().trim());
            }
            if (StringUtils.isNotBlank(purchaseGame.getGameName())) {
                queryMap.put("gameName", purchaseGame.getGameName().trim());
            }
            if (StringUtils.isNotBlank(purchaseGame.getGoodsTypeName()) && !purchaseGame.getGoodsTypeName().equals("全部")) {
                queryMap.put("goodsTypeName", purchaseGame.getGoodsTypeName().trim());
            }
            if (StringUtils.isNotBlank(purchaseGame.getDeliveryTypeName())) {
                queryMap.put("deliveryTypeName", purchaseGame.getDeliveryTypeName().trim());
            }
            //目前数据库设计需要进行模糊查询
            if (StringUtils.isNotBlank(purchaseGame.getTradeTypeName()) && !purchaseGame.getTradeTypeName().equals("全部")) {
                queryMap.put("tradeTypeNameMode", purchaseGame.getTradeTypeName().trim());
            }
            if (purchaseGame.getDeliveryTypeId() != null && purchaseGame.getDeliveryTypeId() != 0) {
                queryMap.put("deliveryTypeId", purchaseGame.getDeliveryTypeId());
            }
        }
        GenericPage<PurchaseGame> genericPage = purchaseGameManager.queryListInPage(queryMap, this.start, this.limit, "id", false);
        List<PurchaseGame> data = genericPage.getData();
        if (!CollectionUtils.isEmpty(data)) {
            for (PurchaseGame purchase : data) {
                List<PurchaserGameTrade> purchaserGameTrades = purchaserGameTradeManager.selectByPurchaserId(purchase.getId());
                if (CollectionUtils.isEmpty(purchaserGameTrades)) {
                    purchase.setTradeTypeId("");
                    purchase.setTradeTypeName("");
                    continue;
                }
                String tradeTypeId = "";
                String tradeTypeName = "";
                for (PurchaserGameTrade purchaserGameTrade : purchaserGameTrades) {
                    tradeTypeId += purchaserGameTrade.getTradeTypeId() + ",";
                    tradeTypeName += purchaserGameTrade.getTradeTypeName() + ",";
                }
                tradeTypeId = tradeTypeId.substring(0,tradeTypeId.length() - 1);
                if (StringUtils.isNotBlank(tradeTypeName)) {
                    tradeTypeName = tradeTypeName.substring(0,tradeTypeName.length() - 1);
                }
                purchase.setTradeTypeId(tradeTypeId);
                purchase.setTradeTypeName(tradeTypeName);
            }
        }
        purchaseGameList = data;
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 查询启用状态的商品类目
     *
     * @return
     */
    public String queryGoodsTypeName() {
        Map<String, Object> quert = new HashMap<String, Object>();
        quert.put("isEnabled", true);
        GenericPage<GameCategoryConfig> page = gameCategoryConfigDao.selectByMap(quert, this.limit, this.start, "id", false);

        //筛选用商品类目名
        GameCategoryConfig all = new GameCategoryConfig();
        all.setName("全部");
        gameCategoryConfigList = page.getData();
        gameCategoryConfigList.add(0, all);
        totalCount = page.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 查询启用状态的交易方式名称
     *
     * @return
     */
    public String queryTradeTypeName() throws ParseException {
        Map<String, Object> querr = new HashMap<String, Object>();
        querr.put("enabled", true);
        GenericPage<Trade> PAGE = tradeDao.selectByMap(querr, this.limit, this.start, "id", false);
        Trade all = new Trade();
        all.setName("全部");
        tradeList = PAGE.getData();
        tradeList.add(0, all);
        totalCount = PAGE.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 增加交易方式标识数据
     * @return
     */
    public String addTradeLogoData() {
        try{
            purchaseGameManager.addTradeLogoData();
            return this.returnSuccess();
        }catch (Exception e){
            return this.returnError(e.getMessage());
        }
    }


    public PurchaseGame getPurchaseGame() {
        return purchaseGame;
    }

    public void setPurchaseGame(PurchaseGame purchaseGame) {
        this.purchaseGame = purchaseGame;
    }

    public List<PurchaseGame> getPurchaseGameList() {
        return purchaseGameList;
    }

    public void setPurchaseGameList(List<PurchaseGame> purchaseGameList) {
        this.purchaseGameList = purchaseGameList;
    }

    public List<GameCategoryConfig> getGameCategoryConfigList() {
        return gameCategoryConfigList;
    }

    public void setGameCategoryConfigList(List<GameCategoryConfig> gameCategoryConfigList) {
        this.gameCategoryConfigList = gameCategoryConfigList;
    }

    public List<Trade> getTradeList() {
        return tradeList;
    }

    public void setTradeList(List<Trade> tradeList) {
        this.tradeList = tradeList;
    }
}
