/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		GameInfoManagerImpl
 *	包	名：		com.wzitech.gamegold.common.game.impl
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-20 下午2:17:21
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.game.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.game.IGameInfoManager;
import com.wzitech.gamegold.common.game.dao.IGameInfoRedisDAO;
import com.wzitech.gamegold.common.game.entity.*;
import com.wzitech.gamegold.common.utils.StreamIOHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.List;

/**
 * 游戏信息类目管理
 * @author SunChengfei
 *
 */
@Component
public class GameInfoManagerImpl extends AbstractBusinessObject implements IGameInfoManager {
	@Autowired
	IGameInfoRedisDAO gameInfoRedisDAO;

    /**
     * 日志记录器
     */
    protected final Logger logger = LoggerFactory.getLogger(GameInfoManagerImpl.class);

    /**
     * 设置连接超时时间
     */
    @Value("${httpclient.connection.timeout}")
    private int connectionTimeout = 30000;

    /**
     * 设置读取超时时间
     */
    @Value("${httpclient.connection.sotimeout}")
    private int SoTimeout = 120000;

    /**
     * 设置最大连接数
     */
    @Value("${httpclient.connection.maxconnection}")
    private int maxConnection = 200;

    /**
     * 设置每个路由最大连接数
     */
    @Value("${httpclient.connection.maxrouteconnection}")
    private int maxRouteConneciton = 50;

    /**
     * 设置接收缓冲
     */
    @Value("${httpclient.connection.socketbuffersize}")
    private int sockeBufferSize = 8192;

    /**
     * 设置失败重试次数
     */
    @Value("${httpclient.connection.maxretry}")
    private int maxRetry = 5;

    /**
     * 下载多线程管理器
     */
    private static PoolingClientConnectionManager connMger;

    /**
     * 下载参数
     */
    private static HttpParams connParams;
	
	private JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
	
	@Value("${gameInfo.getGameInfo.request}")
	private String getGameInfoRequest="";
	
	@Value("${gameInfo.getGameAll.request}")
	private String getGameAllRequest="";
	
	@Value("${gameInfo.getCompanyAll.request}") 
	private String getCompanyAllRequest="";
	
	@Value("${gameInfo.getGameByCompany.request}")
	private String getGameByCompanyRequest="";
	
	@Value("${gameInfo.getGameRaceAndMoney.request}")
	private String getGameRaceAndMoneyRequest="";
	
	@Value("${gameInfo.analysisUrl.request}")
	private String analysisUrlRequest="";

