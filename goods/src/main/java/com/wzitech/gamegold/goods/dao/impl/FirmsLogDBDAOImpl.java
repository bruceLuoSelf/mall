package com.wzitech.gamegold.goods.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.goods.dao.IFirmsLogDBDAO;
import com.wzitech.gamegold.goods.entity.FirmsLog;
import org.springframework.stereotype.Repository;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/17  wurongfan           ZW_J_JB_00072金币商城对外接口优化
 *
 */
@Repository
public class FirmsLogDBDAOImpl extends AbstractMyBatisDAO<FirmsLog,Long> implements IFirmsLogDBDAO {
}
