package com.example.dynamic.schema.controller;

import com.example.dynamic.schema.helper.DynamicHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2021-04-21 11:40
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/dynamic")
public class DynamicController {
    @Autowired
    private DynamicHelper helper;

    @GetMapping
    public Object dynamic(String dbName) {
        if (helper.createDatabase(dbName) <= 0) {
            return "数据库创建失败啦";
        }
        helper.updateSchema(dbName);
        return "成功啦";
    }
}
