/************************************************************************************
 * Copyright 2012 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		AccountServiceImpl
 * 包	名：		com.wzitech.chinabeauty.facade.service.usermgmt.impl
 * 项目名称：	chinabeauty-facade-frontend
 * 作	者：		SunChengfei
 * 创建时间：	2013-9-26
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2013-9-26 下午2:51:45
 ************************************************************************************/
package com.wzitech.gamegold.facade.backend.action.usermgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.common.userinfo.entity.UserInfo;
import com.wzitech.gamegold.facade.backend.contant.WebServerContants;
import com.wzitech.gamegold.facade.backend.extjs.AbstractFileUploadAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.filemgmt.business.IFileManager;
import com.wzitech.gamegold.order.business.IServiceSortManager;
import com.wzitech.gamegold.repository.business.IHuanXinManager;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.shorder.business.IUserModifiActionLogManager;
import com.wzitech.gamegold.shorder.entity.UserModifiActionLog;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("prototype")
@ExceptionToJSON
public class UserAction extends AbstractFileUploadAction {

    private static final long serialVersionUID = 7158706093540959280L;

    private UserInfoEO user;

    // 封装上传文件域的属性
    private File avatarUrl;

    // 封装上传文件名的属性
    private String avatarUrlFileName;

    private Long id;

    private Boolean mainAccountIdNotNull;

    private List<UserInfoEO> userList;

    @Autowired
    IUserInfoManager userInfoManager;

    @Autowired
    IServiceSortManager serviceSortManager;

    @Autowired
    IFileManager fileManager;

    @Autowired
    ISellerManager sellerManager;
    @Autowired
    IHuanXinManager huanXinManager;

    @Autowired
    IUserModifiActionLogManager userModifiActionLogManager;

