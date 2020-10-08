package com.soft1851.content.center.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

/**
 * @author xunmi
 * @ClassName UserAddBonusMsgDto
 * @Description TODO
 * @Date 2020/10/8
 * @Version 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户积分DTO")
public class UserAddBonusMsgDto {

    @ApiModelProperty(name = "userId", value = "用户id")
    private Integer userId;

    @ApiModelProperty(name = "bonus", value = "积分")
    private Integer bonus;
}
