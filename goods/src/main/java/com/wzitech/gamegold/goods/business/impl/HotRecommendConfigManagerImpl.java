package com.wzitech.gamegold.goods.business.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.goods.business.IHotRecommendConfigManager;
import com.wzitech.gamegold.goods.dao.IHotRecommendConfigDBDAO;
import com.wzitech.gamegold.goods.dao.IHotRecommendConfigRedisDAO;
import com.wzitech.gamegold.goods.entity.HotRecommendConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 前台安心买热卖商品配置管理
 * <p>
 * Update History
 * Date         Name            Reason For Update
 * ----------------------------------------------
 * 2017/5/12    wrf              ZW_C_JB_00008商城增加通货
 *
 * @author yemq
 */
@Component
public class HotRecommendConfigManagerImpl implements IHotRecommendConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(HotRecommendConfigManagerImpl.class);

    @Autowired
    private IHotRecommendConfigDBDAO dao;
    @Autowired
    private IHotRecommendConfigRedisDAO redisDao;

    /**
     * 添加热卖商品配置记录
     *
     * @param config
     * @return 主键ID
     */
    @Override
    @Transactional
    public HotRecommendConfig add(HotRecommendConfig config) {
        if (config == null) {
            throw new SystemException(ResponseCodes.IllegalArguments.getCode(), ResponseCodes.IllegalArguments.getMessage());
        }
//        if (config.getGameId() == null) {
//            throw new SystemException(ResponseCodes.EmptyGameId.getCode(), ResponseCodes.EmptyGameId.getMessage());
//        }
        if (StringUtils.isBlank(config.getGameName())) {
            throw new SystemException(ResponseCodes.EmptyGameName.getCode(), ResponseCodes.EmptyGameName.getMessage());
        }
        /*if (StringUtils.isBlank(config.getCategoryIcon1())) {
            throw new SystemException(ResponseCodes.EmptyCategoryIcon1.getCode(), ResponseCodes.EmptyCategoryIcon1.getMessage());
        }*/

        // 查询是否存在多条配置
        Map<String, Object> queryParam = Maps.newHashMap();
        queryParam.put("gameName", config.getGameName());
        queryParam.put("goodsTypeName",config.getGoodsTypeName());
        int count = dao.countByMap(queryParam);
        if (count > 0) {
            throw new SystemException(ResponseCodes.MultiHotRecommendConfig.getCode(), ResponseCodes.MultiHotRecommendConfig.getMessage());
        }

        // 保存到数据库
        Date now = new Date();
        config.setGoldCounts(checkAndSortGoldCounts(config.getGoldCounts()));
        config.setCategoryIcon1(checkAndSortCategoryIcon(config.getCategoryIcon1()));
        config.setCategoryIcon2(checkAndSortCategoryIcon(config.getCategoryIcon2()));
        config.setCategoryIcon3(checkAndSortCategoryIcon(config.getCategoryIcon3()));
        config.setCreateTime(now);
        config.setLastUpdateTime(now);
        config.setOperatorId(CurrentUserContext.getUid());
        Long id = (Long) dao.insert(config);
        config.setId(id);

        // 保存到redis
        redisDao.saveUpdate(config);
        return config;
    }

    /**
     * 更新配置
     *
     * @param config
     * @return
     */
    @Override
    @Transactional
    public HotRecommendConfig update(HotRecommendConfig config) {
        if (config == null) {
            throw new SystemException(ResponseCodes.IllegalArguments.getCode(), ResponseCodes.IllegalArguments.getMessage());
        }
        if (config.getId() == null) {
            throw new SystemException(ResponseCodes.EmptyId.getCode(), ResponseCodes.EmptyId.getMessage());
        }
        /*if (StringUtils.isBlank(config.getCategoryIcon1())) {
            throw new SystemException(ResponseCodes.EmptyCategoryIcon1.getCode(), ResponseCodes.EmptyCategoryIcon1.getMessage());
        }*/

        config.setGoldCounts(checkAndSortGoldCounts(config.getGoldCounts()));
        config.setCategoryIcon1(checkAndSortCategoryIcon(config.getCategoryIcon1()));
        config.setCategoryIcon2(checkAndSortCategoryIcon(config.getCategoryIcon2()));
        config.setCategoryIcon3(checkAndSortCategoryIcon(config.getCategoryIcon3()));
        config.setLastUpdateTime(new Date());
        config.setOperatorId(CurrentUserContext.getUid());
        dao.update(config);

        // 更新到redis
        redisDao.saveUpdate(config);

        // 写入操作日志
        writeLog(config);
        return config;
    }

    /**
     * 根据ID批量删除
     *
     * @param ids
     */
    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new SystemException(ResponseCodes.EmptyId.getCode(), ResponseCodes.EmptyId.getMessage());
        }

        // 获取游戏名称
        List<HotRecommendConfig> gameNameList = Lists.newArrayList();
        for (Long id : ids) {
            HotRecommendConfig config = dao.selectById(id);
            gameNameList.add(config);
        }

        // 批量删除
        dao.batchDeleteByIds(ids);

        // 从redis中删除
        for (HotRecommendConfig hotRecommendConfig : gameNameList) {
            redisDao.remove(hotRecommendConfig.getGameName(),hotRecommendConfig.getGoodsTypeName());
        }
    }

    /**
     * 分页查询
     *
     * @param queryMap
     * @param limit
     * @param start
     * @param sortBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<HotRecommendConfig> queryConfigList(Map<String, Object> queryMap, int limit, int start, String sortBy, boolean isAsc) {
        return dao.selectByMap(queryMap, limit, start, sortBy, isAsc);
    }

    /**
     * 获取配置
     *
     * @param gameName
     * @return
     */
    /**
     * ZW_C_JB_00008_20170512 start  mod
     **/
    public HotRecommendConfig getConfigFromRedis(String gameName, String goodsTypeName) {
        if(StringUtils.isBlank(goodsTypeName)){
            goodsTypeName = ServicesContants.GOODS_TYPE_GOLD;
        }
        HotRecommendConfig hotRecommendConfig = redisDao.getHotRecommendConfig(gameName, goodsTypeName);
        if (null == hotRecommendConfig) {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("gameName", gameName);
            queryMap.put("goodsTypeName",goodsTypeName);
            hotRecommendConfig = dao.selectByProp(queryMap);
            if (null == hotRecommendConfig) {
                throw new SystemException(ResponseCodes.IllegalArguments.getCode(),
                        ResponseCodes.IllegalArguments.getMessage());
            }
            redisDao.saveUpdate(hotRecommendConfig);
        }
        return hotRecommendConfig;
    }
    /**ZW_C_JB_00008_20170512 end **/
    /**
     * 对goldCounts检查并进行由小到大排序
     *
     * @param goldCounts
     * @return
     */
    private String checkAndSortGoldCounts(String goldCounts) {
        if (StringUtils.isBlank(goldCounts)) {
            throw new SystemException(ResponseCodes.EmptyGoldCounts.getCode(), ResponseCodes.EmptyGoldCounts.getMessage());
        }
        goldCounts = StringUtils.trim(goldCounts);
        String[] goldCountArr = goldCounts.split(",");
        List<Long> newGoldCounts = Lists.newArrayList();
        for (String goldCount : goldCountArr) {
            try {
                newGoldCounts.add(Long.valueOf(StringUtils.trim(goldCount)));
            } catch (NumberFormatException ex) {
                throw new SystemException(ResponseCodes.IllegalGoldCounts.getCode(), ResponseCodes.IllegalGoldCounts.getMessage());
            }
        }
        Collections.sort(newGoldCounts);
        goldCounts = "";
        for (int i = 0, j = newGoldCounts.size(); i < j; i++) {
            goldCounts += newGoldCounts.get(i);
            if (i != (j - 1)) {
                goldCounts += ",";
            }
        }
        return goldCounts;
    }

    /**
     * 对产品栏目图标检查并排序
     *
     * @param categoryIcon
     * @return
     */
    private String checkAndSortCategoryIcon(String categoryIcon) {
        if (StringUtils.isBlank(categoryIcon))
            return "";

        categoryIcon = StringUtils.trim(categoryIcon);
        String[] categoryIconArr = categoryIcon.split(",");
        List<Long> icons = Lists.newArrayList();
        for (String icon : categoryIconArr) {
            try {
                icons.add(Long.valueOf(StringUtils.trim(icon)));
            } catch (NumberFormatException ex) {
                throw new SystemException(ResponseCodes.IllegalCategoryIcon.getCode(), ResponseCodes.IllegalCategoryIcon.getMessage());
            }
        }
        Collections.sort(icons);
        categoryIcon = "";
        for (int i = 0, j = icons.size(); i < j; i++) {
            categoryIcon += icons.get(i);
            if (i != (j - 1)) {
                categoryIcon += ",";
            }
        }
        return categoryIcon;
    }

    /**
     * 将操作日志写入文件
     *
     * @param config
     */
    private void writeLog(HotRecommendConfig config) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        sb.append("时间：").append(sdf.format(new Date()));
        sb.append("\t游戏：").append(config.getGameName());
        sb.append("\t最低价：");
        if (config.isShowCategory23()) {
            sb.append("开启");
        } else {
            sb.append("关闭");
        }
        sb.append("\t操作员：").append(CurrentUserContext.getUserLoginAccount());
        sb.append("\r\n");

        try {
            String path = "/srv/jboss-as-7.1.1.Final/log";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileWriter fw = new FileWriter(file + "/zuidijia.log", true);
            fw.write(sb.toString());
            fw.close();
        } catch (Exception e) {
            logger.error("安心买日志写入失败", e);
        }
    }
}
