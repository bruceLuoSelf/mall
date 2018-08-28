package com.wzitech.gamegold.shorder;

import com.wzitech.gamegold.shorder.business.IAmoutHttp;
import com.wzitech.gamegold.shorder.business.IFundManager;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.utils.SevenBaoFund;
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

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by chengXY on 2017/8/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-shorder-context.xml"})
@ActiveProfiles("development")
public class ConnToZBaoTest {
    @Autowired
    private IAmoutHttp amoutHttp;
    @Autowired
    private SevenBaoFund sevenBaoFund;
    @Autowired
    IFundManager fundManager;

    @Test
    public void testZBao() throws IOException {
        DeliveryOrder deliveryOrder= new DeliveryOrder();
        deliveryOrder.setBuyerUid("US15070179087191-0504");
        deliveryOrder.setOrderId("SG1705250001019");
        BigDecimal bigDecimal= new BigDecimal("15.55");
        deliveryOrder.setRealAmount(bigDecimal);
        deliveryOrder.setRoleName("123456");

//        amoutHttp.conToZBao(deliveryOrder);
    }


    @Test
    public void test1019(){
        String account= "supmj3";
        String uid ="US12072040278001-00D0";
        BigDecimal big = new BigDecimal("15.55");
        String url ="http://192.168.40.135:8083/7Bao-frontend/services/updatefund/changefund";
        String orderId ="SG00000001019";
//        BigDecimal total = new BigDecimal("100.00");
//        BigDecimal avavilbale = new BigDecimal("90.00");
//        BigDecimal freeze = new BigDecimal("90.00");
        fundManager.freezeAmountZBao(account,uid,big,1,url,orderId);

    }
}
