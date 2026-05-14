package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 包名称： com.sky.controller.admin
 * 类名称：EmployeeController
 * 类描述：员工管理相关接口
 * 创建人：@author Rain_润
 * 创建时间：2026-05-14 15:52
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工管理相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody @Validated EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }
    /**
     * 新增员工
     */
    @PostMapping("")
    @ApiOperation("新增员工")
    public Result<EmployeeLoginVO> save(@RequestBody @Validated EmployeeDTO employeeDTO) {
        log.info("新增员工：{}", employeeDTO);
        employeeService.save(employeeDTO);
        return  Result.success();
    }
    /**
     * 分页查询
     */
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(@Validated EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询：{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return  Result.success(pageResult);
    }
    /**
     * 启用禁用员工账号
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id) {
        log.info("启用禁用员工账号：id={}, status={}", id, status);
        employeeService.startOrStop(id, status);
        return  Result.success();
    }

    /**
     * 修改员工密码
     * @param passwordEditDTO
     * @return
     */
    @PutMapping("editPassword")
    @ApiOperation("修改员工密码")
    public Result<String> editPassword(@RequestBody @Validated PasswordEditDTO passwordEditDTO) {
        log.info("修改员工密码：{}", passwordEditDTO);
        employeeService.editPassword(passwordEditDTO);
        return  Result.success();

    }
    /**
     * 根据id查询员工信息
     * @param id
      * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工信息")
    public Result<Employee> getById(@PathVariable("id") Long id) {
        log.info("根据id查询员工信息：id={}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }
    /**
     * 编辑员工信息
     */
    @PutMapping("")
    @ApiOperation("编辑员工信息")
    public Result<String> edit(@RequestBody @Validated EmployeeDTO employeeDTO) {
        log.info("编辑员工信息：{}", employeeDTO);
        employeeService.edit(employeeDTO);
        return  Result.success();
    }
}
