package com.chen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.Result;
import com.chen.domain.Dish;
import com.chen.dto.DishDto;

public interface DishService extends IService<Dish> {

    public boolean saveDish(DishDto dishDto);

    public DishDto getOneById(Long id);

    public boolean updateDish(DishDto dishDto);

    public boolean dropDishByIds(Long[] ids);
}
