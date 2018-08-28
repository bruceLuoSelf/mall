package com.wzitech.gamegold.facade.frontend;


import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import com.wzitech.gamegold.shorder.utils.ISendHttpToSevenBao;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * Created by 340032 on 2017/8/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class SevenBaoFundTest {

    @Autowired
    ISendHttpToSevenBao sendHttpToSevenBao;

    @Value("${7bao.check.url}")
    private String sevenBaoFundUrl;
    @Test
    public void test1(){

//        //发起http请求调用7bao接口
//        JSONObject jsonResult = sendHttpToSevenBao.httpPost(sevenBaoFundUrl,null);
//        System.out.println(jsonResult.toString()+"*****************");
//        if (jsonResult!=null) {
//            //获得返回结果
//            String ConfigKey = (String) jsonResult.get("configKey");
//            String ConfigValue = (String) jsonResult.get("configValue");
//            String AvailableFundKey = (String) jsonResult.get("availableFundKey");
//            String AvailableFundValue = (String) jsonResult.get("availableFundValue");
//            System.out.println(ConfigKey);
//            System.out.println(ConfigValue);
//            System.out.println(AvailableFundKey);
//            System.out.println(AvailableFundValue);
//            SystemConfig systemConfig = new SystemConfig();
//            systemConfig.setConfigKey(ConfigKey);
//            systemConfig.setConfigValue(ConfigValue);
//            systemConfig.setAvailableFundKey(AvailableFundKey);
//            systemConfig.setAvailableFundValue(AvailableFundValue);
//            System.out.println(systemConfig);
//        }
//        return;

    }

}