package com.soft1851.user.center.util;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author xunmi
 * @ClassName DateUtil
 * @Description 时间处理工具类
 * @Date 2020/10/17
 * @Version 1.0
 **/
public class DateUtil {

    /**
     * 判断两个 Date 类型的时间差
     *
     * @param lastSignIn
     * @param signIn
     * @return 返回一个 Integer 类型的时间差值
     */
    public static Integer between(Date lastSignIn, Date signIn) {
        LocalDateTime last = LocalDateTime.ofInstant(lastSignIn.toInstant(), ZoneId.of("Asia/Shanghai"));
        LocalDateTime now = LocalDateTime.ofInstant(signIn.toInstant(), ZoneId.of("Asia/Shanghai"));
        long dayLong = LocalDateTimeUtil.between(last, now).toDays();
        return Integer.parseInt(String.valueOf(dayLong));
    }
}
