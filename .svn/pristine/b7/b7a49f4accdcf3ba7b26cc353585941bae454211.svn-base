package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.shorder.business.IStockManager;
import com.wzitech.gamegold.shorder.dao.IStockDao;
import com.wzitech.gamegold.shorder.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by 340032 on 2018/6/25.
 */
@Component
public class StockManagerImpl implements IStockManager {


    @Autowired
   private IStockDao stockDao;


    @Override
    public void update(Stock stock) {
        if (stock !=null){
            stockDao.update(stock);
        }
    }

    @Override
    public Stock queryByOrderId(Long subOrderId) {
        return stockDao.selectById(subOrderId);
    }

    @Override
    @Transactional
    public void add(Stock stock) {
        stockDao.insert(stock);
    }


}
