package com.example.dynamic.schema.interceptor;

import com.example.dynamic.schema.config.DataSourceInfo;
import com.example.dynamic.schema.config.DynamicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 2021-04-21 16:23
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class DataSourceInterceptor implements HandlerInterceptor {
    @Autowired
    private DynamicDataSource dataSource;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String dsName = request.getHeader("DS");
        if (StringUtils.hasText(dsName)) {
            String dbName = "db_" + dsName;
            DataSourceInfo info = DataSourceInfo.builder()
                    .name(dsName)
                    .driver("com.mysql.cj.jdbc.Driver")
                    .url("jdbc:mysql://localhost:3306/" + dbName + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai")
                    .username("root")
                    .password("root")
                    .build();
            dataSource.addAndSwitchTo(info, true);
        } else {
            dataSource.switchToDefault();
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        dataSource.destroy();
    }
}
