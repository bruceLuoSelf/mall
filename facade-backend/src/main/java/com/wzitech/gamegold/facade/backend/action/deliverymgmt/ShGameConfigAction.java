package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.google.common.collect.Lists;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.IGoodsTypeManager;
import com.wzitech.gamegold.shorder.business.IPurchaseGameManager;
import com.wzitech.gamegold.shorder.business.IShGameConfigManager;
import com.wzitech.gamegold.shorder.dto.GameGoodsTypeDTO;
import com.wzitech.gamegold.shorder.entity.CommonType;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by Administrator on 2017/1/4.
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class ShGameConfigAction extends AbstractAction {

    private ShGameConfig shGameConfig;


    private Long id;

    private List<ShGameConfig> gameConfigList;

    private Integer deliveryTypeId;
    private String gameName;
    private String goodsTypeId;
    private Integer shMode;
    private List<CommonType> commonTypeList;

    private List<GameGoodsTypeDTO> gameGoodsTypeList;

    @Autowired
    IShGameConfigManager gameConfigManager;
    @Autowired
    IGoodsTypeManager goodsTypeManager;
    @Autowired
    IPurchaseGameManager purchaseGameManager;

    /**
     * 分页查询收货游戏配置
     *
     * @return
     */
    public String queryShGameConfig() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (shGameConfig != null) {
            if (StringUtils.isNotBlank(shGameConfig.getGameName()) || StringUtils.isNotBlank(shGameConfig.getGoodsTypeName())) {
                //如果游戏名称是‘全部’，查询全部
                if (!shGameConfig.getGameName().equals("全部")) {
                    map.put("gameName", shGameConfig.getGameName());

                }
                if (!shGameConfig.getGoodsTypeName().equals("全部")) {
                    map.put("goodsTypeName", shGameConfig.getGoodsTypeName());
                }
            }
        }
        GenericPage<ShGameConfig> page = gameConfigManager.queryByMap(map, this.start, this.limit, "update_time", false);
        gameConfigList = page.getData();
        totalCount = page.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 根据游戏名称，开关查询游戏类目配置，不分页
     *
     * @return
     */
    public String queryConfigByGameName() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (shGameConfig != null) {
            if (StringUtils.isNotBlank(shGameConfig.getGameName())) {
                //如果游戏名称是‘全部’，查询全部
                if (!shGameConfig.getGameName().equals("全部")) {
                    map.put("gameName", shGameConfig.getGameName());
                } else {
                    return this.returnError("游戏名称不能为空");
                }
            } else {
                return this.returnError("游戏名称不能为空");
            }
            if (shGameConfig.getIsEnabled() != null) {
                map.put("isEnabled", shGameConfig.getIsEnabled());
            }
            if (shGameConfig.getEnableMall() != null) {
                map.put("enableMall", shGameConfig.getEnableMall());
            }
        }
        gameConfigList = gameConfigManager.queryByMap(map, "update_time", false);
        return this.returnSuccess();
    }

    public String queryConfigByGameNameAndDeliveryTypeId() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(gameName)) {
            map.put("gameName", gameName);
        }
        //todo 临时做法，需要扩展
//        if (deliveryTypeId != null) {
//            map.put("enableRobot", deliveryTypeId == DeliveryTypeEnum.Manual.getCode());
//        }
        gameConfigList = gameConfigManager.queryByMap(map, "id", false);
        return this.returnSuccess();
    }

    public String queryConfigByGameNameAndDeliveryTypeIdAndGoodsType() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(gameName)) {
            map.put("gameName", gameName);
        }

        //todo 临时做法，需要扩展
