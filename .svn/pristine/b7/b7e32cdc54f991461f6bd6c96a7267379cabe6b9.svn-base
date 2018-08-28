package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.FundDetail;
import com.wzitech.gamegold.shorder.entity.PayDetail;

import java.util.List;
import java.util.Map;

/**
 * 资金明细管理
 */
public interface IFundDetailManager {
    /**
     * 保存资金明细
     *
     * @param fundDetail
     */
    void save(FundDetail fundDetail);

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
    GenericPage<FundDetail> queryPage(Map<String, Object> paramMap, int pageSize, int startIndex, String orderBy,
                                      Boolean isAsc);

    /**
     * 是否存在重复充值明细
     *
     * @param loginAccount
     * @param payOrderId
     * @return
     */
    boolean isExistFundDetail(String loginAccount, String payOrderId, int type);
}
