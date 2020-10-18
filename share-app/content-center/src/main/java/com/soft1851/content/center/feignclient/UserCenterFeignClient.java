package com.soft1851.content.center.feignclient;

import com.purgeteam.dispose.starter.Result;
import com.soft1851.content.center.domain.dto.UserAddBonusMsgDTO;
import com.soft1851.content.center.domain.entity.User;
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
     * @return User
     */
    @GetMapping("/users/{id}")
    Result<User> findUserById(@PathVariable(value = "id") Integer id);

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
     * @return
     */
    @PostMapping("/users/add-bonus")
    User handleBonus(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDto);

}