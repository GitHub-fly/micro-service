package com.soft1851.content.center.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NoticeServiceTest {

    @Resource
    private NoticeService noticeService;

    @Test
    void getLatest() {
        System.out.println(noticeService.getLatest());
    }
}