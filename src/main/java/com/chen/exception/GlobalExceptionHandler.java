package com.chen.exception;

import com.chen.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * description: 全局异常处理
 * className:GlobalExceptionHandler
 * author: chenqifan
 * date:2023/3/217:52
 **/

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * @description:异常处理
     * @author: chenqifan
     * @date: 17:55 2023/3/2
     * @return com.chen.common.Result<java.lang.String>
     **/

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> duplicateEntryExceptionHandler(SQLIntegrityConstraintViolationException exception){
        log.error(exception.getMessage());
        if(exception.getMessage().contains("Duplicate entry")){
            String[] split = exception.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return Result.error(msg);
        }
        return Result.error("未知错误");
    }

    @ExceptionHandler(NullPointerException.class)
    public Result<String> sessionTimeoutException(NullPointerException exception){
        log.error(exception.getMessage());
        if(exception.getMessage().contains("javax.servlet.http.HttpSession.getAttribute(")&&exception.getMessage().contains("is null"))
            return Result.error("session超时，登录退出，请重新登录");
        return Result.error("未知错误");
    }
}
