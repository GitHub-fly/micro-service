package com.soft1851.mysqlclient.controller;

import com.soft1851.mysqlclient.entity.Like;
import com.soft1851.mysqlclient.mapper.LikeMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xunmi
 * @ClassName MysqlController
 * @Description TODO
 * @Date 2020/9/10
 * @Version 1.0
 **/
@RestController
public class MysqlController {
    @Resource
    private LikeMapper likeMapper;

    @GetMapping("/all")
    public List<Like> getAll() {
        return likeMapper.selectList(null);
    }

    @GetMapping("print")
    public String printHello() {
        return "Hello!";
    }
}
