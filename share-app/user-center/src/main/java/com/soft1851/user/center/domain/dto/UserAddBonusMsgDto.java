package com.soft1851.user.center.domain.dto;

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
@Table(name = "用户增加积分DTO")
public class UserAddBonusMsgDto {

    private Integer userId;

    private Integer bonus;
}
