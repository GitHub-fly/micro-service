package com.soft1851.content.center.service;

import com.soft1851.content.center.domain.entity.Notice;

/**
 * @author xunmi
 * @ClassName NoticeService
 * @Description TODO
 * @Date 2020/10/4
 * @Version 1.0
 **/
public interface NoticeService {


    /**
     * 查询最新公告
     * @return Notice
     */
    Notice getLatest();
}
