package com.tigerjoys.onion.communication.server.core.session;

import io.netty.channel.ChannelHandlerContext;

/**
 * session
 * @author chengang
 *
 */
public class DefaultSession implements Session {
	
	/**
	 * Channel通道
	 */
	private final ChannelHandlerContext ctx;
	
	/**
	 * 设备ID
	 */
	private final String deviceId;
	
	/**
	 * session是否关闭
	 */
	private volatile boolean closed = false;

	public DefaultSession(ChannelHandlerContext ctx , String deviceId) {
		this.ctx = ctx;
		this.deviceId = deviceId;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void close() {
		closed = true;
		try {
			ctx.close();
		} finally {
			SessionManager.getInstance().removeSession(this);
		}
	}

	@Override
	public boolean isClosed() {
		return closed;
	}

}
