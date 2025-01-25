package com.shiro.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "bill.category")

public class DefaultCategories {

    // 支出分类列表
    private List<CategoryConfig> expense;

    // 收入分类列表
    private List<CategoryConfig> income;

    // 内部配置类
    @Data
    public static class CategoryConfig {
        private String name;
        private Integer sort;
    }
}