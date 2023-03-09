package com.chen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.Result;
import com.chen.domain.Dish;
import com.chen.domain.DishFlavor;
import com.chen.dto.DishDto;
import com.chen.mapper.DishMapper;
import com.chen.service.DishFlavorService;
import com.chen.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * description:
 * className:DishServiceImpl
 * author: chenqifan
 * date:2023/3/317:54
 **/
@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {


    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * @description:保存菜品和口味
     * @author: chenqifan
     * @date: 14:20 2023/3/4
     * @param dishDto
     * @return com.chen.common.Result<java.lang.String>
     **/

    public boolean saveDish(DishDto dishDto){
        //保存菜品
        boolean b1 = this.save(dishDto);
        //获取菜品id
        Long idDish = dishDto.getId();

        boolean b2 = true;
        List<DishFlavor> flavors = dishDto.getFlavors();
        if(!flavors.isEmpty()){
            flavors = flavors.stream().map(flavor -> {
                flavor.setDishId(idDish);
                return flavor;
            }).collect(Collectors.toList());
            //保存菜品口味表
            b2 = dishFlavorService.saveBatch(flavors);
        }

        return b1&b2;
    }

    /**
     * @description:通过id获取dish
     * @author: chenqifan
     * @date: 14:38 2023/3/4
     * @return com.chen.domain.Dish
     **/

    @Override
    public DishDto getOneById(Long id) {
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        //对象拷贝
        BeanUtils.copyProperties(dish,dishDto);

        QueryWrapper<DishFlavor> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(wrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }


    /**
     * @description:修改dish和dishflavor
     * @author: chenqifan
     * @date: 15:05 2023/3/4
     * @param dishDto
     * @return boolean
     **/

    @Override
    public boolean updateDish(DishDto dishDto) {
        /*Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);*/
        boolean b1 = this.updateById(dishDto);
        boolean b3 = true;
        List<DishFlavor> flavors = dishDto.getFlavors();
        /*if(!flavors.isEmpty()){
            QueryWrapper<DishFlavor> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(DishFlavor::getDishId,dishDto.getId());
            if(!dishFlavorService.list(wrapper).isEmpty()){
                b2 = dishFlavorService.remove(wrapper);
            }
            flavors = flavors.stream().map(flavor -> {
                 flavor.setDishId(dishDto.getId());
                 return flavor;
            }).collect(Collectors.toList());
            b3 = dishFlavorService.saveBatch(flavors);
        }*/
        Long dishId = dishDto.getId();
        QueryWrapper<DishFlavor> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DishFlavor::getDishId,dishId);
        dishFlavorService.remove(wrapper);
        //根据dishId去删除表中的记录
        if(!flavors.isEmpty()){
            flavors.forEach(flavor -> {
                flavor.setDishId(dishId);
                flavor.setId(null);
            });
            b3 = dishFlavorService.saveBatch(flavors);
        }
        return b1&&b3;
    }

    @Override
    public boolean dropDishByIds(Long[] ids) {
        UpdateWrapper<Dish> wrapper = new UpdateWrapper<>();
        wrapper.set("is_deleted",0);
        wrapper.lambda().in(Dish::getId,ids);
        boolean b1 = this.update(wrapper);

        QueryWrapper<DishFlavor> wrapper2 = new QueryWrapper<>();
        wrapper2.lambda().select(DishFlavor::getId);
        wrapper2.lambda().in(DishFlavor::getDishId,ids);
        List<DishFlavor> flavors = dishFlavorService.list(wrapper2);
        boolean b2 = true;
        if(!flavors.isEmpty()){
            List<Long> idFlavors = flavors.stream().map(flavor -> flavor.getId()).collect(Collectors.toList());
            b2 = dishFlavorService.removeByIds(idFlavors);
        }
        return b1&&b2;
    }
}
