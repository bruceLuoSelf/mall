package com.wzitech.gamegold.shorder;

import com.wzitech.gamegold.shorder.business.IFundManager;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
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

/**
 * Created by chengXY on 2017/8/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-shorder-context.xml"})
@ActiveProfiles("development")
public class NewSettlementTest {
    @Autowired
    private IFundManager fundManager;
    @Test
    public void cxyTest0825() throws IOException {
        DeliveryOrder deliveryOrder = new DeliveryOrder();

        fundManager.newSettlement(deliveryOrder);
    }
}
