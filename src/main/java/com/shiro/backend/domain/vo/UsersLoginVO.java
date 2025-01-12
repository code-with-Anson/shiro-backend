package com.shiro.backend.domain.vo;

import lombok.Data;

@Data
public class UsersLoginVO {
    private Long userId;
    private String token;
}
