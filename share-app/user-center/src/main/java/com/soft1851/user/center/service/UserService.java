package com.soft1851.user.center.service;

import com.soft1851.user.center.domain.dto.LoginDTO;
import com.soft1851.user.center.domain.dto.SignInRespDTO;
import com.soft1851.user.center.domain.dto.UserAddBonusMsgDTO;
import com.soft1851.user.center.domain.entity.BonusEventLog;
import com.soft1851.user.center.domain.entity.User;

import java.util.List;

/**
 * @author xunmi
 * @ClassName UserService
 * @Description TODO
 * @Date 2020/9/29
 * @Version 1.0
 **/
public interface UserService {
    /**
     * 根据id获得用户详情
     *
     * @param id
     * @return User
     */
    User findById(Integer id);


    /**
     * 为用户增加投稿积分
     *
     * @param userAddBonusMsgDto
     */
    void handleShareBonus(UserAddBonusMsgDTO userAddBonusMsgDto);

    /**
     * 用户登录的方法
     *
     * @param loginDTO
     * @param openId
     * @return
     */
    User login(LoginDTO loginDTO, String openId);

    /**
     * 用户签到
     *
     * @param userId
     * @return
     */
    SignInRespDTO signIn(Integer userId);

    /**
     * 检查用户今日是否签到
     *
     * @param userId
     * @return 返回 1 -> 已签到
     * 返回 0 -> 未签到
     */
    Integer checkSignIn(Integer userId);

    /**
     * 更新积分
     *
     * @param userAddBonusMsgDTO
     */
    void updateBonus(UserAddBonusMsgDTO userAddBonusMsgDTO);

    /**
     * 获取日志
     *
     * @param userId
     * @return
     */
    List<BonusEventLog> getBonusEventLogs(int userId);
}
