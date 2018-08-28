package com.wzitech.gamegold.goods.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.goods.dao.SevenBaoBindLogDBDAO;
import com.wzitech.gamegold.goods.entity.SevenBaoBindLogEO;
import org.springframework.stereotype.Repository;

/**
 *  * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/16 wangmin				ZW_C_JB_00021 商城资金改造
 *
 */
@Repository
public class SevenBaoBindLogDBDAOImpl extends AbstractMyBatisDAO<SevenBaoBindLogEO,Long> implements SevenBaoBindLogDBDAO {
}
