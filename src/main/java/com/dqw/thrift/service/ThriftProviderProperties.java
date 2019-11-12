package com.dqw.thrift.service;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/11/12
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "thrift.service.config")
@ConditionalOnProperty(name = "thrift.service.enabled", havingValue = "true")
public class ThriftProviderProperties {
    private int port;
    private int minThreads;
    private int maxThreads;
}
