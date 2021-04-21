package com.example.dynamic.schema.config;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Created on 2021-04-21 15:30
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Builder
@EqualsAndHashCode
public class DataSourceInfo {
    private final String name;
    private final String driver;
    private final String url;
    private final String username;
    private final String password;
}
