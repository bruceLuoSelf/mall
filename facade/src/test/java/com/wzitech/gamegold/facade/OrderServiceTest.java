/**
 *
 */
package com.wzitech.gamegold.facade;

import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.facade.service.complaint.IComplaintService;
import com.wzitech.gamegold.facade.service.complaint.dto.QueryOrdersByUidRequest;
import com.wzitech.gamegold.facade.service.order.IOrderService;
import com.wzitech.gamegold.facade.service.order.dto.CancelOrderRequest;
import com.wzitech.gamegold.facade.service.order.dto.QueryOrderInfoRequest;
import com.wzitech.gamegold.facade.service.order.dto.QueryOrderListRequest;
import com.wzitech.gamegold.facade.service.order.dto.TransferOrderRequest;
import com.wzitech.gamegold.facade.service.receiving.IReceivingService;
import com.wzitech.gamegold.facade.service.receiving.dto.*;
import com.wzitech.gamegold.facade.service.servicer.ICustomServicer;
import com.wzitech.gamegold.facade.service.servicer.dto.LoginRequest;
import com.wzitech.gamegold.facade.utils.DESHelper;
import com.wzitech.gamegold.shorder.business.IDeliverySubOrderManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * @author wzitech
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-context.xml"})
@ActiveProfiles("development")
public class OrderServiceTest {
    @Autowired
    IComplaintService complaintService;
    @Autowired
    IOrderService orderService;
    @Autowired
    IReceivingService receivingService;
    @Autowired
    IDeliverySubOrderManager deliverySubOrderManager;
    @Autowired
    ICustomServicer customServicer;

    @Test
    public void queryOrderByUidTest() {
        QueryOrdersByUidRequest request = new QueryOrdersByUidRequest();
        request.setUid("1000001");
        request.setP(1);
        request.setPs(10);
        request.setMinDate("2007-2-3");
        request.setMaxDate("2014-5-9");

        complaintService.queryOrdersByUid(request, null);
    }

    //查询订单
    @Test
    public void queryOrderListTest() {
        QueryOrderListRequest request = new QueryOrderListRequest();
        request.setOrderStatus(OrderState.WaitDelivery.getCode());
        request.setRequestNum(5);
        request.setQueryType(1);
        request.setSign("a");
        orderService.queryOrderList(request, null);
    }

    //移交订单
    @Test
    public void transferOrderTest() {
        TransferOrderRequest resquest = new TransferOrderRequest();
        resquest.setOrderId("YX14050900720*1054193");
        orderService.transferOrder(resquest, null);
    }

    //取消订单
    @Test
    public void cancelOrderTest() {
        CancelOrderRequest request = new CancelOrderRequest();
        request.setOrderId("YX14050900720*1054193");
        request.setReconfig(1);
        orderService.cancelOrder(request, null);
    }

    //当前查询
    @Test
    public void queryOrderInfo() {
        QueryOrderInfoRequest request = new QueryOrderInfoRequest();
        request.setOrderId("YX1510030005615*2368091");
        request.setOpid("");
        orderService.queryOrderInfo(request, null);
    }

    /**
     * 异常转人工接口测试
     */
    @Test
    public void QueryMachineAbnormalTurnManualOrderListResponse() {
        QueryMachineAbnormalTurnManualOrderListRequest orderListRequest = new QueryMachineAbnormalTurnManualOrderListRequest();
        orderListRequest.setSign("19c89050523f69f5bc7590319f9a098c");
        receivingService.queryMachineAbnormalTurnManualOrderList(orderListRequest, null);
    }

    /**
     * 获取取消订单原因接口测试
     */
    @Test
    public void cancellationOrderReason() {
        CancellationOrderReasonRequest reasonRequest = new CancellationOrderReasonRequest();
        reasonRequest.setSign("19c89050523f69f5bc7590319f9a098c");
//		receivingService.cancellationOrderReason(null);
    }

    /**
     * 物服完单接口测试
     */
    @Test
    public void logisticsSheet() {
        LogisticsSheetRequest sheetRequest = new LogisticsSheetRequest();
        sheetRequest.setSign("e5b1270bcb81f978d9d84dd9ef25e52e");
        sheetRequest.setGoldCount(1522L);
        sheetRequest.setOpid("ad");
        sheetRequest.setOppwd("1hQPFPRsCt4=");
//		sheetRequest.setOrderId("Y4865");.
        receivingService.logisticsSheet(sheetRequest, null);
        try {
            String encrypt = DESHelper.encrypt("ok", "58@$7uc^");
            System.out.println(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 推送消息实现接口测试
     */
    @Test
    public void pushMessage() {
        PushMessageRequest pushMessageRequest = new PushMessageRequest();
        pushMessageRequest.setSign("f5e930d045b7ec2054c210f6a0ce392d");
        pushMessageRequest.setMessage("");
        pushMessageRequest.setOpid("ad");
        pushMessageRequest.setOppwd("1hQPFPRsCt4=");
        receivingService.pushMessage(pushMessageRequest, null);
    }

    /**
     * 推送消息实现接口测试
     */
    @Test
    public void cancellationOfOrder() {
        CancellationOfOrderRequest pushMessageRequest = new CancellationOfOrderRequest();
        pushMessageRequest.setSign("f5e930d045b7ec2054c210f6a0ce392d");
        pushMessageRequest.setReason("ad");
        pushMessageRequest.setRemarks("ad");
        pushMessageRequest.setOpid("ad");
        pushMessageRequest.setOppwd("1hQPFPRsCt4=");
        receivingService.cancellationOfOrder(pushMessageRequest, null);
    }


    /**
     * 异常转人工查询方法测试
     */

    @Test
    public void setDeliverySubOrderManager() {
//		Map<String,Object> queryMap = new HashMap<String,Object>();
//		queryMap.put("status", DeliveryOrderStatus.WAIT_DELIVERY.getCode());
//		List<DeliverySubOrder> deliverySubOrderList=deliverySubOrderManager.queryMachineAbnormalTurnManualOrderList(queryMap);
//		for(DeliverySubOrder deliverySubOrder: deliverySubOrderList){
//			System.out.println(deliverySubOrder.toString());
//		}
//		CancellationOfOrderRequest cancellationOfOrderRequest = new CancellationOfOrderRequest();
//		cancellationOfOrderRequest.setOrderId("1552");
//		cancellationOfOrderRequest.setRemarks("英俊潇洒，风流倜傥");
//		QueryOrderDetaileRequest queryOrderDetaileRequest = new QueryOrderDetaileRequest();
//
//		queryOrderDetaileRequest.setOrderId("SH1702040000012");
//
//		QueryOrderDetaileResponse orderDetails = receivingService.getOrderDetails(queryOrderDetaileRequest, null);
//
//		System.out.println(orderDetails);

    }

    @Test

    public void test() throws Exception {
//        //http://yxbmall.5173.com/gamegold-facade/services/user/login?loginId=bL5G0qVvePFf7o4Bc84Bhw%3D%3D&userPwd=HtygQUbx7HY%3D&passpod=&sign=131521285274e8a03f3b94f53d03b20c
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setLoginId("bL5G0qVvePFf7o4Bc84Bhw==");
//        loginRequest.setUserPwd("HtygQUbx7HY=");
//        loginRequest.setPasspod("");
//        loginRequest.setSign("131521285274e8a03f3b94f53d03b20c");
////        loginRequest.setLoginId("wf020@5173.com");
////        loginRequest.setUserPwd("888888");
//        customServicer.login(loginRequest, null);
        String requestAccount = DESHelper.encrypt("wf021@5173.com", "58@$7uc^");
    }

}
