package com.shiro.main.domain.dto;

import com.shiro.main.domain.po.RenewCategory;
import com.shiro.main.exception.CategoryWrongArgsException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "循环计费分类-数据传输对象")
public class AddRenewCategoryDTO {
    @ApiModelProperty(value = "循环计费分类名称", required = true, example = "测试用循环账单分类")
    private String name;

    public RenewCategory toEntity() {
        RenewCategory renewCategory = new RenewCategory();
        if (name == null) {
            throw new CategoryWrongArgsException("缺少分类名称参数");
        }
        renewCategory.setName(this.name);
        return renewCategory;
    }
}
