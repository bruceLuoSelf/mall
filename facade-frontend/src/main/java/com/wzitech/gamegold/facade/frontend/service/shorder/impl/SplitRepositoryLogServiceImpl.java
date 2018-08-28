package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.service.shorder.ISplitRepositoryLogService;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.SplitReporitoyLogResponse;
import com.wzitech.gamegold.shorder.business.ISplitRepositoryLogManager;
import com.wzitech.gamegold.shorder.entity.SplitRepositoryLog;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 分仓日志
 */
@Service("SplitRepositoryLogService")
@Path("splitRepositoryLog")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class SplitRepositoryLogServiceImpl extends AbstractBaseService implements ISplitRepositoryLogService {
    @Autowired
    ISplitRepositoryLogManager splitRepositoryLogManager;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 分页获取分仓日志列表
     * @param purchaseOrderRequest
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @Path("queryRepositoryLogList")
    @GET
    @Override
    public IServiceResponse queryRepositoryLogList(@QueryParam("")PurchaseOrderRequest purchaseOrderRequest,  @QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize, @Context HttpServletRequest request) {
        SplitReporitoyLogResponse response=new SplitReporitoyLogResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {
            // 获取当前用户
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }
            String startTime = purchaseOrderRequest.getStartTime();
            String endTime = purchaseOrderRequest.getEndTime();
            Date startCreateTime = null;
            Date endCreateTime = null;
            if (StringUtils.isNotBlank(startTime)) {
                startCreateTime = DATE_FORMAT.parse(startTime);
            }
            if (StringUtils.isNotBlank(endTime)) {
                endCreateTime = DATE_FORMAT.parse(endTime);
            }
            if (daysBetween(startCreateTime, endCreateTime) > 7) {
                responseStatus.setStatus(ResponseCodes.Over7Days.getCode(), ResponseCodes.Over7Days.getMessage());
                return response;
            }
            if (page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = 15;
            }
            int start = (page-1) * pageSize;
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("gameName",purchaseOrderRequest.getGameName());
            paramMap.put("region", purchaseOrderRequest.getRegion());
            paramMap.put("server", purchaseOrderRequest.getServer());
            paramMap.put("gameRace", purchaseOrderRequest.getGameRace());
            paramMap.put("buyerAccount", userInfo.getLoginAccount());
            paramMap.put("gameAccount", purchaseOrderRequest.getGameAccount());
            paramMap.put("startCreateTime", startCreateTime);
            paramMap.put("endCreateTime", endCreateTime);
            paramMap.put("roleName",purchaseOrderRequest.getRoleName());
            if (StringUtils.isNotBlank(purchaseOrderRequest.getIncomeType())) {
                paramMap.put("incomeType",Integer.valueOf(purchaseOrderRequest.getIncomeType()));
            }
            if (StringUtils.isNotBlank(purchaseOrderRequest.getLogType())) {
                paramMap.put("logType",Integer.valueOf(purchaseOrderRequest.getLogType()));
            }
            GenericPage<SplitRepositoryLog> genericPage =splitRepositoryLogManager.queryListInPage(paramMap,start,
                    pageSize,"create_time",false);
            response.setSplitRepositoryLogList(genericPage.getData());
            response.setTotalCount(genericPage.getTotalCount());
            response.setPageSize(pageSize);
            response.setCurrPage(page);
            response.setTotalPage(genericPage.getTotalPageCount());
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        }
        catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取库存日志发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取库存日志发生未知异常:{}", e);
        }

        return response;
    }

    private int daysBetween(Date smdate, Date bdate) {
        try {
            if (smdate == null || bdate == null) {
                return 0;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);

            return Integer.parseInt(String.valueOf(between_days));
        } catch (Exception ex) {
            logger.error("时间差发生未知异常:{}", ex);
        }
        return 0;
    }
}
