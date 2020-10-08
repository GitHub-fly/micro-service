package com.soft1851.content.center.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author xunmi
 * @ClassName TestGitHubFeignClient
 * @Description TODO
 * @Date 2020/9/30
 * @Version 1.0
 **/
@FeignClient(name = "github", url = "https://github.com/GitHub-fly")
public interface TestGitHubFeignClient {

    @GetMapping("")
    String index();

}
