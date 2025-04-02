package com.popcorn.popcorn.config;

import org.hibernate.resource.jdbc.ResourceRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImgConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/popup/**")
                .addResourceLocations("file:/Users/idblab/Desktop/project/popcorn/BE/src/main/resources/static/images/")
                .setCachePeriod(3600); //1시간 동안 캐싱
    }
}
