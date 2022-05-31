package com.huanghe.enums;

public enum FileTypeEnum implements ValueEnum<String> {
    ZIP(".zip");

    private final String value;

    FileTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
