package com.wzitech.gamegold.shorder.business.impl;

import com.google.common.collect.Maps;
import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.utils.DESHelper;
import com.wzitech.gamegold.shorder.business.IGameAccountManager;
import com.wzitech.gamegold.shorder.business.IRepositoryInfoManager;
import com.wzitech.gamegold.shorder.dao.IGameAccountDBDAO;
import com.wzitech.gamegold.shorder.dao.IPurchaseOrderDBDAO;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import com.wzitech.gamegold.shorder.entity.GameAccount;
import com.wzitech.gamegold.shorder.entity.PurchaseOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * 收货角色信息
 */
@Component
public class GameAccountManagerImpl extends AbstractBusinessObject implements IGameAccountManager {
    @Autowired
    IGameAccountDBDAO gameAccountDBDAO;
    @Autowired
    IPurchaseOrderDBDAO purchaseOrderDBDAO;
    @Resource(name = "queryRepositoryInfo")
    IRepositoryInfoManager repositoryInfoManager;

    @Resource(name = "syncRepositoryManager")
    ISyncRepositoryManager syncRepositoryManager;

    /**
     * 加密KEY
     */
    @Value("${shrobot.secret_key}")
    private String secretKey = "";

    public static String urlEncoding(String content, String enc) {
        if (StringUtils.isBlank(content)) return "";
        try {
            return URLEncoder.encode(content, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 更新库存
     *
     * @param gameAccount
     */
    public void updateGameAccountByConfig(GameAccount gameAccount) {
        gameAccountDBDAO.update(gameAccount);
    }

    /**
     * 更新库存
     *
     * @param gameAccount
     */
    public void updateGameAccount(GameAccount gameAccount) {
        gameAccountDBDAO.update(gameAccount);
    }

    @Override
    @Transactional
    public GameAccount addGameAccount(GameAccount gameAccount) throws SystemException {
        if (gameAccount == null) {
            throw new SystemException(ResponseCodes.EmptyShGameAccount.getCode());
        }
        if (StringUtils.isEmpty(gameAccount.getGameName())) {
            throw new SystemException(ResponseCodes.EmptyGameName.getCode());
        }
        if (StringUtils.isEmpty(gameAccount.getRegion())) {
            throw new SystemException(ResponseCodes.EmptyRegion.getCode());
        }
        if (StringUtils.isEmpty(gameAccount.getServer())) {
            throw new SystemException(ResponseCodes.EmptyGameServer.getCode());
        }
        if (StringUtils.isEmpty(gameAccount.getGameAccount())) {
            throw new SystemException(ResponseCodes.EmptyShRoleName.getCode());
        }
        if (StringUtils.isEmpty(gameAccount.getGamePwd())) {
            throw new SystemException(ResponseCodes.EmptyGamePassWord.getCode());
        }
        if (StringUtils.isEmpty(gameAccount.getRoleName())) {
            throw new SystemException(ResponseCodes.EmptyShRoleName.getCode());
        }
        if (StringUtils.isEmpty(gameAccount.getSecondPwd())) {
            gameAccount.setSecondPwd("");
            //throw new SystemException(ResponseCodes.EmptyWarehousePwd.getCode());
        }
        if (gameAccount.getLevel() < 0) {
            throw new SystemException(ResponseCodes.EmptyLevl.getCode());
        }
        if (gameAccount.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new SystemException(ResponseCodes.ShUnitPriceMustGreaterThanZero.getCode());
        }
        if (gameAccount.getCount() < 0) {
            throw new SystemException(ResponseCodes.EmptyShCount.getCode());
        }

        //设置新增时的默认值
        gameAccount.setIsPackFull(false);
        gameAccount.setUpdateTime(new Date());

        //查询是否已存在该收货方，相同的账号角色信息，如果存在，目前按照覆盖的原则，而不是累加
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("buyerAccount", gameAccount.getBuyerAccount());
        queryMap.put("gameName", gameAccount.getGameName());
        queryMap.put("region", gameAccount.getRegion());
        queryMap.put("server", gameAccount.getServer());
        queryMap.put("gameAccount", gameAccount.getGameAccount());
        queryMap.put("roleName", gameAccount.getRoleName());

        List<GameAccount> gameAccountList = gameAccountDBDAO.queryGameAccount(queryMap);
        //保存到数据库中
        if (!CollectionUtils.isEmpty(gameAccountList)) {
            GameAccount dbExsitGameAccount = gameAccountList.get(0);
            if (gameAccount.getCount() == null) {
                gameAccount.setCount(dbExsitGameAccount.getCount());
            } else if (gameAccount.getCount() != null && gameAccount.getCount() > 999999999) {
                gameAccount.setCount(999999999L);
            }

            if (gameAccount.getLevel() == null) {
                gameAccount.setLevel(dbExsitGameAccount.getLevel());
            }

            //默认收货角色为空的话，设置非收货角色
            if (gameAccount.getIsShRole() == null) {
                gameAccount.setIsShRole(false);
            }

            if (gameAccount.getPrice() == null) {
                gameAccount.setPrice(dbExsitGameAccount.getPrice());
            } else {
                gameAccount.setPrice(gameAccount.getPrice().setScale(5, BigDecimal.ROUND_HALF_UP));
            }
            gameAccount.setId(dbExsitGameAccount.getId());
            gameAccount.setRepositoryCount(dbExsitGameAccount.getRepositoryCount());
            //如果先前是下架的，则空闲
            if (gameAccount.getStatus() != null && gameAccount.getStatus().intValue() == GameAccount.S_OFFLINE) {
                gameAccount.setStatus(GameAccount.S_FREE);
            }
            gameAccountDBDAO.update(gameAccount);
        } else {
            gameAccount.setStatus(GameAccount.S_FREE);
            gameAccount.setStockTime(new Date());
            //判断此角色是否存在销售库存表中
            if (repositoryInfoManager.countByGameAccount(gameAccount)) {
                gameAccount.setIsSale(true);
            }
            gameAccountDBDAO.insert(gameAccount);
        }
        return gameAccount;
    }

    /*
        分页查询收货角色
         */
    @Override
    public List<GameAccount> queryGameAccount(Map<String, Object> queryMap) {
        List<GameAccount> list = gameAccountDBDAO.queryGameAccount(queryMap);

        //隐藏敏感数据
        for (GameAccount gameAccount : list) {
            gameAccount.setGamePwd("");
            gameAccount.setSecondPwd("");
            gameAccount.setRepositoryCount(0L);
            gameAccount.setIsPackFull(null);
        }
        return list;
    }

    /**
     * 修改密码
     */
    @Override
    @Transactional
    public int updateGamePwd(Long id, String gamePwd, String secondPwd) {
        GameAccount gameAccount = gameAccountDBDAO.selectById(id);
        gameAccount.setGamePwd(gamePwd);
        gameAccount.setSecondPwd(secondPwd);
        //如果先前是下架的，则空闲
        if (gameAccount.getStatus() != null && gameAccount.getStatus().intValue() == GameAccount.S_OFFLINE) {
            gameAccount.setStatus(GameAccount.S_FREE);
        }

        return gameAccountDBDAO.update(gameAccount);
    }

    /**
     * 删除
     */
    @Override
    @Transactional
    public int deleteGameAccount(Long id) {
        //删除游戏账号后，同步库存数量
        GameAccount gameAccount = gameAccountDBDAO.selectById(id);

        int result = gameAccountDBDAO.deleteById(id);

        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("buyerAccount", gameAccount.getBuyerAccount());
        queryMap.put("gameName", gameAccount.getGameName());
        queryMap.put("region", gameAccount.getRegion());
        queryMap.put("server", gameAccount.getServer());
        purchaseOrderDBDAO.updatePurchaseOrderCount(queryMap);
        return result;
    }

    /**
     * 可以收货的账号角色
     *
     * @param order
     * @return
     */
    @Override
    public List<GameAccount> queryGameAccountCanSh(DeliveryOrder order) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("buyerAccount", order.getBuyerAccount());
        params.put("gameName", order.getGameName());
        params.put("region", order.getRegion());
        params.put("server", order.getServer());
        params.put("gameRace", order.getGameRace());
        params.put("status", GameAccount.S_FREE);
        params.put("orderBy", "count desc");
        params.put("isLock", true);
        return gameAccountDBDAO.queryGameAccountCanSh(params);
    }

    /**
     * @param
     * @return
     */
    @Override
    public Boolean queryOnlyGameAccountCanSh(DeliverySubOrder suborder) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("gameAccount", suborder.getGameAccount());
        params.put("gameName", suborder.getGameName());
//        params.put("roleName",suborder.getGameRole());
        Long count = gameAccountDBDAO.queryOnlyGameAccountCanSh(params);
        if (count == null) {
            count = 0L;
        }
        return count > 0L ? false : true;
    }

    /**
     * 可以收货的账号角色
     *
     * @param order
     * @return
     */
    @Override
    public long queryGameAccountCanShCountSum(DeliveryOrder order) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("buyerAccount", order.getBuyerAccount());
        params.put("gameName", order.getGameName());
        params.put("region", order.getRegion());
        params.put("server", order.getServer());
        params.put("gameRace", order.getGameRace());
        params.put("status", GameAccount.S_FREE);
        params.put("orderBy", "count desc");
        return gameAccountDBDAO.queryGameAccountCanShCountSum(params);
    }

