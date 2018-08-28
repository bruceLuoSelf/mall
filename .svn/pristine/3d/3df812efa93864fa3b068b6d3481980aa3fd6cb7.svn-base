package com.wzitech.gamegold.facade.frontend.service.repository.impl;

import com.google.common.collect.Lists;
import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.common.enums.CheckState;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.facade.frontend.service.order.ICheckSellerOrderService;
import com.wzitech.gamegold.facade.frontend.service.order.dto.QuerySellerOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.order.dto.QuerySellerOrderResponse;
import com.wzitech.gamegold.facade.frontend.service.order.dto.TransferOrderResponse;
import com.wzitech.gamegold.facade.frontend.service.repository.ICheckRepositoryService;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.CheckRepositoryManageResponse;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.RepositoryDTO;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.RepositoryResponse;
import com.wzitech.gamegold.goods.business.ICheckRepositoryManageRedisManager;
import com.wzitech.gamegold.goods.business.IFirmInfoManager;
import com.wzitech.gamegold.goods.business.IFirmsAccountManager;
import com.wzitech.gamegold.goods.entity.FirmsAccount;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.business.ISubOrderDetailManager;
import com.wzitech.gamegold.order.dao.IConfigResultInfoDBDAO;
import com.wzitech.gamegold.order.dto.SubOrderDetailDTO;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.business.IRepositoryUploadProcessor;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/05  wubiao              修改用户信息校验
 */
@Service("CheckRepositoryManageService")
@Path("firms")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class CheckRepositoryServiceImpl extends AbstractBaseService implements ICheckRepositoryService, ICheckSellerOrderService {
    private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private IRepositoryManager repositoryManager;

    @Autowired
    private IRepositoryUploadProcessor repositoryUploadProcessor;

    @Autowired
    IOrderInfoManager orderInfoManager;

    @Autowired
    IConfigResultInfoDBDAO configResultInfoDBDAO;

    @Autowired
    ISubOrderDetailManager subOrderDetailManager;

    @Value("${firmsSecret.encryptKey}")
    private String encryptKey = "";

    /**
     * 新增cookieName_20170707
     */
    @Value("${firmTokenName}")
    private String firmTokenName = "";

    @Autowired
    private ICheckRepositoryManageRedisManager checkRepositoryManageRedisManager;

    @Autowired
    ISellerManager sellerManager;

    @Autowired
    IFirmsAccountManager firmsAccountManager;

    @Autowired
    IFirmInfoManager firmInfoManager;

    private  JsonMapper jsonMapper=JsonMapper.nonDefaultMapper();

//    /**
//     * 上传库存excel文件
//     *
//     * @param in
//     * @param request
//     * @return
//     */
//    @Path("/upload")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @Produces(value = {"text/html;charset=UTF-8", "application/json;charset=UTF-8"})
//    @POST
//    @Override
//    public RepositoryResponse uploadRepository(@Multipart("gameName") String gameName,
//                                               @Multipart("excelFile") InputStream in,
//                                               @Context HttpServletRequest request) {
//        logger.info("开始处理上传的库存excel文件");
//        RepositoryResponse response = new RepositoryResponse();
//        ResponseStatus responseStatus = new ResponseStatus();
//        response.setResponseStatus(responseStatus);
//        /********新增校验用户信息异常处理_20170711_START************/
//        // 检查卖家信息
//        try {
//            if (!checkSeller(response, request)) {
//                return response;
//            }
//        } catch (SystemException ex) {
//            //捕获系统异常
//            String msg;
//            if (ex.getArgs() == null || ex.getArgs().length == 0) {
//                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
//            } else {
//                msg = ex.getArgs()[0];
//            }
//            responseStatus.setStatus(ex.getErrorCode(), msg);
//            logger.error("校验用户信息发生异常:{}", ex);
//        } catch (Exception e) {
//            // 捕获未知异常
//            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
//            logger.error("校验用户信息发生未知异常:{}", e);
//        }
//        /********新增校验用户信息异常处理_20170711_END**********/
//        if (StringUtils.isBlank(gameName)) {
//            responseStatus.setStatus(ResponseCodes.EmptyGameName.getCode(), ResponseCodes.EmptyGameName.getMessage());
//            return response;
//        }
//        if (in == null) {
//            responseStatus.setStatus(ResponseCodes.EmptyUploadFile.getCode(), ResponseCodes.EmptyUploadFile.getMessage());
//            return response;
//        }
//        try {
//            //厂商上传库存接口直接从cookie中获取
//            IUser seller = updateUserInfo(request);
//            repositoryUploadProcessor.process(gameName, in, seller);
//            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
//        } catch (SystemException e) {
//            //捕获系统异常
//            String msg;
//            if (e.getArgs() == null || e.getArgs().length == 0) {
//                msg = ResponseCodes.getResponseByCode(e.getErrorCode()).getMessage();
//            } else {
//                msg = e.getArgs()[0];
//            }
//            responseStatus.setStatus(e.getErrorCode(), msg);
//            logger.error("处理上传的库存excel文件出错了", e);
//        } catch (Exception e) {
//            // 捕获未知异常
//            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
//            logger.error("处理上传的库存excel文件出错了", e);
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        logger.info("结束处理上传的库存excel文件");
//        return response;
//    }

    @Override
    @Path("/batchSave")
    @POST
    public RepositoryResponse uploadRepositoryWithJsonData(String jsonData, @Context HttpServletRequest request) {
        RepositoryResponse repositoryResponse = new RepositoryResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        repositoryResponse.setResponseStatus(responseStatus);
        try {
            // 检查卖家信息
            if (!checkSeller(repositoryResponse, request)) {
                return repositoryResponse;
            }
            //厂商上传库存接口直接从cookie中获取
            IUser seller = updateUserInfo(request);
            List<RepositoryInfo> repositoryList=jsonMapper.fromJson(jsonData,jsonMapper.createCollectionType(ArrayList.class,RepositoryInfo.class));
            repositoryManager.addRepository(repositoryList,seller);
            responseStatus.setStatus(ResponseCodes.Success.getCode(),ResponseCodes.Success.getMessage());
//            logger.info("batchSave结束，" + jsonData);
        } catch (SystemException e) {
            logger.error("上传库存发生异常;{}",e);
            String msg;
            if (e.getArgs() == null || e.getArgs().length == 0) {
                msg = ResponseCodes.getResponseByCode(e.getErrorCode()).getMessage();
            } else {
                msg = e.getArgs()[0];
            }
            responseStatus.setStatus(e.getErrorCode(), msg);
        }catch (Exception e){
            logger.error("上传库存发生未知异常;{}",e);
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(),ResponseCodes.UnKnownError.getMessage());
        }
        return repositoryResponse;
    }
