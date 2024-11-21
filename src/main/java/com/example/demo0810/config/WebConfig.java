package com.example.demo0810.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/Image/**")
                .addResourceLocations("file:///C:/Image/");
    }
}

/*
    /Image/** 경로로 요청 시, C:/Image 경로에서 파일을 찾음
    저장할 때 "/Image/**" 으로 해 놨기에 이렇게 하는거
*/

