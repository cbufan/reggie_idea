package com.chen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.domain.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * description:
 * className:DishMapper
 * author: chenqifan
 * date:2023/3/317:53
 **/

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
