package com.soft1851.content.center.domain.dto;

import com.soft1851.content.center.domain.AuditStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xunmi
 * @ClassName ShareAuditDTO
 * @Description 分享审核数据传输对象
 * @Date 2020/10/18
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("审核分享的数据传输对象")
public class ShareAuditDTO {
    /**
     * 审核状态
     */
    @ApiModelProperty(name = "auditStatusEnum",value = "审核状态")
    private AuditStatusEnum auditStatusEnum;
    /**
     * 原因
     */
    @ApiModelProperty(name = "reason",value = "原因")
    private String reason;

    /**
     * 是否发布显示
     */
    @ApiModelProperty(name = "showFlag",value = "是否发布显示")
    private Boolean showFlag;
}