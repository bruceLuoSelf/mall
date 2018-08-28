package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.business.IFundDetailManager;
import com.wzitech.gamegold.shorder.dao.IFundDetailDao;
import com.wzitech.gamegold.shorder.entity.FundDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 资金明细管理
 *
 * @author yemq
 */
@Component
public class FundDetailManagerImpl implements IFundDetailManager {
    @Autowired
    private IFundDetailDao fundDetailDao;


    /**
     * 保存资金明细
     *
     * @param fundDetail
     */
    @Override
    @Transactional
    public void save(FundDetail fundDetail) {
        fundDetailDao.insert(fundDetail);
    }

    /**
     * 分页查询资金明细
     *
     * @param paramMap
     * @param pageSize
     * @param startIndex
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Transactional(readOnly = true)
    public GenericPage<FundDetail> queryPage(Map<String, Object> paramMap, int pageSize, int startIndex, String orderBy,
                                             Boolean isAsc) {
        return fundDetailDao.selectByMap(paramMap, pageSize, startIndex, orderBy, isAsc);
    }

    /**
     * 是否存在重复充值明细
     *
     * @param loginAccount
     * @param payOrderId
     * @return
     */
    @Override
    public boolean isExistFundDetail(String loginAccount, String payOrderId, int type) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("buyerAccount", loginAccount);
        paramMap.put("payOrderId", payOrderId);
        paramMap.put("type", type);
        return fundDetailDao.countByMap(paramMap) > 0;
    }
}
