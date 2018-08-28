package com.wzitech.gamegold.common.enums;

/**
 * 短信操作类型
 * @author yemq
 */
public enum MessageOperateType implements GenericEnum<MessageOperateType>{
    ADD(1, "添加"),
    MODIFY(2, "修改"),
    ENABLED(3, "挂起"),
    DISABLED(4, "解挂");

    private int code;
    private String name;

    MessageOperateType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取枚举代码
     *
     * @return
     */
    @Override
    public String getEnumCode() {
        return String.valueOf(this.code);
    }
}
