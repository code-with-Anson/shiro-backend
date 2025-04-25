package com.shiro.main.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum isRenew {
    notRenew(0, "未开启"),
    isRenew(1, "开启");

    @EnumValue
    private final int value;
    @JsonValue
    private final String description;

    isRenew(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
