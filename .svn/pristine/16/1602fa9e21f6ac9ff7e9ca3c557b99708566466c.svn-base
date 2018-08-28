package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.shorder.business.IShGameConfigManager;
import com.wzitech.gamegold.shorder.dao.IPurchaserDataDao;
import com.wzitech.gamegold.shorder.dao.IShGameConfigDao;
import com.wzitech.gamegold.shorder.dao.IShGameConfigRedisDao;
import com.wzitech.gamegold.shorder.entity.PurchaserData;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/4.
 *
 *  Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/12  lvshuyan           ZW_C_JB_00008 商城增加通货
 */
@Component
public class ShGameConfigManagerImpl extends AbstractBusinessObject implements IShGameConfigManager{
    @Autowired
    private IShGameConfigDao shGameConfigDao;
    @Autowired
    private IShGameConfigRedisDao shGameConfigRedisDao;
    @Autowired
    private IPurchaserDataDao purchaserDataDao;

    @Override
    public GenericPage<ShGameConfig> queryByMap(Map<String, Object> map, int start, int pageSize, String orderBy, boolean isAsc) {
        if(null == map){
            map = new HashMap<String, Object>();

        }
        //检查分页参数
        if (pageSize < 1) {
            throw new IllegalArgumentException("分页pageSize参数必须大于1");
        }

        if (start < 0) {
            throw new IllegalArgumentException("分页startIndex参数必须大于0");
        }

        return shGameConfigDao.selectByMap(map,pageSize,start,orderBy,isAsc);
    }

    @Override
    public List<ShGameConfig> queryByMap(Map<String, Object> map, String orderBy, boolean isAsc) {
        if(null == map){
            map = new HashMap<String, Object>();
        }
        return shGameConfigDao.selectByMap(map,orderBy,isAsc);
    }

    /**
     * 修改配置
     * @param shGameConfig
     * @return
     */
    @Override
    @Transactional
    public int updateShGameConfig(ShGameConfig shGameConfig) {
        if (shGameConfig != null) {
            ShGameConfig shGameConfig1 = shGameConfigDao.selectById(shGameConfig.getId());
            if (shGameConfig1 != null) {
                shGameConfigRedisDao.delete(shGameConfig1);
            }
        }
        shGameConfig.setUpdateTime(new Date());
        int result = shGameConfigDao.update(shGameConfig);
        shGameConfigRedisDao.save(shGameConfig);
        return result;
    }

    /**
     * 删除配置
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int deleteGameConfig(Long id) {
        ShGameConfig shGameConfig = shGameConfigDao.selectById(id);
        int result = shGameConfigDao.deleteById(id);
        shGameConfigRedisDao.delete(shGameConfig);
        return result;
    }

    /**
     * 增加配置
     * @param shGameConfig
     */
    @Override
    @Transactional
    public void addShGameConfig(ShGameConfig shGameConfig) {
        ShGameConfig config = this.selectShGameConfig(shGameConfig);
        if(config != null){
            throw new SystemException(ResponseCodes.MultiHotRecommendConfig.getCode(), ResponseCodes.MultiHotRecommendConfig.getMessage());
        }

        shGameConfig.setCreateTime(new Date());
        shGameConfig.setUpdateTime(new Date());
         shGameConfigDao.insert(shGameConfig);
         shGameConfigRedisDao.save(shGameConfig);

    }

    /**
     * 通过游戏名称，商品名称获取收货游戏配置
     * ZW_C_JB_00008_20170512 增加通货类型 update by lsy
     * @param gameName
     * @return
     */
    @Override
    public ShGameConfig getConfigByGameName(String gameName, String goodsTypeName, Boolean isEnabled, Boolean enableMall) {
            ShGameConfig shGameConfig= shGameConfigRedisDao.getByConfigKey(gameName, goodsTypeName);
        if(shGameConfig==null ){
            Map queryMap = new HashMap();
            queryMap.put("gameName", gameName);
            queryMap.put("goodsTypeName", goodsTypeName); //ZW_C_JB_00008_20170512 ADD
            List<ShGameConfig> shGameConfigList1 = shGameConfigDao.selectByMap(queryMap);
            if(shGameConfigList1!=null&&!shGameConfigList1.isEmpty()){
                shGameConfig=shGameConfigList1.get(0);
                shGameConfigRedisDao.save(shGameConfig);
            }
        }
        if(shGameConfig!=null){
            shGameConfig.setPoundage(shGameConfig.getPoundage() == null ? new BigDecimal(0.03) : shGameConfig.getPoundage());
            if(isEnabled!=null && shGameConfig.getIsEnabled()!=isEnabled){
                   return null;
               }
               if(enableMall!=null && shGameConfig.getEnableMall()!=enableMall){
                   return null;
               }
           }
            return shGameConfig;
    }

