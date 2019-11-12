# spring-boot-starter-thrift
thrift快速启动
## 入门
### provider:  
application.yml
```yml
server:
  port: 9000

thrift:
  service:
    enabled: true
    config:
      port: 10000
      minThreads: 10
      maxThreads: 20
```
java:
```java
@Slf4j
@ThriftProvider
public class HelloAgentImpl implements HelloAgent.Iface {
    @Override
    public boolean hello(String hello) throws TException {
        log.info("hello world");
        return true;
    }
}
```
### client:  
application.yml
```yml
server:
  port: 8000
thrift:
  client:
    enabled: true
    config:
      instances:
        helloInstances:
          port: 10000
          host: localhost
```
java:
```
@Slf4j
@RestController
public class HelloClientController {

    @ThriftClient(instances = "helloInstances", serviceName = "helloAgentImpl")
    private HelloAgent.Client helloClient;

    @RequestMapping(value = "/hello")
    public Map<String, Object> hello() throws TException {
        Map<String, Object> map = new HashMap<>();
        map.put("data", helloClient.hello("hello"));
        return map;
    }
}
```
## 解释
### @ThriftProvider:
```java
public @interface ThriftProvider {
    /**
     * 自定义服务名称
     * 如果serviceName不为空,使用注解的name作为服务名称,否则使用默认类名首字母小写为服务名称
     *
     * @return 服务名称
     */
    String serviceName() default "";
}
```
### @ThriftClient:
```java
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
```
