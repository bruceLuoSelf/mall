package com.wzitech.gamegold.facade.frontend;

import com.wzitech.gamegold.common.insurance.dto.BQOrderServiceSoap;
import com.wzitech.gamegold.common.insurance.dto.BQType;
import com.wzitech.gamegold.common.insurance.dto.BackDTO;
import com.wzitech.gamegold.common.insurance.dto.OrderDTO;
import com.wzitech.gamegold.common.utils.DateUtil;
import com.wzitech.gamegold.order.business.IInsuranceManager;
import com.wzitech.gamegold.order.business.IInsuranceOrderManager;
import com.wzitech.gamegold.order.entity.InsuranceOrder;
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

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 336335 on 2015/6/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("production")
public class InsuranceServiceTest {
    /*@Autowired
    private BQOrderServiceSoap insuranceServiceClient;*/

    @Autowired
    IInsuranceOrderManager insuranceOrderManager;
    /*@Autowired
    IInsuranceManager insuranceManager;*/

    @Test
    public void createBQOrderTest() {
        //查询需要创建保单的订单记录
        List<InsuranceOrder> orders = insuranceOrderManager.queryNeedCreateBQList();
       /* for (InsuranceOrder insuranceOrder : orders) {
            try {
                insuranceManager.createBQOrder(insuranceOrder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    /*@Test
    public void createBQOrder() {
        XMLGregorianCalendar orderPayTime = DateUtil.convertToXMLGregorianCalendar(new Date());
        Calendar expireDate = Calendar.getInstance();
        expireDate.add(Calendar.DAY_OF_MONTH, 15);

        OrderDTO order = new OrderDTO();
        order.setOrderID("YX1506090004731");
        order.setOrderPrice(new BigDecimal("0.00273"));
        order.setBQServicePrice(new BigDecimal("2.75"));
        order.setOrderOp("测试客服111");
        order.setBuyerId("test111");
        order.setBuyerName("测试111");
        order.setGameId("880");
        order.setBuyerPhone("13516847885");
        order.setBuyerQQ("97107635");
        order.setProductName("20分钟内发货");
        order.setTradingType(10);
        order.setIsPicc(true);
        order.setRate(new BigDecimal("0.002"));
        order.setStartDate(orderPayTime);
        order.setEndDate(DateUtil.convertToXMLGregorianCalendar(expireDate.getTime()));
        order.setBQType(BQType.PICC);
        order.setOrderPayTime(orderPayTime);

        BackDTO back = insuranceServiceClient.createBQOrder(order);
        System.out.println("创建保单返回。。。");
    }

    @Test
    public void modifyTransferTime() {
        XMLGregorianCalendar finishTime = DateUtil.convertToXMLGregorianCalendar(new Date());
        String orderId = "YX1506190002093";
        BackDTO back =insuranceServiceClient.modifyTransferTime(orderId, finishTime);
        System.out.println("转账时间返回。。。");
    }*/
}
