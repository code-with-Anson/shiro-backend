package com.shiro.main.domain.dto;

import com.shiro.main.domain.po.Category;
import com.shiro.main.enums.CategoryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "更新分类-数据传输对象", description = "用来传输更新常规账单分类的属性")
public class UpdateCategoryDTO {
    @ApiModelProperty(value = "主键，由雪花算法生成", required = true, example = "1")
    private Long id;

    @ApiModelProperty(value = "分类的名称", required = true, example = "Kei酱很可爱")
    private String name;

    @ApiModelProperty(value = "分类的类型", required = true, example = "支出")
    private CategoryType categoryType;


    public Category toEntity() {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setCategoryType(this.categoryType);
        return category;
    }
}
