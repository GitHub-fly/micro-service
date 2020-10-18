package com.soft1851.user.center.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author xunmi
 * @ClassName WxLoginDTO
 * @Description TODO
 * @Date 2020/10/13
 * @Version 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel("微信登录DTO")
public class WxLoginDTO {

    @ApiModelProperty(name = "code", value = "code")
    private String code;

    @ApiModelProperty(name = "wxNickname", value = "微信昵称")
    private String wxNickname;

    @ApiModelProperty(name = "avatarUrl", value = "头像")
    private String avatarUrl;
}
