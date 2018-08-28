package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.ITradeManager;
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
 * Created by Administrator on 2017/1/4.
 * 收货模式配置
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class TradeAction extends AbstractAction{

    @Autowired
    ITradeManager tradeManager;
    private Trade trade;
    private List<Trade> tradeList;
    private Long id;
    private String name;
    private String gameName;
    private String goodsTypeId;
    private Integer shMode;


    /**
     * 查询启用状态的交易方式名称
     */
    public String queryTradeTypeName() throws ParseException{
        Map<String,Object> queryparam=new HashMap<String, Object>();
        queryparam.put("enabled",true);
        GenericPage<Trade> page=tradeManager.query(queryparam,this.limit,this.start,
                "create_time",false);
        //增加 一个trade 交易方式名称为全部 以备筛选用
        Trade forall =new Trade();
        forall.setName("全部");
        tradeList=page.getData();
        tradeList.add(0,forall);
        totalCount=page.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 查询收货模式配置
     * @return
     * @throws ParseException
     */
    public String queryTrade() throws ParseException {
        Map<String,Object> map=new HashMap<String,Object>();
        if(trade!=null){
            if(StringUtils.isNotBlank(trade.getName())){
                map.put("name",trade.getName().trim());
            }
        }
        GenericPage<Trade> page=tradeManager.query(map,this.limit,this.start,"create_time",false);
        tradeList=page.getData();
        totalCount=page.getTotalCount();
        return this.returnSuccess();
    }

    public String queryTradeList(){
        tradeList = tradeManager.queryByMap(null);
        if (CollectionUtils.isEmpty(tradeList)) {
            totalCount = 0l;
        }else{
            totalCount = (long)tradeList.size();
        }
        return this.returnSuccess();
    }

    /**
     * 添加配置
     * @return
     */
    public String addTrade(){
        try{
            tradeManager.addTrade(trade);
            return returnSuccess();
        }catch (SystemException e){
            return this.returnError(e);
        }
    }

    /**
     * 删除配置
     */
    public String deleteTrade(){
        try{
            if(id!=null){
                tradeManager.deleteTrade(id);
            }
            return returnSuccess();
        }catch (SystemException e){
            return this.returnError(e);
        }
    }

    /**
     * 修改配置
     */
    public String updateTrade(){
        try{
            tradeManager.update(trade.getId(),trade.getTradeLogo());
            return this.returnSuccess();
        }catch(SystemException e){
            return this.returnError(e);
        }
    }

    /**
     * 启用
     * @return
     */
    public String enabledTrade(){
        try{
            tradeManager.enabled(id);
            return this.returnSuccess();
        }catch (SystemException e){
            return this.returnError(e);
        }
    }

    /**
     * 禁用
     * @return
     */
    public String disabledTrade(){
        try{
            tradeManager.disabled(id);
            return returnSuccess();
        }catch (SystemException e){
            return returnError(e);
        }
    }

    /**
     * 根据游戏，商品类型和收货模式查询交易方式
     * @return
     */
    public String selectTradeByGameGoodsTypeAndShMode(){
        Map<String,Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(gameName)) {
            map.put("gameName",gameName);
        }
        if (StringUtils.isNotBlank(goodsTypeId)) {
            map.put("goodsTypeId",goodsTypeId);
        }
        if (shMode != null) {
            map.put("shMode",shMode);
        }
        tradeList = tradeManager.selectTradeByGameGoodsTypeAndShMode(map);
        return returnSuccess();
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public List<Trade> getTradeList() {
        return tradeList;
    }

    public void setTradeList(List<Trade> tradeList) {
        this.tradeList = tradeList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getShMode() {
        return shMode;
    }

    public void setShMode(Integer shMode) {
        this.shMode = shMode;
    }
}
