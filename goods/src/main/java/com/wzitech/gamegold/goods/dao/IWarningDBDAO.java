
package com.wzitech.gamegold.goods.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
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
public interface IWarningDBDAO extends IMyBatisBaseDAO<Warning, Long>{
    public List<Warning> queryByGameAndGoodsType(Map map);
}
