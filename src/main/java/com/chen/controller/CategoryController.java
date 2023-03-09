package com.chen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.common.Result;
import com.chen.domain.Category;
import com.chen.domain.Dish;
import com.chen.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description:
 * className:CategoryController
 * author: chenqifan
 * date:2023/3/317:18
 **/

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public Result<IPage<Category>> page(@RequestParam int page,@RequestParam int pageSize){
        Page<Category> categoryPage = new Page<>(page, pageSize);
        Page<Category> categoryInfos = categoryService.page(categoryPage);
        return Result.success(categoryInfos);
    }

    @PostMapping
    public Result<String> insert(@RequestBody Category category){
        boolean save = categoryService.save(category);
        return save? Result.success("操作成功"):Result.error("操作失败");
    }


    @DeleteMapping
    public Result<String> delete(@RequestParam long ids){
        boolean b = categoryService.removeById(ids);
        return b?Result.success("操作成功"):Result.error("操作失败");
    }

    @PutMapping
    public Result<String> update(@RequestBody Category category){
        long id = category.getId();
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Category::getId,id);
        boolean b = categoryService.update(category, wrapper);
        return b?Result.success("操作成功"):Result.error("操作失败");
    }

    @GetMapping("/list")
    public Result<List<Category>> getDishList(@RequestParam(defaultValue = "-1") int type){
        System.out.println(type);
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(type != -1,Category::getType,type);
        List<Category> categoryInfos = categoryService.list(wrapper);
        return Result.success(categoryInfos);
    }
}