    /**
     * 通过游戏名称，查询所有启用商城的商品类型
     * 增加通货类型 update by lsy
     * @param gameName
     * @return
     */
    @Override
    public List<ShGameConfig> getConfigByGameNameAndSwitch(String gameName, Boolean isEnabled, Boolean enableMall) {
        if(StringUtils.isBlank(gameName)){
            throw new IllegalArgumentException("游戏名称不能为空");
        }
        List<ShGameConfig> shGameConfigList = shGameConfigRedisDao.selectByNameAndSwitch(gameName, isEnabled, enableMall);
        if(CollectionUtils.isEmpty(shGameConfigList)){
            Map queryMap = new HashMap();
            queryMap.put("gameName", gameName);
            queryMap.put("isEnabled", isEnabled);
            queryMap.put("enableMall", enableMall);
            shGameConfigList = shGameConfigDao.selectByMap(queryMap);
            if(CollectionUtils.isNotEmpty(shGameConfigList)){
                for(ShGameConfig config : shGameConfigList){
                    shGameConfigRedisDao.save(config);
                }
            }
        }
        return shGameConfigList;
    }

    /**
     * 禁用
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int disableUser(Long id) {
        if (id != null) {
            ShGameConfig shGameConfig = shGameConfigDao.selectById(id);
            if (shGameConfig != null) {
                shGameConfig.setIsEnabled(false);
                shGameConfig.setUpdateTime(new Date());
                shGameConfigDao.update(shGameConfig);
                shGameConfigRedisDao.save(shGameConfig);
            }
        }
        return 0;
    }

    /**
     * 启用
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int qyUser(Long id) {
        if (id != null) {
            ShGameConfig shGameConfig = shGameConfigDao.selectById(id);
            if (shGameConfig != null) {
                shGameConfig.setIsEnabled(true);
                shGameConfig.setUpdateTime(new Date());
                shGameConfigDao.update(shGameConfig);
                shGameConfigRedisDao.save(shGameConfig);

            }
        }
        return 0;
    }

    /**
     *禁用商城
     * 增加商品类型 add by lcs
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int disableMall(Long id) {
        if (id != null) {
            ShGameConfig shGameConfig = shGameConfigDao.selectById(id);
            if (shGameConfig != null) {
                shGameConfig.setEnableMall(false);
                shGameConfig.setUpdateTime(new Date());
                shGameConfigDao.update(shGameConfig);
                shGameConfigRedisDao.save(shGameConfig);
            }
        }
        return 0;
    }

    /**
     * 禁用九宫格数据
     */
    @Override
    @Transactional
    public int disableNineBlock(Long id) {
        if (id != null) {
            ShGameConfig shGameConfig = shGameConfigDao.selectById(id);
            if (shGameConfig != null) {
                shGameConfig.setNineBlockEnable(false);
                shGameConfig.setUpdateTime(new Date());
                shGameConfigDao.update(shGameConfig);
                shGameConfigRedisDao.save(shGameConfig);
            }
        }
        return 0;
    }

    /**
     * 开启九宫格数据
     */
    @Override
    @Transactional
    public int enableNineBlock(Long id) {
        if (id != null) {
            ShGameConfig shGameConfig = shGameConfigDao.selectById(id);
            if (shGameConfig != null) {
                shGameConfig.setNineBlockEnable(true);
                shGameConfig.setUpdateTime(new Date());
                shGameConfigDao.update(shGameConfig);
                shGameConfigRedisDao.save(shGameConfig);
            }
        }
        return 0;
    }

    /**
     * 开启分仓配置
     */
    @Override
    @Transactional
    public int isSplitWarehouse(Long id) {
        if (id != null) {
            ShGameConfig shGameConfig = shGameConfigDao.selectById(id);
            if (shGameConfig != null) {
                shGameConfig.setIsSplit(true);
                shGameConfig.setUpdateTime(new Date());
                shGameConfigDao.update(shGameConfig);
                shGameConfigRedisDao.save(shGameConfig);
            }
        }
        return 0;
    }

