package com.wzitech.gamegold.order.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.order.business.ICurrencyConfigManager;
import com.wzitech.gamegold.order.dao.ICurrencyConfigDBDAO;
import com.wzitech.gamegold.order.entity.CurrencyConfigEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通货配置管理接口实现
 * Created by 340032 on 2018/3/15.
 */
@Component
public class CurrencyConfigManagerImpl extends AbstractBusinessObject implements ICurrencyConfigManager{


    @Autowired
    ICurrencyConfigDBDAO currencyConfigDBDAO;

    /**
     * 添加通货配置
     * @param currencyConfigEO
     * @return
     * @throws SystemException
     */
    @Override
    public void addCurrencyConfig(CurrencyConfigEO currencyConfigEO) throws SystemException {
        if (currencyConfigEO ==null){
            throw new SystemException(
                    ResponseCodes.EmptyGoodsInfo.getCode(),ResponseCodes.EmptyGoodsInfo.getMessage());
        }
        if (StringUtils.isEmpty(currencyConfigEO.getGameName())) {
            throw new SystemException(
                    ResponseCodes.EmptyGameName.getCode(),ResponseCodes.EmptyGameName.getCode());
        }
        if (StringUtils.isEmpty(currencyConfigEO.getGoodsType())) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsType.getCode(),ResponseCodes.EmptyGoodsType.getCode());
        }
        if (StringUtils.isEmpty(currencyConfigEO.getField())){
            throw new SystemException(
                    ResponseCodes.EmptyField.getCode(),ResponseCodes.EmptyField.getCode());
        }
        if (StringUtils.isEmpty(currencyConfigEO.getFieldMeaning())){
            throw new SystemException(
                    ResponseCodes.EmptyFieldMeaning.getCode(),ResponseCodes.EmptyFieldMeaning.getCode());
        }
        if (StringUtils.isEmpty(currencyConfigEO.getFieldType())){
            throw new SystemException(
                    ResponseCodes.EmptyFieldType.getCode(),ResponseCodes.EmptyFieldType.getCode());
        }
        if (StringUtils.isEmpty(currencyConfigEO.getFieldType())){
            throw new SystemException(
                    ResponseCodes.EmptyFieldType.getCode(),ResponseCodes.EmptyFieldType.getCode());
        }
