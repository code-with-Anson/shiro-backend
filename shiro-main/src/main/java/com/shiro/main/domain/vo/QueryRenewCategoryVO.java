package com.shiro.main.domain.vo;

import com.shiro.main.domain.po.RenewCategory;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 查找用户拥有的常规账单分类
 */
@Data
@ApiModel(value = "按照用户id查找分类-视觉传输实体")
public class QueryRenewCategoryVO implements Serializable {

    private Long id;
    private String name;

    public static QueryRenewCategoryVO toVO(RenewCategory renewCategory) {
        QueryRenewCategoryVO vo = new QueryRenewCategoryVO();
        vo.setId(renewCategory.getId());
        vo.setName(renewCategory.getName());
        return vo;
    }
}
