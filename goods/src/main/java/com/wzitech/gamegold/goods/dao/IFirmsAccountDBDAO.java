package com.wzitech.gamegold.goods.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.goods.entity.FirmInfo;
import com.wzitech.gamegold.goods.entity.FirmsAccount;

import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/7/5
 * 厂商表DB
 */

public interface IFirmsAccountDBDAO extends IMyBatisBaseDAO<FirmsAccount, Long> {

   void batchUpdateName(FirmInfo firmInfo);

   void delAccountByfirmsSecret(String firmsSecret);

   /**
    * 新增厂商查询
    *add by wubiao on 20170713
    * @param map
    * @return
    */
   FirmsAccount getFirmsAccountByMap(Map<String, Object> map);

   void batchUpdateSecret(List<FirmsAccount> firmsAccountList);

   String queryFirmsSecretByLoginAccount(String loginAccount);

}
