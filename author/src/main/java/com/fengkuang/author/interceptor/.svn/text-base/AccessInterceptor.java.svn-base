package com.fengkuang.author.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 跨域拦截器
 *
 * @author zhaichong
 */
@Component
public class AccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("Access-Control-Max-Age", "1800");//30 min*//*
        System.out.println("进入拦截器.............");
        System.out.println(request.getHeader("Origin"));
        System.out.println(request.getMethod());
        System.out.println(request.getHeader("Access-Control-Request-Method") != null);
        System.out.println("OPTIONS".equals(request.getMethod()));*/
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("P3P", "CP=CAO PSA OUR");
        response.addHeader("Access-Control-Allow-Methods", "POST,GET,TRACE,OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type,Origin,Accept");
        response.addHeader("Access-Control-Max-Age", "1800");
//        if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
//        }
/*
        System.out.println("拦截器执行结束。。。。。。。。。。。");
*/
        return true;
    }
}