//        if (deliveryTypeId != null) {
//            map.put("enableRobot", deliveryTypeId == DeliveryTypeEnum.Manual.getCode());
//        }
        if (goodsTypeId != null) {
            map.put("goodsTypeId", goodsTypeId);
        }
        gameConfigList = gameConfigManager.queryByMap(map, "id", false);
        if (gameConfigList != null && gameConfigList.size() > 0) {
            commonTypeList = new ArrayList<CommonType>();
            for (ShGameConfig gameConfig : gameConfigList) {
                String tradeTypeIdStr = gameConfig.getTradeTypeId();
                String tradeTypeNameStr = gameConfig.getTradeType();

                List<String> tradeTypeIdList = new ArrayList<String>();
                List<String> tradeTypeNameList = new ArrayList<String>();
                if (tradeTypeIdStr.indexOf(",") > -1) {
                    tradeTypeIdList = Arrays.asList(tradeTypeIdStr.split(","));
                    tradeTypeNameList = Arrays.asList(tradeTypeNameStr.split(","));
                }

                if (tradeTypeIdList != null && tradeTypeIdList.size() > 0) {
                    for (int i = 0; i < tradeTypeIdList.size(); i++) {
                        int tradeTypeId = Integer.parseInt(tradeTypeIdList.get(i));
                        String tradeTypeName = tradeTypeNameList.get(i);
                        commonTypeList.add(new CommonType(tradeTypeId, tradeTypeName));
                    }
                }
            }
        }
        return this.returnSuccess();
    }

    /**
     * 根据游戏名称和收货模式查询商品类型
     * @return
     */
    public String selectGoodsTypeByGameAndShMode(){
        Map<String,Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(gameName)) {
            map.put("gameName",gameName);
        }
        if (shMode != null) {
            map.put("shMode",shMode);
        }
        gameConfigList = gameConfigManager.selectGoodsTypeByGameAndShMode(map);
        return this.returnSuccess();
    }


    /**
     * 删除收货游戏配置
     *
     * @return
     */
    public String deleteShGameConfig() {
        try {
            gameConfigManager.deleteGameConfig(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }

    }

    /**
     * 修改收货游戏配置
     *
     * @return
     */
    public String updateShGameConfig() {
        if (shGameConfig != null) {
            //根据游戏名称和游戏类目查询游戏配置，如果存在不能修改
            ShGameConfig gameConfig = gameConfigManager.selectById(this.shGameConfig.getId());
            if (gameConfig != null) {
                gameConfig.setUnitName(this.shGameConfig.getUnitName());
                gameConfig.setEnableRobot(this.shGameConfig.getEnableRobot());
                gameConfig.setTradeType(this.shGameConfig.getTradeType());
                gameConfig.setTradeTypeId(this.shGameConfig.getTradeTypeId());
                gameConfig.setIsEnabled(this.shGameConfig.getIsEnabled());
                gameConfig.setMinCount(this.shGameConfig.getMinCount());
                gameConfig.setMinBuyAmount(this.shGameConfig.getMinBuyAmount());//新增'最低购买金额'
                gameConfig.setNineBlockConfigure(this.shGameConfig.getNineBlockConfigure());
                gameConfig.setDeliveryMessage(this.shGameConfig.getDeliveryMessage());
                gameConfig.setPoundage(this.shGameConfig.getPoundage());
                gameConfig.setRepositoryCount(this.shGameConfig.getRepositoryCount());
                gameConfig.setNeedCount(this.shGameConfig.getNeedCount());
                gameConfig.setMailFee(this.shGameConfig.getMailFee());
                gameConfig.setThresholdCount(this.shGameConfig.getThresholdCount());
                gameConfigManager.updateShGameConfig(gameConfig);
                purchaseGameManager.update(this.shGameConfig);
                return this.returnSuccess();
            }
//            if(s == null){
//                gameConfigManager.updateShGameConfig(shGameConfig);
//                //关联同步收货商游戏属性配置，根据交易类目和游戏名称设置收货商游戏属性配置
//                PurchaseGame p = new PurchaseGame();
//                update(p,shGameConfig);
//            }else{
//               // return this.returnError("该类目的游戏已经存在");
//                //如果存在则删除，将这条修改的记录作为新数据添加上去
//                gameConfigManager.deleteGameConfig(s.getId());
//                addShGameConfig();
//                PurchaseGame p = new PurchaseGame();
//               update(p,shGameConfig);
//            }
        }
        return this.returnSuccess();
    }

    /**
     * 增加
     *
     * @return
     */
    public String addShGameConfig() {

        //根据游戏类目名称查询游戏类目id
        Long id = goodsTypeManager.queryGoodsTypeIdByName(shGameConfig.getGoodsTypeName());
        shGameConfig.setGoodsTypeId(id);
        shGameConfig.setNineBlockEnable(true);
        if (shGameConfig != null) {
            //如果交易类目和游戏名称已经存在，不添加
            ShGameConfig s = gameConfigManager.selectShGameConfig(shGameConfig);
            if (s == null) {
                // gameConfigManager.selectShGameConfig(shGameConfig);
                gameConfigManager.addShGameConfig(shGameConfig);
                return this.returnSuccess();
            } else {
                return this.returnError("该类目的游戏已经存在");
            }
        }
        return this.returnError("添加失败");
    }

    /**
     * 禁用
     *
     * @return
     */
    public String disableShGameConfig() {
        try {
            gameConfigManager.disableUser(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 启用用户
     *
     * @return
     */
    public String qyShGameConfig() {
        try {
            gameConfigManager.qyUser(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 禁用商城
     * add by lvchengsheng 2015.5.16 ZW_C_JB_00008
     *
     * @return
     */
    public String disableMallShGameConfig() {
        try {
            gameConfigManager.disableMall(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }


    /**
     * 禁用九宫格
     */
    public String disableNineBlock(){
        try {
            gameConfigManager.disableNineBlock(id);
            return this.returnSuccess();
        }catch (SystemException e){
            return this.returnError(e);
        }
    }

    /**
     * 启用九宫格
     */
    public String enableNineBlock(){
        try {
            gameConfigManager.enableNineBlock(id);
            return this.returnSuccess();
        }catch (SystemException e){
            return this.returnError(e);
        }
    }

    /**
     * 启用分仓配置
     */
    public String isSplitWarehouse(){
        try {
            gameConfigManager.isSplitWarehouse(id);
            return this.returnSuccess();
        }catch (SystemException e){
            return this.returnError(e);
        }
    }

    /**
     * 禁用分仓配置
     */
    public String disableWarehouse(){
        try {
            gameConfigManager.disableWarehouse(id);
            return this.returnSuccess();
        }catch (SystemException e){
            return this.returnError(e);
        }
    }

    /**
     * 启用商城
     * add by lvchengsheng 2015.5.16 ZW_C_JB_00008
     *
     * @return
     */
    public String enableMallShGameConfig() {
        try {
            gameConfigManager.enableMall(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 获取游戏商品类型列表
     * @return
     */
    public String queryGameGoodsType(){
        List<ShGameConfig> configList = gameConfigManager.selectEnableConfig();
        gameGoodsTypeList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(configList)) {
            for (ShGameConfig config : configList) {
                gameGoodsTypeList.add(new GameGoodsTypeDTO(config.getId(),config.getGameName() +":"+ config.getGoodsTypeName()));
            }
        }
        return this.returnSuccess();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShGameConfig getShGameConfig() {
        return shGameConfig;
    }

    public void setShGameConfig(ShGameConfig shGameConfig) {
        this.shGameConfig = shGameConfig;
    }

    public List<ShGameConfig> getGameConfigList() {
        return gameConfigList;
    }

    public void setGameConfigList(List<ShGameConfig> gameConfigList) {
        this.gameConfigList = gameConfigList;
    }

    public Integer getDeliveryTypeId() {
        return deliveryTypeId;
    }

    public void setDeliveryTypeId(Integer deliveryTypeId) {
        this.deliveryTypeId = deliveryTypeId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public List<CommonType> getCommonTypeList() {
        return commonTypeList;
    }

    public void setCommonTypeList(List<CommonType> commonTypeList) {
        this.commonTypeList = commonTypeList;
    }

    public List<GameGoodsTypeDTO> getGameGoodsTypeList() {
        return gameGoodsTypeList;
    }

    public void setGameGoodsTypeList(List<GameGoodsTypeDTO> gameGoodsTypeList) {
        this.gameGoodsTypeList = gameGoodsTypeList;
    }

    public Integer getShMode() {
        return shMode;
    }

    public void setShMode(Integer shMode) {
        this.shMode = shMode;
    }
}
