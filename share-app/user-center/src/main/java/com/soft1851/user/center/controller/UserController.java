package com.soft1851.user.center.controller;

import com.soft1851.user.center.domain.dto.UserAddBonusMsgDto;
import com.soft1851.user.center.domain.entity.User;
import com.soft1851.user.center.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xunmi
 * @ClassName UserController
 * @Description TODO
 * @Date 2020/9/23
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/{id}")
    public User findUserById(@PathVariable Integer id) {
        log.info("我被请求了...");
        return this.userService.findById(id);
    }

    @GetMapping("/hello")
    public String getData() {
        log.info("我被调用了");
        return "Hello, I'm from user-center";
    }

    @GetMapping("/q")
    public User getUser(User user) {
        return user;
    }

    @PostMapping("/bonus")
    public void addBonus(@RequestBody UserAddBonusMsgDto userAddBonusMsgDto) {
        userService.handleShareBonus(userAddBonusMsgDto);
    }
}
