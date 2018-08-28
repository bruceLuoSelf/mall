package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/4.
 */
public class ShGameConfig extends BaseEntity {
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 单位
     */
    private String unitName;
    /**
     * 是否启用收货
     */
    private Boolean isEnabled;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后更新时间
     */
    private Date updateTime;
    /**
     * 当前游戏支持的交易方式（多个用逗号分割）
     */
    private String tradeType;
    /**
     * 当前游戏支持的交易方式Id（多个用逗号分隔）
     */
    private String tradeTypeId;

    /**
     * 当前游戏类目
     */
    private String goodsTypeName;

    /**
     * 最低购买金额 ADD 20170606
     */
    private BigDecimal minBuyAmount;

    /**
     * 当前游戏类目id
     */
    private Long goodsTypeId;
    /**
     * 当前游戏是否支持机器收货
     */

    private Boolean enableRobot;

    /**
     * 是否开启商城出售
     * wrf 5.11新增
     */
    private Boolean enableMall;


    /**
     * M、APP商城接口增加通货项目
     *
     * 九宫格配置 以json形式的字符串 表示可以购买 如 10  ，20，30 金额的商品
     */
    private String nineBlockConfigure;

    /**
     * 发货信息提示
     */
    private String deliveryMessage;

    private boolean nineBlockEnable;

    /**
     *库存上限
     * @return
     */
    private Long repositoryCount;

    /**
     * 缺口上限
     * @return
     */
    private Long needCount;

    /**
     * 邮寄手续费
     * @return
     */
    private Double mailFee;

    /**
     * 分仓阈值
     * @return
     */
    private Long thresholdCount;

    /**
     * 收货商分仓开关
     * @return
     */
    private Boolean isSplit;

    public Boolean getIsSplit() {
        return isSplit;
    }

    public void setIsSplit(Boolean split) {
        isSplit = split;
    }

    public Long getRepositoryCount() {
        return repositoryCount;
    }

    public void setRepositoryCount(Long repositoryCount) {
        this.repositoryCount = repositoryCount;
    }

    public Long getNeedCount() {
        return needCount;
    }

    public void setNeedCount(Long needCount) {
        this.needCount = needCount;
    }

    public Double getMailFee() {
        return mailFee;
    }

    public void setMailFee(Double mailFee) {
        this.mailFee = mailFee;
    }

    public Long getThresholdCount() {
        return thresholdCount;
    }

    public void setThresholdCount(Long thresholdCount) {
        this.thresholdCount = thresholdCount;
    }

    public boolean getNineBlockEnable() {
        return nineBlockEnable;
    }

    public void setNineBlockEnable(boolean nineBlockEnable) {
        this.nineBlockEnable = nineBlockEnable;
    }

    public String getNineBlockConfigure() {
        return nineBlockConfigure;
    }

    public void setNineBlockConfigure(String nineBlockConfigure) {
        this.nineBlockConfigure = nineBlockConfigure;
    }

    public String getDeliveryMessage() {
        return deliveryMessage;
    }

    public void setDeliveryMessage(String deliveryMessage) {
        this.deliveryMessage = deliveryMessage;
    }

    private Integer minCount;

    public Integer getMinCount() {
        return minCount;
    }

    public void setMinCount(Integer minCount) {
        this.minCount = minCount;
    }

    /**
     * 手续费
     */
    private BigDecimal poundage;

    public BigDecimal getPoundage() {
        return poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }

    public ShGameConfig() {
    }

    public ShGameConfig(Long goodsTypeId, String goodsTypeName) {
        this.goodsTypeId = goodsTypeId;
        this.goodsTypeName = goodsTypeName;
    }

    public Boolean getEnableMall() {
        return enableMall;
    }

    public void setEnableMall(Boolean enableMall) {
        this.enableMall = enableMall;
    }

    /**
     * get and set方法
     *
     * @return
     */
    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }


    public Long getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(long goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public Boolean getEnableRobot() {
        return enableRobot;
    }

    public void setEnableRobot(Boolean enableRobot) {
        this.enableRobot = enableRobot;
    }

    /**
     * get and set方法
     *
     * @return
     */
    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getUnitName() {
        if (unitName != null) {
            unitName = unitName.trim();
        }
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeTypeId() {
        return tradeTypeId;
    }

    public void setTradeTypeId(String tradeTypeId) {
        this.tradeTypeId = tradeTypeId;
    }

    public BigDecimal getMinBuyAmount() {
        return minBuyAmount;
    }

    public void setMinBuyAmount(BigDecimal minBuyAmount) {
        this.minBuyAmount = minBuyAmount;
    }
}
