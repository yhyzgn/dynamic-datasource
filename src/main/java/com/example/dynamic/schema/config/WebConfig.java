package com.example.dynamic.schema.config;

import com.example.dynamic.schema.interceptor.DataSourceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created on 2021-04-21 16:27
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private DataSourceInterceptor dataSourceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(dataSourceInterceptor).addPathPatterns("/ds/**");
    }
}
