package com.chen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chen.common.Result;
import com.chen.domain.Setmeal;
import com.chen.dto.SetmealDto;
import com.chen.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description:
 * className:SetMealController
 * author: chenqifan
 * date:2023/3/416:51
 **/

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;


    /**
     * @description: 保存套餐，并保存套餐与菜品的关系
     * @author: chenqifan
     * @date: 19:57 2023/3/4
     * @param setmealDto
     * @return com.chen.common.Result<java.lang.String>
     **/
    @PostMapping
    public Result<String> saveSetmeal(@RequestBody SetmealDto setmealDto){
        boolean b = setmealService.saveSetmeal(setmealDto);
        return b?Result.success("操作成功"):Result.error("操作失败");
    }


    /**
     * @description:分页查询setmeal，根据名字模糊查询
     * @author: chenqifan
     * @date: 20:10 2023/3/4
     * @param page
     * @param pageSize
     * @param name
     * @return com.chen.common.Result<com.baomidou.mybatisplus.core.metadata.IPage<com.chen.domain.Setmeal>>
     **/

    @GetMapping("/page")
    public Result<IPage<Setmeal>> page(@RequestParam int page,@RequestParam int pageSize,@RequestParam(defaultValue = "") String name){
        IPage<Setmeal> setmealInfos = setmealService.page(page, pageSize, name);
        return Result.success(setmealInfos);
    }


    @PostMapping("/status/0")
    public Result<String> closeSetmeal(@RequestParam Long[] ids){
        boolean b = setmealService.closeSetmeal(ids);
        return b?Result.success("操作成功"):Result.error("操作失败");
    }

    @PostMapping("/status/1")
    public Result<String> openSetmeal(@RequestParam Long[] ids){
        boolean b = setmealService.openSetmeal(ids);
        return b?Result.success("操作成功"):Result.error("操作失败");
    }

    @DeleteMapping
    public Result<String> deleteSetmealByIds(@RequestParam Long[] ids){
        boolean b = setmealService.deleteSetmealByIds(ids);
        return b?Result.success("操作成功"):Result.error("操作失败");
    }

    @GetMapping("/{id}")
    public Result<SetmealDto> getSetmealDtoById(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getSetmealDtoById(id);
        return Result.success(setmealDto);
    }

    @PutMapping
    public Result<String> updateSetemal(@RequestBody SetmealDto setmealDto){
        boolean b = setmealService.updateSetmeal(setmealDto);
        return b?Result.success("操作成功"):Result.error("操作失败");
    }

    @GetMapping("/list")
    public Result<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        wrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(wrapper);
        return Result.success(list);
    }

}
