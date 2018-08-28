package com.wzitech.gamegold.facade.frontend.service.order;

import com.wzitech.gamegold.facade.frontend.service.order.dto.CurrencyConfigRequest;
import com.wzitech.gamegold.facade.frontend.service.order.dto.CurrencyConfigResponse;
import com.wzitech.gamegold.facade.frontend.service.order.dto.QueryOrderRequest;
import com.wzitech.gamegold.order.entity.CurrencyConfigEO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

/**
 * Created by 340032 on 2018/3/19.
 */
public interface ICurrencyConfigService {

    /**
     * 查询通货配置
     * @param currencyConfigRequest
     * @param request
     * @return
     */
    CurrencyConfigResponse queryCurrencyConfig(@QueryParam("")CurrencyConfigRequest currencyConfigRequest);
}
