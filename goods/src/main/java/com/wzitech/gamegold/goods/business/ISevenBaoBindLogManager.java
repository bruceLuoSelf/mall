package com.wzitech.gamegold.goods.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.goods.entity.SevenBaoBindLogEO;

import java.util.Map;

/**
 *  * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/16 wangmin				ZW_C_JB_00021 商城资金改造
 *
 */
public interface ISevenBaoBindLogManager {
    //添加日志信息
    public void   add(SevenBaoBindLogEO sevenBaoBindLogEO);
    //查询所有日志信息
    public GenericPage<SevenBaoBindLogEO> selectLog(Map<String, Object> map, int pageSize, int start, String orderBy, Boolean isAsc);
}
