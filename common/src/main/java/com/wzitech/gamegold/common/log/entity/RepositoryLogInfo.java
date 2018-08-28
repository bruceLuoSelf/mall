package com.wzitech.gamegold.common.log.entity;

/**
 * 库存日志实体
 * @author yemq
 * *         Update History
 *         Date         Name            Reason For Update
 *         ----------------------------------------------
 *         2017/5/15    wrf              ZW_C_JB_00008商城增加通货
 */
public class RepositoryLogInfo extends BaseLogInfo {
    /**
     * 库存ID
     */
    private Long repositoryId;

    /**
     * 卖家5173账号
     */
    private String sellerAccount;

    /**
     * 游戏账号
     */
    private String gameAccount;

    /**
     * 卖家游戏角色名
     */
    private String sellerGameRole;

    /**
     * 游戏名
     */
    private String gameName;

    /**
     * 区
     */
    private String region;

    /**
     * 服
     */
    private String server;

    /**
     * 阵营
     */
    private String gameRace;
    /**
     * ZW_C_JB_00008_2017/5/15 add start
     */
    /**
     * 商品类型
     */
    private String goodsTypeName;

    /**
     * 商品类型id
     */
    private Long goodsTypeId;

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public void setGoodsTypeId(Long goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public Long getGoodsTypeId() {
        return goodsTypeId;
    }
    /**
     * ZW_C_JB_00008_2017/5/15 add end
     */
    public RepositoryLogInfo() {}

    public Long getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    public String getGameAccount() {
        return gameAccount;
    }

    public void setGameAccount(String gameAccount) {
        this.gameAccount = gameAccount;
    }

    public String getSellerGameRole() {
        return sellerGameRole;
    }

    public void setSellerGameRole(String sellerGameRole) {
        this.sellerGameRole = sellerGameRole;
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
}
