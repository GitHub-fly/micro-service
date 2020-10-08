package com.soft1851.content.center.feignclient;

import com.soft1851.content.center.configuration.UserFeignConfiguration;
import com.soft1851.content.center.domain.dto.UserAddBonusMsgDto;
import com.soft1851.content.center.domain.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author xunmi
 * @ClassName UserCenterFeignClient
 * @Description TODO
 * @Date 2020/9/29
 * @Version 1.0
 **/
@FeignClient(name = "user-center")
public interface UserCenterFeignClient {
    /**
     * http://user-center/users/{id}
     *
     * @param id
     * @return UserDTO
     */
    @GetMapping("/users/{id}")
    UserDTO findUserById(@PathVariable Integer id);

    /**
     * hello测试
     *
     * @return String
     */
    @GetMapping("/users/hello")
    String getHello();

    /**
     * 用户增加积分方法
     *
     * @param userAddBonusMsgDto
     */
    @PostMapping("/users/bonus")
    void handleBonus(@RequestBody UserAddBonusMsgDto userAddBonusMsgDto);

}