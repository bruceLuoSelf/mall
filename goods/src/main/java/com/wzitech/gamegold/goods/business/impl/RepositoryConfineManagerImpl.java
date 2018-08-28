package com.wzitech.gamegold.goods.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.goods.business.IRepositoryConfineManager;
import com.wzitech.gamegold.goods.dao.*;
import com.wzitech.gamegold.goods.entity.ReferencePrice;
import com.wzitech.gamegold.goods.entity.RepositoryConfine;
import com.wzitech.gamegold.goods.entity.RepositoryConfineInfo;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.dao.IRepositoryDBDAO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by jhlcitadmin on 2017/3/20.
 * Update History
 * Date         Name            Reason For Update
 * ----------------------------------------------
 * 2017/5/16    wrf              ZW_C_JB_00008商城增加通货
 *
 */
@Component
public class RepositoryConfineManagerImpl extends AbstractBusinessObject implements IRepositoryConfineManager {

    @Autowired
    private IRepositoryConfigRedisDAO repositoryConfigRedisDAO;

    @Autowired
    private IRepositoryConfineDao repositoryConfigDao;

    @Autowired
    private IRepositoryDBDAO repositoryDBDAO;

    @Autowired
    private IReferencePriceRedisDao referencePriceRedisDao;

    @Autowired
    private IReferencePriceDao referencePriceDao;


    @Autowired
    private IRepositoryConfineInfoRedisDao repositoryConfineInfoRedisDao;

    @Autowired
    private IGameRepositoryDao gameRepositoryDao;

    @Autowired
    private IRepositoryManager repositoryManager;


