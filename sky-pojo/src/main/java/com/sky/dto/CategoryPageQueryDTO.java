package com.sky.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CategoryPageQueryDTO implements Serializable {

    //页码
    @NotNull(message = "页码不能为空")
    private int page;

    //每页记录数
    @NotNull(message = "每页记录数不能为空")
    private int pageSize;

    //分类名称
    private String name;

    //分类类型 1菜品分类  2套餐分类
    private Integer type;

}
