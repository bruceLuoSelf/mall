package com.wzitech.gamegold.goods.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.goods.dao.IFirmInfoDBDAO;
import com.wzitech.gamegold.goods.entity.FirmInfo;
import com.wzitech.gamegold.goods.entity.GoodsInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wangmin
 * Date:2017/7/5
 * 厂商信息
 */
@Repository
public class FirmInfoBDDAOImpl extends AbstractMyBatisDAO<FirmInfo, Long> implements IFirmInfoDBDAO {

}
