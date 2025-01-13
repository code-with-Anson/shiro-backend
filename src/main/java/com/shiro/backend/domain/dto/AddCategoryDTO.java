package com.shiro.backend.domain.dto;

import com.shiro.backend.domain.po.Category;
import com.shiro.backend.exception.CategoryWrongArgsException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "常规账单分类-数据传输对象")
public class AddCategoryDTO {
    @ApiModelProperty(value = "常规账单分类名称", required = true, example = "测试用常规账单分类")
    private String name;

    public Category toEntity() {
        Category category = new Category();
        if (name == null) {
            throw new CategoryWrongArgsException("缺少分类名称参数");
        }
        category.setName(this.name);
        return category;
    }
}
