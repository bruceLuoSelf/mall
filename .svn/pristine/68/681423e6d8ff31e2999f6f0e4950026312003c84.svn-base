/************************************************************************************
 * Copyright 2014 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		OrderServiceTest
 * 包	名：		com.wzitech.gamegold.facade.frontend
 * 项目名称：	gamegold-facade-frontend
 * 作	者：		SunChengfei
 * 创建时间：	2014-2-18
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014-2-18 下午10:47:10
 ************************************************************************************/
package test;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.OrderState;

import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.gamegold.app.service.order.IOrderService;
import com.wzitech.gamegold.gamegold.app.service.order.dto.*;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
import com.wzitech.gamegold.order.dao.IOrderInfoRedisDAO;
import com.wzitech.gamegold.order.dao.IServiceEvaluateDao;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import junit.framework.Assert;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SunChengfei
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gamegold-app-context.xml"})
@ActiveProfiles("development")
public class OrderServiceTest {
    @Autowired
    IOrderService orderService;

    /**
     * 初始化用户session
     */
    @Before
    public void initDB() {
        UserInfoEO loginUser = new UserInfoEO();
        loginUser.setUid("US14033148679192-033B");
        loginUser.setLoginAccount("GeiliTest");
        loginUser.setUserType(2);
        CurrentUserContext.setUser(loginUser);
    }

