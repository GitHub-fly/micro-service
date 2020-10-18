package com.soft1851.user.center.util;

import com.soft1851.user.center.domain.entity.BonusEventLog;
import com.soft1851.user.center.domain.entity.User;
import com.soft1851.user.center.mapper.BonusEventLogMapper;
import com.soft1851.user.center.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DateUtilTest {

    @Resource
    private BonusEventLogMapper bonusEventLogMapper;

    @Test
    void between() {
        BonusEventLog bonusEventLog = bonusEventLogMapper.selectByPrimaryKey(1);
        System.out.println(DateUtil.between(bonusEventLog.getCreateTime(), new Date()));



    }
}