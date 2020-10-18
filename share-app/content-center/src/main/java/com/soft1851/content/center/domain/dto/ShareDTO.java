package com.soft1851.content.center.domain.dto;

import com.soft1851.content.center.domain.entity.Share;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xunmi
 * @ClassName ShareDTO
 * @Description TODO
 * @Date 2020/9/29
 * @Version 1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("分享详情，带发布人昵称")
public class ShareDTO {

    @ApiModelProperty(name = "share", value = "分享信息")
    private Share share;

    /**
     * 发布人
     */
    @ApiModelProperty(name = "wxNickname", value = "发布人昵称")
    private String wxNickname;
}