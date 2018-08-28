package com.wzitech.gamegold.facade.frontend.service.repository.impl;

import com.google.common.collect.Lists;
import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.common.enums.CheckState;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.accessLimit.AccessLimit;
import com.wzitech.gamegold.facade.frontend.service.repository.IRepositoryManageService;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.RepositoryDTO;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.RepositoryResponse;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.business.IRepositoryUploadProcessor;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.shorder.business.IShGameConfigManager;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 卖家库存管理Service实现类
 *
 * @author yemq
 *         Update History
 *         Date        Name                Reason for change
 *         ----------  ----------------    ----------------------
 *         2017/05/17  lvshuyan           ZW_C_JB_00008 商城增加通货
 */
@Service("RepositoryManageService")
@Path("/seller/repository")
@Produces(value = {"application/json;charset=UTF-8"})
public class RepositoryManageServiceImpl extends AbstractBaseService implements IRepositoryManageService {
    @Autowired
    private ISellerManager sellerManager;

    @Autowired
    private IRepositoryManager repositoryManager;

    @Autowired
    private IRepositoryUploadProcessor repositoryUploadProcessor;

    @Autowired
    private IShGameConfigManager shGameConfigManager;

    /**
     * 上传库存excel文件
     *
     * @param in
     * @param request
     * @return
     */
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(value = {"text/html;charset=UTF-8", "application/json;charset=UTF-8"})
    @POST
    @Override
    @AccessLimit
    public RepositoryResponse upload(@Multipart("gameName") String gameName,
                                     @Multipart("excelFile") InputStream in,
                                     @Context HttpServletRequest request) {
        logger.info("开始处理上传的库存excel文件");
        RepositoryResponse response = new RepositoryResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        // 检查卖家信息
        if (!checkSeller(response)) {
            return response;
        }

        if (StringUtils.isBlank(gameName)) {
            responseStatus.setStatus(ResponseCodes.EmptyGameName.getCode(), ResponseCodes.EmptyGameName.getMessage());
            return response;
        }
        if (in == null) {
            responseStatus.setStatus(ResponseCodes.EmptyUploadFile.getCode(), ResponseCodes.EmptyUploadFile.getMessage());
            return response;
        }

        try {
            IUser seller = CurrentUserContext.getUser();
            repositoryUploadProcessor.process(gameName, in, seller);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            //捕获系统异常
            String msg = null;
            if (e.getArgs() == null || e.getArgs().length == 0) {
                msg = ResponseCodes.getResponseByCode(e.getErrorCode()).getMessage();
            } else {
                msg = e.getArgs()[0];
            }
            responseStatus.setStatus(e.getErrorCode(), msg);
            logger.error("处理上传的库存excel文件出错了", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("处理上传的库存excel文件出错了", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("结束处理上传的库存excel文件");
        return response;
    }

    /**
     * 添加库存
     *
     * @param repositoryDTO
     * @param request
     * @return
     */
    @POST
    @Path("/add")
    @Override
    @AccessLimit
    public RepositoryResponse addRepository(RepositoryDTO repositoryDTO, @Context HttpServletRequest request) {
        RepositoryResponse response = new RepositoryResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        // 检查卖家信息
        if (!checkSeller(response)) {
            return response;
        }
        try {
            if (repositoryDTO == null) {
                throw new SystemException(ResponseCodes.EmptyRepository.getCode(), ResponseCodes.EmptyRepository.getMessage());
            }
            IUser seller = CurrentUserContext.getUser();
            RepositoryInfo repositoryInfo = new RepositoryInfo();
            repositoryInfo.setGameName(repositoryDTO.getGameName());
            repositoryInfo.setRegion(repositoryDTO.getGameRegion());
            repositoryInfo.setServer(repositoryDTO.getGameServer());
            repositoryInfo.setGameRace(repositoryDTO.getGameRace());
            repositoryInfo.setMoneyName(repositoryDTO.getMoneyName());
            repositoryInfo.setUnitPrice(repositoryDTO.getUnitPrice().setScale(5, BigDecimal.ROUND_HALF_UP));//ZW_C_JB_00008_20170522 MODIFY 单价限制小数后5位
            repositoryInfo.setGoldCount(repositoryDTO.getGoldCount());
            repositoryInfo.setSellableCount(repositoryDTO.getSellableGoldCount());
            repositoryInfo.setGameAccount(repositoryDTO.getGameAccount());
            repositoryInfo.setGamePassWord(repositoryDTO.getGamePwd());
            repositoryInfo.setSellerGameRole(repositoryDTO.getGameRole());
            repositoryInfo.setSonGamePassWord(repositoryDTO.getSecondaryPwd());
            repositoryInfo.setHousePassword(repositoryDTO.getWarehousePwd());
            repositoryInfo.setAccountUid(seller.getUid());
            repositoryInfo.setLoginAccount(seller.getLoginAccount());

            String goodsTypeName = StringUtils.isBlank(repositoryDTO.getGoodsTypeName()) ? ServicesContants.GOODS_TYPE_GOLD : repositoryDTO.getGoodsTypeName();
            Long goodsTypeId = repositoryDTO.getGoodsTypeId() == null ? ServicesContants.GOODS_TYPE_GOLD_ID : repositoryDTO.getGoodsTypeId();
//            repositoryInfo.setGoodsTypeName(repositoryDTO.getGoodsTypeName());//ZW_C_JB_00008_20170516 ADD
//            repositoryInfo.setGoodsTypeId(repositoryDTO.getGoodsTypeId());//ZW_C_JB_00008_20170522 ADD
            repositoryInfo.setGoodsTypeName(goodsTypeName);//ZW_C_JB_00008_20170516 ADD
            repositoryInfo.setGoodsTypeId(goodsTypeId);//ZW_C_JB_00008_20170522 ADD

            // 将这行库存记录更新到数据库
            RepositoryInfo repository = repositoryManager.addRepositorySingle(repositoryInfo);
            response.setRepository(toDTO(repository));
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            //捕获系统异常
            String msg = null;
            if (e.getArgs() == null || e.getArgs().length == 0) {
                msg = ResponseCodes.getResponseByCode(e.getErrorCode()).getMessage();
            } else {
                msg = e.getArgs()[0];
            }
            responseStatus.setStatus(e.getErrorCode(), msg);
            logger.error("添加库存出错了", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("添加库存出错了", e);
        }

        return response;
    }

    /**
     * 批量修改库存
     *
     * @param jsondata 批量库存json数据
     * @param request
     * @return
     */
    @Override
    @POST
    @Path("/batchUpdate")
    @AccessLimit
    public RepositoryResponse batchUpdateRepository(String jsondata, @Context HttpServletRequest request) {
        RepositoryResponse response = new RepositoryResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        // 检查卖家信息
        if (!checkSeller(response)) {
            return response;
        }

        try {
            if (StringUtils.isBlank(jsondata)) {
                throw new SystemException(ResponseCodes.EmptyRepository.getCode(),
                        ResponseCodes.EmptyRepository.getMessage());
            }

            JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
            List<RepositoryDTO> list = jsonMapper.fromJson(jsondata,
                    jsonMapper.createCollectionType(ArrayList.class, RepositoryDTO.class));
            if (CollectionUtils.isEmpty(list)) {
                responseStatus.setStatus(ResponseCodes.IllegalArguments.getCode(),
                        ResponseCodes.IllegalArguments.getMessage());
                return response;
            }

            List<RepositoryInfo> repositoryInfoList = new ArrayList<RepositoryInfo>();
            IUser seller = CurrentUserContext.getUser();
            for (RepositoryDTO dto : list) {
                if (dto.getUnitPrice() == null
                        && dto.getGoldCount() == null && dto.getSellableGoldCount() == null
                        && StringUtils.isBlank(dto.getGameAccount()) && StringUtils.isBlank(dto.getGameRole())) {
                    continue;
                }
                RepositoryInfo repositoryInfo = new RepositoryInfo();
                repositoryInfo.setId(dto.getId());
                repositoryInfo.setAccountUid(seller.getUid());
                repositoryInfo.setLoginAccount(seller.getLoginAccount());
                repositoryInfo.setMoneyName(dto.getMoneyName());
                repositoryInfo.setUnitPrice(dto.getUnitPrice().setScale(5, BigDecimal.ROUND_HALF_UP));//ZW_C_JB_00008_20170522 MODIFY 单价限制小数后5位
                repositoryInfo.setGoldCount(dto.getGoldCount());
                repositoryInfo.setSellableCount(dto.getSellableGoldCount());
                repositoryInfo.setGameAccount(dto.getGameAccount());
                repositoryInfo.setSellerGameRole(dto.getGameRole());
//                repositoryInfo.setGoodsTypeName(dto.getGoodsTypeName());

                String goodsTypeName = StringUtils.isBlank(dto.getGoodsTypeName()) ? ServicesContants.GOODS_TYPE_GOLD : dto.getGoodsTypeName();
                Long goodsTypeId = dto.getGoodsTypeId() == null ? ServicesContants.GOODS_TYPE_GOLD_ID : dto.getGoodsTypeId();
                repositoryInfo.setGoodsTypeName(goodsTypeName);//ZW_C_JB_00008_20170516 ADD
                repositoryInfo.setGoodsTypeId(goodsTypeId);//ZW_C_JB_00008_20170522 ADD

                //游戏密码
                if (StringUtils.isNotBlank(dto.getGamePwd()) && !dto.getGamePwd().equals("******")) {
                    repositoryInfo.setGamePassWord(dto.getGamePwd().trim());
                }
                repositoryInfoList.add(repositoryInfo);
            }
            repositoryManager.batchUpdateSellerRepository(repositoryInfoList, seller);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            //捕获系统异常
            String msg = null;
            if (e.getArgs() == null || e.getArgs().length == 0) {
                msg = ResponseCodes.getResponseByCode(e.getErrorCode()).getMessage();
            } else {
                msg = e.getArgs()[0];
            }
            responseStatus.setStatus(e.getErrorCode(), msg);
            logger.error("批量修改库存出错了", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("批量修改库存出错了", e);
        }

        return response;
    }

    /**
     * 修改库存
     *
     * @param repositoryDTO
     * @param request
     * @return
     */
   /* @POST
    @Path("/update")
    @Override
    public RepositoryResponse updateRepository(@FormParam("") RepositoryDTO repositoryDTO, @Context HttpServletRequest request) {
        RepositoryResponse response = new RepositoryResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        // 检查卖家信息
        if (!checkSeller(response)) {
            return response;
        }

        try {
            if (repositoryDTO == null) {
                throw new SystemException(ResponseCodes.EmptyRepository.getCode(), ResponseCodes.EmptyRepository.getMessage());
            }
            if (repositoryDTO.getId() == null || repositoryDTO.getId() <= 0) {
                throw new SystemException(ResponseCodes.EmptyRepositoryId.getCode(), ResponseCodes.EmptyRepositoryId.getMessage());
            }

            IUser seller = CurrentUserContext.getUser();

            RepositoryInfo repositoryInfo = new RepositoryInfo();
            repositoryInfo.setId(repositoryDTO.getId());
            repositoryInfo.setGameName(repositoryDTO.getGameName());
            repositoryInfo.setRegion(repositoryDTO.getGameRegion());
            repositoryInfo.setServer(repositoryDTO.getGameServer());
            repositoryInfo.setGameRace(repositoryDTO.getGameRegion());
            repositoryInfo.setMoneyName(repositoryDTO.getMoneyName());
            repositoryInfo.setUnitPrice(repositoryDTO.getUnitPrice());
            if (repositoryDTO.getGoldCount() != null)
                repositoryInfo.setGoldCount(repositoryDTO.getGoldCount());
            if (repositoryDTO.getSellableGoldCount() != null)
                repositoryInfo.setSellableCount(repositoryDTO.getSellableGoldCount());
            repositoryInfo.setGameAccount(repositoryDTO.getGameAccount());
            repositoryInfo.setGamePassWord(repositoryDTO.getGamePwd());
            repositoryInfo.setSellerGameRole(repositoryDTO.getGameRole());
            repositoryInfo.setSonGamePassWord(repositoryDTO.getSecondaryPwd());
            repositoryInfo.setHousePassword(repositoryDTO.getWarehousePwd());
            repositoryInfo.setAccountUid(seller.getUid());
            repositoryInfo.setLoginAccount(seller.getLoginAccount());

            // 将这行库存记录更新到数据库
            repositoryManager.modifyRepository(repositoryInfo);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            //捕获系统异常
            String msg = null;
            if (e.getArgs() == null || e.getArgs().length == 0) {
                msg = ResponseCodes.getResponseByCode(e.getErrorCode()).getMessage();
            } else {
                msg = e.getArgs()[0];
            }
            responseStatus.setStatus(e.getErrorCode(), msg);
            logger.error("修改库存出错了", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("修改库存出错了", e);
        }

        return response;
    }
    */

    /**
     * 删除库存
     *
     * @param ids
     * @param request
     * @return
     */
    @POST
    @Path("/remove")
    @Override
    public RepositoryResponse removeRepository(@FormParam("ids") List<Long> ids, @Context HttpServletRequest request) {
        RepositoryResponse response = new RepositoryResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        // 检查卖家信息
        if (!checkSeller(response)) {
            return response;
        }

        try {
            if (CollectionUtils.isEmpty(ids)) {
                throw new SystemException(ResponseCodes.EmptyRepositoryId.getCode(), ResponseCodes.EmptyRepositoryId.getMessage());
            }

            IUser seller = CurrentUserContext.getUser();
            repositoryManager.deleteSellerRepositorys(ids, seller);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            //捕获系统异常
            String msg = null;
            if (e.getArgs() == null || e.getArgs().length == 0) {
                msg = ResponseCodes.getResponseByCode(e.getErrorCode()).getMessage();
            } else {
                msg = e.getArgs()[0];
            }
            responseStatus.setStatus(e.getErrorCode(), msg);
            logger.error("删除库存出错了", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("删除库存出错了", e);
        }

        return response;
    }

    /**
     * 分页查询库存
     *
     * @param repositoryDTO
     * @param page
     * @param pageSize
     * @param request
     * @return Update History
     * Date        Name                Reason for change
     * ----------  ----------------    ----------------------
     * 2017/05/15  wubiao           ZW_C_JB_00008 商城增加通货
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

        // 检查卖家信息
        if (!checkSeller(response)) {
            return response;
        }

        try {
            IUser user = CurrentUserContext.getUser();

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
                        if (isAsc){
                            asc = SortField.ASC;
                        } else {
                            asc = SortField.DESC;
                        }
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
            Map<String,Long> map = new HashMap<String, Long>();
            if (!CollectionUtils.isEmpty(data)) {
                for (RepositoryInfo repository : data) {
                    RepositoryDTO dto = toDTO(repository);
                    repositoryDTOList.add(dto);
                    Long thresholdCount = map.get(repository.getGameName() + repository.getGoodsTypeName());
                    if (thresholdCount == null) {
                        ShGameConfig shGameConfig = shGameConfigManager.getConfigByGameName(repository.getGameName(), repository.getGoodsTypeName(), null, null);
                        if (shGameConfig != null && shGameConfig.getThresholdCount() != null) {
                            map.put(repository.getGameName() + repository.getGoodsTypeName(),shGameConfig.getThresholdCount());
                            thresholdCount = shGameConfig.getThresholdCount();
                        }
                    }
                    dto.setThresholdCount(thresholdCount == null ? 0 : thresholdCount);
                    dto.setSellableGoldCount(dto.getSellableGoldCount() == null ? 0 : dto.getSellableGoldCount());
                }
            }
            SellerInfo sellerInfo = sellerManager.querySellerInfo(user.getLoginAccount());
            if (sellerInfo.getIsShieldedType() != null && sellerInfo.getIsShieldedType()) {
                response.setIsJiShou(true);
            }else{
                response.setIsJiShou(false);
            }
            response.setRepositoryList(repositoryDTOList);
            response.setTotalCount(totalCount);
            response.setPageSize(pageSize);
            response.setCurrPage(page);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取库存发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取库存发生未知异常:{}", e);
        }

        return response;
    }

    /**
     * 根据库存ID查询库存
     *
     * @param id
     * @param request
     * @return
     */
    @GET
    @Path("/{id}")
    @Override
    public RepositoryResponse queryRepository(@PathParam("id") Long id, @Context HttpServletRequest request) {
        RepositoryResponse response = new RepositoryResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        // 检查卖家信息
        if (!checkSeller(response)) {
            return response;
        }

        if (id == null || id <= 0) {
            responseStatus.setStatus(ResponseCodes.EmptyId.getCode(), ResponseCodes.EmptyId.getMessage());
            return response;
        }

        try {
            IUser user = CurrentUserContext.getUser();
            RepositoryInfo repository = repositoryManager.querySellerRepository(user, id);
            RepositoryDTO dto = toDTO(repository);
            response.setRepository(dto);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取库存发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取库存发生异常:{}", e);
        }

        return response;
    }

    /**
     * 上线
     */
    @Override
    @Path("online")
    @GET
    public IServiceResponse online() {
        RepositoryResponse response = new RepositoryResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        // 检查卖家信息
        if (!checkSeller(response)) {
            return response;
        }

        try {
            IUser user = CurrentUserContext.getUser();
            sellerManager.online(user.getLoginAccount(), user.getUid());
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("设置卖家上线发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("设置卖家上线发生异常:{}", e);
        }

        return response;
    }

    /**
     * 下线
     *
     * @return
     */
    @Override
    @Path("offline")
    @GET
    public IServiceResponse offline() {
        RepositoryResponse response = new RepositoryResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        // 检查卖家信息
        if (!checkSeller(response)) {
            return response;
        }

        try {
            IUser user = CurrentUserContext.getUser();
            sellerManager.offline(user.getLoginAccount(), user.getUid());
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("设置卖家下线发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("设置卖家下线发生异常:{}", e);
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
        dto.setStockCount(repository.getStockCount() == null ? 0 : repository.getStockCount());
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
    protected boolean checkSeller(RepositoryResponse response) {
        ResponseStatus responseStatus = response.getResponseStatus();

        // 获取当前用户
        IUser user = CurrentUserContext.getUser();
        if (user == null) {
            responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
            return false;
        }

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
//        else{
//            if(seller.getOpenShState()!= ShOpenState.OPEN.getCode()){
//                responseStatus.setStatus(ResponseCodes.SellerNoOpenSh.getCode(), ResponseCodes.SellerNoOpenSh.getMessage());
//                return false;
//            }
//        }

        return true;
    }

    /**
     * 检查卖家信息
     *
     * @return
     */
    protected boolean checkSellerForRob(RepositoryResponse response) {
        ResponseStatus responseStatus = response.getResponseStatus();

        // 获取当前用户
        IUser user = CurrentUserContext.getUser();
        if (user == null) {
            responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
            return false;
        }

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
//        else{
//            if(seller.getOpenShState()!= ShOpenState.OPEN.getCode()){
//                responseStatus.setStatus(ResponseCodes.SellerNoOpenSh.getCode(), ResponseCodes.SellerNoOpenSh.getMessage());
//                return false;
//            }
//        }

        if (!seller.getIsPriceRob()) {
            responseStatus.setStatus(ResponseCodes.SellerNoPriceRob.getCode(), ResponseCodes.SellerNoPriceRob.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 分页获取所有区服的最低价
     *
     * @param repositoryDTO
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @GET
    @Path("/queryLowestPriceRepository")
    @Override
    public RepositoryResponse queryLowestPriceRepository(@QueryParam("") RepositoryDTO repositoryDTO,
                                                         @QueryParam("page") Integer page,
                                                         @QueryParam("pageSize") Integer pageSize,
                                                         @QueryParam("sortByField") String sortByField,
                                                         @QueryParam("isAsc") Boolean isAsc,
                                                         @Context HttpServletRequest request) {
        RepositoryResponse response = new RepositoryResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        // 检查卖家信息
        if (!checkSellerForRob(response)) {
            return response;
        }

        try {
            IUser user = CurrentUserContext.getUser();


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

            String gameName = repositoryDTO.getGameName();
            String region = repositoryDTO.getGameRegion();
            String server = repositoryDTO.getGameServer();
            String gameRace = repositoryDTO.getGameRace();
            String loginAccount = user.getLoginAccount();

            List<SortField> sortFields = Lists.newArrayList();
            if (StringUtils.isNotBlank(sortByField)) {
                String asc = SortField.ASC;
                if (isAsc != null) {
                    if (isAsc)
                        asc = SortField.ASC;
                    else
                        asc = SortField.DESC;
                }
                sortFields.add(new SortField(sortByField, asc));
            }
            if (sortFields.size() == 0) {
                sortFields.add(new SortField("GAME_NAME", SortField.ASC));
                sortFields.add(new SortField("REGION", SortField.ASC));
                sortFields.add(new SortField("SERVER", SortField.ASC));
            }
            GenericPage<RepositoryInfo> genericPage = repositoryManager.selectLowestList(loginAccount, gameName, region, server, gameRace, sortFields, start, pageSize);
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
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取所有区服的最低价发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取所有区服的最低价发生未知异常:{}", e);
        }

        return response;
    }

    /**
     * 批量修改库存
     *
     * @param jsondata 批量库存json数据
     * @param request
     * @return
     */
    @Override
    @POST
    @Path("/batchUpdateForRob")
    @AccessLimit
    public RepositoryResponse batchUpdateRepositoryForRob(String jsondata, @Context HttpServletRequest request) {
        RepositoryResponse response = new RepositoryResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        // 检查卖家信息
        if (!checkSellerForRob(response)) {
            return response;
        }

        try {
            if (StringUtils.isBlank(jsondata)) {
                throw new SystemException(ResponseCodes.EmptyRepository.getCode(),
                        ResponseCodes.EmptyRepository.getMessage());
            }

            JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
            List<RepositoryDTO> list = jsonMapper.fromJson(jsondata,
                    jsonMapper.createCollectionType(ArrayList.class, RepositoryDTO.class));
            if (CollectionUtils.isEmpty(list)) {
                responseStatus.setStatus(ResponseCodes.IllegalArguments.getCode(),
                        ResponseCodes.IllegalArguments.getMessage());
                return response;
            }

            List<RepositoryInfo> repositoryInfoList = new ArrayList<RepositoryInfo>();
            IUser seller = CurrentUserContext.getUser();
            for (RepositoryDTO dto : list) {
                if (dto.getUnitPrice() == null
                        && dto.getGoldCount() == null && dto.getSellableGoldCount() == null
                        && StringUtils.isBlank(dto.getGameAccount()) && StringUtils.isBlank(dto.getGameRole())) {
                    continue;
                }
                RepositoryInfo repositoryInfo = new RepositoryInfo();
                repositoryInfo.setId(dto.getId());
                repositoryInfo.setAccountUid(seller.getUid());
                repositoryInfo.setLoginAccount(seller.getLoginAccount());
                repositoryInfo.setMoneyName(dto.getMoneyName());
                repositoryInfo.setUnitPrice(dto.getUnitPrice());
                repositoryInfo.setGoldCount(dto.getGoldCount());
                repositoryInfo.setSellableCount(dto.getSellableGoldCount());
                repositoryInfo.setGameAccount(dto.getGameAccount());
                repositoryInfo.setSellerGameRole(dto.getGameRole());
                repositoryInfoList.add(repositoryInfo);
            }
            repositoryManager.batchUpdateSellerRepository(repositoryInfoList, seller);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            //捕获系统异常
            String msg = null;
            if (e.getArgs() == null || e.getArgs().length == 0) {
                msg = ResponseCodes.getResponseByCode(e.getErrorCode()).getMessage();
            } else {
                msg = e.getArgs()[0];
            }
            responseStatus.setStatus(e.getErrorCode(), msg);
            logger.error("批量修改库存出错了", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("批量修改库存出错了", e);
        }

        return response;
    }

    /**
     * 分页查询库存
     *
     * @param repositoryDTO
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @GET
    @Path("/pageForRob")
    @Override
    public RepositoryResponse queryRepositoryForRob(@QueryParam("") RepositoryDTO repositoryDTO,
                                                    @QueryParam("page") Integer page,
                                                    @QueryParam("pageSize") Integer pageSize,
                                                    @QueryParam("sortByField") String sortByField,
                                                    @QueryParam("isAsc") Boolean isAsc,
                                                    @Context HttpServletRequest request) {
        RepositoryResponse response = new RepositoryResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        // 检查卖家信息
        if (!checkSellerForRob(response)) {
            return response;
        }

        try {
            IUser user = CurrentUserContext.getUser();

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
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取库存发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取库存发生未知异常:{}", e);
        }

        return response;
    }
}
