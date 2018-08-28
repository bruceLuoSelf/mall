package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.order.entity.ShippingInfoEO;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
/**
 *
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/13  zhujun           ZW_C_JB_00008 导出增加商品类型
 **/
@Controller
@Scope("prototype")
@ExceptionToJSON
public class ShippingAction extends AbstractAction {

	private Date createStartTime;
	
	private Date createEndTime;
	
	private String orderId;
	
	private String gameName;
	
	private String detailServiceAccount;
	
	private String buyerAccount;
	
	private String sellerAccount;

	private String goodsTypeName;

    /**
     * 只显示已取消的库存配置记录
     */
    private Boolean onlyDisplayCancelled;

    /**
     * 订单类型
	 * 1-担保
	 * 2-寄售物服
	 * 3-寄售机器人
     */
    private Integer orderType;
	
	private List<ShippingInfoEO> shippingInfoList;
	
	private List<SellerInfo> sellerList;
	
	@Autowired
	IOrderInfoManager orderInfoManager;
	
	@Autowired
	IOrderInfoDBDAO orderInfoDBDAO;
	
	@Autowired
	ISellerManager sellerManager;
	
	@Autowired
	IUserInfoManager userInfoManager;
	
	/**
	 * 查询出库存订单信息列表
	 * @return
	 */
	public String queryShipping() {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		int userType = CurrentUserContext.getUserType();
		if(UserType.SystemManager.getCode()!=userType){
			if(UserType.MakeOrder.getCode()==userType||UserType.RecruitBusiness.getCode()==userType){
				UserInfoEO user = (UserInfoEO)CurrentUserContext.getUser();
				paramMap.put("servicerId", user.getMainAccountId());
			} else if (UserType.ConsignmentService.getCode() == userType) {
                // 寄售客服
                paramMap.put("consignmentId", CurrentUserContext.getUid());
            } else{
				paramMap.put("servicerId", CurrentUserContext.getUid());
			}
		}else{
			if(StringUtils.isNotEmpty(detailServiceAccount)){
				UserInfoEO servicer = userInfoManager.findUserByAccount(detailServiceAccount);
				if(servicer!=null){
					paramMap.put("servicerId", servicer.getId());			
				}else{
					paramMap.put("servicerId", 0L);
				}		
			}	
		}
		paramMap.put("createStartTime", createStartTime);
		paramMap.put("createEndTime", WebServerUtil.oneDateLastTime(createEndTime));
		paramMap.put("orderId", orderId);
		paramMap.put("gameName", gameName);
		paramMap.put("buyerAccount", buyerAccount);
		paramMap.put("sellerAccount", sellerAccount);
		//出库订单就是已经配置完成的待发货的订单
		paramMap.put("orderState", OrderState.WaitDelivery.getCode());
        paramMap.put("onlyDisplayCancelled", onlyDisplayCancelled);
        if (orderType != null) {
            paramMap.put("orderType", orderType);
        }
		/** ZW_C_JB_00008_20170513_START	**/
		paramMap.put("goodsTypeName", goodsTypeName);
		/** ZW_C_JB_00008_20170513_END	**/
		GenericPage<ShippingInfoEO> genericPage = orderInfoManager.queryShippingList(paramMap, "CREATE_TIME", true, this.limit, this.start);
		shippingInfoList = genericPage.getData();
		totalCount = genericPage.getTotalCount();
		return this.returnSuccess();
	}
	
	/**
	 * 通过订单ID，查询订单所配置的卖家列表
	 * 2017/05/17 查询条件新增商品类型
	 * @return
	 */
	public String querySellerByOrderId() {
		sellerList = sellerManager.querySellerByOrderId(orderId,goodsTypeName);
		return this.returnSuccess();
	}
	
	public String setCopy() {
		try {
			OrderInfoEO orderInfo = orderInfoManager.selectById(orderId);
			if(orderInfo==null){
				throw new SystemException(ResponseCodes.EmptyOrderInfo.getCode(),ResponseCodes.EmptyOrderInfo.getMessage());
			}
			orderInfo.setIsCopy(true);
			orderInfoDBDAO.update(orderInfo);
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}

	public List<ShippingInfoEO> getShippingInfoList() {
		return shippingInfoList;
	}

	public void setCreateStartTime(Date createStartTime) {
		this.createStartTime = createStartTime;
	}

	public void setCreateEndTime(Date createEndTime) {
		this.createEndTime = createEndTime;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<SellerInfo> getSellerList() {
		return sellerList;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setDetailServiceAccount(String detailServiceAccount) {
		this.detailServiceAccount = detailServiceAccount;
	}

	public void setBuyerAccount(String buyerAccount) {
		this.buyerAccount = buyerAccount;
	}

	public void setSellerAccount(String sellerAccount) {
		this.sellerAccount = sellerAccount;
	}

    public void setOnlyDisplayCancelled(Boolean onlyDisplayCancelled) {
        this.onlyDisplayCancelled = onlyDisplayCancelled;
    }

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getGoodsTypeName() {
		return goodsTypeName;
	}

	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}
}