package com.wzitech.gamegold.facade.frontend.service.repository;

import com.wzitech.gamegold.facade.frontend.service.repository.dto.MConnectionResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.util.Map;

/**
 * Created by jhlcitadmin on 2017/3/16.
 */
public interface IMConnectionService {

    public MConnectionResponse getReferencePrice(String jsondate,@Context HttpServletRequest request);

}
