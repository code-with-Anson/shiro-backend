package com.shiro.main.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "按照常规账单分类查找账单-传输实体类")
public class QueryCategoryBillsDTO extends PageDTO {
    @ApiModelProperty(value = "分类id列表", required = true, example = "1")
    private Long categoryId;
}
