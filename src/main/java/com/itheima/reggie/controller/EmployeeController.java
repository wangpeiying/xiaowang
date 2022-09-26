package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.CommonsConst;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * 员工控制类
 *
 * @author tongbing
 * @date 2022/4/21
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService=null;
    /**
     * 登录功能
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R login(HttpServletRequest request, @RequestBody Employee employee){
        //将页面提交的密码进行MD5加密
        String password = employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        //根据用户名查数据库(查不到返回结果)
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //查不到用户名 返回失败
        if (emp==null){
            return R.error("登录失败");
        }
        //比对密码(密码错误返回结果)
        if (!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }
        //查询员工状态，员工状态禁用下不可登录
        if (emp.getStatus()==0){
            return R.error("账户已锁定");
        }
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工注销
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的当前登录员工的id
        HttpSession session = request.getSession();
        session.removeAttribute("employee");
        log.info("用户退出登录.........");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> addEmployee(HttpServletRequest request,@RequestBody Employee employee){
        //设置默认密码 使用md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //调用service
        employeeService.save(employee);
        log.info("新增员工信息{}",employee.toString());
        return R.success("添加成功");
    }

    /**
     *
     * @param page 当前页面
     * @param pageSize 页面大小
     * @param name 需要查询用户的名称
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        //1.构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //2.构造条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Employee::getName,name);
        //3.添加排序 按照账户创建时间排序
        queryWrapper.orderByAsc(Employee::getCreateTime);
        //4.执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id修改用户信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee){
        //if (String(res.code) === '1') 返回json
        //执行修改
        employeeService.updateById(employee);
     return R.success("信息修改成功");
     }

     /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if (employee!=null){
            return R.success(employee);
        }
        return R.error("没有查询到相关信息");
    }



}