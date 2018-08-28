package com.wzitech.gamegold.facade.frontend.service.vote.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.QueryServicerResponse;
import com.wzitech.gamegold.facade.frontend.service.vote.IServicerVote;
import com.wzitech.gamegold.facade.frontend.service.vote.dto.ServicerVoteRequest;
import com.wzitech.gamegold.facade.frontend.service.vote.dto.ServicerVoteResponse;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.usermgmt.business.IServicerVoteManager;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.*;

/**
 * 客服投票服务实现
 *
 * @author yemq
 */
@Service("ServicerVoteService")
@Path("servicer")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class ServicerVoteServiceImpl extends AbstractBaseService implements IServicerVote {
    @Autowired
    private IServicerVoteManager servicerVoteManager;
    @Autowired
    private IUserInfoManager userInfoManager;
    @Autowired
    private IOrderInfoManager orderInfoManager;

    /**
     * 查询客服
     *
     * @return
     */
    @Path("queryServicers")
    @GET
    public IServiceResponse queryServicers() {
        QueryServicerResponse response = new QueryServicerResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            //queryMap.put("isDeleted", false);
            List<UserInfoEO> servicers = userInfoManager.findServicers(queryMap, "ID", false);
            response.setUserInfoEOs(servicers);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("获取客服发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取客服发生异常:{}", ex);
        }
        logger.debug("获取客服响应信息:{}", response);
        return response;
    }

    /**
     * 对客服投票
     *
     * @param servicerVoteRequest
     * @param request
     */
    @Override
    @Path("incrVote")
    @POST
    public IServiceResponse incrVote(ServicerVoteRequest servicerVoteRequest, @Context HttpServletRequest request) {
        // 获取用户真实IP地址（配置在nginx）
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();
        }
        String operatorId = CurrentUserContext.getUidStr();
        String operatorName = CurrentUserContext.getUserLoginAccount();
        logger.info("servicerId:{},operatorId:{},operatorName:{},ip:{}", new Object[]{servicerVoteRequest.getServicerId(), operatorId, operatorName, ip});

        ServicerVoteResponse response = new ServicerVoteResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {
            // 查询投票者是否有商城订单,只有购买过商城订单的用户才能投票
            Date startTime = DateUtils.parseDate("2015-01-16 00:00:00", new String[]{"yyyy-MM-dd HH:mm:ss"});
            List<Integer> orderStateList = new ArrayList<Integer>();
            orderStateList.add(OrderState.Delivery.getCode());
            orderStateList.add(OrderState.Statement.getCode());
            orderStateList.add(OrderState.Receive.getCode());
            StringBuilder orderStates = new StringBuilder();
            for (int i = 0; i < orderStateList.size(); i++) {
                orderStates.append(orderStateList.get(i));
                if (i != (orderStateList.size() -1))
                    orderStates.append(",");
            }

            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("uid", operatorId);
            queryMap.put("createStartTime", startTime);
            queryMap.put("orderStates", orderStates.toString());
            int orderCount = orderInfoManager.countByMap(queryMap);
            if (orderCount == 0) {
                // 只有购买过商城订单的用户才能投票
                responseStatus.setStatus(ResponseCodes.VoteNoOrder.getCode(), ResponseCodes.VoteNoOrder.getMessage());
                return response;
            }

            int vote = servicerVoteManager.incrVote(servicerVoteRequest.getServicerId(), operatorId, operatorName, ip);
            response.setVote(vote);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("对客服投票发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("对客服投票发生异常:{}", ex);
        }
        logger.debug("对客服投票响应信息:{}", response);
        return response;
    }
}
