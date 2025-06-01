package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * 
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /***
     * 新增员工
     * 
     * @param employee
     */
    void postEmp(Employee employee);

    /***
     * 根据名字查询员工
     * 
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> queryEmpByName(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 根据id修改员工
     * 
     * @param employee
     */
    void updateEmp(Employee employee);

    /**
     * 根据id查找员工
     * 
     * @param id
     * @return
     */
    Employee getEmpById(Long id);
}
