package com.wzitech.gamegold.shrobot.service.repository.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.utils.SignHelper;
import com.wzitech.gamegold.shorder.business.IGameAccountManager;
import com.wzitech.gamegold.shorder.business.IGtrOrderManager;
import com.wzitech.gamegold.shorder.business.ISplitRepositoryLogManager;
import com.wzitech.gamegold.shorder.business.ISplitRepositoryRequestManager;
import com.wzitech.gamegold.shorder.entity.GameAccount;
import com.wzitech.gamegold.shorder.entity.GtrShOrder;
import com.wzitech.gamegold.shorder.entity.SplitRepositoryLog;
import com.wzitech.gamegold.shorder.entity.SplitRepositoryRequest;
import com.wzitech.gamegold.shrobot.dto.BaseResponse;
import com.wzitech.gamegold.shrobot.service.order.dto.*;
import com.wzitech.gamegold.shrobot.service.repository.IRepositoryService;
import com.wzitech.gamegold.shrobot.service.repository.dto.ModifyRepositoryRequest;
import com.wzitech.gamegold.shrobot.service.repository.dto.WriteRepositoryLogRequest;
import com.wzitech.gamegold.store.business.IShStoreManager;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 分仓库存管理接口
 */
@Service("SHRepositoryService")
@Path("/repository")
@Produces("application/json;charset=UTF-8")
public class RepositoryServiceImpl extends AbstractBaseService implements IRepositoryService {
    @Autowired
    private IGameAccountManager gameAccountManager;

    @Autowired
    private ISplitRepositoryRequestManager splitRepositoryRequestManager;

    @Autowired
    private ISplitRepositoryLogManager splitRepositoryLogManager;

    @Autowired
    private IGtrOrderManager gtrOrderManager;

    @Autowired
    private IShStoreManager shStoreManager;

    /**
     * 签名KEY
     */
    @Value("${shrobot.sign_key}")
    private String signKey = "";

