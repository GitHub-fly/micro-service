package com.soft1851.content.center.controller;

import com.soft1851.content.center.domain.AuditStatusEnum;
import com.soft1851.content.center.domain.dto.AuditDTO;
import com.soft1851.content.center.domain.dto.ShareDTO;
import com.soft1851.content.center.domain.dto.ShareRequestDTO;
import com.soft1851.content.center.domain.dto.UserAddBonusMsgDto;
import com.soft1851.content.center.domain.entity.Share;
import com.soft1851.content.center.mapper.ShareMapper;
import com.soft1851.content.center.service.ShareService;
import com.soft1851.content.center.service.impl.ShareServiceImpl;
import com.soft1851.content.center.util.ResponseResult;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.AsyncRestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author xunmi
 * @ClassName ShareController
 * @Description TODO
 * @Date 2020/9/29
 * @Version 1.0
 **/
@RestController
@RequestMapping("/share")
@Api(tags = "分享接口", value = "提供分享相关的 Rest API")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {

    private final ShareService shareService;
    private final ShareMapper shareMapper;
    private final AsyncRestTemplate asyncRestTemplate;

    @PostMapping("/all")
    public ResponseResult getAllData() {
        return ResponseResult.success(shareService.getAllShare());
    }

//    @GetMapping("/{id}")
//    public ResponseResult findById(@PathVariable Integer id) {
//        return ResponseResult.success(shareService.findById(id));
//    }

    @GetMapping(value = "/{id}")
    public ShareDTO findById(@PathVariable Integer id) {
        return shareService.findById(id);
    }

    @GetMapping(value = "/query")
    @ApiIgnore
    public List<Share> query(
            @RequestParam(required = false) String title,
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer userId) throws Exception {
        if (pageSize > 100) {
            pageSize = 100;
        }
        return shareService.query(title, pageNo, pageSize, userId).getList();
    }

    @PostMapping("/contribute")
    public Share contribute(@RequestBody ShareRequestDTO shareRequestDTO) {
        return shareService.contribute(shareRequestDTO);
    }

    @PutMapping("/contribute/{id}")
    public Share editShare(@RequestBody ShareRequestDTO shareRequestDTO, @PathVariable Integer id) {
        return shareService.editShare(shareRequestDTO, id);
    }


    /**
     * Feign 同步
     *
     * @param auditDTO
     * @param id
     * @return
     */
    @PutMapping(value = "/admin/audit/{id}")
    public AuditDTO checkShare(@RequestBody AuditDTO auditDTO, @PathVariable Integer id) {
        return shareService.checkShare(auditDTO, id);
    }

    /**
     * 异步
     *
     * @param auditDTO
     * @param id
     * @return
     */
    @PutMapping(value = "/admin/rocketmq/audit/{id}")
    public AuditDTO checkShareRocketMQ(@RequestBody AuditDTO auditDTO, @PathVariable Integer id) {
        return shareService.checkShareRocketMQ(auditDTO, id);
    }

    /**
     * AsyncRestTemplate 异步
     */
    @PutMapping(value = "/admin/ansyncRestTemplate/{id}")
    public AuditDTO checkShareAsyncRestTemplate(@RequestBody AuditDTO auditDTO, @PathVariable Integer id) {
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
            UserAddBonusMsgDto userAddBonusMsgDto = UserAddBonusMsgDto.builder()
                    .userId(share.getUserId())
                    .bonus(50)
                    .build();
            System.out.println(userAddBonusMsgDto);
            //异步发送
            ListenableFuture<ResponseEntity<UserAddBonusMsgDto>> entity = asyncRestTemplate.postForEntity(url, httpEntity, UserAddBonusMsgDto.class, userAddBonusMsgDto);
        }
        return auditDTO;
    }
}
