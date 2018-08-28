package com.wzitech.gamegold.goods.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.goods.dao.IFirmsAccountDBDAO;
import com.wzitech.gamegold.goods.entity.FirmInfo;
import com.wzitech.gamegold.goods.entity.FirmsAccount;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/7/5
 * 厂商信息
 */
@Repository
public class FirmsAccountBDDAOImpl extends AbstractMyBatisDAO<FirmsAccount, Long> implements IFirmsAccountDBDAO {

    @Override
    public void batchUpdateName(FirmInfo firmInfo) {
        this.getSqlSession().update(this.mapperNamespace + ".batchUpdateName", firmInfo);
    }

    @Override
    public void delAccountByfirmsSecret(String firmsSecret) {
        this.getSqlSession().update(this.mapperNamespace + ".delAccountByfirmsSecret", firmsSecret);
    }

    @Override
    public FirmsAccount getFirmsAccountByMap(Map<String, Object> map) {
        return this.getSqlSession().selectOne(this.mapperNamespace + ".checkSelectByMap", map);
    }

    @Override
    public void batchUpdateSecret(List<FirmsAccount> firmsAccountList) {
        this.getSqlSession().update(this.mapperNamespace + ".batchUpdateSecret", firmsAccountList);
    }

    @Override
    public String queryFirmsSecretByLoginAccount(String loginAccount) {
        return this.getSqlSession().selectOne(this.mapperNamespace + ".queryFirmsSecretByLoginAccount", loginAccount);
    }
}
