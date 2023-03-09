package com.chen.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * description:
 * className:LoginCheckInterceptor
 * author: chenqifan
 * date:2023/3/215:19
 **/

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截到的请求:{}",request.getRequestURI());
        Object employee = request.getSession().getAttribute("employee");
        if(employee == null){
            return false;
        }
        return true;
    }
}
