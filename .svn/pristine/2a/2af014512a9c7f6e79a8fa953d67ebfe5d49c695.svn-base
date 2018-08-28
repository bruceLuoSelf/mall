package com.wzitech.gamegold.facade.frontend;

import com.wzitech.gamegold.common.userinfo.IUserInfoService;
import com.wzitech.gamegold.common.userinfo.entity.UserInfo;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
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
 * 获取5173用户信息测试类
 *
 * @author yemq
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class GetUserInfoServiceTest {

    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IUserInfoManager userInfoManager;

    @Test
    public void getUserInfo() throws IOException {
        UserInfo userInfo = userInfoService.getUserInfo("US13040840319001-01D6");
    }

    @Test
    public void queryAssureServiceIDByGame() {
        UserInfoEO userInfoEO = userInfoManager.queryAssureServiceIDByGame("地下城与勇士");
        System.out.println("===========");
        System.out.println(userInfoEO);
        System.out.println("===========");
    }
}
