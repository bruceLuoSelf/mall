package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.order.business.IOrderConfigManager;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by Administrator.
 *
 *  Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/12  huangyanling           ZW_C_JB_00008 商城增加通货
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class OrderAction extends AbstractAction {

	private OrderInfoEO orderInfo;
	
	private List<RepositoryInfo> repositoryList;
	
	private RepositoryInfo repository;
	
	private ConfigResultInfoEO configResult;
	
	private Date createStartTime;
	
	private Date createEndTime;
	
	private String sellerAccount;
	
	private String buyerAccount;
	
	private Integer orderState;
	
	private Boolean manualOperation;
	
	private String gameName;
	
	private String orderId;
	
	private String detailServiceAccount;
	
	private String buyerQq;

	private Integer refererType;

    private Integer goodsTypeId;//ZW_C_JB_00008 add

	private String goodsTypeName;//ZW_C_JB_00008 add


    /**
     * 是否延迟
     */
    private Boolean isDelay;

	/**
	 * 订单类型
	 * 1-担保
	 * 2-寄售物服
	 * 3-寄售机器人
	 */
	private Integer orderType;

    /**
     * 总的保费金额
     */
    private BigDecimal totalPreminums;
	
	//private String userAccount;
	
	//private String receiver;
	
	private List<OrderInfoEO> orderInfoList;
	
	private List<ConfigResultInfoEO> configInfoList;
	
	@Autowired
	IOrderInfoManager orderInfoManager;
	
	@Autowired
	IRepositoryManager repositoryManager;
	
	@Autowired
	IOrderConfigManager orderConfigQuery;
	
	@Autowired
	IUserInfoManager userInfoManager;
	
	/**
	 * 查询订单信息列表
	 * @return
	 */
	public String queryOrder() {
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
		paramMap.put("sellerAccount", sellerAccount);
		paramMap.put("userAccount", buyerAccount);
		paramMap.put("orderState", orderState);
		paramMap.put("orderId", orderId);
		paramMap.put("orderGameName", gameName);
		paramMap.put("buyerQq", buyerQq);
		paramMap.put("configResultIsDel", false);
		paramMap.put("manualOperation", manualOperation);
		paramMap.put("refererType", refererType);
        paramMap.put("isDelay", isDelay);
        paramMap.put("goodsTypeId", goodsTypeId);// ZW_C_JB_00008 add
		paramMap.put("goodsTypeName", goodsTypeName);//ZW_C_JB_00008 add

        if (orderType != null) {
			paramMap.put("orderType", orderType);
		}

		GenericPage<OrderInfoEO> genericPage = orderInfoManager.queryOrderInfo(paramMap, "CREATE_TIME", false, this.limit, this.start);
		orderInfoList = genericPage.getData();

		totalCount = genericPage.getTotalCount();
		return this.returnSuccess();
	}

    /**
     * 查询买了保险的订单
     * @return
     */
    public String queryBuyInsuranceOrder() {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("isBuyInsurance", true);
        paramMap.put("userAccount", buyerAccount);
        paramMap.put("orderState", orderState);
        paramMap.put("orderId", orderId);
        paramMap.put("orderGameName", gameName);
        paramMap.put("buyerQq", buyerQq);
        paramMap.put("configResultIsDel", false);
        paramMap.put("isDelay", isDelay);
		paramMap.put("goodsTypeName",goodsTypeName); /**ZW_C_JB_00008_2017/5/16 add **/
		if (orderState == null) {
			paramMap.put("sendStartTime", createStartTime);
			paramMap.put("sendEndTime", WebServerUtil.oneDateLastTime(createEndTime));
		} else if (orderState == OrderState.Delivery.getCode()) {
			paramMap.put("sendStartTime", createStartTime);
			paramMap.put("sendEndTime", WebServerUtil.oneDateLastTime(createEndTime));
		} else if (orderState == OrderState.Statement.getCode()) {
			paramMap.put("statementStartTime", createStartTime);
			paramMap.put("statementEndTime", WebServerUtil.oneDateLastTime(createEndTime));
		}

        GenericPage<OrderInfoEO> genericPage = orderInfoManager.queryOrderInfo(paramMap, "CREATE_TIME", false, this.limit, this.start);
        orderInfoList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 统计保费
     * @return
     */
    public String statisticPremiums() {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("isBuyInsurance", true);
        paramMap.put("userAccount", buyerAccount);
        paramMap.put("orderState", orderState);
        paramMap.put("orderId", orderId);
        paramMap.put("orderGameName", gameName);
        paramMap.put("buyerQq", buyerQq);
        paramMap.put("configResultIsDel", false);
        paramMap.put("isDelay", isDelay);
		paramMap.put("goodsTypeName",goodsTypeName); /**ZW_C_JB_00008_2017/5/16 add **/

		if (orderState == null) {
			paramMap.put("sendStartTime", createStartTime);
			paramMap.put("sendEndTime", WebServerUtil.oneDateLastTime(createEndTime));
		} else if (orderState == OrderState.Delivery.getCode()) {
			paramMap.put("sendStartTime", createStartTime);
			paramMap.put("sendEndTime", WebServerUtil.oneDateLastTime(createEndTime));
		} else if (orderState == OrderState.Statement.getCode()) {
			paramMap.put("statementStartTime", createStartTime);
			paramMap.put("statementEndTime", WebServerUtil.oneDateLastTime(createEndTime));
		}

        totalPreminums = orderInfoManager.statisticPremiums(paramMap);
        return this.returnSuccess();
    }

	/**
	 * 查询订单配置信息列表
	 * @return
	 */
	public String queryOrderConfig() {
		try {
			configInfoList = orderConfigQuery.orderConfigList(orderId);
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}
	
	/**
	 * 配置订单信息
	 * @return
	 */
	public String configOrder() {
		try {
			orderInfoManager.configOrder(orderInfo, repositoryList, configInfoList);
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}
	
	/**
	 * 重新配置
	 * @return
	 */
	public String replaceConfigOrder() {
		try {
			orderInfoManager.replaceConfigOrder(orderInfo, repository, configResult);
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}
	
	/**
	 * 设置订单信息状态并修改是否延迟状态
	 * @return
	 */
	public String changeOrderState() {
		try {
			orderInfoManager.changeOrderState(orderInfo.getOrderId(), orderInfo.getOrderState(), orderInfo.getIsDelay());
			if(orderInfo.getOrderState()== OrderState.Refund.getCode()){
				orderInfoManager.saveOrderRefundReason(orderInfo.getOrderId(),orderInfo.getRefundReason(),orderInfo.getRemark());
			}
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}
	
	/**
	 * 设置订单信息是否有货状态
	 * @return
	 */
	public String notStoreOrder() {
		try {
			orderInfoManager.notStoreOrder(orderInfo.getOrderId(), orderInfo.getIsHaveStore());
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}

	/**
	 * 同步订单支付状态
	 * @return
	 */
	public String syncOrderPaymentStatus() {
		try {
			boolean success = orderInfoManager.syncOrderPaymentStatus(orderId);
			String message = success ? "同步成功" : "同步失败";
			return this.returnSuccess(message);
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}

	public RepositoryInfo getRepository() {
		return repository;
	}

	public void setRepository(RepositoryInfo repository) {
		this.repository = repository;
	}

	public OrderInfoEO getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfoEO orderInfo) {
		this.orderInfo = orderInfo;
	}

	public List<OrderInfoEO> getOrderInfoList() {
		return orderInfoList;
	}

    public void setOrderInfoList(List<OrderInfoEO> orderInfoList) {
        this.orderInfoList = orderInfoList;
    }

    public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setCreateStartTime(Date createStartTime) {
		this.createStartTime = createStartTime;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public void setManualOperation(Boolean manualOperation) {
		this.manualOperation = manualOperation;
	}

	public void setCreateEndTime(Date createEndTime) {
		this.createEndTime = createEndTime;
	}

	public List<ConfigResultInfoEO> getConfigInfoList() {
		return configInfoList;
	}

	public void setConfigInfoList(List<ConfigResultInfoEO> configInfoList) {
		this.configInfoList = configInfoList;
	}

	public void setRepositoryList(List<RepositoryInfo> repositoryList) {
		this.repositoryList = repositoryList;
	}

	public ConfigResultInfoEO getConfigResult() {
		return configResult;
	}

	public void setConfigResult(ConfigResultInfoEO configResult) {
		this.configResult = configResult;
	}

	public void setSellerAccount(String sellerAccount) {
		this.sellerAccount = sellerAccount;
	}

	public void setBuyerAccount(String buyerAccount) {
		this.buyerAccount = buyerAccount;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setDetailServiceAccount(String detailServiceAccount) {
		this.detailServiceAccount = detailServiceAccount;
	}

	public void setBuyerQq(String buyerQq) {
		this.buyerQq = buyerQq;
	}

	public void setRefererType(Integer refererType) {
		this.refererType = refererType;
	}

    public void setIsDelay(Boolean isDelay) {
        this.isDelay = isDelay;
    }

    public BigDecimal getTotalPreminums() {
        return totalPreminums;
    }

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

    public void setGoodsTypeId(Integer goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }
}