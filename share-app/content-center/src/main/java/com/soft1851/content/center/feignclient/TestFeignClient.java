package com.soft1851.content.center.feignclient;

import com.soft1851.content.center.domain.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author xunmi
 * @ClassName TestFeignClient
 * @Description TODO
 * @Date 2020/9/30
 * @Version 1.0
 **/
@FeignClient("user-center")
public interface TestFeignClient {

    @GetMapping("/users/q")
    UserDTO getUser(@SpringQueryMap UserDTO userDTO);
}
