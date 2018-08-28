package com.wzitech.gamegold.facade.backend.business;

/**
 * 卖家
 */
public interface ISellerShManager {

    void checkSh(String account, String uid, boolean isOpenSh, Long id);

    /**
     * 开通收货时候调用主站设置该用户为卖家
     * */
    void setMerChant(String uid, int code);

    String isRealName(String uid);
}
