package com.soft1851.user.center.domain.dto;

import com.soft1851.user.center.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xunmi
 * @ClassName LoginRestDTO
 * @Description TODO
 * @Date 2020/10/13
 * @Version 1.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRespDTO {

    /**
     * 用户信息
     */
    private UserRespDTO user;

    /**
     * token数据
     */
    private JwtTokenRespDTO token;

    /**
     * 用户今日是否签到
     * 1 -> 已签到
     * 0 -> 未签到
     */
    private Integer isSignIn;
}

