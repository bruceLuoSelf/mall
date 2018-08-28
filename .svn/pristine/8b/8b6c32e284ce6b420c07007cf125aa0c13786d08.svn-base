package com.wzitech.gamegold.common.enums;

/**
 * Created by 339931 on 2017/4/6.
 */
public enum RefundReason {

    ClosedTradeByErrorGame(101,"由于买家选错游戏区服，当前交易被迫终止"),
    ClosedTradeByErrorGameRace(102,"由于买家选错游戏阵营，当前交易被迫终止"),
    ClosedTradeByErrorGameRole(103,"由于买家的游戏角色名或数字ID错误，当前交易被迫终止"),
    ClosedTradeByErrorLevel(104,"由于买家的游戏角色名等级不够或未到达游戏运营商的最低交易等级，无法到指定地点 交易，当前交易被迫终止"),
    ClosedTradeBySeller(105,"由于买家通过QQ或者电话要求客服终止当前交易"),
    ClosedTradeByOvertimeAndDisConnect(106,"由于买家未及时交易，且客服通过QQ或电话均无法与买家取得联系，当前交易被迫终止"),
    CancelTradeByOvertime(107,"超过时间没有支付，订单自动取消"),
    OutOfStock(201,"卖家游戏角色无货或少货"),
    LoginExceptionByErrorGameRole(202,"卖家游戏角色数据异常，客服无法登录游戏"),
    BeyondLimitEmail(203,"卖家游戏内当日发送邮件次数已达上限"),
    BeyondLimitDelivery(204,"卖家游戏角色等级今日已达发货上限，无法发货，交易取消"),
    ServerBusy(301,"游戏运营商服务器维护（包括：服务器繁忙等）"),
    LoginExceptionByCarrieroperator(302,"游戏运营商原因，导致卖家游戏帐号无法登录游戏，交易取消"),
    OtherReson(401,"其他原因"),
    NotEnoughCapacity(303, "收货商收货角色异常，已进行撤单处理，请您重新下单。");

    private String name;
    private int code;

    RefundReason(int code,String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code值获取对应的枚举
     * @param code
     * @return
     */
    public static RefundReason getReasonByCode(int code){
        for(RefundReason reason:RefundReason.values()){
            if(reason.getCode()== code){
                return reason;
            }
        }
        throw new IllegalArgumentException("未能找到匹配的RefundReason:" + code);
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
