package com.wzitech.gamegold.repository.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.repository.entity.SellerSetting;

import java.util.List;
import java.util.Map;

/**
 * 店铺配置信息
 */
public interface ISellerSettingDBDAO extends IMyBatisBaseDAO<SellerSetting, Long> {
    public SellerSetting findById(Long  uid);

    /**
     *
     * 是否存在
     * @author wangjunjie
     * @param paramMap
     * @return
     * @see
     */
    public Boolean IsExsitGameAndRegion(Map<String, Object> paramMap);

    /**
     *
     * 查询包含店铺名称信息
     * @author wangjunjie
     * @param paramMap
     * @return
     * @see
     */
    public SellerSetting selectShopName(Map<String, Object> paramMap);

    /**
     * 根据游戏和卖家账号查找店铺信息列表
     * @param paramMap
     * @return
     */
    public List<SellerSetting> selectByLoginAccountList(Map<String, Object> paramMap);

    /**
     * 更新店铺排序
     * @param id
     * @param sort
     */
    void updateSort(long id, int sort);
}
