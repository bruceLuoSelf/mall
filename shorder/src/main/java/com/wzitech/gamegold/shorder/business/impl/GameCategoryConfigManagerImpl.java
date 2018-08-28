package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.shorder.business.IGameCategoryConfigManager;
import com.wzitech.gamegold.shorder.dao.IGameCategoryConfigDao;
import com.wzitech.gamegold.shorder.dao.IPurchaseGameDao;
import com.wzitech.gamegold.shorder.dao.IShGameConfigDao;
import com.wzitech.gamegold.shorder.entity.GameCategoryConfig;
import com.wzitech.gamegold.shorder.entity.PurchaseGame;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏类目配置
 * 孙杨 2017/2/14
 */
@Component
@Transactional
public class GameCategoryConfigManagerImpl implements IGameCategoryConfigManager {

    @Autowired
    IGameCategoryConfigDao gameCategoryConfigDao;

    @Autowired
    private IShGameConfigDao shGameConfigDao;
    @Autowired
    private IPurchaseGameDao purchaseGameDao;

    /**
     * 分页查询
     * @param paramMap
     * @param limit
     * @param startIndex
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<GameCategoryConfig> queryPage(Map<String, Object> paramMap, int limit, int startIndex, String orderBy, Boolean isAsc) {
        return gameCategoryConfigDao.selectByMap(paramMap, limit, startIndex, orderBy, isAsc);
    }

    /**
     * 不分页查询
     * @param paramMap
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public List<GameCategoryConfig> queryPage(Map<String, Object> paramMap, String orderBy, Boolean isAsc) {
        return gameCategoryConfigDao.selectByMap(paramMap,  orderBy, isAsc);
    }

    /**
     * 删除系统配置
     */
    public void deleteConfigById(Long id) {
        GameCategoryConfig gameCategoryConfig = gameCategoryConfigDao.selectById(id);
        if (gameCategoryConfig != null) {
            gameCategoryConfigDao.deleteById(id);
            jointDeleteGoodsType(gameCategoryConfig);

        }
    }

    private void jointDeleteGoodsType(GameCategoryConfig gameCategoryConfig) {
        //对收货游戏配置表进行删除
        Map map = new HashMap<String, Object>();
        map.put("goodsTypeId", gameCategoryConfig.getId()+"");

        //对收货配置表进行级联删除
        List<ShGameConfig> shGameConfigs = shGameConfigDao.selectByMap(map);

        if (shGameConfigs != null && shGameConfigs.size() != 0) {

            for (int i = 0; i < shGameConfigs.size(); i++) {

                ShGameConfig sc = shGameConfigs.get(i);

//                //将原来的goodType字符串进行更新
//                sc.setGoodsTypeName("");
//
//                //将原来的setGoodsTypeId进行更新
//                sc.setGoodsTypeId(0);
//
//                //更新修改时间
//                sc.setUpdateTime(new Date());
                shGameConfigDao.deleteById(sc.getId());
            }

        }

        map.put("goodsTypeId", gameCategoryConfig.getId());
        //对收货商游戏配置表进行级联删除
        List<PurchaseGame> purchaseGames = purchaseGameDao.selectByMap(map);

        if (purchaseGames != null && purchaseGames.size() != 0) {

            for (int i = 0; i < purchaseGames.size(); i++) {

                PurchaseGame sc = purchaseGames.get(i);

//                //将原来的GoodsTypeid字符串进行更新
//                sc.setGoodsTypeId(0);
//
//                //将原来的trade_type进行更新
//                sc.setGoodsTypeName("");
//
//                sc.setDeliveryTypeName("暂停收货");
//                sc.setDeliveryTypeId(3 + "");
                purchaseGameDao.deleteById(sc.getId());
            }
        }
    }

    /**
     * 增加系统配置
     */
    @Override
    public void addConfig(GameCategoryConfig gameCategoryConfig) {
        HashMap<String,Object
                > queryMap=new HashMap<String, Object>();
        queryMap.put("name",gameCategoryConfig.getName());
        int count=gameCategoryConfigDao.countByMap(queryMap);
        if(count>0){
            throw new SystemException(ResponseCodes.GameCategoryConfig.getCode(),ResponseCodes.GameCategoryConfig.getMessage());
        }
        if (gameCategoryConfig != null) {
            gameCategoryConfig.setIsEnabled(true);
            gameCategoryConfig.setCreateTime(new Date());
            gameCategoryConfig.setUpdateTime(new Date());
            gameCategoryConfigDao.insert(gameCategoryConfig);
        }
    }

    /**
     * 修改系统配置
     */
    @Override
    public void updateConfig(GameCategoryConfig gameCategoryConfig) {
        if (gameCategoryConfig != null) {
            gameCategoryConfig.setUpdateTime(new Date());
            gameCategoryConfigDao.update(gameCategoryConfig);
        }
    }

    /**
     * 关闭配置
     *
     * @param id
     */
    @Override
    public void disableConfig(Long id) {
        if (id != null) {
            GameCategoryConfig gameCategoryConfig = gameCategoryConfigDao.selectById(id);
            if (gameCategoryConfig != null) {
                gameCategoryConfig.setIsEnabled(false);
                //对GoodsType字段进行更新删除
                jointDeleteGoodsType(gameCategoryConfig);
                gameCategoryConfigDao.update(gameCategoryConfig);
            }
        }
    }

    /**
     * 启用配置
     *
     * @param id
     */
    public void qyConfig(Long id) {
        if (id != null) {
            GameCategoryConfig gameCategoryConfig = gameCategoryConfigDao.selectById(id);
            if (gameCategoryConfig != null) {
                gameCategoryConfig.setIsEnabled(true);
                gameCategoryConfigDao.update(gameCategoryConfig);
            }
        }
    }

    /**
     *
     去掉多余的，号
     */
    private String deleteOtherComma(String str) {
        //进行字符串去开始的，号
        if (StringUtils.isNotBlank(str)) {
            if (str.indexOf(",") == 0) {
                str = str.substring(1);
//                        System.out.println("切割后：：：："+str);
            }
            //进行字符串去结束的，号
            if (str.lastIndexOf(",") == str.length() - 1) {
                str = str.substring(0, str.length() - 1);
                System.out.println("切割后：：：：" + str);
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
}