//
//    /**
//     * 添加库存
//     *
//     * @param repositoryDTO
//     * @param request
//     * @return
//     */
//    @POST
//    @Path("/add")
//    @Override
//    public RepositoryResponse addRepository(RepositoryDTO repositoryDTO, @Context HttpServletRequest request) {
//        RepositoryResponse response = new RepositoryResponse();
//        ResponseStatus responseStatus = new ResponseStatus();
//        response.setResponseStatus(responseStatus);
//        try {
//            // 检查卖家信息
//            if (!checkSeller(response, request)) {
//                return response;
//            }
//            if (repositoryDTO == null) {
//                throw new SystemException(ResponseCodes.EmptyRepository.getCode(), ResponseCodes.EmptyRepository.getMessage());
//            }
//            IUser seller = updateUserInfo(request);
//            RepositoryInfo repositoryInfo = new RepositoryInfo();
//            repositoryInfo.setGameName(repositoryDTO.getGameName());
//            repositoryInfo.setRegion(repositoryDTO.getGameRegion());
//            repositoryInfo.setServer(repositoryDTO.getGameServer());
//            repositoryInfo.setGameRace(repositoryDTO.getGameRace());
//            repositoryInfo.setMoneyName(repositoryDTO.getMoneyName());
//            if (repositoryDTO.getUnitPrice() == null || repositoryDTO.getUnitPrice().doubleValue() <= 0) {
//                throw new SystemException(ResponseCodes.EmptyGoodsPrice.getCode(), ResponseCodes.EmptyGoodsPrice.getMessage());
//            }
//            repositoryInfo.setUnitPrice(repositoryDTO.getUnitPrice().setScale(5, BigDecimal.ROUND_HALF_UP));//ZW_C_JB_00008_20170522 MODIFY 单价限制小数后5位
//            repositoryInfo.setGoldCount(repositoryDTO.getGoldCount());
//            repositoryInfo.setSellableCount(repositoryDTO.getSellableGoldCount());
//            repositoryInfo.setGameAccount(repositoryDTO.getGameAccount());
//            repositoryInfo.setGamePassWord(repositoryDTO.getGamePwd());
//            repositoryInfo.setSellerGameRole(repositoryDTO.getGameRole());
//            repositoryInfo.setSonGamePassWord(repositoryDTO.getSecondaryPwd());
//            repositoryInfo.setHousePassword(repositoryDTO.getWarehousePwd());
//            repositoryInfo.setAccountUid(seller.getUid());
//            repositoryInfo.setLoginAccount(seller.getLoginAccount());
//            String goodsTypeName = StringUtils.isBlank(repositoryDTO.getGoodsTypeName()) ? ServicesContants.GOODS_TYPE_GOLD : repositoryDTO.getGoodsTypeName();
//            Long goodsTypeId = repositoryDTO.getGoodsTypeId() == null ? ServicesContants.GOODS_TYPE_GOLD_ID : repositoryDTO.getGoodsTypeId();
////            repositoryInfo.setGoodsTypeName(repositoryDTO.getGoodsTypeName());//ZW_C_JB_00008_20170516 ADD
////            repositoryInfo.setGoodsTypeId(repositoryDTO.getGoodsTypeId());//ZW_C_JB_00008_20170522 ADD
//            repositoryInfo.setGoodsTypeName(goodsTypeName);//ZW_C_JB_00008_20170516 ADD
//            repositoryInfo.setGoodsTypeId(goodsTypeId);//ZW_C_JB_00008_20170522 ADD
//
//            // 将这行库存记录更新到数据库
//            RepositoryInfo repository = repositoryManager.addRepository(repositoryInfo);
//            response.setRepository(toDTO(repository));
//            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
//        } catch (SystemException e) {
//            //捕获系统异常
//            String msg;
//            if (e.getArgs() == null || e.getArgs().length == 0) {
//                msg = ResponseCodes.getResponseByCode(e.getErrorCode()).getMessage();
//            } else {
//                msg = e.getArgs()[0];
//            }
//            responseStatus.setStatus(e.getErrorCode(), msg);
//            logger.error("添加库存出错了", e);
//        } catch (Exception e) {
//            // 捕获未知异常
//            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
//            logger.error("添加库存出错了", e);
//        }
//        return response;
//    }

