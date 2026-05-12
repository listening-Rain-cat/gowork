package com.sky.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class EmployeeDTO implements Serializable {
    private Long id;
    @NotNull(message = "用户名不能为空")
    private String username;
    @NotNull(message = "姓名不能为空")
    private String name;
    @NotNull(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号必须为11位")
    private String phone;
    @NotNull(message = "性别不能为空")
    private String sex;
    @NotNull(message = "身份证号码不能为空")
    private String idNumber;

}
