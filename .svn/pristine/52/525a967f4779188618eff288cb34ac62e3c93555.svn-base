package com.wzitech.gamegold.repository.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.repository.business.ISellerSettingManager;
import com.wzitech.gamegold.repository.dao.ISellerSettingDBDAO;
import com.wzitech.gamegold.repository.entity.SellerSetting;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SellerSettingManagerImpl extends AbstractBusinessObject implements ISellerSettingManager {
    @Autowired
    ISellerSettingDBDAO sellerSettingDBDAO;

    @Override
    public GenericPage<SellerSetting> querySellerSetting(
            Map<String, Object> queryMap, int limit, int start, String sortBy,
            boolean isAsc) {
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "ID";
        }

        GenericPage<SellerSetting> genericPage = sellerSettingDBDAO.selectByMap(queryMap, limit,
                start, sortBy, isAsc);

        return genericPage;
    }

    @Override
    @Transactional
    public Boolean addSellerSetting(SellerSetting sellerSetting) throws SystemException {
        if(sellerSetting==null){
            throw new SystemException(ResponseCodes.SellerSettingEmptyWrong.getCode());
        }

        Map<String, Object> queryParam=new HashMap<String, Object>();
        queryParam.put("gameName",sellerSetting.getGameName());
        //queryParam.put("region",sellerSetting.getRegion());
        queryParam.put("loginAccount",sellerSetting.getLoginAccount());
        queryParam.put("goodsTypeName",sellerSetting.getGoodsTypeName());
        if(sellerSettingDBDAO.IsExsitGameAndRegion(queryParam))
        {
            return false;
        }
        else
        {
            sellerSetting.setIsDeleted(true);
            sellerSettingDBDAO.insert(sellerSetting);
            return true;
        }
    }

    @Override
    @Transactional
    public Boolean modifySellerSetting(SellerSetting sellerSetting) throws SystemException {
        Map<String, Object> queryParam=new HashMap<String, Object>();
        queryParam.put("gameName",sellerSetting.getGameName());
        queryParam.put("loginAccount",sellerSetting.getLoginAccount());
        List<SellerSetting> list= sellerSettingDBDAO.selectByMap(queryParam,"ID",true);
        if(list!=null&&list.size()>0) {
            long id = list.get(0).getId();
            if(id!=sellerSetting.getId().longValue()){
                return false;
            }
        }
        // 更新DB
        sellerSettingDBDAO.update(sellerSetting);
        return true;
    }

    @Override
    @Transactional
    public void deleteSellerSetting(List<Long> ids) throws SystemException {
        if(ids==null||ids.size()==0){
            throw new SystemException(ResponseCodes.UserIdEmptyWrong.getCode());
        }

        sellerSettingDBDAO.batchDeleteByIds(ids);
    }

    @Override
    public void settingEnable(List<Long> ids,Boolean isDeleted) {
        List<SellerSetting> list=new ArrayList<SellerSetting>();
        for (Long id : ids) {
            SellerSetting sellerSetting = sellerSettingDBDAO.findById(id);
            if(sellerSetting==null){
                throw new SystemException(ResponseCodes.UserNotExitedWrong.getCode());
            }
            sellerSetting.setIsDeleted(isDeleted);
            list.add(sellerSetting);
        }
        sellerSettingDBDAO.batchUpdate(list);
    }

    @Override
    public List<SellerSetting> selectByLoginAccountList(String gameName,String loginAccountSql)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("gameName", gameName);
        map.put("loginAccountList",loginAccountSql);
        return sellerSettingDBDAO.selectByLoginAccountList(map);
    }

    @Override
    public SellerSetting querySellSettingByGameNameAndSeller(String gameName, String loginAccount,String goodsTypeName) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("gameName", gameName);
        queryMap.put("loginAccount",loginAccount);
        queryMap.put("isDeleted", false);
        queryMap.put("goodsTypeName",goodsTypeName);
        return sellerSettingDBDAO.selectShopName(queryMap);
    }

    /**
     * 更新店铺排序
     *
     * @param id
     * @param sort
     */
    @Override
    public void updateSort(long id, int sort) {
        sellerSettingDBDAO.updateSort(id, sort);
    }
}
