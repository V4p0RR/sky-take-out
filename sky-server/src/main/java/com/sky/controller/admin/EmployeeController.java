package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee") // 可指定基址
@Slf4j
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
    // 返回带有EmployeeLoginVO类型参数的Result类！
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        // 登录成功后，生成jwt令牌
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
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     * 
     * @param employeeLoginDTO
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping
    public Result postEmp(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工:{}", employeeDTO);
        employeeService.postEmp(employeeDTO);
        return Result.success("新增成功");
    }

    /**
     * 根据员工名字分页查询员工
     * 
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> getEmpPageByName(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("根据名字:{}分页查询员工", employeePageQueryDTO.getName());
        PageResult pageResult = employeeService.getEmpPageByName(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用/禁用员工
     * 
     * @param status
     * @param id
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/status/{status}")
    public Result useOrBanEmp(@PathVariable Integer status, Long id) {
        log.info("要设置的状态:{},员工id:{}", status, id);
        employeeService.useOrBanEmp(status, id);
        return Result.success();
    }

    /**
     * 根据id查找员工
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> getEmpById(@PathVariable Long id) {
        log.info("查找id为{}的员工", id);
        Employee employee = employeeService.getEmpById(id);
        return Result.success(employee);
    }

    @SuppressWarnings("rawtypes")
    @PutMapping
    public Result updateEmpById(@RequestBody Employee employee) {
        log.info("修改id为{}的员工信息", employee.getId());
        employeeService.updateEmpById(employee);
        return Result.success();
    }
}
