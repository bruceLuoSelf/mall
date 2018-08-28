package com.wzitech.gamegold.shorder.dao;

import com.wzitech.gamegold.shorder.entity.OrderLog;

import java.util.List;

/**
 * Created by Administrator on 2017/2/17.
 */
public interface IOrderLogRedisDao {

    /**
     * 保存聊天记录
     */
    void save(OrderLog orderLog,String orderId,String userAccount);

    /**
     *查询聊天记录
     */
    public List<OrderLog> getByOrderLog(String key);

    /**
     * 删除聊天记录
     */
    void deleteChatRecord(String key,String ob,Object o);
}
