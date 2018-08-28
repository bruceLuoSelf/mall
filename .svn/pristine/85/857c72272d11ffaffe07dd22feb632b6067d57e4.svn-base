package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.BlackListEO;

/**
 * Created by 340032 on 2017/12/25.
 */
public interface IBalckListDao extends IMyBatisBaseDAO<BlackListEO,Long> {

    /**
     * 通过5173名查找用户
     * @param
     */
    public BlackListEO findUserByLoginAccount(String loginAccount);


    /**
     * 根据用户Id查找用户
     * @param id
     * @return
     */
    public BlackListEO findUserById(Long  id);


    /**
     * 通过5173用户和状态判断是否禁止出货
     */
    public  BlackListEO blackListAccount(String loginAccount,Boolean enable);


}
