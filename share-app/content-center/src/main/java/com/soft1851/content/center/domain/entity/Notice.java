package com.soft1851.content.center.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author xunmi
 * @ClassName Notice
 * @Description TODO
 * @Date 2020/10/4
 * @Version 1.0
 **/
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "notice")
@ApiModel("公告")
public class Notice {

    @Column(name = "id")
    @Id
    @GeneratedValue(generator = "JDBC")
    @ApiModelProperty(name = "id", value = "公告id")
    private Integer id;

    @Column(name = "content")
    @ApiModelProperty(name = "content", value = "公告内容")
    private String content;

    @Column(name = "show_flag")
    @ApiModelProperty(name = "showFlag", value = "是否显示 0：否 1：是")
    private Boolean showFlag;

    /**
     * 东八区
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}

