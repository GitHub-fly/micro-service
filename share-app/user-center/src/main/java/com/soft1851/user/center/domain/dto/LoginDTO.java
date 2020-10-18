package com.soft1851.user.center.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xunmi
 * @ClassName LoginDTO
 * @Description TODO
 * @Date 2020/10/13
 * @Version 1.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    /**
     * openId
     */
    private String openId;

    /**
     * loginCode
     */
    private String loginCode;

    /**
     * 微信昵称
     */
    private String wxNickname;

    /**
     * 头像地址
     */
    private String avatarUrl;
}
