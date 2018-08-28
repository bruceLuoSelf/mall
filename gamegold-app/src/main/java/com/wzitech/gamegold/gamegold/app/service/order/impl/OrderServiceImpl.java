
package com.wzitech.gamegold.gamegold.app.service.order.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.BeanMapper;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.enums.RefundReason;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.common.utils.RequestRealIp;
import com.wzitech.gamegold.common.utils.SignHelper;
import com.wzitech.gamegold.gamegold.app.service.order.IOrderService;
import com.wzitech.gamegold.gamegold.app.service.order.dto.*;
import com.wzitech.gamegold.order.business.IInsuranceSettingsManager;
import com.wzitech.gamegold.goods.business.IRepositoryConfineManager;
import com.wzitech.gamegold.goods.dao.IGoodsInfoDBDAO;
import com.wzitech.gamegold.goods.dao.IReferencePriceDao;
import com.wzitech.gamegold.goods.dao.IReferencePriceRedisDao;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.entity.*;
import com.wzitech.gamegold.shorder.business.ISystemConfigManager;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author lingchengshu
 *         <p>
 *         Update History
 *         Date         Name            Reason For Update
 *         ----------------------------------------------
 *         2017/5/16    wrf              ZW_C_JB_00008商城增加通货
 */
@Service("OrderService")
@Path("order")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class OrderServiceImpl extends AbstractBaseService implements IOrderService {
    @Autowired
    IOrderInfoManager orderInfoManager;
    @Autowired
    IInsuranceSettingsManager iInsuranceSettingsManager;

    @Autowired
    IRepositoryConfineManager repositoryConfineManager;

    @Autowired
    IReferencePriceDao referencePriceDao;

    @Autowired
    IReferencePriceRedisDao referencePriceRedisDao;

    @Autowired
    IGoodsInfoDBDAO goodsInfoDBDAO;

    @Autowired
    private ISystemConfigManager systemConfigManager;

    @Value("${encrypt.5173.key}")
    private String encryptKey = "";

    /**
     * 根据游戏获取保险配置
     *
     * @param orderInsuranceQueryRequest
     * @param request
     * @return
     */

    @Path("queryinsurance")
    @POST
    @Override
    public IServiceResponse orderQueryInsurance(OrderInsuranceQueryRequest orderInsuranceQueryRequest, @Context HttpServletRequest request) {
        logger.debug("根据游戏获取保险配置查询请求：{}", orderInsuranceQueryRequest);
        // 初始化返回数据
        OrderInsuranceQueryResponse response = new OrderInsuranceQueryResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            // 查询保险配置
            InsuranceSettings insuranceSettings = iInsuranceSettingsManager.queryByGameName(orderInsuranceQueryRequest.getGameName());
            response.setInsuranceSettings(insuranceSettings);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());

        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("根据游戏获取保险配置查询发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(
                    ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据游戏获取保险配置查询发生未知异常:{}", ex);
        }
        logger.debug("根据游戏获取保险配置查询响应信息:{}", response);
        return response;
    }

    /**
     * 新增订单
     *
     * @param addOrderRequest
     * @param request
     * @return
     */
    @Override
    @POST
    @Path("addorder")
    public IServiceResponse addOrder(AddOrderRequest addOrderRequest, @Context HttpServletRequest request) {
        logger.debug("当前新增订单:{}", addOrderRequest);
        // 初始化返回数据
        AddOrderResponse response = new AddOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            // 校验MD5,页面传递参数仅有
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s",
                    addOrderRequest.getGameName(), addOrderRequest.getRegion(), addOrderRequest.getServer(), addOrderRequest.getGameRace(), addOrderRequest.getGameId(), addOrderRequest.getRegionId(),
                    addOrderRequest.getServerId(), addOrderRequest.getRaceId(), addOrderRequest.getTitle(), addOrderRequest.getGoodsCat(), addOrderRequest.getGoodsId(), addOrderRequest.getUnitPrice(),
                    addOrderRequest.getGoldCount(), addOrderRequest.getServicerId(), addOrderRequest.getMobileNumber(), addOrderRequest.getQq(), addOrderRequest.getReceiver(), addOrderRequest.getGameLevel(),
                    addOrderRequest.getSellerLoginAccount(), addOrderRequest.getMoneyName(), addOrderRequest.getRefererType(), addOrderRequest.getInternetBar(), addOrderRequest.getUserId(),
                    addOrderRequest.getUserAccount(), addOrderRequest.getBuyInsurance(), encryptKey));

            if (!StringUtils.equals(toEncrypt, addOrderRequest.getSign())) {
                response.setMsg(response.getMsg() + " (签名不一致)");
                return response;
            }

            //将请求参数转化成一个OrderInfoEO
            OrderInfoEO orderInfo = BeanMapper.map(addOrderRequest, OrderInfoEO.class);
            orderInfo.setUid(addOrderRequest.getUserId());
            orderInfo.setUserAccount(addOrderRequest.getUserAccount());
            orderInfo = orderInfoManager.addOrderInfo(orderInfo, addOrderRequest.getDiscount());
            response.setOrderInfo(orderInfo);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("当前新增订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(
                    ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("当前新增订单发生未知异常:{}", ex);
        }
        logger.debug("当前新增订单响应信息:{}", response);
        return response;
    }

    /**
     * 获取订单列表
     *
     * @param queryOrderRequest
     * @param request
     * @return
     */
    @Path("queryorder")
    @POST
    @Override
    public IServiceResponse queryOrder(QueryOrderRequest queryOrderRequest, @Context HttpServletRequest request) {
        logger.debug("当前查询订单:{}", queryOrderRequest);
        // 初始化返回数据
        QueryOrderResponse response = new QueryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            // 校验MD5
            String toEncrypt;
            String goodsTypeName = ServicesContants.GOODS_TYPE_GOLD;

            SystemConfig systemConfig = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.NEW_ENCRYPT_GOODSTYPENAME_FOR_APP_OR_NOT.getKey());
            if (systemConfig != null) {
                //开启通货
                toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s",
                        queryOrderRequest.getPageSize(), queryOrderRequest.getStart(), queryOrderRequest.getOrderBy(), queryOrderRequest.getOrderState(),
                        queryOrderRequest.getStartOrderCreate(), queryOrderRequest.getEndOrderCreate(), queryOrderRequest.getOrderId(),
                        queryOrderRequest.getGameName(), queryOrderRequest.getRegion(), queryOrderRequest.getServer(), queryOrderRequest.getUserId(),
                        queryOrderRequest.getUserAccount(), queryOrderRequest.getGoodsTypeName(), encryptKey));
                goodsTypeName = queryOrderRequest.getGoodsTypeName();
            } else {
                toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s",
                        queryOrderRequest.getPageSize(), queryOrderRequest.getStart(), queryOrderRequest.getOrderBy(), queryOrderRequest.getOrderState(),
                        queryOrderRequest.getStartOrderCreate(), queryOrderRequest.getEndOrderCreate(), queryOrderRequest.getOrderId(),
                        queryOrderRequest.getGameName(), queryOrderRequest.getRegion(), queryOrderRequest.getServer(), queryOrderRequest.getUserId(),
                        queryOrderRequest.getUserAccount(), encryptKey));
            }

            if (!StringUtils.equals(toEncrypt, queryOrderRequest.getSign())) {
                response.setMsg(response.getMsg() + " (签名不一致)");
                return response;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startOrderCreate = null;
            Date endOrderCreate = null;
            String startOrder = queryOrderRequest.getStartOrderCreate();
            String endOrder = queryOrderRequest.getEndOrderCreate();
            if (StringUtils.isNotBlank(startOrder)) {
                startOrder += " 00:00:00";
                startOrderCreate = (Date) sdf.parse(startOrder);
            }
            if (StringUtils.isNotBlank(endOrder)) {
                endOrder += " 23:59:59";
                endOrderCreate = (Date) sdf.parse(endOrder);
            }

            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("uid", queryOrderRequest.getUserId());
            queryMap.put("orderId", queryOrderRequest.getOrderId());
            queryMap.put("orderState", queryOrderRequest.getOrderState());
            queryMap.put("gameName", queryOrderRequest.getGameName());
            queryMap.put("region", queryOrderRequest.getRegion());
            queryMap.put("server", queryOrderRequest.getServer());
            queryMap.put("createStartTime", startOrderCreate);
            queryMap.put("createEndTime", endOrderCreate);
            queryMap.put("orderStates", queryOrderRequest.getOrderStates());
            queryMap.put("goodsTypeName", goodsTypeName);
            //查询开始指针位置
            Integer index = queryOrderRequest.getStart() * queryOrderRequest.getPageSize();
            GenericPage<OrderInfoEO> orders = orderInfoManager.queryOrderInfo(queryMap, queryOrderRequest.getOrderBy(),
                    false, queryOrderRequest.getPageSize(), index);

            response.setOrders(orders);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("当前查询订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(
                    ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("当前查询订单发生未知异常:{}", ex);
        }
        logger.debug("当前查询订单响应信息:{}", response);
        return response;
    }

    /**
     * 根据多种状态查询订单列表
     *
     * @param queryOrderRequest
     * @param request
     * @return
     */
    @Path("queryOrderByType")
    @POST
    @Override
    public IServiceResponse queryOrderByType(QueryOrderTypeRequest queryOrderRequest, @Context HttpServletRequest request) {
        logger.debug("当前查询订单:{}", queryOrderRequest);
        // 初始化返回数据
        QueryOrderResponse response = new QueryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            // 校验MD5
            String toEncrypt;
            String goodsTypeName = ServicesContants.GOODS_TYPE_GOLD;

            SystemConfig systemConfig = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.NEW_ENCRYPT_GOODSTYPENAME_FOR_APP_OR_NOT.getKey());
            if (systemConfig != null && systemConfig.getEnabled()) {
                toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s",
                        queryOrderRequest.getPageSize(), queryOrderRequest.getStart(), queryOrderRequest.getOrderBy(), queryOrderRequest.getOrderState(),
                        queryOrderRequest.getStartOrderCreate(), queryOrderRequest.getEndOrderCreate(), queryOrderRequest.getOrderId(),
                        queryOrderRequest.getGameName(), queryOrderRequest.getRegion(), queryOrderRequest.getServer(), queryOrderRequest.getUserId(),
                        queryOrderRequest.getUserAccount(), queryOrderRequest.getGoodsTypeName(), encryptKey));
            } else {
                toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s",
                        queryOrderRequest.getPageSize(), queryOrderRequest.getStart(), queryOrderRequest.getOrderBy(), queryOrderRequest.getOrderState(),
                        queryOrderRequest.getStartOrderCreate(), queryOrderRequest.getEndOrderCreate(), queryOrderRequest.getOrderId(),
                        queryOrderRequest.getGameName(), queryOrderRequest.getRegion(), queryOrderRequest.getServer(), queryOrderRequest.getUserId(),
                        queryOrderRequest.getUserAccount(), encryptKey));
            }

            if (!StringUtils.equals(toEncrypt, queryOrderRequest.getSign())) {
                response.setMsg(response.getMsg() + " (签名不一致)");
                return response;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startOrderCreate = null;
            Date endOrderCreate = null;
            String startOrder = queryOrderRequest.getStartOrderCreate();
            String endOrder = queryOrderRequest.getEndOrderCreate();
            if (StringUtils.isNotBlank(startOrder)) {
                startOrder += " 00:00:00";
                startOrderCreate = (Date) sdf.parse(startOrder);
            }
            if (StringUtils.isNotBlank(endOrder)) {
                endOrder += " 23:59:59";
                endOrderCreate = (Date) sdf.parse(endOrder);
            }
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("uid", queryOrderRequest.getUserId());
            queryMap.put("orderId", queryOrderRequest.getOrderId());
            queryMap.put("orderStates", queryOrderRequest.getOrderState());
            queryMap.put("gameName", queryOrderRequest.getGameName());
            queryMap.put("region", queryOrderRequest.getRegion());
            queryMap.put("server", queryOrderRequest.getServer());
            queryMap.put("createStartTime", startOrderCreate);
            queryMap.put("createEndTime", endOrderCreate);
            queryMap.put("goodsTypeName", goodsTypeName); /**ZW_C_JB_00008_2017/5/16 add **/
            //查询开始指针位置
            Integer index = queryOrderRequest.getStart() * queryOrderRequest.getPageSize();
            GenericPage<OrderInfoEO> orders = orderInfoManager.queryOrderInfo(queryMap, queryOrderRequest.getOrderBy(),
                    false, queryOrderRequest.getPageSize(), index);

            response.setOrders(orders);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("当前查询订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(
                    ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("当前查询订单发生未知异常:{}", ex);
        }
        logger.debug("当前查询订单响应信息:{}", response);
        return response;
    }


    /**
     * 获取订单详情
     *
     * @param queryOrderByIdRequest
     * @param request
     * @return
     */
    @Path("queryorderbyid")
    @POST
    @Override
    public IServiceResponse queryOrderById(QueryOrderRequest queryOrderByIdRequest, @Context HttpServletRequest request) {
        logger.debug("当前查询订单:{}", queryOrderByIdRequest);
        // 初始化返回数据
        QueryOrderResponse response = new QueryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            //校验订单号是否为空
            if (StringUtils.isEmpty(queryOrderByIdRequest.getOrderId())) {
                responseStatus.setStatus(ResponseCodes.EmptyOrderId.getCode(),
                        ResponseCodes.EmptyOrderId.getMessage());
                return response;
            }
            // 校验MD5
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s",
                    queryOrderByIdRequest.getOrderId(), queryOrderByIdRequest.getUserId(),
                    queryOrderByIdRequest.getUserAccount(), encryptKey));

            if (!StringUtils.equals(toEncrypt, queryOrderByIdRequest.getSign())) {
                response.setMsg(response.getMsg() + " (签名不一致)");
                return response;
            }

            OrderInfoEO order = orderInfoManager.selectById(queryOrderByIdRequest.getOrderId());
            if (order != null) {
                //判断查询出来的订单是否属于当前用户
                if (!order.getUserAccount().equals(queryOrderByIdRequest.getUserAccount())) {
                    responseStatus.setStatus(ResponseCodes.NotYourOrder.getCode(), ResponseCodes.NotYourOrder.getMessage());
                    return response;
                }
                //超过4/24小时,逻辑上设置isEvaluate/isReEvaluate为true，以用于前台判断评论/追加评论的显示与隐藏
                if ((order.getOrderState() == OrderState.Delivery.getCode() || order.getOrderState() == OrderState.Statement.getCode()
                        || order.getOrderState() == OrderState.Refund.getCode()) && order.getSendTime() != null) {
                    if (order.getIsEvaluate() == null) {
                        order.setIsEvaluate(isDelay(4, order.getSendTime()));
                    }
                    if (order.getIsReEvaluate() == null) {
                        order.setIsReEvaluate(isDelay(24, order.getSendTime()));
                    }
                }
            }
            //如果订单状态为已取消
            if (order.getOrderState() == OrderState.Cancelled.getCode()) {
                //退单原因判空,如果为空，手动添加一个退单原因
                if (order.getCancelReson() == null) {
                    order.setCancelReson("其他取消原因");

                }
            }

            //如果订单状态为已退款，并且退单原因为空，手动设置退单原因
            if (order.getOrderState() == OrderState.Refund.getCode()) {
                //获取退款原因
                if (order.getRefundReason() != null) {
                    order.setCancelReson(RefundReason.getReasonByCode(order.getRefundReason()).getName());
                } else {
                    order.setCancelReson(order.getCancelReson() == null ? "其他退款原因" : order.getCancelReson());
                }
            }

            //start 环信一对一 by汪俊杰 20170515
            UserInfoEO userInfo = order.getServicerInfo();
            if (userInfo != null) {
                userInfo.setHxAppPwd(null);
                userInfo.setHxPwd(null);
            }
            //end

            response.setCurrentDate(new Date().getTime());
            response.setOrderInfoEO(order);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("当前查询订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(
                    ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("当前查询订单发生未知异常:{}", ex);
        }
        logger.debug("当前查询订单响应信息:{}", response);
        return response;
    }

    private boolean isDelay(int hour, Date sendTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(sendTime);
        c.add(Calendar.HOUR_OF_DAY, hour);
        long afterHour = c.getTimeInMillis();
        Date now = new Date();
        if (now.getTime() > afterHour) {
            return true;
        }
        return false;
    }


    /**
     * 新增订单(含单价判断)
     *
     * @param addOrderRequest
     * @param request
     * @return
     */
    @Override
    @POST
    @Path("addorderWithUnitPrice")

    public IServiceResponse addOrderWithUnitPrice(AddOrderRequest addOrderRequest, @Context HttpServletRequest request) {
        logger.debug("当前新增订单:{}", addOrderRequest);
        // 初始化返回数据
        AddOrderResponse response = new AddOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            String toEncrypt = null;
            SystemConfig systemConfigByKey = systemConfigManager.getTotalSystemConfigByKey(SystemConfigEnum.OPEN_OR_CLOSE_INTERFACE_FOR_M.getKey());
            //将请求参数转化成一个OrderInfoEO
            OrderInfoEO orderInfo = BeanMapper.map(addOrderRequest, OrderInfoEO.class);
            //如果开启了新版接口给M站，参数需加一个TerminalIp; 值为1 增加了新的通货treeset的验证  值为2 未加通货的旧有验证方式
            if (systemConfigByKey.getEnabled() && "1".equals(systemConfigByKey.getConfigValue())) {
                TreeSet<String> treeSet = new TreeSet<String>();
                treeSet.add(addOrderRequest.getGoodsTypeName());
                treeSet.add(addOrderRequest.getTerminalIp());
                treeSet.add(addOrderRequest.getGameName());
                treeSet.add(addOrderRequest.getRegion());
                treeSet.add(addOrderRequest.getServer());
                treeSet.add(addOrderRequest.getGameRace());
                treeSet.add(addOrderRequest.getUnitPrice().toString());
                treeSet.add(addOrderRequest.getGoldCount().toString());
                treeSet.add(addOrderRequest.getMobileNumber());
                treeSet.add(addOrderRequest.getQq());
                treeSet.add(addOrderRequest.getReceiver());
                treeSet.add(addOrderRequest.getMoneyName());
                treeSet.add(addOrderRequest.getRefererType().toString());
                treeSet.add(addOrderRequest.getUserId());
                treeSet.add(addOrderRequest.getUserAccount());
                treeSet.add(addOrderRequest.getPayment().toString());
                treeSet.add(addOrderRequest.getTitle());
                treeSet.add(addOrderRequest.getLimitPrice().toString());
                treeSet.add(addOrderRequest.getField());
                toEncrypt = EncryptHelper.md5(SignHelper.signMd5(treeSet, encryptKey));
            }
            if (systemConfigByKey.getEnabled() && "2".equals(systemConfigByKey.getConfigValue())) {
                String newFormat = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s",
                        addOrderRequest.getTerminalIp(), addOrderRequest.getGameName(), addOrderRequest.getRegion(), addOrderRequest.getServer(), addOrderRequest.getGameRace()
                        , addOrderRequest.getUnitPrice(), addOrderRequest.getGoldCount(), addOrderRequest.getMobileNumber(), addOrderRequest.getQq(),
                        addOrderRequest.getReceiver(), addOrderRequest.getMoneyName(), addOrderRequest.getRefererType(), addOrderRequest.getUserId(),
                        addOrderRequest.getUserAccount(), addOrderRequest.getPayment(), addOrderRequest.getTitle(), addOrderRequest.getLimitPrice(), encryptKey);
                logger.info("M站下单接口访问加密参数为：" + newFormat);
                toEncrypt = EncryptHelper.md5(newFormat);
                orderInfo.setGoodsTypeName(ServicesContants.GOODS_TYPE_GOLD);
            }
            if (!systemConfigByKey.getEnabled() && "1".equals(systemConfigByKey.getConfigValue())) {
                TreeSet<String> treeSet = new TreeSet<String>();
                treeSet.add(addOrderRequest.getGoodsTypeName());
                treeSet.add(addOrderRequest.getGameName());
                treeSet.add(addOrderRequest.getRegion());
                treeSet.add(addOrderRequest.getServer());
                treeSet.add(addOrderRequest.getGameRace());
                treeSet.add(addOrderRequest.getUnitPrice().toString());
                treeSet.add(addOrderRequest.getGoldCount().toString());
                treeSet.add(addOrderRequest.getMobileNumber());
                treeSet.add(addOrderRequest.getQq());
                treeSet.add(addOrderRequest.getReceiver());
                treeSet.add(addOrderRequest.getMoneyName());
                treeSet.add(addOrderRequest.getRefererType().toString());
                treeSet.add(addOrderRequest.getUserId());
                treeSet.add(addOrderRequest.getUserAccount());
                treeSet.add(addOrderRequest.getPayment().toString());
                treeSet.add(addOrderRequest.getTitle());
                treeSet.add(addOrderRequest.getLimitPrice().toString());
                treeSet.add(addOrderRequest.getField());
                toEncrypt = EncryptHelper.md5(SignHelper.signMd5(treeSet, encryptKey));
            }
            if (!systemConfigByKey.getEnabled() && "2".equals(systemConfigByKey.getConfigValue())) {
                String oldFormat = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s",
                        addOrderRequest.getGameName(), addOrderRequest.getRegion(), addOrderRequest.getServer(), addOrderRequest.getGameRace()
                        , addOrderRequest.getUnitPrice(), addOrderRequest.getGoldCount(), addOrderRequest.getMobileNumber(), addOrderRequest.getQq(),
                        addOrderRequest.getReceiver(), addOrderRequest.getMoneyName(), addOrderRequest.getRefererType(), addOrderRequest.getUserId(),
                        addOrderRequest.getUserAccount(), addOrderRequest.getPayment(), addOrderRequest.getTitle(), addOrderRequest.getLimitPrice(), encryptKey);
                // 旧接口校验MD5,页面传递参数仅有
                logger.info("M站下单旧接口访问加密参数为：" + oldFormat);
                toEncrypt = EncryptHelper.md5(oldFormat);
                orderInfo.setGoodsTypeName(ServicesContants.GOODS_TYPE_GOLD);
            }
            if (!StringUtils.equals(toEncrypt, addOrderRequest.getSign())) {
                response.setMsg(response.getMsg() + " (签名不一致)");
                return response;
            }

            if (null == orderInfo) {
                responseStatus.setStatus(ResponseCodes.NullData.getCode(), ResponseCodes.NullData.getMessage());
                return response;
            }
            //以下为正常下单逻辑
            orderInfo.setUid(addOrderRequest.getUserId());
            orderInfo.setUserAccount(addOrderRequest.getUserAccount());
            orderInfo = orderInfoManager.addOrderInfoFromM(orderInfo);
            response.setOrderInfo(orderInfo);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("当前新增订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(
                    ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("当前新增订单发生未知异常:{}", ex);
        }
        logger.debug("当前新增订单响应信息:{}", response);
        return response;
    }

    /**
     * app新增订单(含单价判断)
     *
     * @param addOrderRequest
     * @param request
     * @return
     */
    @Override
    @POST
    @Path("addorderWithUnitPriceForApp")

    public IServiceResponse addOrderWithUnitPriceForApp(AddOrderRequest addOrderRequest, @Context HttpServletRequest request) {
        logger.debug("当前新增订单:{}", addOrderRequest);
        // 初始化返回数据
        AddOrderResponse response = new AddOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        if (addOrderRequest != null) {
            if (StringUtils.isEmpty(addOrderRequest.getGameRace())) {
                addOrderRequest.setGameRace("");
            }
        }
        SystemConfig systemConfigByKey = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.NEW_ENCRYPT_GOODSTYPENAME_FOR_APP_OR_NOT.getKey());
        String format = null;
        try {
            //将请求参数转化成一个OrderInfoEO
            OrderInfoEO orderInfo = BeanMapper.map(addOrderRequest, OrderInfoEO.class);
            //开启状态
            if (systemConfigByKey != null) {
                format = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s",
                        addOrderRequest.getGameName(), addOrderRequest.getRegion(), addOrderRequest.getServer(), addOrderRequest.getGameRace()
                        , addOrderRequest.getUnitPrice(), addOrderRequest.getGoldCount(), addOrderRequest.getMobileNumber(), addOrderRequest.getQq(),
                        addOrderRequest.getReceiver(), addOrderRequest.getMoneyName(), addOrderRequest.getRefererType(), addOrderRequest.getUserId(),
                        addOrderRequest.getPayment(), addOrderRequest.getTitle(), addOrderRequest.getLimitPrice(),
                        addOrderRequest.getIsBuyInsurance(), addOrderRequest.getMobileId(), addOrderRequest.getMobileType(), addOrderRequest.getTerminalIp(), addOrderRequest.getGoodsTypeName(), addOrderRequest.getField(),
                        encryptKey);
                logger.info("参数签名是：" + format);
            } else {
                //不开启
                format = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s",
                        addOrderRequest.getGameName(), addOrderRequest.getRegion(), addOrderRequest.getServer(), addOrderRequest.getGameRace()
                        , addOrderRequest.getUnitPrice(), addOrderRequest.getGoldCount(), addOrderRequest.getMobileNumber(), addOrderRequest.getQq(),
                        addOrderRequest.getReceiver(), addOrderRequest.getMoneyName(), addOrderRequest.getRefererType(), addOrderRequest.getUserId(),
                        addOrderRequest.getPayment(), addOrderRequest.getTitle(), addOrderRequest.getLimitPrice(),
                        addOrderRequest.getIsBuyInsurance(), addOrderRequest.getMobileId(), addOrderRequest.getMobileType(), addOrderRequest.getTerminalIp(),
                        encryptKey);
                logger.info("参数签名是：" + format);
                orderInfo.setGoodsTypeName(ServicesContants.GOODS_TYPE_GOLD);
            }
            String toEncrypt = EncryptHelper.md5(format);
            if (!StringUtils.equals(toEncrypt, addOrderRequest.getSign())) {
                response.setMsg(response.getMsg() + " (签名不一致)");
                return response;
            }
            if (null == orderInfo) {
                responseStatus.setStatus(ResponseCodes.NullData.getCode(), ResponseCodes.NullData.getMessage());
                return response;
            }
            //以下为正常下单逻辑
            orderInfo.setUid(addOrderRequest.getUserId());
            orderInfo.setUserAccount(addOrderRequest.getUserAccount());
            orderInfo.setGoodsId(0L);
            orderInfo = orderInfoManager.addOrderInfoFromM(orderInfo);
            response.setOrderInfo(orderInfo);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("当前新增订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(
                    ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("当前新增订单发生未知异常:{}", ex);
        }
        logger.debug("当前新增订单响应信息:{}", response);
        return response;
    }
}
