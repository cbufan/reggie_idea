package com.chen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.domain.Employee;
import com.chen.mapper.EmployeeMapper;
import com.chen.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * description:
 * className:EmployeeServiceImpl
 * author: chenqifan
 * date:2023/3/213:59
 **/

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{
}