//    /**
//     * 批量修改库存
//     *
//     * @param jsondata 批量库存json数据
//     * @param request
//     * @return
//     */
//    @Override
//    @POST
//    @Path("/batchUpdate")
//    public RepositoryResponse UpdateRepository(String jsondata, @Context HttpServletRequest request) {
//        RepositoryResponse response = new RepositoryResponse();
//        ResponseStatus responseStatus = new ResponseStatus();
//        response.setResponseStatus(responseStatus);
//        try {
//            // 检查卖家信息
//            if (!checkSeller(response, request)) {
//                return response;
//            }
//            if (StringUtils.isBlank(jsondata)) {
//                throw new SystemException(ResponseCodes.EmptyRepository.getCode(),
//                        ResponseCodes.EmptyRepository.getMessage());
//            }
//            JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
//            List<RepositoryDTO> list = jsonMapper.fromJson(jsondata,
//                    jsonMapper.createCollectionType(ArrayList.class, RepositoryDTO.class));
//            if (CollectionUtils.isEmpty(list)) {
//                responseStatus.setStatus(ResponseCodes.IllegalArguments.getCode(),
//                        ResponseCodes.IllegalArguments.getMessage());
//                return response;
//            }
//            List<RepositoryInfo> repositoryInfoList = new ArrayList<RepositoryInfo>();
//            IUser seller = updateUserInfo(request);
//            for (RepositoryDTO dto : list) {
//                if (dto.getUnitPrice() == null
//                        && dto.getGoldCount() == null && dto.getSellableGoldCount() == null
//                        && StringUtils.isBlank(dto.getGameAccount()) && StringUtils.isBlank(dto.getGameRole())) {
//                    continue;
//                }
//                RepositoryInfo repositoryInfo = new RepositoryInfo();
//                repositoryInfo.setId(dto.getId());
//                repositoryInfo.setAccountUid(seller.getUid());
//                repositoryInfo.setLoginAccount(seller.getLoginAccount());
//                repositoryInfo.setMoneyName(dto.getMoneyName());
//                repositoryInfo.setUnitPrice(dto.getUnitPrice() != null ? dto.getUnitPrice().setScale(5, BigDecimal.ROUND_HALF_UP) : null);//ZW_C_JB_00008_20170522 MODIFY 单价限制小数后5位
//                repositoryInfo.setGoldCount(dto.getGoldCount());
//                repositoryInfo.setSellableCount(dto.getSellableGoldCount());
//                repositoryInfo.setGameAccount(dto.getGameAccount());
//                repositoryInfo.setSellerGameRole(dto.getGameRole());
////                repositoryInfo.setGoodsTypeName(dto.getGoodsTypeName());
//
//                String goodsTypeName = StringUtils.isBlank(dto.getGoodsTypeName()) ? ServicesContants.GOODS_TYPE_GOLD : dto.getGoodsTypeName();
//                Long goodsTypeId = dto.getGoodsTypeId() == null ? ServicesContants.GOODS_TYPE_GOLD_ID : dto.getGoodsTypeId();
//                repositoryInfo.setGoodsTypeName(goodsTypeName);//ZW_C_JB_00008_20170516 ADD
//                repositoryInfo.setGoodsTypeId(goodsTypeId);//ZW_C_JB_00008_20170522 ADD
//
//                //游戏密码
//                if (StringUtils.isNotBlank(dto.getGamePwd()) && !dto.getGamePwd().equals("******")) {
//                    repositoryInfo.setGamePassWord(dto.getGamePwd().trim());
//                }
//                repositoryInfoList.add(repositoryInfo);
//            }
//            repositoryManager.batchUpdateSellerRepository(repositoryInfoList, seller);
//            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
//        } catch (SystemException e) {
//            //捕获系统异常
//            String msg;
//            if (e.getArgs() == null || e.getArgs().length == 0) {
//                msg = ResponseCodes.getResponseByCode(e.getErrorCode()).getMessage();
//            } else {
//                msg = e.getArgs()[0];
//            }
//            responseStatus.setStatus(e.getErrorCode(), msg);
//            logger.error("批量修改库存出错了", e);
//        } catch (Exception e) {
//            // 捕获未知异常
//            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
//            logger.error("批量修改库存出错了", e);
//        }
//        return response;
//    }

    /**
     * 分页查询库存
     *
     * @param repositoryDTO
     * @param page
     * @param pageSize
     * @param request
     * @return Update History
     */
    @GET
    @Path("/page")
    @Override
    public RepositoryResponse queryRepository(@QueryParam("") RepositoryDTO repositoryDTO,
                                              @QueryParam("page") Integer page,
                                              @QueryParam("pageSize") Integer pageSize,
                                              @QueryParam("sortByField") String sortByField,
                                              @QueryParam("isAsc") Boolean isAsc,
                                              @Context HttpServletRequest request) {
        RepositoryResponse response = new RepositoryResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            // 检查卖家信息
            if (!checkSeller(response, request)) {
                return response;
            }
            //新增根据cookie获取用户信息
            IUser user = updateUserInfo(request);
            if (page == null || page <= 0) {
                page = 1;
            }
            if (pageSize == null || pageSize <= 0) {
                pageSize = 25;
            }
//            else if (pageSize > 100) {
//                //防止大批量的查询
//                pageSize = 100;
//            }
            int start = (page - 1) * pageSize;
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("backGameName", repositoryDTO.getGameName());
            paramMap.put("backRegion", repositoryDTO.getGameRegion());
            paramMap.put("backServer", repositoryDTO.getGameServer());
            paramMap.put("backGameRace", repositoryDTO.getGameRace());
            paramMap.put("gameAccount", repositoryDTO.getGameAccount());
            paramMap.put("sellerGameRole", repositoryDTO.getGameRole());
            paramMap.put("orderUnitPrice", repositoryDTO.getUnitPrice());
            paramMap.put("goodsTypeName", repositoryDTO.getGoodsTypeName());//ZW_C_JB_00008_20170514 ADD
            paramMap.put("loginAccount", user.getLoginAccount());
            paramMap.put("accountUid", user.getUid());
            List<SortField> sortFields = Lists.newArrayList();
            if (StringUtils.isNotBlank(sortByField)) {
                String field = null;
                if (StringUtils.equals(sortByField, "1")) { // 按单价排序
                    field = "UNIT_PRICE";
                } else if (StringUtils.equals(sortByField, "2")) { // 按可销售库存排序
                    field = "SELLABLE_COUNT";
                }
                if (field != null) {
                    String asc = SortField.ASC;
                    if (isAsc != null) {
                        if (isAsc)
                            asc = SortField.ASC;
                        else
                            asc = SortField.DESC;
                    }
                    sortFields.add(new SortField(field, asc));
                }
            }
            if (sortFields.size() == 0) {
                sortFields.add(new SortField("GAME_NAME", SortField.ASC));
                sortFields.add(new SortField("REGION", SortField.ASC));
                sortFields.add(new SortField("SERVER", SortField.ASC));
            }
            GenericPage<RepositoryInfo> genericPage = repositoryManager.queryPageRepository(paramMap, sortFields, start, pageSize);
            List<RepositoryInfo> data = genericPage.getData();
            Long totalCount = genericPage.getTotalCount();
            List<RepositoryDTO> repositoryDTOList = new ArrayList<RepositoryDTO>();
            if (!CollectionUtils.isEmpty(data)) {
                for (RepositoryInfo repository : data) {
                    repositoryDTOList.add(toDTO(repository));
                }
            }
            response.setRepositoryList(repositoryDTOList);
            response.setTotalCount(totalCount);
            response.setPageSize(pageSize);
            response.setCurrPage(page);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            //捕获系统异常
            String msg;
            if (e.getArgs() == null || e.getArgs().length == 0) {
                msg = ResponseCodes.getResponseByCode(e.getErrorCode()).getMessage();
            } else {
                msg = e.getArgs()[0];
            }
            responseStatus.setStatus(e.getErrorCode(), msg);
            logger.error("获取库存发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取库存发生未知异常:{}", e);
        }
        return response;
    }

    protected RepositoryDTO toDTO(RepositoryInfo repository) {
        RepositoryDTO dto = new RepositoryDTO();
        dto.setId(repository.getId());
        dto.setGameName(repository.getGameName());
        dto.setGameId(repository.getGameId());
        dto.setGameRegion(repository.getRegion());
        dto.setGameRegionId(repository.getGameRegionId());
        dto.setGameServer(repository.getServer());
        dto.setGameServerId(repository.getGameServerId());
        dto.setGameRace(repository.getGameRace());
        dto.setGameRaceId(repository.getGameRaceId());
        dto.setGameAccount(repository.getGameAccount());
        //dto.setGamePwd(repository.getGamePassWord());
        dto.setGameRole(repository.getSellerGameRole());
        //dto.setWarehousePwd(repository.getHousePassword());
        //dto.setSecondaryPwd(repository.getSonGamePassWord());
        dto.setGoldCount(repository.getGoldCount());
        dto.setGoodsTypeName(repository.getGoodsTypeName());//ZW_C_JB_00008_20170516 ADD '响应参数'
        if (repository.getSellableCount() != null) {
            dto.setSellableGoldCount(repository.getSellableCount());
        }
        dto.setMoneyName(repository.getMoneyName());
        dto.setUnitPrice(repository.getUnitPrice());
        dto.setLastUpdateTime(repository.getLastUpdateTime());
        dto.setCreateTime(repository.getCreateTime());
        BigDecimal d = repository.getUnitPriceSeller() == null ? BigDecimal.ZERO : repository.getUnitPriceSeller();
        dto.setUnitPriceSeller(d);
        return dto;
    }

    /**
     * 检查卖家信息
     *
     * @return
     */
    protected boolean checkSeller(RepositoryResponse response, @Context HttpServletRequest request) {
        ResponseStatus responseStatus = response.getResponseStatus();
        IUser user = updateUserInfo(request);/***cookie校验用户信息***/
        SellerInfo seller = sellerManager.querySellerInfo(user.getLoginAccount());
        if (seller == null) {
            responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
            return false;
        }
        if (seller.getCheckState() != CheckState.PassAudited.getCode()) {
            if (seller.getCheckState() == CheckState.UnAudited.getCode()) {
                responseStatus.setStatus(ResponseCodes.SellerUnAudited.getCode(), ResponseCodes.SellerUnAudited.getMessage());
            } else {
                responseStatus.setStatus(ResponseCodes.SellerUnPassAudited.getCode(), ResponseCodes.SellerUnPassAudited.getMessage());
            }
            return false;
        }
        return true;
    }

    /**
     * 检查卖家信息
     *
     * @return
     */
    protected boolean checkSellerForRob(RepositoryResponse response, @Context HttpServletRequest request) {
        ResponseStatus responseStatus = response.getResponseStatus();
        IUser user = updateUserInfo(request);/****cookie校验用户信息***/
        SellerInfo seller = sellerManager.querySellerInfo(user.getLoginAccount());
        if (seller == null) {
            responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
            return false;
        }
        if (seller.getCheckState() != CheckState.PassAudited.getCode()) {
            if (seller.getCheckState() == CheckState.UnAudited.getCode()) {
                responseStatus.setStatus(ResponseCodes.SellerUnAudited.getCode(), ResponseCodes.SellerUnAudited.getMessage());
            } else {
                responseStatus.setStatus(ResponseCodes.SellerUnPassAudited.getCode(), ResponseCodes.SellerUnPassAudited.getMessage());
            }
            return false;
        }
        if (!seller.getIsPriceRob()) {
            responseStatus.setStatus(ResponseCodes.SellerNoPriceRob.getCode(), ResponseCodes.SellerNoPriceRob.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 分页查询卖家订单数据, 返回少量字段
     *
     * @param params
     * @return
     */
    @GET
    @Path("/simpleOrderList")
    @Override
    public IServiceResponse queryOrderList(@QueryParam("") QuerySellerOrderRequest params, @Context HttpServletRequest request) {
        QuerySellerOrderResponse response = new QuerySellerOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            UserInfoEO userInfo = updateUserInfo(request);/***cookie校验用户信息***/
            Date startOrderCreate = null;
            Date endOrderCreate = null;
            String startOrder = params.getStartOrderCreate();
            String endOrder = params.getEndOrderCreate();
            if (StringUtils.isNotBlank(startOrder)) {
                startOrder += " 00:00:00";
                startOrderCreate = sdf.parse(startOrder);
            }
            if (StringUtils.isNotBlank(endOrder)) {
                endOrder += " 23:59:59";
                endOrderCreate = sdf.parse(endOrder);
            }

            if (params.getPageSize() == null) {
                params.setPageSize(100);
            }

            if (params.getStart() == null) {
                params.setStart(0);
            }

            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("sellerAccount", userInfo.getLoginAccount());
            queryMap.put("sellerUid", userInfo.getUid());
            queryMap.put("orderId", params.getOrderId());
            queryMap.put("gameName", params.getGameName());
            queryMap.put("gameRegion", params.getRegion());
            queryMap.put("gameServer", params.getServer());
            queryMap.put("gameRace", params.getGameRace());
            queryMap.put("startCreateTime", startOrderCreate);
            queryMap.put("endCreateTime", endOrderCreate);
            queryMap.put("isDeleted", false);
            if (StringUtils.isNotBlank(params.getSubOrderId())) {
                queryMap.put("subOrderId", Long.parseLong(params.getSubOrderId()));
            }
            if (StringUtils.isNotBlank(params.getOrderState())) {
                queryMap.put("state", Integer.parseInt(params.getOrderState())); ///3
            }
            if (StringUtils.isNotBlank(params.getIsConsignment())) {
                queryMap.put("isConsignment", Boolean.parseBoolean(params.getIsConsignment()));
            }
            GenericPage<SubOrderDetailDTO> results = subOrderDetailManager.querySellerSimpleOrders(queryMap, "CREATE_TIME",
                    true, params.getPageSize(), params.getStart());
            SellerInfo sellerInfo = sellerManager.findByAccountAndUid(userInfo.getLoginAccount(), userInfo.getUid());
            boolean result = false;
            if (sellerInfo != null && (sellerInfo.getIsHelper() == null || !sellerInfo.getIsHelper())) {
                result = true;
            }
            if (results.hasData()) {
                List<SubOrderDetailDTO> datas = results.getData();
                for (int i = 0, j = datas.size(); i < j; i++) {
                    SubOrderDetailDTO dto = datas.get(i);
                    // 没有小助手权限的卖家，不显示买家信息
                    if (result) {
                        dto.setBuyerInfo(null);
                    }
                    //适配老数据
                    subOrderDetailManager.dealData(dto);
                }
            }
            response.setResult(results);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            //捕获系统异常
            String msg;
            if (ex.getArgs() == null || ex.getArgs().length == 0) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.error("查询卖家订单列表发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("查询卖家订单列表发生未知异常:{}", ex);
        }
        return response;
    }

    /**
     * 查询订单详情
     *
     * @param orderId    主订单号
     * @param subOrderId 子订单号
     * @return
     */
    @GET
    @Path("/{orderId}/{subOrderId}")
    @Override
    public IServiceResponse queryOrderDetail(@PathParam("orderId") String orderId, @PathParam("subOrderId") Long subOrderId, @Context HttpServletRequest request) {
        QuerySellerOrderResponse response = new QuerySellerOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            UserInfoEO userInfo = updateUserInfo(request);/****cookie校验用户信息***/
            if (StringUtils.isBlank(orderId) || subOrderId == null) {
                throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
            } else {
                orderId = StringUtils.trim(orderId);
            }
            Map<String, Object> queryParam = new HashMap<String, Object>();
            queryParam.put("orderId", orderId);
            queryParam.put("subOrderId", subOrderId);
            queryParam.put("sellerUid", userInfo.getUid());
            queryParam.put("sellerAccount", userInfo.getLoginAccount());
            SubOrderDetailDTO subOrderDetail = subOrderDetailManager.querySellerOrderDetail(queryParam);
            if (subOrderDetail != null) {
                SellerInfo sellerInfo = sellerManager.findByAccountAndUid(userInfo.getLoginAccount(), userInfo.getUid());
                if (sellerInfo != null && (sellerInfo.getIsHelper() == null || !sellerInfo.getIsHelper())) {
                    // 没有小助手权限的卖家，不显示买家信息
                    subOrderDetail.setBuyerInfo(null);
                }
                subOrderDetailManager.dealData(subOrderDetail);
            }
            response.setSubOrderDetail(subOrderDetail);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            String msg;
            if (ex.getArgs() == null || ex.getArgs().length == 0) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.error("查询卖家订单详情发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("查询卖家订单详情发生未知异常:{}", ex);
        }
        return response;
    }

    /**
     * 移交订单
     *
     * @param orderId    主订单号
     * @param subOrderId 子订单号
     * @return
     */
    @GET
    @Path("/transfer/{orderId}/{subOrderId}")
    @Override
    public IServiceResponse transferOrderMethod(@PathParam("orderId") String orderId, @PathParam("subOrderId") Long subOrderId, @Context HttpServletRequest request) {
        TransferOrderResponse response = new TransferOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            UserInfoEO userInfo = updateUserInfo(request);/****cookie校验用户信息***/
            orderInfoManager.transferOrderForHelper(orderId, subOrderId, userInfo);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            String msg;
            if (ex.getArgs() == null || ex.getArgs().length == 0) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            if (!"40014".equals(ex.getErrorCode())) {
                logger.error("小助手订单移交发生异常:{}", ex);
            }
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("小助手订单移交发生未知异常:{}", ex);
        }
        return response;
    }

    @Override
    @Path("/token")
    @Consumes("application/x-www-form-urlencoded;charset=UTF-8")
    @POST
    public CheckRepositoryManageResponse token(@FormParam("firmsSecret") String firmsSecret, @FormParam("loginAccount") String loginAccount, @FormParam("sign") String sign, @Context HttpServletResponse response) {
        CheckRepositoryManageResponse checkRepositoryManageResponse = new CheckRepositoryManageResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        checkRepositoryManageResponse.setResponseStatus(responseStatus);
        try {
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s", firmsSecret, loginAccount, encryptKey, "5173"));
            if (!toEncrypt.equals(sign)) {
                throw new SystemException(ResponseCodes.EntrycpyFail.getCode(), ResponseCodes.EntrycpyFail.getMessage());
            }
            String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
            logger.info("生成cookie："+uuid);

            if (StringUtils.isBlank(loginAccount)) {
                throw new SystemException(ResponseCodes.ParamNotEmpty.getCode(), ResponseCodes.ParamNotEmpty.getMessage());
            }
            SellerInfo sellerInfo = sellerManager.querySellerInfo(loginAccount);
            if (null == sellerInfo || StringUtils.isBlank(sellerInfo.getUid())) {
                throw new SystemException(ResponseCodes.ErrorSellerUid.getCode(), ResponseCodes.ErrorSellerUid.getMessage());
            }
            //查询厂商是否启用
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("loginAccount", sellerInfo.getLoginAccount());
            map.put("uid", sellerInfo.getUid());
            map.put("firmsSecret", firmsSecret);
            FirmsAccount firmsAccountResult = firmsAccountManager.getFirmsAccountByMap(map);
            if (firmsAccountResult == null) {
                throw new SystemException(ResponseCodes.NotOpenFunctrion.getCode(), ResponseCodes.NotOpenFunctrion.getMessage());
            }

            Integer min = checkRepositoryManageRedisManager.addFirmAndLoginAccountAndUid(uuid, firmsSecret, loginAccount, sellerInfo.getUid());
            Cookie cookie = new Cookie(firmTokenName, uuid);
            checkRepositoryManageResponse.setCookie(uuid);
            cookie.setMaxAge(min * 60);
            response.addCookie(cookie);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            responseStatus.setStatus(ResponseCodes.TransforFail.getCode(), ResponseCodes.TransforFail.getMessage());
        } catch (SystemException e) {
            //捕获系统异常
            String msg;
            if (e.getArgs() == null || e.getArgs().length == 0) {
                msg = ResponseCodes.getResponseByCode(e.getErrorCode()).getMessage();
            } else {
                msg = e.getArgs()[0];
            }
            responseStatus.setStatus(e.getErrorCode(), msg);
            logger.error("验证发生异常:{}", e);
        } catch (Exception e) {
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("验证发生未知异常:{}", e);
        }
        return checkRepositoryManageResponse;
    }

    /**
     * 校验用户信息并返回UserInfo
     *
     * @param request
     * @return
     */
    public UserInfoEO updateUserInfo(@Context HttpServletRequest request) {
        //获取当前用户返回的cookie信息
        String cookie = null;
        Cookie[] cookies = request.getCookies();
        if (null == cookies || cookies.length <= 0) {
            logger.info("cookie已失效或不存在:{}", ResponseCodes.RedisTokenExpire.getCode());
            throw new SystemException(ResponseCodes.RedisTokenExpire.getCode(), ResponseCodes.RedisTokenExpire.getMessage());
        }
        for (Cookie acookie : cookies) {
            if (firmTokenName.equals(acookie.getName())) {
                cookie = acookie.getValue();
                logger.info("获取当前cookie信息为:{}", cookie);
                break;
            }
        }
        //根据解析的cookie获取实体对象
        FirmsAccount firmsAccountResult = checkRepositoryManageRedisManager.getRedisValue(cookie);
        if (firmsAccountResult == null || StringUtils.isBlank(firmsAccountResult.getLoginAccount()) || StringUtils.isBlank(firmsAccountResult.getUid())) {
            throw new SystemException(ResponseCodes.RedisTokenExpire.getCode(), ResponseCodes.RedisTokenExpire.getMessage());
        }
        //设置用户信息
        UserInfoEO userInfoEO = new UserInfoEO();
        userInfoEO.setLoginAccount(firmsAccountResult.getLoginAccount());
        userInfoEO.setUid(firmsAccountResult.getUid());
        return userInfoEO;
    }

}
