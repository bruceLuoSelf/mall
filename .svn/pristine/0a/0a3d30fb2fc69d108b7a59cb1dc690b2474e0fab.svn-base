package com.wzitech.gamegold.repository.business;


import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * 环信聊天
 * 孙杨 on 2017/2/15.
 */
public interface IHuanXinManager {

    void setHuanXinToken(String token);

    String getHuanXinToken();

    Long getHXTokenTimeout();

    /**
     * 注册环信用户
     */
    String registerHuanXin(Long id);

    /**
     * 根据用户账号生成环信账号
     *
     * @param userInfoEO
     */
    void registerHuanXinLoginAccount(UserInfoEO userInfoEO);

    /**
     * 批量初始化环信账号
     */
    public void registerHuanXinUserAll() throws InterruptedException;

    /**
     * 开通收货的商家批量生成环信账号和密码
     */
    void registerHuanXinByIdS() throws InterruptedException;

    public Boolean isHaveRegisterHX(SellerInfo seller);

    //查询出货商欢环信id
    public SellerInfo selectHxAccountById(String id);

    public void createChatroomId(String chHxId, String shHxId, String xtHxId, DeliveryOrder deliveryOrder);

    //注册出货商的环信账号跟密码
    public DeliveryOrder registerHuanXinByDeliveryOrder(DeliveryOrder d);

    //注册收货商的账号跟密码
    public DeliveryOrder registerHuanXinByDeliveryOrderBuyerHxAndAdminHx(DeliveryOrder d);

    //防止出现异常循环抛出异常
    public void loopCreateChatroomId(final String chHxId,final String shHxId,final String xtHxId,final DeliveryOrder deliveryOrder);

    /**
     * 注册环信
     * */
    void registerHx(DeliveryOrder deliveryOrder);
}
