package com.example.dynamic.schema.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Created on 2021-04-21 15:32
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
public class DataSourceConfig {
    @Value("${spring.datasource.url}")
    private String defaultUrl;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.username}")
    private String defaultUsername;
    @Value("${spring.datasource.password}")
    private String defaultPassword;

    @Bean
    @Primary
    public DynamicDataSource dynamicDataSource() {
        return new DynamicDataSource(defaultDataSource());
    }

    public static DataSource createDataSource(DataSourceInfo info) {
        return DataSourceBuilder.create()
                .url(info.getUrl())
                .driverClassName(info.getDriver())
                .username(info.getUsername())
                .password(info.getPassword()).build();
    }

    private DataSource defaultDataSource() {
        return DataSourceBuilder.create()
                .url(defaultUrl)
                .driverClassName(driverClassName)
                .username(defaultUsername)
                .password(defaultPassword).build();
    }
}
