package com.chen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chen.common.BaseContext;
import com.chen.common.Result;
import com.chen.domain.ShoppingCart;
import com.chen.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description:
 * className:ShoppingCartController
 * author: chenqifan
 * date:2023/3/618:17
 **/

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("购物车{}", shoppingCart);
        //设置当前用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //查询当前用户加购菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, currentId);
        if (dishId != null){
            //添加购物车的是菜品
            wrapper.eq(ShoppingCart::getDishId,dishId);

        }else {
            wrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }
        List<ShoppingCart> cartlist = shoppingCartService.list(wrapper);
        ShoppingCart sc = new ShoppingCart();
        if(!cartlist.isEmpty()){
            if(shoppingCart.getDishFlavor() != null){
                for(ShoppingCart cart:cartlist){
                    if(shoppingCart.getDishFlavor().equals(cart.getDishFlavor())){
                        cart.setNumber(cart.getNumber() + 1);
                        shoppingCartService.updateById(cart);
                        sc = cart;
                        break;
                    }
                };
                if(sc.getId() == null){
                    shoppingCartService.save(shoppingCart);
                    sc = shoppingCart;
                }
            }else{
                for(ShoppingCart cart:cartlist){
                    if(cart.getDishFlavor() == null){
                        cart.setNumber(cart.getNumber() + 1);
                        shoppingCartService.updateById(cart);
                        sc = cart;
                        break;
                    }
                };
                if(sc.getId() == null){
                    shoppingCartService.save(shoppingCart);
                    sc = shoppingCart;
                }
            }
        }else{
            shoppingCartService.save(shoppingCart);
            sc = shoppingCart;
        }

        return Result.success(sc);
    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> list = shoppingCartService.list(wrapper);
        return Result.success(list);
    }

    @DeleteMapping ("clean")
    public Result<String> clean(){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        boolean b = shoppingCartService.remove(wrapper);
        return b?Result.success("操作成功"):Result.error("操作失败");
    }

    @PostMapping("/sub")
    public Result<String> sub(@RequestBody ShoppingCart shoppingCart ){
        System.out.println(shoppingCart);
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();

        if(shoppingCart.getDishId() != null){
            wrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
            if(shoppingCart.getDishFlavor() != null){
                wrapper.eq(ShoppingCart::getDishFlavor,shoppingCart.getDishFlavor());
                ShoppingCart one = shoppingCartService.getOne(wrapper);
                if(one.getNumber()>1){
                    one.setNumber(one.getNumber()-1);
                    shoppingCartService.updateById(one);
                }else{
                    shoppingCartService.removeById(one);
                }
            }else {
                wrapper.eq(ShoppingCart::getDishFlavor,"");
                ShoppingCart one = shoppingCartService.getOne(wrapper);
                if(one.getNumber()>1){
                    one.setNumber(one.getNumber()-1);
                    shoppingCartService.updateById(one);
                }else{
                    shoppingCartService.removeById(one);
                }
            }


        }else{
            wrapper.eq(shoppingCart.getSetmealId() != null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
            ShoppingCart cart = shoppingCartService.getOne(wrapper);
            if(cart.getNumber()>1){
                cart.setNumber(cart.getNumber() - 1);
                shoppingCartService.updateById(cart);
            }else{
                shoppingCartService.removeById(cart);
            }
        }

        return Result.success("操作成功");
    }
}
