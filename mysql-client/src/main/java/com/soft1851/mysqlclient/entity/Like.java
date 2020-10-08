package com.soft1851.mysqlclient.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xunmi
 * @ClassName Like
 * @Description TODO
 * @Date 2020/9/13
 * @Version 1.0
 **/
@Data
@TableName("t_like")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Like {

    @TableField("id")
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("article_id")
    private Long articleId;

}