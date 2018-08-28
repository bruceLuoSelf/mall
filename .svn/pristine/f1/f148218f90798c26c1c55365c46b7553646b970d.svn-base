/**
 * 
 */
package com.wzitech.gamegold.facade.frontend;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.FundsQueryType;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.paymgmt.IPayManager;
import com.wzitech.gamegold.common.paymgmt.dto.BatchTransferResponse;
import com.wzitech.gamegold.common.paymgmt.dto.QueryDetailResponse;
import com.wzitech.gamegold.common.paymgmt.dto.TransferUserInfo;
import com.wzitech.gamegold.common.utils.PayHelper;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wzitech
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class PaymentTest {
	@Autowired
	IPayManager payManager;
	@Autowired
	IOrderInfoManager orderInfoManager;
	
	// 测试批量转账
	@Test
	public void testBatchTransfer() {
//		System.out.println("测试批量转账 begin");
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("out_trade_no", "DB20110905678122");
//		map.put("total_fee", "55.5");
//		map.put("is_op", "0");
//		map.put("op_name", "");
//		map.put("user_id", "US14022163863213-0315");
//		map.put("user_name", "GeiliTest");
//		map.put("get_user_id", "US13101754804001-0296");
//		map.put("get_user_name", "1017100");
//		map.put("sub_commission_fee", "10.0");
//		map.put("transfer_list", "US13082142338001-025D#201308211#10.00");
//		map.put("request_ip", "127.0.0.1");
		
		ArrayList<TransferUserInfo> transferUserInfos = new ArrayList<TransferUserInfo>();
		TransferUserInfo userInfo = new TransferUserInfo();
		transferUserInfos.add(userInfo);
		userInfo.setGetUserId("US13082142338001-025D");
		userInfo.setGetUserName("201308211");
		userInfo.setTransferFee(new BigDecimal(88.00));
		
		BatchTransferResponse response = this.payManager.batchTransfer("YX14030200031",new BigDecimal(98.00),"US14022163863213-0315",
				"GeiliTest++","US14022163863213-0315","GeiliTest",new BigDecimal(10), transferUserInfos);
		
		System.out.println("测试批量转账 end："+response);
	}
	
	/**
	 * 批量转账明细测试
	 */
	@Test
	public void BatchTransferDetailTest() {
		String sd = "2014-05-01 00:00:00";
		String ed = "2014-05-31 23:59:59";
		Date s_date = null;
		Date e_date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			s_date = (Date) sdf.parse(sd);
			e_date = (Date) sdf.parse(ed);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		QueryDetailResponse response = new QueryDetailResponse();

		paramMap.put("createStartTime", s_date);
		paramMap.put("createEndTime", e_date);
		paramMap.put("orderState", 5);
		// paramMap.put("configResultIsDel", false);
		GenericPage<OrderInfoEO> genericPage = orderInfoManager.queryOrderInfo(
				paramMap, "CREATE_TIME", false, 10000, 20545);
		List<OrderInfoEO> OrderInfoEOs = genericPage.getData();
		List<OrderInfoEO> errorlist = new ArrayList();
		for (OrderInfoEO orderInfoEO : OrderInfoEOs) {
			if (orderInfoEO.getOrderId() != null) {
				response = payManager.queryDetail(
						FundsQueryType.Transfer.getCode(),
						orderInfoEO.getOrderId(), "US14030369097192-031F");
				if (response != null) {
					if ("SUCCESS".equals(response.getMessage())
							&& response.getCommitStatus() == 1
							&& response.getEarnStatus() == 1
							&& (orderInfoEO.getOrderId()).equals(response
									.getTradeNo())) {
						continue;
					} else {
						errorlist.add(orderInfoEO);
					}
				} else {
					errorlist.add(orderInfoEO);
				}
			}
			response = null;
		}
		if (errorlist != null) {
			System.out.println("-------转账未成功订单信息     start---------");
			for (OrderInfoEO orderInfo : errorlist) {
				System.out.println("[订单号：" + orderInfo.getOrderId() + "-"
						+ "UID:" + orderInfo.getUid() + "]");
			}
			System.out.println("-------转账未成功订单信息      end-----------");
		}
	}

	/**
	 * 退款测试
	 */
	@Test
	public void refundTest(){
		payManager.refund("YX14060403244", "US11041282839234-06CF", "zclovednf", new BigDecimal(1405.00));
	}

    /**
     * 支付明细测试
     */
    @Test
    public void payDetailTest(){
        payManager.queryDetail(FundsQueryType.Payment.getCode(),"YX14050701770","US14050574458192-035E");
    }

    /**
     * 转账明细测试
     */
    @Test
    public void transferDetailTest(){
        payManager.queryDetail(FundsQueryType.Transfer.getCode(),"YX14050701770","US14030369097192-031F");
    }
    /**
     * 多个转账明细测试
     */
    @Test
    public void transfersDetailTest(){
    	List<String> list = new ArrayList();
    	list.add("YX14052700668");
    	list.add("YX14052601800");
    	list.add("YX14052501914");
    	list.add("YX14052700491");
    	list.add("YX14051500558");
    	list.add("YX14051203149");
    	list.add("YX14050801326");
		List<String> errorlist = new ArrayList();
		QueryDetailResponse response = new QueryDetailResponse();
		for (String orderInfoId : list) {
			if (orderInfoId != null) {
				response = payManager.queryDetail(
						FundsQueryType.Transfer.getCode(),
						orderInfoId, "US14030369097192-031F");
				if (response != null) {
					if ("SUCCESS".equals(response.getMessage())
							&& response.getCommitStatus() == 1
							&& response.getEarnStatus() == 1
							&& (orderInfoId).equals(response
									.getTradeNo())) {
						continue;
					} else {
						errorlist.add(orderInfoId);
					}
				} else {
					errorlist.add(orderInfoId);
				}
			}
			response = null;
		}
		if (errorlist != null) {
			System.out.println("-------转账未成功订单ID     start---------");
			for (String orderInfoId : errorlist) {
				System.out.println("[订单号：" + orderInfoId +  "]");
			}
			System.out.println("-------转账未成功订单ID      end-----------");
		}
    }

    /**
     * 退款明细测试
     */
    @Test
    public void refutnDetailTest(){
        payManager.queryDetail(FundsQueryType.Refund.getCode(),"YX14060403244","US11041282839234-06CF");
    }

    JsonMapper jsonMapper =JsonMapper.nonDefaultMapper();

    /**
     * 日志记录器
     */
    protected final Logger logger = LoggerFactory.getLogger(PaymentTest.class);

    @Test
    public void jsonTest(){
//        BatchTransferResponse response = new BatchTransferResponse();
        QueryDetailResponse response = new QueryDetailResponse();
        try {
//            String respoString = "{\"OrderId\":\"S201405290058269721\",\"RequestSum\":0.1000,\"Message\":\"账户余额不足！\",\"Result\":3}";
            String respoString = "{\"CommitStatus\":1,\"EarnStatus\":1, \"Message\":\"SUCCESS\", \"TradeNo\":\"YX14060304098\"}";
//            String respoString = "ILLEGAL_ARGUMENT";
            response = this.jsonMapper.fromJson(respoString, QueryDetailResponse.class);
            if (null != response && StringUtils.isNotEmpty(response.getMessage())) {
                response.setMessage(new String(response.getMessage().getBytes("GB2312"), "UTF-8"));
            }
        }catch (Exception e){
            response = null;
            logger.error("查询资金明细发生异常:{}", ExceptionUtils.getStackTrace(e));
        }

        System.out.println(response);

        boolean flag = true;
        if(response==null || response.getEarnStatus()!=1){
            System.out.println("");
        }

        OrderInfoEO orderInfoEO = new OrderInfoEO();
        orderInfoEO.setOrderId("YX14060403192");
        if(!StringUtils.equals(response.getTradeNo(),orderInfoEO.getOrderId())){
            System.out.println("");
        }

        System.out.println(response);
    }



    /**
     * 付款通知测试
     */
    @Test
    public void paymentNotify() {
        orderInfoManager.changeOrderState("YX1506170000733", OrderState.Paid.getCode(), null);
    }

	/**
	 * 测试
	 */
//	public Map<String, String> getBaseParams() {
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("service", "direct_pay_order_by_user_outer");
//		params.put("sign_type", "MD5");
//		params.put("input_charset", "gb2312");
//		params.put("client_ip", String.valueOf(PayHelper.ipToLong("127.0.0.1")));
//		params.put("version", "1.3");
//		params.put("payment_type", "3");
//		params.put("trading_type", this.tradingType);
//		params.put("dep_name", this.depName);
//		params.put("create_internal_site_id", this.createInterNalSiteId);
//		params.put("create_trade_no_ip", String.valueOf(PayHelper.ipToLong(this.createTradeNoIP)));
//		params.put("trading_method", this.tradingMethod);
//		params.put("notify_url", this.notifyURL);
//		params.put("return_url", this.returnURL);
//		return params;
		/*if (this.params == null) {
			this.params = new HashMap<String, String>();
			this.params.put("service", this.service);
			this.params.put("sign_type", this.signType);
			this.params.put("input_charset", this.inputCharset);
			this.params.put("client_ip", String.valueOf(PayHelper.ipToLong(this.clientIP)));
			this.params.put("version", this.version);
			this.params.put("payment_type", this.paymentType);
			this.params.put("trading_type", this.tradingType);
			this.params.put("dep_name", this.depName);
			this.params.put("create_internal_site_id", this.createInterNalSiteId);
			this.params.put("create_trade_no_ip", String.valueOf(PayHelper.ipToLong(this.createTradeNoIP)));
			this.params.put("trading_method", this.tradingMethod);
			this.params.put("notify_url", this.notifyURL);
			this.params.put("return_url", this.returnURL);
		}
		return this.params;*/
//	}
}
