package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.order.business.IDiscountCouponManager;
import com.wzitech.gamegold.order.entity.DiscountCoupon;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("prototype")
@ExceptionToJSON
public class DiscountCouponAction  extends AbstractAction {
    @Autowired
    IDiscountCouponManager discountCouponManager;

    private List<DiscountCoupon> discountCouponList;

    private DiscountCoupon discountCoupon;

    private Boolean isUsed;

    /**
     * 查询评价统计信息
     * @return
     */
    public String queryDiscountCoupon(){
        Map<String, Object> paramMap = new HashMap<String, Object>();

        if(discountCoupon.getCouponType()!=0){
            paramMap.put("couponType",discountCoupon.getCouponType());
        }
        if(!StringUtils.isBlank(discountCoupon.getCode())){
            paramMap.put("code",discountCoupon.getCode());
        }
        if(!StringUtils.isBlank(discountCoupon.getOrderId()))
        {
            paramMap.put("orderId",discountCoupon.getOrderId());
        }
        paramMap.put("isUsed",isUsed);
        paramMap.put("startTime", discountCoupon.getStartTime());
        paramMap.put("endTime", WebServerUtil.oneDateLastTime(discountCoupon.getEndTime()));
        GenericPage<DiscountCoupon> genericPage = discountCouponManager.queryDiscountCoupon(paramMap, this.limit, this.start, "ID", true);
        discountCouponList = genericPage.getData();
        totalCount = genericPage.getTotalCount();

        return this.returnSuccess();
    }

    public List<DiscountCoupon> getDiscountCouponList() {
        return discountCouponList;
    }

    public void setDiscountCouponList(List<DiscountCoupon> discountCouponList) {
        this.discountCouponList = discountCouponList;
    }

    public DiscountCoupon getDiscountCoupon() {
        return discountCoupon;
    }

    public void setDiscountCoupon(DiscountCoupon discountCoupon) {
        this.discountCoupon = discountCoupon;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }
}
