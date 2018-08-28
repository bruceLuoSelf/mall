package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.Stock;
import org.springframework.stereotype.Repository;

/**
 * 盘存表dao
 * Created by 340032 on 2018/6/20.
 */

public interface IStockDao extends IMyBatisBaseDAO<Stock,Long> {
    Stock queryByOrderIdAnSubId(String orderId,Long subId);
}
