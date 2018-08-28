package com.wzitech.gamegold.goods.business;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.log.entity.GoodsLogInfo;
import com.wzitech.gamegold.goods.entity.GoodsInfo;

import java.util.Map;

/**
 * 商品日志管理接口
 * @author yemq
 */
public interface IGoodsLogManager {

    void add(GoodsLogInfo logInfo, GoodsInfo goods);

    GenericPage<GoodsLogInfo> queryLog(Map<String, Object> queryMap, int limit, int start, String sortBy, boolean isAsc)throws SystemException;
}
