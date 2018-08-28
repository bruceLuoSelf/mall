package com.wzitech.gamegold.facade.frontend.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.order.entity.CurrencyConfigEO;

import java.util.List;

/**
 * Created by 340032 on 2018/3/19.
 */
public class CurrencyConfigResponse extends AbstractServiceResponse {


    /**
     * 返回list
     */
    private List<CurrencyConfigEO> list;
    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 商品类型
     */
    private String goodsType;

    /**
     * 字段
     */
    private String field;

    /**
     * 字段含义
     */
    private String fieldMeaning;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 最小值
     */
    private int minValue;

    /**
     * 最大值
     */
    private int maxValue;

    /**
     * 是否必填
     */
    private Boolean  enabled;

    /**
     * 值
     */
    private String value;

    /**
     * 单位
     */
    private String unitName;

    public List<CurrencyConfigEO> getList() {
        return list;
    }

    public void setList(List<CurrencyConfigEO> list) {
        this.list = list;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldMeaning() {
        return fieldMeaning;
    }

    public void setFieldMeaning(String fieldMeaning) {
        this.fieldMeaning = fieldMeaning;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
