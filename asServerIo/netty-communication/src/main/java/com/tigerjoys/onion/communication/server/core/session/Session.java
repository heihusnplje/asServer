package com.tigerjoys.onion.communication.server.core.session;

import io.netty.channel.ChannelHandlerContext;

/**
 * session
 * @author chengang
 *
 */
public interface Session {
	
	/**
	 * 获得连接通道
	 * @return ChannelHandlerContext
	 */
	public ChannelHandlerContext getCtx();

	/**
	 * 获得设备ID
	 * @return String
	 */
	public String getDeviceId();
	
	/**
	 * 关闭session
	 */
	public void close();

	/**
	 * session是否关闭
	 * @return boolean
	 */
	public boolean isClosed();

}
