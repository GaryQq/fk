package com.fengkuang.author.config;

import com.fengkuang.author.interceptor.AccessInterceptor;
import com.fengkuang.author.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author zhaichong
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Resource
    private AccessInterceptor accessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptor());
        registration.addPathPatterns("/**");
        registration.excludePathPatterns("/", "/author/login", "/error", "/static/**", "/author/logout",
                "/author/toApply", "/*.html", "/js/**", "/article/uploadImage", "/author/authCode", "/author/apply");*/


        registry.addInterceptor(accessInterceptor).addPathPatterns("/**");

        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns("/", "/author/login", "/error", "/static/**", "/author/logout",
                "/author/toApply", "/*.html", "/js/**", "/article/uploadImage", "/author/authCode", "/author/apply");

    }
}