//        if (StringUtils.isEmpty(currencyConfigEO.getValue())){
//            throw new SystemException(
//                    ResponseCodes.EmptyValue.getCode(),ResponseCodes.EmptyValue.getCode());
//        }
//        if (StringUtils.isEmpty(currencyConfigEO.getUnitName())){
//            throw new SystemException(
//                    ResponseCodes.EmptyUnitName.getCode(),ResponseCodes.EmptyUnitName.getCode());
//        }
//        if ("0".equals(currencyConfigEO.getMinValue())){
//            currencyConfigEO.setMinValue(0);
//        }
//        if ("0".equals(currencyConfigEO.getMaxValue())){
//            currencyConfigEO.setMaxValue(0);
//        }
//        currencyConfigEO.setCreateTime(new Date());
//        currencyConfigEO.setUpdateTime(new Date());
        currencyConfigDBDAO.insert(currencyConfigEO);

    }

    @Override
    public void modifyCurrencyConfig(CurrencyConfigEO currencyConfigEO) throws SystemException {
//        CurrencyConfigEO currencyConfigEO=currencyConfigDBDAO.selectConfig(id);
        if (currencyConfigEO ==null){
            throw new SystemException(
                    ResponseCodes.EmptyGoodsInfo.getCode(),ResponseCodes.EmptyGoodsInfo.getMessage());
        }
        if (StringUtils.isEmpty(currencyConfigEO.getGameName())) {
            throw new SystemException(
                    ResponseCodes.EmptyGameName.getCode(),ResponseCodes.EmptyGameName.getCode());
        }
        if (StringUtils.isEmpty(currencyConfigEO.getGoodsType())) {
            throw new SystemException(
                    ResponseCodes.EmptyGoodsType.getCode(),ResponseCodes.EmptyGoodsType.getCode());
        }
        if (StringUtils.isEmpty(currencyConfigEO.getField())){
            throw new SystemException(
                    ResponseCodes.EmptyField.getCode(),ResponseCodes.EmptyField.getCode());
        }
        if (StringUtils.isEmpty(currencyConfigEO.getFieldMeaning())){
            throw new SystemException(
                    ResponseCodes.EmptyFieldMeaning.getCode(),ResponseCodes.EmptyFieldMeaning.getCode());
        }
        if (StringUtils.isEmpty(currencyConfigEO.getFieldType())){
            throw new SystemException(
                    ResponseCodes.EmptyFieldType.getCode(),ResponseCodes.EmptyFieldType.getCode());
        }
        if (StringUtils.isEmpty(currencyConfigEO.getFieldType())){
            throw new SystemException(
                    ResponseCodes.EmptyFieldType.getCode(),ResponseCodes.EmptyFieldType.getCode());
        }
        if (StringUtils.isEmpty(currencyConfigEO.getValue())){
            throw new SystemException(
                    ResponseCodes.EmptyValue.getCode(),ResponseCodes.EmptyValue.getCode());
        }
//        if ("0".equals(currencyConfigEO.getMinValue())){
//            currencyConfigEO.setMinValue(0);
//        }
//        if ("0".equals(currencyConfigEO.getMaxValue())){
//            currencyConfigEO.setMaxValue(0);
//        }
//        currencyConfigEO.setCreateTime(new Date());
//        currencyConfigEO.setUpdateTime(new Date());
        currencyConfigDBDAO.update(currencyConfigEO);

    }

    @Override
    public void deleteCurrencyConfig(Long id) throws SystemException {
        currencyConfigDBDAO.removeByid(id);
    }
    /**
     * 分页查询
     *
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param
     * @return
     */
    @Override
    public GenericPage<CurrencyConfigEO> queryListInPage(Map<String, Object> map, int start, int pageSize, String orderBy, boolean isAsc) {
        if (null == map) {
            map = new HashMap<String, Object>();
        }

        //检查分页参数
        if (pageSize < 1) {
            throw new IllegalArgumentException("分页pageSize参数必须大于1");
        }

        if (start < 0) {
            throw new IllegalArgumentException("分页startIndex参数必须大于0");
        }

        return currencyConfigDBDAO.selectByMap(map, pageSize, start, orderBy, isAsc);
    }

    @Override
    public List<CurrencyConfigEO> queryCurrencyConfig(String gameName,String goodsType) throws SystemException {
//        if (StringUtils.isEmpty(gameName)) {
//            throw new SystemException(
//                    ResponseCodes.EmptyGameName.getCode(),ResponseCodes.EmptyGameName.getCode());
//        }
//        if (StringUtils.isEmpty(goodsType)) {
//            throw new SystemException(
//                    ResponseCodes.EmptyGoodsType.getCode(),ResponseCodes.EmptyGoodsType.getCode());
//        }

        return currencyConfigDBDAO.queryCurrencyConfig(gameName,goodsType);
    }

    @Override
    public CurrencyConfigEO selectById(Long id) {
        return currencyConfigDBDAO.selectConfig(id);
    }

    @Override
    public CurrencyConfigEO selectCurrency(CurrencyConfigEO currencyConfigEO) {

        return currencyConfigDBDAO.selectCurrency(currencyConfigEO);
    }

    @Override
    public GenericPage<CurrencyConfigEO> queryInPage(Map<String, Object> map, int start, int pageSize, String orderBy, boolean isAsc) {
        if (null == map) {
            map = new HashMap<String, Object>();
        }

        //检查分页参数
        if (pageSize < 1) {
            throw new IllegalArgumentException("分页pageSize参数必须大于1");
        }

        if (start < 0) {
            throw new IllegalArgumentException("分页startIndex参数必须大于0");
        }

        return currencyConfigDBDAO.selectByMap(map, pageSize, start, orderBy, isAsc);
    }

}
