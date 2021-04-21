package com.example.dynamic.schema.config;

import com.example.dynamic.schema.holder.DataSourceHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 2021-04-21 15:18
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private final static String DEFAULT_DATA_SOURCE_NAME = "com.example.dynamic.schema.config.DynamicDataSource##default-datasource";
    private final Map<Object, Object> dataSourceMap = new ConcurrentHashMap<>();
    private final Map<String, DataSourceInfo> dataSourceInfoMap = new ConcurrentHashMap<>();

    public DynamicDataSource(DataSource defaultDataSource) {
        super.setDefaultTargetDataSource(defaultDataSource);
        super.setTargetDataSources(dataSourceMap);
        dataSourceMap.put(DEFAULT_DATA_SOURCE_NAME, defaultDataSource);
        DataSourceHolder.set(DEFAULT_DATA_SOURCE_NAME);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.get();
    }

    public synchronized boolean add(DataSourceInfo info, boolean override) {
        if (dataSourceMap.containsKey(info.getName()) && info.equals(dataSourceInfoMap.get(info.getName()))) {
            return true;
        } else if (dataSourceMap.containsKey(info.getName()) && !override) {
            return false;
        } else {
            DataSource ds = DataSourceConfig.createDataSource(info);
            dataSourceMap.put(info.getName(), ds);
            dataSourceInfoMap.put(info.getName(), info);
            // must
            super.afterPropertiesSet();
            return true;
        }
    }

    public synchronized boolean addAndSwitchTo(DataSourceInfo info, boolean override) {
        if (add(info, override)) {
            DataSourceHolder.set(info.getName());
        }
        return true;
    }

    public synchronized boolean switchTo(String dsName) {
        if (!dataSourceMap.containsKey(dsName)) {
            return false;
        }
        DataSourceHolder.set(dsName);
        return true;
    }

    public synchronized boolean delete(String dsName) {
        if (!dataSourceMap.containsKey(dsName)) {
            return false;
        }
        dataSourceMap.remove(dsName);
        dataSourceInfoMap.remove(dsName);
        return true;
    }

    public DataSource getDefaultDataSource() {
        return (DataSource) dataSourceMap.get(DEFAULT_DATA_SOURCE_NAME);
    }

    public void switchToDefault() {
        DataSourceHolder.set(DEFAULT_DATA_SOURCE_NAME);
    }

    public void destroy() {
        DataSourceHolder.clear();
    }
}
