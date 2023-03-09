package com.chen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.common.BaseContext;
import com.chen.common.Result;
import com.chen.domain.OrderDetail;
import com.chen.domain.Orders;
import com.chen.service.OrderDetailService;
import com.chen.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Watchable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description:
 * className:OrderController
 * author: chenqifan
 * date:2023/3/913:06
 **/

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;


    /**
     * @description: 用户下单
     * @author: chenqifan
     * @date: 13:43 2023/3/9
     * @param orders
     * @return com.chen.common.Result<java.lang.String>
     **/
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return Result.success("下单成功");
    }

    @GetMapping("/userPage")
    public Result<IPage<OrderDetail>> userPage(@RequestParam int page, @RequestParam int pageSize){
        Page<OrderDetail> pageInfo = new Page<>(page,pageSize,false);

        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        List<Orders> orders = orderService.list(wrapper);
        List<Long> orderIds = orders.stream().map(order-> Long.parseLong(order.getNumber())).collect(Collectors.toList());

        LambdaQueryWrapper<OrderDetail> wrapper2 = new LambdaQueryWrapper<>();
        orderIds.forEach(id ->{
            wrapper2.eq(OrderDetail::getOrderId,id);
            wrapper2.or();
        });
        Page<OrderDetail> orderDetails = orderDetailService.page(pageInfo, wrapper2);
        return Result.success(orderDetails);
    }
}
