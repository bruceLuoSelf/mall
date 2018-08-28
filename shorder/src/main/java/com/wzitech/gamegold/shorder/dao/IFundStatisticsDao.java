package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.FundStatistics;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金统计
 * Created by Administrator on 2016/12/16.
 */
public interface IFundStatisticsDao extends IMyBatisBaseDAO<FundStatistics, Long> {
    /**
     * 获取最后一天的统计数据
     *
     * @return
     */
    FundStatistics queryLastDayData();

    /**
     * 按时间统计支付金额
     *
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal queryRechargeAmount(Date startTime, Date endTime);

    /**
     * 按日期统计退款金额
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal queryRefundAmount(Date startTime, Date endTime);

    /**
     * 按日期统计付款金额
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal queryPayAmount(Date startTime, Date endTime);

    /**
     * 按时间统计金币转7bao的金额
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal queryJbToZbao(Date startTime, Date endTime);
}
