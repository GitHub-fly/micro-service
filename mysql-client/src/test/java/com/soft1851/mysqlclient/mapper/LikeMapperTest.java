package com.soft1851.mysqlclient.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LikeMapperTest {
    @Resource
    private LikeMapper likeMapper;

    @Test
    public void testSelect() {
        likeMapper.selectList(null).forEach(System.out::println);
    }
}