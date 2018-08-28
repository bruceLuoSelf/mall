package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.shorder.dao.IRefundOrderIdRedisDao;
import com.wzitech.gamegold.shorder.entity.RefundOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 通过缓存生成退款订单号
 */
@Repository
public class RefundOrderIdRedisDaoImpl extends AbstractRedisDAO<RefundOrder> implements IRefundOrderIdRedisDao {
    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    /**
     * 生成订单号
     *
     * @return
     */
    @Override
    public String getOrderId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        Date now = new Date();
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(now);
        long id = valueOps.increment(ORDER_ID_SEQ, 1);
        if (id == 1L) {
            //设置缓存数据最后的失效时间为当天的最后一秒
            Calendar lastCal = Calendar.getInstance();
            lastCal.set(nowCal.get(Calendar.YEAR), nowCal.get(Calendar.MONTH), nowCal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
            lastCal.set(Calendar.MILLISECOND, 999);
            template.expireAt(ORDER_ID_SEQ, lastCal.getTime());
        }
        String orderId = StringUtils.leftPad(String.valueOf(id), 7, '0');
        StringBuffer sb = new StringBuffer("SHTK");
        sb.append(sdf.format(nowCal.getTime())).append(orderId);
        return sb.toString();
    }
}
