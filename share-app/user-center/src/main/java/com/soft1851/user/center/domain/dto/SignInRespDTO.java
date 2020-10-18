package com.soft1851.user.center.domain.dto;

import com.soft1851.user.center.domain.entity.BonusEventLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xunmi
 * @ClassName SignInRespDTO
 * @Description TODO
 * @Date 2020/10/18
 * @Version 1.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInRespDTO {

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 返回值
     */
    private BonusEventLog bonusEventLog;
}
