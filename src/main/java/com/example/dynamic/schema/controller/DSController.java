package com.example.dynamic.schema.controller;

import com.example.dynamic.schema.entity.UserEntity;
import com.example.dynamic.schema.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * Created on 2021-04-21 16:28
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/ds")
public class DSController {
    private final static Random RANDOM = new Random();
    @Autowired
    private UserRepository repository;

    @GetMapping
    public Object test(String name) {
        UserEntity user = UserEntity.builder()
                .name(name)
                .age(RANDOM.nextInt(100))
                .build();
        return repository.save(user);
    }
}
