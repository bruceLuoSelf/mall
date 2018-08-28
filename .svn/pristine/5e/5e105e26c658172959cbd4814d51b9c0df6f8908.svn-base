package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.security.Base64;
import com.wzitech.chaos.framework.server.common.security.Digests;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentIpContext;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.shorder.business.IBlackListManager;
import com.wzitech.gamegold.shorder.dao.IBalckListDao;
import com.wzitech.gamegold.shorder.entity.BlackListEO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * Created by 340032 on 2017/12/25.
 */
@Component("BlackListEOManagerImpl")
public class BlackListManagerImpl extends AbstractBusinessObject implements IBlackListManager {

    @Autowired
    IBalckListDao balckListDao;

    /**
     * 查询用户信息
     * @param queryMap
     * @param limit
     * @param start
     * @param sortBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<BlackListEO> queryUser(Map<String, Object> queryMap, int limit, int start, String sortBy, boolean isAsc) {
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "ID";
        }
        return balckListDao.selectByMap(queryMap, limit, start, sortBy, isAsc);
    }


    @Override
    @Transactional
    public void addUser1(BlackListEO blackListeo) throws SystemException {
        if (blackListeo == null) {
            throw new SystemException(ResponseCodes.UserEmptyWrong.getCode());
        }
        if (StringUtils.isBlank(blackListeo.getLoginAccount())) {
            throw new SystemException(ResponseCodes.AccountEmptyWrong.getCode());
        }
//        if (StringUtils.isBlank(blackListeo.getAddPerson())){
//            throw new SystemException(ResponseCodes.UserIsAddPerson.getCode());
//        }
            blackListeo.setAddPerson(CurrentUserContext.getUserLoginAccount());
        blackListeo.setLoginAccount(StringUtils.lowerCase(StringUtils.trim(blackListeo.getLoginAccount())));
        BlackListEO userByLoginAccount = balckListDao.findUserByLoginAccount(blackListeo.getLoginAccount());
        if ( userByLoginAccount != null) {
            throw new SystemException(ResponseCodes.ExitedAccount.getCode());
        }
        blackListeo.setCreateTime(new Date());
        blackListeo.setEnable(false);
        balckListDao.insert(blackListeo);
    }


    @Override
    @Transactional
    public void deleteUser1(Long id) throws SystemException {
        if (id == null) {
            throw new SystemException(ResponseCodes.UserIdEmptyWrong.getCode());
        }
        BlackListEO dbUser = balckListDao.findUserById(id);
        if (dbUser == null) {
            throw new SystemException(ResponseCodes.UserNotExitedWrong.getCode());
        }
        balckListDao.deleteById(id);

        logger.info("删除用户：{},操作员:{},IP:{}", new Object[]{dbUser.getLoginAccount(),
                CurrentUserContext.getUserLoginAccount(), CurrentIpContext.getIp()});
    }


    @Override
    @Transactional
    public void enableUser1(Long id) throws SystemException {
        if (id == null) {
            throw new SystemException(ResponseCodes.UserIdEmptyWrong.getCode());
        }
        BlackListEO dbUser = balckListDao.findUserById(id);
        if (dbUser == null) {
            throw new SystemException(ResponseCodes.UserNotExitedWrong.getCode());
        }
        if (!dbUser.getEnable()) {
            throw new SystemException(ResponseCodes.UserIsEnableWrong.getCode());
        }
        dbUser.setEnable(false);
        balckListDao.update(dbUser);
        logger.info("禁用用户：{},操作员:{},IP:{}", new Object[]{dbUser.getLoginAccount(),
                CurrentUserContext.getUserLoginAccount(), CurrentIpContext.getIp()});
    }


    @Override
    @Transactional
    public void disableUser1(Long id) throws SystemException {
        if (id == null) {
            throw new SystemException(ResponseCodes.UserIdEmptyWrong.getCode());
        }
        BlackListEO dbUser = balckListDao.findUserById(id);
        if (dbUser == null) {
            throw new SystemException(ResponseCodes.UserNotExitedWrong.getCode());
        }
        if (dbUser.getEnable()) {
            throw new SystemException(ResponseCodes.UserIsDisableWrong.getCode());
        }
        dbUser.setEnable(true);
        balckListDao.update(dbUser);

        logger.info("禁用用户：{},操作员:{},IP:{}", new Object[]{dbUser.getLoginAccount(),
                CurrentUserContext.getUserLoginAccount(), CurrentIpContext.getIp()});
    }
}
