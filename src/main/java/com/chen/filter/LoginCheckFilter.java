package com.chen.filter;

import com.alibaba.fastjson.JSON;
import com.chen.common.BaseContext;
import com.chen.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description: 检查登录状态
 * className:LoginCheckFilter
 * author: chenqifan
 * date:2023/3/215:11
 **/

@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Component
public class LoginCheckFilter implements Filter {
    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截到请求：{},{}",request.getMethod(),request.getRequestURI());

       //1.获取本次请求的URI
        String requestURI = request.getServletPath();
        //定义不需要拦截的路径
        String[] passUrls = new String[]{
                "/employee",
                "/employee/page",
           "/employee/login",
           "/employee/logout",
                "/backend/api/**",
                "/backend/images/**",
                "/backend/js/**",
                "/backend/page/login/**",
                "/backend/plugins/**",
                "/backend/styles/**",
                "/backend/favicon.ico",
                "/front/**",
                "/user/**"
        };

        //2.检查请求路径和放行路径是否匹配
        boolean check = check(passUrls, requestURI);

        //3.如果为true,则放行
        if (check){
            log.info("本次请求不需要处理");
            filterChain.doFilter(request,response);
            return;
        }


        //4.判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }


        //5.如果未登录，用响应流的方式向客户端页面响应数据
        log.info("用户未登录，前端跳转到登录页面");
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
    }

    public boolean check(String[] urls,String requestUrl){
        for(String url : urls){
            boolean match = PATH_MATCHER.match(url,requestUrl);
            if(match) return true;
        }
        return false;
    }
}
