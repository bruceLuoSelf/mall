package com.wzitech.gamegold.facade.frontend.service.repository.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.dto.BaseResponse;
import com.wzitech.gamegold.facade.frontend.service.repository.IQueryRepositoryService;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.QueryLowestPriceRequest;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.QueryLowestPriceResponse;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.QueryRepositoryRequest;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 335854 on 2015/7/10.
 */
@Service("QueryRepositoryService")
@Path("queryRepository")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class QueryRepositoryServiceImpl extends AbstractBaseService implements IQueryRepositoryService{
    @Autowired
    IRepositoryManager repositoryManager;

    @Path("queryLowestPriceListservice")
    @Override
    @GET
    public IServiceResponse queryLowestPriceList(@QueryParam("")QueryLowestPriceRequest queryLowestPriceRequest, @Context HttpServletRequest request) {
        logger.debug("当前获取最低价库存请求信息:{}", queryLowestPriceRequest);
        // 初始化返回数据
        QueryLowestPriceResponse response = new QueryLowestPriceResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            String gameName=queryLowestPriceRequest.getGameName();
            String region=queryLowestPriceRequest.getRegion();
            String race=queryLowestPriceRequest.getRace();

            List<RepositoryInfo> genericPage = repositoryManager.queryLowerPriceList(gameName,region,race);
            response.setRepositoryInfos(genericPage);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());

        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("最低价库存查询发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("最低价库存查询发生异常:{}", ex);
        }
        logger.debug("最低价库存查询响应信息:{}", response);
        return response;
    }

    /**
     * 测试接口
     * @return
     */
    @Path("queryRepositoryTest")
    @POST
    @Override
    public IServiceResponse queryRepositoryTest(QueryRepositoryRequest request,@Context HttpServletRequest httpRequest) {
        BaseResponse response = new BaseResponse();
        try{
            Map<String, Object> paramMap = new HashMap<String, Object>();
            if (request != null) {
                if (request.getIsNeed() != null && request.getIsNeed()) {
                    paramMap.remove("servicerId");
                }
                if (StringUtils.isNotBlank(request.getGameName()) && StringUtils.isBlank(request.getRegion())) {
                    // 按游戏属性2中的游戏名称和游戏服务器进行模糊查询
                    paramMap.put("gameName", request.getGameName());
                    paramMap.put("server", request.getServer());
                } else {
                    // 按游戏属性中的游戏名称、游戏区、游戏服务器进行精准查询
                    paramMap.put("backGameName", request.getGameName());
                    paramMap.put("backRegion", request.getRegion());
                    paramMap.put("backServer", request.getServer());
                }

                paramMap.put("sellableCount", request.getGoldCount());
                paramMap.put("orderUnitPrice", request.getUnitPrice());
//                paramMap.put("loginAccount", request.getLoginAccount());
//                paramMap.put("gameAccount", request.getGameAccount());
                paramMap.put("isNeed", request.getIsNeed());
                paramMap.put("goodsTypeName",request.getGoodsTypeName());//ZW_C_JB_00008 add
                // 卖家是否在线
                paramMap.put("isOnline", (request.getSellerIsOnline()));
            }
            boolean salableRepository = true;
            if (StringUtils.isNotBlank(request.getGoodsTypeName()) && !(request.getGoodsTypeName().equals("游戏币") && "地下城与勇士".equals(request.getGameName()))){
                salableRepository =false;
            }
            logger.info("查询库存列表:" + paramMap);
            GenericPage<RepositoryInfo> genericPage = repositoryManager.queryRepository(paramMap, request.getLimit(), request.getStart(), "UNIT_PRICE", true);
            List<RepositoryInfo> list = genericPage.getData();
            Long totalCount = genericPage.getTotalCount();
            response.setData(list);
            response.setTotalCount(totalCount);
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