    @Test
    public void addOrderTest() {
        AddOrderRequest request = new AddOrderRequest();

        request.setUserId("US14033148679192-033B");
        request.setUserAccount("1208809713");
        request.setGameId("44343b06076d4a7a95a0ef22aac481ae");
        request.setGameLevel(8);
        request.setGameName("地下城与勇士");
        request.setGameRace(null);
        request.setGoldCount(555);
        request.setGoodsCat(5);
        request.setGoodsId(55555L);
        request.setInternetBar("都等网吧");
        request.setMobileNumber("15555555555");
        request.setMoneyName("元宝");
        request.setQq("55555555");
        request.setRaceId(null);
        request.setReceiver("撒旦");
        request.setRefererType(null);
        request.setRegion("广东区");
        request.setRegionId("f24ae3dcc776413cafa79bc54337d74b");
        request.setSellerLoginAccount("GeiliTest");
        request.setServer("一区(北京网通)");
        request.setServerId("0141cb0bd42a49b1b2325dda6fe87e48");
        request.setServicerId(1000075L);
        request.setTitle("当前最低价");
        request.setUnitPrice(new BigDecimal(0.1));
        request.setSign("f79bf5437a40478dd864fb7109c3cb31");


        try {
            String encryptKey = "app58@$7uc^";
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s",
                    request.getGameName(), request.getRegion(), request.getServer(), request.getGameRace(), request.getGameId(), request.getRegionId(),
                    request.getServerId(), request.getRaceId(), request.getTitle(), request.getGoodsCat(), request.getGoodsId(), request.getUnitPrice(),
                    request.getGoldCount(), request.getServicerId(), request.getMobileNumber(), request.getQq(), request.getReceiver(), request.getGameLevel(),
                    request.getSellerLoginAccount(), request.getMoneyName(), request.getRefererType(), request.getInternetBar(), request.getUserId(),
                    request.getUserAccount(), encryptKey));
            System.out.println(toEncrypt);
            if (StringUtils.equals(toEncrypt, request.getSign())) {
                System.out.println("签名一致！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("!!!");
        orderService.addOrder(request, null);

        System.out.println("222");
        System.out.println(orderService.addOrder(request, null));

    }

    @Test
    public void queryOrderTest() {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String sd = "2014-05-01 00:00:00";
//		String ed = "2014-05-31 23:59:59";
//		Date s_date = null;
//		Date e_date = null;
//		try {
//			s_date = (Date) sdf.parse(sd);
//			e_date = (Date) sdf.parse(ed);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
        QueryOrderRequest queryOrderRequest = new QueryOrderRequest();
        queryOrderRequest.setOrderBy("0");
        queryOrderRequest.setGameName("地下城与勇士");
        queryOrderRequest.setStartOrderCreate("2015-05-01");
        queryOrderRequest.setEndOrderCreate("2016-09-31");
        queryOrderRequest.setPageSize(5);
        queryOrderRequest.setStart(1);
        queryOrderRequest.setOrderState(1);
        queryOrderRequest.setOrderId(null);
        queryOrderRequest.setRegion("西南区");
        queryOrderRequest.setServer("西南2区");
        queryOrderRequest.setSign("c74e8c2581330fde8a077da397711e34");
        try {
            // 校验MD5
            String encryptKey = "app58@$7uc^";
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s",
                    queryOrderRequest.getPageSize(), queryOrderRequest.getStart(), queryOrderRequest.getOrderBy(), queryOrderRequest.getOrderState(),
                    queryOrderRequest.getStartOrderCreate(), queryOrderRequest.getEndOrderCreate(), queryOrderRequest.getGameName(),
                    queryOrderRequest.getOrderId(), queryOrderRequest.getRegion(), queryOrderRequest.getServer(), queryOrderRequest.getUserId(),
                    queryOrderRequest.getUserAccount(), encryptKey));
            System.out.println(toEncrypt);
            if (StringUtils.equals(toEncrypt, queryOrderRequest.getSign())) {
                System.out.println("签名一致！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(111);
        //orderService.queryOrder(queryOrderRequest, null);
        //System.out.println(orderService.queryOrder(queryOrderRequest, null));
        QueryOrderResponse response = (QueryOrderResponse) orderService.queryOrder(queryOrderRequest, null);


        Long l = response.getOrders().getTotalCount();
        System.out.println("code：" + response.getResponseStatus().getCode());
        System.out.println("message：" + response.getResponseStatus().getMessage());

    }

    @Test
    public void queryorderbyidTest() {
        QueryOrderRequest request = new QueryOrderRequest();
        request.setOrderId("YX1704210005684");
        request.setUserId("US17021747003213-0759");
        request.setUserAccount("M2_18738161475");
        request.setSign("bc84e7c548fe7ab1a45ac56430cc5929");
        QueryOrderResponse response = (QueryOrderResponse) orderService.queryOrderById(request, null);
    }

    @Test
    public  void test(){
        QueryOrderRequest  request=new QueryOrderRequest();
        request.setOrderId("YX1705120006485");
        request.setUserId("US16042155107213-062B");
        request.setUserAccount("jgmtest1");
        request.setSign("ec88080677dca48b95aa3228cd421a86");
        orderService.queryOrderById(request,null);
    }
    @Autowired
    IOrderInfoDBDAO orderInfoDBDAO;
    @Autowired
    IServiceEvaluateDao serviceEvaluateDao;
    @Test
    public void testCancel(){
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("orderState", OrderState.WaitPayment.getCode());
        Date now = new Date();
        Date deleteTime = DateUtils.addSeconds(now, -600);
        queryMap.put("createEndTime", deleteTime);
        List<OrderInfoEO> deleteList = orderInfoDBDAO.selectByMap(queryMap, "ID", false);
    }

    @Test
    public void testEv(){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", "YX1510070001399");
        paramMap.put("serviceId", 1000065);
        int countDb = serviceEvaluateDao.countByMap(paramMap);
        if (countDb > 0) {
            throw new SystemException(ResponseCodes.AlreadyEvaluate.getCode(), ResponseCodes.AlreadyEvaluate.getMessage());
        }
    }

    @Test
    public void testSign() throws IOException {
        String format="地下城与勇士_广东区_广东1区__0.02084_1440_15088247227_262889939_sfdsfsd_万金_5_US17031573351191-0773_30.0_闪电发货，灵活购买_20.00_false_iOS11.2;x86_64;C677B277-DD0E-4927-8A6D-792680FA7C64_2_192.168.33.1_app58@$7uc^";
        String toEncrypt = EncryptHelper.md5(format);
    }
}
