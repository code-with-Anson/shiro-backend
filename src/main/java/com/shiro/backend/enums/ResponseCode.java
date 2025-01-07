package com.shiro.backend.enums;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS(20039, "成功"),
    FAILURE(50039, "失败");

    private final int value; // 使用 final
    private final String description; // 使用 final


    ResponseCode(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