    /**
     * 加密KEY
     */
    @Value("${shrobot.secret_key}")
    private String secretKey = "";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 分仓订单列表
     *
     * @param params
     * @param request
     * @return
     */
    @Override
    @Path("/splitlist")
    @GET
    public IServiceResponse orderList(@QueryParam("") OrderListRequest params, @Context HttpServletRequest request) {
        OrderListResponse response = new OrderListResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            if (params == null) {
                throw new SystemException(ResponseCodes.EmptyParams.getCode(),
                        ResponseCodes.EmptyParams.getMessage());
            }
            if (StringUtils.isBlank(params.getSign())) {
                throw new SystemException(ResponseCodes.EmptySign.getCode(), ResponseCodes.EmptySign.getMessage());
            }
            String orderId = params.getOrderId();
            Integer pageSize = params.getPageSize();
            if (pageSize == null) {
                pageSize = Integer.valueOf(50);
            }

            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("pageSize", String.valueOf(pageSize));
            map.put("orderId", orderId);
            String sign2 = SignHelper.sign2(map, signKey, "UTF-8");
            if (!StringUtils.equals(params.getSign(), sign2)) {
                throw new SystemException(ResponseCodes.InvalidSign.getCode(), ResponseCodes.InvalidSign.getMessage());
            }

            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("pageSize", String.valueOf(pageSize));
            if (StringUtils.isNotBlank(orderId)) {
                queryMap.put("orderId", orderId);
            } else {
                queryMap.put("status", SplitRepositoryRequest.S_WAIT_PROCESS);
            }
            List<GtrShOrder> list = gtrOrderManager.querySplitOrderListInPage(queryMap, 0, pageSize, "id", true);
            response.setOrders(list);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("分仓订单列表接口发生异常", ex);
        }
        return response;
    }

    /**
     * 更新库存
     *
     * @param params
     * @return
     */
    @Override
    @GET
    @Path("update")
    public IServiceResponse update(@QueryParam("") ModifyRepositoryRequest params) {
        BaseResponse response = new BaseResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            if (params == null) {
                throw new SystemException(ResponseCodes.EmptyParams.getCode(),
                        ResponseCodes.EmptyParams.getMessage());
            }
            if (StringUtils.isBlank(params.getOrderId())) {
                throw new SystemException(ResponseCodes.EmptyOrderId.getCode(),
                        ResponseCodes.EmptyOrderId.getMessage());
            }
//            if (StringUtils.isBlank(params.getRoleName())) {
//                throw new SystemException(ResponseCodes.EmptyRoleName.getCode(), ResponseCodes.EmptyRoleName.getMessage());
//            }
            if (params.getRoleLevel() == null) {
                throw new SystemException(ResponseCodes.EmptyLevl.getCode(), ResponseCodes.EmptyLevl.getMessage());
            }
            if (params.getCount() == null) {
                throw new SystemException(ResponseCodes.EmptyGoldCount.getCode(), ResponseCodes.EmptyGoldCount.getMessage());
            }
            if (params.getFmRoleLevel() == null) {
                throw new SystemException(ResponseCodes.EmptyLevl.getCode(), ResponseCodes.EmptyLevl.getMessage());
            }
            if (params.getFmCount() == null) {
                throw new SystemException(ResponseCodes.EmptyGoldCount.getCode(), ResponseCodes.EmptyGoldCount.getMessage());
            }
            if (StringUtils.isBlank(params.getSign())) {
                throw new SystemException(ResponseCodes.EmptySign.getCode(), ResponseCodes.EmptySign.getMessage());
            }

            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("orderId", params.getOrderId());
            //map.put("roleName", params.getRoleName());
            map.put("roleLevel", params.getRoleLevel() == null ? "" : String.valueOf(params.getRoleLevel()));
            map.put("count", params.getCount() == null ? "" : String.valueOf(params.getCount()));
            map.put("fmRoleLevel", params.getFmRoleLevel() == null ? "" : String.valueOf(params.getFmRoleLevel()));
            map.put("fmCount", params.getFmCount() == null ? "" : String.valueOf(params.getFmCount()));
            String sign2 = SignHelper.sign2(map, signKey, "UTF-8");
            if (!StringUtils.equals(params.getSign(), sign2)) {
                throw new SystemException(ResponseCodes.InvalidSign.getCode(), ResponseCodes.InvalidSign.getMessage());
            }

//            // 查询分仓订单
//            SplitRepositoryRequest order = splitRepositoryRequestManager.queryByOrderId(params.getOrderId());
//            if (order == null)
//                throw new SystemException(ResponseCodes.NotExistSplitRepoOrder.getCode(),
//                        ResponseCodes.NotExistSplitRepoOrder.getMessage());
//
//            // 更新角色库存和等级
//            gameAccountManager.updateRepositoryCountAndLevel(order.getBuyerAccount(), order.getGameName(), order.getRegion(),
//                    order.getServer(), order.getGameRace(), order.getGameAccount(), params.getRoleName(),
//                    params.getCount(), params.getRoleLevel());

            //分仓结果
            shStoreManager.spliteResult(params.getOrderId(), params.getCount(), params.getRoleLevel(), params.getFmCount(), params.getFmRoleLevel());

            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.error("更新库存接口发生异常", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("更新库存接口发生异常", ex);
        }

        return response;
    }

    /**
     * 写分仓日志
     *
     * @param params
     * @return
     */
    @Override
    @POST
    @Path("writelog")
    public IServiceResponse writeLog(WriteRepositoryLogRequest params) {
        BaseResponse response = new BaseResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            if (params == null) {
                throw new SystemException(ResponseCodes.IllegalArguments.getCode(),
                        ResponseCodes.IllegalArguments.getMessage());
            }
            if (StringUtils.isBlank(params.getOrderId())) {
                throw new SystemException(ResponseCodes.EmptyOrderId.getCode(),
                        ResponseCodes.EmptyOrderId.getMessage());
            }

            if (StringUtils.isBlank(params.getLog())) {
                throw new SystemException(ResponseCodes.EmptyLogOperateInfo.getCode(),
                        ResponseCodes.EmptyLogOperateInfo.getMessage());
            }

            if (StringUtils.isBlank(params.getSign())) {
                throw new SystemException(ResponseCodes.EmptySign.getCode(), ResponseCodes.EmptySign.getMessage());
            }

            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("orderId", params.getOrderId());
            map.put("log", params.getLog());
//            map.put("img", params.getImg());
            String sign2 = SignHelper.sign2(map, signKey, "UTF-8");
            if (!StringUtils.equals(params.getSign(), sign2)) {
                throw new SystemException(ResponseCodes.InvalidSign.getCode(), ResponseCodes.InvalidSign.getMessage());
            }

            // 查询分仓订单
            SplitRepositoryRequest order = splitRepositoryRequestManager.queryByOrderId(params.getOrderId());
            if (order == null)
                throw new SystemException(ResponseCodes.NotExistSplitRepoOrder.getCode(),
                        ResponseCodes.NotExistSplitRepoOrder.getMessage());

            SplitRepositoryLog log = new SplitRepositoryLog();
            log.setFcId(order.getId());
            log.setBuyerAccount(order.getBuyerAccount());
            log.setGameName(order.getGameName());
            log.setRegion(order.getRegion());
            log.setServer(order.getServer());
            log.setGameRace(order.getGameRace());
            log.setGameAccount(order.getGameAccount());
            log.setLog(params.getLog());
//            log.setImgPath(params.getImg());
            log.setCreateTime(new Date());
            splitRepositoryLogManager.saveLog(log);

            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.error("写分仓日志接口发生异常", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("写分仓日志接口发生异常", ex);
        }

        return response;
    }

    /**
     * 更改分仓请求状态
     *
     * @param request
     * @return
     */
    @Override
    @GET
    @Path("changestate")
    public IServiceResponse changeState(@QueryParam("") SplitOrderStateRequest request) {
        BaseResponse response = new BaseResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            String orderId = request.getOrderId();
            Integer status = request.getStatus();
            String sign = request.getSign();
            if (StringUtils.isBlank(orderId)) {
                throw new SystemException(ResponseCodes.EmptyOrderId.getCode(),
                        ResponseCodes.EmptyOrderId.getMessage());
            }
            if (status == null) {
                throw new SystemException(ResponseCodes.EmptyStatus.getCode(), ResponseCodes.EmptyStatus.getMessage());
            } else {
                if (status != SplitRepositoryRequest.S_SPLIT && status != SplitRepositoryRequest.S_FINISH) {
                    throw new SystemException(ResponseCodes.IllegalArguments.getCode(), "不是一个有效的状态值");
                }
            }
            if (StringUtils.isBlank(sign)) {
                throw new SystemException(ResponseCodes.EmptySign.getCode(), ResponseCodes.EmptySign.getMessage());
            }

            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("orderId", orderId);
            map.put("status", status == null ? "" : String.valueOf(status));
            String sign2 = SignHelper.sign2(map, signKey, "UTF-8");
            if (!StringUtils.equals(sign, sign2)) {
                throw new SystemException(ResponseCodes.InvalidSign.getCode(), ResponseCodes.InvalidSign.getMessage());
            }

            splitRepositoryRequestManager.changeState(orderId, status);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.error("更改分仓请求状态接口发生异常", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("更改分仓请求状态接口发生异常", ex);
        }

        return response;
    }

    /**
     * 提供盘库帐号信息列表
     *
     * @param pageSize
     * @param sign
     * @return
     */
    @Override
    @Path("/gameaccountlist")
    @GET
    public IServiceResponse gameAccountList(@QueryParam("pageSize") Integer pageSize, @QueryParam("sign") String sign) {
        GameAccountListResponse response = new GameAccountListResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            if (StringUtils.isBlank(sign)) {
                throw new SystemException(ResponseCodes.EmptySign.getCode(), ResponseCodes.EmptySign.getMessage());
            }
            if (pageSize == null) {
                pageSize = Integer.valueOf(50);
            }

            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("pageSize", String.valueOf(pageSize));
            String sign2 = SignHelper.sign2(map, signKey, "UTF-8");
            if (!StringUtils.equals(sign, sign2)) {
                throw new SystemException(ResponseCodes.InvalidSign.getCode(), ResponseCodes.InvalidSign.getMessage());
            }

            List<GameAccount> list = gameAccountManager.queryNeesStoreCheckListInPage(0, pageSize);
            response.setAccountDatas(list);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("提供盘库帐号信息列表接口发生异常", ex);
        }
        return response;
    }

    /**
     * 盘库
     *
     * @param gameAccountRequest
     * @return
     */
    @Override
    @Path("/updateaccount")
    @POST
    public IServiceResponse updateAccountStore(GameAccountRequest gameAccountRequest) {
        GameAccountListResponse response = new GameAccountListResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            String buyAccount = gameAccountRequest.getBuyAccount();
            String gameName = gameAccountRequest.getGameName();
            String region = gameAccountRequest.getRegion();
            String server = gameAccountRequest.getServer();
            String gameRace = gameAccountRequest.getGameRace();
            String gameAccount = gameAccountRequest.getGameAccount();
            String roleName = gameAccountRequest.getRoleName();
            Long count = gameAccountRequest.getCount();
            String sign = gameAccountRequest.getSign();
            if (StringUtils.isBlank(sign)) {
                throw new SystemException(ResponseCodes.EmptySign.getCode(), ResponseCodes.EmptySign.getMessage());
            }

            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("buyAccount", buyAccount);
            map.put("gameName", gameName);
            map.put("region", region);
            map.put("server", server);
            map.put("gameRace", gameRace);
            map.put("gameAccount", gameAccount);
            map.put("roleName", roleName);
            map.put("count", count.toString());
            String sign2 = SignHelper.sign2(map, signKey, "UTF-8");
            if (!StringUtils.equals(sign, sign2)) {
                throw new SystemException(ResponseCodes.InvalidSign.getCode(), ResponseCodes.InvalidSign.getMessage());
            }

            gameAccountManager.updateRepositoryCountAndLevel(buyAccount, gameName, region, server, gameRace, gameAccount, roleName, count, null);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("盘库接口发生异常", ex);
        }
        return response;
    }
}
