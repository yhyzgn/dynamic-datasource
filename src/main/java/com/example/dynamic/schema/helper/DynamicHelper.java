package com.example.dynamic.schema.helper;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.schema.TargetType;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * JPA根据Entity动态更新表结构
 * <p>
 * <a href="https://www.oodlestechnologies.com/blogs/Create-Database-And-Its-Tables-Manually-Using-Spring-Data-JPA/">Reference</a>
 * <p>
 * Created on 2021-04-21 11:20
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class DynamicHelper {

    public int createDatabase(String dbName) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "root");
            Statement stmt = connection.createStatement();
            return stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateSchema(String dbName) {
        Map<String, Object> settings = new HashMap<>();
        settings.put(Environment.HBM2DDL_AUTO, "update");
        settings.put(Environment.DIALECT, MySQL8Dialect.class);
        settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        settings.put(Environment.URL, "jdbc:mysql://localhost:3306/" + dbName + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai");
        settings.put(Environment.USER, "root");
        settings.put(Environment.PASS, "root");
        settings.put(Environment.SHOW_SQL, true);
        settings.put(Environment.AUTOCOMMIT, true);

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(settings).build();
        try {
            MetadataSources ms = new MetadataSources(registry);
            Set<Class<?>> classes = getClassInPackage("com.example.dynamic.schema.entity");
            classes.forEach(ms::addAnnotatedClass);
            MetadataImplementor implementor = (MetadataImplementor) ms.buildMetadata();
            implementor.validate();
            SchemaUpdate su = new SchemaUpdate();
            su.setFormat(true).setHaltOnError(false).setDelimiter(";");
            su.execute(EnumSet.of(TargetType.DATABASE), implementor, registry);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private Set<Class<?>> getClassInPackage(String packageName) {
        Reflections reflections = new Reflections(packageName, new TypeAnnotationsScanner(), new SubTypesScanner(false));
        return reflections.getTypesAnnotatedWith(Entity.class);
    }
}
