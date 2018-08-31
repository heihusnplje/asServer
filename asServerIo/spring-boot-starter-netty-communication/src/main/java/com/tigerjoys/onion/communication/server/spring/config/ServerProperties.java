package com.tigerjoys.onion.communication.server.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * application.yml中的netty服务配置信息
 * @author chengang
 *
 */
@ConfigurationProperties(prefix = "socket.server")
public class ServerProperties {
	
	/**
	 * 端口号
	 */
	private int port;
	
	/**
	 * 默认扫描的包路径
	 */
	private String basePackages;
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getBasePackages() {
		return basePackages;
	}

	public void setBasePackages(String basePackages) {
		this.basePackages = basePackages;
	}

}
