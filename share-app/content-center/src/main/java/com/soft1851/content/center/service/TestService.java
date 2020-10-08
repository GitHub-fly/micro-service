package com.soft1851.content.center.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xunmi
 * @ClassName TestService
 * @Description TODO
 * @Date 2020/10/6
 * @Version 1.0
 **/
@Service
@Slf4j
public class TestService {

    // 指定 sentinel 的资源名称
    @SentinelResource("common")

    public String commonMethod() {
        log.info("commonMethod...");
        return "common";
    }
}
