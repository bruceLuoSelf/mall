package com.wzitech.gamegold.goods.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.goods.entity.Warning;
import com.wzitech.gamegold.goods.dao.IWarningDBDAO;
import org.springframework.stereotype.Repository;

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
@Repository
public class WarningDBDAOImpl extends AbstractMyBatisDAO<Warning, Long> implements IWarningDBDAO{

    public List<Warning> queryByGameAndGoodsType(Map map){
        return this.getSqlSession().selectList(getMapperNamespace() + ".queryByGameAndGoodsType",map);
    }
}
