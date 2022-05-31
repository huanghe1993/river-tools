package com.huanghe.enums;

/**
 * 功能描述 简单的枚举类，即只包含value(实现此接口可使用{@link EnumUtils}中的方法
 *
 * @author h00518386
 * @since 2022-02-28
 */
public interface ValueEnum<T> {

    /**
     * 获取枚举值
     *
     * @return 枚举值
     */
    T getValue();
}
