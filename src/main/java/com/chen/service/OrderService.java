package com.chen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.domain.Orders;
import com.chen.exception.CustomException;

public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
