package com.soft1851.user.center.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Resource
    private UserServiceImpl userService;

    @Test
    void signIn() {
        System.out.println(userService.signIn(1));
    }
}