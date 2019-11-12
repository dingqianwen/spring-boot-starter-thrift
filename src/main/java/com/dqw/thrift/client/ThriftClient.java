package com.dqw.thrift.client;


import java.lang.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/11/5
 * @since 1.0.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ThriftClient {
    /**
     * 实例名称
     *
     * @return String
     */
    String instances();

    /**
     * 服务实例注册的服务名称(服务提供者的名称),默认为类字母小写
     * <p>
     * 例如服务提供者:
     * <@ThriftProvider>
     * public class HelloAgentImpl implements HelloAgent.Iface {
     * }
     * 此服务的名称为:helloAgentImpl
     * </p>
     *
     * @return String
     */
    String serviceName();
}