    /**
     * 禁用九宫格数据
     */
    @Override
    @Transactional
    public int disableWarehouse(Long id) {
        if (id != null) {
            ShGameConfig shGameConfig = shGameConfigDao.selectById(id);
            if (shGameConfig != null) {
                shGameConfig.setIsSplit(false);
                shGameConfig.setUpdateTime(new Date());
                shGameConfigDao.update(shGameConfig);
                shGameConfigRedisDao.save(shGameConfig);
            }
        }
        return 0;
    }
    /**
     * 增加商品类型 add by lcs
     *启用商城
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int enableMall(Long id) {
        if (id != null) {
            ShGameConfig shGameConfig = shGameConfigDao.selectById(id);
            if (shGameConfig != null) {
                shGameConfig.setEnableMall(true);
                shGameConfig.setUpdateTime(new Date());
                shGameConfigDao.update(shGameConfig);
                shGameConfigRedisDao.save(shGameConfig);
            }
        }
        return 0;
    }

    /**
     * 查询所有启用收货的配置
     * @return
     */
    @Override
    public List<ShGameConfig> selectEnableConfig() {
        Map<String,Object> map=new HashMap<String, Object>();
            map.put("isEnabled",true);
        List<ShGameConfig> list=shGameConfigDao.selectByMap(map);
        if(null==list){
            throw new SystemException(ResponseCodes.NotAvailableConfig.getCode(),
                    ResponseCodes.NotAvailableConfig.getMessage());
        }
        return list;
    }


    /**
     * 同步所有卖家的交易模式
     *
     * @return
     */
    private void synAllPurchaseList(Long tradeType) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("purchaseOrderOnline",true);
        int totalCount = purchaserDataDao.countByMap(queryMap);
        int pageSize = 100;//每次读取100笔
        int totalPage = 0;
        if (totalCount % pageSize == 0) {
            totalPage = totalCount / pageSize;
        } else {
            totalPage = totalCount / pageSize + 1;
        }

        //递归获取所有需要分仓的帐号
        synAllPurchaseListByPage(0, pageSize, totalPage, queryMap, "id", true, tradeType);
    }

    /**
     * 递归获取采购商数据，并更新交易模式
     *
     * @param index
     * @param pageSize
     * @param totalPage
     * @param queryMap
     * @param orderFiled
     * @param isAsc
     * @param tradeType
     */
    private void synAllPurchaseListByPage(int index, int pageSize, int totalPage, Map<String, Object> queryMap, String orderFiled, Boolean isAsc, Long tradeType) {
        if (index < totalPage) {
            GenericPage<PurchaserData> genericPage = purchaserDataDao.selectByMap(queryMap, pageSize, index * pageSize, orderFiled, isAsc);
            List<PurchaserData> pageData = genericPage.getData();
            if (pageData != null && pageData.size() > 0) {
                for (PurchaserData purchaserData : pageData) {
                    String tradeTypeData = purchaserData.getTradeType();
                    String tradeTypeNameData = purchaserData.getTradeTypeName();
                    if (StringUtils.isNotBlank(tradeTypeData) && StringUtils.isNotBlank(tradeTypeNameData) && tradeTypeNameData.indexOf(",") > 0 && tradeTypeData.indexOf(",") > 0) {
                        String[] tradeTypeArr = tradeTypeData.split(",");
                        String[] tradeTypeNameArr = tradeTypeNameData.split(",");
                        StringBuilder resultType = new StringBuilder();
                        StringBuilder resultTypeName = new StringBuilder();
                        for (int i = 0; i < tradeTypeArr.length; i++) {
                            String id = tradeTypeArr[i];
                            String name = tradeTypeNameArr[i];
                            if (!id.equals(tradeType.toString())) {
                                resultType.append(id + ",");
                                resultTypeName.append(name + ",");
                            }
                        }

                        if (StringUtils.isNotBlank(resultType)) {
                            purchaserData.setTradeType(resultType.substring(0, resultType.length() - 1));
                            purchaserData.setTradeTypeName(resultTypeName.substring(0, resultTypeName.length() - 1));
                            purchaserDataDao.update(purchaserData);
                        }
                    }
                }
            }
            index++;
            synAllPurchaseListByPage(index, pageSize, totalPage, queryMap, orderFiled, isAsc, tradeType);
        }
    }

   public ShGameConfig selectShGameConfig(ShGameConfig shGameConfig){
        return shGameConfigDao.selectShGame(shGameConfig);
    }

    @Override
    public ShGameConfig selectById(Long id) {
        return shGameConfigDao.selectById(id);
    }

    @Override
    public List<ShGameConfig> selectGoodsTypeByGameAndShMode(Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        return shGameConfigDao.selectGoodsTypeByGameAndShMode(map);
    }

    /**
     * 根据游戏名称和商品类型id获取手续费
     * @param gameName
     * @param goodsTypeId
     * @return
     */
    @Override
    public BigDecimal getPoundage(String gameName, String goodsTypeId) {
        if (StringUtils.isBlank(gameName)) {
            throw new SystemException(ResponseCodes.EmptyGameName.getCode(), ResponseCodes.EmptyGameName.getMessage());
        }
        if (StringUtils.isBlank(goodsTypeId)) {
            throw new SystemException(ResponseCodes.NullGoodsTypeId.getCode(), ResponseCodes.NullGoodsTypeId.getMessage());
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("gameName",gameName);
        map.put("goodsTypeId",goodsTypeId);
        return shGameConfigDao.getPoundage(map);
    }
}
