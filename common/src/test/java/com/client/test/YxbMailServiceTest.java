package com.client.test;

import java.math.BigDecimal;

import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.wzitech.gamegold.common.tradeorder.client.YxbBizOfferDataIn;
import com.wzitech.gamegold.common.tradeorder.client.YxbMallServiceSoap;
import com.wzitech.gamegold.common.tradeorder.header.ServerHeaderOutProcessor;

public class YxbMailServiceTest {

	public static void main(String[] args) {
		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
		
//		factoryBean.getInInterceptors().add(new ServerHeaderInProcessor());
		factoryBean.getOutInterceptors().add(new ServerHeaderOutProcessor("123","123"));
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
//		factoryBean.getInInterceptors().add(new LoggingInInterceptor());
		
		
		factoryBean.setServiceClass(YxbMallServiceSoap.class);
		factoryBean
				.setAddress("http://tradeservice.5173.com/ServiceForYxbMall/YxbMallService.asmx");
		YxbMallServiceSoap service = (YxbMallServiceSoap) factoryBean.create();

		YxbBizOfferDataIn dataIn = new YxbBizOfferDataIn();
		dataIn.setGameAreaName("网通");
		dataIn.setGameName("阿凡达之恋001");
		dataIn.setGameRaceName("迪奥布斯");
		dataIn.setGameServerName("梦幻之城(荣华)");
		dataIn.setMaxPrice(BigDecimal.valueOf(1000));
		dataIn.setMinPrice(BigDecimal.valueOf(1));
		dataIn.setPageIndex(1);
		dataIn.setPageSize(10);
		dataIn.setServiceType(1);

		System.out.println(service.getYxbBizOfferList(dataIn));
	}
}
