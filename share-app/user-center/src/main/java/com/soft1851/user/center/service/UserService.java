package com.soft1851.user.center.service;

import com.soft1851.user.center.domain.dto.UserAddBonusMsgDto;
import com.soft1851.user.center.domain.entity.User;

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
    void handleShareBonus(UserAddBonusMsgDto userAddBonusMsgDto);

}
