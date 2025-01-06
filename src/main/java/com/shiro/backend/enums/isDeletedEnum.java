package com.shiro.backend.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum isDeletedEnum {
    notDeleted(0, "正常"),
    isDeleted(1, "被逻辑删除");

    @EnumValue
    private int value;
    @JsonValue
    private String description;

    isDeletedEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
