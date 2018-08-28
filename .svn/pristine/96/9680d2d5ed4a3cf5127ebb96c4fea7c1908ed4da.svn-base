package com.wzitech.gamegold.facade.frontend.service.repository;

import com.wzitech.gamegold.facade.frontend.service.repository.dto.CheckRepositoryManageResponse;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.RepositoryDTO;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.RepositoryResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import java.io.InputStream;


/**
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/05  wubiao              解密请求路径获取用户账号
 */
public interface ICheckRepositoryService {
//    /**
//     * 上传库存excel文件
//     *
//     * @param gameName
//     * @param in
//     * @param request
//     * @return
//     */
//    RepositoryResponse uploadRepository(String gameName, InputStream in, HttpServletRequest request);

    RepositoryResponse uploadRepositoryWithJsonData(String jsonData,HttpServletRequest request);
//
//    /**
//     * 添加库存
//     *
//     * @param repositoryDTO
//     * @param request
//     * @return
//     */
//    RepositoryResponse addRepository(RepositoryDTO repositoryDTO, HttpServletRequest request);
//
//    /**
//     * 批量修改库存
//     *
//     * @param jsondata 批量库存json数据
//     * @param request
//     * @return
//     */
//    RepositoryResponse UpdateRepository(String jsondata, HttpServletRequest request);


    /**
     * 分页查询库存
     *
     * @param repositoryDTO
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    RepositoryResponse queryRepository(RepositoryDTO repositoryDTO, Integer page, Integer pageSize,
                                       String sortBy, Boolean isAsc, HttpServletRequest request);

    /**
     * 注册令牌
     *
     * @param firmsSecret  厂商密匙
     * @param loginAccount 登陆卖家账号
     * @param sign         Md5加密字符串
     */
    CheckRepositoryManageResponse token(String firmsSecret, String loginAccount, String sign, @Context HttpServletResponse response);



}
