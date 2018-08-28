package com.wzitech.gamegold.repository.util;

import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.repository.business.IHttpToSevenBao;
import com.wzitech.gamegold.repository.entity.AccountIntDto;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by 340032 on 2017/8/26.
 */
@Component
public class AccountZBaoUtil {

    /**
     * 日志输出
     */
    private static final Logger logger = LoggerFactory.getLogger(AccountZBaoUtil.class);

    @Autowired
    IHttpToSevenBao HttpToSevenBao;

    @Value("${7bao.Account.url}")
    private String sevenBaoUrl;
    @Value("${7bao.ser.key}")
    private String serKey;

    public String createZBaoAccount(AccountIntDto accountIntDto) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        logger.info("创建修改的7Bao用户:{}", accountIntDto);
        //md5加密
        String format = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", accountIntDto.getLoginAccount(), accountIntDto.getQq(),
                accountIntDto.getName(), accountIntDto.getUserBind(), accountIntDto.getUid(), accountIntDto.getPhoneNumber(),
                df.format(accountIntDto.getTotalAmountBao()), accountIntDto.getFreezeAmountBao(), df.format(accountIntDto.getAvailableAmountBao()),
                stringTONUll(accountIntDto.getApplyTime() == null ? "" : accountIntDto.getApplyTime().toString()), accountIntDto.getIsShBind(),
                accountIntDto.getDataJson(), serKey);
        logger.info("初始化老用户签名{}", format);
        String md5 = null;
        try {
            md5 = EncryptHelper.md5(format);
        } catch (IOException e) {
            e.printStackTrace();
        }
        accountIntDto.setSign(md5);
        JSONObject jsonParam = JSONObject.fromObject(accountIntDto);
//        //发起http请求调用7bao接口
//        JSONObject jsonResult = HttpToSevenBao.httpPost(sevenBaoUrl, jsonParam);
//        logger.info("调7bao返回值:{}",jsonResult);
//        //判断返回值
//        String responseStatusStr = jsonResult.getString("responseStatus");
//        JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
//        ResponseStatus responseStatus = jsonMapper.fromJson(responseStatusStr, ResponseStatus.class);
//        logger.info("响应代码{}",responseStatus.getCode());
//        if(!"00".equals(responseStatus.getCode())){
//            throw new SystemException(responseStatus.getCode(), responseStatus.getMessage());
//        }
//        if (jsonResult != null) {
//            //获得返回结果
//            String sevenBaoAccount = jsonResult.getString("zbaoLoginAccount");
//            return sevenBaoAccount;
//        }

        String response = HttpToSevenBao.sendHttpPost(sevenBaoUrl, jsonParam);
        JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
        ZBaoAccountResponse accountResponse = jsonMapper.fromJson(response, ZBaoAccountResponse.class);
        logger.info("createZBaoAccount,baseResponse,{}", accountResponse);
        if (accountResponse == null) {
            throw new SystemException(ResponseCodes.ResponseError.getCode(), ResponseCodes.ResponseError.getMessage());
        }
        ResponseStatus responseStatus = accountResponse.getResponseStatus();
        if (responseStatus == null || !responseStatus.getCode().equals("00")) {
            throw new SystemException(responseStatus.getCode(), responseStatus.getMessage());
        }
        return accountResponse.getZbaoLoginAccount();
    }

    private String stringTONUll(String str) {
        return str == null ? "" : str;
    }
}