    /**
     * 计算最小单价的算法
     */
    @Override
    public ReferencePrice selectByMinUnitPrice(RepositoryInfo info) {
        if (StringUtils.isBlank(info.getGameName()) || StringUtils.isBlank(info.getRegion()) || StringUtils.isBlank(info.getServer())) {
            throw new SystemException(ResponseCodes.NullGameNameAndRegionAndServer.getCode(),
                    ResponseCodes.NullGameNameAndRegionAndServer.getMessage());
        }
        ReferencePrice referencePrice = new ReferencePrice();
        //对参数进行查询
        referencePrice.setGameName(info.getGameName());
        referencePrice.setRegionName(info.getRegion());
        referencePrice.setServerName(info.getServer());
        if (info.getGameRace() != null)
            referencePrice.setRaceName(info.getGameRace());
        referencePrice.setMoneyName(info.getMoneyName());
        referencePrice.setGoodsTypeName(info.getGoodsTypeName());
        //查询信息的保存
        HashMap<String, Object> str = new HashMap<String, Object>();
        str.put("goodsTypeName",info.getGoodsTypeName());
        List<RepositoryInfo> repositoryInfos = new ArrayList<RepositoryInfo>();
        //根据游戏在游戏详情库存限制配置表中查询是否有对应的库存配置
        RepositoryConfineInfo confineInfo = repositoryConfineInfoRedisDao.selectRepositoryConfineInfo(info.getGameName(), info.getRegion(), info.getServer(), info.getGameRace() == null ? "" : info.getGameRace(),info.getGoodsTypeName());
        //在redis没有查询到的情况下载数据库查询
        if (confineInfo == null) {
            Map<String, Object> selectParamMap = new HashMap<String, Object>();
            selectParamMap.put("gameName", info.getGameName());
            selectParamMap.put("serverName", info.getServer());
            selectParamMap.put("regionName", info.getRegion());
            selectParamMap.put("raceName", info.getGameRace());
            selectParamMap.put("goodsTypeName",info.getGoodsTypeName());
            List<RepositoryConfineInfo> repositoryConfineInfos = gameRepositoryDao.selectByMap(selectParamMap);
//            List<RepositoryConfine> gameNameRC = repositoryConfigDao.selectByProp("gameName", info.getGameName());
            //判断该配置有没有启用
            if (repositoryConfineInfos == null || repositoryConfineInfos.size() == 0 || repositoryConfineInfos.get(0).getEnabled() == false) {
                confineInfo = null;
            } else {
                confineInfo = repositoryConfineInfos.get(0);
                if (org.apache.commons.lang.StringUtils.isBlank(confineInfo.getRaceName())) {
                    confineInfo.setRaceName("");
                }
                repositoryConfineInfoRedisDao.addRepositoryConfineInfo(confineInfo);
            }
        }
        //如果还没有查到限制配置设置配置为通用配置
        if (confineInfo == null || confineInfo.getRepositoryCount() == null || confineInfo.getEnabled() == false) {
            //在没取到的情况下加载通用配置信息
            RepositoryConfine repositoryConfine = repositoryConfigRedisDAO.selectRepositoryByGameName(info.getGameName(),info.getGoodsTypeName());
            if (repositoryConfine == null) {
                repositoryConfine = repositoryConfigDao.selectRepositoryByMap(info.getGameName(),info.getGoodsTypeName());
            }
            if (repositoryConfine != null && repositoryConfine.getIsEnabled() == true) {
                str.put("sellableCount", repositoryConfine.getRepositoryCount());
                repositoryConfigRedisDAO.saveRepositoryConfig(repositoryConfine);
            } else {
                str.put("sellableCount", 0);
            }
        } else {
            str.put("sellableCount", confineInfo.getRepositoryCount());
            //在查询到的情况下如果是没有启用
//            if (!confineInfo.getIsEnabled()) {
//                //异常抛出的处理 说明库存限制配置没有打通
//                throw new SystemException(ResponseCodes.NotMatchConfig.getCode(),ResponseCodes.NotMatchConfig.getMessage());
//            }
        }
        if (info.getGameName().equals("地下城与勇士")) {
            if (StringUtils.isNotBlank(info.getGameRace())) {
                throw new SystemException(ResponseCodes.NotAvaliableRegionAndServer.getCode(),
                        ResponseCodes.NotAvaliableRegionAndServer.getMessage());
            }
            str.put("gameName", "地下城与勇士");
            str.put("limit", 2);
            str.put("region", info.getRegion());
            /**ZW_C_JB_00008_2017/05/16 wrf add **/  //固定是游戏币 旧版打通  只是改了mapper文件
            str = getRepositoryInfoByServers(info, str);
            //只有地下城有数量判断
            if(info.getGoodsTypeName().equals(ServicesContants.GOODS_TYPE_GOLD)){
                //游戏币跨区跨服查询 singleServer  moreServer
                str.put("singleServer",info.getServer());
                //取到多区服配置
                str.put("moreServer",str.get("server"));
                repositoryInfos = repositoryDBDAO.selectMINunitPriceByOtherGameForCross(str);
            }else {
                str.put("server","'"+info.getServer()+"'");
                repositoryInfos = repositoryDBDAO.selectMINunitPriceByOtherGame(str);
            }
            referencePrice = getReferencePriceExsit(referencePrice, repositoryInfos);
            info = repositoryInfos.get(0);
            //如果查出来的数据有2或以上
            if (repositoryInfos.size() >= 2) {
                referencePrice.setTotalAccount(BigInteger.valueOf(repositoryInfos.get(0).getSellableCount() + repositoryInfos.get(1).getSellableCount()));
                referencePrice.setUnitPrice(info.getUnitPrice().add(repositoryInfos.get(1).getUnitPrice()).divide(new BigDecimal("2")).setScale(5, BigDecimal.ROUND_HALF_UP));
                referencePrice.setMoneyName(info.getMoneyName());
            } else {
                referencePrice.setTotalAccount(BigInteger.valueOf(info.getSellableCount()));
                referencePrice.setUnitPrice(info.getUnitPrice().setScale(5, BigDecimal.ROUND_HALF_UP));
                referencePrice.setMoneyName(info.getMoneyName());
            }
        } else {
            //非地下城与勇士的游戏取最低库存的第一个
            str.put("gameName", info.getGameName());
            str.put("region", info.getRegion());
            str.put("gameRace", info.getGameRace());
            //查询游戏的合并服
            str = getRepositoryInfoByServers(info, str);
            repositoryInfos = repositoryDBDAO.selectMINunitPriceByOtherGame(str);
            referencePrice = getReferencePriceExsit(referencePrice, repositoryInfos);
            info = repositoryInfos.get(0);
            referencePrice.setTotalAccount(BigInteger.valueOf(info.getSellableCount()));
            referencePrice.setUnitPrice(info.getUnitPrice().setScale(5, BigDecimal.ROUND_HALF_UP));
            referencePrice.setMoneyName(info.getMoneyName());
        }
        //判断是否存在 更新操作
        ReferencePrice exsistReferencePrice = referencePriceDao.selectByGameNameAndRegionNameAndServerNameAndRaceName(referencePrice);
        if (exsistReferencePrice != null) {
            exsistReferencePrice.setTotalAccount(referencePrice.getTotalAccount());
            exsistReferencePrice.setUnitPrice(referencePrice.getUnitPrice());
            exsistReferencePrice.setMoneyName(referencePrice.getMoneyName());
            exsistReferencePrice.setUpdateTime(new Date());
            referencePriceDao.update(exsistReferencePrice);
            //将计算好的最低参考价进行redis更新
            referencePriceRedisDao.saveUpdate(exsistReferencePrice);
        } else {
            logger.info("添加库存信息{}", referencePrice);
            if (StringUtils.isNotBlank(referencePrice.getGameName()) || StringUtils.isNotBlank(referencePrice.getServerName()) || StringUtils.isNotBlank(referencePrice.getRegionName())) {
                referencePrice.setUpdateTime(new Date());
                referencePrice.setCreateTime(new Date());
                referencePriceDao.insert(referencePrice);
                //将计算好的最低参考价进行redis保存
                referencePriceRedisDao.saveUpdate(referencePrice);
            }
        }
        return referencePrice;
    }

