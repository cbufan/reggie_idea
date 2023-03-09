package com.chen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.domain.Setmeal;
import com.chen.domain.SetmealDish;
import com.chen.dto.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    public boolean saveSetmeal(SetmealDto setmealDto);

    public IPage<Setmeal> page(int page, int pageSize, String name);

    public boolean closeSetmeal(Long[] ids);

    public boolean openSetmeal(Long[] ids);

    public boolean deleteSetmealByIds(Long[] ids);

    public SetmealDto getSetmealDtoById(Long ids);

    public boolean updateSetmeal(SetmealDto setmealDto);
}
