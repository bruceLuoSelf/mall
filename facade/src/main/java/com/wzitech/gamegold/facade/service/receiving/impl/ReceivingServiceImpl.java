package com.wzitech.gamegold.facade.service.receiving.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.enums.*;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.facade.service.receiving.IReceivingService;
import com.wzitech.gamegold.facade.service.receiving.dto.QueryOrderDetaileDTO;
import com.wzitech.gamegold.facade.utils.DESHelper;
import com.wzitech.gamegold.order.dao.IConfigResultInfoDBDAO;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.dao.*;
import com.wzitech.gamegold.shorder.entity.*;
import com.wzitech.gamegold.shorder.enums.PicSourceEnum;
import com.wzitech.gamegold.shorder.enums.ShowUserImgEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.wzitech.gamegold.facade.service.receiving.dto.*;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;

import com.wzitech.gamegold.facade.service.receiving.dto.QueryOrderDetaileRequest;
import com.wzitech.gamegold.facade.service.receiving.dto.QueryOrderDetaileResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 340082 on 2017/5/5.
 */
@Service("ReceivingService")
@Path("receiving")
@Produces("application/xml;charset=gb2312")
@Consumes("application/json;charset=UTF-8")
public class ReceivingServiceImpl extends AbstractBaseService implements IReceivingService {

    @Value("${encrypt.5173.key}")
    private String encryptKey = "";

    @Autowired
    private IDeliveryOrderManager deliveryOrderManager;

    @Autowired
    private IDeliverySubOrderDao deliverySubOrderDao;

    @Autowired
    private IDeliverySubOrderManager deliverySubOrderManager;

    @Autowired
    private IRobotImgDAO robotImgDAO;
    @Autowired
    private IDeliveryOrderStartRedisDao deliveryOrderStartRedisDao;
    @Autowired
    private IGameAccountManager gameAccountManager;

    @Autowired
    private IShGameConfigManager shGameConfigManager;

    @Autowired
    private IDeliveryOrderLogDao deliveryOrderLogDao;

    @Autowired
    private IManualDeliveryOrderManager manualDeliveryOrderManager;

    @Autowired
    private IConfigResultInfoDBDAO configResultInfoDBDAO;


    /**
     * 根据订单id查询出货商订单详情
     * <p>
     * ZW_C_JB_00004 sunyang
     *
     * @param queryOrderDetaileRequest
     * @param request
     * @return
     */
    @Path("getOrderDetails")
    @GET
    @Override
    public QueryOrderDetaileResponse getOrderDetails(@QueryParam("") QueryOrderDetaileRequest queryOrderDetaileRequest,
                                                     @Context HttpServletRequest request) {
        logger.info("查询出货商订单详情:{}", queryOrderDetaileRequest);
        QueryOrderDetaileResponse queryOrderDetaileResponse = new QueryOrderDetaileResponse();
        try {
            if (StringUtils.isBlank(queryOrderDetaileRequest.getOrderId())) {
                queryOrderDetaileResponse.setMsg("订单id为空");
                queryOrderDetaileResponse.setStatus(false);
                logger.info("订单id为空");
                return queryOrderDetaileResponse;
            }
            String orderId = queryOrderDetaileRequest.getOrderId();
            // 校验MD5
            String decPwd = DESHelper.decrypt(queryOrderDetaileRequest.getOppwd(), encryptKey);
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s", queryOrderDetaileRequest.getOrderId(),
                    queryOrderDetaileRequest.getOpid(), encryptKey, decPwd));

            //验证失败返回原因
            if (!StringUtils.equals(toEncrypt, queryOrderDetaileRequest.getSign())) {
                queryOrderDetaileResponse.setMsg("验证失败");
                queryOrderDetaileResponse.setStatus(false);
                logger.info("MD5验证失败");
                return queryOrderDetaileResponse;
            }
            //根据订单id查询出货主订单
            DeliveryOrder deliveryOrder = deliveryOrderManager.getByOrderId(orderId);
            if (deliveryOrder == null) {
                throw new SystemException(ResponseCodes.OrderLogIdInvalid.getCode(),
                        ResponseCodes.OrderLogIdInvalid.getMessage());
            }
            //准备数据
            QueryOrderDetaileDTO queryOrderDetaileDTO = new QueryOrderDetaileDTO();
            //订单id
            queryOrderDetaileDTO.setOrderId(orderId);
            //游戏名
            queryOrderDetaileDTO.setGameName(deliveryOrder.getGameName());
            //游戏区
            queryOrderDetaileDTO.setGameAreaName(deliveryOrder.getRegion());
            //游戏服
            queryOrderDetaileDTO.setGameAreaServer(deliveryOrder.getServer());
            //订单状态
            queryOrderDetaileDTO.setOrderStatus(deliveryOrder.getStatus().toString());
            //出货数量
            queryOrderDetaileDTO.setQuantity(deliveryOrder.getCount());
            //出货商5173账号
            queryOrderDetaileDTO.setSellerAccount(deliveryOrder.getSellerAccount());
            //收货商5173账号
            queryOrderDetaileDTO.setBuyerAccount(deliveryOrder.getBuyerAccount());
            //出货商手机
            queryOrderDetaileDTO.setPhone(deliveryOrder.getPhone());
            //交易单价
            queryOrderDetaileDTO.setPrice(deliveryOrder.getPrice());
            //总价
            queryOrderDetaileDTO.setOriginalPrice(Double.parseDouble(deliveryOrder.getAmount().toString()));
            //交易地址
            queryOrderDetaileDTO.setAddress(deliveryOrder.getAddress());
            //实际出货金额
            queryOrderDetaileDTO.setRealAmount(deliveryOrder.getRealAmount());
            //交易方式
            queryOrderDetaileDTO.setCustomBuyPatterns(QueryOrderDetaileDTO.FUMO);
            //下单时间
            queryOrderDetaileDTO.setCreateDate(deliveryOrder.getCreateTime());
            //出货商角色名
            queryOrderDetaileDTO.setRoleName(deliveryOrder.getRoleName());
            //全自动转单理由
            queryOrderDetaileDTO.setGtrReason(deliveryOrder.getMachineArtificialReason());
            //收货商手机号码
            queryOrderDetaileDTO.setSellerMobile(deliveryOrder.getBuyerPhone());
            //接手物服id
            queryOrderDetaileDTO.setTakeOverSubjectId(deliveryOrder.getTakeOverSubjectId());
            if (deliveryOrder.getTakeOverSubjectId() != null) {
                queryOrderDetaileDTO.setTakeOverSubjectId(deliveryOrder.getTakeOverSubjectId());
            }

