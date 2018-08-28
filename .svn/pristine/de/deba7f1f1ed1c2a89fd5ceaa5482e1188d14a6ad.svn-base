import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.DeliveryOrderGTRType;
import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import com.wzitech.gamegold.common.paymgmt.IPayManager;
import com.wzitech.gamegold.common.paymgmt.dto.DirectPayTransferResponse;
import com.wzitech.gamegold.common.utils.SignHelper;
import com.wzitech.gamegold.goods.business.IWarningManager;
import com.wzitech.gamegold.goods.entity.Warning;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.dao.IDeliverySubOrderDao;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import com.wzitech.gamegold.shorder.entity.PayOrder;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import com.wzitech.gamegold.shrobot.job.AutoTimeOutOrderForMachineDeliveryJob;
import com.wzitech.gamegold.shrobot.job.AutomaticQueuingJob;
import org.apache.commons.collections.map.HashedMap;
import org.hibernate.validator.jtype.Generic;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 公共资金服务通用付款接口测试
 *
 * @author yemq
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/shrobot-context.xml"})
@ActiveProfiles("development")
public class DirectPayTransferTest {
    private static final Logger logger = LoggerFactory.getLogger(DirectPayTransferTest.class);

    @Autowired
    IPayManager payManager;
    @Autowired
    IDeliveryOrderFinishManager deliveryOrderFinishManager;
    @Autowired
    IDeliverySubOrderManager subOrderManager;
    @Autowired
    IDeliverySubOrderDao subOrderDao;
    @Autowired
    IDeliveryOrderManager deliveryOrderManager;
    @Autowired
    IFundStatisticsManager fundStatisticsManager;
    @Autowired
    IWarningManager warningManager;
    @Autowired
    AutoTimeOutOrderForMachineDeliveryJob autoTimeOutOrderForMachineDeliveryJob;

    /**
     * 新增友情提示
     */
    @Test
    public void insert() {
        Warning warning = new Warning();
        warning.setGameId("456465");
        warning.setGameName("哈哈额");
        warning.setGoodsTypeId(54567L);
        warning.setGoodsTypeName("表兄");
        warning.setWarning("不要啦");
        warningManager.insert(warning);
    }

    /**
     * 删除友情提示
     */
    @Test
    public void delete() {
        List<Long> ids = new ArrayList<Long>();
        ids.add(3L);
        warningManager.deleteWarning(ids);
    }

    /**
     * 查询友情提示
     */
    @Test
    public void query() {
        Map map = new HashedMap();
        map.put("gameId", "4123");
        GenericPage<Warning> list = warningManager.selectByMap(map, 25, 0, "ID", false);
        System.out.println(list.getTotalCount());
        for (Warning w : list.getData()) {
            System.out.println(w);
        }
    }

    /**
     * 修改友情提示
     */
    @Test
    public void update() {
        Warning warning = new Warning();
        warning.setId(5L);
        warning.setGameId("456465");
        warning.setGameName("额");
        warning.setGoodsTypeId(54567L);
        warning.setGoodsTypeName("表第");
        warning.setWarning("不要啦");
        warningManager.update(warning);
    }


    @Test
    public void testDirectPayTransfer() {
        String id = "1";
        String orderId = "SHFK1604280000002";
        String sellerId = "US14022163863213-0315";
        String sellerName = "GeiliTest";
        BigDecimal totalFee = new BigDecimal("2.00");
        Date createTime = new Date();
        String retrospectOrderId = "SHZF1604280000003";
        DirectPayTransferResponse response = payManager.directPayTransfer(id, orderId, sellerId, sellerName, totalFee,
                createTime, retrospectOrderId);

        logger.info("付款请求返回，result:{},message:{},orderId:{},requestSum:{}", new Object[]{response.isResult(),
                response.getMessage(), response.getOrderId(), response.getRequestSum()});
    }

    @Test
    public void test() throws ParseException {
//        DeliverySubOrder subOrder=subOrderDao.selectByIdForUpdate(9255L);
//        deliveryOrderManager.updateGameAccountInfo(subOrder, DeliveryOrderGTRType.ERROR_PWD,true);

        //deliveryOrderManager.cancelTimeoutOrder(31L);
//        Calendar now = Calendar.getInstance();
//        Calendar start = Calendar.getInstance();
//        start.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH) - 25, 0, 0, 0);
//        start.set(Calendar.MILLISECOND, 000);
//        Date startTime = start.getTime();
//         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        simpleDateFormat.parse(startTime.toString());
        fundStatisticsManager.statistics();
        //deliveryOrderFinishManager.cancel("SH1701140000012", 426L, "3", 304,  "游戏异常", true);
    }

    @Autowired
    IPayOrderManager payOrderManager;
    @Autowired
    IRefundOrderManager refundOrderManager;
    @Autowired
    ISystemConfigManager systemConfigManager;

    @Test
    public void testData() {
        SystemConfig automateTimeout = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.AUTO_TIMEOUT_PAY_ORDER.getKey());
        if (automateTimeout == null || !automateTimeout.getEnabled()) {
            return;
        }
        String configValue = automateTimeout.getConfigValue();
        int minute = Integer.parseInt(configValue);//超过多少天自动退款

        List<PayOrder> list = payOrderManager.queryTimeoutPayOrders(minute);
        if (list != null && list.size() > 0) {
            for (PayOrder payOrder : list) {
//                refundOrderManager.autoRefundTimeoutPayOrder(payOrder.getOrderId(), payOrder.getAccount(), minute);
            }
        }
    }

//    /**
//     * 转账
//     */
//    @Autowired
//    private AutoConfigDeliveryOrderJob autoConfigDeliveryOrderJob;

//    @Autowired
//    private AutomateTimeout automateTimeout;


    @Autowired
    IDeliverySubOrderDao deliverySubOrderDao;

    @Autowired
    AutomaticQueuingJob automaticQueuingJob;

    @Test
    public void transfer() {
//        automaticQueuingJob.autoPlay();
//        String orderId = "SG1709220000870";
//        deliveryOrderManager.transfer(orderId);

//        Map<String, String> map = new LinkedHashMap<String, String>();
//        map.put("orderId", "SH1213");
//        map.put("id","77777");
////            map.put("type", String.valueOf(params.getType()));
////            map.put("role_name", params.getRoleName());
////            map.put("level", params.getLevel());
//        map.put("img", "src12132");
////            map.put("count", (params.getCount() == null) ? null : params.getCount().toString());
//        map.put("log", "好几家好几家");
//
//        SignHelper.sign2(map, "111", "UTF-8");
//        autoConfigDeliveryOrderJob.autoConfig();

//        automaticQueuingJob.autoPlay();
//        try {
////            automateTimeout.autoPlay();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date parse = simpleDateFormat.parse("2017-2-30");
//            System.out.println(parse.getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        String pwd = "5f0d5f3feaeb6844a1504375688a4af87d70a27de673e92bc83f2d279eee84bc9fe2cfc99b68e052dd4b99b4f711f3a2c45b674fb506469b958aefadb9f59860422fc3a54c773224";


        try {
            String encode = URLEncoder.encode("Wenzi@520", "UTF-8");
            System.out.println(encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    @Autowired
    IDeliveryOrderLogManager orderLogManager;
    @Test
    public void autoTimeOutOrderForMachineDeliveryJob(){
//        autoTimeOutOrderForMachineDeliveryJob.autoTimeOutMachineDeliveryOrder();
        Boolean flag = orderLogManager.isHaveLog(Long.valueOf("2919"));
    }
}
