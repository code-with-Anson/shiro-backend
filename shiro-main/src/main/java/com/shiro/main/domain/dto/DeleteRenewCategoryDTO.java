package com.shiro.main.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "批量删除分类-数据传输对象")
public class DeleteRenewCategoryDTO {
    @ApiModelProperty(value = "需要批量删除的分类的id列表", required = true, example = "[1, 2, 3]")
    private List<Long> renewCategoryIds;
}
