package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.dto.SellerDTO;

/**
 * Created by chengXY on 2017/8/24.
 */
public interface ISellerDTOdao extends IMyBatisBaseDAO<SellerDTO,Long> {
    //查询该出货单的卖家信息
    SellerDTO findByAccountAndUid(String account, String uid);

    //根据账号查询
    SellerDTO findByAccount(String account);

    //根据Id查询采购商是否开通了新资金
    SellerDTO findById(Long id);
}
