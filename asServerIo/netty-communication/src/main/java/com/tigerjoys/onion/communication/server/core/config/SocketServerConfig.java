package com.tigerjoys.onion.communication.server.core.config;

import com.tigerjoys.onion.communication.server.core.hotkey.IProxyFactory;

public final class SocketServerConfig {
	
	private int port;
	
	private String basePackages;
	
	private IProxyFactory proxyFactory;

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

	public IProxyFactory getProxyFactory() {
		return proxyFactory;
	}

	public void setProxyFactory(IProxyFactory proxyFactory) {
		this.proxyFactory = proxyFactory;
	}

}
