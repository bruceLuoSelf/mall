package com.wzitech.gamegold.goods.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.goods.business.ISevenBaoBindLogManager;
import com.wzitech.gamegold.goods.dao.SevenBaoBindLogDBDAO;
import com.wzitech.gamegold.goods.entity.SevenBaoBindLogEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 *  * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/16 wangmin				ZW_C_JB_00021 商城资金改造
 *
 */
@Component
public class SevenBaoBindLogManagerImpl extends AbstractBusinessObject implements ISevenBaoBindLogManager {
    @Autowired
    SevenBaoBindLogDBDAO sevenBaoBindLogDBDAO;

    @Override
    @Transactional
    public void add(SevenBaoBindLogEO sevenBaoBindLogEO) {
        sevenBaoBindLogEO.setUpdateTime(new Date());
        sevenBaoBindLogDBDAO.insert(sevenBaoBindLogEO);
    }

    @Override
    @Transactional
    public GenericPage<SevenBaoBindLogEO> selectLog(Map<String, Object> map, int pageSize, int start, String orderBy, Boolean isAsc) {
        return sevenBaoBindLogDBDAO.selectByMap(map,pageSize,start,orderBy,isAsc);
    }
}
