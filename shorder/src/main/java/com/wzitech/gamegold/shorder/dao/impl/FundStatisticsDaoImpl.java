package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IFundStatisticsDao;
import com.wzitech.gamegold.shorder.entity.FundStatistics;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**资金统计
 * Created by Administrator on 2016/12/16.
 */
@Repository
public class FundStatisticsDaoImpl extends AbstractMyBatisDAO<FundStatistics, Long> implements IFundStatisticsDao{

    /**
     * 获取最后一天的统计数据
     *
     * @return
     */
    @Override
    public FundStatistics queryLastDayData() {
        return getSqlSession().selectOne(getMapperNamespace() + ".queryLastDayData");
    }

    /**
     * 按时间统计支付金额
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public BigDecimal queryRechargeAmount(Date startTime, Date endTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectOne(getMapperNamespace() + ".queryRechargeAmount", params);
    }

    /**
     * 按日期统计退款金额
     * @param startTime
     * @param endTime
     * @return
     */
    public BigDecimal queryRefundAmount(Date startTime, Date endTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectOne(getMapperNamespace() + ".queryRefundAmount", params);
    }

    /**
     * 按日期统计付款金额
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public BigDecimal queryPayAmount(Date startTime, Date endTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectOne(getMapperNamespace() + ".queryPayAmount", params);
    }

    @Override
    public BigDecimal queryJbToZbao(Date startTime, Date endTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectOne(getMapperNamespace() + ".queryJbToZbao", params);
    }
}
