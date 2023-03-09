package com.chen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.domain.Category;
import com.chen.mapper.CategoryMapper;
import com.chen.mapper.EmployeeMapper;
import com.chen.service.CategoryService;
import org.springframework.stereotype.Service;

/**
 * description:
 * className:CategoryServiceImpl
 * author: chenqifan
 * date:2023/3/317:26
 **/

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
