import com.wzitech.gamegold.jsrobot.service.order.IOrderService;
import com.wzitech.gamegold.jsrobot.service.order.dto.TransferOrderRequest;
import com.wzitech.gamegold.jsrobot.utils.DESHelper;
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
 * Created by 336335 on 2015/10/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/jsrobot-context.xml"})
@ActiveProfiles("development")
public class PasswordEncryptTest {
    public static void main(String[] args) throws Exception {
        String pwd = "nXXryni9bRn5FV0Wspz82LNwbtTP8GID";
        System.out.println(DESHelper.decrypt(pwd, "4G^v9_c!k8"));
    }

    @Autowired
    private IOrderService orderService;

    @Test
    public void test1() {
        TransferOrderRequest transferOrderRequest=new TransferOrderRequest();
        transferOrderRequest.setOrderId("YX1801040001318");
        transferOrderRequest.setSubOrderId(9160738L);
        orderService.transfer(transferOrderRequest);
    }
}
