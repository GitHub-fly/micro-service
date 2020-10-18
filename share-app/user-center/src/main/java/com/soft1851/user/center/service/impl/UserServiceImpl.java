package com.soft1851.user.center.service.impl;

import com.soft1851.user.center.domain.Event;
import com.soft1851.user.center.domain.dto.LoginDTO;
import com.soft1851.user.center.domain.dto.SignInRespDTO;
import com.soft1851.user.center.domain.dto.UserAddBonusMsgDTO;
import com.soft1851.user.center.domain.entity.BonusEventLog;
import com.soft1851.user.center.domain.entity.User;
import com.soft1851.user.center.mapper.BonusEventLogMapper;
import com.soft1851.user.center.mapper.UserMapper;
import com.soft1851.user.center.service.UserService;
import com.soft1851.user.center.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

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
        return userMapper.selectByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void handleShareBonus(UserAddBonusMsgDTO userAddBonusMsgDto) {
        log.info(">>>>>>>>增加积分的基本信息：" + userAddBonusMsgDto);
        // 1. 为用户加积分
        Integer userId = userAddBonusMsgDto.getUserId();
        Integer bonus = userAddBonusMsgDto.getBonus();
        User user = userMapper.selectByPrimaryKey(userId);
        user.setBonus(bonus + user.getBonus());
        userMapper.updateByPrimaryKeySelective(user);
        // 2. 写积分日志
        bonusEventLogMapper.insert(
                BonusEventLog.builder()
                        .userId(userId)
                        .value(bonus)
                        .event(userAddBonusMsgDto.getEvent())
                        .createTime(new Date())
                        .description(userAddBonusMsgDto.getDescription())
                        .build()
        );
        log.info("积分添加完毕...");
    }

    @Override
    public User login(LoginDTO loginDTO, String openId) {
        // 先根据 openId(微信id) 查找用户
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("wxId", openId);
        List<User> users = userMapper.selectByExample(example);
        // 没找到，是新用户，直接注册
        if (users.size() == 0) {
            User saveUser = new User();
            BeanUtils.copyProperties(loginDTO, saveUser);
            saveUser.setWxId(openId);
            saveUser.setRoles("user");
            saveUser.setBonus(100);
            saveUser.setCreateTime(new Date());
            saveUser.setUpdateTime(new Date());
            userMapper.insertSelective(saveUser);
            log.info(">>>>>>>>新注册的用户信息：" + userMapper);
            return saveUser;
        }
        return users.get(0);
    }

    @Override
    public SignInRespDTO signIn(Integer userId) {
        SignInRespDTO signInRespDTO = new SignInRespDTO();
        /* 判断用户今天日否签过到
           若为 1，表示已签到
           若为 2，表示未签到，则进行签到操作
         */
        if (checkSignIn(userId) == 1) {
            signInRespDTO.setMsg("您已签到！");
        } else {
            BonusEventLog bonusEventLog = BonusEventLog.builder()
                    .userId(userId)
                    .event(Event.SIGN_IN.toString())
                    .value(20)
                    .description("签到加积分")
                    .createTime(new Date())
                    .build();
            log.info(">>>>>>>>签到成功：" + bonusEventLog);
            bonusEventLogMapper.insert(bonusEventLog);
            // 加积分
            userMapper.updateByPrimaryKeySelective(User.builder()
                    .id(userId)
                    .bonus(userMapper.selectByPrimaryKey(userId).getBonus() + 20)
                    .build());
            signInRespDTO.setMsg("签到成功！");
            signInRespDTO.setBonusEventLog(bonusEventLog);
        }
        return signInRespDTO;
    }

    @Override
    public Integer checkSignIn(Integer userId) {
        Example example = new Example(BonusEventLog.class);
        example.setOrderByClause("id DESC");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("event", Event.SIGN_IN);
        criteria.andEqualTo("userId", userId);
        List<BonusEventLog> bonusEventLogs = bonusEventLogMapper.selectByExample(example);
        /*
         判断日志表中有没有记录，并且判断两次签到的时间差是否大于 0
         若大于 0 表示签到成功，若小于等于 0 表示在同一天内签到
         */
        if (bonusEventLogs.size() != 0 && DateUtil.between(bonusEventLogs.get(0).getCreateTime(), new Date()) <= 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBonus(UserAddBonusMsgDTO userAddBonusMsgDTO) {
        System.out.println(userAddBonusMsgDTO);
        // 1. 为用户修改积分
        Integer userId = userAddBonusMsgDTO.getUserId();
        Integer bonus = userAddBonusMsgDTO.getBonus();
        User user = this.userMapper.selectByPrimaryKey(userId);
        user.setBonus(user.getBonus() + bonus);
        this.userMapper.updateByPrimaryKeySelective(user);

        // 2. 记录日志到bonus_event_log表里面
        this.bonusEventLogMapper.insert(
                BonusEventLog.builder()
                        .userId(userId)
                        .value(bonus)
                        .event(userAddBonusMsgDTO.getEvent())
                        .createTime(new Date())
                        .description(userAddBonusMsgDTO.getDescription())
                        .build()
        );
        log.info("积分添加完毕...");
    }

    @Override
    public List<BonusEventLog> getBonusEventLogs(int userId) {
        Example example = new Example(BonusEventLog.class);
        example.setOrderByClause("id DESC");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        return bonusEventLogMapper.selectByExample(example);
    }
}