    @PostConstruct
    private void afterInitialization(){
        // 初始化下载线程池
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(
                new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(
                new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

        connMger = new PoolingClientConnectionManager(schemeRegistry);
        connMger.setDefaultMaxPerRoute(maxRouteConneciton);
        connMger.setMaxTotal(maxConnection);

        // 初始化下载参数
        connParams = new BasicHttpParams();
        connParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeout);
        connParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, SoTimeout);
        connParams.setParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, sockeBufferSize);
    }

    public HttpClient getHttpClient() {
        DefaultHttpClient client = new DefaultHttpClient(connMger, connParams);
        client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(maxRetry, false));
        return client;
    }

	/**
	 * 查询所有的游戏
	 */
	@Override
	public GameInfos getGameAll() throws IOException, JAXBException {
		//现在redis中查询所有的游戏
		GameInfos gameInfos = gameInfoRedisDAO.getGameAll();
		//若redis中未找到所有游戏，则尝试去调用5173的接口查找
		if (gameInfos == null || gameInfos.getGameList() == null || gameInfos.getGameList().size() == 0) {
			logger.debug("在redis中未找到{}相关信息，尝试在数据库中查找。");
			
			//调用5173接口查询所有的游戏
			HttpClient httpClient = getHttpClient();
	        HttpGet getMethod = new HttpGet(getGameAllRequest);
	        HttpResponse response = httpClient.execute(getMethod);
	        String responseStr = StreamIOHelper.inputStreamToStr(response.getEntity().getContent(), "UTF-8");
	        responseStr = responseStr.replace("&", "&amp;");
	        logger.debug("5173获取所有游戏请求url:{},返回:{}", new Object[]{getGameAllRequest, responseStr});

	        JAXBContext jaxbContext = JAXBContext.newInstance(GameInfos.class);
	        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	        StreamSource streamSource = new StreamSource(new StringReader(responseStr));
	        JAXBElement<GameInfos> je = jaxbUnmarshaller.unmarshal(streamSource, GameInfos.class);
	        gameInfos = (GameInfos) je.getValue();
	        
	        //如果在5173的接口中有对应的信息返回，则将信息存入redis中
	        if (gameInfos != null) {
	        	for (GameInfo gameList : gameInfos.getGameList()) {
	        		gameList.setId(gameList.getId().trim());
	        		gameList.setName(gameList.getName().trim());
				}
	        	
	        	logger.debug("在5173接口中查找到{}相关的信息，重新将该信息存储到redis。");
	        	gameInfoRedisDAO.saveGameAll(gameInfos);
			}
		}
        return gameInfos;
    }

	/**
	 * 根据游戏ID获取游戏信息
	 */
	@Override
	public GameDetailInfo getGameById(String gameId) throws IOException, JAXBException {
		//先从redis中查询游戏信息
		GameDetailInfo gameDetailInfo = gameInfoRedisDAO.getGameById(gameId);
		//若redis中未找到对应的游戏信息，则尝试调用5173的接口查找
		if (gameDetailInfo == null || gameDetailInfo.getGameAreaList() == null
				|| gameDetailInfo.getGameAreaList().size() == 0) {
			logger.debug("在redis中未找到{}相关信息，尝试在数据库中查找。", gameId);
			
			//调用5173的接口查询游戏信息
			String requestUrl = String.format(getGameInfoRequest, gameId);
	        HttpClient httpClient = getHttpClient();
	        HttpGet getMethod = new HttpGet(requestUrl);
	        HttpResponse response = httpClient.execute(getMethod);
			String responseStr = StreamIOHelper.inputStreamToStr(response.getEntity().getContent(), "UTF-8");
			 responseStr = responseStr.replace("&", "&amp;");
			logger.debug("5173根据游戏Id获取游戏信息请求url:{},返回:{}", new Object[]{requestUrl, responseStr});
			
			JAXBContext jaxbContext = JAXBContext.newInstance(GameDetailInfo.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			StreamSource streamSource = new StreamSource(new StringReader(responseStr));
			JAXBElement<GameDetailInfo> je = jaxbUnmarshaller.unmarshal(streamSource, GameDetailInfo.class);
			gameDetailInfo = (GameDetailInfo)je.getValue(); 
			
			//如果在5173的接口中有对应的信息返回，则将信息存入redis中
			if (gameDetailInfo != null) {
				gameDetailInfo.setName(gameDetailInfo.getName().trim());
				gameDetailInfo.setSpell(gameDetailInfo.getSpell().trim());

				if (gameDetailInfo.getGameAreaList() != null && gameDetailInfo.getGameAreaList().size() > 0) {
					for (GameArea gaemArea : gameDetailInfo.getGameAreaList()) {
						gaemArea.setId(gaemArea.getId().trim());
						gaemArea.setName(gaemArea.getName().trim());

						if (gaemArea.getGameServerList() != null && gaemArea.getGameServerList().size() > 0) {
							for (GameServer server : gaemArea.getGameServerList()) {
								server.setId(server.getId().trim());
								server.setName(server.getName().trim());
							}
						}
					}
				}
				
				logger.debug("在5173接口中查找到{}相关的信息，重新将该信息存储到redis。", gameId);
				gameInfoRedisDAO.saveGameDetailInfo(gameDetailInfo);
			}
		}
		return gameDetailInfo;
	}

	/**
	 * 查询所有游戏厂商
	 */
	@Override
	public GameCompanyInfo getAllCompany() throws IOException, JAXBException {
		//现在redis中查询所有的游戏
		GameCompanyInfo gameCompany = gameInfoRedisDAO.getAllComoany();
		//若redis中未找到所有游戏，则尝试去调用5173的接口查找
		if (gameCompany == null || gameCompany.getCompanyNameList() == null 
				|| gameCompany.getCompanyNameList().size() == 0) {
			logger.debug("在redis中未找到{}相关信息，尝试在数据库中查找。");
			
			//调用5173接口查询所有游戏厂商
	        HttpClient httpClient = getHttpClient();
			HttpGet getMethod = new HttpGet(getCompanyAllRequest);
	        HttpResponse response = httpClient.execute(getMethod);
	        String responseStr = StreamIOHelper.inputStreamToStr(response.getEntity().getContent(), "UTF-8");
			logger.debug("5173获取所有厂商名称信息请求url:{},返回:{}", new Object[]{getCompanyAllRequest, responseStr});
			
			JAXBContext jaxbContext = JAXBContext.newInstance(GameCompanyInfo.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			StreamSource streamSource = new StreamSource(new StringReader(responseStr));
			JAXBElement<GameCompanyInfo> je = jaxbUnmarshaller.unmarshal(streamSource, GameCompanyInfo.class);
			gameCompany = (GameCompanyInfo)je.getValue();
			
			//如果在5173的接口中有对应的信息返回，则将信息存入redis中
			if (gameCompany != null) {
				for (String company : gameCompany.getCompanyNameList()) {
					company = company.trim();			
				}
				
				logger.debug("在5173接口中查找到{}相关的信息，重新将该信息存储到redis。");
				gameInfoRedisDAO.saveGameCompany(gameCompany);
			}
		}		
		return gameCompany;
	}

	/**
	 * 根据游戏厂商名称获取游戏44343b06076d4a7a95a0ef22aac481ae
	 * 1a0d128d66b24896bf7dcf7430083cf0
	 * da451dc0df8d40e9a7aa54842687a127
	 */
	@Override
	public GameInfos getGameByCompany(String companyName) throws IOException, JAXBException {
		//先从redis中查询游戏
		GameInfos gameInfos = gameInfoRedisDAO.getGameByCompany(companyName);
		//如果redis中未找到对应的游戏，则尝试调用5173的接口查找
		if (gameInfos == null || gameInfos.getGameList() == null || gameInfos.getGameList().size() == 0) {
			logger.debug("在redis中未找到{}相关信息，尝试在数据库中查找。", companyName);
			
			//调用5173接口
			String companyNameRequest = URLEncoder.encode(companyName, "UTF-8");
			String requestUrl = String.format(getGameByCompanyRequest, companyNameRequest);

	        HttpClient httpClient = getHttpClient();
	        HttpGet getMethod = new HttpGet(requestUrl);
			HttpResponse response = httpClient.execute(getMethod);
			String responseStr = StreamIOHelper.inputStreamToStr(response.getEntity().getContent(), "UTF-8");
			logger.debug("5173根据厂商名称获取游戏信息url:{},返回:{}", new Object[]{getGameByCompanyRequest, responseStr});
			
			JAXBContext jaxbContext = JAXBContext.newInstance(GameInfos.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			StreamSource streamSource = new StreamSource(new StringReader(responseStr));
			JAXBElement<GameInfos> je = jaxbUnmarshaller.unmarshal(streamSource, GameInfos.class);
			gameInfos = (GameInfos)je.getValue();
			
			//如果在5173接口中有对应的信息返回，则保存在redis中
			if (gameInfos != null) {
				for (GameInfo gameList : gameInfos.getGameList()) {
	        		gameList.setId(gameList.getId().trim());
	        		gameList.setName(gameList.getName().trim());
				}
				
				logger.debug("在5173接口中查找到{}相关的信息，重新将该信息存储到redis。", companyName);
				gameInfoRedisDAO.saveGameByCompany(companyName, gameInfos);
			}
		}
		return gameInfos;
	}

	/**
	 * 根据游戏Id获取阵营，游戏币信息
	 */
	@Override
	public GameRaceAndMoney getRaceAndMoney(String gameId) throws IOException, JAXBException {
		//先从redis中查询游戏
		GameRaceAndMoney gameRaceAndMoney = gameInfoRedisDAO.getRaceAndMoney(gameId);
		//如果redis中未找到对应的游戏，则尝试调用5173的接口查找
		if (gameRaceAndMoney == null || gameRaceAndMoney.getGameMoneyList() == null 
				|| gameRaceAndMoney.getGameMoneyList().size() == 0) {
			logger.debug("在redis中未找到{}相关信息，尝试在数据库中查找。", gameId);
			
			//调用5173接口
			String requestUrl = String.format(getGameRaceAndMoneyRequest, gameId);
	        HttpClient httpClient = getHttpClient();
	        HttpGet getMethod = new HttpGet(requestUrl);


			HttpResponse response = httpClient.execute(getMethod);
			String responseStr = StreamIOHelper.inputStreamToStr(response.getEntity().getContent(), "UTF-8");
			 responseStr = responseStr.replace("&", "&amp;");
			logger.debug("5173根据游戏Id获取游戏阵营及游戏币信息请求url:{},返回:{}", new Object[]{requestUrl, responseStr});
			
			JAXBContext jaxbContext = JAXBContext.newInstance(GameRaceAndMoneys.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			StreamSource streamSource = new StreamSource(new StringReader(responseStr));
			JAXBElement<GameRaceAndMoneys> je = jaxbUnmarshaller.unmarshal(streamSource, GameRaceAndMoneys.class);
			GameRaceAndMoneys gameInfos = (GameRaceAndMoneys)je.getValue();
			
			/**
			 * 目前5173提供该接口有误
			 * 实际返回的是所有的游戏数据，请求参数gameId未起作用
			 */
			for(GameRaceAndMoney gameInfo : gameInfos.getRaceMoneyList()){
				if(StringUtils.equals(gameInfo.getId(), gameId)){
					//如果在5173接口中有对应的信息返回，则保存在redis中
					if (gameInfo != null) {
						gameInfo.setId(gameInfo.getId());
						if(gameInfo.getGameRaceList() != null && gameInfo.getGameRaceList().size() > 0){
							for (GameRaceInfo raceList : gameInfo.getGameRaceList()) {
								raceList.setId(raceList.getId().trim());
								raceList.setName(raceList.getName().trim());
							}
						}
						if (gameInfo.getGameMoneyList() != null && gameInfo.getGameMoneyList().size() > 0) {
							for (GameMoneyInfo moneyList : gameInfo.getGameMoneyList()) {
								moneyList.setId(moneyList.getId().trim());
								moneyList.setName(moneyList.getName().trim());
							}
						}
						
						logger.debug("在5173接口中查找到{}相关的信息，重新将该信息存储到redis。", gameId);
						gameInfoRedisDAO.svaeGameRaceAndMoney(gameInfo);
					}
					return gameInfo;
				}
			}		
		}
		return gameRaceAndMoney;
	}

	/**
	 * 根据currentUrl查找对应的游戏页面
	 */
	@Override
	public AnalysisUrlResponse analysisUrl(String currentUrl) throws IOException {
		//先从redis中查询游戏
		AnalysisUrlResponse responseUrl = gameInfoRedisDAO.getGameUrl(currentUrl);
		
		//如果redis中未找到对应的游戏，则尝试调用5173的接口查找
		if (responseUrl == null) {
			logger.debug("在redis中未找到{}相关信息，尝试在数据库中查找。", currentUrl);
			
			//调用5173接口
			String requestUrl = String.format(analysisUrlRequest, currentUrl);
	        logger.debug("5173解析页面请求url:{}", currentUrl);
	        logger.debug("5173解析页面请求完整url:{}", requestUrl);
	        try {
	            HttpClient httpClient = getHttpClient();
	            HttpGet getMethod = new HttpGet(requestUrl);
	            HttpResponse response = httpClient.execute(getMethod);
	            String responseStr = StreamIOHelper.inputStreamToStr(response.getEntity().getContent(), "GB2312");
	            logger.debug("5173解析页面url请求:{},返回:{}", new Object[]{requestUrl, responseStr});
	            // 解析json
	            responseUrl = jsonMapper.fromJson(responseStr, AnalysisUrlResponse.class);
	            
	            //如果在5173接口中有对应的信息返回，则保存在redis中
	            if (responseUrl != null) {
	            	responseUrl.setGameName(responseUrl.getGameName().trim());
	            	responseUrl.setGameRegion(responseUrl.getGameRegion().trim());
	            	responseUrl.setGameServer(responseUrl.getGameServer().trim());
					//测试用
//					responseUrl.setGoodsTypeName("挑战书");
//					//获取通货类目
					responseUrl.setGoodsTypeName(responseUrl.getGoodsTypeName().trim());

	            	if (responseUrl.getGameRace() != null) {
	            		responseUrl.setGameRace(responseUrl.getGameRace().trim());
					}
	            		            	
	            	logger.debug("在5173接口中查找到{}相关的信息，重新将该信息存储到redis。", currentUrl);
	            	gameInfoRedisDAO.saveUrl(currentUrl, responseUrl);
				}
	            return responseUrl;
	        } catch (Exception ex) {
	            logger.error("5173解析页面请求url发生异常:{}", ExceptionUtils.getStackTrace(ex));
	            return null;
	        }
		}
		return responseUrl;   
    }
	
	@Override
	public GameNameAndId getIdByProp(String queryGameName, String anyName, Integer type) throws Exception {
		String GAME_NAME = "";
		if (type != null) {
			GameNameAndId gameNameAndIdRedis = gameInfoRedisDAO.getIds(queryGameName, anyName, type);
			if (gameNameAndIdRedis == null) {
				logger.debug("在redis中未找到{}相关信息，尝试去主站查找。");
				GameNameAndId gameNameAndId = new GameNameAndId();
				GameInfos gameInfos = getGameAll();
				List<GameInfo> gameInfoList = gameInfos.getGameList();
				for (GameInfo gameInfo : gameInfoList) {
					if (gameInfo == null) {
						continue;
					}

					if (!(("44343b06076d4a7a95a0ef22aac481ae").equals(gameInfo.getId()) || ("da451dc0df8d40e9a7aa54842687a127").equals(gameInfo.getId())
							|| ("1a0d128d66b24896bf7dcf7430083cf0").equals(gameInfo.getId()) || ("5865420422194d68947c3d4b79a83204").equals(gameInfo.getId())
							|| ("a36ead01453c40b584f8e1e687723f2d").equals(gameInfo.getId()) || ("3971da6971b3483987caa24bdfb30425").equals(gameInfo.getId())
							|| ("2fdfb7d3fcd84b97b1e702d02c9ee7a7").equals(gameInfo.getId()) || ("47885d8f3f0245c3948a07a571bb78ef").equals(gameInfo.getId())
							|| ("880").equals(gameInfo.getId()) || ("0ba72c47be2a46eeac63bf45d336b0ba").equals(gameInfo.getId())
							|| ("3cb8fe8afd2743e08ab577cbb525650f").equals(gameInfo.getId())
							|| ("354b46af3bc8484fa4d434fca0ec9da0").equals(gameInfo.getId())/*镇魔曲*/
							|| ("c2925c77d1a749c0b1b2b3324d5785f7").equals(gameInfo.getId())/*QQ华夏*/
							|| ("56b957c99db144ddb042541daa4df8fd").equals(gameInfo.getId())/*上古世纪*/
							|| ("c96417a69a86411d95be5d5e5c0c12fa").equals(gameInfo.getId())/*怪物猎人*/
							|| ("19217d865b294d88b775744afb7bdfa5").equals(gameInfo.getId())/*冒险岛2*/
							|| ("20c8bbc1b9794fc98bd96859624d4769").equals(gameInfo.getId()) /*'新天龙八部'*/
					 )) {
						continue;
					}
					String gameName = gameInfo.getName();
					GAME_NAME = gameName;
					String gameId = gameInfo.getId();
					gameNameAndId.setAnyName(gameName);
					gameNameAndId.setAnyId(gameId);
					gameNameAndId.setType(1);
					gameNameAndId.setGameName(GAME_NAME);
					gameInfoRedisDAO.saveIds(gameNameAndId);
					GameDetailInfo gameDetailInfo = getGameById(gameId);
					List<GameArea> gameAreaList = gameDetailInfo.getGameAreaList();
					if (gameAreaList == null || gameAreaList.size() == 0) continue;
					for (GameArea gameArea : gameAreaList) {
						String areaName = gameArea.getName();
						String areaId = gameArea.getId();
						gameNameAndId.setAnyName(areaName);
						gameNameAndId.setAnyId(areaId);
						gameNameAndId.setType(2);
						gameNameAndId.setGameName(GAME_NAME);
						gameInfoRedisDAO.saveIds(gameNameAndId);
						List<GameServer> gameServerList = gameArea.getGameServerList();
						if (gameServerList != null && gameServerList.size() != 0) {
							for (GameServer gameServer : gameServerList) {
								String serverName = gameServer.getName();
								String serverId = gameServer.getId();
								gameNameAndId.setAnyName(serverName);
								gameNameAndId.setAnyId(serverId);
								gameNameAndId.setType(3);
								gameNameAndId.setGameName(GAME_NAME);
								gameInfoRedisDAO.saveIds(gameNameAndId);
							}
						}
					}

					GameRaceAndMoney gameRaceAndMoney = getRaceAndMoney(gameId);
					List<GameRaceInfo> gameRaceInfoList = gameRaceAndMoney.getGameRaceList();
					if (gameRaceInfoList != null && gameRaceInfoList.size() > 0) {
						for (GameRaceInfo gameRaceInfo : gameRaceInfoList) {
							String raceName = gameRaceInfo.getName();
							String raceId = gameRaceInfo.getId();
							gameNameAndId.setAnyName(raceName);
							gameNameAndId.setAnyId(raceId);
							gameNameAndId.setType(4);
							gameNameAndId.setGameName(GAME_NAME);
							gameInfoRedisDAO.saveIds(gameNameAndId);
						}
					}
				}
				gameNameAndIdRedis = gameInfoRedisDAO.getIds(queryGameName, anyName, type);
				if (gameNameAndIdRedis == null) {
					return null;
				} else {
					return gameNameAndIdRedis;
				}
			}
			return gameNameAndIdRedis;
		} else {
			return null;
		}
	}
}
