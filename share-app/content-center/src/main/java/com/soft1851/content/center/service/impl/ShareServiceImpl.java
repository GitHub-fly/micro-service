package com.soft1851.content.center.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.purgeteam.dispose.starter.Result;
import com.soft1851.content.center.domain.AuditStatusEnum;
import com.soft1851.content.center.domain.dto.*;
import com.soft1851.content.center.domain.entity.Share;
import com.soft1851.content.center.domain.entity.User;
import com.soft1851.content.center.feignclient.UserCenterFeignClient;
import com.soft1851.content.center.mapper.MidUserShareMapper;
import com.soft1851.content.center.mapper.ShareMapper;
import com.soft1851.content.center.service.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

        Result<User> userResult = userCenterFeignClient.findUserById(userId);
        User user = convert(userResult);
        ShareDTO shareDTO = new ShareDTO();
        shareDTO.setShare(share);
        shareDTO.setWxNickname(user.getWxNickname());
        return shareDTO;
    }

    @Override
    public String getHello() {
        return this.userCenterFeignClient.getHello();
    }

    @Override
    public PageInfo<Share> query(String title, Integer pageNo, Integer pageSize, Integer userId) {
        //启动分页
        PageHelper.startPage(pageNo, pageSize);
        //构造查询实例
        Example example = new Example(Share.class);
        Example.Criteria criteria = example.createCriteria();
        // 如标题关键字不空，则加上模糊查询条件，否则结果即所有数据
        if (StringUtil.isNotEmpty(title)) {
            criteria.andLike("title", "%" + title + "%");
        }
        //执行按条件查询
        List<Share> shares = this.shareMapper.selectByExample(example);
        //处理后的Share数据列表
        List<Share> sharesDeal;
        // 1. 如果用户未登录，那么downloadUrl全部设为null
        if (userId == null) {
            sharesDeal = shares.stream()
                    .peek(share -> share.setDownloadUrl(null))
                    .collect(Collectors.toList());
        }
        // 2. 如果用户登录了，那么查询一下mid_user_share，如果没有数据，那么这条share的downloadUrl也设为null
        //只有自己分享的资源才能直接看到下载链接，否则显示"兑换"
        else {
            sharesDeal = shares.stream()
                    .peek(share -> {
                        MidUserShare midUserShare = this.midUserShareMapper.selectOne(
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
        return new PageInfo<>(sharesDeal);
    }

    @Override
    public Share contribute(ShareRequestDTO shareRequestDTO) {
        Share share = new Share();
        BeanUtils.copyProperties(shareRequestDTO, share);
        share.setCreateTime(new Date());
        share.setUpdateTime(new Date());
        share.setBuyCount(0);
        share.setShowFlag(false);
        share.setAuditStatus(AuditStatusEnum.NOT_YET.name());
        share.setReason("未审核");
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
    public Share checkShare(ShareAuditDTO shareAuditDTO, Integer shareId) {
        // 1. 查询share是否存在，不存在或者当前的audit_status != NOT_YET，那么抛异常
        Share share = shareMapper.selectByPrimaryKey(shareId);
        if (share == null) {
            throw new IllegalArgumentException("参数非法！该分享不存在！");
        }
        if (!Objects.equals(AuditStatusEnum.NOT_YET.name(), share.getAuditStatus())) {
            throw new IllegalArgumentException("参数非法！该分享已审核通过或审核不通过！");
        }
        // 2.审核资源，将状态改为PASS或REJECT，更新原因和是否发布显示
        share.setAuditStatus(shareAuditDTO.getAuditStatusEnum().toString());
        share.setReason(shareAuditDTO.getReason());
        share.setShowFlag(shareAuditDTO.getShowFlag());
        this.shareMapper.updateByPrimaryKey(share);
        //3. 向mid_user插入一条数据，分享的作者通过审核后，默认拥有了下载权限
        this.midUserShareMapper.insert(
                MidUserShare.builder()
                        .userId(share.getUserId())
                        .shareId(shareId)
                        .build()
        );
        // 4. 如果是 PASS，那么发送消息给 rocketmq，让用户中心去消费，并为发布人添加积分
        if (AuditStatusEnum.PASS.equals(shareAuditDTO.getAuditStatusEnum())) {
            this.rocketMQTemplate.convertAndSend(
                    "add-bonus",
                    UserAddBonusMsgDTO.builder()
                            .userId(share.getUserId())
                            .bonus(50)
                            .build());
        }
        return share;
    }

    @Override
    public ShareAuditDTO checkShareRocketMQ(ShareAuditDTO auditDTO, Integer shareId) {
        // 1、更改审核状态
        Share share = shareMapper.selectByPrimaryKey(shareId);
        share.setAuditStatus(auditDTO.getAuditStatusEnum().toString());
        share.setReason(auditDTO.getReason());
        shareMapper.updateByPrimaryKeySelective(share);
        // 2、判断是否需要给用户添加积分
        if (AuditStatusEnum.PASS.equals(auditDTO.getAuditStatusEnum())) {
            rocketMQTemplate.convertAndSend(
                    "add-bonus",
                    UserAddBonusMsgDTO.builder()
                            .userId(share.getUserId())
                            .bonus(50)
                            .build()
            );
        }
        return auditDTO;
    }

    @SneakyThrows
    @Override
    public Share exchange(ExchangeDTO exchangeDTO) {
        int userId = exchangeDTO.getUserId();
        int shareId = exchangeDTO.getShareId();
        // 1. 根据 id 查询 share，校验是否存在
        Optional<Share> shareOptional = Optional.ofNullable(shareMapper.selectByPrimaryKey(shareId));
        if (!shareOptional.isPresent()) {
            throw new IllegalArgumentException("该分享不存在！");
        }
        Share share = shareOptional.get();
        Integer price = share.getPrice();
        // 2. 如果当前用户已经兑换过该分享，则直接返回，相反，增加一条购买次数
        Optional<MidUserShare> midUserShareOptional = Optional.ofNullable(
                midUserShareMapper.selectOne(
                        MidUserShare.builder()
                                .shareId(shareId)
                                .userId(userId)
                                .build()
                )
        );
        if (midUserShareOptional.isPresent()) {
            return share;
        }
        // 增加购买次数
        share.setBuyCount(share.getBuyCount() + 1);
        shareMapper.updateByPrimaryKeySelective(share);
        // 4. 扣积分
        userCenterFeignClient.handleBonus(
                UserAddBonusMsgDTO.builder()
                        .userId(userId)
                        .bonus(price * -1)
                        .build()
        );
        // 5. 向 mid_user_share表中插入一条记录
        midUserShareMapper.insert(
                MidUserShare.builder()
                        .userId(userId)
                        .shareId(shareId)
                        .build()
        );
        return share;
    }

    /**
     * 我的兑换
     *
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return sharesDeal
     */
    @Override
    public PageInfo<Share> myExchange(Integer pageNo, Integer pageSize, Integer userId) {
        Example example = new Example(Share.class);
        example.setOrderByClause("id DESC");
        List<Share> shares = this.shareMapper.selectByExample(example);
        // 处理后的 Share 数据列表
        List<Share> sharesDeal;
        // 过滤出在中间表存在记录的数据
        System.out.println(userId);
        sharesDeal = shares.stream()
                .filter(share -> this.midUserShareMapper.selectOne(
                        MidUserShare.builder()
                                .userId(userId)
                                .shareId(share.getId())
                                .build()) != null
                )
                .collect(Collectors.toList());
        PageHelper.startPage(pageNo, pageSize);
        return new PageInfo<>(sharesDeal);
    }


    /**
     * 我的投稿
     *
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return shares
     */
    @Override
    public PageInfo<Share> myContribute(Integer pageNo, Integer pageSize, Integer userId) {
        PageHelper.startPage(pageNo, pageSize);
        Example example = new Example(Share.class);
        example.setOrderByClause("id DESC");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        List<Share> shares = this.shareMapper.selectByExample(example);
        return new PageInfo<>(shares);
    }


    /**
     * 查询待审核状态的shares列表
     *
     * @return List<Share>
     */
    @Override
    public List<Share> querySharesNotYet() {
        Example example = new Example(Share.class);
        example.setOrderByClause("id DESC");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("showFlag", false)
                .andEqualTo("auditStatus", "NOT_YET");
        return this.shareMapper.selectByExample(example);
    }

    /**
     * 将统一的返回响应结果转换为 User 类型
     *
     * @param userResult
     * @return
     */
    private User convert(Result<User> userResult) {
        ObjectMapper mapper = new ObjectMapper();
        User user = null;
        try {
            String json = mapper.writeValueAsString(userResult.getData());
            user = mapper.readValue(json, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return user;
    }
}