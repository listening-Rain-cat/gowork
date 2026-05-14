package com.sky.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class EmployeePageQueryDTO implements Serializable {

    //员工姓名
    private String name;
    @NotNull(message = "页码不能为空")
    //页码
    private int page;
    @NotNull(message = "每页显示记录数不能为空")
    //每页显示记录数
    private int pageSize;

}
