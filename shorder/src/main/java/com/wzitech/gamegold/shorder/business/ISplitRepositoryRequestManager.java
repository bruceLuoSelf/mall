package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import com.wzitech.gamegold.shorder.entity.SplitRepositoryRequest;

import java.util.List;
import java.util.Map;

/**
 * 分仓请求管理
 */
public interface ISplitRepositoryRequestManager {

    /**
     * 创建分仓请求
     *
     * @param deliverySubOrder
     */
    void create(DeliverySubOrder deliverySubOrder);

    /**
     * 根据订单ID查询分仓请求
     *
     * @param orderId
     * @return
     */
    SplitRepositoryRequest queryByOrderId(String orderId);

    /**
     * 更改分仓订单状态
     *
     * @param orderId
     * @param status
     */
    void changeState(String orderId, int status);

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
     GenericPage<SplitRepositoryRequest> queryListInPage(Map<String, Object> map, int pageSize,int start, String orderBy, boolean isAsc);

    /**
     * 增加分仓订单
     */
    void addSplitRepositoryRequest(SplitRepositoryRequest entity);

    /**
     * 分仓结果
     * @param orderId
     * @param repsitoryCount
     * @param level
     * @param fmRepsitoryCount
     * @param fmLevel
     */
    SplitRepositoryRequest spliteResult(String orderId,Long repsitoryCount,Integer level,Long fmRepsitoryCount,Integer fmLevel);

    /**
     * 根据条件查询分仓订单列表
     * @param map
     * @return
     */
    List<SplitRepositoryRequest> selectByMap(Map<String,Object> map,String orderBy,Boolean isAsc);


    /**
     * 查出所有，导出表格用
     * */
    List<SplitRepositoryRequest> queryAllForExport(Map<String, Object> queryParam);
}

