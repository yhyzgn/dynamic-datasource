package com.example.dynamic.schema.holder;

/**
 * Created on 2021-04-21 15:21
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class DataSourceHolder {
    private final static ThreadLocal<String> TL = new InheritableThreadLocal<>();

    public static void set(String dsName) {
        TL.set(dsName);
    }

    public static String get() {
        return TL.get();
    }

    public static void clear() {
        TL.remove();
    }
}
