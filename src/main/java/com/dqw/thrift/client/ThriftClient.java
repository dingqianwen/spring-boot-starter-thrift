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
     * 实例名称,可能会存在多个实例,例如订单/用户/会员,这里表示选择使用哪个实例
     *
     * @return String
     */
    String instances();

    /**
     * 每个实例里面会存在多个ThriftProvider,这里指定使用哪一个ThriftProvider
     * <p>
     * 例如服务提供者:
     * <@ThriftProvider>
     * public class HelloAgentImpl implements HelloAgent.Iface {
     * }
     * 此服务的名称为:helloAgentImpl
     * <p>
     * 例如服务提供者:
     * <@ThriftProvider(serviceName="helloAgent")>
     * public class HelloAgentImpl implements HelloAgent.Iface {
     * }
     * 此服务的名称为:helloAgent
     * </p>
     *
     * @return String
     */
    String serviceName();
}
