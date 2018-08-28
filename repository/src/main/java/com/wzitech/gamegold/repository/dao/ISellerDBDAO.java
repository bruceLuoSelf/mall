/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ISellerDBDAO
 *	包	名：		com.wzitech.gamegold.repository.dao
 *	项目名称：	gamegold-repository
 *	作	者：		HeJian
 *	创建时间：	2014-2-22
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-22 下午9:55:21
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.repository.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.repository.entity.SellerInfo;

import java.util.List;
import java.util.Map;

/**
 * 卖家DB
 * @author HeJian
 *
 */
public interface ISellerDBDAO extends IMyBatisBaseDAO<SellerInfo, Long>{

	/**
	 * 
	 * <p>通过订单ID，查询订单所配置的卖家列表</p> 
	 * @author Think
	 * @date 2014-3-15 上午10:17:51
	 * @param paramMap
	 * @return
	 * @see
	 */
	List<SellerInfo> selectByOrderId(Map<String, Object> paramMap);


	/**
	 * 查询所有数据
	 *
     * @return
     */
	public GenericPage<SellerInfo> findUserList(Map<String, Object> queryMap, int limit, int start);



	GenericPage<SellerInfo> selectOpenSellerByMap(Map<String, Object> paramMap, int pageSize, int startIndex);
	
	List<SellerInfo> selectByPower();

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
	 * @param servicerIdNew
	 * @return
	 */
	void UpdateService(Long servicerId,Long servicerIdNew);

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
	 * 设置开通/关闭出货
	 *
	 * @param account
	 * @param uid
	 */
	void checkSh(String account, String uid,int openShState);
	/**
	 * 添加7bao账号
	 * */
	void updateSevenBaoAccount(String accoutn,String uid,Boolean flag);

	/**
	 * 添加已阅读
	 * @param uid
	 * @param isAgree
     */
	void updateAgree(String loginAccount,String uid,Boolean isAgree);

	/**
	 *初始化老用户 更新 绑定与阅读字段
	 * @param sellerInfo
     */
	void updateAgreeInit(List<SellerInfo> sellerInfo);

	/**
	 * 开启/关闭自动更新价格
	 * @param account
	 * @param uid
	 * @param isPriceRob
	 */
	void enablePriceRob(String account, String uid,boolean isPriceRob );

	//根据买家用户id获取环信id
	SellerInfo selectHxAccountById(String id);

	//查询所有的卖家
	List<SellerInfo> selectSellerInfoAll();

	SellerInfo findSellerDetailByLoginAccount(String account);

	List<SellerInfo> querySellerInfo(String loginAccount);
}
