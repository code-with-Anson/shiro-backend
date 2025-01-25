package com.shiro.backend.domain.dto;

import com.shiro.backend.domain.po.Category;
import com.shiro.backend.enums.CategoryType;
import com.shiro.backend.exception.CategoryWrongArgsException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "常规账单分类-数据传输对象")
public class AddCategoryDTO {
    @ApiModelProperty(value = "常规账单分类名称", required = true, example = "测试用常规账单分类")
    private String name;

    @ApiModelProperty(value = "常规账单分类类型", required = true, example = "支出")
    private CategoryType categoryType;

    public Category toEntity() {
        Category category = new Category();
        if (name == null || categoryType == null) {
            throw new CategoryWrongArgsException("缺少分类名称参数");
        }
        category.setName(this.name);
        category.setCategoryType(this.categoryType);
        return category;
    }
}
