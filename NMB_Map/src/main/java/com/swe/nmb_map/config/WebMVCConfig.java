package com.swe.nmb_map.config;

import com.swe.nmb_map.interceptors.LoginProtectInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: spring-headline-part-new
 * @description:
 * @author: Xavier
 * @create: 2025-03-18 21:22
 **/

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LoginProtectInterceptor loginProtectInterceptor;

    /**
     * 添加拦截器配置
     *
     * 该方法用于向应用程序添加拦截器，以在请求处理之前或之后执行特定逻辑
     * 主要用于配置登录保护拦截器，以确保在执行特定请求之前用户已登录
     *
     * @param registry 拦截器注册器，用于添加和配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录保护拦截器，用于确保访问受保护资源前用户已登录
        // 这里配置该拦截器应用于所有收藏以及反馈操作
        registry.addInterceptor(loginProtectInterceptor)
                .addPathPatterns("/favorite/**")
                .addPathPatterns("/feedback/**");
    }
}
