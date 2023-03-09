package com.chen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.common.BaseContext;
import com.chen.common.Result;
import com.chen.domain.Employee;
import com.chen.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;



/**
 * description:
 * className:EmployeeController
 * author: chenqifan
 * date:2023/3/212:51
 **/

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * @description: 员工登录处理
     * @author: chenqifan
     * @date: 14:12 2023/3/2
     * @param request
     * @param employee
     * @return java.lang.String
     **/
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        /**
         //1.将页面的密码进行md5加密
         //2.根据用户名查询数据库
         //3.查询不到就返回失败结果
         //4.查询到就进行密码比对
         //5.查看员工状态，如果禁用，则返回员工禁用
         //6.登录成功，就将员工id存入Session并返回登录成功的结果
         **/
        log.info("request:"+request.getRequestURI()+",login...");
        //1
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //wrapper.eq("实体类::查询字段","条件值");
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3
        if(emp == null){
            return Result.error("没有此用户");
        }

        //4
        if(!emp.getPassword().equals(password)){
            return Result.error("密码错误");
        }

        //5
        if(emp.getStatus() == 0){
            return Result.error("账户已禁用");
        }

        //6
        request.getSession().setAttribute("employee",emp.getId());
        emp.setPassword(null);
        return Result.success(emp);
    }


    /**
     * @description: 登出
     * @author: chenqifan
     * @date: 17:38 2023/3/2
     * @param request
     * @return com.chen.common.Result<java.lang.String>
     **/
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        request.removeAttribute("employee");
        return Result.success("退出成功");
    }

    /**
     * @description: 分页查询
     * @author: chenqifan
     * @date: 17:38 2023/3/2
     * @param page 页码
     * @param pageSize 每个页码的数据量
     * @return com.chen.common.Result<java.util.List<com.chen.domain.Employee>>
     **/
    @GetMapping("/page")
    public Result<IPage<Employee>> page(@RequestParam long page,@RequestParam long pageSize,@RequestParam(defaultValue = "") String name){
        Page<Employee> employeePage = new Page<>(page,pageSize);
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.lambda().notIn(Employee::getId,1L);
        wrapper.lambda().like(!"".equals(name),Employee::getName,name);
        IPage<Employee> employeeInfos = employeeService.page(employeePage,wrapper);
        return Result.success(employeeInfos);

    }

    /**
     * @description: 添加员工
     * @author: chenqifan
     * @date: 17:39 2023/3/2
     * @param employee
     * @return com.chen.common.Result<java.lang.String>
     **/
    @PostMapping
    public Result<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息:{}",employee);
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
/*        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());*/

        //获得当前登录用户的id
/*        long empId = (long) request.getSession().getAttribute("employee");

        employee.setCreateUser(empId);

        employee.setUpdateUser(empId);*/

        employeeService.save(employee);

        return Result.success("新增员工成功");
    }


    /**
     * @description: 更新员工
     * @author: chenqifan
     * @date: 15:42 2023/3/3
     * @param request
     * @param employee
     * @return com.chen.common.Result<java.lang.String>
     **/
    @PutMapping
    public Result<String> update(HttpServletRequest request,@RequestBody Employee employee){
        /*employee.setUpdateUser((long) request.getSession().getAttribute("employee"));
        employee.setUpdateTime(LocalDateTime.now());*/
        log.info("EmployeeController.update()...");
        boolean b = employeeService.updateById(employee);
        System.out.println(b);
        return b?(Result.success("操作成功")):(Result.error("操作失败"));
    }

    /**
     * @description: 通过id来查询employee
     * @author: chenqifan
     * @date: 16:34 2023/3/3
     * @param idStr
     * @return com.chen.common.Result<com.chen.domain.Employee>
     **/
    @GetMapping("/{id}")
    public Result<Employee> getOne(@PathVariable("id") String idStr){
        long id = Long.parseLong(idStr);
        Employee emp = employeeService.getById(id);
        if(emp!=null)
        return Result.success(emp);
        return Result.error("查询失败");
    }

}
