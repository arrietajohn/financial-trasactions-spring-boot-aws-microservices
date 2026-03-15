package com.johncarrieta2.accountservice.infrastructure.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.pattern.PathPatternParser;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setPatternParser(new PathPatternParser());
    }
}
