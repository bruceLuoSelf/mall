package com.wzitech.gamegold.common.enums;

/**
 * Created by 339931 on 2017/5/11.
 * 机器转人工状态枚举
 */
public enum MachineArtificialStatus {
    /**
     *  已转人工
     */
    ArtificialTransferSuccess(1,"已转人工"),
    /**
     *  转人工失败
     */
    ArtificialTransferFailed(2,"转人工失败"),

    /**
     *  转人工失败
     */
    ArtificialAuto(3,"机器开始上号交易"),

    ;

    private int code;
    private String name;

    MachineArtificialStatus(int code,String name){
        this.code=code;
        this.name=name;
    }

    /**
     * 根据code获取对应的枚举类
     * @param code
     * @return
     */
    public static MachineArtificialStatus getTypeByCode(int code){
        for(MachineArtificialStatus type:MachineArtificialStatus.values()){
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
