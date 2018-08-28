/************************************************************************************
 * Copyright 2014 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		SellerServiceImpl
 * 包	名：		com.wzitech.gamegold.facade.frontend.service.repository.impl
 * 项目名称：	gamegold-facade-frontend
 * 作	者：		HeJian
 * 创建时间：	2014-2-23
 * 描	述：
 * 更新纪录：	1. HeJian 创建于 2014-2-23 上午11:34:10
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.repository.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.BeanMapper;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.CheckState;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.ShOpenState;
import com.wzitech.gamegold.common.servicer.IServicerOrderManager;
import com.wzitech.gamegold.facade.frontend.service.repository.ISellerService;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.*;
import com.wzitech.gamegold.filemgmt.business.IFileManager;
import com.wzitech.gamegold.goods.business.IFirmsAccountManager;
import com.wzitech.gamegold.goods.entity.FirmsAccount;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.dao.IRepositoryDBDAO;
import com.wzitech.gamegold.repository.dao.IRepositoryRedisDAO;
import com.wzitech.gamegold.repository.dao.ISellerDBDAO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 卖家服务实现
 * @author HeJian
 *
 */
@Service("SellerService")
@Path("seller")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class SellerServiceImpl extends AbstractBaseService implements ISellerService {
    @Autowired
    ISellerManager sellerManager;

    @Autowired
    IRepositoryManager repositoryManager;

    @Autowired
    IFileManager fileManager;

    @Autowired
    ISellerDBDAO sellerDBDAO;

    @Autowired
    IRepositoryDBDAO repositoryDBDAO;

    @Autowired
    IServicerOrderManager servicerOrderManager;

    @Autowired
    IRepositoryRedisDAO repositoryRedisDAO;

    @Autowired
    IFirmsAccountManager firmsAccountManager;

    @Path("applyseller")
    @POST
    @Override
    public IServiceResponse applySeller(ApplySellerRequest applySellerRequest,
                                        @Context HttpServletRequest request) {
        logger.debug("当前申请成为卖家请求信息:{}", applySellerRequest);
        // 初始化返回数据
        ApplySellerResponse response = new ApplySellerResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            // 获取当前用户
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }

            SellerInfo sellerInfo = BeanMapper.map(applySellerRequest, SellerInfo.class);
            sellerInfo.setUid(userInfo.getUid());
            sellerInfo.setLoginAccount(userInfo.getLoginAccount());
            //start jiyangxin ZW_C_JB_00021商城资金改造
            //添加资金改造的字段新用户入驻时设置为未阅读协议
            sellerInfo.setisAgree(false);
            //end jiyangxin ZW_C_JB_00021商城资金改造
            if (applySellerRequest.getIsOpenSh()) {
                sellerInfo.setOpenShState(ShOpenState.WAIT_OPEN.getCode());
            }

            // 卖家游戏不能为空
            if (StringUtils.isEmpty(sellerInfo.getGames())) {
                responseStatus.setStatus(ResponseCodes.EmptySellerGame.getCode(), ResponseCodes.EmptySellerGame.getMessage());
                return response;
            }

            synchronized (this) {
                // 判断卖家是否已存在
                SellerInfo dbSeller = sellerManager.querySellerInfo(userInfo.getLoginAccount());
                if (dbSeller != null) {
                    if (dbSeller.getCheckState() != CheckState.UnPassAudited.getCode()) {
                        // 审核通过或者未审核，提示卖家已存在
                        responseStatus.setStatus(ResponseCodes.ExitSeller.getCode(), ResponseCodes.ExitSeller.getMessage());
                        return response;
                    } else {
                        // 审核未通过，更新当前卖家信息
                        sellerInfo.setId(dbSeller.getId());
                        sellerInfo.setCheckState(CheckState.UnAudited.getCode()); // 状态修改为未审核
                        sellerManager.modifySeller(sellerInfo);
                    }
                } else {
                    sellerManager.applySeller(sellerInfo);
                }
            }

            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("申请成为卖家发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("申请成为卖家发生异常:{}", ex);
        }
        logger.debug("申请成为卖家响应信息:{}", response);
        return response;
    }

    @Path("uploadpasspod")
    @Consumes("multipart/form-data")
    @Produces("text/html;charset=UTF-8")
    @POST
    @Override
    public IServiceResponse uploadPasspod(@Multipart(value = "file", required = false) byte[] file,
                                          @Multipart(value = "gameName", required = false) String gameName,
                                          @Multipart(value = "gameAccount", required = false) String gameAccount, @Context HttpServletRequest request) {
        // 初始化返回数据
        UploadPasspodResponse response = new UploadPasspodResponse();
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
            if (file == null || file.length == 0) {
                responseStatus.setStatus(ResponseCodes.EmptyPasspod.getCode(),
                        ResponseCodes.EmptyPasspod.getMessage());
                return response;
            }

            String[] url = fileManager.savePasspod(file, userInfo.getUid());

            if (StringUtils.isEmpty(gameName)) {
                gameName = request.getHeader("gamegold-gameName");
            }
            if (StringUtils.isEmpty(gameAccount)) {
                gameAccount = request.getHeader("gamegold-gameAccount");
            }

            RepositoryInfo repositoryInfo = new RepositoryInfo();
            repositoryInfo.setGameName(gameName);
            repositoryInfo.setGameAccount(gameAccount);
            repositoryInfo.setAccountUid(userInfo.getUid());
            repositoryInfo.setLoginAccount(userInfo.getLoginAccount());
            repositoryInfo.setPasspodUrl(url[0]);

            repositoryManager.updatePasspod(repositoryInfo);
            response.setPasspodUrl(url[0]);
            responseStatus.setStatus(ResponseCodes.Success.getCode(),
                    ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("卖家上传密保卡发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(),
                    ResponseCodes.UnKnownError.getMessage());
            logger.error("卖家上传密保卡发生异常:{}", ex);
        }
        logger.debug("卖家上传密保卡响应信息:{}", response);
        return response;
    }

    @Path("querysellerinfo")
    @POST
    @Override
    public IServiceResponse querySellerInfo(
            QuerySellerInfoRequest querySellerInfoRequest,
            @Context HttpServletRequest request) {
        logger.debug("当前查询卖家信息请求信息:{}", querySellerInfoRequest);
        // 初始化返回数据
        QuerySellerInfoResponse response = new QuerySellerInfoResponse();
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
            SellerInfo sellerInfo = sellerManager.querySellerInfo(userInfo.getLoginAccount());
            String firmsAccount=firmsAccountManager.queryFirmsAccountByLoginAccount(userInfo.getLoginAccount());
            response.setSellerInfo(sellerInfo);
            response.setCopyRight(firmsAccount);
            responseStatus.setStatus(ResponseCodes.Success.getCode(),
                    ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("查询卖家信息发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(),
                    ResponseCodes.UnKnownError.getMessage());
            logger.error("查询卖家信息发生异常:{}", ex);
        }
        logger.debug("查询卖家信息响应信息:{}", response);
        return response;
    }

    @Path("alterservice")
    @Override
    @POST
    public IServiceResponse alterServicer(
            QuerySellerInfoRequest querySellerInfoRequest,
            @Context HttpServletRequest request) {
        logger.debug("当前查询卖家信息请求信息:{}", querySellerInfoRequest);
        // 初始化返回数据
        QuerySellerInfoResponse response = new QuerySellerInfoResponse();
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
            if (querySellerInfoRequest.getServicerId() == 0) {
                responseStatus.setStatus(ResponseCodes.EmptyAlterServiceID.getCode(),
                        ResponseCodes.EmptyAlterServiceID.getMessage());
                return response;
            }

            SellerInfo sellerInfo = sellerManager.querySellerInfo(userInfo.getLoginAccount());
            long previousServiceId = sellerInfo.getServicerId();

            sellerInfo.setServicerId(querySellerInfoRequest.getServicerId());
            sellerInfo.setLastUpdateTime(new Date());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("accountUid", sellerInfo.getUid());
            sellerDBDAO.update(sellerInfo);
            response.setSellerInfo(sellerInfo);
//			List<RepositoryInfo> repositoryInfos =  repositoryDBDAO.selectByMap(map,"ID", true);
//			if(repositoryInfos!=null){
//				for(RepositoryInfo repositoryInfo:repositoryInfos){
//					repositoryInfo.setServicerId(querySellerInfoRequest.getServicerId());
//					repositoryInfo.setLastUpdateTime(new Date());
//					repositoryDBDAO.update(repositoryInfo);
//					// 客服处理订单数初始化
//					// 意思即：一个客服有库存了，即可以开始处理订单了
//					if (!servicerOrderManager.isInitOrderNum(repositoryInfo.getGameName(),
//							repositoryInfo.getRegion(), repositoryInfo.getServer(),
//							repositoryInfo.getGameRace(), repositoryInfo.getServicerId())) {
//						servicerOrderManager.initOrderNum(repositoryInfo.getGameName(),
//								repositoryInfo.getRegion(), repositoryInfo.getServer(),
//								repositoryInfo.getGameRace(),
//								repositoryInfo.getServicerId());
//					}
//					// redis中库存总量
//					// redis查询速度快，方便前台查询符合条件的客服信息
//					//更新更改后的客服redis
//					Long goldCount = repositoryManager.queryMaxCount(repositoryInfo.getGameName(), repositoryInfo.getRegion(),
//							repositoryInfo.getServer(), repositoryInfo.getGameRace(), repositoryInfo.getServicerId());
//					repositoryRedisDAO.saveRepositorySum(repositoryInfo, goldCount);
//					// redis中库存总量
//					// redis查询速度快，方便前台查询符合条件的客服信息
//					//更新以前的客服redis
//					Long previousGoldCount = repositoryManager.queryMaxCount(repositoryInfo.getGameName(), repositoryInfo.getRegion(),
//							repositoryInfo.getServer(), repositoryInfo.getGameRace(), previousServiceId);
//					repositoryInfo.setServicerId(previousServiceId);
//					repositoryRedisDAO.saveRepositorySum(repositoryInfo, previousGoldCount);
//				}
//			}
//			response.setRepositoryInfos(repositoryInfos);
            responseStatus.setStatus(ResponseCodes.Success.getCode(),
                    ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("查询卖家信息发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(),
                    ResponseCodes.UnKnownError.getMessage());
            logger.error("查询卖家信息发生异常:{}", ex);
        }
        logger.debug("查询卖家信息响应信息:{}", response);
        return response;
    }
}
