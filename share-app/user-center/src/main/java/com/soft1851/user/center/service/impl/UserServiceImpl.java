package com.soft1851.user.center.service.impl;

import com.soft1851.user.center.domain.dto.UserAddBonusMsgDto;
import com.soft1851.user.center.domain.entity.BonusEventLog;
import com.soft1851.user.center.domain.entity.User;
import com.soft1851.user.center.mapper.BonusEventLogMapper;
import com.soft1851.user.center.mapper.UserMapper;
import com.soft1851.user.center.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author xunmi
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Date 2020/9/29
 * @Version 1.0
 **/
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final BonusEventLogMapper bonusEventLogMapper;

    @Override
    public User findById(Integer id) {
        return this.userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void handleShareBonus(UserAddBonusMsgDto userAddBonusMsgDto) {
        // 1. 为用户加积分
        Integer userId = userAddBonusMsgDto.getUserId();
        Integer bonus = userAddBonusMsgDto.getBonus();
        User user = userMapper.selectByPrimaryKey(userId);
        user.setBonus(bonus + user.getBonus());
        userMapper.updateByPrimaryKeySelective(user);
        // 2. 写积分日志
        bonusEventLogMapper.insert(BonusEventLog.builder()
                .userId(userId)
                .value(userAddBonusMsgDto.getBonus())
                .event("CONTRIBUTE")
                .createTime(new Date())
                .description("投稿加积分")
                .build()
        );
    }

}
