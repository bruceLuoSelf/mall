package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.shorder.dao.IPayOrderRedisDao;
import com.wzitech.gamegold.shorder.entity.PayOrder;
import com.wzitech.gamegold.shorder.enums.OrderPrefix;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 生成收货方付款订单号
 */
@Repository
public class PayOrderRedisDaoImpl extends AbstractRedisDAO<PayOrder> implements IPayOrderRedisDao {
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
        StringBuffer sb = new StringBuffer("SHZF");
        sb.append(sdf.format(nowCal.getTime())).append(orderId);
        return sb.toString();
    }

    /**
     * 生成新订单号
     *
     * @return
     */
    @Override
    public String getZBaoOrderId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        Date now = new Date();
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(now);

        String dateFomat = sdf.format(nowCal.getTime());
        String keyData = ORDER_ID_SEQ + dateFomat;
        //兼容老数据
        String oldCount = valueOps.get(ORDER_ID_SEQ);
        if (StringUtils.isBlank(valueOps.get(keyData)) && StringUtils.isNotBlank(oldCount)) {
            valueOps.set(keyData, oldCount);
        }
        long id = valueOps.increment(keyData, 1);
        if (id == 1L) {
            //设置缓存数据最后的失效时间为当天的最后一秒
            Calendar lastCal = Calendar.getInstance();
            lastCal.set(nowCal.get(Calendar.YEAR), nowCal.get(Calendar.MONTH), nowCal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
            lastCal.set(Calendar.MILLISECOND, 999);
            lastCal.add(Calendar.DAY_OF_MONTH, 1);
            template.expireAt(keyData, lastCal.getTime());
        }
        String orderId = StringUtils.leftPad(String.valueOf(id), 7, '0');
        StringBuffer sb = new StringBuffer(OrderPrefix.NEW_PAY_ID.getName());
        sb.append(dateFomat).append(orderId);
        return sb.toString();
    }
}
