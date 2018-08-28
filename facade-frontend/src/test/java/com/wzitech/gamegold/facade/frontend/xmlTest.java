package com.wzitech.gamegold.facade.frontend;

import com.fasterxml.jackson.databind.JavaType;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.business.IRepositoryTransfer;
import com.wzitech.gamegold.repository.entity.RegionServer;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class xmlTest {


	@Resource(name = "dnfRepositoryTransferManager")
	IRepositoryTransfer dnfRepositoryTransferManager;




	private static final JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

	@Test
	public void test232(){
		String str = dnfRepositoryTransferManager.getRepositoryTransferFile();
		System.out.println(str);
		JavaType javaType=jsonMapper.createCollectionType(ArrayList.class,RegionServer.class);
		List<RegionServer> regionServers = jsonMapper.fromJson(str, javaType);
		System.out.println(regionServers);
		Map<String, Object> servers = dnfRepositoryTransferManager.getServers("广东1区");
		System.out.println(servers);

	}

	@Autowired
	IRepositoryManager repositoryManager;

	@Test
	public void test2e32(){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("gameName","地下城与勇士");
		map.put("server","广东1区");
		Map<String, Object> process = dnfRepositoryTransferManager.process(map);
		Map<String, Object> serverMap = repositoryManager.processRepositoryTransfer(map);
		System.out.println(serverMap);
	}


}
