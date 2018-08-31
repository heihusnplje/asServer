package com.tigerjoys.onion.communication.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.andrcid.process.client.core.ClientBootstrap;
import com.andrcid.process.client.core.config.SocketClientConfig;
import com.andrcid.process.client.core.context.Global;
import com.tigerjoys.communication.client.test.command.ITestCommand;

@SpringBootApplication
public class TestBootstrap {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestBootstrap.class);
	
	public static void main(String[] args) throws InterruptedException, IOException {
		ApplicationContext ctx = SpringApplication.run(TestBootstrap.class, args);
		String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
		for (String profile : activeProfiles) {
			LOGGER.warn("Spring Boot 使用profile为:{}", profile);
		}
		
		SocketClientConfig config = new SocketClientConfig();
		config.setBasePackages("com.tigerjoys.communication.client.test.command");
		config.setServerHost("127.0.0.1");
		config.setServerPort(9527);
		
		System.out.println("连接服务端开启");
		ClientBootstrap client = new ClientBootstrap(config);
		client.start();
		
		
		Thread.sleep(5000);
		
		Global.getInstance().resolve(ITestCommand.class).sayHello();
		
		/*List<String> arr = new ArrayList<>();
		arr.add("a");
		arr.add("b");
		arr.add("c");
		
		RequestMessage request = new RequestMessage(ClientConstant.DEVICE_ID , "/api/test/123");
		request.setMessageTime(System.currentTimeMillis());
		request.setBody(JsonHelper.toJson(arr));
		
		Protocol p = new Protocol(0, MessageFromType.CLIENT, request);
		
		ISession session = SessionFactory.getSession();
		try {
			p = session.request(p);
			System.out.println("返回数据结果：" + JsonHelper.toJson(p.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		System.in.read();
	}
	
}
