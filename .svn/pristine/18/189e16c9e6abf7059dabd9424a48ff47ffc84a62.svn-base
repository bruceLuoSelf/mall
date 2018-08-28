package com.wzitech.gamegold.facade.frontend.utils;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.facade.frontend.dto.ZBaoDTO;
import com.wzitech.gamegold.shorder.utils.ISendHttpToSevenBao;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by chengXY on 2017/8/21.
 */


@Component
public class ConZBaoUtil {
    @Autowired
    ISendHttpToSevenBao sendHttpToSevenBao;

    @Value("${7bao.send.url}")
    private String sevenBaoUrl;

    @Value("${7bao.ser.key}")
    private String serKey;

    /**
     * 日志记录器
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    public String createZBaoAccount(ZBaoDTO sendDataDTO){
        //md5加密
        String format = String.format("%s_%s_%s_%s_%s_%s_%s", sendDataDTO.getLoginAccount(),sendDataDTO.getQq(),
                sendDataDTO.getName(),sendDataDTO.getUserBind(),sendDataDTO.getUid(),sendDataDTO.getPhoneNumber(),
                serKey);
        String md5 = null;
        try {
            md5 = EncryptHelper.md5(format);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendDataDTO.setSign(md5);
        JSONObject jsonParam = JSONObject.fromObject(sendDataDTO);
        if (jsonParam==null) {
            logger.info("解绑,绑定,传7bao参数不能为空:{}",jsonParam);
        throw new SystemException(ResponseCodes.EmptyParams.getCode(), ResponseCodes.EmptyParams.getMessage());
        }
            //发起http请求调用7bao接口
            JSONObject jsonResult = sendHttpToSevenBao.httpPost(sevenBaoUrl, jsonParam);
            //获得返回结果
        if (jsonResult==null ){
            logger.info("7bao返回参数不能为空:{}",jsonResult);
            throw new SystemException(ResponseCodes.EmptyParams.getCode(), ResponseCodes.EmptyParams.getMessage());
        }
            String sevenBaoAccount = (String) jsonResult.get("zbaoLoginAccount");
            return sevenBaoAccount;

    }
}



