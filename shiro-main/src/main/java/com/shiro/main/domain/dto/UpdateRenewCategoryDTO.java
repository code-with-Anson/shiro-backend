package com.shiro.main.domain.dto;

import com.shiro.main.domain.po.RenewCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "更新分类-数据传输对象", description = "用来传输更新循环账单分类的属性")
public class UpdateRenewCategoryDTO {
    @ApiModelProperty(value = "主键，由雪花算法生成", required = true, example = "1")
    private Long id;

    @ApiModelProperty(value = "分类的名称", required = true, example = "Kei酱很可爱")
    private String name;

    public RenewCategory toEntity() {
        RenewCategory renewCategory = new RenewCategory();
        renewCategory.setId(id);
        renewCategory.setName(name);
        return renewCategory;
    }
}
