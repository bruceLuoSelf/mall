package com.wzitech.gamegold.goods.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.goods.entity.FirmInfo;
import com.wzitech.gamegold.goods.entity.FirmsAccount;
import com.wzitech.gamegold.goods.entity.FirmsLog;

import java.util.Map;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/17  wurongfan           ZW_J_JB_00072金币商城对外接口优化
 *
 */
public interface IFirmsLogManager {
    public void addLog(FirmsLog firmsLog);

    public GenericPage<FirmsLog> selectLog(Map<String, Object> map, int pageSize, int start, String orderBy, Boolean isAsc);

    String createLog(IUser user, String update, FirmInfo firmInfo);

    String createLog(IUser user, String update, FirmsAccount firmsAccount);

    //update 操作的字段 新增 删除 更新秘钥 修改
    void createFirmsAccountLog(String update, FirmsAccount firmsAccount);
}
