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
     * 如果providerName不为空,使用注解的name作为服务名称,否则使用默认类名首字母小写为服务名称
     *
     * @return 服务名称
     */
    String providerName() default "";
}
