package com.dqw.thrift.provider;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/11/5
 * @since 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ThriftProvider {
    /**
     * 自定义服务名称
     *
     * @return 服务名称
     */
    String serviceName() default "";
}
