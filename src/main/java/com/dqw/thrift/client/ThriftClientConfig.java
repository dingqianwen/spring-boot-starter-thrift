package com.dqw.thrift.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
@ConditionalOnBean(ThriftClientProperties.class)
public class ThriftClientConfig {
    @Resource
    private ThriftClientProperties clientConfig;
    @Resource
    private ApplicationContext applicationContext;

    private static final Set<String> OBJECT_METHOD = Stream.of(Object.class.getDeclaredMethods()).map(Method::getName).collect(Collectors.toSet());

    /**
     * 程序启动时对加了@ThriftClient注解的属性进行代理
     */
    @PostConstruct
    public void init() {
        log.info("Initialize ThriftClient");
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Component.class);
        Collection<Object> values = beans.values();
        for (Object value : values) {
            Field[] declaredFields = value.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(ThriftClient.class)) {
                    checkThriftClient(field);
                    autowiredThrift(value, field);
                }
            }
        }
        log.info("Initialization ThriftClient complete");
    }

    /**
     * 检查注解与属性是否匹配
     *
     * @param field 属性
     */
    private void checkThriftClient(Field field) {
        if (!field.getType().getName().endsWith("$Client")) {
            throw new RuntimeException(String.format("%s,The attribute with the @ThriftClient annotation is not ThriftClient", field.getType()));
        }
    }

    /**
     * 对带有@ThriftClient注解的属性注入
     *
     * @param field 带有@ThriftClient注解的属性
     */
    private void autowiredThrift(Object value, Field field) {
        try {
            field.setAccessible(true);
            ThriftClient thriftClient = field.getAnnotation(ThriftClient.class);
            Map<String, ThriftClientProperties.Instances> instancesMaps = clientConfig.getInstances();
            ThriftClientProperties.Instances instances = instancesMaps.get(thriftClient.instances());
            if (instances == null) {
                throw new RuntimeException(String.format("%s,%s instance not found", field.getType(), thriftClient.instances()));
            }
            TSocket transport = new TSocket(instances.getHost(), instances.getPort());
            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol tMultiplexedProtocol = new TMultiplexedProtocol(protocol, thriftClient.providerName());
            String fieldTypeName = field.getType().getName();
            Class<?> fieldTypeClass = Class.forName(fieldTypeName);
            Constructor<?> constructor = fieldTypeClass.getDeclaredConstructors()[0];
            //属性对象
            Object clientObj = constructor.newInstance(tMultiplexedProtocol);

            Object thriftClientProxy = proxyThriftClient(clientObj, transport, fieldTypeClass, tMultiplexedProtocol);
            //赋值生成的代理对象
            field.set(value, thriftClientProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建代理对象
     *
     * @param clientObj            clientObj
     * @param transport            用此对象完成open and close
     * @param aClass               被代理类
     * @param tMultiplexedProtocol tMultiplexedProtocol
     * @return 生成的代理类
     */
    private Object proxyThriftClient(final Object clientObj, final TSocket transport, Class<?> aClass, TMultiplexedProtocol tMultiplexedProtocol) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(aClass);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, methodProxy) -> {
            try {
                //如果调用Object中的方法,不open与close
                if (OBJECT_METHOD.contains(method.getName())) {
                    return method.invoke(clientObj, args);
                }
                transport.open();
                log.info("open thrift");
                return method.invoke(clientObj, args);
            } finally {
                if (transport.isOpen()) {
                    transport.close();
                    log.info("close thrift");
                }
            }
        });
        return enhancer.create(new Class[]{TProtocol.class}, new Object[]{tMultiplexedProtocol});
    }
}
