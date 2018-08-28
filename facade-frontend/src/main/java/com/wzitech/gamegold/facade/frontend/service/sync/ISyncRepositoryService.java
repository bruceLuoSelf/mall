package com.wzitech.gamegold.facade.frontend.service.sync;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.sync.dto.SyncRepositoryRequest;

import javax.ws.rs.QueryParam;

/**
 * Created by 340082 on 2018/6/12.
 */
public interface ISyncRepositoryService {

    IServiceResponse syncSelectRepositoryPrice(@QueryParam("")SyncRepositoryRequest request);

    IServiceResponse syncAddRepository(@QueryParam("")SyncRepositoryRequest request);

}
