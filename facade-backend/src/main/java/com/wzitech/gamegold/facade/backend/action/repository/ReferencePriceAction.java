package com.wzitech.gamegold.facade.backend.action.repository;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.goods.business.IReferencePriceManager;
import com.wzitech.gamegold.goods.entity.ReferencePrice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jhlcitadmin on 2017/3/21.
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class ReferencePriceAction extends AbstractAction {
    @Autowired
    IReferencePriceManager referencePriceManager;
    private ReferencePrice referencePrice;

    private List<ReferencePrice> referencePriceList;

    public ReferencePrice getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(ReferencePrice referencePrice) {
        this.referencePrice = referencePrice;
    }

    public List<ReferencePrice> getReferencePriceList() {
        return referencePriceList;
    }

    public void setReferencePriceList(List<ReferencePrice> referencePriceList) {
        this.referencePriceList = referencePriceList;
    }
    //查询全部数据
    public String queryReferencePrice(){
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (referencePrice != null) {
            if(StringUtils.isNotBlank(referencePrice.getGameName())){
                queryMap.put("gameName",referencePrice.getGameName());
            }
            if(StringUtils.isNotBlank(referencePrice.getRegionName())){
                queryMap.put("regionName",referencePrice.getRegionName());
            }
            if(StringUtils.isNotBlank(referencePrice.getServerName())){
                queryMap.put("serverName",referencePrice.getServerName());
            }
            if(StringUtils.isNotBlank(referencePrice.getRaceName())){
                queryMap.put("raceName",referencePrice.getRaceName());
            }
            if(StringUtils.isNotBlank(referencePrice.getGoodsTypeName())){
                queryMap.put("goodsTypeName",referencePrice.getGoodsTypeName());
            }
        }
        GenericPage<ReferencePrice> genericPage = referencePriceManager.queryReferencePriceList(queryMap, this.limit, this.start, "id", true);
        referencePriceList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

}
