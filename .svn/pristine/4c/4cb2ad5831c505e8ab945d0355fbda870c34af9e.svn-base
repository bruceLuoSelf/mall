package com.wzitech.gamegold.goods.business;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.goods.entity.Warning;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/15.
 * 友情提示配置
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/15  liuchanghua           ZW_C_JB_00008 商城增加通货
 */
public interface IWarningManager {

    /**
     * 新增
     * @param warning
     * @return
     */
    Long insert(Warning warning);

    /**
     * 删除
     * @param ids
     */
    void deleteWarning(List<Long> ids) throws SystemException;

    /**
     * 修改
     * @param warning
     */
    void update(Warning warning);

    /**
     * 按id查找
     * @param id
     * @return
     */
    Warning findById(Long id);

    /**
     * 分页查询
     * @param queryMap
     * @param limit
     * @param start
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<Warning> selectByMap(
            Map<String, Object> queryMap, int limit, int start, String orderBy, boolean isAsc);

    /**
     * 查询(不分页)
     * @param queryMap
     * @param sortBy
     * @param isAsc
     * @return
     */
    public List<Warning> queryByMap(Map<String,Object> queryMap, String sortBy, boolean isAsc);
}
