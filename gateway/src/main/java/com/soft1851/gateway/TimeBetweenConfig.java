package com.soft1851.gateway;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.xml.stream.Location;
import java.time.LocalTime;

/**
 * @author xunmi
 * @ClassName TimeBetweenConfig
 * @Description 定义开始和结束的两个参数
 * @Date 2020/10/9
 * @Version 1.0
 **/
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeBetweenConfig {

    private LocalTime start;

    private LocalTime end;

}
