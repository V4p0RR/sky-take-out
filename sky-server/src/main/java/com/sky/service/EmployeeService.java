package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * 
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * 
     * @param employeeLoginDTO
     */
    void postEmp(EmployeeDTO employeeDTO);

    /***
     * 根据员工名字分页查询员工
     * 
     * @param employeePageQueryDTO
     * @return
     */
    PageResult getEmpPageByName(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用/禁用员工
     * 
     * @param status
     * @param id
     */
    void useOrBanEmp(Integer status, Long id);

    /**
     * 根据id查找员工
     * 
     * @param id
     * @return
     */
    Employee getEmpById(Long id);

    /**
     * 根据id修改员工
     * 
     * @param employee
     */
    void updateEmpById(Employee employee);

    /**
     * 根据id修改密码
     * 
     * @param passwordEditDTO
     */
    void editPassword(PasswordEditDTO passwordEditDTO);

}
