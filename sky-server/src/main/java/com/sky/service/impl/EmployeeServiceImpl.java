package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordEditFailedException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;

import com.sky.service.EmployeeService;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        // 1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        // 2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            // 账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 密码比对
        // 进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            // 密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            // 账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 3、返回实体对象
        return employee;
    }

    /***
     * 新增员工
     * 
     * @param
     */
    public void postEmp(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employeeMapper.postEmp(employee);
    }

    /**
     * 根据员工名字分页查找员工
     * 
     * @param
     * @return
     */
    public PageResult getEmpPageByName(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> page = employeeMapper.queryEmpByName(employeePageQueryDTO);
        long total = page.getTotal();
        List<Employee> list = page.getResult();
        return new PageResult(total, list);
    }

    /**
     * 启用/禁用员工
     * 
     * @param
     */
    public void useOrBanEmp(Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build(); // 用employee封装，然后将mapper定义为修改员工，提高复用性
        employeeMapper.updateEmp(employee);
    }

    /**
     * 根据id查询员工
     * 
     * @param
     * @return
     */
    public Employee getEmpById(Long id) {
        return employeeMapper.getEmpById(id);
    }

    /**
     * 根据id修改员工
     * 
     * @param employee
     */
    public void updateEmpById(Employee employee) {
        employeeMapper.updateEmp(employee);
    }

    /**
     * 根据id修改密码
     * 
     * @param passwordEditDTO
     */
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        passwordEditDTO.setEmpId(BaseContext.getCurrentId());
        Employee employee = getEmpById(passwordEditDTO.getEmpId());
        if (DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()).equals(employee.getPassword())) {
            throw new PasswordEditFailedException("修改失败！新密码不能与旧密码相同！");
        }
        String oldPassword = passwordEditDTO.getOldPassword();
        if (DigestUtils.md5DigestAsHex(oldPassword.getBytes()).equals(employee.getPassword())) {
            passwordEditDTO.setNewPassword(DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()));
            Employee employee2 = Employee.builder()
                    .password(passwordEditDTO.getNewPassword())
                    .id(passwordEditDTO.getEmpId())
                    .build();
            employeeMapper.editPassword(employee2);
        } else {
            throw new PasswordEditFailedException("修改失败！原密码错误！");
        }

    }
}
