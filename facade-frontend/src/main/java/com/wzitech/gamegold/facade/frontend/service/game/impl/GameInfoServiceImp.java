package com.wzitech.gamegold.facade.frontend.service.game.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import javax.ws.rs.*;
import javax.xml.bind.JAXBException;

import com.wzitech.gamegold.common.game.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.game.*;

import com.wzitech.gamegold.facade.frontend.service.game.IGameInfoService;
import com.wzitech.gamegold.facade.frontend.service.game.dto.QueryDiscountRequest;
import com.wzitech.gamegold.facade.frontend.service.game.dto.QueryGameInfoResponse;

/**
 *
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * yubaihai 创建于 2014-6-28 下午3:43:33
 * 2017/05/14  wubiao           ZW_C_JB_00008 商城增加通货
 */
@Service("GameInfoService")
@Path("gameinfo")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class GameInfoServiceImp extends AbstractBaseService implements IGameInfoService{
	@Autowired
	IGameInfoManager gameInfoManager;

	private List<GameRaceInfo> gameRaceList = new ArrayList<GameRaceInfo>();
	
	@Path("getallcompanies")
	@POST
	@Override
	public IServiceResponse getAllCompanies() throws IOException, JAXBException{
		// 初始化返回数据
		QueryGameInfoResponse response = new QueryGameInfoResponse();
		ResponseStatus responseStatus = new ResponseStatus();
		response.setResponseStatus(responseStatus);
	try{
		GameCompanyInfo gameCompanyInfo = gameInfoManager.getAllCompany();
		response.setCompanielist(gameCompanyInfo.getCompanyNameList());
		responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());	
	} catch (SystemException ex) {
		// 捕获系统异常
		responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
		logger.error("当前查询游戏发生异常:{}", ex);
	} catch (Exception ex) {
		// 捕获未知异常
		responseStatus.setStatus(
				ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
		logger.error("当前查询游戏发生未知异常:{}", ex);
	}
	logger.debug("当前查询游戏响应信息:{}", response);
	return response;
	}
	
	@Path("getgamebycompany")
	@POST
	@Override
	public IServiceResponse getGameByCompany(QueryDiscountRequest queryDiscountRequest) throws IOException, JAXBException {
		       // 初始化返回数据
				QueryGameInfoResponse response = new QueryGameInfoResponse();
				ResponseStatus responseStatus = new ResponseStatus();
				response.setResponseStatus(responseStatus);
			try{
				GameInfos gameInfos = gameInfoManager.getGameByCompany(queryDiscountRequest.getCompanie());
				response.setGameList(gameInfos.getGameList());
				responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());	
			} catch (SystemException ex) {
				// 捕获系统异常
				responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
				logger.error("当前查询游戏发生异常:{}", ex);
			} catch (Exception ex) {
				// 捕获未知异常
				responseStatus.setStatus(
						ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
				logger.error("当前查询游戏发生未知异常:{}", ex);
			}
			logger.debug("当前查询游戏响应信息:{}", response);
			return response;
	}
	
	@Path("getdetailgame")
	@POST
	@Override
	public IServiceResponse getGameById(QueryDiscountRequest queryDiscountRequest) throws IOException, JAXBException {
		QueryGameInfoResponse response = new QueryGameInfoResponse();
		ResponseStatus responseStatus = new ResponseStatus();
		response.setResponseStatus(responseStatus);
	try{
		GameDetailInfo gameDetailInfo = gameInfoManager.getGameById(queryDiscountRequest.getGameid());
		response.setGameDetailInfo(gameDetailInfo);
		responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());	
	} catch (SystemException ex) {
		// 捕获系统异常
		responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
		logger.error("当前查询游戏发生异常:{}", ex);
	} catch (Exception ex) {
		// 捕获未知异常
		responseStatus.setStatus(
				ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
		logger.error("当前查询游戏发生未知异常:{}", ex);
	}
	logger.debug("当前查询详细游戏响应信息:{}", response);
	 return response;
	}

	/**
	 * ZW_C_JB_00008_20170514 ADD '根据GameId查询阵营'
	 *
	 * @param queryDiscountRequest
	 * @return
	 */
	@Path("getGameRaceInfo")
	@POST
	@Override
	public IServiceResponse getGameRaceInfo(QueryDiscountRequest queryDiscountRequest) {
		QueryGameInfoResponse response = new QueryGameInfoResponse();
		ResponseStatus responseStatus = new ResponseStatus();
		response.setResponseStatus(responseStatus);
		try {
			GameRaceAndMoney gameRaceAndMoney = gameInfoManager.getRaceAndMoney(queryDiscountRequest.getGameid());
			if (gameRaceAndMoney != null) {
				gameRaceList = gameRaceAndMoney.getGameRaceList();
				response.setGameRaceInfoList(gameRaceList);
				responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
			}
		} catch (IOException ex) {
			responseStatus.setStatus(
					ResponseCodes.QeuryGameRaceInfoError.getCode(), ResponseCodes.QeuryGameRaceInfoError.getMessage());
			logger.error("当前查询游戏阵营发生异常:{}", ex);
		} catch (JAXBException e) {
			responseStatus.setStatus(
					ResponseCodes.QeuryGameRaceInfoParseXMLError.getCode(), ResponseCodes.QeuryGameRaceInfoParseXMLError.getMessage());
			logger.error("查询游戏阵营信息XML转换异常", e);
		}
		logger.debug("当前查询游戏阵营响应信息:{}", response);
		return response;
	}

}