    // 查询合区合服之后的游戏库存信息
    private HashMap<String, Object> getRepositoryInfoByServers(RepositoryInfo info, HashMap<String, Object> str) {
        //查询游戏的合并服
        Map<String, Object> serverMap = new HashMap<String, Object>();
        serverMap.put("backGameName", info.getGameName());
        serverMap.put("backRegion", info.getRegion());
        serverMap.put("gameRace", info.getGameRace());
        serverMap.put("backServer", info.getServer());
        serverMap.put("isDeleted", false);
        Map<String, Object> servers = repositoryManager.processRepositoryTransfer2(serverMap);
        //将查询的游戏的所有的合并服存入集合
        String server = (String) servers.get("servers");
        if (StringUtils.isBlank(server)) {
            server = "'" + info.getServer() + "'";
        }
        str.put("server", server);
        str.put("regions", servers.get("regions"));
        return str;
    }

    //判断单价库存是否存在,设置挂起事务
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private ReferencePrice getReferencePriceExsit(ReferencePrice referencePrice, List<RepositoryInfo> repositoryInfos) {
        if (repositoryInfos == null || repositoryInfos.size() <= 0) {
            ReferencePrice selectReferencePrice = referencePriceDao.selectByGameNameAndRegionNameAndServerNameAndRaceName(referencePrice);
            if (selectReferencePrice != null) {
                selectReferencePrice.setUnitPrice(null);
                selectReferencePrice.setTotalAccount(new BigInteger("0"));
                selectReferencePrice.setUpdateTime(new Date());
                referencePriceDao.update(selectReferencePrice);
                referencePriceRedisDao.saveUpdate(selectReferencePrice);
            } /*else {
                referencePrice.setUnitPrice(null);
                referencePrice.setTotalAccount(new BigInteger("0"));
                referencePrice.setCreateTime(new Date());
                referencePrice.setUpdateTime(new Date());
                referencePriceDao.insert(referencePrice);
                referencePriceRedisDao.saveUpdate(referencePrice);
            }*/
            throw new SystemException(ResponseCodes.NotAvaliableRegionAndServer.getCode(),
                    ResponseCodes.NotAvaliableRegionAndServer.getMessage());
        }
        return referencePrice;
    }

    //添加
    public void addRepositoryConfig(RepositoryConfine repositoryConfine) {
        if (repositoryConfine != null) {
            repositoryConfigDao.insert(repositoryConfine);
            //存入redis中
            repositoryConfigRedisDAO.saveRepositoryConfig(repositoryConfine);
        }
    }

