package com.andrcid.process.client.core.config;

/**
 * 客户端配置
 * @author chengang
 *
 */
public final class SocketClientConfig {
	
	/**
	 * 服务地址
	 */
	private String serverHost;
	
	/**
	 * 服务端口
	 */
	private int serverPort;
	
	/**
	 * 需要扫描的代理包路径
	 */
	private String basePackages;

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getBasePackages() {
		return basePackages;
	}

	public void setBasePackages(String basePackages) {
		this.basePackages = basePackages;
	}

}
