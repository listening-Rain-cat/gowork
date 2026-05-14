package com.sky.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class PasswordEditDTO implements Serializable {
    @NotNull(message = "员工id不能为空")
    //员工id
    private Long empId;
    @NotNull(message = "旧密码不能为空")
    //旧密码
    private String oldPassword;
    @NotNull(message = "新密码不能为空")
    //新密码
    private String newPassword;

}
