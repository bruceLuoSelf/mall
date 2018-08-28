package com.wzitech.gamegold.facade.frontend.service.game;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.game.dto.QueryDiscountRequest;

/**
 *
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/14  wubiao              ZW_C_JB_00008 商城增加通货
 */
public interface IGameInfoService {
	public IServiceResponse getAllCompanies() throws IOException, JAXBException;
	
	public IServiceResponse getGameByCompany(QueryDiscountRequest request) throws IOException, JAXBException ;

	public IServiceResponse getGameById(QueryDiscountRequest queryDiscountRequest) throws IOException, JAXBException ;

	/*************ZW_C_JB_00008_20170514 ADD '根据GameId查询阵营'******/
	IServiceResponse getGameRaceInfo(QueryDiscountRequest queryDiscountRequest);

}
