package com.chen.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * description: 返回前端页面的结果类
 * className:Result
 * author: chenqifan
 * date:2023/3/214:02
 **/

@Data
public class Result<T>{
    private Integer code; //编码：1成功，0或其他数字为失败

    private String msg;  //返回信息，成功信息或失败信息

    private T data;  //返回前端页面的数据

    private Map map = new HashMap(); //动态数据

    public static <T> Result<T> success(T object){
        Result<T> r = new Result<>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> Result<T> error(String msg){
        Result<T> r = new Result<T>();
        r.msg = msg;
        r.code = 0;
        return r;
    }
}
