package com.wzitech.gamegold.facade.frontend.service.repository;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.QueryLowestPriceRequest;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.QueryRepositoryRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * Created by 335854 on 2015/7/10.
 */
public interface IQueryRepositoryService {
    IServiceResponse queryLowestPriceList(QueryLowestPriceRequest queryLowestPriceRequest, HttpServletRequest request);

    IServiceResponse queryRepositoryTest(QueryRepositoryRequest request,@Context HttpServletRequest httpRequest);
}
