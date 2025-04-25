package com.shiro.main.domain.vo;

import com.shiro.main.domain.po.Category;
import com.shiro.main.enums.CategoryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 查找用户拥有的常规账单分类
 */
@Data
@ApiModel(value = "按照用户id查找分类-视觉传输实体")
public class QueryCategoryVO implements Serializable {

    @ApiModelProperty(value = "按照用户id查找分类-视觉传输实体-分类id")
    private Long id;
    @ApiModelProperty(value = "按照用户id查找分类-视觉传输实体-分类名称")
    private String name;
    @ApiModelProperty(value = "按照用户id查找分类-视觉传输实体-分类类型")
    private CategoryType categoryType;

    public static QueryCategoryVO toVO(Category category) {
        QueryCategoryVO vo = new QueryCategoryVO();
        vo.setId(category.getId());
        vo.setName(category.getName());
        vo.setCategoryType(category.getCategoryType());
        return vo;
    }
}
