package com.soft1851.user.center.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.soft1851.user.center.auth.CheckLogin;
import com.soft1851.user.center.domain.dto.*;
import com.soft1851.user.center.domain.entity.BonusEventLog;
import com.soft1851.user.center.domain.entity.User;
import com.soft1851.user.center.service.UserService;
import com.soft1851.user.center.util.JwtOperator;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final WxMaService wxMaService;

    private final JwtOperator jwtOperator;

    @GetMapping(value = "/{id}")
    public User findUserById(@PathVariable Integer id) {
        log.info("我被请求了...");
        return userService.findById(id);
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

    @PostMapping("/add-bonus")
    public User addBonus(@RequestBody UserAddBonusDTO userAddBonusDTO) {
        log.info("增加积分接口被请求了...");
        Integer userId = userAddBonusDTO.getUserId();
        userService.handleShareBonus(
                UserAddBonusMsgDTO.builder()
                        .userId(userId)
                        .bonus(userAddBonusDTO.getBonus())
                        .description("兑换分享...")
                        .event("BUY")
                        .build()
        );
        return userService.findById(userId);
    }

    @PostMapping(value = "/login")
    public LoginRespDTO login(@RequestBody LoginDTO loginDTO) throws WxErrorException {
        String openId;
        // 微信小程序登录，需要根据 code 请求 openId
        if (loginDTO.getLoginCode() != null) {
            // 微信服务端校验是否已经登录的结果
            WxMaJscode2SessionResult result = wxMaService.getUserService()
                    .getSessionInfo(loginDTO.getLoginCode());
            // 微信的 openId，用户在微信这边的唯一标识
            openId = result.getOpenid();
        } else {
            openId = loginDTO.getOpenId();
        }
        // 看用户是否注册，如果没有注册就（插入），如果已经注册就返回信息
        User user = userService.login(loginDTO, openId);
        // 颁发 token
        Map<String, Object> userInfo = new HashMap<>(3);
        userInfo.put("id", user.getId());
        userInfo.put("wxNickname", user.getWxNickname());
        userInfo.put("role", user.getRoles());
        String token = jwtOperator.generateToken(userInfo);

        log.info(
                "{}登录成功，生成的token = {}，有效期到：{}",
                user.getWxNickname(),
                token,
                jwtOperator.getExpirationTime()
        );
        return LoginRespDTO.builder()
                .user(UserRespDTO.builder()
                        .id(user.getId())
                        .wxNickname(user.getWxNickname())
                        .avatarUrl(user.getAvatarUrl())
                        .bonus(user.getBonus())
                        .roles(user.getRoles())
                        .build())
                .token(JwtTokenRespDTO
                        .builder()
                        .token(token)
                        .expirationTime(jwtOperator.getExpirationTime().getTime())
                        .build())
                .isSignIn(userService.checkSignIn(user.getId()))
                .build();
    }

    @PostMapping("/sign-in")
    public SignInRespDTO signIn(@RequestBody UserAddBonusDTO userAddBonusDTO) {
        return userService.signIn(userAddBonusDTO.getUserId());
    }

    @PutMapping(value = "/update-bonus")
    @ApiOperation(value = "修改用户积分", notes = "修改用户积分")
    public User updateBonus(@RequestBody UserAddBonusDTO userAddBonusDTO) {
        log.info("修改积分接口被请求了...");
        Integer userId = userAddBonusDTO.getUserId();
        userService.updateBonus(
                UserAddBonusMsgDTO.builder()
                        .userId(userId)
                        .bonus(userAddBonusDTO.getBonus())
                        .description("兑换分享")
                        .event("BUY")
                        .build()
        );
        return this.userService.findById(userId);
    }

    @GetMapping(value = "/my-bonus-log")
    @CheckLogin
    @ApiOperation(value = "查询积分明细", notes = "查询积分明细")
    public List<BonusEventLog> getBonusEvents(@RequestHeader(value = "X-Token", required = false) String token) {
        log.info("查询积分明细接口被请求了...");
        int userId = getUserIdFromToken(token);
        return userService.getBonusEventLogs(userId);
    }

    /**
     * @param token :token
     * @return userId: 用户id
     * @description: 封装一个从token中提取userId的方法
     */
    private int getUserIdFromToken(String token) {
        log.info(">>>>>>>>>>>token" + token);
        int userId = 0;
        String noToken = "no-token";
        if (!noToken.equals(token)) {
            Claims claims = this.jwtOperator.getClaimsFromToken(token);
            log.info(claims.toString());
            userId = (Integer) claims.get("id");
        } else {
            log.info("没有token");
        }
        return userId;
    }
}
