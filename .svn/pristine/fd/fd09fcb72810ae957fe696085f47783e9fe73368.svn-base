package com.wzitech.gamegold.repository.business;


import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.repository.entity.TransferFile;

import java.util.List;
import java.util.Map;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/20  wangmin           ZW_J_JB_00073 金币商城增加区服互通功能
 */
public interface ITransFileManager {
    /**
     * 修改
     * @param
     * @throws SystemException
     */
    void modifyTransFile(Long id, String JsonString, String gameName) throws SystemException;

    /**
     * 查询
     * @param map
     * @param pageSize
     * @param start
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<TransferFile> selectTransFileInfo(Map<String, Object> map, int pageSize, int start, String orderBy, Boolean isAsc);

    List<TransferFile> selectAllInfo(String gameName, Long id);
}
