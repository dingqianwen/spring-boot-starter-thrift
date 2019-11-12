package com.dqw.thrift.client;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

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
@ConfigurationProperties(prefix = "thrift.client.config")
@ConditionalOnProperty(name = "thrift.client.enabled", havingValue = "true")
public class ThriftClientProperties {
    private Map<String, Instances> instances;

    @Data
    public static class Instances {
        private Integer port;
        private String host;
    }
}
