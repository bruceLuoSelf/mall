package test;


import com.wzitech.gamegold.shorder.business.IConfigManager;

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
 * 根据ID获取配资接口测试
 * @author  lcs
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gamegold-app-context.xml"})
@ActiveProfiles("development")
public class DirectPayTransferTest {

    @Autowired
    IConfigManager configManager;


    @Test
    public void testSelectConfig() {
        Long id = 21L;
        System.out.println(configManager.getById(id));
    }


}
