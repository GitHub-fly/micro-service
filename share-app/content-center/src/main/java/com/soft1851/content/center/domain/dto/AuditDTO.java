package com.soft1851.content.center.domain.dto;

import com.soft1851.content.center.domain.AuditStatusEnum;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xunmi
 * @ClassName AuditDTO
 * @Description TODO
 * @Date 2020/10/8
 * @Version 1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("分享审核DTO")
public class AuditDTO {

    private AuditStatusEnum auditStatusEnum;

    private String reason;
}
