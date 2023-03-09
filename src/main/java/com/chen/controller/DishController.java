package com.chen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.common.Result;
import com.chen.domain.Dish;
import com.chen.domain.DishFlavor;
import com.chen.dto.DishDto;
import com.chen.service.DishFlavorService;
import com.chen.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * description:
 * className:DishController
 * author: chenqifan
 * date:2023/3/317:55
 **/

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;


    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * @description:分页
     * @author: chenqifan
     * @date: 13:57 2023/3/4
     * @param page
     * @param pageSize
     * @return com.chen.common.Result<com.baomidou.mybatisplus.core.metadata.IPage<com.chen.domain.Dish>>
     **/

    @GetMapping("/page")
    public Result<IPage<Dish>> page(@RequestParam int page,@RequestParam int pageSize,@RequestParam(defaultValue = "") String name){
        Page<Dish> dishPage = new Page<>(page, pageSize);
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted",1);
        wrapper.lambda().like(!"".equals(name),Dish::getName,name);
        Page<Dish> dishInfos = dishService.page(dishPage,wrapper);
        return Result.success(dishInfos);

    }

    /**
     * @description: 批量禁用菜品
     * @author: chenqifan
     * @date: 13:58 2023/3/4
     * @param request
     * @param ids
     * @return com.chen.common.Result<java.lang.String>
     **/

    @PostMapping("/status/0")
    public Result<String> closeBatchDish(HttpServletRequest request, @RequestParam Long[] ids){
        UpdateWrapper<Dish> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(Dish::getStatus,0);
        wrapper.lambda().set(Dish::getUpdateTime, LocalDateTime.now());
        wrapper.lambda().set(Dish::getUpdateUser,request.getSession().getAttribute("employee"));
        wrapper.lambda().in(Dish::getId,ids);
        boolean b = dishService.update(wrapper);

        return b?Result.success("操作成功"):Result.error("操作失败");
    }

    /**
     * @description:批量开启菜品状态
     * @author: chenqifan
     * @date: 13:58 2023/3/4
     * @param request
     * @param ids
     * @return com.chen.common.Result<java.lang.String>
     **/

    @PostMapping("/status/1")
    public Result<String> openBatchDish(HttpServletRequest request,@RequestParam Long[] ids){
        UpdateWrapper<Dish> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(Dish::getStatus,1);
        wrapper.lambda().set(Dish::getUpdateTime, LocalDateTime.now());
        wrapper.lambda().set(Dish::getUpdateUser,request.getSession().getAttribute("employee"));
        wrapper.lambda().in(Dish::getId,ids);
        boolean b = dishService.update(wrapper);

        return b?Result.success("操作成功"):Result.error("操作失败");
    }

    /**
     * @description: 批量逻辑删除菜品
     * @author: chenqifan
     * @date: 13:58 2023/3/4
     * @param ids
     * @return com.chen.common.Result<java.lang.String>
     **/
    @DeleteMapping
    public Result<String> deleteBatchDish(@RequestParam Long[] ids){
        boolean b = dishService.dropDishByIds(ids);
        return b?Result.success("操作成功"):Result.error("操作失败");
    }


    /**
     * @description:新增菜品
     * @author: chenqifan
     * @date: 14:14 2023/3/4
     * @param dishDto
     * @return com.chen.common.Result<java.lang.String>
     **/

    @PostMapping
    public Result<String> saveDish(@RequestBody DishDto dishDto){
        boolean b = dishService.saveDish(dishDto);
        return b?Result.success("操作成功"):Result.error("操作失败");
    }


    /**
     * @description: 根据id查询dish和dishflavor并封装
     * @author: chenqifan
     * @date: 15:01 2023/3/4
     * @param id
     * @return com.chen.common.Result<com.chen.dto.DishDto>
     **/
    @GetMapping("/{id}")
    public Result<DishDto> getOneById(@PathVariable Long id){
        DishDto dishDto = dishService.getOneById(id);
        return Result.success(dishDto);
    }


    /**
     * @description:修改dish和dishflavor
     * @author: chenqifan
     * @date: 15:02 2023/3/4
     * @return com.chen.common.Result<java.lang.String>
     **/
    @PutMapping
    public Result<String> updateDish(@RequestBody DishDto dishDto){
        boolean b = dishService.updateDish(dishDto);
        return b?Result.success("操作成功"):Result.error("操作失败");
    }



    /**
     * @description: 通过分类id来查询菜品集合
     * @author: chenqifan
     * @date: 19:29 2023/3/4
     * @param categoryId
     * @return com.chen.common.Result<java.util.List<com.chen.domain.Dish>>
     **/
    @GetMapping("/list")
/*    public Result<List<Dish>> getDishesByCategoryId(@RequestParam Long categoryId){
        System.out.println(categoryId);
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Dish::getCategoryId,categoryId);
        List<Dish> dishes = dishService.list(wrapper);
        return Result.success(dishes);
    }*/

    public Result<List<DishDto>> getDishesByCategoryId(@RequestParam Long categoryId) {
        System.out.println(categoryId);
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Dish::getCategoryId, categoryId);
        List<Dish> dishes = dishService.list(wrapper);
        List<DishDto> dishDtos = dishes.stream().map(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            return dishDto;
        }).collect(Collectors.toList());

        dishDtos = dishDtos.stream().map(dishDto -> {
            LambdaQueryWrapper<DishFlavor> lambadaWrapper = new LambdaQueryWrapper<>();
            lambadaWrapper.eq(DishFlavor::getDishId, dishDto.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(lambadaWrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());
        return Result.success(dishDtos);

    }

}
