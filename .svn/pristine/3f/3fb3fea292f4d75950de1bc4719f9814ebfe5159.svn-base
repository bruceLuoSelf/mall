package com.wzitech.gamegold.order.business.impl.orderstate;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.LogType;
import com.wzitech.gamegold.common.enums.OrderCenterOrderStatus;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.log.entity.OrderLogInfo;
import com.wzitech.gamegold.common.servicer.IServicerOrderManager;
import com.wzitech.gamegold.goods.business.IGoodsInfoManager;
import com.wzitech.gamegold.order.business.*;
import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.shorder.business.IFundManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 订单改变到结单状态的管理接口实现类
 *
 * @author ztjie
 */
@Component
public class Statement extends AbstractState {

    protected static final Log log = LogFactory.getLog(Statement.class);

    @Autowired
    IOrderInfoDBDAO orderInfoDBDAO;

    /*@Autowired
    ILogManager logManager;*/
    @Autowired
    IOrderLogManager orderLogManager;

    @Autowired
    IAutoPayManager autoPayManager;

    @Autowired
    IGoodsInfoManager goodsInfoManager;

    @Autowired
    IServicerOrderManager servicerOrderManager;

    @Autowired
    IFundManager fundManager;

    @Autowired
    IZbaoFundManager zbaoFundManager;

    @Autowired
    IOrderConfigManager orderConfigManager;

    @Autowired
    ISellerManager sellerManager;


    @Autowired
    IOrderPushMainManager orderPushMainManager;


    /*@Autowired
    private IInsuranceOrderManager insuranceOrderManager;*/

    private OrderInfoEO changeOrderState(String orderId, OrderState orderState)
            throws SystemException {
        if (orderId == null) {
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(),
                    ResponseCodes.EmptyOrderId.getMessage());
        }
        // 更新订单设置订单状态
        OrderInfoEO orderInfo = orderInfoDBDAO.queryOrderForUpdate(orderId);

        // 订单状态无改变，就不做后续操作
        if (orderState.equals(orderInfo.getOrderState())) {
            return null;
        }

        // 订单状态先前状态必须是已发货或已收货
        if (orderInfo.getOrderState() != OrderState.Delivery.getCode()
                && orderInfo.getOrderState() != OrderState.Receive.getCode()) {
            throw new SystemException(ResponseCodes.StateAfterNotIn.getCode(),
                    "订单状态先前状态必须是已发货或已收货");
        }

        return orderInfo;
    }

    @Override
    @Transactional
    public OrderInfoEO handle(String orderId){
        OrderInfoEO orderInfo = this.changeOrderState(orderId,
                OrderState.Statement);
        if (orderInfo == null) {
            return orderInfo;
        }
        //订单先前状态
        Integer orderOldState = orderInfo.getOrderState();
        // 订单完成，设置完成时间
        log.info("订单状态是结单状态，调用打款方法");
        orderInfo.setEndTime(new Date());

//        //查询当前所有的子订单信息，需要给开通新资金的收货商进行充值操作
//        List<ConfigResultInfoEO> configList = orderConfigManager.orderConfigList(orderId, false);
//        for (ConfigResultInfoEO configResult : configList) {
//            //判断是否为已发货的订单
//            if (configResult.getState().equals(OrderState.Delivery.getCode())) {
//                String loginAccount = configResult.getLoginAccount();
//                //查询当前卖家的信息，判断是否已绑定7bao并且开通新资金
//                SellerInfo sellerInfo = sellerManager.findByAccountAndUid(loginAccount, configResult.getAccountUid());
//                if (sellerInfo.getIsNewFund()!=null && sellerInfo.getIsNewFund() && sellerInfo.getisAgree()!=null && sellerInfo.getisAgree()) {
//                    //调用7bao增加资金接口，注意增加的资金为卖家的收益款
//                    zbaoFundManager.recharge7Bao(loginAccount, configResult.getIncome(), FundType.RECHARGE_AUTO_7BAO.getCode());
//                }
//            }
//        }

        boolean result = autoPayManager.pay(orderInfo);
//		//调用7bao增加资金接口
//		zbaoFundManager.recharge7Bao(orderInfo.getUserAccount(),orderInfo.getTotalPrice(), RECHARGE_AUTO_7BAO.getCode());
        if (result) {
            // 修改订单状态
            orderInfo.setOrderState(OrderState.Statement.getCode());

            // 更新订单状态
            orderInfoDBDAO.updateOrderState(orderInfo);
            //商城销售订单同步推送Mq
            int orderStatus= OrderCenterOrderStatus.SUCCESS_TRADE.getCode();
            orderPushMainManager.orderPushMain(orderInfo);

            // 创建需要创建保单的订单记录
            /*if (orderInfo.getIsBuyInsurance() != null && orderInfo.getIsBuyInsurance()) {
                InsuranceOrder insuranceOrder = new InsuranceOrder();
				insuranceOrder.setOrderId(orderId);
				insuranceOrder.setType(InsuranceOrder.TYPE_CREATE_ORDER);
				insuranceOrderManager.addOrder(insuranceOrder);
			}*/

            // 增加订单修改日志
            StringBuffer sb = new StringBuffer();
            sb.append("订单号：").append(orderInfo.getOrderId()).append("，订单状态从");
            sb.append(OrderState.getTypeByCode(orderOldState).getName());

            sb.append("变成").append(OrderState.Statement.getName());

            //logManager.add(ModuleType.ORDER, sb.toString(), CurrentUserContext.getUser());
            log.info(sb.toString());

            OrderLogInfo orderLogInfo = new OrderLogInfo();
            orderLogInfo.setLogType(LogType.ORDER_STATEMENT);
            orderLogInfo.setOrderId(orderId);
            orderLogInfo.setRemark(sb.toString());
            orderLogManager.add(orderLogInfo);

            // 添加商品销量  只有订单结单才做商品销量加1的操作
            goodsInfoManager.addSales(orderInfo.getGoodsId());
            // 客服处理订单数减1
//			servicerOrderManager.subOrderNum(orderInfo.getGameName(),
//					orderInfo.getRegion(), orderInfo.getServer(),
//					orderInfo.getGameRace(), orderInfo.getServicerId());
//			log.info("订单状态修改成功，并打款成功");
        } else {
            log.info("订单状态修改失败，并打款失败");
            throw new SystemException(
                    ResponseCodes.ChangeOrderInfoError.getCode(),
                    ResponseCodes.ChangeOrderInfoError.getMessage());
        }
        return orderInfo;
    }
}
