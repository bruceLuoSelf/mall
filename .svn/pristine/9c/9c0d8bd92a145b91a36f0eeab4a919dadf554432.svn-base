package com.wzitech.gamegold.facade.frontend.service.repository.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.enums.OrderType;
import com.wzitech.gamegold.common.enums.RefererType;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import com.wzitech.gamegold.facade.frontend.service.repository.IMConnectionService;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.MConnectionResponse;
import com.wzitech.gamegold.goods.business.IMConnectionManager;
import com.wzitech.gamegold.goods.business.IWarningManager;
import com.wzitech.gamegold.goods.entity.ReferencePrice;
import com.wzitech.gamegold.goods.entity.Warning;
import com.wzitech.gamegold.shorder.business.IShGameConfigManager;
import com.wzitech.gamegold.shorder.business.ISystemConfigManager;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jhlcitadmin on 2017/3/16.
 */
@Service("MConnectionService")
@Path("mConnection")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class MConnectionServiceImpl extends AbstractBaseService implements IMConnectionService  {

    @Autowired
    private IMConnectionManager mConnectionManager;

    @Autowired
    private IShGameConfigManager shGameConfigManager;

    @Autowired
    private ISystemConfigManager systemConfigManager;

    @Autowired
    private IWarningManager warningManager;

    /**
     * 计算参考价算法接口
     *
     * @return
     */

    @Override
    @POST
    @Path("getReferencePrice")
    public MConnectionResponse getReferencePrice(String jsondate, @Context HttpServletRequest request) {
        MConnectionResponse response = new MConnectionResponse();
        try {
            JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
            ReferencePrice requestRfp = jsonMapper.fromJson(jsondate, ReferencePrice.class);
            //当游戏或游戏名，区，服不存在的时候
            if (StringUtils.isBlank(requestRfp.getGameName()) || StringUtils.isBlank(requestRfp.getServerName()) || StringUtils.isBlank(requestRfp.getRegionName())) {
                return response;
            }
            if( StringUtils.isBlank(requestRfp.getGoodsTypeName())){
                requestRfp.setGoodsTypeName("游戏币");
            }
            ReferencePrice selectRfp = mConnectionManager.getReferencePrice(requestRfp);
            if(selectRfp==null || selectRfp.getUnitPrice()==null){
                throw new SystemException(ResponseCodes.NotAvaliableRegionAndServer.getCode(),
                        ResponseCodes.NotAvaliableRegionAndServer.getMessage());
            }
            Map map = new HashMap();
            map.put("gameName", requestRfp.getGameName());
            map.put("goodsTypeName",requestRfp.getGoodsTypeName());
            //金币的前提默认查询主站配置
            map.put("tradeType", RefererType.mOrder.getCode());
            List<Warning> warningList = warningManager.queryByMap(map, "ID", true);
            if(warningList!=null && warningList.size()==1){
                response.setmAddMsg(warningList.get(0).getWarning());
            }
            map.remove("gameName");
            warningList = warningManager.queryByMap(map, "ID", true);
            if(warningList!=null && warningList.size()>=1){
                response.setmAddMsg(warningList.get(0).getWarning());
            }
            map.put("gameName", requestRfp.getGameName());
            map.remove("goodsTypeName");
            warningList = warningManager.queryByMap(map, "ID", true);
            if(warningList!=null && warningList.size()>=1){
                response.setmAddMsg(warningList.get(0).getWarning());
            }

//            response.setmAddMsg(systemConfigManager.getSystemConfigByKey(SystemConfigEnum.M_ADD_ORDER_MSG.getKey()).getConfigValue());
            ShGameConfig configByGameName = shGameConfigManager.getConfigByGameName(requestRfp.getGameName(), requestRfp.getGoodsTypeName(), null, true);
            response.setTotalCount(selectRfp.getTotalAccount().toString());
            response.setUnitPrice(selectRfp.getUnitPrice().toString());
            response.setMoneyName(selectRfp.getMoneyName().toString());
            response.setShGameConfig(configByGameName);
        } catch (SystemException ex) {
            response.setResponseStatus(new ResponseStatus(ex.getErrorCode(), ex.getArgs()[0].toString()));
            logger.error("当前查询订单发生异常:{}", ex);
        }
        return response;
    }
}
