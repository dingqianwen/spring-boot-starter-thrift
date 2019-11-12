package com.dqw.thrift.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/11/5
 * @since 1.0.0
 */
@Slf4j
@Component
@ConditionalOnBean(ThriftProviderProperties.class)
public class ThriftContextRefreshedEvent implements ApplicationListener<ContextRefreshedEvent> {
    @Resource
    private ThriftProviderProperties thriftProviderProperties;

    /**
     * 当spring容器初始化完毕后,开始初始化Thrift
     *
     * @param contextRefreshedEvent contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("Ready to load Thrift!");
        ThriftProviderExecutor thriftProviderExecutor = new ThriftProviderExecutor(contextRefreshedEvent.getApplicationContext(), thriftProviderProperties);
        Thread thread = new Thread(thriftProviderExecutor);
        thread.start();
    }

}