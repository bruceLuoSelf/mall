package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.ShDeliveryTypeEnum;
import com.wzitech.gamegold.shorder.business.ITradeManager;
import com.wzitech.gamegold.shorder.dao.*;
import com.wzitech.gamegold.shorder.entity.PurchaseGame;
import com.wzitech.gamegold.shorder.entity.PurchaserData;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import com.wzitech.gamegold.shorder.entity.Trade;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Administrator on 2017/1/4.
 */
@Component
public class TradeManagerImpl implements ITradeManager {

    @Autowired
    private ITradeDao tradeDao;
    @Autowired
    private ITradeRedisDao tradeRedisDao;

    @Autowired
    private IShGameConfigDao shGameConfigDao;
    @Autowired
    private IPurchaseGameDao purchaseGameDao;
    @Autowired
    private IPurchaserDataDao purchaserDataDao;
    @Autowired
    private IShGameTradeDao shGameTradeDao;

    /**
     * 添加功能
     *
     * @param trade
     * @return
     */
    @Override
    @Transactional
    public Trade addTrade(Trade trade) {
        HashMap<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("name", trade.getName());
        int count = tradeDao.countByMap(queryMap);
        if (count > 0) {
            throw new SystemException(ResponseCodes.GameCategoryConfig.getCode(), ResponseCodes.GameCategoryConfig.getMessage());
        }
        queryMap.clear();
        queryMap.put("tradeLogo",trade.getTradeLogo());
        int logoCount = tradeDao.countByMap(queryMap);
        if (logoCount > 0) {
            throw new SystemException(ResponseCodes.TradeLogoExist.getCode(), ResponseCodes.TradeLogoExist.getMessage());
        }
        trade.setEnabled(true);
        trade.setCreateTime(new Date());
        trade.setUpdateTime(trade.getCreateTime());
        tradeDao.insert(trade);
        tradeRedisDao.save(trade);
        return trade;
    }

    /**
     * 根据ids查询交易方式名称
     *
     * @param ids
     * @return
     */
    @Override
    public String getNamesByIds(String ids) {
        String[] tradeIds;
        String names = "";
        Trade trade = new Trade();
        boolean button = false;
        if (ids.contains(",")) {
            tradeIds = ids.trim().split(",");
            List<String> idList = new LinkedList<String>();
            idList = Arrays.asList(tradeIds);
            //将多个id存入集合并去重
            Set<String> idSet = new HashSet<String>(idList);
            if (idSet.size() <= 0) {
                return names;
            }
            for (String id : idSet) {
                if (StringUtils.isBlank(id)) {
                    continue;
                }
                Long tradeId = new Long(id.trim());
                trade = tradeDao.selectById(tradeId);
                if (trade != null && trade.getName() != null) {
                    names += trade.getName() + ",";
                    button = true;
                }
            }
            if (button)
                names = names.substring(0, names.lastIndexOf(","));
            return names;
        } else {
            trade = tradeDao.selectById(new Long(ids));
            if (trade != null) {
                names = trade.getName();
            }
            return names;
        }

    }

