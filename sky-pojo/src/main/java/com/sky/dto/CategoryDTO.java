package com.sky.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CategoryDTO implements Serializable {

    //主键
//    @NotNull(message = "id不能为空")
    private Long id;

    //类型 1 菜品分类 2 套餐分类
    @NotNull(message = "type不能为空")
    private Integer type;

    //分类名称
    @NotNull(message = "name不能为空")
    private String name;

    //排序
    @NotNull(message = "sort不能为空")
    private Integer sort;

}
