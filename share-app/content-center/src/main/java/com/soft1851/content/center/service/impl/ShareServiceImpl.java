package com.soft1851.content.center.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.soft1851.content.center.domain.AuditStatusEnum;
import com.soft1851.content.center.domain.dto.*;
import com.soft1851.content.center.domain.entity.Share;
import com.soft1851.content.center.feignclient.UserCenterFeignClient;
import com.soft1851.content.center.mapper.MidUserShareMapper;
import com.soft1851.content.center.mapper.ShareMapper;
import com.soft1851.content.center.service.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xunmi
 * @ClassName ShareService
 * @Description TODO
 * @Date 2020/9/29
 * @Version 1.0
 **/

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareServiceImpl implements ShareService {

    private final ShareMapper shareMapper;
    private final MidUserShareMapper midUserShareMapper;
    private final UserCenterFeignClient userCenterFeignClient;
    private final RocketMQTemplate rocketMQTemplate;


    @Override
    public List<Share> getAllShare() {
        return shareMapper.selectAll();
    }

    @Override
    public ShareDTO findById(Integer id) {
        // 获取分享实体
        Share share = this.shareMapper.selectByPrimaryKey(id);
        // 获得发布人id
        Integer userId = share.getUserId();

        // 1. 代码不可读
        // 2. 复杂的url难以维护：https://user-center/s?ie={ie}&f={f}&rsv_bp=1&rsv_idx=1&tn=baidu&wd=a&rsv_pq=c86459bd002cfbaa&rsv_t=edb19hb%2BvO%2BTySu8dtmbl%2F9dCK%2FIgdyUX%2BxuFYuE0G08aHH5FkeP3n3BXxw&rqlang=cn&rsv_enter=1&rsv_sug3=1&rsv_sug2=0&inputT=611&rsv_sug4=611
        // 3. 难以相应需求的变化，变化很没有幸福感
        // 4. 编程体验不统一
        UserDTO userDTO = this.userCenterFeignClient.findUserById(userId);

        ShareDTO shareDTO = new ShareDTO();
        // 属性的装配
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());
        return shareDTO;
    }

    @Override
    public String getHello() {
        return this.userCenterFeignClient.getHello();
    }

    @Override
    public PageInfo<Share> query(String title, Integer pageNo, Integer pageSize, Integer userId) {
        // 启动分页
        PageHelper.startPage(pageNo, pageSize);
        // 构造查询实例
        Example example = new Example(Share.class);
        Example.Criteria criteria = example.createCriteria();
        // 如标题关键字不空，则加上模糊查询条件，否则结果即所有数据
        if (StringUtil.isNotEmpty(title)) {
            criteria.andLike("title", "%" + title + "%");
        }
        // 执行案条件查询
        List<Share> shares = shareMapper.selectByExample(example);
        // 处理后的 Share 数据列表
        List<Share> shareDeal;
        // 1. 如果用户未登录，那么 downloadURL 全部设为 null
        if (userId == null) {
            shareDeal = shares.stream()
                    .peek(share -> {
                        share.setDownloadUrl(null);
                    })
                    .collect(Collectors.toList());
        }
        // 2. 如果用户登录了，那么查询一下 mid_user_share，如果没有数据，那么这条 share 的download 也设为 null
        // 只有自己分享的资源才能直接看到下载链接， 否则显示“兑换”
        else {
            shareDeal = shares.stream()
                    .peek(share -> {
                        MidUserShare midUserShare = midUserShareMapper.selectOne(
                                MidUserShare.builder()
                                        .userId(userId)
                                        .shareId(share.getId())
                                        .build()
                        );
                        if (midUserShare == null) {
                            share.setDownloadUrl(null);
                        }
                    })
                    .collect(Collectors.toList());
        }
        return new PageInfo<>(shareDeal);
    }

    @Override
    public Share contribute(ShareRequestDTO shareRequestDTO) {
        Share share = new Share();
        BeanUtils.copyProperties(shareRequestDTO, share);
        share.setUserId(1);
        share.setCreateTime(LocalDateTime.now());
        share.setUpdateTime(LocalDateTime.now());
        share.setCover("");
        share.setBuyCount(0);
        share.setShowFlag(false);
        share.setAuditStatus("");
        share.setReason("");
        shareMapper.insert(share);
        return share;
    }

    @Override
    public Share editShare(ShareRequestDTO shareRequestDTO, Integer shareId) {
        Share share = shareMapper.selectByPrimaryKey(shareId);
        BeanUtils.copyProperties(shareRequestDTO, share);
        shareMapper.updateByPrimaryKeySelective(share);
        return share;
    }

    @Override
    public AuditDTO checkShare(AuditDTO auditDTO, Integer shareId) {
        Share share = shareMapper.selectByPrimaryKey(shareId);
        share.setAuditStatus(auditDTO.getAuditStatusEnum().toString());
        share.setReason(auditDTO.getReason());
        shareMapper.updateByPrimaryKeySelective(share);
        // 添加是否增加积分的判断
        userCenterFeignClient.handleBonus(UserAddBonusMsgDto.builder()
                .userId(share.getUserId())
                .bonus(50)
                .build());
        return auditDTO;
    }

    @Override
    public AuditDTO checkShareRocketMQ(AuditDTO auditDTO, Integer shareId) {
        // 1、更改审核状态
        Share share = shareMapper.selectByPrimaryKey(shareId);
        share.setAuditStatus(auditDTO.getAuditStatusEnum().toString());
        share.setReason(auditDTO.getReason());
        shareMapper.updateByPrimaryKeySelective(share);
        // 2、判断是否需要给用户添加积分
        if (AuditStatusEnum.PASS.equals(auditDTO.getAuditStatusEnum())) {
            rocketMQTemplate.convertAndSend(
                    "add-bonus",
                    UserAddBonusMsgDto.builder()
                            .userId(share.getUserId())
                            .bonus(50)
                            .build()
            );
        }
        return auditDTO;
    }
}