    /**
     * 删除功能
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteTrade(Long id) {
        Trade trade = tradeDao.selectById(id);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("tradeTableId",id);
        int count = shGameTradeDao.countByMap(map);
        if (count > 0) {
            throw new SystemException(ResponseCodes.ExistGameTrade.getCode(), ResponseCodes.ExistGameTrade.getMessage());
        }
        if (trade != null) {
            tradeRedisDao.delete(trade);
//            jointDelete(trade);
        }
        tradeDao.deleteById(id);
    }

    /**
     * 对表中的trade属性进行级联删除
     *
     * @param trade
     */
    private void jointDelete(Trade trade) {
        //对收货游戏配置表进行删除
        Map map = new HashMap<String, Object>();
        map.put("tradeTypeId", trade.getId());
        //对收货配置表进行级联删除
        List<ShGameConfig> shGameConfigs = shGameConfigDao.selectByLikeTrade(map);
        if (shGameConfigs != null && shGameConfigs.size() != 0) {
            for (int i = 0; i < shGameConfigs.size(); i++) {
                ShGameConfig sc = shGameConfigs.get(i);
                //将原来的trade_type字符串进行更新
                String tradeType = sc.getTradeType();
//                tradeType = tradeType.replaceAll(trade.getName() + "", "");
                if (sc.getTradeType() != null && sc.getTradeType() != "") {
                    String[] trades = tradeType.split(",");
                    StringBuffer sb = new StringBuffer();
                    if (trades.length != 0) {
                        for (String s : trades) {
                            if (!s.equals(trade.getName())) {
                                sb.append(s);
                                sb.append(",");
                            }
                        }
                        tradeType = deleteOtherComma(sb.toString());
                        sc.setTradeType(tradeType);
                    }
                }

                //将原来的trade_type_id进行更新
                String tradeTypeId = sc.getTradeTypeId();
//                tradeTypeId = tradeTypeId.replaceAll(trade.getId() + "", "");
                if (sc.getTradeType() != null && sc.getTradeType() != "") {
                    String[] tradeIds = tradeTypeId.split(",");
                    StringBuffer sbId = new StringBuffer();

                    if (tradeIds.length != 0) {
                        for (String s : tradeIds
                                ) {
                            if (!s.equals(trade.getId() + "")) {
                                sbId.append(s);
                                sbId.append(",");
                            }
                        }
                        tradeTypeId = deleteOtherComma(sbId.toString());
                        sc.setTradeTypeId(tradeTypeId);
                    }
                }

//                    shGameConfigRedisDao.save(sc);
                if (StringUtils.isBlank(sc.getTradeTypeId()) || StringUtils.isBlank(sc.getTradeType())) {
                    shGameConfigDao.deleteById(sc.getId());
                } else {
                    //更新修改时间
                    sc.setUpdateTime(new Date());
                    shGameConfigDao.update(sc);
                }
            }
        }

        //对收货商配置表进行级联删除
        List<PurchaserData> purchaserDatas = purchaserDataDao.selectByLikeTrade(map);

        if (purchaserDatas != null && purchaserDatas.size() != 0) {

            for (int i = 0; i < purchaserDatas.size(); i++) {
                PurchaserData sc = purchaserDatas.get(i);
                //将原来的trade_type字符串进行更新
                String tradeType = sc.getTradeTypeName();
//                tradeType = tradeType.replaceAll(trade.getName() + "", "");
                if (StringUtils.isNotBlank(tradeType)) {
                    String[] trades = tradeType.split(",");
                    StringBuffer sb = new StringBuffer();
                    if (trades.length != 0) {
                        for (String s : trades
                                ) {
                            if (!s.equals(trade.getName())) {
                                sb.append(s);
                                sb.append(",");
                            }
                        }
                        tradeType = deleteOtherComma(sb.toString());
                        sc.setTradeTypeName(tradeType);
                    }
                }
                //将原来的trade_type_id进行更新
                String tradeTypeId = sc.getTradeType();
//                tradeTypeId = tradeTypeId.replaceAll(trade.getId() + "", "");
                if (StringUtils.isNotBlank(tradeTypeId)) {
                    String[] tradeIds = tradeTypeId.split(",");
                    StringBuffer sbId = new StringBuffer();

                    if (tradeIds.length != 0) {
                        for (String s : tradeIds
                                ) {
                            if (!s.equals(trade.getId() + "")) {
                                sbId.append(s);
                                sbId.append(",");
                            }
                        }
                        tradeTypeId = deleteOtherComma(sbId.toString());
                        sc.setTradeType(tradeTypeId);
                    }
                }
                if (StringUtils.isBlank(sc.getTradeTypeName()) || StringUtils.isBlank(sc.getTradeType())) {
                    purchaserDataDao.deleteById(sc.getId());
                } else {
                    purchaserDataDao.update(sc);
                }

//                    shGameConfigRedisDao.save(sc);

            }
        }

        //对收货商游戏配置表进行级联删除
        List<PurchaseGame> purchaseGames = purchaseGameDao.selectByLikeTrade(map);

        if (purchaseGames != null && purchaseGames.size() != 0) {

            for (int i = 0; i < purchaseGames.size(); i++) {

                PurchaseGame sc = purchaseGames.get(i);

                //将原来的trade_type字符串进行更新
                String tradeType = sc.getTradeTypeName();
//                tradeType = tradeType.replaceAll(trade.getName() + "", "");
                if (sc.getTradeTypeName() != null && sc.getTradeTypeName() != "") {
                    String[] trades = tradeType.split(",");
                    StringBuffer sb = new StringBuffer();
                    if (trades.length != 0) {
                        for (String s : trades
                                ) {
                            if (!s.equals(trade.getName())) {
                                sb.append(s);
                                sb.append(",");
                            }
                        }
                        tradeType = deleteOtherComma(sb.toString());
                        sc.setTradeTypeName(tradeType);
                    }
                }

                //将原来的trade_type_id进行更新
                String tradeTypeId = sc.getTradeTypeId();
//                tradeTypeId = tradeTypeId.replaceAll(trade.getId() + "", "");
                if (sc.getTradeTypeId() != null && sc.getTradeTypeId() != "") {
                    String[] tradeIds = tradeTypeId.split(",");
                    StringBuffer sbId = new StringBuffer();

                    if (tradeIds.length != 0) {
                        for (String s : tradeIds
                                ) {
                            if (!s.equals(trade.getId() + "")) {
                                sbId.append(s);
                                sbId.append(",");
                            }
                        }
                        tradeTypeId = deleteOtherComma(sbId.toString());
                        sc.setTradeTypeId(tradeTypeId);
                    }
                }

                if (StringUtils.isBlank(sc.getTradeTypeName()) || StringUtils.isBlank(sc.getTradeTypeId())) {
                    sc.setDeliveryTypeName(ShDeliveryTypeEnum.Pause.getName());
                    sc.setDeliveryTypeId(ShDeliveryTypeEnum.Pause.getCode());
                }

//                    shGameConfigRedisDao.save(sc);
                purchaseGameDao.update(sc);
            }
        }
    }

