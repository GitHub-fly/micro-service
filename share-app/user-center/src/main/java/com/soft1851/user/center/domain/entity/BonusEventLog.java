package com.soft1851.user.center.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author xunmi
 * @ClassName BonusEventLog
 * @Description TODO
 * @Date 2020/10/8
 * @Version 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bonus_event_log")
public class BonusEventLog {

    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    /**
     * 积分操作值
     */
    @Column(name = "value")
    private Integer value;

    @Column(name = "event")
    private String event;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "description")
    private String description;

}
