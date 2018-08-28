package com.wzitech.gamegold.facade.frontend;

import com.wzitech.gamegold.common.enums.FundsQueryType;
import com.wzitech.gamegold.common.paymgmt.IPayManager;
import com.wzitech.gamegold.common.paymgmt.dto.BatchTransferResponse;
import com.wzitech.gamegold.common.paymgmt.dto.TransferUserInfo;
import com.wzitech.gamegold.common.paymgmt.dto.VaQueryDetailResponse;
import com.wzitech.gamegold.order.business.IAutoPayManager;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
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
import java.util.ArrayList;

/**
 * 售得&批量P2P转账接口测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class BatchTransferTest {
    private static final Logger logger = LoggerFactory.getLogger(BatchTransferTest.class);

    @Autowired
    IPayManager payManager;
    @Autowired
    IOrderInfoManager orderInfoManager;
    @Autowired
    IAutoPayManager autoPayManager;

    // 测试批量转账
    @Test
    public void testBatchTransfer() {
        logger.info("测试批量转账 begin");

        ArrayList<TransferUserInfo> transferUserInfos = new ArrayList<TransferUserInfo>();
        TransferUserInfo userInfo = new TransferUserInfo();
        transferUserInfos.add(userInfo);
        userInfo.setGetUserId("US12090550257001-00FF");
        userInfo.setGetUserName("byj0007");
        userInfo.setTransferFee(new BigDecimal("20.01"));

        BatchTransferResponse response = this.payManager.batchTransfer(
                "YX1604200001203",
                new BigDecimal("20.01"),
                "US12090550257001-00FF",
                "byj0007",
                new BigDecimal(0.00),
                transferUserInfos);

        logger.info("测试批量转账 end：{}", response);
    }

    /**
     * 支付明细测试
     */
    @Test
    public void payDetailTest() {
        payManager.queryDetail(FundsQueryType.Payment.getCode(), "YX1604200001204", "US12090550257001-00FF");
    }

    @Test
    public void queryWithdrawalsDetailByBillId() {
//        payManager.queryWithdrawalsDetail(FundsQueryType.Withdrawals.getCode(), "", "YX1801230001207", "", "");
//        payManager.queryWithdrawalsDetail(FundsQueryType.Withdrawals.getCode(), "N201801233760370941", "");
        VaQueryDetailResponse response = autoPayManager.queryWithdrawalsDetailByBillId("YX1801220001255");
        System.out.println(response);
    }

    @Test
    public void queryWithdrawalsDetailByOrderId() {
//        payManager.queryWithdrawalsDetail(FundsQueryType.Withdrawals.getCode(), "", "YX1801230001207", "", "");
//        payManager.queryWithdrawalsDetail(FundsQueryType.Withdrawals.getCode(), "N201801233760370941", "");
        VaQueryDetailResponse response = autoPayManager.queryWithdrawalsDetailByOrderId("N2018012414578986582");
        System.out.println(response);
    }
}
