/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		SellerDBDAOImpl
 *	包	名：		com.wzitech.gamegold.repository.dao.impl
 *	项目名称：	gamegold-repository
 *	作	者：		HeJian
 *	创建时间：	2014-2-22
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-22 下午9:56:23
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.repository.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.repository.dao.ISellerDBDAO;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 卖家DB实现
 * @author HeJian
 *
 */
@Repository
public class SellerDBDAOImpl extends AbstractMyBatisDAO<SellerInfo, Long> implements ISellerDBDAO{



	@Override
	public List<SellerInfo> selectByOrderId(Map<String, Object> paramMap) {
		return this.getSqlSession().selectList(getMapperNamespace() + ".selectByOrderId", paramMap);
	}

	@Override
	public GenericPage<SellerInfo> findUserList(Map<String, Object> queryMap, int limit, int start) {
		GenericPage<SellerInfo> genericPage = this.selectByMap(queryMap, limit, start);
		return genericPage;
	}


	@Override
	public List<SellerInfo> selectByPower() {
		return this.getSqlSession().selectList(getMapperNamespace() + ".selectByPower");
	}


//	@Override
//	public GenericPage<T> selectOpenSellerByMap(Map<String, Object> paramMap, int pageSize, int startIndex) {
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("limit", pageSize);
//		params.put("start", startIndex);
//		return this.getSqlSession().selectList(getMapperNamespace() + ".selectOpenSellerByMap",params);
//	}
	public GenericPage<SellerInfo> selectOpenSellerByMap(Map<String, Object> queryParam, int pageSize, int startIndex) {
		if(pageSize < 1) {
			throw new IllegalArgumentException("分页pageSize参数必须大于1");
		} else if(startIndex < 0) {
			throw new IllegalArgumentException("分页startIndex参数必须大于0");
		} else {
			if(null == queryParam) {
				queryParam = new HashMap();
			}
			int totalSize = this.countByMap((Map)queryParam);
			if(totalSize == 0) {
				return new GenericPage((long)startIndex, (long)totalSize, (long)pageSize, (List)null);
			} else {
				List pagedData = this.getSqlSession().selectList(this.mapperNamespace + ".selectOpenSellerByMap", queryParam, new RowBounds(startIndex, pageSize));
				return new GenericPage((long)startIndex, (long)totalSize, (long)pageSize, pagedData);
			}
		}
	}


	/**
	 * 根据卖家账号和ID获取卖家信息
	 *
	 * @param account
	 * @param uid
	 * @return
	 */
	@Override
	public SellerInfo findByAccountAndUid(String account, String uid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("uid", uid);
		return this.getSqlSession().selectOne(getMapperNamespace() + ".findByAccountAndUid", params);
	}

	/**
	 * 更新客服
	 *
	 * @param servicerId
	 * @param servicerIdNew
	 * @return
	 */
	@Override
	public void UpdateService(Long servicerId,Long servicerIdNew)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("servicerId", servicerId);
		params.put("servicerIdNew", servicerIdNew);

		this.getSqlSession().selectOne(getMapperNamespace() + ".batchUpdateService", params);
	}

	/**
	 * 设置卖家为上线
	 *
	 * @param account
	 * @param uid
	 */
	@Override
	public void online(String account, String uid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("uid", uid);
		getSqlSession().update(getMapperNamespace() + ".online", params);
	}

	/**
	 * 设置卖家为下线
	 *
	 * @param account
	 * @param uid
	 */
	@Override
	public void offline(String account, String uid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("uid", uid);
		getSqlSession().update(getMapperNamespace() + ".offline", params);
	}

	/**
	 * 设置开通/关闭出货
	 *
	 * @param account
	 * @param uid
	 */
	@Override
	public void checkSh(String account, String uid,int openShState) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("uid", uid);
		params.put("openShState",openShState);
		getSqlSession().update(getMapperNamespace() + ".checkSh", params);
	}

	/**
	 * 添加7bao账号
	 * */
	@Override
	public void updateSevenBaoAccount(String account, String uid,Boolean isUserBind){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("uid", uid);
		params.put("isUserBind",isUserBind);
		getSqlSession().update(getMapperNamespace()+".updateSevenBaoAccount",params);
	}
	/**
	 * 添加已阅读
	 * @param isAgree
	 */
	@Override
	public void updateAgree(String loginAccount,String uid, Boolean isAgree) {
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("loginAccount",loginAccount);
		params.put("uid",uid);
		params.put("isAgree",isAgree);
		getSqlSession().update(getMapperNamespace()+".isAgree",params);





//		Map<String,Object> params = new HashMap<String, Object>();
//		params.put("account", account);
//		params.put("uid", uid);
//		params.put("sevenBaoAccount",sevenBaoAccount);
//		getSqlSession().update(getMapperNamespace()+".updateSevenBaoAccount",params);
	}

	@Override
	public void updateAgreeInit(List<SellerInfo> sellerInfo) {
		this.getSqlSession().update(getMapperNamespace()+".batchUpdate",sellerInfo);
	}

	/**
	 * 开启/关闭自动更新价格
	 * @param account
	 * @param uid
	 * @param isPriceRob
	 */
	@Override
	public void enablePriceRob(String account, String uid,boolean isPriceRob ){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("uid", uid);
		params.put("isPriceRob",isPriceRob);
		getSqlSession().update(getMapperNamespace() + ".enablePriceRob", params);
	}

	@Override
	public SellerInfo selectHxAccountById(String id){
		return this.getSqlSession().selectOne(getMapperNamespace() + ".selectHxAccountById",id);
	}

	@Override
	public List<SellerInfo> selectSellerInfoAll() {
		return this.getSqlSession().selectList(getMapperNamespace()+".selectSellerInfoAll");
	}

	@Override
	public SellerInfo findSellerDetailByLoginAccount(String account){
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("account",account);
		return  this.getSqlSession().selectOne(getMapperNamespace() + ".findSellerDetailByLoginAccount", paramMap);
	}

	@Override
	public List<SellerInfo> querySellerInfo(String loginAccount) {
		return this.getSqlSession().selectList(getMapperNamespace() + ".querySellerInfo",loginAccount);
	}
}