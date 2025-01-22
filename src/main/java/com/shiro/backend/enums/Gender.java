package com.shiro.backend.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Gender {
    MALE(0, "男"),
    FEMALE(1, "女"),
    OTHER(2, "扫地机器人-爱丽丝");

    @EnumValue // 告诉 MyBatis-Plus，枚举类中哪个字段会被映射到数据库中
    private final int value;
    @JsonValue
    private final String description;

    Gender(int value, String description) {
        this.value = value;
        this.description = description;
    }


    /*  @JsonValue用来告诉 Jackson（或其他 JSON 序列化工具），在序列化枚举时应该输出哪个字段
        这样就可以在序列化为JSON的时候输出为
        {
            "id": 1,
                "name": "TestUser",
                "sex": "Male", // 输出的是 description，而非 value
                "avatar": "http://example.com/avatar.png",
                "createdAt": "2025-01-07T12:34:56",
                "updatedAt": "2025-01-07T12:34:56"
        }*/
}