    /**
     * 可以收货的账号角色  机器收货使用
     *
     * @param queryMap
     * @return
     */
    @Override
    public List<GameAccount> queryGameAccountCanShByAutomete(Map<String, Object> queryMap) {
        return gameAccountDBDAO.queryGameAccountCanShByAutomete(queryMap);
    }

    /**
     * 更新游戏角色库存数量和等级
     *
     * @param buyerAccount    收货商5173账号
     * @param gameName        游戏名
     * @param region          区
     * @param server          服
     * @param gameRace        阵营
     * @param gameAccount     游戏账号
     * @param roleName        游戏角色名
     * @param repositoryCount 库存数量
     * @param level           角色等级
     * @return boolean 返回是否更新成功
     */
    @Override
    @Transactional
    public boolean updateRepositoryCountAndLevel(String buyerAccount, String gameName, String region, String server,
                                                 String gameRace, String gameAccount, String roleName, long repositoryCount,
                                                 Integer level) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("buyerAccount", buyerAccount);
        params.put("gameName", gameName);
        params.put("region", region);
        params.put("server", server);
        params.put("gameRace", gameRace);
        params.put("gameAccount", gameAccount);
        params.put("roleName", roleName);
        if (repositoryCount > 0) {
            params.put("repositoryCount", repositoryCount);
        }
        if (level != null && level > 0) {
            params.put("level", level);
        }
        params.put("stockTime", new Date());
        int count = gameAccountDBDAO.updateAccountByMap(params);
        if (count > 0)
            return true;
        return false;
    }

    /**
     * 更新游戏角色库存数量
     *
     * @param buyerAccount    收货商5173账号
     * @param gameName        游戏名
     * @param region          区
     * @param server          服
     * @param gameRace        阵营
     * @param gameAccount     游戏账号
     * @param roleName        游戏角色名
     * @param repositoryCount 库存数量
     * @return boolean 返回是否更新成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRepositoryCount(String buyerAccount, String gameName, String region, String server,
                                      String gameRace, String gameAccount, String roleName, long repositoryCount, Boolean isStock) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("buyerAccount", buyerAccount);
        params.put("gameName", gameName);
        params.put("region", region);
        params.put("server", server);
        params.put("gameRace", gameRace);
        params.put("gameAccount", gameAccount);
        params.put("roleName", roleName);
        params.put("repositoryCount", repositoryCount);
        if (isStock) {
            params.put("stockTime", new Date());
        }
        int count = gameAccountDBDAO.addRepositoryCount(params);
        if (count > 0)
            return true;
        return false;
    }

    @Override
    public List<GameAccount> queryGameAccount(String buyerAccount, String gameName, String reion, String server, String gameAccount, String roleName) {
        return gameAccountDBDAO.queryGameAccount(buyerAccount, gameName, reion, server, gameAccount, roleName);
    }

    /**
     * 更新游戏角色库存数量和等级
     *
     * @param buyerAccount    收货商5173账号
     * @param gameName        游戏名
     * @param region          区
     * @param server          服
     * @param gameRace        阵营
     * @param gameAccount     游戏账号
     * @param roleName        游戏角色名
     * @param repositoryCount 库存数量
     * @return boolean 返回是否更新成功
     */
    @Override
    @Transactional
    public boolean reduceRepositoryCount(String buyerAccount, String gameName, String region, String server,
                                         String gameRace, String gameAccount, String roleName, long repositoryCount, Boolean isStock) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("buyerAccount", buyerAccount);
        params.put("gameName", gameName);
        params.put("region", region);
        params.put("server", server);
        params.put("gameRace", gameRace);
        params.put("gameAccount", gameAccount);
        params.put("roleName", roleName);
        params.put("repositoryCount", repositoryCount);
        if (isStock) {
            params.put("stockTime", new Date());
        }
        int count = gameAccountDBDAO.reduceRepositoryCount(params);
        if (count > 0)
            return true;
        return false;
    }

    /**
     * 更新游戏角色库存数量
     *
     * @param buyerAccount    收货商5173账号
     * @param gameName        游戏名
     * @param region          区
     * @param server          服
     * @param gameRace        阵营
     * @param gameAccount     游戏账号
     * @param roleName        游戏角色名
     * @param repositoryCount 库存数量
     * @return boolean 返回是否更新成功
     */
    @Override
    @Transactional
    public boolean updateRepositoryCount(String buyerAccount, String gameName, String region, String server,
                                         String gameRace, String gameAccount, String roleName, Long repositoryCount, String goodsTypeName) {
        if (!ServicesContants.GOODS_TYPE_GOLD.equals(goodsTypeName)) {
            return false;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("buyerAccount", buyerAccount);
        params.put("gameName", gameName);
        params.put("region", region);
        params.put("server", server);
        params.put("gameRace", gameRace);
        params.put("gameAccount", gameAccount);
        params.put("roleName", roleName);
        params.put("repositoryCount", repositoryCount);
        params.put("updateTime", new Date());
        int count = gameAccountDBDAO.updateAccountByMapNotAspect(params);
        if (count > 0)
            return true;
        return false;
    }

    /**
     * 分页查询账号库存信息
     *
     * @param map
     * @param start
     * @param pageSize
     * @param sortFields
     * @return
     */
    @Override
    public GenericPage<GameAccount> queryListInPage(Map<String, Object> map, int start, int pageSize, List<SortField> sortFields) {
        GenericPage<GameAccount> genericPage = gameAccountDBDAO.selectRepositoryGameAccountList(map, sortFields, start, pageSize);
        return genericPage;
    }

    /**
     * 导出
     *
     * @param map
     * @param sortFields
     * @return
     */
    @Override
    public List<GameAccount> queryListInPage(Map<String, Object> map, List<SortField> sortFields) {
        List<GameAccount> list = gameAccountDBDAO.selectRepositoryGameAccountList(map, sortFields);
        return list;
    }

    /**
     * 分页查询账号库存信息
     *
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<GameAccount> queryNeesStoreCheckListInPage(int start, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("needStoreCheck", true);
        GenericPage<GameAccount> genericPage = gameAccountDBDAO.selectRepositoryGameAccountList(map, null, start, pageSize);
        List<GameAccount> list = genericPage.getData();
        if (list != null && list.size() > 0) {
            for (GameAccount gameAccount : list) {
                String accountData = getEncryptAccountInfo(gameAccount.getGameAccount(), gameAccount.getGamePwd(), gameAccount.getSecondPwd());
                gameAccount.setGameAccount(accountData);
                gameAccount.setGamePwd(null);
                gameAccount.setSecondPwd(null);
                gameAccount.setCount(null);
                gameAccount.setLevel(null);
                gameAccount.setPrice(null);
                gameAccount.setIsPackFull(null);
                gameAccount.setIsShRole(null);
                gameAccount.setStatus(null);
                gameAccount.setBuyerUid(null);
            }
        }
        return list;
    }

    /**
     * 根据游戏收货商、游戏名、区、服获取收货角色
     *
     * @param buyerAccount
     * @param gameName
     * @param region
     * @param server
     * @return
     */
    @Override
    public Set<String> selectRoleNames(String buyerAccount, String gameName, String region, String server) {
        Set<String> roleNames = new HashSet<String>();
        if (StringUtils.isNotBlank(buyerAccount) && StringUtils.isNotBlank(gameName) && StringUtils.isNotBlank(region) && StringUtils.isNotBlank(server)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("buyerAccount", buyerAccount);
            map.put("gameName", gameName);
            map.put("region", region);
            map.put("server", server);
            map.put("isShRole", true);
            List<GameAccount> gameAccountList = queryGameAccount(map);
            if (gameAccountList != null && gameAccountList.size() > 0) {
                for (GameAccount entity : gameAccountList) {
                    roleNames.add(entity.getRoleName());
                }
            }
        }
        return roleNames;
    }

    /**
     * 获取加密的账号信息
     *
     * @param gameAccount
     * @param gamePwd
     * @param secondPwd
     * @return
     */
    private String getEncryptAccountInfo(String gameAccount, String gamePwd, String secondPwd) {
        //账户信息（游戏账号+游戏密码+二级密码DES加密）
        StringBuilder s = new StringBuilder("{");
        s.append("\"gameAccount\":\"").append(gameAccount).append("\",");
        s.append("\"gamePwd\":\"").append(urlEncoding(gamePwd, "UTF-8")).append("\",");
        s.append("\"secondPwd\":\"").append(urlEncoding(secondPwd, "UTF-8")).append("\"");
        s.append("}");
        String account = null;
        try {
            account = DESHelper.encryptDesEcb(s.toString(), secretKey);
        } catch (Exception e) {
            logger.error("DES加密账号信息出错了", e);
        }
        return account;
    }

    /**
     * 根据游戏收货商、游戏名、区、服、阵营删除收货角色
     * ZW_C_JB_00004 mj
     *
     * @param ids
     * @param logginAccount
     * @return
     */
    @Override
    @Transactional
    public String deleteGameAccountByPurchaseOrderId(List<Long> ids, String logginAccount) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        PurchaseOrder entity;
        Long id;
        int tradingNum = 0;
        if (ids == null && ids.size() < 1) {
            throw new SystemException(ResponseCodes.EmptyPurchaseOrderId.getCode(),
                    ResponseCodes.EmptyPurchaseOrderId.getMessage());
        }
        Iterator<Long> iterator = ids.listIterator();
        while (iterator.hasNext()) {
            map = new HashMap<String, Object>();
            id = iterator.next();
            if (id == null) {
                //删除id为空的数据
                iterator.remove();
                continue;
            }
            entity = purchaseOrderDBDAO.selectByIdForUpdate(id);
            logger.info("需要删除采购单：{}", entity);
            if (entity == null) {
                throw new SystemException(ResponseCodes.EmptyPurchaseOrder.getCode(),
                        ResponseCodes.EmptyPurchaseOrder.getMessage());
            }
            //不是当用用户的采购单则不能操作
            if (StringUtils.isBlank(entity.getBuyerAccount())) {
                throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                        ResponseCodes.NotFindPurchaser.getMessage());
            } else if (!entity.getBuyerAccount().equals(logginAccount)) {
                throw new SystemException(ResponseCodes.NotYourOrder.getCode(),
                        ResponseCodes.NotYourOrder.getMessage());
            }

            //游戏名称、区服非空判断
            if (StringUtils.isBlank(entity.getGameName()) || StringUtils.isBlank(entity.getRegion())
                    || StringUtils.isBlank(entity.getServer())) {
                throw new SystemException(ResponseCodes.NullGameNameAndRegionAndServer.getCode(),
                        ResponseCodes.NullGameNameAndRegionAndServer.getMessage());
            }
            //将采购单的卖家账号、游戏名、区服存入map
            map.put("buyerAccount", entity.getBuyerAccount());
            map.put("gameName", entity.getGameName());
            map.put("region", entity.getRegion());
            map.put("server", entity.getServer());
            if (StringUtils.isNotBlank(entity.getGameRace())) {
                map.put("gameRace", entity.getGameRace());
            }
            //查询采购单对应的收货角色并判断是否在交易中
            List<GameAccount> gameAccountList = gameAccountDBDAO.selectByMap(map);
            //判断是否有交易中的收货角色开关
            boolean flag = true;
            if (gameAccountList != null && gameAccountList.size() > 0) {
                for (GameAccount gameAccount : gameAccountList) {
                    if (gameAccount.getStatus() != null && gameAccount.S_RECEIVING == gameAccount.getStatus().intValue()) {
                        //有交易中的收货角色则不删除其采购单
                        iterator.remove();
                        flag = false;
                        ++tradingNum;
                        logger.info("收货角色在交易中此采购单不能删除：{}，删除失败", gameAccount);
                        break;
                    }
                }
                //有交易中的收货角色则不删除收货角色
                if (flag) {
                    list.add(map);
                }
            }
        }
        //删除数据
        if (ids.size() > 0) {
            if (list.size() > 0) {
                //先批量删除收货角色
                gameAccountDBDAO.deleteGameAccountByMap(list);
                logger.info("收货角色对应的游戏名区服及收货商为：{}，删除成功", list);
            }
            //批量删除采购单
            purchaseOrderDBDAO.deletePurchaseOrders(ids);
            logger.info("采购单id为：{}，删除成功", ids);
            if (tradingNum > 0) {
                return "部分商品正在交易无法删除，请在交易完成后进行删除";
            } else {
                return "删除成功";
            }
        }
        return "商品正在交易中，不能删除";
    }

    /**
     * updateStatus
     */
    @Override
    public void updateStatus(Map<String, Object> map) {
        gameAccountDBDAO.updateStatus(map);
    }

    @Override
    public void addTodaySaleCount(Long saleCount,String loginAccount, String gameName, String region, String server, String gameRace, String gameAccount, String gameRole) {
        if (StringUtils.isBlank(gameName)) {
            throw new SystemException(ResponseCodes.EmptyGameName.getCode(), ResponseCodes.EmptyGameName.getMessage());
        }
        if(StringUtils.isBlank(loginAccount)){
            throw new SystemException(ResponseCodes.NotEmptyLoginAccount.getCode(),ResponseCodes.NotEmptyLoginAccount.getMessage());
        }
        if (StringUtils.isBlank(region)) {
            throw new SystemException(ResponseCodes.EmptyRegion.getCode(), ResponseCodes.EmptyRegion.getMessage());
        }
        if (StringUtils.isBlank(server)) {
            throw new SystemException(ResponseCodes.EmptyGameServer.getCode(), ResponseCodes.EmptyGameServer.getMessage());
        }
        if (StringUtils.isBlank(gameAccount)) {
            throw new SystemException(ResponseCodes.EmptyGameAccount.getCode(), ResponseCodes.EmptyGameAccount.getMessage());
        }
        if (StringUtils.isBlank(gameRole)) {
            throw new SystemException(ResponseCodes.EmptyRoleName.getCode(), ResponseCodes.EmptyRoleName.getMessage());
        }
        if (saleCount == null || saleCount < 0) {
            throw new SystemException(ResponseCodes.EmptyGoodsCount.getCode(), ResponseCodes.EmptyGoodsCount.getMessage());
        }
        Map<String, Object> updateTodaySaleMap = new HashMap<String, Object>();
        updateTodaySaleMap.put("gameName", gameName);
        updateTodaySaleMap.put("region", region);
        updateTodaySaleMap.put("server", server);
        if (StringUtils.isNotBlank(gameRace)) {
            updateTodaySaleMap.put("gameRace", gameRace);
        }
        updateTodaySaleMap.put("gameAccount", gameAccount);
        updateTodaySaleMap.put("roleName", gameRole);
        updateTodaySaleMap.put("buyerAccount",loginAccount);
        updateTodaySaleMap.put("addTodaySaleCount", saleCount);
        gameAccountDBDAO.updateStatus(updateTodaySaleMap);
    }

    /**
     * 清空今日已售数量
     */
    @Override
    public void emptyTodaySaleCount() {
        gameAccountDBDAO.emptyTodaySaleCount();
    }
}
