package com.soft1851.user.center.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xunmi
 * @ClassName UserAddBonusDTO
 * @Description TODO
 * @Date 2020/10/15
 * @Version 1.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户增加积分DTO")
public class UserAddBonusDTO {

    private Integer userId;

    /**
     * 积分
     */
    private Integer bonus;
}
