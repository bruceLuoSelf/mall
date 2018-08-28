package com.wzitech.gamegold.common.enums;

/**
 * Created by 339931 on 2017/5/11.
 */
public enum MachineArtificialReason{

    OvertimeDeal(301,"交易超时"),
    BackpackFull(302,"背包已满"),
    OvertimeTransaction(303,"不是附魔师"),
    CodeAndPasswordError(304,"帐号密码错误"),
    DynamicToken(305,"动态令牌"),
    TransactionLocationError(306,"不在交易地点"),
    InitialState(307,"出战状态"),
    OtherReason(308,"其他原因");

    private int code;
    private String name;

    MachineArtificialReason(int code, String name){
        this.code=code;
        this.name=name;
    }

    /**
     * 根据code获取对应的枚举类
     * @param code
     * @return
     */
    public static MachineArtificialReason getTypeByCode(int code){
        for(MachineArtificialReason type:MachineArtificialReason.values()){
            if(code==type.getCode()){
                return  type;
            }
        }
        throw new IllegalArgumentException("未能找到匹配的MachineArtificialStatus："+code);
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
