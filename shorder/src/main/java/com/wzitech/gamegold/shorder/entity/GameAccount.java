package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收货角色
 */
public class GameAccount extends BaseEntity {
    /**
     * 角色状态：空闲
     */
    public static final int S_FREE = 1;
    /**
     * 角色状态：收货中
     */
    public static final int S_RECEIVING = 2;
    /**
     * 角色状态：分仓中
     */
    public static final int S_SPLIT = 3;
    /**
     * 角色状态：下架
     */
    public static final int S_OFFLINE = 4;
    /**
     * 收货方5173账号
     */
    private String buyerAccount;
    /**
     * 收货方5173UID
     */
    private String buyerUid;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 游戏区
     */
    private String region;
    /**
     * 游戏服
     */
    private String server;
    /**
     * 游戏阵营
     */
    private String gameRace;
    /**
     * 收货的游戏账号
     */
    private String gameAccount;
    /**
     * 游戏密码
     */
    private String gamePwd;
    /**
     * 收货的角色名
     */
    private String roleName;
    /**
     * 二级密码
     */
    private String secondPwd;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 收货数量
     */
    private Long count;

    /**
     * 最小收货量，上传excel时统计用，不存数据库
     */
    private Long minCount;
    /**
     * 收货单价
     */
    private BigDecimal price;
    /**
     * 库存数量
     */
    private Long repositoryCount;
    /**
     * 是否收货角色
     */
    private Boolean isShRole;
    /**
     * 背包是否已满
     */
    private Boolean isPackFull;
    /**
     * 角色状态
     * <li>1：空闲</li>
     * <li>2：收货中</li>
     * <li>3：分仓中</li>
     */
    private Integer status;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 盘货时间
     */
    private Date stockTime;

    /**
     * 联系电话
     */
    private String tel;
    /**
     * 是否销售角色
     */
    private Boolean isSale;

    public Boolean getIsSale() {
        return isSale;
    }

    public void setIsSale(Boolean sale) {
        isSale = sale;
    }

    private Long repositoryId;

    public Long getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Long repositoryId) {
        this.repositoryId = repositoryId;
    }

    /**
     * 以下为分仓项目 新增库存明细信息
     */

    /**
     * 交易上限
     *
     * @return
     */
    private Long tradeQuota;

    /**
     * 库存上限
     *
     * @return
     */
    private Long repositoryQuota;

    /**
     * 库存缺口
     *
     * @BigDecimal
     */
    private Long repositoryGaps;

    /**
     * 今日已售数量
     */
    private Long todaySaleCount;

    /**
     * 冻结数量
     */
    private Long  freezeNeedCount;

    public Long getFreezeNeedCount() {
        return freezeNeedCount;
    }

    public void setFreezeNeedCount(Long freezeNeedCount) {
        this.freezeNeedCount = freezeNeedCount;
    }

    public Long getTodaySaleCount() {
        return todaySaleCount;
    }

    public void setTodaySaleCount(Long todaySaleCount) {
        this.todaySaleCount = todaySaleCount;
    }

    public Long getTradeQuota() {
        return tradeQuota;
    }

    public void setTradeQuota(Long tradeQuota) {
        this.tradeQuota = tradeQuota;
    }

    public Long getRepositoryQuota() {
        return repositoryQuota;
    }

    public void setRepositoryQuota(Long repositoryQuota) {
        this.repositoryQuota = repositoryQuota;
    }

    public Long getRepositoryGaps() {
        return repositoryGaps;
    }

    public void setRepositoryGaps(Long repositoryGaps) {
        this.repositoryGaps = repositoryGaps;
    }

    public GameAccount() {
    }

    public Long getMinCount() {
        return minCount;
    }

    public void setMinCount(Long minCount) {
        this.minCount = minCount;
    }

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getGameRace() {
        return gameRace;
    }

    public void setGameRace(String gameRace) {
        this.gameRace = gameRace;
    }

    public String getGameAccount() {
        return gameAccount;
    }

    public void setGameAccount(String gameAccount) {
        this.gameAccount = gameAccount;
    }

    public String getGamePwd() {
        return gamePwd;
    }

    public void setGamePwd(String gamePwd) {
        this.gamePwd = gamePwd;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getSecondPwd() {
        return secondPwd;
    }

    public void setSecondPwd(String secondPwd) {
        this.secondPwd = secondPwd;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getIsShRole() {
        return isShRole;
    }

    public void setIsShRole(Boolean isShRole) {
        this.isShRole = isShRole;
    }

    public Boolean getIsPackFull() {
        return isPackFull;
    }

    public void setIsPackFull(Boolean isPackFull) {
        this.isPackFull = isPackFull;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getRepositoryCount() {
        return repositoryCount;
    }

    public void setRepositoryCount(Long repositoryCount) {
        this.repositoryCount = repositoryCount;
    }

    public String getBuyerUid() {
        return buyerUid;
    }

    public void setBuyerUid(String buyerUid) {
        this.buyerUid = buyerUid;
    }

    public Date getStockTime() {
        return stockTime;
    }

    public void setStockTime(Date stockTime) {
        this.stockTime = stockTime;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
