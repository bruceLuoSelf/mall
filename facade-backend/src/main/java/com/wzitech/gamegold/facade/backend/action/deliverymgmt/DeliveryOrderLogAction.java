package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.utils.DateUtil;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderLogManager;
import com.wzitech.gamegold.shorder.entity.OrderLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 出货订单日志
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class DeliveryOrderLogAction extends AbstractAction {

    private OrderLog orderLog;

    private Date startTime;

    private Date endTime;

    private List<OrderLog> orderLogList;

    @Autowired
    IDeliveryOrderLogManager deliveryOrderLogManager;

    /**
     * 根据订单号查询出货订单日志
     * @return
     */
    public String queryDeliveryOrderLog(){
        Map<String,Object> map=new HashMap<String,Object>();
        if(orderLog!=null){
            if(orderLog.getOrderId()!=null){
                map.put("orderId",orderLog.getOrderId());
            }
        }
        map.put("createStartTime", startTime);
        map.put("createEndTime", WebServerUtil.oneDateLastTime(endTime));
        GenericPage<OrderLog> page= deliveryOrderLogManager.queryByMap(map,this.limit,this.start,"create_time", false);
        orderLogList=page.getData();
        totalCount = page.getTotalCount();
        return this.returnSuccess();
}

    public OrderLog getOrderLog() {
        return orderLog;
    }

    public void setOrderLog(OrderLog orderLog) {
        this.orderLog = orderLog;
    }


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<OrderLog> getOrderLogList() {
        return orderLogList;
    }

    public void setOrderLogList(List<OrderLog> orderLogList) {
        this.orderLogList = orderLogList;
    }
}
