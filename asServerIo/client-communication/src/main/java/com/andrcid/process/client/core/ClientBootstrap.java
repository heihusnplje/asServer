package com.andrcid.process.client.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andrcid.process.client.core.config.SocketClientConfig;
import com.andrcid.process.client.core.context.Global;
import com.andrcid.process.client.core.hotkey.GuiceModule;
import com.andrcid.process.client.core.server.SocketServer;
import com.google.inject.Guice;

/**
 * 客户端启动类
 * @author chengang
 *
 */
public final class ClientBootstrap {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientBootstrap.class);
	
	public ClientBootstrap(SocketClientConfig clientConfig) {
		Global.getInstance().setClientConfig(clientConfig);
	}
	
	/**
	 * 开启通信服务
	 * @throws InterruptedException 
	 */
	public void start() throws InterruptedException {
		LOGGER.info("----------------------start client netty server------------------");
		
		//加载guice注入服务
		loadGuice();
		
		//加载服务
		loadServer();
		
		LOGGER.info("----------------------start client netty server finish------------");
	}
	
	/**
	 * 开启guice
	 */
	private void loadGuice() {
		Global.getInstance().setInjector(Guice.createInjector(new GuiceModule()));
	}
	
	/**
	 * 加载服务
	 * @throws InterruptedException 
	 */
	private void loadServer() throws InterruptedException {
		SocketServer server = new SocketServer();
		server.start();
	}

}
