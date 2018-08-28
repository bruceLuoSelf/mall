package com.wzitech.gamegold.rc8.service.order.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.TradeType;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.order.business.IOrderConfigManager;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.rc8.dto.Response;
import com.wzitech.gamegold.rc8.service.order.IOrderNewService;
import com.wzitech.gamegold.rc8.service.order.dto.*;
import com.wzitech.gamegold.rc8.utils.DESHelper;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.cxf.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 340032 on 2018/2/6.
 */
@Service("OrderNewService")
@Path("Neworder")
@Produces({"application/xml;charset=UTF-8", "application/json;charset=UTF-8"})
@Consumes("application/json;charset=UTF-8")
public class OrderNewServiceImpl extends AbstractBaseService implements IOrderNewService {

    @Autowired
    private IOrderInfoManager orderInfoManager;

    @Autowired
    private IOrderConfigManager orderConfigManager;

    @Value("${RC.5173.key}")
    private String RCKey = "";


    /**
     * 获取订单取消可选的理由列表
     *
     * @return
     */
    @Path("reasons")
    @GET
    @Override
    public String getCancelOrderReasonXml(@QueryParam("name") String name,
                                          @QueryParam("version") String version,
                                          @QueryParam("sign") String sign) throws UnsupportedEncodingException {
        InputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            // 校验MD5
            /*String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s", name, version, encryptKey));
            if (!StringUtils.equals(toEncrypt, sign)) {
                Response result = new Response();
                result.setStatus(false);
                result.setMsg("失败 (签名不一致)");
                return JaxbMapper.toXml(result, "UTF-8");
            }*/

            in = this.getClass().getResourceAsStream("/META-INF/OrderCancelReason.xml");
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
        } catch (Exception e) {
            logger.error("获取订单取消的可选理由列表出错了", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("关闭订单取消的可选理由文件出错了", e);
                }
            }
        }
        return out.toString("UTF-8");
    }

    /**
     * 查询订单列表
     *
     * @param request
     * @return
     */
    @Override
    @Path("list")
    @GET
    public QueryOrderListResponse queryOrderList(@QueryParam("") QueryOrderListRequest request) {
        // 初始化返回数据
        QueryOrderListResponse response = new QueryOrderListResponse();
        response.setMsg("查询失败");
        response.setStatus(false);

        try {
            String decPwd = DESHelper.decrypt(request.getPwd(), RCKey);

            // 校验MD5
            String toEncrypt = EncryptHelper.md5(
                    String.format("%s_%s_%s_%s", request.getName(), decPwd,request.getVersion(), RCKey)
            );

            if (!StringUtils.equals(toEncrypt, request.getSign())) {
                response.setMsg(response.getMsg() + " (签名不一致)");
                return response;
            }

            // 查询参数
            Map<String, Object> queryMap = new HashMap<String, Object>();

            if (request.getTimeType() != null) {
                Date stime = null, etime = null;
                if (StringUtils.isNotBlank(request.getStime())) {
                    stime = DateUtils.parseDate(request.getStime(), "yyyy-MM-dd HH:mm:ss");
                }
                if (StringUtils.isNotBlank(request.getEtime())) {
                    etime = DateUtils.parseDate(request.getEtime(), "yyyy-MM-dd HH:mm:ss");
                }

                if (request.getTimeType() == QueryOrderListRequest.TIME_TYPE_CREATE) {
                    queryMap.put("createStartTime", stime);
                    queryMap.put("createEndTime", etime);
                } else {
                    queryMap.put("statementStartTime", stime);
                    queryMap.put("statementEndTime", etime);
                }
            }

            if (request.getOrderStatus() != 0) {
                queryMap.put("orderState", request.getOrderStatus());
            }

            if (StringUtils.isNotBlank(request.getManualOperation())) {
                queryMap.put("manualOperation", Boolean.parseBoolean(request.getManualOperation()));
            }

            Long serviceId = CurrentUserContext.getUid();
            queryMap.put("servicerId", serviceId); // 只获取指定客服的订单
            queryMap.put("gameId", request.getGameId());
            queryMap.put("userAccount", request.getBuy());
            queryMap.put("buyerQq", request.getBuyQq());
            queryMap.put("sellerAccount", request.getSell());
            queryMap.put("configResultIsDel", false);
            queryMap.put("orderId", request.getOrderId());
            //ZW_C_JB_00008 商城增加通货 增加商品类型 add lvchengsheng
            if (StringUtils.isBlank(request.getGoodsTypeName())) {
                queryMap.put("goodsTypeName", "全部");
            } else {
                queryMap.put("goodsTypeName", request.getGoodsTypeName());
            }
            int start = 0;
            if (request.getPage() != 0) {
                start = request.getPage() * request.getPageSize();
            }
            GenericPage<OrderInfoEO> pages = orderInfoManager.queryOrderInfo(queryMap, "CREATE_TIME", false, request.getPageSize(), start);
            List<OrderInfoEO> orders = pages.getData();
            if (!CollectionUtils.isEmpty(orders)) {
                List<QueryOrderListResponse.Order> dtos = new ArrayList<QueryOrderListResponse.Order>();
                for (OrderInfoEO order : orders) {
                    QueryOrderListResponse.Order dto = new QueryOrderListResponse.Order();
                    dto.setTitle(order.getTitle());
                    dto.setOrderId(order.getOrderId());
                    dto.setGameName(order.getGameName());
                    dto.setGameRegion(order.getRegion());
                    dto.setGameServer(order.getServer());
                    dto.setGameRace(order.getGameRace());
                    dto.setGoldCount(order.getGoldCount());
                    dto.setUnitPrice(order.getUnitPrice());
                    dto.setTotalPrice(order.getTotalPrice());
                    dto.setBuyerAccount(order.getUserAccount());
                    dto.setBuyerQq(order.getQq());
                    dto.setBuyerGameRole(order.getReceiver());
                    dto.setStatus(order.getOrderState());
                    //ZW_C_JB_00008 商城增加通货 增加商品类型返回值 add lvchengsheng
                    dto.setGoodsTypeName(order.getGoodsTypeName());
                    dto.setGoodsTypeId(order.getGoodsTypeId());
                    dto.setCreateTime(DateFormatUtils.format(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                    if (order.getPayTime() != null) {
                        dto.setPaidTime(DateFormatUtils.format(order.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    if (order.getSendTime() != null) {
                        dto.setSendTime(DateFormatUtils.format(order.getSendTime(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    if (order.getEndTime() != null) {
                        dto.setEndTime(DateFormatUtils.format(order.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    //数字id
                    if (StringUtils.isNotBlank(order.getGameNumberId())) {
                        dto.setDigitalId(order.getGameNumberId());
                    }
                    //游戏等级
                    if (null != order.getGameLevel()) {
                        dto.setGameGrade(order.getGameLevel());
                    }
                    // 设置客服账号
                    UserInfoEO service = order.getServicerInfo();
                    if (service == null || StringUtils.isEmpty(service.getLoginAccount())) {
                        dto.setServiceAccount(order.getServicerInfo().getLoginAccount());
                    } else {
                        // 如果是系统管理员等配置单，设置为主客服
                        if (service.getUserType() != UserType.AssureService.getCode()
                                && service.getUserType() != UserType.MakeOrder.getCode()) {
                            dto.setServiceAccount(order.getServicerInfo().getLoginAccount());
                        } else {
                            dto.setServiceAccount(service.getLoginAccount());
                        }
                    }
                    dtos.add(dto);
                }
                response.setOrders(dtos);
            }

            response.setTotalCount(pages.getTotalCount());
            response.setTotalPage(pages.getTotalPageCount());
            response.setMsg("查询成功");
            response.setStatus(true);
        } catch (SystemException ex) {
            // 捕获系统异常
            response.setMsg(ex.getArgs()[0].toString());
            logger.error("查询参数：{}", request);
            logger.error("查询订单列表发生异常", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            response.setMsg("系统发生未知异常");
            logger.error("查询参数：{}", request);
            logger.error("查询订单列表发生异常", ex);
        }

        return response;
    }

    /**
     * 查询订单详情
     *
     * @param request
     * @return
     */
    @Override
    @Path("/{orderId}")
    @GET
    @Produces({"application/xml;charset=UTF-8"})
    @Consumes("application/xml;charset=UTF-8")
    public QueryOrderInfoResponse queryOrderInfo(@PathParam("orderId") String orderId,
                                 @QueryParam("") QueryOrderInfoRequest request) {
        QueryOrderInfoResponse response = new QueryOrderInfoResponse();
        response.setMsg("查询失败");
        response.setStatus(false);

        try {

            String decPwd = DESHelper.decrypt(request.getPwd(), RCKey);

            // 校验MD5
            String toEncrypt = EncryptHelper.md5(
                    String.format("%s_%s_%s_%s", request.getName(), decPwd,request.getVersion(), RCKey)
            );

            if (!StringUtils.equals(toEncrypt, request.getSign())) {
                response.setMsg(response.getMsg() + " (签名不一致)");
//                String xml = JaxbMapper.toXml(response, "UTF-8");
//                logger.info("xml:{}", xml);
//                return DESHelper.encrypt(xml, RCKey);
            }

            if (StringUtils.isBlank(orderId)) {
                throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
            } else {
                orderId = StringUtils.trim(orderId);
            }

            OrderInfoEO order = orderInfoManager.selectById(orderId);
            if (order == null) {
                throw new SystemException(ResponseCodes.NotFoundOrder.getCode(), ResponseCodes.NotFoundOrder.getMessage());
            }

            UserInfoEO servicer = order.getServicerInfo();
            Long serviceId = CurrentUserContext.getUid();
            if (!order.getServicerId().equals(serviceId)) {
                String msg = String.format(orderId + "，所属客服：%s/%s", servicer.getLoginAccount(), servicer.getRealName());
                throw new SystemException(ResponseCodes.NotYourOrder.getCode(), msg);
            }

            QueryOrderInfoResponse.Order orderDto = new QueryOrderInfoResponse.Order();
            // 设置订单详情
            orderDto.setTitle(order.getTitle());
            orderDto.setOrderId(order.getOrderId());
            orderDto.setHowMuchTimeDelivery(String.valueOf(order.getDeliveryTime()));
            orderDto.setGoldCount(order.getGoldCount());
            orderDto.setGoldUnit(order.getMoneyName());
            orderDto.setPrice(order.getUnitPrice());
            orderDto.setTotalPrice(order.getTotalPrice());
            orderDto.setOrderStatus(order.getOrderState());
            orderDto.setIsDelay(order.getIsDelay());
            orderDto.setIsHaveStore(order.getIsHaveStore());
            orderDto.setFields(order.getField());
            //ZW_C_JB_00008 商城增加通货 增加商品类型返回值 add lvchengsheng
            orderDto.setGoodsTypeName(order.getGoodsTypeName());
            orderDto.setGoodsTypeId(order.getGoodsTypeId());
            orderDto.setCreateTime(DateFormatUtils.format(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            if (order.getPayTime() != null) {
                orderDto.setPaidTime(DateFormatUtils.format(order.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            if (order.getSendTime() != null) {
                orderDto.setSendTime(DateFormatUtils.format(order.getSendTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            if (order.getEndTime() != null) {
                orderDto.setEndTime(DateFormatUtils.format(order.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            //数字id
            if (StringUtils.isNotBlank(order.getGameNumberId())) {
                orderDto.setDigitalId(order.getGameNumberId());
            }
            //游戏等级
            if (null != order.getGameLevel()) {
                orderDto.setGameGrade(order.getGameLevel());
            }
            // 设置游戏属性
            QueryOrderInfoResponse.GameInfo gameInfo = new QueryOrderInfoResponse.GameInfo();
            gameInfo.setGame(order.getGameName());
            gameInfo.setGameId(order.getGameId());
            gameInfo.setGameRegion(order.getRegion());
            gameInfo.setGameRegionId(order.getRegionId());
            gameInfo.setGameServer(order.getServer());
            gameInfo.setGameServerId(order.getServerId());
            gameInfo.setGameRace(order.getGameRace());
            gameInfo.setGameRaceId(order.getRaceId());
            orderDto.setGameInfo(gameInfo);

            // 设置买家信息
            QueryOrderInfoResponse.BuyerInfo buyerInfo = new QueryOrderInfoResponse.BuyerInfo();
            buyerInfo.setAccount(order.getUserAccount());
            buyerInfo.setPhone(order.getMobileNumber());
            buyerInfo.setQq(order.getQq());
            buyerInfo.setGameRole(order.getReceiver());
            buyerInfo.setGameLevel(order.getGameLevel());
            orderDto.setBuyerInfo(buyerInfo);

            // 设置客服信息
            QueryOrderInfoResponse.CustomerService serviceInfo = new QueryOrderInfoResponse.CustomerService();
            serviceInfo.setId(servicer.getId());
            serviceInfo.setName(servicer.getNickName());
            serviceInfo.setRealName(servicer.getRealName());
            serviceInfo.setAccount(servicer.getLoginAccount());
            orderDto.setServiceInfo(serviceInfo);

            if (order.getTradeType() != null) {
                if (order.getTradeType() == TradeType.NoDivid.getCode()) {
                    // 当面交易
                    orderDto.setTradeTypeDesc("当面");
                    orderDto.setNamedPlacesOfDelivery(order.getTradePlaceEO().getPlaceName());
                } else {
                    orderDto.setTradeTypeDesc("邮寄");
                }
            }

            // 游戏币数量
            Long goldCount = 0L;
            // 库存单价
            BigDecimal price = BigDecimal.ZERO;
            // 配置入库总额
            BigDecimal totalPrice = BigDecimal.ZERO;
            // 卖家收益
            BigDecimal income = BigDecimal.ZERO;
            // 卖家佣金
            BigDecimal commission = BigDecimal.ZERO;
            // 订单单价
            BigDecimal orderPrice = BigDecimal.ZERO;
            // 配置出库总额
            BigDecimal orderTotalPrice = BigDecimal.ZERO;
            // 差额收入
            BigDecimal balance = BigDecimal.ZERO;

            List<QueryOrderInfoResponse.SubOrder> subOrderList = new ArrayList<QueryOrderInfoResponse.SubOrder>();
            List<ConfigResultInfoEO> configResults = orderConfigManager.orderConfigList(orderId);
            if (!CollectionUtils.isEmpty(configResults)) {
                for (ConfigResultInfoEO configResult : configResults) {
                    RepositoryInfo repositoryInfo = configResult.getRepositoryInfo();
                    SellerInfo seller = repositoryInfo.getSellerInfo();

                    // 设置订单详情
                    QueryOrderInfoResponse.SubOrder dto = new QueryOrderInfoResponse.SubOrder();
                    dto.setId(configResult.getId());
                    dto.setConfigGoldCount(configResult.getConfigGoldCount());
                    dto.setOrderUnitPrice(order.getUnitPrice());
                    dto.setRepositoryUnitPrice(configResult.getRepositoryUnitPrice());
                    dto.setTotalPrice(configResult.getTotalPrice());
                    dto.setOrderTotalPrice(order.getUnitPrice().multiply(new BigDecimal(configResult.getConfigGoldCount())));
                    dto.setIsConsignment(configResult.getIsConsignment());
                    dto.setIsHelper(seller.getIsHelper());
                    dto.setOrderStatus(configResult.getState());
                    dto.setIncome(configResult.getIncome());
                    dto.setCommission(configResult.getCommission());
                    dto.setBalance(configResult.getBalance());
                    dto.setCreateTime(DateFormatUtils.format(configResult.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));

                    // 设置交易员信息
                    if (configResult.getOptionUser() != null) {
                        QueryOrderInfoResponse.TraderInfo traderInfo = new QueryOrderInfoResponse.TraderInfo();
                        traderInfo.setId(configResult.getOptionId());
                        traderInfo.setAccount(configResult.getOptionUser().getLoginAccount());
                        traderInfo.setName(configResult.getOptionUser().getRealName());
                        dto.setTrader(traderInfo);
                    } else {
                        if (configResult.getIsJsRobot() != null && configResult.getIsJsRobot()) {
                            QueryOrderInfoResponse.TraderInfo traderInfo = new QueryOrderInfoResponse.TraderInfo();
                            traderInfo.setName("寄售机器人");
                            dto.setTrader(traderInfo);
                        }
                    }

                    // 设置卖家信息
                    QueryOrderInfoResponse.SellerInfo sellerInfo = new QueryOrderInfoResponse.SellerInfo();
                    sellerInfo.setAccount(repositoryInfo.getLoginAccount());
                    sellerInfo.setName(seller.getName());
                    sellerInfo.setPhone(seller.getPhoneNumber());
                    sellerInfo.setQq(seller.getQq());
                    sellerInfo.setGameAccount(repositoryInfo.getGameAccount());
                    //sellerInfo.setGamePassword(repositoryInfo.getGamePassWord());
                    //sellerInfo.setGameSecondaryPassword(repositoryInfo.getSonGamePassWord());
                    sellerInfo.setGameRole(repositoryInfo.getSellerGameRole());
                    if (StringUtils.isBlank(repositoryInfo.getPasspodUrl())) {
                        sellerInfo.setHasPassport(false);
                    } else {
                        sellerInfo.setHasPassport(true);
                    }
                    dto.setSellerInfo(sellerInfo);

                    subOrderList.add(dto);

                    // 统计
                    goldCount = goldCount + configResult.getConfigGoldCount();
                    price = price.add(configResult.getRepositoryUnitPrice());
                    totalPrice = totalPrice.add(configResult.getTotalPrice());
                    orderPrice = orderPrice.add(order.getUnitPrice());
                    orderTotalPrice = orderTotalPrice.add(dto.getOrderTotalPrice());
                    if (configResult.getIncome() != null) {
                        income = income.add(configResult.getIncome());
                    }
                    if (configResult.getCommission() != null) {
                        commission = commission.add(configResult.getCommission());
                    }
                    if (configResult.getBalance() != null) {
                        balance = balance.add(configResult.getBalance());
                    }
                }

                // 统计
                QueryOrderInfoResponse.StatisticalData statisticalData = new QueryOrderInfoResponse.StatisticalData();
                statisticalData.setGoldCount(goldCount);
                statisticalData.setPrice(price.divide(new BigDecimal(configResults.size()), 5));
                statisticalData.setTotalPrice(totalPrice);
                statisticalData.setIncome(income);
                statisticalData.setCommission(commission);
                statisticalData.setOrderPrice(orderPrice.divide(new BigDecimal(configResults.size()), 5));
                statisticalData.setOrderTotalPrice(orderTotalPrice);

                response.setStatisticalData(statisticalData);
            }

            response.setOrder(orderDto);
            response.setSubOrderList(subOrderList);
            response.setMsg("查询成功");
            response.setStatus(true);
//            String xml = JaxbMapper.toXml(response, "UTF-8");
            return response;
//            logger.info("order:{}", xml);
//            return DESHelper.encrypt(xml, RCKey);
        } catch (SystemException ex) {
            // 捕获系统异常
            String err = String.format("(%s/%s)", ex.getErrorCode(), ex.getArgs()[0]);
            response.setMsg(response.getMsg() + err);
            logger.error("查询参数：{},{}", orderId, request);
            logger.error("查询订单详情发生异常", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            response.setMsg("系统发生未知异常");
            logger.error("查询参数：{},{}", orderId, request);
            logger.error("查询订单详情发生异常", ex);
        }

//        String xml = JaxbMapper.toXml(response, "UTF-8");
//        try {
//            logger.info("xml:{}", xml);
//            return DESHelper.encrypt(xml, RCKey);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
        return null;
    }

    /**
     * 订单移交，只能移交担保/小助手的订单
     *
     * @param orderId 格式：订单号_配单号,例如：YX1503130000773_2014020，如果只有一笔配单，可以只传订单号(例如：YX1503130000773)
     * @param request
     * @return
     */
    @Override
    @GET
    @Path("/transfer/{orderId}")
    public Response transfer(@PathParam("orderId") String orderId,@QueryParam("") TransferOrderRequest request) {
        Response response = new Response();
        response.setMsg("移交操作失败");
        response.setStatus(false);
        try {
            String decPwd = DESHelper.decrypt(request.getPwd(), RCKey);

            // 校验MD5
            String toEncrypt = EncryptHelper.md5(
                    String.format("%s_%s_%s_%s", request.getName(), decPwd, request.getVersion(),RCKey)
            );

            if (!StringUtils.equals(toEncrypt, request.getSign())) {
                response.setMsg(response.getMsg() + " (签名不一致)");
                return response;
            }

            if (StringUtils.isBlank(orderId)) {
                throw new SystemException(ResponseCodes.NotFoundOrder.getCode(), ResponseCodes.NotFoundOrder.getMessage());
            } else {
                orderId = StringUtils.trim(orderId);
            }

            if (orderId.indexOf("_") == -1) {
                throw new SystemException(ResponseCodes.ExistMultiConfigResult.getCode(), ResponseCodes.ExistMultiConfigResult.getMessage());
            }

            String id = orderId.split("_")[0];
            OrderInfoEO order = orderInfoManager.selectById(id);
            if (order == null) {
                throw new SystemException(ResponseCodes.NotFoundOrder.getCode(), ResponseCodes.NotFoundOrder.getMessage());
            }

            UserInfoEO servicer = order.getServicerInfo();
            Long serviceId = CurrentUserContext.getUid();
            if (!order.getServicerId().equals(serviceId)) {
                String msg = String.format(orderId + "，所属客服：%s/%s", servicer.getLoginAccount(), servicer.getRealName());
                throw new SystemException(ResponseCodes.NotYourOrder.getCode(), msg);
            }

            orderInfoManager.transferOrder(orderId);
            response.setStatus(true);
            response.setMsg("移交操作成功");
        } catch (SystemException ex) {
            // 捕获系统异常
            response.setMsg(ex.getArgs()[0].toString());
            logger.error("查询参数：{},{}", orderId, request);
            logger.error("移交操作发生异常", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            response.setMsg("系统发生未知异常");
            logger.error("查询参数：{},{}", orderId, request);
            logger.error("移交操作发生异常", ex);
        }
        return response;
    }

    /**
     * 订单结单接口
     *
     * @param orderId
     * @param request
     * @return
     */
    @Override
    @GET
    @Path("statement/{orderId}")
    public Response statement(@PathParam("orderId") String orderId,@QueryParam("") StatementOrderRequest request) {
        Response response = new Response();
        response.setMsg("结单操作失败");
        response.setStatus(false);

        try {
            String decPwd = DESHelper.decrypt(request.getPwd(), RCKey);

            // 校验MD5
            String toEncrypt = EncryptHelper.md5(
                    String.format("%s_%s_%s_%s", request.getName(), decPwd,request.getVersion(),RCKey)
            );

            if (!StringUtils.equals(toEncrypt, request.getSign())) {
                response.setMsg(response.getMsg() + " (签名不一致)");
                return response;
            }

            if (StringUtils.isBlank(orderId)) {
                throw new SystemException(ResponseCodes.NotFoundOrder.getCode(), ResponseCodes.NotFoundOrder.getMessage());
            } else {
                orderId = StringUtils.trim(orderId);
            }

            OrderInfoEO order = orderInfoManager.selectById(orderId);
            if (order == null) {
                throw new SystemException(ResponseCodes.NotFoundOrder.getCode(), ResponseCodes.NotFoundOrder.getMessage());
            }

            UserInfoEO servicer = order.getServicerInfo();
            Long serviceId = CurrentUserContext.getUid();
            if (!order.getServicerId().equals(serviceId)) {
                String msg = String.format(orderId + "，所属客服：%s/%s", servicer.getLoginAccount(), servicer.getRealName());
                throw new SystemException(ResponseCodes.NotYourOrder.getCode(), msg);
            }

            boolean success = orderInfoManager.statement(orderId);
            if (success) {
                response.setStatus(true);
                response.setMsg("结单操作成功");
            } else {
                response.setStatus(false);
                response.setMsg("还有子订单未发货不能结单");
            }

        } catch (SystemException ex) {
            // 捕获系统异常
            response.setMsg(ex.getArgs()[0].toString());
            logger.error("查询参数：{},{}", orderId, request);
            logger.error("结单操作发生异常", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            response.setMsg("系统发生未知异常");
            logger.error("查询参数：{},{}", orderId, request);
            logger.error("结单操作发生异常", ex);
        }
        return response;
    }


    /**
     * 订单退款
     * @param orderId
     * @param request
     * @return
     */
    @Override
    @GET
    @Path("refund/{orderId}")
    public Response refund(@PathParam("orderId") String orderId,@QueryParam("") RefundOrderRequest request) {
        Response response = new Response();
        response.setMsg("退款操作失败");
        response.setStatus(false);

        try {
            String decPwd = DESHelper.decrypt(request.getPwd(), RCKey);

            // 校验MD5
            String toEncrypt = EncryptHelper.md5(
                    String.format("%s_%s_%s_%s", request.getName(), decPwd, request.getVersion(),RCKey)
            );

            if (!StringUtils.equals(toEncrypt, request.getSign())) {
                response.setMsg(response.getMsg() + " (签名不一致)");
                return response;
            }

            if (StringUtils.isBlank(orderId)) {
                throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
            }

            if (request.getRefundReason() == null) {
                throw new SystemException(ResponseCodes.EmptyRefundReason.getCode(), ResponseCodes.EmptyRefundReason.getMessage());
            }

            boolean success = orderInfoManager.refund(orderId, request.getRefundReason(), request.getReasonremark());
            if (success) {
                response.setStatus(true);
                response.setMsg("退款操作成功");
            } else {
                response.setStatus(false);
                response.setMsg("部分子订单已发货，不能进行退款");
            }
        } catch (SystemException ex) {
            // 捕获系统异常
            response.setMsg(ex.getArgs()[0].toString());
            logger.error("查询参数：{},{}", orderId, request);
            logger.error("退款操作发生异常", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            response.setMsg("系统发生未知异常");
            logger.error("查询参数：{},{}", orderId, request);
            logger.error("退款操作发生异常", ex);
        }
        return response;
    }

    /**
     * 获取退款原因
     * @param request
     * @return
     */
    @Path("/refundReasonList")
    @GET
    @Override
    public String refundReasonList(@Context HttpServletRequest request) throws UnsupportedEncodingException {
        /*InputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            in = new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"WEB-INF/classes/META-INF/OrderRefundReason.xml");
            //in = new FileInputStream(this.getClass().getResource("/").getPath()+"META-INF/OrderRefundReason.xml");
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
                } catch (IOException e1) {e1.printStackTrace();}
            }
        }
        return out.toString("UTF-8");*/

        InputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            in = this.getClass().getResourceAsStream("/META-INF/OrderRefundReason.xml");
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
        } catch (Exception e) {
            logger.error("获取订单取消的可选理由列表出错了", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("关闭订单取消的可选理由文件出错了", e);
                }
            }
        }
        return out.toString("UTF-8");
    }

    /**
     * 获取主订单号
     *
     * @param id
     * @return
     */
    private String getOrderId(String id) {
        if (id.indexOf("_") == -1)
            throw new SystemException(ResponseCodes.NotFoundOrder.getCode(), ResponseCodes.NotFoundOrder.getMessage());
        return id.split("_")[0];
    }

    /**
     * 获取子订单号
     *
     * @param id
     * @return
     */
    private Long getSubOrderId(String id) {
        if (id.indexOf("_") == -1)
            throw new SystemException(ResponseCodes.NotFoundOrder.getCode(), ResponseCodes.NotFoundOrder.getMessage());
        return Long.valueOf(id.split("_")[1]);
    }
}
