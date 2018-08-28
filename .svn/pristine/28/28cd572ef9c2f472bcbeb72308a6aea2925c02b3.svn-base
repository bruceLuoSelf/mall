package com.wzitech.gamegold.facade.frontend.service.repository;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.RepositoryDTO;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.RepositoryResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

/**
 * 卖家库存管理Service
 *
 * @author yemq
 */
public interface IRepositoryManageService {
    /**
     * 上传库存excel文件
     *
     * @param gameName
     * @param in
     * @param request
     * @return
     */
    RepositoryResponse upload(String gameName, InputStream in, HttpServletRequest request);

    /**
     * 添加库存
     *
     * @param repositoryDTO
     * @param request
     * @return
     */
    RepositoryResponse addRepository(RepositoryDTO repositoryDTO, HttpServletRequest request);

    /**
     * 修改库存
     *
     * @param repositoryDTO
     * @param request
     * @return

    RepositoryResponse updateRepository(RepositoryDTO repositoryDTO, HttpServletRequest request);*/

    /**
     * 批量修改库存
     * @param jsondata 批量库存json数据
     * @param request
     * @return
     */
    RepositoryResponse batchUpdateRepository(String jsondata, HttpServletRequest request);

    /**
     * 删除库存
     *
     * @param ids
     * @param request
     * @return
     */
    RepositoryResponse removeRepository(List<Long> ids, HttpServletRequest request);

    /**
     * 分页查询库存
     * @param repositoryDTO
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    RepositoryResponse queryRepository(RepositoryDTO repositoryDTO, Integer page, Integer pageSize,
                                       String sortBy, Boolean isAsc,  HttpServletRequest request);

    /**
     * 根据库存ID查询库存
     *
     * @param id
     * @param request
     * @return
     */
    IServiceResponse queryRepository(Long id, HttpServletRequest request);

    /**
     * 上线
     */
    IServiceResponse online();

    /**
     * 下线
     * @return
     */
    IServiceResponse offline();

    /**
     * 分页获取所有区服的最低价
     *
     * @param repositoryDTO
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    RepositoryResponse queryLowestPriceRepository(RepositoryDTO repositoryDTO,
                                                  Integer page,
                                                  Integer pageSize,
                                                  String sortByField,
                                                  Boolean isAsc,
                                                  HttpServletRequest request);

    /**
     * 批量修改库存
     * @param jsondata
     * @param request
     * @return
     */
    RepositoryResponse batchUpdateRepositoryForRob(String jsondata, HttpServletRequest request);

    /**
     * 分页查询库存
     * @param repositoryDTO
     * @param page
     * @param pageSize
     * @param sortByField
     * @param isAsc
     * @param request
     * @return
     */
    RepositoryResponse queryRepositoryForRob(RepositoryDTO repositoryDTO,Integer page, Integer pageSize, String sortByField,Boolean isAsc, HttpServletRequest request);
}
