package com.wzitech.gamegold.shorder.business;


import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.QqOnLineEO;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/10.
 */
public interface IQqOnLineManager {
    /**
     * 根据条件分页查询订单
     * @param map
     * @param orderBy
     * @param isAsc
     * @param pageSize
     * @param start
     * @return
     * @throws
     */
    GenericPage<QqOnLineEO> query(Map<String, Object> map, int pageSize, int start, String orderBy, Boolean isAsc);
    /**
     * 随机获取在线客服QQ
     * @return
     */
    QqOnLineEO getRandomQq();
    /**
     * 添加收货模式配置
     * @param qqOnLine
     * @return
     */
    QqOnLineEO addQqOnLineEO(QqOnLineEO qqOnLine);

    void deleteQqOnLineEO(Long id);

    void updateQqOnLineEO(QqOnLineEO qqOnLine);

    void enabled(Long id);

    void disabled(Long id);
}
