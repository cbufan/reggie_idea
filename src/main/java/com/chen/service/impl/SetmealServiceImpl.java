package com.chen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.domain.Setmeal;
import com.chen.domain.SetmealDish;
import com.chen.dto.SetmealDto;
import com.chen.mapper.SetmealMapper;
import com.chen.service.SetmealDishService;
import com.chen.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description:
 * className:SetMealServiceimpl
 * author: chenqifan
 * date:2023/3/416:50
 **/

@Service
@Transactional
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;


    /**
     * @description: 保存套餐信息，并更新setmeal_dish表
     * @author: chenqifan
     * @date: 19:42 2023/3/4
     * @param setmealDto
     * @return boolean
     **/
    @Override
    public boolean saveSetmeal(SetmealDto setmealDto) {
        //保存套餐
        boolean b1 = this.save(setmealDto);
        //获取套餐的id
        Long idSetmeal = setmealDto.getId();
        //每个SetmealDish都设置该套餐的id
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(setmealDish -> {
            setmealDish.setSetmealId(idSetmeal);
            return setmealDish;
        }).collect(Collectors.toList());
        boolean b2 = setmealDishService.saveBatch(setmealDishes);
        return b1&&b2;
    }


    /**
     * @description:分页查询，根据名字模糊分页查询
     * @author: chenqifan
     * @date: 20:01 2023/3/4
     * @param page
     * @param pageSize
     * @param name default = ""
     * @return java.util.List<com.chen.domain.Setmeal>
     **/
    @Override
    public IPage<Setmeal> page(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(!"".equals(name),Setmeal::getName,name);
        Page<Setmeal> setmealInfos = this.page(setmealPage, wrapper);
        return setmealInfos;
    }


    /**
     * @description:批量停售setmeal
     * @author: chenqifan
     * @date: 20:18 2023/3/4
     * @param ids
     * @return boolean
     **/
    @Override
    public boolean closeSetmeal(Long[] ids) {
        UpdateWrapper<Setmeal> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(Setmeal::getStatus,0);
        wrapper.lambda().in(Setmeal::getId,ids);
        boolean b = this.update(wrapper);
        return b;
    }

    /**
     * @description:批量启售setmeal
     * @author: chenqifan
     * @date: 20:41 2023/3/4
     * @param ids
     * @return boolean
     **/
    @Override
    public boolean openSetmeal(Long[] ids) {
        UpdateWrapper<Setmeal> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(Setmeal::getStatus,1);
        wrapper.lambda().in(Setmeal::getId,ids);
        boolean b = this.update(wrapper);
        return b;
    }

    /**
     * @description:通过id批量删除setmeal和setmealdish
     * @author: chenqifan
     * @date: 20:42 2023/3/4
     * @param ids
     * @return boolean
     **/
    @Override
    public boolean deleteSetmealByIds(Long[] ids) {
        boolean b1 = this.removeByIds(Arrays.asList(ids));
        QueryWrapper<SetmealDish> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SetmealDish::getSetmealId,ids);
        boolean b2 = setmealDishService.remove(wrapper);
        return b1&&b2;
    }


    /**
     * @description:通过id获取对应的setmealdto
     * @author: chenqifan
     * @date: 20:49 2023/3/4
     * @param id
     * @return com.chen.dto.SetmealDto
     **/
    @Override
    public SetmealDto getSetmealDtoById(Long id) {
        SetmealDto setmealDto = new SetmealDto();
        Setmeal setmeal = this.getById(id);
        BeanUtils.copyProperties(setmeal,setmealDto);
        QueryWrapper<SetmealDish> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SetmealDish::getSetmealId,String.valueOf(id));
        List<SetmealDish> setmealDishes = setmealDishService.list(wrapper);
        setmealDto.setSetmealDishes(setmealDishes);

        return setmealDto;
    }

    /**
     * @description:更新setmeal和setmealdish
     * @author: chenqifan
     * @date: 21:21 2023/3/4
     * @param setmealDto
     * @return boolean
     **/
    @Override
    public boolean updateSetmeal(SetmealDto setmealDto) {
        boolean b1 = this.updateById(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        /*List<Long> ids = setmealDishes.stream().map(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId());
            return setmealDish.getDishId();
        }).collect(Collectors.toList());
        boolean b2 = true;
        if(!ids.isEmpty()) {
            UpdateWrapper<SetmealDish> wrapper = new UpdateWrapper<>();
            wrapper.lambda().in(SetmealDish::getDishId, ids);
            setmealDishService.remove(wrapper);
        }*/
        //获取setmeal 的id，删除此id对应的菜品信息
        Long id = setmealDto.getId();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(id);
        });
        QueryWrapper<SetmealDish> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SetmealDish::getSetmealId,id);
        boolean b2 = setmealDishService.remove(wrapper);
        boolean b3 = setmealDishService.saveBatch(setmealDishes);
        return b1&&b2&&b3;
    }


}
