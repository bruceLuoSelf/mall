package com.wzitech.gamegold.order.business.impl;

import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.common.userinfo.entity.UserInfo;
import com.wzitech.gamegold.order.business.IServiceSortManager;
import com.wzitech.gamegold.order.dao.IConfigResultInfoDBDAO;
import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
import com.wzitech.gamegold.order.dao.IServiceSortDao;
import com.wzitech.gamegold.order.entity.ServiceSort;
import com.wzitech.gamegold.usermgmt.dao.rdb.IUserInfoDBDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 寄售物服做单统计
 *
 * @author yemq
 */
@Component
public class ServiceSortManagerImpl implements IServiceSortManager {

    @Autowired
    private IServiceSortDao serviceSortDao;

    @Autowired
    private IUserInfoDBDAO userInfoDBDAO;

    @Autowired
    private IConfigResultInfoDBDAO configResultInfoDBDAO;

    @Autowired
    private IOrderInfoDBDAO orderInfoDBDAO;

    /**
     * 初始化客服待发货订单数量和最后分配时间
     *
     * @param serviceId
     */
    @Override
    @Transactional
    public void initOrderCount(Long serviceId) {
        UserInfoEO service = userInfoDBDAO.findUserById(serviceId);
        if (service.getUserType() != UserType.ConsignmentService.getCode()
                && service.getUserType() != UserType.AssureService.getCode()) {
            return;
        }

        // 查询当前客服待发货数量和最后分配的时间
        Map<String, Object> params = new HashMap<String, Object>();
        if (service.getUserType() == UserType.ConsignmentService.getCode()) {
            params.put("isConsignment", true);
        } else if (service.getUserType() == UserType.AssureService.getCode()) {
            params.put("isConsignment", false);
        }
        params.put("serviceId", serviceId);
        Map<String, Object> result = null;
        if (service.getUserType() == UserType.ConsignmentService.getCode()) {
            result = configResultInfoDBDAO.selectCurrWaitDeliveryData(params);
        } else if (service.getUserType() == UserType.AssureService.getCode()) {
            result = orderInfoDBDAO.selectCurrWaitDeliveryData(params);
        }
        long waitDeliveryCount = (Long) result.get("COUNT");
        Date lastUpdateTime = (Date) result.get("LAST_UPDATE_TIME");

        // 查询是否有该客服，没有则增加
        ServiceSort serviceSort = serviceSortDao.selectByServiceId(serviceId, true);
        if (serviceSort == null) {
            UserInfoEO u = new UserInfoEO();
            u.setId(serviceId);
            serviceSort = new ServiceSort();
            serviceSort.setService(u);
            serviceSort.setWaitDelivery(waitDeliveryCount);
            serviceSort.setLastUpdateTime(lastUpdateTime);
            serviceSortDao.insert(serviceSort);
        } else {
            serviceSort.setWaitDelivery(waitDeliveryCount);
            serviceSort.setLastUpdateTime(lastUpdateTime);
            serviceSortDao.update(serviceSort);
        }
    }

    /**
     * 根据待发货订单数由少到多，获取已排序的客服数据
     *
     * @param userType 只能传寄售或担保
     */
    @Override
    @Transactional(readOnly = true)
    public List<ServiceSort> getSortedServiceList(int userType, String gameName) {
        List<ServiceSort> list = serviceSortDao.selectSortedServiceList(userType, gameName);
        return list;
    }

    /**
     * 获取下一个该分配的客服ID
     *
     * @param userType 只能传寄售或担保
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Long getNextServiceId(int userType, String gameName) {
        return serviceSortDao.selectNextServiceId(userType, gameName);
    }

    /**
     * 客服待发货订单数量+1
     *
     * @param serviceId
     */
    @Override
    @Transactional
    public void incOrderCount(Long serviceId) {
        serviceSortDao.incOrderCount(serviceId);
    }

    /**
     * 客服待发货订单数量-1
     *
     * @param serviceId
     */
    @Override
    @Transactional
    public void decOrderCount(Long serviceId) {
        serviceSortDao.decOrderCount(serviceId);
    }


}