    /**
     * 查询用户列表
     * @return
     */
    public String queryUser() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (user != null) {
            queryMap.put("userType", user.getUserType());
            IUser currentUser = CurrentUserContext.getUser();
            if (currentUser.getId() != null) {
                if ((currentUser.getUserType() == UserType.AssureService.getCode()) || (currentUser.getUserType() == UserType.ConsignmentService.getCode())) {
                    queryMap.put("mainAccountId", currentUser.getId());
                }
            } else {
                queryMap.put("mainAccountId", null);
            }
            if (StringUtils.isNotBlank(user.getLoginAccount())) {
                queryMap.put("loginAccount", user.getLoginAccount().trim());
            }
            if (StringUtils.isNotBlank(user.getNickName())) {
                queryMap.put("nickName", user.getNickName().trim());
            }
            if (StringUtils.isNotBlank(user.getRealName())) {
                queryMap.put("realName", user.getRealName().trim());
            }
        }
        if (mainAccountIdNotNull != null) {
            queryMap.remove("mainAccountId");
            queryMap.put("mainAccountIdNotNull", mainAccountIdNotNull);
        }
        GenericPage<UserInfoEO> genericPage = userInfoManager.queryUser(queryMap, this.limit, this.start, null, true);
        userList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 新增后台管理用户
     * @return
     * @throws IOException
     */
    public String addUser() throws IOException {
        try {
            IUser currentUser = CurrentUserContext.getUser();
            if (currentUser.getId() != null) {
                if ((currentUser.getUserType() == UserType.AssureService.getCode()) || (currentUser.getUserType() == UserType.ConsignmentService.getCode())) {
                    user.setMainAccountId(currentUser.getId());
                }
            }
            createAvataUrl();
            userInfoManager.addUser(user);
            //添加记录日志
            UserModifiActionLog userModifiActionLog = new UserModifiActionLog();
            userModifiActionLog.setUserId(currentUser.getId());
            userModifiActionLog.setUserType(currentUser.getUserType());
            userModifiActionLog.setModifiUserId(user.getId());
            userModifiActionLog.setUserAccount(currentUser.getLoginAccount());
            userModifiActionLog.setLog("添加了一条登录名邮箱为"+user.getLoginAccount()+"的数据，操作人是" + currentUser.getLoginAccount());
            userModifiActionLog.setUpdateTime(new Date());
            userModifiActionLogManager.addLog(userModifiActionLog);
//            UserInfoEO userInfoEO = userInfoManager.findUserByAccount(user.getLoginAccount());
//            if (userInfoEO != null) {
//                huanXinManager.registerHuanXinLoginAccount(userInfoEO);
//            }
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 修改后台管理用户
     * @return
     * @throws IOException
     */
    public String modifyUser() throws IOException {
        try {
            IUser currentUser = CurrentUserContext.getUser();
            if (currentUser.getId() != null) {
                if ((currentUser.getUserType() == UserType.AssureService.getCode()) || (currentUser.getUserType() == UserType.ConsignmentService.getCode())) {
                    user.setMainAccountId(currentUser.getId());
                }
            }
            createAvataUrl();
            user.setId(id);
            //修改用户信息
            userInfoManager.modifyUserInfo(user);
            //添加记录日志
            UserModifiActionLog userModifiActionLog = new UserModifiActionLog();
            userModifiActionLog.setUserId(currentUser.getId());
            userModifiActionLog.setUserType(currentUser.getUserType());
            userModifiActionLog.setModifiUserId(user.getId());
            userModifiActionLog.setLog("修改了一条数据,这条数据登录邮箱是"+user.getLoginAccount()+",操作人是" + currentUser.getLoginAccount());
            userModifiActionLog.setUpdateTime(new Date());
            userModifiActionLog.setUserAccount(currentUser.getLoginAccount());
            userModifiActionLogManager.addLog(userModifiActionLog);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String modifyPwd() throws IOException {
        try {
            user.setId(id);
            userInfoManager.modifyPwd(user);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 管理员修改子账号信息
     * @return
     * @throws IOException
     */
    public String adminModifySubUser() throws IOException {
        try {
            userInfoManager.modifyUserInfo(user);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    private void createAvataUrl() throws IOException {
        if (avatarUrl != null) {
            if (!FilenameUtils.isExtension(avatarUrlFileName.toLowerCase(), WebServerContants.IMAGE_EXTENSIONS)) {
                throw new SystemException(ResponseCodes.ImageTypeWrong.getCode());
            }
            if (StringUtils.isBlank(user.getLoginAccount())) {
                throw new SystemException(ResponseCodes.AccountEmptyWrong.getCode());
            }
            String[] uris = fileManager.saveAvatar(WebServerUtil.changeFileToByteArray(avatarUrl), user.getLoginAccount());
            if (StringUtils.isBlank(uris[0])) {
                throw new SystemException(ResponseCodes.UserThumbWrong.getCode());
            }
            user.setAvatarUrl(uris[0]);
        }
    }

    /**
     * 启用用户
     * @return
     */
    public String enableUser() {
        try {
            userInfoManager.enableUser(id);
            serviceSortManager.initOrderCount(id);
            UserInfoEO userInfo = userInfoManager.selectUserById(id);
            UserModifiActionLog userModifiActionLog = new UserModifiActionLog();
            userModifiActionLog.setUserId(CurrentUserContext.getUser().getId());
            userModifiActionLog.setUserType(CurrentUserContext.getUser().getUserType());
            userModifiActionLog.setModifiUserId(id);
            userModifiActionLog.setLog("启用了一条登录名邮箱为"+userInfo.getLoginAccount()+"的数据，操作人是" + CurrentUserContext.getUser().getLoginAccount());
            userModifiActionLog.setUpdateTime(new Date());
            userModifiActionLog.setUserAccount(CurrentUserContext.getUser().getLoginAccount());
            userModifiActionLogManager.addLog(userModifiActionLog);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 禁用用户
     * @return
     */
    public String disableUser() {
        try {
            userInfoManager.disableUser(id);
            //根据这个id找到对应的数据
            UserInfoEO userInfo = userInfoManager.selectUserById(id);
            UserModifiActionLog userModifiActionLog = new UserModifiActionLog();
            userModifiActionLog.setUserId(CurrentUserContext.getUser().getId());
            userModifiActionLog.setUserType(CurrentUserContext.getUser().getUserType());
            userModifiActionLog.setModifiUserId(id);
            userModifiActionLog.setLog("禁用了一条登录邮箱为"+userInfo.getLoginAccount()+"的数据，操作人是" + CurrentUserContext.getUser().getLoginAccount());
            userModifiActionLog.setUpdateTime(new Date());
            userModifiActionLog.setUserAccount(CurrentUserContext.getUser().getLoginAccount());
            userModifiActionLogManager.addLog(userModifiActionLog);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 删除用户
     * @return
     */
    public String deleteUser() {
        try {
            //userInfoManager.deleteUser(id);
//			UserInfoEO userInfoEO=userInfoManager.findRZServicer();
//			if(userInfoEO!=null){
//				Long servicerIdNew=userInfoEO.getId();
//				sellerManager.UpdateService(id,servicerIdNew);
//			}
            UserInfoEO userInfo = userInfoManager.selectUserById(id);
            sellerManager.UpdateService(id);
            //添加记录日志
            UserModifiActionLog userModifiActionLog = new UserModifiActionLog();
            userModifiActionLog.setUserId(CurrentUserContext.getUser().getId());
            userModifiActionLog.setUserType(CurrentUserContext.getUser().getUserType());
            userModifiActionLog.setModifiUserId(id);
            userModifiActionLog.setLog("删除了一条登录名邮箱为"+userInfo.getLoginAccount()+"的数据，操作人是" + CurrentUserContext.getUser().getLoginAccount());
            userModifiActionLog.setUpdateTime(new Date());
            userModifiActionLog.setUserAccount(CurrentUserContext.getUser().getLoginAccount());
            userModifiActionLogManager.addLog(userModifiActionLog);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 初始化环信账号
     *
     * @return
     */
    public String productionAccount() throws InterruptedException {
        try {
//            huanXinManager.registerHuanXinUserAll();
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public List<UserInfoEO> getUserList() {
        return userList;
    }

    public void setUser(UserInfoEO user) {
        this.user = user;
    }

    public UserInfoEO getUser() {
        return user;
    }

    public void setAvatarUrl(File avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setAvatarUrlFileName(String avatarUrlFileName) {
        this.avatarUrlFileName = avatarUrlFileName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMainAccountIdNotNull(Boolean mainAccountIdNotNull) {
        this.mainAccountIdNotNull = mainAccountIdNotNull;
    }

}
