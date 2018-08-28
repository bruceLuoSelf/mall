package com.wzitech.gamegold.facade.frontend;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.RepositoryDTO;
import com.wzitech.gamegold.goods.business.ICheckRepositoryManageRedisManager;
import com.wzitech.gamegold.goods.dao.ICheckRespostitoryManageRedisDAO;
import com.wzitech.gamegold.order.business.IAutoConfigManager;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.business.impl.orderstate.State;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.repository.dao.IRepositoryDBDAO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.shorder.business.IDeliverySubOrderManager;
import org.apache.commons.lang3.time.DateUtils;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class AutoConfigTest {

	@Autowired
    IOrderInfoManager orderInfoManager;
	
	@Autowired
	IAutoConfigManager autoConfigManager;

	@Autowired
	IDeliverySubOrderManager deliverySubOrderManager;

    @Autowired
    State paid;

    @Test
    public void paidTest() {
        paid.handle("YX1808280009485");
    }
	
	@Test
	public void configTest(){
        OrderInfoEO orderInfo = orderInfoManager.selectById("YX1503060000633");
		autoConfigManager.autoConfigOrder(orderInfo);
	}
	
	@Test
	public void timeTest(){
		Date now = new Date();
		Date TransTime = DateUtils.addSeconds(now, -1800);
		System.out.println(TransTime);
	}

	@Autowired
	ICheckRepositoryManageRedisManager redisManager;
	@Test
	public void tet32(){
		String s = redisManager.checkIn("1561a672f97a4b918b2ef1e6dd7b1591", "bab5c2917f274067a48bc6d642a19443", "lxm4433");
		System.out.println(s);
	}
	@Autowired
	private ICheckRespostitoryManageRedisDAO checkRepostoryManageRedsiDao;

	@Test
	public void test34(){
		String uuid = UUID.randomUUID().toString().trim().replace("-", "");
		String firmsSecret = "2089a7b96c93405a8edbfa757ce2f8f1";
		String loginAccount = "pa_85y1a6";
		String uid = "US18053047644213-015C";
		Integer integer = checkRepostoryManageRedsiDao.saveFirmAndLoginAccount(uuid, firmsSecret, loginAccount, uid);
		System.out.println(uuid);
	}

	@Autowired
	IRepositoryDBDAO repositoryDBDAO;

	@Test
	public void atest23(){
		RepositoryInfo repositoryInfo = repositoryDBDAO.selectById(2199999l);
		System.out.println(repositoryInfo);
	}
	JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
	@Test
	public void tets2(){
		List<RepositoryDTO> list = new ArrayList<RepositoryDTO>();
		RepositoryDTO dto = new RepositoryDTO();
		dto.setGameAccount("1049408508");
		dto.setGameRole("bruce");
		list.add(dto);
		String s = jsonMapper.toJson(list);
		System.out.println(s);

	}



	@Test
	public void tets2sd() throws IOException {
		deliverySubOrderManager.robotWithdrawalOrder(4883l);

	}
}
