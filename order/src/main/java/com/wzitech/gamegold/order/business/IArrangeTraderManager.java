package com.wzitech.gamegold.order.business;

import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

import java.util.List;

/**
 * 安排交易员接口
 * @author yemq
 */
public interface IArrangeTraderManager {
    /**
     * 对配单安排交易员，寄售走寄售交易，担保走担保交易
     *
     * @param configResult 配单记录
     * @param sellerLoginAccount 卖家账号
     * @param serviceId 当前选择的客服
     */
    void arrangeTrader(ConfigResultInfoEO configResult, String sellerLoginAccount, Long serviceId, String gameName, String region);

    /**
     * 获取当前所有的寄售客服
     *
     * @return List<UserInfoEO>
     */
    List<UserInfoEO> getConsignmentServices();
}