    /**
     * 修改功能
     *
     * @param trade
     * @return
     */
    @Override
    @Transactional
    public void updateTrade(Trade trade) {
        if (trade != null) {
            Trade oldTrade = tradeDao.selectById(trade.getId());
            if (oldTrade != null) {
                tradeRedisDao.delete(oldTrade);
                trade.setUpdateTime(new Date());
                tradeDao.update(trade);
                Trade newTrade = tradeDao.selectById(trade.getId());
                tradeRedisDao.save(newTrade);
            }
        }
    }

    /**
     * 修改
     * @param id
     * @param tradeLogo
     */
    @Override
    public void update(Long id,Integer tradeLogo) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("tradeLogo",tradeLogo);
        int count = tradeDao.countByMap(map);
        if (count > 0) {
            throw new SystemException(ResponseCodes.TradeLogoExist.getCode(), ResponseCodes.TradeLogoExist.getMessage());
        }
        Trade trade = tradeDao.selectById(id);
        trade.setTradeLogo(tradeLogo);
        if (trade != null) {
            this.updateTrade(trade);
        }
    }

    /**
     * 启用
     *
     * @param id
     */
    @Override
    @Transactional
    public void enabled(Long id) {
        if (id != null) {
            Trade trade = tradeDao.selectById(id);
            if (trade != null) {
                trade.setEnabled(true);
                this.updateTrade(trade);
            }
        }
    }

    /**
     * 禁用
     *
     * @param id
     */
    @Override
    @Transactional
    public void disabled(Long id) {
        if (id != null) {
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("tradeTableId",id);
            int count = shGameTradeDao.countByMap(map);
            if (count > 0) {
                throw new SystemException(ResponseCodes.ExistGameTrade.getCode(), ResponseCodes.ExistGameTrade.getMessage());
            }
            Trade trade = tradeDao.selectById(id);
            if (trade != null) {
                trade.setEnabled(Boolean.FALSE);
                trade.setUpdateTime(new Date());
                this.updateTrade(trade);
//                jointDelete(trade);
                tradeRedisDao.delete(trade);
            }
        }
    }

    /**
     * 分页查询
     *
     * @param map
     * @param pageSize
     * @param start
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<Trade> query(Map<String, Object> map, int pageSize, int start, String orderBy, Boolean isAsc) {

        return tradeDao.selectByMap(map, pageSize, start, orderBy, isAsc);
    }

    /**
     * 去掉多余的，号
     */
    private String deleteOtherComma(String str) {

        if (str.equals(",")) {
            return "";
        }

        //进行字符串去开始的，号
        if (StringUtils.isNotBlank(str)) {
            if (str.indexOf(",") == 0) {
                str = str.substring(1);
//                        System.out.println("切割后：：：："+str);
            }
            //进行字符串去结束的，号
            if (str.lastIndexOf(",") == str.length() - 1) {
                str = str.substring(0, str.length() - 1);
//                System.out.println("切割后：：：：" + str);
            }
            //进行字符串去中间的的，号
            if (str.contains(",,")) {
                int i = str.indexOf(",,");
                str = str.substring(0, i + 1) + str.substring(i + 2);
//            System.out.println("切割后：：：："+str);
            }
            return str;
        }
        return str;
    }

    /**
     * 获取启动的交易方式
     * @param id
     * @return
     */
    @Override
    public String getById(Long id) {
        Trade trade = tradeDao.selectById(id);
        if (trade == null || !trade.getEnabled()) {
            throw new SystemException(ResponseCodes.NoEnableTradeType.getCode());
        }
        return trade.getName();
    }

    @Override
    public List<Trade> queryByMap(Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        return tradeDao.selectByMap(map);
    }

    @Override
    public Trade selectById(Long id) {
        return tradeDao.selectById(id);
    }

    @Override
    public List<Trade> selectTradeByGameGoodsTypeAndShMode(Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        return tradeDao.selectTradeByGameGoodsTypeAndShMode(map);
    }
}
