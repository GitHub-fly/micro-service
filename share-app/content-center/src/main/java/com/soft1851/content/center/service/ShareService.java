package com.soft1851.content.center.service;

import com.github.pagehelper.PageInfo;
import com.soft1851.content.center.domain.dto.ExchangeDTO;
import com.soft1851.content.center.domain.dto.ShareAuditDTO;
import com.soft1851.content.center.domain.dto.ShareDTO;
import com.soft1851.content.center.domain.dto.ShareRequestDTO;
import com.soft1851.content.center.domain.entity.Share;

import java.util.List;

/**
 * @author xunmi
 * @ClassName ShareService
 * @Description TODO
 * @Date 2020/9/29
 * @Version 1.0
 **/
public interface ShareService {

    /**
     * 获得分享详情
     *
     * @param id
     * @return
     */
    ShareDTO findById(Integer id);

    /**
     * 查询所有的分享信息
     *
     * @return
     */
    List<Share> getAllShare();

    /**
     * 测试方法
     *
     * @return
     */
    String getHello();

    /**
     * 根据标题模糊查询某个用户的分享列表数据，title 为空则为所有数据，查询结果分页
     *
     * @param title
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return
     */
    PageInfo<Share> query(String title, Integer pageNo, Integer pageSize, Integer userId);

    /**
     * 投稿的方法
     *
     * @param shareRequestDTO
     * @return
     */
    Share contribute(ShareRequestDTO shareRequestDTO);

    /**
     * 编辑投稿的方法
     *
     * @param shareRequestDTO
     * @param shareId
     * @return
     */
    Share editShare(ShareRequestDTO shareRequestDTO, Integer shareId);


    /**
     * 管理员审核
     *
     * @param auditDTO
     * @param shareId
     * @return
     */
    Share checkShare(ShareAuditDTO auditDTO, Integer shareId);


    /**
     * 通过 rocketmq 来实现增加积分
     *
     * @param auditDTO
     * @param shareId
     * @return
     */
    ShareAuditDTO checkShareRocketMQ(ShareAuditDTO auditDTO, Integer shareId);

    /**
     * 兑换
     *
     * @param exchangeDTO
     * @return
     */
    Share exchange(ExchangeDTO exchangeDTO);

    /**
     * 我的兑换记录
     *
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return
     */
    PageInfo<Share> myExchange(Integer pageNo, Integer pageSize, Integer userId);

    /**
     * 我的投稿记录
     *
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return
     */
    PageInfo<Share> myContribute(Integer pageNo, Integer pageSize, Integer userId);

    /**
     * 查询未审核分享数据
     *
     * @return
     */
    List<Share> querySharesNotYet();
}
