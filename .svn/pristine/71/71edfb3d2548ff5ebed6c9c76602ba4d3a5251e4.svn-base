package com.wzitech.gamegold.facade.backend.business.impl;

import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.entity.BaseResponse;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.repository.entity.SendDataDTO;
import com.wzitech.gamegold.shorder.utils.ISendHttpToSevenBao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by chengXY on 2017/8/18.
 * 调用此方法创建7bao账号
 * 参数：SendDataDTO
 * 返回值：7bao账号
 */
@Component
public class CreateZBaoUtil {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ISendHttpToSevenBao sendHttpToSevenBao;

    @Value("${7bao.send.url}")
    private String sevenBaoUrl;

    @Value("${7bao.ser.key}")
    private String serKey;

    public Boolean createZBaoAccount(SendDataDTO sendDataDTO) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        sendDataDTO.setTotalAmount(BigDecimal.ZERO);
        sendDataDTO.setFreezeAmount(BigDecimal.ZERO);
        sendDataDTO.setAvailableAmount(BigDecimal.ZERO);
        JSONArray dataJson = new JSONArray();
        sendDataDTO.setDataJson(dataJson);
        //md5加密
        String format = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", stringTONUll(sendDataDTO.getLoginAccount()), sendDataDTO.getUserBind(),
                sendDataDTO.getIsShBind(), df.format(sendDataDTO.getTotalAmount()), df.format(sendDataDTO.getFreezeAmount()),
                df.format(sendDataDTO.getAvailableAmount()),
                df.format(sendDataDTO.getCheckTotalAmount()),df.format(sendDataDTO.getCheckAvailableAmount()),df.format(sendDataDTO.getCheckFreezeAmount()), sendDataDTO.getDataJson(), serKey);
        logger.info("请求绑定,解绑接口 CreateZBaoUtil:{}", format);
        String md5 = null;
        try {
            md5 = EncryptHelper.md5(format);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendDataDTO.setSign(md5);
        JSONObject jsonParam = JSONObject.fromObject(sendDataDTO);
        //发起http请求调用7bao接口
//        JSONObject jsonResult = null;
////        try {
//        jsonResult = sendHttpToSevenBao.httpPost(sevenBaoUrl, jsonParam);
        //判断返回值

//        logger.info("请求绑定,解绑接口 jsonResult:{}", jsonResult);
//        String responseStatusStr = jsonResult.getString("responseStatus");
//        JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
//        ResponseStatus responseStatus = jsonMapper.fromJson(responseStatusStr, ResponseStatus.class);
//        if (!"00".equals(responseStatus.getCode())) {
//            throw new SystemException(responseStatus.getCode(), responseStatus.getMessage());
//        }
//        }catch (Exception e){
//            logger.info("请求7bao生成7bao账号接口失败:{}",e);
//            throw  new SystemException("请求7bao生成7bao账号接口失败");
//        }

        //调用
        String response = sendHttpToSevenBao.sendHttpPost(sevenBaoUrl, jsonParam);
        JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
        BaseResponse baseResponse = jsonMapper.fromJson(response, BaseResponse.class);
        logger.info("createZBaoAccount,baseResponse,{}", baseResponse);
        if (baseResponse == null) {
            throw new SystemException(ResponseCodes.ResponseError.getCode(), ResponseCodes.ResponseError.getMessage());
        }
        ResponseStatus responseStatus = baseResponse.getResponseStatus();
        if (responseStatus == null || !responseStatus.getCode().equals("00")) {
            throw new SystemException(responseStatus.getCode(), responseStatus.getMessage());
        }

        return true;
    }

    private String stringTONUll(String str) {
        return str == null ? "" : str;
    }
}
