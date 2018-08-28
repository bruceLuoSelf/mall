package com.wzitech.gamegold.repository.business.impl;

import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.goods.business.IHotRecommendConfigManager;
import com.wzitech.gamegold.goods.entity.HotRecommendConfig;
import com.wzitech.gamegold.repository.business.ICategoryRepositoryManager;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.dao.IRepositoryDBDAO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.shorder.business.IShGameConfigManager;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 安心买栏目2、3库存查询
 *
 * @author yemq
 *         Update History
 *         Date         Name            Reason For Update
 *         ----------------------------------------------
 *         2017/05/12   wrf             ZW_C_JB_00008商城增加通货
 *         2017/06/07   wubiao          通货优化
 */
@Component
public class CategoryRepositoryManagerImpl implements ICategoryRepositoryManager {

    @Autowired
    IRepositoryDBDAO repositoryDBDAO;

    @Autowired
    IRepositoryManager repositoryManager;

    @Autowired
    IHotRecommendConfigManager hotRecommendConfigManager;

    /**
     * ADD '通货优化'
     */
    @Autowired
    IShGameConfigManager shGameConfigManager;

    /**
     * 查询安心买栏目最便宜库存
     *
     * @param queryMap
     * @param biggestUnitPrice 最大单价,大于这个单价的库存不显示,可以为空,为空不作为判断条件
     * @return
     */
    public List<RepositoryInfo> queryCategoryRepository(Map<String, Object> queryMap, BigDecimal biggestUnitPrice) {
        //List<RepositoryInfo> results = new ArrayList<RepositoryInfo>();
        String singleServer = (String) queryMap.get("backServer");
        queryMap.put("isDeleted", false);
        String singleRegion = (String) queryMap.get("backRegion");
        if (StringUtils.isBlank(singleRegion)) {
            singleRegion = (String) queryMap.get("region");
        }
        queryMap = repositoryManager.processRepositoryTransfer2(queryMap);

        String gameName = (String) queryMap.get("gameName");
        if (StringUtils.isBlank(gameName)) {
            gameName = (String) queryMap.get("backGameName");
        }
        // 判断是否只显示寄售库存
        String goodsTypeName = (String) queryMap.get("goodsTypeName");
        if (isOnlyShowConsignmentRepository(gameName, goodsTypeName)) {
            queryMap.put("onShowConsignmentRepository", true);
        }

        /*****新增根据游戏名称和商品类型查询最低购买金额_ADD_'通货优化'_START*****/
        if (StringUtils.isNotBlank(gameName)) { //新增参数判断
            ShGameConfig config = shGameConfigManager.getConfigByGameName(gameName, StringUtils.isBlank(goodsTypeName) ? ServicesContants.GOODS_TYPE_GOLD : goodsTypeName, null, null);
            if (config != null && config.getMinBuyAmount() != null) {
                queryMap.put("smallestAmount", config.getMinBuyAmount());
                queryMap.put("sellableCount", config.getMinCount());
            }
        }

        /*****新增根据游戏名称和商品类型查询最低购买金额_ADD_'通货优化'_END*****/
        queryMap.put("isOnline", true); // 只显示卖家在线的库存
        List<RepositoryInfo> results = new ArrayList<RepositoryInfo>();
        if ("地下城与勇士".equals(gameName) && goodsTypeName.equals(ServicesContants.GOODS_TYPE_GOLD)) {
            //查询跨区跨服信息  作为新参数 地下城与勇士+游戏币跨区
            queryMap.put("backRegion", singleRegion);
            queryMap.put("server", singleServer);
            results = queryMinPriceRepositoryMixedServer(queryMap, 0, 1);
        } else if ("地下城与勇士".equals(gameName) && !goodsTypeName.equals(ServicesContants.GOODS_TYPE_GOLD)) {
            //地下城与勇士  其他商品类型的 不做跨区服  取单独唯一区服
            queryMap.put("servers", "'"+singleServer+"'");
            results = queryGroupRepository(queryMap, 0, 1);
        } else {
            //其他游戏按照旧有逻辑执行
            results = queryGroupRepository(queryMap, 0, 1);
        }

        if (!CollectionUtils.isEmpty(results)) {
            for (int i = 0; i < results.size(); i++) {
                RepositoryInfo repositoryInfo = results.get(i);
                BigDecimal unitPrice = repositoryInfo.getUnitPrice();
                // 库存单价大于最大单价时，抛弃
                if (biggestUnitPrice != null && unitPrice.compareTo(biggestUnitPrice) == 1) {
                    results.remove(i);
                }
            }
        }
        return results;
    }

    /**
     * 是否只显示寄售库存
     *
     * @param gameName
     * @return
     */
    private boolean isOnlyShowConsignmentRepository(String gameName, String goodsTypeName) {
        // 获取该游戏安心买热卖商品配置
        HotRecommendConfig hotRecommendConfig = hotRecommendConfigManager.getConfigFromRedis(gameName, goodsTypeName);
        Boolean isConsignmentMode = hotRecommendConfig.getIsConsignmentMode();
        if (isConsignmentMode != null && isConsignmentMode == true) {
            // 寄售模式的话，根据设置的时间段，只显示寄售的库存
            String startTime = hotRecommendConfig.getConsignmentStartTime();
            String endTime = hotRecommendConfig.getConsignmentEndTime();
            if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                String[] startTimeArr = startTime.split(":");
                String[] endTimeArr = endTime.split(":");

                Calendar calStartTime = Calendar.getInstance();
                calStartTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimeArr[0]));
                calStartTime.set(Calendar.MINUTE, Integer.parseInt(startTimeArr[1]));
                calStartTime.set(Calendar.SECOND, 0);

                Calendar calEndTime = Calendar.getInstance();
                calEndTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTimeArr[0]));
                calEndTime.set(Calendar.MINUTE, Integer.parseInt(endTimeArr[1]));
                calEndTime.set(Calendar.SECOND, 0);


                // 这个时间段只显示寄售库存，担保库存不显示
                long nowTimeInMillis = Calendar.getInstance().getTimeInMillis();
                if (nowTimeInMillis >= calStartTime.getTimeInMillis()) {
                    if (nowTimeInMillis <= calEndTime.getTimeInMillis()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * 对卖家库存单价进行分组，按单价由低到高，库存由多到少排序
     */
    @Override
    public List<RepositoryInfo> queryGroupRepository(Map<String, Object> queryParam, int start, int pageSize) {
        return repositoryDBDAO.queryGroupRepository(queryParam, start, pageSize);
    }

    @Override
    public List<RepositoryInfo> queryMinPriceRepositoryMixedServer(Map<String, Object> queryMap, int start, int pageSize) {
        return repositoryDBDAO.queryMinUnitPriceRepository(queryMap, start, pageSize);
    }
}
