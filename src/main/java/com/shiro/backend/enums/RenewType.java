package com.shiro.backend.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RenewType {
    DAY(0, "日付"),
    MONTH(1, "月付"),
    YEAR(2, "年付");


    @EnumValue // 告诉 MyBatis-Plus，枚举类中哪个字段会被映射到数据库中
    private final int value;
    @JsonValue
    private final String description;

    RenewType(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
