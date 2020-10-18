package com.soft1851.user.center.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xunmi
 * @ClassName JwtTokenRespDTO
 * @Description TODO
 * @Date 2020/10/13
 * @Version 1.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenRespDTO {

    private String token;

    /**
     * 过期时间
     */
    private Long expirationTime;
}
