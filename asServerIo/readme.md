#### 服务通信框架
> client-communication 客户端通信服务框架

> netty-communication 服务端通信服务框架

> communication-message-protocol 通信消息协议框架

> spring-boot-starter-netty-communication 支持spring boot

> spring-boot-netty-test 测试项目

#### 如何使用

#### 1.如果直接开启服务端，则使用如下代码：
```java
SocketServerConfig serverConfig = new SocketServerConfig();
serverConfig.setBasePackages("com.tigerjoys.onion.communication.server.command");
serverConfig.setPort(9527);
serverConfig.setProxyFactory(new DefaultProxyFactory());

NettyBootstrap boot = new NettyBootstrap(serverConfig);
boot.start();
```

#### 2.使用spring boot：
maven引入jar包

```maven
<dependency>
	<groupId>com.tigerjoys.onion.netty.communication</groupId>
	<artifactId>spring-boot-starter-netty-communication</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

application.yml配置如下：

```yml
socket: 
  server:
    enable: true
    port: 9527
    basePackages: com.tigerjoys.onion.communication.server.command
```

#### 3.client端开启
maven引入jar包

```maven
<dependency>
	<groupId>com.tigerjoys.onion.netty.communication</groupId>
	<artifactId>client-communication</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

Client端开启服务代码：

```java
SocketClientConfig config = new SocketClientConfig();
config.setBasePackages("com.andrcid.process.client.command");
config.setServerHost("127.0.0.1");
config.setServerPort(9527);

ClientBootstrap client = new ClientBootstrap(config);
client.start();
```

客户端如何调用PC Server远程接口

```java
@Module //添加动态代理标注
@Controller //标注为远程接口
public interface IServiceRemote {
	
	//远程接口URL
	@RequestMapping("/api/test/123")
	public List<String> sayHello(@Param("aa") String x , @Param("b") int b);//@Param请求的参数

}
```
类和方法说明：<br/>
@Module 必须加，标注为自动代理<br/>
@Controller 标注为远程接口<br/>
@RequestMapping	请求的API接口映射<br/>
<br/>
调用参数说明：<br/>
1.如果调用的是基本对象，或者多参数方法，则必须要加@Param("value") value代表请求的参数名称<br/>
2.如果调用没有参数，则无须添加任何注解<br/>
3.如果调用的是复杂对象类型，并且有且仅有一个参数，则无须添加@Param注解，自动请求的是此对象的JSON序列化后的参数体<br/>
<br/>
如何使用动态代理：<br/>

```java
//接口类
public interface ITestCommand {
	
	public void sayHello();

}
```

```java
//实现类，注意远程API类和此动态代理类可以不放在一个包下。
@Module(from=ITestCommand.class)//from必须填写接口名称
public class TestCommandImpl implements ITestCommand {
	
	@Inject//注入远程代理对象
	private IServiceRemote serviceRemote;

	@Override
	public void sayHello() {
		//调用远程代理方法
		System.out.println(serviceRemote.sayHello("abc" , 101));
	}

}

```

## 其他

如有问题，随时联系。
