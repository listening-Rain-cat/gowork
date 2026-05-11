package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
       if(!employee.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
           throw  new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
       }
        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }
    /**
     * 退出登录
     */
    @Override
    public void logout(EmployeeLoginDTO employeeLoginDTO) {

    }
    /**
     * 新增员工
      * @param employeeDTO
      */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        //先判断用户名是否存在
        Employee employee = employeeMapper.getByUsername(employeeDTO.getUsername());
        if(employee == null){
            //用户名已存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_EXISTED);
        }
        //处理加密
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setStatus(StatusConstant.ENABLE);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //设置当前记录的创建人和修改人（暂时写死，后续完善）
        //TODO 解析JWT改为当前用户的ID
        employee.setCreateUser(1L);
        employee.setUpdateUser(1L);
        employeeMapper.save(employee);
    }
}
