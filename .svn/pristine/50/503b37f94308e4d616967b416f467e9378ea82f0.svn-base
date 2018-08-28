package com.wzitech.gamegold.common.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * 佣金工具类
 * @author yemq
 */
@Deprecated
public class CommissionUtil {

    /**
     * 收取多少佣金
     * @param createTime 下单时间
     * @param gameName 游戏名
     * @param region 游戏区
     * @return
     */
    public static double getCommission(Date createTime, String gameName, String region) {
        // 默认手续费
        double commission = 0.06;

        if ("剑灵".equals(gameName)) {
            commission = 0.06;
        } else if ("疾风之刃".equals(gameName)) {
            commission = 0.08;
        } else if ("天涯明月刀".equals(gameName)) {
            commission = 0.03;

            // 天涯明月刀，于 2015-08-24 12:00:00 开始收6%手续费
            Calendar startChargingTime = Calendar.getInstance();
            startChargingTime.set(2015, Calendar.AUGUST, 24, 12, 0, 0);
            if (createTime.getTime() >= startChargingTime.getTimeInMillis())
                commission = 0.06;
        } else if ("QQ华夏".equals(gameName)) {
            commission = 0.02;
        } else if ("斗战神".equals(gameName)) {
            commission = 0.05;
        } else if ("QQ三国".equals(gameName)) {
            commission = 0.08;
        } else if ("魔兽世界(国服)".equals(gameName)) {
            commission = 0.08;
        } else if ("剑侠情缘Ⅲ".equals(gameName)) {
            commission = 0.08;
        } else if ("龙之谷".equals(gameName)) {
            commission = 0.08;
        } else if ("地下城与勇士".equals(gameName)) {
            commission = 0.06;
        }

        return commission;
    }
}
