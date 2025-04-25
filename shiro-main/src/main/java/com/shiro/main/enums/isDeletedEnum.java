package com.shiro.main.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum isDeletedEnum {
    notDeleted(0, "正常"),
    isDeleted(1, "被逻辑删除");

    @EnumValue
    private final int value;
    @JsonValue
    private final String description;

    isDeletedEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
