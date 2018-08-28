package com.wzitech.gamegold.shorder.utils;

import com.wzitech.gamegold.shorder.dao.IConfigRedisDao;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by 340032 on 2017/8/23.
 */
@Component
public class SevenBaoFund {
    @Autowired
    ISendHttpToSevenBao sendHttpToSevenBao;
    @Value("${7bao.check.url}")
    private String sevenBaoFundUrl;
    protected static final Logger logger = LoggerFactory.getLogger(SevenBaoFund.class);

    @Autowired
    IConfigRedisDao redisDao;

    public SystemConfig createFund() {
        //先从redis查询
        SystemConfig systemConfigFromRedis = redisDao.querySevenBaoFund();
        logger.info("createFund,redis,jsonResult：{}", systemConfigFromRedis);
        if (systemConfigFromRedis != null) {
            return systemConfigFromRedis;
        }

        try {
            //发起http请求调用7bao接口
            JSONObject jsonResult = sendHttpToSevenBao.httpPost(sevenBaoFundUrl, null);
            logger.info("createFund,7bao,jsonResult：{}", jsonResult);
            if (jsonResult == null) {
                return null;
            }

            //获得返回结果
            String MarginConfigKey = (String) jsonResult.get("configKey");
            String MarginConfigValue = (String) jsonResult.get("configValue");
            String AvailableFundKey = (String) jsonResult.get("availableFundKey");
            String AvailableFundValue = (String) jsonResult.get("availableFundValue");
            SystemConfig systemConfig = new SystemConfig();
            systemConfig.setConfigKey(MarginConfigKey);
            systemConfig.setConfigValue(MarginConfigValue);
            systemConfig.setAvailableFundKey(AvailableFundKey);
            systemConfig.setAvailableFundValue(AvailableFundValue);
            redisDao.saveSevenBaoFund(systemConfig);
            return systemConfig;
        } catch (Exception e) {
            logger.error("createFund出现异常：{}", e);
        }
        return null;
    }
}
