package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.business.IPayDetailManager;
import com.wzitech.gamegold.shorder.dao.IPayDetailDao;
import com.wzitech.gamegold.shorder.entity.PayDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 付款明细管理
 *
 * @author yemq
 */
@Component
public class PayDetailManagerImpl implements IPayDetailManager {
    @Autowired
    private IPayDetailDao payDetailDao;

    /**
     * 查询付款明细
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return 付款信息
     */
    @Override
    public GenericPage<PayDetail> queryListInPage(Map<String, Object> map, int start, int pageSize,
                                                  String orderBy, boolean isAsc) {
        if (map ==null) {
            map = new HashMap<String, Object>();
        }

        //查看分页参数
        if(pageSize < 1){
            throw new IllegalArgumentException("分页数pageSize需大于1");
        }

        if (start < 0) {
            throw new IllegalArgumentException("分页startIndex参数必须大于0");
        }
        return payDetailDao.selectByMap(map,pageSize,start,orderBy,isAsc);
    }

    /**
     * 创建付款明细
     *
     * @param payOrderId 关联的支付单号
     * @param chOrderId  关联的出货单号
     * @param amount     付款金额
     * @return
     */
    @Override
    @Transactional
    public PayDetail create(String payOrderId, String chOrderId, BigDecimal amount) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("payOrderId", payOrderId);
        params.put("isLocked", true);
        int count = payDetailDao.countBymap(params);
        // 生成付款明细订单号,格式：付款单号_笔数
        String newOrderId = payOrderId + "_" + (++count);

        PayDetail payDetail = new PayDetail();
        payDetail.setOrderId(newOrderId);
        payDetail.setPayOrderId(payOrderId);
        payDetail.setChOrderId(chOrderId);
        payDetail.setAmount(amount);
        payDetail.setStatus(PayDetail.WAIT_PAID);
        payDetail.setCreateTime(new Date());
        payDetailDao.insert(payDetail);
        return payDetail;
    }

    @Override
    @Transactional
    public List<PayDetail> queryByMap(Map<String, Object> paramMap) {
        return payDetailDao.selectByMap(paramMap);
    }

    @Transactional
    public void update(PayDetail payDetail) {
        payDetailDao.update(payDetail);
    }

}
