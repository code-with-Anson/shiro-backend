package com.shiro.backend.domain.vo;

import com.shiro.backend.domain.po.Category;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 查找用户拥有的常规账单分类
 */
@Data
@ApiModel(value = "按照用户id查找分类-视觉传输实体")
public class QueryCategoryVO {

    private Long id;
    private String name;

    public static QueryCategoryVO toVO(Category category) {
        QueryCategoryVO vo = new QueryCategoryVO();
        vo.setId(category.getId());
        vo.setName(category.getName());
        return vo;
    }
}
