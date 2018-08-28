package com.wzitech.gamegold.facade.frontend.service.order.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.service.order.ICurrencyConfigService;
import com.wzitech.gamegold.facade.frontend.service.order.dto.CurrencyConfigRequest;
import com.wzitech.gamegold.facade.frontend.service.order.dto.CurrencyConfigResponse;
import com.wzitech.gamegold.facade.frontend.service.order.dto.QueryOrderRequest;
import com.wzitech.gamegold.order.dao.ICurrencyConfigDBDAO;
import com.wzitech.gamegold.order.entity.CurrencyConfigEO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import java.util.List;

/**
 * Created by 340032 on 2018/3/19.
 */

@Service("CurrencyConfigService")
@Path("currencyConfig")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class CurrencyConfigServiceImpl extends AbstractBaseService implements ICurrencyConfigService {



    @Autowired
    ICurrencyConfigDBDAO currencyConfigDBDAO;

    //@QueryParam("")
    @Path("queryCurrencyConfig")
    @POST
    @Override
    public CurrencyConfigResponse queryCurrencyConfig(CurrencyConfigRequest currencyConfigRequest) {
        CurrencyConfigResponse response=new CurrencyConfigResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try{
            if (StringUtils.isEmpty(currencyConfigRequest.getGameName())) {
                throw new SystemException(
                        ResponseCodes.EmptyGameName.getCode(),ResponseCodes.EmptyGameName.getMessage());
            }
            List<CurrencyConfigEO> currencyConfig= currencyConfigDBDAO.queryCurrencyConfig(currencyConfigRequest.getGameName(),currencyConfigRequest.getGoodsType());
            if (currencyConfig==null  || currencyConfig.size()==0){
                throw new SystemException(
                        ResponseCodes.NotAvailableGameConfig.getCode(),ResponseCodes.NotAvailableGameConfig.getCode());
            }

//            if (StringUtils.isEmpty(currencyConfig.getGoodsType())) {
//                throw new SystemException(
//                        ResponseCodes.EmptyGoodsType.getCode(),ResponseCodes.EmptyGoodsType.getCode());
//            }
//            if (StringUtils.isEmpty(currencyConfig.getField())){
//                throw new SystemException(
//                        ResponseCodes.EmptyField.getCode(),ResponseCodes.EmptyField.getCode());
//            }
//            if (StringUtils.isEmpty(currencyConfig.getFieldMeaning())){
//                throw new SystemException(
//                        ResponseCodes.EmptyFieldMeaning.getCode(),ResponseCodes.EmptyFieldMeaning.getCode());
//            }
//            if (StringUtils.isEmpty(currencyConfig.getFieldType())){
//                throw new SystemException(
//                        ResponseCodes.EmptyFieldType.getCode(),ResponseCodes.EmptyFieldType.getCode());
//            }
//            if (StringUtils.isEmpty(currencyConfig.getFieldType())){
//                throw new SystemException(
//                        ResponseCodes.EmptyFieldType.getCode(),ResponseCodes.EmptyFieldType.getCode());
//            }
//            if (StringUtils.isEmpty(currencyConfig.getValue())){
//                throw new SystemException(
//                        ResponseCodes.EmptyValue.getCode(),ResponseCodes.EmptyValue.getCode());
//            }
//            if (StringUtils.isEmpty(currencyConfig.getUnitName())){
//                throw new SystemException(
//                        ResponseCodes.EmptyUnitName.getCode(),ResponseCodes.EmptyUnitName.getCode());
//            }
//            if ("0".equals(currencyConfig.getMinValue())){
//                currencyConfig.setMinValue(0);
//            }
//            if ("0".equals(currencyConfig.getMaxValue())){
//                currencyConfig.setMaxValue(0);
//            }
//            response.setGoodsType(currencyConfig.getGoodsType());
//            response.setField(currencyConfig.getField());
//            response.setFieldMeaning(currencyConfig.getFieldMeaning());
//            response.setFieldType(currencyConfig.getFieldType());
//            response.setMinValue(currencyConfig.getMinValue());
//            response.setMaxValue(currencyConfig.getMaxValue());
//            response.setEnabled(currencyConfig.getEnabled());
//            response.setUnitName(currencyConfig.getUnitName());
//            response.setValue(currencyConfig.getValue());
            response.setList(currencyConfig);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
            return response;
        }catch (SystemException ex){
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("当前查询通货配置发生异常:{}", ex);
        }catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(
                    ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("当前查询通货配置发生未知异常:{}", ex);
        }
        logger.debug("当前查询通货配置响应信息:{}", response);
        return null;
    }
}
