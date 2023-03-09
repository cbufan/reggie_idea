package com.chen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.domain.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * description:
 * className:CategoryMapper
 * author: chenqifan
 * date:2023/3/317:24
 **/

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
