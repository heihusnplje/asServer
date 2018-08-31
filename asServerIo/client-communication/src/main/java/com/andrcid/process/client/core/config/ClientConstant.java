package com.andrcid.process.client.core.config;

/**
 * 客户端常量
 * @author chengang
 *
 */
public final class ClientConstant {
	
	/**
	 * 设备ID
	 */
	public static final String DEVICE_ID = "100001";
	
	/**
	 * DEX包路径
	 */
	public static final String DEX_PATH = "/data/local/tmp";
	
	/**
	 * DEX包解压路径
	 */
	public static final String CACHE_PATH = "=/data/local/tmp/cache/";
	
	/**
	 * 最大的消息ID
	 */
	public static final long MAX_MESSAGE_ID = 1024 * 1024 * 1024;
	
	/**
	 * 最大消息等待超时时间,5分钟
	 */
	public static final long MAX_RECEIVE_TIME_OUT = 300000;
	
	private ClientConstant() {
		
	}

}
