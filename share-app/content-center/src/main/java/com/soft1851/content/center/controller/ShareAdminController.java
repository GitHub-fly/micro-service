package com.soft1851.content.center.controller;

import com.soft1851.content.center.auth.CheckAuthorization;
import com.soft1851.content.center.domain.AuditStatusEnum;
import com.soft1851.content.center.domain.dto.ShareAuditDTO;
import com.soft1851.content.center.domain.dto.UserAddBonusMsgDTO;
import com.soft1851.content.center.domain.entity.Share;
import com.soft1851.content.center.mapper.ShareMapper;
import com.soft1851.content.center.service.ShareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.List;

/**
 * @author xunmi
 * @ClassName ShareAdminController
 * @Description TODO
 * @Date 2020/10/18
 * @Version 1.0
 **/
@RestController
@RequestMapping("/admin/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "管理员接口", value = "提供管理员相关的Rest API")
@Slf4j
public class ShareAdminController {

    private final ShareService shareService;
    private final ShareMapper shareMapper;
    private final AsyncRestTemplate asyncRestTemplate;

    @PutMapping(value = "/audit/{id}")
    @CheckAuthorization("admin")
    @ApiOperation(value = "管理员审核分享", notes = "管理员审核分享")
    public Share auditById(@PathVariable Integer id, @RequestBody ShareAuditDTO auditDTO) {
        log.info(id + ">>>>>>>>>>>>>" + auditDTO);
        //此处需要认证授权
        return this.shareService.checkShare(auditDTO, id);
    }

    @GetMapping(value = "/list")
    @CheckAuthorization("admin")
    @ApiOperation(value = "待审核分享列表", notes = "待审核分享列表")
    public List<Share> getSharesNotYet() {
        return shareService.querySharesNotYet();
    }


//  作业题目

    /**
     * Feign 同步
     *
     * @param shareAuditDTO
     * @param id
     * @return
     */
    @PutMapping(value = "/admin/audit/{id}")
    public Share checkShare(@RequestBody ShareAuditDTO shareAuditDTO, @PathVariable Integer id) {
        return shareService.checkShare(shareAuditDTO, id);
    }

    /**
     * 异步
     *
     * @param auditDTO
     * @param id
     * @return
     */
    @PutMapping(value = "/admin/rocketmq/audit/{id}")
    public ShareAuditDTO checkShareRocketMQ(@RequestBody ShareAuditDTO auditDTO, @PathVariable Integer id) {
        return shareService.checkShareRocketMQ(auditDTO, id);
    }

    /**
     * AsyncRestTemplate 异步
     */
    @PutMapping(value = "/admin/ansyncRestTemplate/{id}")
    public ShareAuditDTO checkShareAsyncRestTemplate(@RequestBody ShareAuditDTO auditDTO, @PathVariable Integer id) {
        Share share = shareMapper.selectByPrimaryKey(id);
        share.setAuditStatus(auditDTO.getAuditStatusEnum().toString());
        share.setReason(auditDTO.getReason());
        shareMapper.updateByPrimaryKeySelective(share);
        if (AuditStatusEnum.PASS.equals(auditDTO.getAuditStatusEnum())) {
            String url = "http://localhost:7001/users/bonus";
            //设置Header
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Content-Type", "application/json;charset=UTF-8");
            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
            UserAddBonusMsgDTO userAddBonusMsgDto = UserAddBonusMsgDTO.builder()
                    .userId(share.getUserId())
                    .bonus(50)
                    .build();
            System.out.println(userAddBonusMsgDto);
            //异步发送
            ListenableFuture<ResponseEntity<UserAddBonusMsgDTO>> entity = asyncRestTemplate.postForEntity(url, httpEntity, UserAddBonusMsgDTO.class, userAddBonusMsgDto);
        }
        return auditDTO;
    }
}