            //接手物服姓名
            queryOrderDetaileDTO.setTakeOverSubject(deliveryOrder.getTakeOverSubject());
            List<DeliverySubOrder> deliverySubOrder = deliverySubOrderManager.queryWaitForTradeOrders(deliveryOrder.getId(), false);
            if (CollectionUtils.isNotEmpty(deliverySubOrder)) {

                //收货商角色名
                queryOrderDetaileDTO.setSellerGameRole(deliverySubOrder.get(0).getGameRole());
                //收货数量
                queryOrderDetaileDTO.setPurchCount(deliverySubOrder.get(0).getCount());

                //实际收货数量
                if (deliverySubOrder.get(0).getRealCount() != null) {
                    queryOrderDetaileDTO.setPurchCount(deliverySubOrder.get(0).getRealCount());
                    //实际出货数量
                    if (deliveryOrder.getRealCount() != null) {
                        queryOrderDetaileDTO.setRealCount(deliveryOrder.getRealCount());
                    } else {
                        queryOrderDetaileDTO.setRealCount(deliverySubOrder.get(0).getRealCount());
                    }
                } else {
                    queryOrderDetaileDTO.setPurchCount(0l);
                }

                //登陆游戏账号
                if (StringUtils.isNotBlank(deliverySubOrder.get(0).getGameAccount())) {
                    queryOrderDetaileDTO.setAccount(deliverySubOrder.get(0).getGameAccount());
                    queryOrderDetaileDTO.setSellerGameRole(deliverySubOrder.get(0).getGameAccount());
                }
                //登陆密码
                if (StringUtils.isNotBlank(deliverySubOrder.get(0).getGamePwd())) {
                    queryOrderDetaileDTO.setPassword(deliverySubOrder.get(0).getGamePwd());
                }

                //账号信息
//                long surplusCount = deliveryOrder.getCount() - deliveryOrder.getRealCount();
                queryOrderDetaileDTO.setAccountRegInfos("游戏账号：" + deliverySubOrder.get(0).getGameAccount() + "；" +
                        "游戏密码：" + deliverySubOrder.get(0).getGamePwd() + "；" + "仓库密码：" + deliverySubOrder.get(0)
                        .getSecondPwd() + "；" + "游戏角色名：" + deliverySubOrder.get(0).getGameRole() + "；" + "交易游戏币数："
                        + deliverySubOrder.get(0).getCount() + "；"
                );
            } else {
                queryOrderDetaileResponse.setMsg(ResponseCodes.NoSubOrder.getMessage());
                queryOrderDetaileResponse.setStatus(false);
                logger.info(ResponseCodes.NoSubOrder.getMessage());
                return queryOrderDetaileResponse;
            }
            queryOrderDetaileDTO.setBizOfferTypeName(QueryOrderDetaileDTO.YXB);
            if (StringUtils.isNotBlank(deliveryOrder.getRoleName())) {
                queryOrderDetaileDTO.setBuyerGameRole(deliveryOrder.getRoleName());
            }
            queryOrderDetaileResponse.setQueryOrderDetaileDTO(queryOrderDetaileDTO);
            queryOrderDetaileResponse.setMsg("获取成功");
            queryOrderDetaileResponse.setStatus(true);
            return queryOrderDetaileResponse;
        } catch (SystemException ex) {
            // 捕获系统异常
            logger.info("查询出货商订单详情发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            logger.info("查询出货商订单详情发生未知异常:{}", ex);
        }
        logger.info("查询出货商订单详情响应信息:{}", queryOrderDetaileResponse);
        return null;
    }

    @Path("getMailOrderDetails")
    @GET
    @Override
    public QueryMailOrderDetailResponse getMailOrderDetails(@QueryParam("") QueryOrderDetaileRequest queryOrderDetaileRequest, @Context HttpServletRequest request) {
        logger.info("查询出货商订单详情:{}", queryOrderDetaileRequest);
        QueryMailOrderDetailResponse queryMailOrderDetailResponse = new QueryMailOrderDetailResponse();
        try {
            if (StringUtils.isBlank(queryOrderDetaileRequest.getOrderId())) {
                queryMailOrderDetailResponse.setMsg("子订单id为空");
                queryMailOrderDetailResponse.setStatus(false);
                logger.info("子订单id为空");
                return queryMailOrderDetailResponse;
            }
            String subOrderId = queryOrderDetaileRequest.getOrderId();
            //校验MD5
            String decPwd = DESHelper.decrypt(queryOrderDetaileRequest.getOppwd(), encryptKey);
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s", queryOrderDetaileRequest.getOrderId(),
                    queryOrderDetaileRequest.getOpid(), encryptKey, decPwd));

            //验证失败返回原因
            if (!StringUtils.equals(toEncrypt, queryOrderDetaileRequest.getSign())) {
                queryMailOrderDetailResponse.setMsg("验证失败");
                queryMailOrderDetailResponse.setStatus(false);
                logger.info("MD5验证失败");
                return queryMailOrderDetailResponse;
            }

            //根据子订单id查询到对应子订单
            DeliverySubOrder deliverySubOrder = deliverySubOrderManager.queryUniqueSubOrder(Long.parseLong(subOrderId));
            //主订单
            DeliveryOrder deliveryOrder = deliveryOrderManager.queryDeliveryOrderByOrderId(deliverySubOrder.getOrderId());
            if (deliverySubOrder != null) {
                //查询子订单集合
                Map<String, Object> queryMap = new HashMap<String, Object>();
                StringBuffer sb = new StringBuffer();
                queryMap.put("multiStatus", sb.append(DeliveryOrderStatus.COMPLETE.getCode())
                        .append(",").append(DeliveryOrderStatus.COMPLETE_PART.getCode()).append(",").append(DeliveryOrderStatus.CANCEL.getCode()).toString());
                queryMap.put("orderId", deliverySubOrder.getOrderId());
                queryMap.put("machineArtificialStatus", MachineArtificialStatus.ArtificialAuto.getCode());
                List<DeliverySubOrder> deliverySubOrders = deliverySubOrderManager.querySubOrders(queryMap);
                queryMap.remove("machineArtificialStatus");
                List<DeliverySubOrder> deliverySubOrderswithmanul = deliverySubOrderManager.querySubOrders(queryMap);

                QueryOrderDetailMailDTO queryOrderDetailMailDTO = new QueryOrderDetailMailDTO();
                if (StringUtils.isNotBlank(deliverySubOrder.getGameAccount())) {
                    queryOrderDetailMailDTO.setAccount(deliverySubOrder.getGameAccount());
                }
                if (null != deliveryOrder.getSellerRoleLevel()) {
                    queryOrderDetailMailDTO.setSellerRoleLevel(deliveryOrder.getSellerRoleLevel().toString());
                }
                queryOrderDetailMailDTO.setBizOfferId("");
                //主订单预计收货数量 万金
                if (deliveryOrder.getCount() != null) {
                    queryOrderDetailMailDTO.setOrderCount(deliveryOrder.getCount());
                }
                //主订单下所有子订单经全自动确认 非人工完单 的最终状态订单数量 万金
                Long totalCountWithoutManualOper = 0l;
                for (DeliverySubOrder deliverySubOrder1 : deliverySubOrders) {
                    totalCountWithoutManualOper += deliverySubOrder1.getRealCount();
                }
                queryOrderDetailMailDTO.setParentOrderId(deliverySubOrder.getOrderId());
                if (StringUtils.isNotBlank(deliverySubOrder.getOtherReason())) {
                    queryOrderDetailMailDTO.setGtrReason(deliverySubOrder.getOtherReason());
                }
                queryOrderDetailMailDTO.setAutoValidationCount(new BigDecimal(totalCountWithoutManualOper));
                queryOrderDetailMailDTO.setTradeAddress("");
                queryOrderDetailMailDTO.setBuyerRoleLevel(deliverySubOrder.getRoleLevel());
                //收货商电话
                if (StringUtils.isNotBlank(deliverySubOrder.getBuyerTel())) {
                    queryOrderDetailMailDTO.setBuyerMobile(deliverySubOrder.getBuyerTel());
                }
                queryOrderDetailMailDTO.setGoodsAddress("");
                BigDecimal havaGoodsCount = BigDecimal.ZERO;
                for (DeliverySubOrder subOrder : deliverySubOrderswithmanul) {
                    havaGoodsCount = havaGoodsCount.add(new BigDecimal(subOrder.getRealCount()));
                }
                //当前主订单下所有最终状态的子订单确认的收货数量
                queryOrderDetailMailDTO.setHaveGoodsCount(havaGoodsCount);

                if (StringUtils.isNotBlank(deliverySubOrder.getGameName())) {
                    queryOrderDetailMailDTO.setGameName(deliverySubOrder.getGameName());
                }
                if (StringUtils.isNotBlank(deliverySubOrder.getRegion())) {
                    queryOrderDetailMailDTO.setGameAreaName(deliverySubOrder.getRegion());
                }
                if (StringUtils.isNotBlank(deliverySubOrder.getServer())) {
                    queryOrderDetailMailDTO.setGameAreaServer(deliverySubOrder.getServer());
                }
                queryOrderDetailMailDTO.setBizOfferTypeName(QueryOrderDetailMailDTO.YXB);
                if (deliveryOrder.getPrice() != null) {
                    queryOrderDetailMailDTO.setPrice(deliveryOrder.getPrice());
                }
                if (deliveryOrder.getAmount() != null) {
                    queryOrderDetailMailDTO.setOriginalPrice(deliveryOrder.getAmount());
                }
                queryOrderDetailMailDTO.setCreateDate(deliverySubOrder.getCreateTime());
                queryOrderDetailMailDTO.setQuantity(deliverySubOrder.getCount());
                if (deliverySubOrder.getRealCount() != null) {
                    queryOrderDetailMailDTO.setRealCount(deliverySubOrder.getRealCount());
                }
                if (deliverySubOrder.getTradeLogo() != null) {
                    queryOrderDetailMailDTO.setCustomBuyPatterns(TradeLogoEnum.getTypeByCode(deliverySubOrder.getTradeLogo()).getType());
                }
                queryOrderDetailMailDTO.setOrderStatus(deliverySubOrder.getStatus().toString());
                if (StringUtils.isNotBlank(deliverySubOrder.getSellerRoleName())) {
                    queryOrderDetailMailDTO.setSellerGameRole(deliverySubOrder.getSellerRoleName());
                }
                //卖家电话
                if (StringUtils.isNotBlank(deliveryOrder.getPhone())) {
                    queryOrderDetailMailDTO.setSellerMobile(deliveryOrder.getPhone());
                }
                if (StringUtils.isNotBlank(deliverySubOrder.getSellerAccount())) {
                    queryOrderDetailMailDTO.setSellerAccount(deliverySubOrder.getSellerAccount());
                }
                if (StringUtils.isNotBlank(deliverySubOrder.getGamePwd())) {
                    queryOrderDetailMailDTO.setPassword(deliverySubOrder.getGamePwd());
                }
                queryOrderDetailMailDTO.setOrderId(deliverySubOrder.getId().toString());
                if (StringUtils.isNotBlank(deliverySubOrder.getBuyerAccount())) {
                    queryOrderDetailMailDTO.setBuyerAccount(deliverySubOrder.getBuyerAccount());
                }
                if (StringUtils.isNotBlank(deliverySubOrder.getGameRole())) {
                    queryOrderDetailMailDTO.setBuyerGameRole(deliverySubOrder.getGameRole());
                }
                if (StringUtils.isNotBlank(deliverySubOrder.getTakeOverSubject())) {
                    queryOrderDetailMailDTO.setTakeOverSubject(deliverySubOrder.getTakeOverSubject());
                }
                if (StringUtils.isNotBlank(deliverySubOrder.getTakeOverSubjectId())) {
                    queryOrderDetailMailDTO.setTakeOverSubjectId(deliverySubOrder.getTakeOverSubjectId());
                }
                queryOrderDetailMailDTO.setAccountRegInfos("游戏账号：" + deliverySubOrder.getGameAccount() +
                        "；" + "游戏密码：" + deliverySubOrder.getGamePwd() + "；仓库密码：" + deliverySubOrder.getSecondPwd()
                        + "；游戏角色名：" + deliverySubOrder.getGameRole() + "；交易游戏币数：" + deliverySubOrder.getCount() + "；");
                Map<String, Object> robotMap = new HashMap<String, Object>();
                robotMap.put("orderId", deliverySubOrder.getOrderId());
                robotMap.put("subOrderId", deliverySubOrder.getId());
                robotMap.put("imgSource", PicSourceEnum.GTR.getCode());
                List<RobotImgEO> robotImgEOs = robotImgDAO.selectByMap(robotMap);
                BigDecimal poundage = shGameConfigManager.getPoundage(deliveryOrder.getGameName(), deliveryOrder.getGoodsType().toString());
                queryOrderDetailMailDTO.setRate(poundage);
                List<String> urlses = new LinkedList<String>();
                for (RobotImgEO robotImgEO : robotImgEOs) {
                    urlses.add(robotImgEO.getImgSrc());
                }
                queryOrderDetailMailDTO.setAutoImageUrls(new Urls(urlses));
                robotMap.put("imgSource", PicSourceEnum.RCPIC.getCode());
                robotImgEOs = robotImgDAO.selectByMap(robotMap);
                List<String> urlsesRc = new LinkedList<String>();
                for (RobotImgEO robotImgEO : robotImgEOs) {
                    urlsesRc.add(robotImgEO.getImgSrc());
                }
                queryOrderDetailMailDTO.setPicUrls(new Urls(urlsesRc));
                //如果是申诉单  会关联到子订单  那么查关联的子订单的日志
                if (deliverySubOrder.getAppealOrder() != null) {
                    subOrderId = deliverySubOrder.getAppealOrder();
                }
                Map<String, Object> orderMap = new HashMap<String, Object>();
                orderMap.put("subId", Long.parseLong(subOrderId));
                List<OrderLog> orderLog = deliveryOrderLogDao.selectByMap(orderMap, "id", true);
                StringBuffer stringBuffer = new StringBuffer("\n");
                for (OrderLog orderLog1 : orderLog) {
                    stringBuffer.append(orderLog1.getLog()).append("\r\n");
                }
                queryOrderDetailMailDTO.setGtrLog(stringBuffer.toString());
                queryMailOrderDetailResponse.setQueryOrderDetailMailDTO(queryOrderDetailMailDTO);
                queryMailOrderDetailResponse.setMsg("获取成功");
                queryMailOrderDetailResponse.setStatus(true);
                queryMailOrderDetailResponse.setOrderStatus(deliverySubOrder.getStatus());
            } else {
                queryMailOrderDetailResponse.setMsg(ResponseCodes.NoSubOrder.getMessage());
                queryMailOrderDetailResponse.setStatus(false);
                logger.info(ResponseCodes.NoSubOrder.getMessage());
                return queryMailOrderDetailResponse;
            }
        } catch (SystemException ex) {
            // 捕获系统异常
            logger.info("查询出货商子订单详情发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            logger.info("查询出货商子订单详情发生未知异常:{}", ex);
        }
        return queryMailOrderDetailResponse;
    }

    /**
     * 根据订单id查询到的状态
     * <p>
     * ZW_C_JB_00004 sunyang
     *
     * @param queryOrderDetaileRequest
     * @param request
     * @return
     */
    @Path("getOrderStatus")
    @GET
    @Override
    public QueryOrderDetaileResponse getOrderStatus(@QueryParam("") QueryOrderDetaileRequest queryOrderDetaileRequest, @Context HttpServletRequest request) {
        logger.info("根据订单id查询到的状态:{}", queryOrderDetaileRequest);
        QueryOrderDetaileResponse queryOrderDetaileResponse = new QueryOrderDetaileResponse();
        try {
            if (StringUtils.isBlank(queryOrderDetaileRequest.getOrderId())) {
                queryOrderDetaileResponse.setMsg("订单id为空");
                queryOrderDetaileResponse.setStatus(false);
                logger.info("订单id为空！");
                return queryOrderDetaileResponse;
            }
            String orderId = queryOrderDetaileRequest.getOrderId();
            // 校验MD5
            String decPwd = DESHelper.decrypt(queryOrderDetaileRequest.getOppwd(), encryptKey);
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s", queryOrderDetaileRequest.getOrderId(),
                    queryOrderDetaileRequest.getOpid(), encryptKey, decPwd));

            //验证失败返回原因
            if (!StringUtils.equals(toEncrypt, queryOrderDetaileRequest.getSign())) {
                queryOrderDetaileResponse.setMsg("MD5验证失败");
                queryOrderDetaileResponse.setStatus(false);
                logger.info("MD5验证失败");
                return queryOrderDetaileResponse;
            }

            try {
                Integer status = deliveryOrderManager.getOrderStatus(orderId);
                if (status == null) {
                    queryOrderDetaileResponse.setMsg("获取订单状态失败");
                    queryOrderDetaileResponse.setStatus(false);
                    logger.info("获取订单状态失败");
                    return queryOrderDetaileResponse;
                }
                queryOrderDetaileResponse.setOrderStatus(status);
                queryOrderDetaileResponse.setMsg("获取成功");
                queryOrderDetaileResponse.setStatus(true);
                logger.info("获取订单状态成功");
                return queryOrderDetaileResponse;
            } catch (Exception e) {
                queryOrderDetaileResponse.setMsg("获取订单状态失败");
                queryOrderDetaileResponse.setStatus(false);
                logger.info("获取订单状态失败");
                return queryOrderDetaileResponse;
            }
        } catch (SystemException ex) {
            // 捕获系统异常
            logger.info("根据订单id查询到的状态发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            logger.info("根据订单id查询到的状态发生未知异常:{}", ex);
        }
        logger.info("根据订单id查询到的状态响应信息:{}", queryOrderDetaileResponse);
        return null;
    }

    @Path("getOrderStatusWithMailDelivery")
    @GET
    @Override
    public QueryMailOrderDetailResponse getOrderStatusWithMailDelivery(@QueryParam("") QueryOrderDetaileRequest queryOrderDetaileRequest, @Context HttpServletRequest request) {
        logger.info("根据订单id查询到的状态:{}", queryOrderDetaileRequest);
        QueryMailOrderDetailResponse queryMailOrderDetailResponse = new QueryMailOrderDetailResponse();
        try {
            if (StringUtils.isBlank(queryOrderDetaileRequest.getOrderId())) {
                queryMailOrderDetailResponse.setMsg("订单id为空");
                queryMailOrderDetailResponse.setStatus(false);
                logger.info("订单id为空！");
                return queryMailOrderDetailResponse;
            }
//
            String orderId = queryOrderDetaileRequest.getOrderId();
            // 校验MD5
            String decPwd = DESHelper.decrypt(queryOrderDetaileRequest.getOppwd(), encryptKey);
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s", queryOrderDetaileRequest.getOrderId(),
                    queryOrderDetaileRequest.getOpid(), encryptKey, decPwd));

            //验证失败返回原因
            if (!StringUtils.equals(toEncrypt, queryOrderDetaileRequest.getSign())) {
                queryMailOrderDetailResponse.setMsg("MD5验证失败");
                queryMailOrderDetailResponse.setStatus(false);
                logger.info("MD5验证失败");
                return queryMailOrderDetailResponse;
            }

            try {
                Integer status = deliverySubOrderManager.getOrderStatus(Long.parseLong(orderId));
                if (status == null) {
                    queryMailOrderDetailResponse.setMsg("获取子订单状态失败");
                    queryMailOrderDetailResponse.setStatus(false);
                    logger.info("获取子订单状态失败");
                    return queryMailOrderDetailResponse;
                }
                queryMailOrderDetailResponse.setOrderStatus(status);
                queryMailOrderDetailResponse.setMsg("获取成功");
                queryMailOrderDetailResponse.setStatus(true);
                logger.info("获取子订单状态成功");
                return queryMailOrderDetailResponse;
            } catch (Exception e) {
                queryMailOrderDetailResponse.setMsg("获取子订单状态失败");
                queryMailOrderDetailResponse.setStatus(false);
                logger.info("获取子订单状态失败");
                return queryMailOrderDetailResponse;
            }
        } catch (SystemException ex) {
            // 捕获系统异常
            logger.info("根据订单id查询到的状态发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            logger.info("根据订单id查询到的状态发生未知异常:{}", ex);
        }
        logger.info("根据订单id查询到的状态响应信息:{}", queryMailOrderDetailResponse);
        return null;
    }

    /**
     * 查找异常转人工订单
     * ZW_C_JB_00004 yexiaokang
     *
     * @param orderListRequest
     * @param request
     * @return
     */
    @Path("queryMachineAbnormalTurnManualOrderList")
    @GET
    @Override
    public QueryMachineAbnormalTurnManualOrderListResponse queryMachineAbnormalTurnManualOrderList(@QueryParam("") QueryMachineAbnormalTurnManualOrderListRequest orderListRequest, @Context HttpServletRequest request) {

        logger.info("当前查询订单列表:{}", orderListRequest);
        // 初始化返回数据
        QueryMachineAbnormalTurnManualOrderListResponse response = new QueryMachineAbnormalTurnManualOrderListResponse();
        response.setStatus(false);
        try {
            //校验MD5
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s", orderListRequest.getRequestNum(), orderListRequest.getQueryType(), encryptKey));
            if (!StringUtils.equals(toEncrypt, orderListRequest.getSign())) {
                logger.info("签名验证不通过");
                response.setMsg("签名验证不通过");
                return response;
            }
            Map<String, Object> queryMap = new HashMap<String, Object>();
            List<QueryMachineAbnormalTurnManualListItemDTO> listItemDTOList = new ArrayList<QueryMachineAbnormalTurnManualListItemDTO>();
            if (orderListRequest.getRequestNum() < 0) {
                logger.info("申请查询数量有误");
                response.setMsg("查询数量请输入大于等于0的值");
                return response;
            }
            queryMap.put("requestNum", orderListRequest.getRequestNum());
            List<DeliverySubOrder> deliverySubOrderList = deliverySubOrderManager.queryMachineAbnormalTurnManualOrderList(queryMap);
            if (CollectionUtils.isNotEmpty(deliverySubOrderList)) {
                for (DeliverySubOrder deliverySubOrder : deliverySubOrderList) {
                    QueryMachineAbnormalTurnManualListItemDTO listItemDTO = new QueryMachineAbnormalTurnManualListItemDTO();
                    listItemDTO.setId(deliverySubOrder.getOrderId());
                    listItemDTO.setGn(deliverySubOrder.getGameName());
                    listItemDTO.setGsn(deliverySubOrder.getRegion());
                    listItemDTO.setGan(deliverySubOrder.getServer());
                    listItemDTO.setYxbMall(QueryMachineAbnormalTurnManualListItemDTO.SH_YXB_MALL);
                    listItemDTO.setMAR(deliverySubOrder.getMachineArtificialReason());
                    listItemDTO.setMAT(deliverySubOrder.getMachineArtificialTime());
                    listItemDTO.setTakeOverSubjectId(deliverySubOrder.getTakeOverSubjectId());
                    listItemDTO.setTakeOverSubject(deliverySubOrder.getTakeOverSubject());
                    listItemDTOList.add(listItemDTO);
                }
            }
            response.setMachineAbnormalTurnManualOrderList(listItemDTOList);
            response.setMsg("操作成功");
            response.setStatus(true);
        } catch (SystemException ex) {
            //捕获系统异常
            response.setMsg(ex.getArgs()[0].toString());
            logger.info("查询异常转人工订单列表发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            response.setMsg("系统发生未知异常");
            logger.info("查询异常转人工订单列表发生未知异常:{}", ex);
        }
        logger.info("查询异常转人工订单列表响应信息:{}", response);
        return response;
    }

    /**
     * 物服完单
     * ZW_C_JB_00004 yexiaokang
     *
     * @param logisticsSheetRequest
     * @param request
     * @return
     */
    @Path("logisticsSheet")
    @GET
    @Override
    public LogisticsSheetResponse logisticsSheet(@QueryParam("") LogisticsSheetRequest logisticsSheetRequest, @Context HttpServletRequest request) {
        logger.info("物服完单:{}", logisticsSheetRequest);
        // 初始化返回数据
        LogisticsSheetResponse response = new LogisticsSheetResponse();
        response.setStatus(false);
        try {
            // 校验MD5
            String decPwd = DESHelper.decrypt(logisticsSheetRequest.getOppwd(), encryptKey);
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s_%s", logisticsSheetRequest.getOrderId()
                    , logisticsSheetRequest.getGoldCount(), logisticsSheetRequest.getOpid(), encryptKey, decPwd));
            if (!StringUtils.equals(toEncrypt, logisticsSheetRequest.getSign())) {
                logger.info("MD5验证失败");
                response.setMsg("MD5验证失败");
                return response;
            }
            deliveryOrderManager.logisticsSheet(logisticsSheetRequest.getOrderId(), logisticsSheetRequest.getGoldCount());
            response.setMsg("操作成功");
            response.setStatus(true);
        } catch (SystemException ex) {
            //捕获系统异常
            response.setMsg(ex.getArgs()[0].toString());
            logger.info("物服完单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            response.setMsg("系统发生未知异常");
            logger.info("物服完单发生未知异常:{}", ex);
        }
        logger.info("物服完单响应信息:{}", response);
        return response;
    }

    /**
     * 推送消息实现接口
     * ZW_C_JB_00004 yexiaokang
     *
     * @param pushMessageRequest
     * @param request
     * @return
     */
    @Path("pushMessage")
    @GET
    @Override
    public PushMessageResponse pushMessage(@QueryParam("") PushMessageRequest pushMessageRequest, @Context HttpServletRequest request) {
        logger.info("推送消息实现:{}", pushMessageRequest);
        // 初始化返回数据
        PushMessageResponse response = new PushMessageResponse();
        response.setStatus(false);
        try {
            // 校验MD5
            String decPwd = DESHelper.decrypt(pushMessageRequest.getOppwd(), encryptKey);
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s_%s", pushMessageRequest.getOrderId()
                    , pushMessageRequest.getMessage(), pushMessageRequest.getOpid(), encryptKey, decPwd));
            if (!StringUtils.equals(toEncrypt, pushMessageRequest.getSign())) {
                logger.info("MD5验证失败");
                response.setMsg("MD5验证失败");
                return response;
            }
            deliverySubOrderManager.pushMessage(pushMessageRequest.getOrderId(), pushMessageRequest.getMessage());
            response.setMsg("操作成功");
            response.setStatus(true);
        } catch (SystemException ex) {
            //捕获系统异常
            response.setMsg(ex.getArgs()[0].toString());
            logger.info("推送消息实现发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            response.setMsg("系统发生未知异常");
            logger.info("推送消息实现发生未知异常:{}", ex);
        }
        logger.info("推送消息实现响应信息:{}", response);
        return response;
    }

    /**
     * 取消订单接口
     * ZW_C_JB_00004 yexiaokang
     *
     * @param cancellationOfOrderRequest
     * @param request
     * @return
     */
    @Path("cancellationOfOrder")
    @GET
    @Override
    public CancellationOfOrderResponse cancellationOfOrder(@QueryParam("") CancellationOfOrderRequest cancellationOfOrderRequest, @Context HttpServletRequest request) {
        logger.info(" 取消订单:{}", cancellationOfOrderRequest);
        // 初始化返回数据
        CancellationOfOrderResponse response = new CancellationOfOrderResponse();
        response.setStatus(false);
        try {
            // 校验MD5
            String decPwd = DESHelper.decrypt(cancellationOfOrderRequest.getOppwd(), encryptKey);
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s_%s_%s", cancellationOfOrderRequest.getOrderId()
                    , cancellationOfOrderRequest.getReason(), cancellationOfOrderRequest.getRemarks(), cancellationOfOrderRequest.getOpid(), encryptKey, decPwd));
            if (!StringUtils.equals(toEncrypt, cancellationOfOrderRequest.getSign())) {
                logger.info("MD5验证失败");
                response.setMsg("MD5验证失败");
                return response;
            }
            if (cancellationOfOrderRequest.getReason().equals("其他原因")) {
                deliveryOrderManager.cancelOrder(cancellationOfOrderRequest.getOrderId(), DeliveryOrder.OHTER_REASON, cancellationOfOrderRequest.getRemarks());
            } else {
                deliveryOrderManager.cancelOrder(cancellationOfOrderRequest.getOrderId(), DeliveryOrder.OHTER_REASON, cancellationOfOrderRequest.getReason());
            }
            response.setMsg("操作成功");
            response.setStatus(true);
        } catch (SystemException ex) {
            //捕获系统异常
            response.setMsg(ex.getArgs()[0].toString());
            logger.info(" 取消订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            response.setMsg("系统发生未知异常");
            logger.info(" 取消订单发生未知异常:{}", ex);
        }
        logger.info("取消订单响应信息:{}", response);
        return response;
    }


    @Override
    @GET
    @Path("mailDeliveryManualOper")
    public LogisticsSheetResponse mailDeliveryManualOper(@QueryParam("") LogisticsSheetRequest logisticsSheetRequest, @Context HttpServletRequest request) {
        logger.info("完单处理开始:{}", logisticsSheetRequest);
        //初始化返回数据
        LogisticsSheetResponse response = new LogisticsSheetResponse();
        response.setStatus(false);
        try {
            // 校验MD5
            String decPwd = DESHelper.decrypt(logisticsSheetRequest.getOppwd(), encryptKey);
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s_%s", logisticsSheetRequest.getOrderId()
                    , logisticsSheetRequest.getGoldCount(), logisticsSheetRequest.getOpid(), encryptKey, decPwd));
            if (!StringUtils.equals(toEncrypt, logisticsSheetRequest.getSign())) {
                logger.info("MD5验证失败");
                response.setMsg("MD5验证失败");
                return response;
            }
            DeliverySubOrder deliverySubOrder = deliverySubOrderManager.selectByOrderId(Long.parseLong(logisticsSheetRequest.getOrderId()), null);
            if (deliverySubOrder == null || deliverySubOrder.getStatus() == DeliveryOrderStatus.COMPLETE.getCode()
                    || deliverySubOrder.getStatus() == DeliveryOrderStatus.COMPLETE_PART.getCode()
                    || deliverySubOrder.getStatus() == DeliveryOrderStatus.CANCEL.getCode()) {
                throw new SystemException(ResponseCodes.NoSubOrder.getCode(), ResponseCodes.NoSubOrder.getMessage());
            }
            if (logisticsSheetRequest.getGoldCount() == -1l) {
                logisticsSheetRequest.setGoldCount(0l);
            }
            Map<Long, Long> countMap = new HashMap<Long, Long>();
            countMap.put(Long.parseLong(logisticsSheetRequest.getOrderId()), logisticsSheetRequest.getGoldCount());
            //如果是撤单就要设置取消原因了
            Map<Long, String> appealReasonMap = new HashMap<Long, String>();
            Map<Long, Integer> reasonMap = new HashMap<Long, Integer>();
            Map<Long, String> remarkMap = new HashMap<Long, String>();
            if (logisticsSheetRequest.getGoldCount() == 0l) {
                reasonMap.put(Long.parseLong(logisticsSheetRequest.getOrderId()), DeliveryOrder.OHTER_REASON);
                appealReasonMap.put(Long.parseLong(logisticsSheetRequest.getOrderId()), logisticsSheetRequest.getReason());
                remarkMap.put(Long.parseLong(logisticsSheetRequest.getOrderId()), logisticsSheetRequest.getRemark());
                deliveryOrderManager.handleOrderForMailDeliveryOrderMax(countMap, deliverySubOrder.getOrderId(), appealReasonMap, reasonMap, remarkMap);
            } else {
                deliveryOrderManager.handleOrderForMailDeliveryOrderMax(countMap, deliverySubOrder.getOrderId(), null, null, null);
            }
            response.setMsg("操作成功");
            response.setStatus(true);
        } catch (SystemException ex) {
            //捕获系统异常
            response.setMsg(ex.getArgs()[0].toLowerCase());
            logger.info("物服完单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            response.setMsg("系统发生未知异常");
            logger.info("物服完单发生未知异常:{}", ex);
        }
        logger.info("物服完单响应信息:{}", response);
        return response;
    }

    /**
     * 获取取消原因接口
     * ZW_C_JB_00004 yexiaokang
     *
     * @param request
     * @return
     */
    @Path("cancellationOrderReason")
    @GET
    @Override
    public String cancellationOrderReason(@Context HttpServletRequest request) {
        InputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            in = new FileInputStream(request.getSession().getServletContext().getRealPath("/") + "WEB-INF/classes/META-INF/cancellationOrderReason.xml");
//            in = new FileInputStream(this.getClass().getResource("/").getPath()+"META-INF/OrderCancelReason.xml");
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return out.toString().replace("utf-8", "gb2312");
    }


    @Path("queryMachineAbnormalTurnManualOrderListWithMailDeliveryOrder")
    @GET
    @Override
    public QueryMachineAbnormalTurnManualOrderListResponse queryMachineAbnormalTurnManualOrderListWithMailDeliveryOrder(@QueryParam("") QueryMachineAbnormalTurnManualOrderListRequest orderListRequest, @Context HttpServletRequest request) {
        logger.info("当前查询子订单列表{}", orderListRequest);
        //初始化返回数据
        QueryMachineAbnormalTurnManualOrderListResponse response = new QueryMachineAbnormalTurnManualOrderListResponse();
        response.setStatus(false);
        try {
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s", orderListRequest.getRequestNum(), orderListRequest.getQueryType(), encryptKey));
            if (!StringUtils.equals(toEncrypt, orderListRequest.getSign())) {
                logger.info("签名验证不通过");
                response.setMsg("签名验证不通过");
                return response;
            }
            Map<String, Object> queryMap = new HashMap<String, Object>();
            List<QueryMachineAbnormalTurnManualListItemDTO> listItemDTOList = new ArrayList<QueryMachineAbnormalTurnManualListItemDTO>();
            queryMap.put("requestNum", orderListRequest.getRequestNum());
            List<DeliverySubOrder> deliverySubOrderList = deliverySubOrderManager.queryMachineAbnormalTurnManualOrderList(queryMap);
            if (CollectionUtils.isNotEmpty(deliverySubOrderList)) {
                for (DeliverySubOrder deliverySubOrder : deliverySubOrderList) {
                    QueryMachineAbnormalTurnManualListItemDTO listItemDTO = new QueryMachineAbnormalTurnManualListItemDTO();
                    listItemDTO.setId(deliverySubOrder.getId().toString());
                    listItemDTO.setGn(deliverySubOrder.getGameName());
                    listItemDTO.setGsn(deliverySubOrder.getRegion());
                    if (deliverySubOrder.getTradeLogo() != null) {
                        listItemDTO.setCustomBuyPatterns(TradeLogoEnum.getTypeByCode(deliverySubOrder.getTradeLogo()).getType());
                    } else {
                        listItemDTO.setCustomBuyPatterns(TradeType.getTypeByCode(deliverySubOrder.getTradeType()).getName());
                    }
                    listItemDTO.setGan(deliverySubOrder.getServer());
                    listItemDTO.setYxbMall(QueryMachineAbnormalTurnManualListItemDTO.SH_YXB_MALL);
                    listItemDTO.setMAR(deliverySubOrder.getMachineArtificialReason());
                    listItemDTO.setMAT(deliverySubOrder.getMachineArtificialTime());
                    listItemDTO.setTakeOverSubjectId(deliverySubOrder.getTakeOverSubjectId());
                    listItemDTO.setTakeOverSubject(deliverySubOrder.getTakeOverSubject());
                    listItemDTOList.add(listItemDTO);
                }
            }
            response.setMachineAbnormalTurnManualOrderList(listItemDTOList);
            response.setMsg("操作成功");
            response.setStatus(true);
        } catch (SystemException ex) {
            //捕获系统异常
            response.setMsg(ex.getArgs()[0].toString());
            logger.info("查询异常转人工订单列表发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            response.setMsg("系统发生未知异常");
            logger.info("查询异常转人工订单列表发生未知异常:{}", ex);
        }
        logger.info("查询异常转人工订单列表响应信息:{}", response);
        return response;
    }

    @Override
    @GET
    @Path("getTradingOrders")
    public PreventAccountConflictResponse preventAccountConflict(@QueryParam("gameName") String gameName,
                                                                 @QueryParam("region") String region,
                                                                 @QueryParam("server") String server,
                                                                 @QueryParam("gameRace") String gameRace,
                                                                 @QueryParam("gameRole") String gameRole,
                                                                 @QueryParam("loginAccount") String loginAccount,
                                                                 @QueryParam("gameAccount") String gameAccount) {
        PreventAccountConflictResponse response = new PreventAccountConflictResponse();
        try {
            logger.info(String.format("防顶号接口查询开始,游戏%s,%s,%s,角色%s,登录5173账号%s,游戏账号%s", gameName, region, server, gameRole, loginAccount, gameAccount));
            if (StringUtils.isBlank(gameName) ||
                    StringUtils.isBlank(region) ||
                    StringUtils.isBlank(server) ||
                    StringUtils.isBlank(gameRole) ||
                    StringUtils.isBlank(loginAccount) ||
                    StringUtils.isBlank(gameAccount)) {
                throw new SystemException(ResponseCodes.EmptyParams.getCode(), ResponseCodes.EmptyParams.getMessage());
            }

            Integer configResultInfoEOs = configResultInfoDBDAO.selectConfigResultWithGameRole(gameName, region, server, gameRace, gameRole, loginAccount, gameAccount);
            if (configResultInfoEOs > 0) {
                response.setSellGameRoleBusy(true);
            } else {
                response.setSellGameRoleBusy(false);
            }
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("gameName", gameName);
            queryMap.put("region", region);
            queryMap.put("server", server);
            queryMap.put("gameRace", gameRace);
            queryMap.put("gameRole", gameRole);
            queryMap.put("buyerAccount", loginAccount);
            queryMap.put("gameAccount", gameAccount);
            queryMap.put("status", 3);
            Integer counts = deliverySubOrderDao.countByMap(queryMap);
            if (counts > 0) {
                response.setDeliveryGameRoleBusy(true);
            } else {
                response.setDeliveryGameRoleBusy(false);
            }
            response.setMsg("操作成功");
            response.setStatus(true);
        } catch (SystemException e) {
            logger.info("查询防顶号发生异常:{}", e);
            if (e.getArgs().length > 0) {
                response.setMsg(e.getArgs()[0]);
                response.setStatus(false);
            } else {
                response.setMsg("操作失败");
                response.setStatus(false);
            }
        } catch (Exception e) {
            logger.info("查询防顶号发生未知异常:{}", e);
            response.setMsg("查询发生未知异常");
            response.setStatus(false);
        }
        return response;
    }

    @Override
    @Path("UploadFile")
    @Consumes("multipart/form-data")
    @Produces("application/xml;charset=gb2312")
    @POST
    public UploadPicsResponse uploadImage(@Multipart(value = "file", required = false) byte[] file,
                                          @Multipart(value = "subOrderId", required = false) Long subOrderId,
                                          @Multipart(value = "orderId", required = false) String orderId,
                                          @Context HttpServletRequest request) {
        UploadPicsResponse uploadPicsResponse = new UploadPicsResponse();
        try {
            UploadPicsRCRequest requestPic = new UploadPicsRCRequest();
            requestPic.setFileBytes(file);
            requestPic.setRequestSource("yxbmall.5173.com");
            requestPic.setUType("0");
            requestPic.setAppNo(null);
            requestPic.setToken(null);
            requestPic.setVersionNumber(0);
            requestPic.setId(null);
            requestPic.setServiceType(2);
            if (StringUtils.isBlank(orderId)) {
                throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
            }
            ImageUploadElements imageUploadElements = manualDeliveryOrderManager.saveImageUrlForSubOrder(subOrderId, requestPic.toString(), orderId, PicSourceEnum.RCPIC.getCode(), ShowUserImgEnum.SHOW_IMG.getCode());
            if (imageUploadElements.isSucceed()) {
                uploadPicsResponse.setMessage("上传成功");
                uploadPicsResponse.setStatus("true");
                uploadPicsResponse.setUrl(imageUploadElements.getOriginalUrl());
            }
            if (!imageUploadElements.isSucceed()) {
                uploadPicsResponse.setMessage(imageUploadElements.getErrorMessage());
                uploadPicsResponse.setStatus("false");
            }
        } catch (SystemException e) {
            logger.error("上传拍卖交易图片发生异常:{}", e);
            uploadPicsResponse.setMessage("上传图片发生异常" + e.getMessage());
            uploadPicsResponse.setStatus("false");
        } catch (Exception e) {
            // 捕获未知异常
            logger.error("上传拍卖交易图片发生异常:{}", e);
            uploadPicsResponse.setMessage("上传图片发生未知异常");
            uploadPicsResponse.setStatus("false");
        }
        return uploadPicsResponse;
    }
}
