package com.wzitech.gamegold.goods.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.goods.business.ICheckRepositoryManageRedisManager;
import com.wzitech.gamegold.goods.dao.ICheckRespostitoryManageRedisDAO;
import com.wzitech.gamegold.goods.dao.IFirmsAccountDBDAO;
import com.wzitech.gamegold.goods.entity.FirmInfo;
import com.wzitech.gamegold.goods.entity.FirmsAccount;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/7  wurongfan           ZW_J_JB_00072金币商城对外接口优化
 */
@Component
public class CheckRepositoryManageRedisManagerImple implements ICheckRepositoryManageRedisManager {

    @Autowired
    private ICheckRespostitoryManageRedisDAO checkRepostoryManageRedsiDao;

    @Autowired
    private IFirmsAccountDBDAO firmsAccountDBDAO;

    @Override
    public Integer addFirmAndLoginAccountAndUid(String uuid, String firmsSecret, String loginAccount, String uid) {
        return checkRepostoryManageRedsiDao.saveFirmAndLoginAccount(uuid, firmsSecret, loginAccount, uid);
    }

    /**
     * 将获取的redis对象转换为实体对象
     *
     * @param cookie
     * @return
     */
    @Override
    public FirmsAccount getRedisValue(String cookie) {
        if (StringUtils.isBlank(cookie)) {
            throw new SystemException(ResponseCodes.ParamNotEmpty.getCode(), ResponseCodes.ParamNotEmpty.getMessage());
        }
        String redisValue = checkRepostoryManageRedsiDao.getRedisValue(cookie);
        if (StringUtils.isBlank(redisValue)) {
            throw new SystemException(ResponseCodes.RedisTokenExpire.getCode(), ResponseCodes.RedisTokenExpire.getMessage());
        }
        //将获取的json格式的数据转换为实体对象
        JSONObject jsonObject = JSONObject.fromObject(redisValue);
        FirmsAccount firmsAccount = (FirmsAccount) jsonObject.toBean(jsonObject, FirmsAccount.class);
        return firmsAccount;
    }

    @Override
    public String checkIn(String firms, String appid, String loginAccount) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("firmsSecret", firms);
        paramMap.put("loginAccount", loginAccount);
        paramMap.put("userSecretKey", appid);
        List<FirmsAccount> firmsAccount = firmsAccountDBDAO.selectByMap(paramMap);
        if (CollectionUtils.isEmpty(firmsAccount) || firmsAccount.size() != 1) {
            throw new SystemException(ResponseCodes.ParamInfoIsEmpty.getCode(), ResponseCodes.ParamInfoIsEmpty.getMessage());
        }
        String token = checkRepostoryManageRedsiDao.saveFirmAndLoginAccount(loginAccount);
        return token;
    }

    @Override
    public Boolean checkOut(String loginAccount, String token) {
        String tokenString = checkRepostoryManageRedsiDao.getToken(loginAccount);
        if (StringUtils.isBlank(tokenString)) {
            return false;
        }
        if (!StringUtils.equals(token, tokenString)) {
            return false;
        }
        return true;
    }
}
