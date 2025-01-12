package com.shiro.backend.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum BillType {
    EXPENSE(0, "支出"),
    INCOME(1, "收入"),
    OTHER(2, "其他");

    @EnumValue // 告诉 MyBatis-Plus，枚举类中哪个字段会被映射到数据库中
    private final int value;
    @JsonValue
    private final String description;

    BillType(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
