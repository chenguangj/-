package com.example.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/chen" , clazz -> clazz.isAnnotationPresent(RestController.class));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/chen/**")
                .excludePathPatterns("/chen/login")
                .excludePathPatterns("/chen/register")
                .excludePathPatterns("/chen/type")
                .excludePathPatterns("/chen/files/**")
                .excludePathPatterns("/chen/adminInfo/export/**")
                .excludePathPatterns("/chen/adminInfo/upload/**")
                .excludePathPatterns("/chen/studentInfo/export/**")
                .excludePathPatterns("/chen/studentInfo/upload/**")
                .excludePathPatterns("/chen/cource/tuidingTe/**")
                .excludePathPatterns("/chen/captcha/**");
    }
}
