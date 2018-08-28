package com.wzitech.gamegold.common.enums;

/**
 * 枚举接口
 */
public interface GenericEnum<E extends Enum<E>> {
    /**
     * 获取枚举代码
     * @return
     */
    String getEnumCode();
}
