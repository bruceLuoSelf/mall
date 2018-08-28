package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.order.business.IServiceEvaluateManager;
import com.wzitech.gamegold.order.entity.ServiceEvaluate;
import com.wzitech.gamegold.order.entity.ServiceEvaluateStatistics;
import com.wzitech.gamegold.repository.entity.SellerSetting;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评价
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class ServiceEvaluateAction extends AbstractAction {

    private ServiceEvaluateStatistics serviceEvaluateStatistics;

    private ServiceEvaluate serviceEvaluate;

    private List<ServiceEvaluateStatistics> serviceEvaluateStatisticsList;

    private List<ServiceEvaluate> serviceEvaluateList;

    @Autowired
    IServiceEvaluateManager serviceEvaluateManager;

    /**
     * 查询评价统计信息
     * @return
     */
    public String queryServiceEvaluate(){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if(serviceEvaluateStatistics!=null){
            if(!serviceEvaluateStatistics.getAccount().equals(""))
            {
                paramMap.put("account",serviceEvaluateStatistics.getAccount());
            }
            paramMap.put("startCreateTime", serviceEvaluateStatistics.getStartTime());
            paramMap.put("endCreateTime",WebServerUtil.oneDateLastTime(serviceEvaluateStatistics.getEndTime()));
        }
        GenericPage<ServiceEvaluateStatistics> genericPage = serviceEvaluateManager.statistics(paramMap, this.limit, this.start, "ID", true);
        serviceEvaluateStatisticsList = genericPage.getData();
        totalCount = genericPage.getTotalCount();

        return this.returnSuccess();
    }

    /**
     * 查询订单统计信息
     * @return
     */
    public String queryOrderEvaluate(){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if(serviceEvaluate!=null){
            int userType = CurrentUserContext.getUserType();
            if(UserType.SystemManager.getCode()!=userType){
                    paramMap.put("serviceId", CurrentUserContext.getUid());
            }
            paramMap.put("startCreateTime", serviceEvaluate.getStartTime());
            paramMap.put("endCreateTime",WebServerUtil.oneDateLastTime(serviceEvaluate.getEndTime()));
            if(serviceEvaluate.getScore()!=0){
                paramMap.put("score",serviceEvaluate.getScore());
            }
            if(!serviceEvaluate.getIsDefault()){
                paramMap.put("isDefault",false);
            }

            if(!serviceEvaluate.getOrderId().equals("")){
                paramMap.put("orderId",serviceEvaluate.getOrderId());
            }
        }
        GenericPage<ServiceEvaluate> genericPage = serviceEvaluateManager.queryServiceEvaluate(paramMap, this.limit, this.start, "CREATE_TIME", false);
        serviceEvaluateList = genericPage.getData();
        totalCount = genericPage.getTotalCount();

        return this.returnSuccess();
    }

    public List<ServiceEvaluateStatistics> getServiceEvaluateStatisticsList() {
        return serviceEvaluateStatisticsList;
    }

    public void setServiceEvaluateStatisticsList(List<ServiceEvaluateStatistics> serviceEvaluateStatisticsList) {
        this.serviceEvaluateStatisticsList = serviceEvaluateStatisticsList;
    }


    public void setServiceEvaluateStatistics(ServiceEvaluateStatistics serviceEvaluateStatistics) {
        this.serviceEvaluateStatistics = serviceEvaluateStatistics;
    }

    public ServiceEvaluateStatistics getServiceEvaluateStatistics() {
        return serviceEvaluateStatistics;
    }

    public void setServiceEvaluate(ServiceEvaluate serviceEvaluate) {
        this.serviceEvaluate = serviceEvaluate;
    }

    public ServiceEvaluate getServiceEvaluate() {
        return serviceEvaluate;
    }

    public List<ServiceEvaluate> getServiceEvaluateList() {
        return serviceEvaluateList;
    }

    public void setServiceEvaluateList(List<ServiceEvaluate> serviceEvaluateList) {
        this.serviceEvaluateList = serviceEvaluateList;
    }
}
