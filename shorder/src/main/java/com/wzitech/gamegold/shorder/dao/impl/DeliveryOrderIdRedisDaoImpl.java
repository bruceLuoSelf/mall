package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderIdRedisDao;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 通过缓存生成出货订单号
 */
@Repository
public class DeliveryOrderIdRedisDaoImpl extends AbstractRedisDAO<DeliveryOrder> implements IDeliveryOrderIdRedisDao {

    private static final String COUNT_NUMBER="gamegold:deliveryOrderCenter:countNumber";

    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

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
        StringBuffer sb = new StringBuffer("SH");
        sb.append(sdf.format(nowCal.getTime())).append(orderId);
        return sb.toString();
    }

    @Override
    public String getNewFundOrderId(){
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

        StringBuffer sb = new StringBuffer("SG");
        sb.append(sdf.format(nowCal.getTime())).append(orderId);
        return sb.toString();
    }

    public String getCount(){
        return valueOps.get(COUNT_NUMBER);
    }

    public void increment(int listSize){
        valueOps.increment(COUNT_NUMBER,listSize);
    }

    @Override
    public boolean lock(long timeout, TimeUnit timeUnit, String lockKey) {
        if (StringUtils.isNotBlank(lockKey)) {
            Boolean result = valueOps.setIfAbsent(RedisKeyHelper.jobLock(lockKey), lockKey);
            if (result != null && result.booleanValue()) {
                template.expire(RedisKeyHelper.jobLock(lockKey), timeout, timeUnit);
            }
            return result;
        }
        return false;
    }

    @Override
    public boolean unlock(String unlockKey) {
        if (StringUtils.isNotBlank(unlockKey)) {
            template.delete(RedisKeyHelper.jobLock(unlockKey));
            return true;
        }
        return false;
    }
}
