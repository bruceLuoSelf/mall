package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import com.wzitech.gamegold.shorder.entity.GameAccount;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 收货角色信息
 */
public interface IGameAccountManager {

    /**
     * 更新库存
     * @param gameAccount
     */
    public void updateGameAccountByConfig(GameAccount gameAccount);

    /**
     * 今日已售数量
     * @param saleCount  交易完成后 提供交易数量-特指游戏币数量 万金为单位
     * @param gameName  游戏名
     * @param region 游戏区
     * @param server 游戏服
     * @param gameRace 游戏阵营若有
     * @param gameAccount 游戏账号
     * @param gameRole 游戏角色
     */
    public void addTodaySaleCount(Long saleCount,String loginAccount,String gameName,String region,String server,String gameRace,String gameAccount,String gameRole);

    /**
      * 添加收货角色信息
      * @param gameAccount
      * @return
      * @throws SystemException
      */
     GameAccount addGameAccount(GameAccount gameAccount) throws SystemException;

     /**
      * 获取设定条件下所有的收货角色信息
      * @param queryMap
      * @return
      */
     List<GameAccount> queryGameAccount(Map<String, Object> queryMap);

    /**
     * 修改密码
     *
     */
      int  updateGamePwd(Long id,String gamePwd,String secondPwd);
    /**
     * 删除
     */
    int deleteGameAccount(Long id);
     /**
      * 可以收货的账号角色
      * @param order
      * @return
      */
     List<GameAccount> queryGameAccountCanSh(DeliveryOrder order);

    /**
     * 可以收货的账号角色
     * @param order
     * @return
     */
    long queryGameAccountCanShCountSum(DeliveryOrder order);

    Boolean queryOnlyGameAccountCanSh(DeliverySubOrder suborder);

    /**
     * 可以收货的账号角色  机器收货使用
     *
     * @param queryMap
     * @return
     */
    List<GameAccount> queryGameAccountCanShByAutomete(Map<String, Object> queryMap);

    /**
     * 更新游戏角色库存数量和等级
     * @param buyerAccount 收货商5173账号
     * @param gameName 游戏名
     * @param region 区
     * @param server 服
     * @param gameRace 阵营
     * @param gameAccount 游戏账号
     * @param roleName 游戏角色名
     * @param repositoryCount 库存数量
     * @param level 角色等级
     * @return boolean 返回是否更新成功
     */
     boolean updateRepositoryCountAndLevel(String buyerAccount, String gameName, String region, String server, String gameRace,String gameAccount, String roleName, long repositoryCount, Integer level);

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
    boolean addRepositoryCount(String buyerAccount, String gameName, String region, String server,
                                                 String gameRace, String gameAccount, String roleName, long repositoryCount,Boolean isStock);

     List<GameAccount> queryGameAccount(String buyerAccount, String gameName, String reion, String server, String gameAccount, String roleName);

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
    boolean reduceRepositoryCount(String buyerAccount, String gameName, String region, String server,
                                         String gameRace, String gameAccount, String roleName, long repositoryCount, Boolean isStock);

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
     boolean updateRepositoryCount(String buyerAccount, String gameName, String region, String server,
                                         String gameRace, String gameAccount, String roleName, Long repositoryCount, String goodsTypeName);

    /**
     * 分页查询账号库存信息
     * @param map
     * @param start
     * @param pageSize
     * @param sortFields
     * @return
     */
    GenericPage<GameAccount> queryListInPage(Map<String,Object> map, int start, int pageSize, List<SortField> sortFields);

    /**
     * 导出
     * @param map
     * @param sortFields
     * @return
     */
    List<GameAccount> queryListInPage(Map<String,Object> map, List<SortField> sortFields);

    /**
     * 分页查询账号库存信息
     * @param start
     * @param pageSize
     * @return
     */
    List<GameAccount> queryNeesStoreCheckListInPage(int start, int pageSize);

    /**
     * 根据游戏收货商、游戏名、区、服获取收货角色
     */
    Set<String> selectRoleNames(String buyerAccount,String gameName,String region,String server);

    /**
     * ZW_C_JB_00004 mj
     * 根据游戏收货商、游戏名、区、服、阵营删除收货角色
     * @param ids
     * @param logginAccount
     * @return
     */

    String deleteGameAccountByPurchaseOrderId(List<Long> ids,String logginAccount);

    /**
     * updateStatus
     *
     */
    void updateStatus(Map<String, Object> map);

    /**
     * 清空今日已售数量
     */
    void emptyTodaySaleCount();
}
