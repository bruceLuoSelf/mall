package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.shorder.business.IFundStatisticsManager;
import com.wzitech.gamegold.shorder.entity.FundStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**资金统计
 * Created by Administrator on 2016/12/16.
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class ShFundStatisticsAction extends AbstractAction {

    private FundStatistics fundStatistics;

    private List<FundStatistics> shFundStatisticsList;

    private Date startTime;

    private Date endTime;
    @Autowired
    private IFundStatisticsManager fundStatisticsManager;

    public String queryShFundStatistics(){
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("createStartTime", startTime);
        queryMap.put("createEndTime", WebServerUtil.oneDateLastTime(endTime));
        GenericPage<FundStatistics> genericPage= fundStatisticsManager.queryListInPage(queryMap, this.start,this.limit,"start_time", false);
        shFundStatisticsList=genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
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

    public FundStatistics getFundStatistics() {
        return fundStatistics;
    }

    public void setFundStatistics(FundStatistics fundStatistics) {
        this.fundStatistics = fundStatistics;
    }

    public List<FundStatistics> getShFundStatisticsList() {
        return shFundStatisticsList;
    }

    public void setShFundStatisticsList(List<FundStatistics> shFundStatisticsList) {
        this.shFundStatisticsList = shFundStatisticsList;
    }

}