    //查询
    public GenericPage<RepositoryConfine> queryRepositoryList(Map<String, Object> queryMap, int limit, int start,
                                                              String sortBy, boolean isAsc) {
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "id";
        }
        return repositoryConfigDao.selectByMap(queryMap, limit, start, sortBy, isAsc);
    }

    //删除
    public int deleteConfig(RepositoryConfine repositoryConfine) {
        //根据id查询名称
        RepositoryConfine repositoryconfine = repositoryConfigDao.selectById(repositoryConfine.getId());
        //根据名称主键删除redis中的数据
        //判空
        if (repositoryconfine == null) {
            throw new SystemException(ResponseCodes.RepositoryIsEmptyIdWrong.getCode());
        }
        repositoryConfigRedisDAO.deleteRepositoryConfig(repositoryconfine);
        return repositoryConfigDao.deleteById(repositoryConfine.getId());

    }

    //启用
    public void enabledConfig(RepositoryConfine repositoryConfine) {
        if (repositoryConfine != null) {
            if (repositoryConfine.getId() == null) {
                throw new SystemException(ResponseCodes.EmptyId.getCode());
            }
            RepositoryConfine repositoryConfig = repositoryConfigDao.selectById(repositoryConfine.getId());
            if (repositoryConfig != null) {
                if (repositoryConfig.getIsEnabled()) {
                    throw new SystemException(ResponseCodes.RepositoryIsEnableWrong.getCode());
                }

                repositoryConfig.setIsEnabled(true);
                repositoryConfigDao.update(repositoryConfig);
                repositoryConfigRedisDAO.saveRepositoryConfig(repositoryConfig);
            }
        }
    }

    //禁用
    public void disableConfig(RepositoryConfine repositoryConfine) {
        if (repositoryConfine != null) {
            if (repositoryConfine.getId() == null) {
                throw new SystemException(ResponseCodes.EmptyId.getCode());
            }
            RepositoryConfine entity = repositoryConfigDao.selectById(repositoryConfine.getId());
            if (entity != null) {
                if (!entity.getIsEnabled()) {
                    throw new SystemException(ResponseCodes.RepositoryIsDisableWrong.getCode());
                }
                entity.setIsEnabled(false);
                repositoryConfigDao.update(entity);
                repositoryConfigRedisDAO.saveRepositoryConfig(entity);
            }

        }
    }

    public RepositoryConfine selectRepositoryByMap(String gameName,String goodsTypeName) {
        return repositoryConfigDao.selectRepositoryByMap(gameName,goodsTypeName);
    }

    //更新
    public void updateConfig(RepositoryConfine repositoryConfine) {
        if (repositoryConfine != null) {
            repositoryConfigDao.update(repositoryConfine);
            repositoryConfigRedisDAO.saveRepositoryConfig(repositoryConfine);
        }
    }

    public List<RepositoryConfine> selectRepository() {
        //从redis中查询通用游戏限制配置
//        RepositoryConfine repositoryConfine = repositoryConfigRedisDAO.selectRepositoryByGameName("通用游戏限制配置");
//        if (repositoryConfine != null) {
//            List<RepositoryConfine> list = new ArrayList<RepositoryConfine>();
//            list.add(0, repositoryConfine);
//            return list;
//        }
        List<RepositoryConfine> list = repositoryConfigDao.selectRepository();
        //保存到redis
        repositoryConfigRedisDAO.saveRepositoryConfig(list.get(0));
        return list;
    }

    public void updateRepositoryCount(BigInteger repositoryCount) {
        RepositoryConfine repositoryConfine = new RepositoryConfine();
        repositoryConfine.setIsEnabled(true);
        repositoryConfine.setUpdateTime(new Date());
        repositoryConfine.setGameName("通用游戏限制配置");
        repositoryConfine.setId(1L);
        repositoryConfine.setRepositoryCount(repositoryCount);
        repositoryConfigDao.update(repositoryConfine);
        //更新redis中的数据
        repositoryConfigRedisDAO.saveRepositoryConfig(repositoryConfine);

    }
}
