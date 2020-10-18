package com.soft1851.content.center.controller;

import com.soft1851.content.center.auth.CheckLogin;
import com.soft1851.content.center.domain.AuditStatusEnum;
import com.soft1851.content.center.domain.dto.*;
import com.soft1851.content.center.domain.entity.Share;
import com.soft1851.content.center.domain.entity.User;
import com.soft1851.content.center.mapper.ShareMapper;
import com.soft1851.content.center.service.ShareService;
import com.soft1851.content.center.service.impl.ShareServiceImpl;
import com.soft1851.content.center.util.JwtOperator;
import com.soft1851.content.center.util.ResponseResult;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.AsyncRestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Optional;

/**
 * @author xunmi
 * @ClassName ShareController
 * @Description TODO
 * @Date 2020/9/29
 * @Version 1.0
 **/
@Log4j
@RestController
@RequestMapping("/shares")
@Api(tags = "分享接口", value = "提供分享相关的 Rest API")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {

    private final ShareService shareService;
    private final ShareMapper shareMapper;
    private final AsyncRestTemplate asyncRestTemplate;
    private final JwtOperator jwtOperator;
    private final int MAX = 100;


    @GetMapping("/test")
    public String test() {
        return shareService.getHello();
    }

    @PostMapping("/all")
    public ResponseResult getAllData() {
        return ResponseResult.success(shareService.getAllShare());
    }


    @GetMapping(value = "/{id}")
    public ShareDTO findById(@PathVariable Integer id) {
        return shareService.findById(id);
    }

    @GetMapping(value = "/query")
    @ApiOperation(value = "分享列表", notes = "分享列表")
    @ApiIgnore
    public List<Share> query(
            @RequestParam(required = false) String title,
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestHeader(value = "X-Token", required = false) String token) {
        if (pageSize > MAX) {
            pageSize = MAX;
        }
        int userId = getUserIdFromToken(token);
        return shareService.query(title, pageNo, pageSize, userId).getList();
    }

    @PostMapping("/contribute")
    @CheckLogin
    @ApiOperation(value = "投稿", notes = "投稿")
    public Share contributeShare(@RequestBody ShareRequestDTO shareRequestDTO,
                               @RequestHeader(value = "X-Token", required = false) String token) {
        log.info(shareRequestDTO + ">>>>>>>>>>>>");
        int userId = getUserIdFromToken(token);
        shareRequestDTO.setUserId(userId);
        return shareService.contribute(shareRequestDTO);
    }


    @PutMapping("/contribute/{id}")
    public Share editShare(@RequestBody ShareRequestDTO shareRequestDTO, @PathVariable Integer id) {
        return shareService.editShare(shareRequestDTO, id);
    }

    @PostMapping("/exchange")
    @CheckLogin
    public Share exchange(@RequestBody ExchangeDTO exchangeDTO) {
        System.out.println(exchangeDTO + ">>>>>>>>>>>>");
        return shareService.exchange(exchangeDTO);
    }

    @GetMapping("/my-exchange")
    @CheckLogin
    @ApiOperation(value = "我的兑换", notes = "我的兑换")
    public List<Share> myExchange(
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestHeader(value = "X-Token", required = false) String token) {
        if (pageSize > MAX) {
            pageSize = MAX;
        }
        int userId = getUserIdFromToken(token);
        System.out.println("userId:" + userId);
        return this.shareService.myExchange(pageNo, pageSize, userId).getList();
    }

    @GetMapping("/my-contribute")
    @CheckLogin
    @ApiOperation(value = "我的投稿", notes = "我的投稿")
    public List<Share> myContribute(
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestHeader(value = "X-Token", required = false) String token) {
        if (pageSize > MAX) {
            pageSize = MAX;
        }
        int userId = getUserIdFromToken(token);
        return this.shareService.myContribute(pageNo, pageSize, userId).getList();
    }


    /**
     * 封装一个从token中提取userId的方法
     *
     * @param token
     * @return userId
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
