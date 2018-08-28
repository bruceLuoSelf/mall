package com.wzitech.gamegold.goods.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.goods.business.IFirmsLogManager;
import com.wzitech.gamegold.goods.dao.IFirmInfoDBDAO;
import com.wzitech.gamegold.goods.dao.IFirmsLogDBDAO;
import com.wzitech.gamegold.goods.entity.FirmInfo;
import com.wzitech.gamegold.goods.entity.FirmsAccount;
import com.wzitech.gamegold.goods.entity.FirmsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/17  wurongfan           ZW_J_JB_00072金币商城对外接口优化
 *
 */
@Component
public class FirmsLogManagerImpl extends AbstractBusinessObject implements IFirmsLogManager {

    @Autowired
    IFirmsLogDBDAO firmsLogDBDAO;

    @Autowired
    IFirmInfoDBDAO firmInfoDBDAO;

    @Override
    public void addLog(FirmsLog firmsLog) {
        firmsLogDBDAO.insert(firmsLog);
    }

    @Override
    public GenericPage<FirmsLog> selectLog(Map<String, Object> map, int pageSize, int start, String orderBy, Boolean isAsc) {
        return firmsLogDBDAO.selectByMap(map, pageSize, start, orderBy, isAsc);
    }

    @Override
    public String createLog(IUser user, String update, FirmInfo firmInfo) {
        if (user == null || update == null || firmInfo == null) {
            throw new NullPointerException();
        }else if (update.equals("修改名称")) {
            FirmInfo selectFirmInfo = firmInfoDBDAO.selectById(firmInfo.getId());
            return user.getLoginAccount() + "对" + selectFirmInfo.getFirmsName() + "工作室" + "进行" + update + "操作" + ",修改后名称为" + firmInfo.getFirmsName();
        }else if (update.equals("权限操作")){
            FirmInfo selectFirmInfo = firmInfoDBDAO.selectById(firmInfo.getId());
            return user.getLoginAccount() + "对" + selectFirmInfo.getFirmsName() + "工作室" + "进行" + update + "," + (firmInfo.getEnabled() ? "开启了该工作室" : "关闭了该工作室");
        }
        return user.getLoginAccount() + "对" + firmInfo.getFirmsName() + "工作室" + "进行" + update + "操作";
    }

    @Override
    public String createLog(IUser user, String update, FirmsAccount firmsAccount) {
        if (user == null || update == null || firmsAccount == null) {
            throw new NullPointerException();
        }else if (update.equals("权限操作")){
            return  firmsAccount.getLoginAccount() + "用户" + "进行" + update + "," + (firmsAccount.getEnabled() ? "开启了该工作室" : "关闭了该工作室");
        }
        return firmsAccount.getLoginAccount() + "用户" + "进行" + update + "操作";
    }

    //update 操作的字段 新增 删除 更新秘钥 修改
    public void createFirmLog(String update ,FirmInfo firmInfo){
        //获取到当前登录用户
        IUser currentUser = CurrentUserContext.getUser();
        //设置当前对象
        FirmsLog firmsLog = new FirmsLog();
        firmsLog.setUserId(currentUser.getId());
        firmsLog.setModifyFirmName(firmInfo.getFirmsName());
        firmsLog.setUpdateTime(new Date());
        firmsLog.setUserType(currentUser.getUserType());
        firmsLog.setUserAccount(currentUser.getLoginAccount());
        firmsLog.setLog(this.createLog(currentUser,update,firmInfo));
        //插入当前对象
        this.addLog(firmsLog);
    }

    //update 操作的字段 新增 删除 更新秘钥 修改
    @Override
    public void createFirmsAccountLog(String update , FirmsAccount firmsAccount){
        //获取到当前登录用户
        IUser currentUser = CurrentUserContext.getUser();
        //设置当前对象
        FirmsLog firmsLog = new FirmsLog();
        firmsLog.setUserId(currentUser.getId());
        firmsLog.setModifyFirmName(firmsAccount.getFirmsName());
        firmsLog.setUpdateTime(new Date());
        firmsLog.setUserType(currentUser.getUserType());
        firmsLog.setUserAccount(currentUser.getLoginAccount());
        firmsLog.setLog(this.createLog(currentUser,update,firmsAccount));
        //插入当前对象
        this.addLog(firmsLog);
    }
}
