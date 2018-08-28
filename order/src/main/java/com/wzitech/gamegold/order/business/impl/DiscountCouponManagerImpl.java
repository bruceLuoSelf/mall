package com.wzitech.gamegold.order.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.CouponPriceKind;
import com.wzitech.gamegold.common.enums.CouponType;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.utils.DateUtil;
import com.wzitech.gamegold.order.business.IDiscountCouponManager;
import com.wzitech.gamegold.order.dao.IDiscountCouponDBDAO;
import com.wzitech.gamegold.order.entity.DiscountCoupon;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 红包
 */
@Component
public class DiscountCouponManagerImpl extends AbstractBusinessObject implements IDiscountCouponManager{
    @Autowired
    IDiscountCouponDBDAO discountCouponDBDAO;

    /**
     * 分页查找红包
     * @param queryMap
     * @param limit
     * @param start
     * @param sortBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<DiscountCoupon> queryDiscountCoupon( Map<String, Object> queryMap, int limit, int start, String sortBy,boolean isAsc) {
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "ID";
        }

        GenericPage<DiscountCoupon> genericPage = discountCouponDBDAO.selectByMap(queryMap, limit,
                start, sortBy, isAsc);

        return genericPage;
    }

    /**
     * 根据条件查找所有的优惠券
     * @param queryMap
     * @param sortBy
     * @param isAsc
     * @return
     */
    @Override
    public List<DiscountCoupon> queryDiscountCouponList( Map<String, Object> queryMap, String sortBy,boolean isAsc) {
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "ID";
        }
        return discountCouponDBDAO.selectByMap(queryMap, sortBy, isAsc);
    }

    /**
     * 查找一笔优惠券记录
     * @param code
     * @param pwd
     * @param couponType
     * @param price
     * @return
     * @throws SystemException
     */
    @Override
    public DiscountCoupon selectUniqueDisCountCoupon(String code,String pwd,Integer couponType,Double price) throws SystemException {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("code",code);
        paramMap.put("couponType", couponType);

        DiscountCoupon discountCoupon=discountCouponDBDAO.selectUniqueDisCountCoupon(paramMap);
        if(discountCoupon!=null)
        {
            if(!discountCoupon.getPwd().equals(pwd))
            {
                if(couponType== CouponType.Hb.getCode())
                {
                    //红包密码错误
                    throw new SystemException(ResponseCodes.ErrorHbPwdCoupon.getCode(), ResponseCodes.ErrorHbPwdCoupon.getMessage());
                }
                else
                {
                    //店铺券密码错误
                    throw new SystemException(ResponseCodes.ErrorDpPwdCoupon.getCode(), ResponseCodes.ErrorDpPwdCoupon.getMessage());
                }
            }
            if(discountCoupon.getIsUsed()){
                //优惠券已使用
                throw new SystemException(ResponseCodes.UsedCoupon.getCode(), ResponseCodes.UsedCoupon.getMessage());
            }
            else{
                if(discountCoupon.getPrice()==CouponPriceKind.Level1.getPrice()){
                    //2元优惠券
                    if(price<CouponPriceKind.Level1.getCode())
                    {
                        //小于100元
                        throw new SystemException(ResponseCodes.UnEnableUseCoupon.getCode(), ResponseCodes.UnEnableUseCoupon.getMessage());
                    }
                }
                else if(discountCoupon.getPrice()==CouponPriceKind.Level2.getPrice()){
                    //5元优惠券
                    if(price<CouponPriceKind.Level2.getCode()){
                        //小于250元
                        throw new SystemException(ResponseCodes.EnableUseCouponLevel1.getCode(), ResponseCodes.EnableUseCouponLevel1.getMessage());
                    }
                }
                else if(discountCoupon.getPrice()==CouponPriceKind.Level3.getPrice()){
                    //10元优惠券
                    if(price<CouponPriceKind.Level3.getCode()){
                        //小于500元
                        throw new SystemException(ResponseCodes.EnableUseCouponLevel2.getCode(), ResponseCodes.EnableUseCouponLevel2.getMessage());
                    }
                }
                else{
                    //其他面值优惠券，暂不支持
                    throw new SystemException(ResponseCodes.NoCoupon.getCode(), ResponseCodes.NoCoupon.getMessage());
                }
            }

            //优惠券已过期
            Date endTime = DateUtil.oneDateLastTime(discountCoupon.getEndTime());
            if (endTime == null) return null;
            Calendar c = Calendar.getInstance();
            c.setTime(endTime);
            long deadlineTime= c.getTimeInMillis();
            Date now = new Date();
            if(now.getTime()>deadlineTime){
                throw new SystemException(ResponseCodes.DeadlineCoupon.getCode(), ResponseCodes.DeadlineCoupon.getMessage());
            }
        }
        else
        {
            //无效的优惠券
            throw new SystemException(ResponseCodes.IllegalCoupon.getCode(), ResponseCodes.IllegalCoupon.getMessage());
        }
        return discountCoupon;
    }

    /**
     * 批量新增
     * @param list
     * @return
     */
    @Override
    public Boolean batchInsert(List<DiscountCoupon> list)
    {
        discountCouponDBDAO.batchInsert(list);
        return true;
    }

    /**
     * 设置红包为已使用
     * @param code
     * @param orderId
     * @param couponType
     * @return
     */
    @Override
    public Boolean UpdateCouponUsed(String code,String orderId,int couponType){
        Map<String, Object> queryMap = new HashMap<String,Object>();
        queryMap.put("code",code);
        queryMap.put("couponType",couponType);
        DiscountCoupon discountCoupon=discountCouponDBDAO.selectUniqueDisCountCoupon(queryMap);
        if(discountCoupon!=null){
            discountCoupon.setOrderId(orderId);
            discountCoupon.setIsUsed(true);
            discountCouponDBDAO.update(discountCoupon);
        }
        return true;
    }
}
