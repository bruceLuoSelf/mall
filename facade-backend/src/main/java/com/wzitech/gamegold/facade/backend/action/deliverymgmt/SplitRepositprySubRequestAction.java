package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.ISplitRepositorySubRequestManager;
import com.wzitech.gamegold.shorder.entity.SplitRepositorySubRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 340032 on 2018/6/29.
 */

@Controller
@Scope("prototype")
@ExceptionToJSON
public class SplitRepositprySubRequestAction extends AbstractAction {

    private SplitRepositorySubRequest splitRepositorySubRequest;

    private List<SplitRepositorySubRequest> splitRepositorySubRequestList;

    @Autowired
    ISplitRepositorySubRequestManager splitRepositorySubRequestManager;

    /**
     * 查询分仓子订单信息列表
     */
    public String querySplitRepositprySubRequest(){
        Map<String,Object> queryMap=new HashMap<String, Object>();
        if (splitRepositorySubRequest !=null){
            if (splitRepositorySubRequest.getOrderId() !=null){
                if(StringUtils.isNotBlank(splitRepositorySubRequest.getOrderId())){
                    queryMap.put("orderId",splitRepositorySubRequest.getOrderId());
                }

            }
        }
        GenericPage<SplitRepositorySubRequest> genericPage=splitRepositorySubRequestManager.queryListInPage(queryMap,
                this.limit,this.start,"update_time",false);
        splitRepositorySubRequestList=genericPage.getData();
        totalCount=genericPage.getTotalCount();
        return this.returnSuccess();
    }

    public SplitRepositorySubRequest getSplitRepositorySubRequest() {
        return splitRepositorySubRequest;
    }

    public void setSplitRepositorySubRequest(SplitRepositorySubRequest splitRepositorySubRequest) {
        this.splitRepositorySubRequest = splitRepositorySubRequest;
    }

    public List<SplitRepositorySubRequest> getSplitRepositorySubRequestList() {
        return splitRepositorySubRequestList;
    }

    public void setSplitRepositorySubRequestList(List<SplitRepositorySubRequest> splitRepositorySubRequestList) {
        splitRepositorySubRequestList = splitRepositorySubRequestList;
    }

    public ISplitRepositorySubRequestManager getSplitRepositorySubRequestManager() {
        return splitRepositorySubRequestManager;
    }

    public void setSplitRepositorySubRequestManager(ISplitRepositorySubRequestManager splitRepositorySubRequestManager) {
        this.splitRepositorySubRequestManager = splitRepositorySubRequestManager;
    }
}
