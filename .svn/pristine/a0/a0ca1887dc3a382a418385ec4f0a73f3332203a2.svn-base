/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		UserInfoDBDAOImpl
 *	包	名：		com.wzitech.gamegold.usermgmt.dao.rdb.impl
 *	项目名称：	gamegold-usermgmt
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午1:24:32
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.dao.rdb.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.userinfo.entity.UserInfo;
import com.wzitech.gamegold.usermgmt.dao.rdb.IUserInfoDBDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户信息DAO
 * @author SunChengfei
 *
 */
@Repository("userInfoDBDAOImpl")
public class UserInfoDBDAOImpl extends AbstractMyBatisDAO<UserInfoEO, Long> implements IUserInfoDBDAO{
	@Override
	public UserInfoEO findUserById(Long  uid) {
		if(uid==null){
			return null;
		}
		return selectUniqueByProp("id",uid);
	}
	
	@Override
	public UserInfoEO findUserByLoginAccount(String loginAccount) {
		if(StringUtils.isEmpty(loginAccount)){
			return null;
		}
		return selectUniqueByProp("loginAccount", loginAccount);
	}
	
	@Override
	public UserInfoEO findUserByNickName(String nickName) {
		if(StringUtils.isEmpty(nickName)){
			return null;
		}
		return selectUniqueByProp("nickName", nickName);
	}
	
	@Override
	public void modifyPhone(UserInfoEO userInfoEO) {
		this.getSqlSession().update(this.mapperNamespace + ".phoneUpdate", userInfoEO);
	}

	@Override
	public GenericPage<UserInfoEO> findUserList(Map<String, Object> queryMap, int limit, int start) {
		GenericPage<UserInfoEO> genericPage = this.selectByMap(queryMap, limit, start);
		return genericPage;
	}
	@Override
	public UserInfoEO queryServiceOrederNum(Long Id) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("ID", Id);
		queryMap.put("isPaid",OrderState.Paid.getCode());
		queryMap.put("isWaitDelivery",OrderState.WaitDelivery.getCode());
		int orderNum = (Integer)this.getSqlSession().selectOne(this.mapperNamespace + ".queryOrederNum", queryMap);
		UserInfoEO userInfoEO = new UserInfoEO();
		userInfoEO.setOrderNum(orderNum);
		return userInfoEO;
	}

    /**
     * 查询寄售客服当前处理订单数量
     * @param id
     * @return
     */
    public UserInfoEO queryConsignmentOrderNum(Long id) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("ID", id);
        queryMap.put("isWaitDelivery",OrderState.WaitDelivery.getCode());
        int orderNum = (Integer)this.getSqlSession().selectOne(this.mapperNamespace + ".queryConsignmentOrederNum", queryMap);
        UserInfoEO userInfoEO = new UserInfoEO();
        userInfoEO.setOrderNum(orderNum);
        return userInfoEO;
    }

	/**
	 * 查询客服当前还有多少卖家入驻需要审核
	 *
	 * @param servicerId
	 * @return
	 */
	@Override
	public int querySellerEnterNum(Long servicerId) {
		int num = (Integer)this.getSqlSession().selectOne(this.mapperNamespace + ".querySellerEnterNum", servicerId);
		return num;
	}

	/**
	 * 查询指定游戏担保客服列表，待发货订单数量少的客服靠前排序
	 * @param gameName
	 * @return
	 */
	@Override
	public List<UserInfoEO> queryAssureServiceByGame(String gameName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gameName", gameName);
		return getSqlSession().selectList(this.getMapperNamespace() + ".queryAssureServiceByGame", params);
	}

	public List<UserInfoEO> randomQuery(){
		return getSqlSession().selectList(this.getMapperNamespace() + ".randomQuery");
	}

	@Override
	public int countByUserMap(Map<String, Object> queryParam) {
		return ((Integer)this.getSqlSession().selectOne(this.mapperNamespace + ".countUserByMap", queryParam)).intValue();
	}

	public GenericPage<UserInfoEO> selectUserByMap(Map<String, Object> queryParam, int pageSize, int startIndex, String orderBy, boolean isAsc) {
		Validate.notBlank(orderBy);
		if(pageSize < 1) {
			throw new IllegalArgumentException("分页pageSize参数必须大于1");
		} else if(startIndex < 0) {
			throw new IllegalArgumentException("分页startIndex参数必须大于0");
		} else {
			if(null == queryParam) {
				queryParam = new HashMap();
			}

			((Map)queryParam).put("ORDERBY", orderBy);
			if(isAsc) {
				((Map)queryParam).put("ORDER", "ASC");
			} else {
				((Map)queryParam).put("ORDER", "DESC");
			}

			int totalSize = this.countByMap((Map)queryParam);
			if(totalSize == 0) {
				return new GenericPage((long)startIndex, (long)totalSize, (long)pageSize, (List)null);
			} else {
				List pagedData = this.getSqlSession().selectList(this.mapperNamespace + ".selectUserByMap", queryParam, new RowBounds(startIndex, pageSize));
				return new GenericPage((long)startIndex, (long)totalSize, (long)pageSize, pagedData);
			}
		}
	}

	/**
	 * 根据接单数量排序查询寄售客服
	 * ZW_C_JB_00004 mj
	 * @return
	 */
	@Override
	public List<UserInfoEO> selectFreeConsignmentService() {
		return this.getSqlSession().selectList(this.getMapperNamespace()+".queryFreeDistributionService");
	}

	/**
	 * 查询指定游戏担保客服列表，待发货订单数量少的客服靠前排序
	 * @param gameName
	 * @return
	 */
	@Override
	public UserInfoEO queryAssureServiceIDByGame(String gameName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gameName", gameName);
		params.put("serviceCharge", 0);
		return getSqlSession().selectOne(this.getMapperNamespace() + ".queryAssureServiceIDByGame", params);
	}
}
