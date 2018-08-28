package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.business.ISplitRepositorySubRequestManager;
import com.wzitech.gamegold.shorder.dao.ISplitRepositorySubRequestDao;
import com.wzitech.gamegold.shorder.entity.SplitRepositoryRequest;
import com.wzitech.gamegold.shorder.entity.SplitRepositorySubRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ljn
 * @date 2018/6/22.
 */
@Service
public class SplitRepositorySubRequestManagerImpl implements ISplitRepositorySubRequestManager {

    @Autowired
    private ISplitRepositorySubRequestDao splitRepositorySubRequestDao;

    /**
     * 根据主订单号查询子订单列表
     * @param orderId
     * @return
     */
    @Override
    public List<SplitRepositorySubRequest> getSubOrderList(String orderId) {
        return splitRepositorySubRequestDao.getSubOrderList(orderId);
    }

    /**
     * 分页查找分仓订单
     *
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<SplitRepositorySubRequest> queryListInPage(Map<String, Object> map, int pageSize, int start, String orderBy, boolean isAsc) {
        if (null == map) {
            map = new HashMap<String, Object>();
        }
        //检查分页参数
        if (pageSize < 1) {
            throw new IllegalArgumentException("分页pageSize参数必须大于1");
        }
        if (start < 0) {
            throw new IllegalArgumentException("分页startIndex参数必须大于0");
        }
        return splitRepositorySubRequestDao.selectByMap(map, pageSize, start, orderBy, isAsc);
    }
}
