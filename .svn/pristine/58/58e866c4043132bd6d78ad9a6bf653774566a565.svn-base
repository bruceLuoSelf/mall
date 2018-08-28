/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ISellerManager
 *	包	名：		com.wzitech.gamegold.repository.business
 *	项目名称：	gamegold-repository
 *	作	者：		HeJian
 *	创建时间：	2014-2-22
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-22 下午9:58:04
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.repository.business;

import java.util.List;
import java.util.Map;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.repository.entity.JBPayOrderTo7BaoEO;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.shorder.entity.PurchaserData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 卖家管理接口
 * @author HeJian
 *
 */
public interface ISellerManager {
	/**
	 * 申请成为卖家
	 * @param seller
	 * @return
	 * @throws SystemException
	 */
	SellerInfo applySeller (SellerInfo seller) throws SystemException;


	SellerInfo modifySeller (Long id,String loginAccount,String shopName,String name,String phoneNumber,String qq,String games,String notes) throws SystemException;

	/**
	 * 修改卖家
	 * @param sellerInfo
	 * @return
	 * @throws SystemException
	 */
	SellerInfo modifySeller(SellerInfo sellerInfo) throws SystemException;

	/**
	 * 删除卖家
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	SellerInfo deleteSeller(Long id) throws SystemException;

	/**
	 * 开通收货的批量生成环信账号
	 * @throws SystemException
     */
	void MassProductionAccount() throws SystemException, InterruptedException;

	/**
	 * 根据条件分页查询卖家信息
	 * @param queryMap
	 * @param limit
	 * @param start
	 * @param sortBy
	 * @param isAsc
	 * @return
	 * @throws SystemException
	 */
	GenericPage<SellerInfo> querySeller(Map<String, Object> queryMap, int limit, int start,
			String sortBy, boolean isAsc)throws SystemException;

	/**
	 * 根据用户ID查询店铺
	 * @param uid
	 * @return
	 * @throws SystemException
	 */
	SellerInfo findSellerByUid(Long uid)throws SystemException;

	/**
	 *
	 * <p>通过卖家ID，进行审核</p>
	 * @author ztjie
	 * @date 2014-2-23 下午7:40:17
	 * @param seller
	 * @see
	 */
	void auditSeller(SellerInfo seller);

	/**
	 * 根据登录帐号查询卖家信息
	 * @param loginAccount
	 * @throws SystemException
	 * @return
	 */
	SellerInfo querySellerInfo(String loginAccount)throws SystemException;

	/**
	 *
	 * <p>通过订单ID，查询订单所配置的卖家列表</p>
	 * @author Think
	 * @date 2017/05/17  zhujun 增加商品类型查询
	 * @param orderId goodsTypeName
	 * @return
	 * @see
	 */
	List<SellerInfo> querySellerByOrderId(String orderId, String goodsTypeName);
	/**
	 * 取消短信权限
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	SellerInfo cancelPower(Long id) throws SystemException;

	/**
	 * 赋予短信权限
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	SellerInfo givePower(Long id) throws SystemException;

	/**
	 * 开启新资金流程
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	SellerInfo openNewFund(Long id) throws SystemException;

	/**
	 * 关闭新资金流程
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	SellerInfo closeNewFund(Long id) throws SystemException;

	/**
	 * 开启合区合服
	 * @param id
	 * @throws SystemException
     */
	void openCross(Long id)throws SystemException;
	/**
	 * 关闭合区合服
	 * @param id
	 * @throws SystemException
     */
	void closeCross(Long id)throws SystemException;
	/**
	 * 取消屏蔽
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	SellerInfo cancelShield(Long id) throws SystemException;

	/**
	 * 屏蔽卖家
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	SellerInfo shieldSeller(Long id) throws SystemException;
	/**
	 * 设置人工处理库存状态
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	SellerInfo giveManualStatus(Long id) throws SystemException;
	/**
	 * 取消人工处理库存状态
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	SellerInfo cancelManualStatus(Long id) throws SystemException;

	/**
	 * 是否寄售
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	SellerInfo saveShieldSeller(Long id,Boolean isShield,Boolean isHelper) throws SystemException;

	/**
	 * 根据卖家账号和ID获取卖家信息
	 * @param account
	 * @param uid
	 * @return
	 */
	SellerInfo findByAccountAndUid(String account, String uid);

	/**
	 * 更新客服
	 *
	 * @param servicerId
	 * @return
	 */
	void UpdateService(Long servicerId);

	/**
	 * 设置卖家为上线
	 * @param account
	 * @param uid
	 */
	void online(String account, String uid);

	/**
	 * 设置卖家为下线
	 * @param account
	 * @param uid
	 */
	void offline(String account, String uid);

	/**
	 * 设置卖家为下线，后台管理员使用
	 *
	 * @param account
	 * @param uid
	 */
	 void offlineBack(String account, String uid);

	/**
	 * 设置开通/关闭出货
	 *
	 * @param account
	 * @param uid
	 */
	 void checkSh(String account, String uid,int openShState);

	/**
	 * 开启/关闭自动更新价格
	 * @param account
	 * @param uid
	 * @param isPriceRob
	 */
	void enablePriceRob(String account, String uid,boolean isPriceRob );

	/**
	 * 账号精确查询卖家信息
	 * @param loginAccount
	 * @return
	 */
	SellerInfo queryloginAccountNotLike(String loginAccount);

	/**
	 * 初始化老用户更新数据库
	 * @param sellerInfo
     */
	void updateAgrre(List<SellerInfo> sellerInfo);

	/**
	 * 根据用户,判断是否有为结完订单
	 * 老资金转 7bao
	 * @param userLoginAccount
	 * @return
     */
//	PurchaserData initAmount(Long id, String userLoginAccount);

//	JSONArray getData(Long id, String userLoginAccount,PurchaserData purchaserData);

	public List<JBPayOrderTo7BaoEO> updatePurchaserData(String userLoginAccount, PurchaserData purchaserData);

	/**
	 * 根据卖家登录账号查询卖家详情
	 * */
	SellerInfo findSellerDetailByLoginAccount(String loginAccount);

}
