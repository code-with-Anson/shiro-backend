package com.shiro.backend.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "逻辑删除循环账单-数据传输对象")
public class DeleteRenewBillDTO {
    @ApiModelProperty(value = "需要批量删除的id列表", required = true, example = "[1, 2, 3]")
    private List<Long> renewBill_ids;
}
