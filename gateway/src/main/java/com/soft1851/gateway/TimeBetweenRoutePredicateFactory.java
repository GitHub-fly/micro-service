package com.soft1851.gateway;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.cglib.core.Local;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import javax.swing.*;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author xunmi
 * @ClassName TimeBetweenRoutePredicateFactory
 * @Description TODO
 * @Date 2020/10/9
 * @Version 1.0
 **/
@Component
public class TimeBetweenRoutePredicateFactory extends AbstractRoutePredicateFactory<TimeBetweenConfig> {

    public TimeBetweenRoutePredicateFactory() {
        super(TimeBetweenConfig.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(TimeBetweenConfig config) {
        LocalTime start = config.getStart();
        LocalTime end = config.getEnd();
        return exchanger -> {
            LocalTime now = LocalTime.now();
            return now.isAfter(start) &&  now.isBefore(end);
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        // 定义参数的顺序，参数名和配置类中一致
        return Arrays.asList("start", "end");
    }
}
