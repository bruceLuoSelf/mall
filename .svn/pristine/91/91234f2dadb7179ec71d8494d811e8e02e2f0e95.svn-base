package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.order.business.ICurrencyConfigManager;
import com.wzitech.gamegold.order.entity.CurrencyConfigEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 340032 on 2018/3/15.
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class CurrencyConfigAction extends AbstractAction {

    private CurrencyConfigEO currencyConfigEO;
    private List<CurrencyConfigEO> CurrencyConfigList;
    private Date createTime;
    private Date endTime;
    private Long id;
    @Autowired
    private ICurrencyConfigManager currencyConfigManager;
    //查询通货配置信息
    public String queryCurrencyConfig(){
        Map<String,Object> map=new HashMap<String, Object>();
        if (StringUtils.isNotBlank(currencyConfigEO.getGameName())) {
            map.put("gameName", currencyConfigEO.getGameName().trim());
        }
//        map.put("createTime",createTime);
//        map.put("endTime", WebServerUtil.oneDateLastTime(endTime));
//        GenericPage<CurrencyConfigEO> genericPage;
        GenericPage<CurrencyConfigEO> genericPage= currencyConfigManager.queryInPage(map, this.start,this.limit,"SORT", true);
        CurrencyConfigList=genericPage.getData();
        totalCount= genericPage.getTotalCount();
        return this.returnSuccess();
    }

    //添加配置信息
    public String addCurrencyConfig(){
        try {
            if (currencyConfigEO==null){
                throw new SystemException("需要添加的通货配置不能为空！");
            }
            CurrencyConfigEO config=currencyConfigManager.selectCurrency(currencyConfigEO);
            if (config !=null){
                throw new SystemException(
                        ResponseCodes.NotCurrencyConfig.getCode(),ResponseCodes.NotCurrencyConfig.getCode());
            }
            currencyConfigManager.addCurrencyConfig(currencyConfigEO);
            return this.returnSuccess();
        }catch (SystemException e){
            return this.returnError(e);
        }

    }

    //修改配置信息
    public String modifyCurrencyConfig(){
        try {
            if (currencyConfigEO ==null){
//                CurrencyConfigEO config=currencyConfigManager.selectCurrency(currencyConfigEO);
//                if (config !=null){
//                    throw new SystemException(
//                            ResponseCodes.NotCurrencyConfig.getCode(),ResponseCodes.NotCurrencyConfig.getCode());
//                }
                throw new SystemException("需要添加的通货配置不能为空！");
            }

            currencyConfigManager.modifyCurrencyConfig(currencyConfigEO);
            return this.returnSuccess();
        }catch (SystemException e){
            return this.returnError(e);
        }
    }

    //删除配置信息
    public String deleteCurrencyConfig(){
        try {
            if (currencyConfigEO.getId()!=null){
                currencyConfigManager.deleteCurrencyConfig(currencyConfigEO.getId());
            }
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }


    public CurrencyConfigEO getCurrencyConfigEO() {
        return currencyConfigEO;
    }

    public void setCurrencyConfigEO(CurrencyConfigEO currencyConfigEO) {
        this.currencyConfigEO = currencyConfigEO;
    }

    public List<CurrencyConfigEO> getCurrencyConfigList() {
        return CurrencyConfigList;
    }

    public void setCurrencyConfigList(List<CurrencyConfigEO> currencyConfigList) {
        CurrencyConfigList = currencyConfigList;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
