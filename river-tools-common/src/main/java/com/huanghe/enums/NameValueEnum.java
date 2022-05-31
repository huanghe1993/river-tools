package com.huanghe.enums;

/**
 * 功能描述：带有枚举Name和Value的接口(实现此接口可使用{@link EnumUtils}中的方法
 *
 * @author h00518386
 * @since 2022-02-28
 */
public interface NameValueEnum<T> extends ValueEnum<T> {

    /**
     * 获取枚举名称
     * @return 枚举名
     */
    String getName();
}
