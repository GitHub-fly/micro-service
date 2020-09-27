package configuration;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xunmi
 * @ClassName RibbonConfig
 * @Description TODO
 * @Date 2020/9/25
 * @Version 1.0
 **/
//@Configuration
public class RibbonConfig {
    @Bean
    public IRule ribbonRule() {
        return new RandomRule();
    }
}
