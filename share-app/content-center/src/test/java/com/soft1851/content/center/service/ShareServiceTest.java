package com.soft1851.content.center.service;

import com.github.pagehelper.PageInfo;
import com.soft1851.content.center.domain.AuditStatusEnum;
import com.soft1851.content.center.domain.dto.ShareAuditDTO;
import com.soft1851.content.center.domain.dto.ShareRequestDTO;
import com.soft1851.content.center.domain.entity.Share;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

@SpringBootTest
class ShareServiceTest {

    @Resource
    private ShareService shareService;

    @Test
    void query() {
        PageInfo<Share> query = shareService.query(null, 1, 10, 1);
        List<Share> list = query.getList();
        list.forEach(item -> System.out.println(item.getTitle() + "," + item.getDownloadUrl()));
    }

    @Test
    void findById() {
        System.out.println(shareService.findById(1));
    }

    @Test
    void contribute() {
        ShareRequestDTO share = ShareRequestDTO.builder()
                .author("张三")
                .downloadUrl("http://xxxx")
                .isOriginal(true)
                .price(123)
                .summary("简介")
                .title("title")
                .build();
        System.out.println(shareService.contribute(share));
    }

    @Test
    void editShare() {
        ShareRequestDTO share = ShareRequestDTO.builder()
                .author("李四")
                .downloadUrl("http://xxxx")
                .isOriginal(true)
                .price(123)
                .summary("简介")
                .title("title")
                .build();
        System.out.println(shareService.editShare(share, 14));
    }

    @Test
    void checkShare() {
        ShareAuditDTO shareAuditDTO = ShareAuditDTO.builder()
                .auditStatusEnum(AuditStatusEnum.PASS)
                .reason("优秀！")
                .build();
        System.out.println(shareService.checkShare(shareAuditDTO, 14));
    }

    @Test
    void testFindById() {
        System.out.println(shareService.findById(1));
    }

    @Test
    void myExchange() {
        PageInfo<Share> sharePageInfo = shareService.myExchange(1, 10, 23);
        sharePageInfo.getList().forEach(System.out::println);
    }
}