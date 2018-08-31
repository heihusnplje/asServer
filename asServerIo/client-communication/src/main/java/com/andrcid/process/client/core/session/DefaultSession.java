package com.andrcid.process.client.core.session;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andrcid.process.client.core.config.ClientConstant;
import com.andrcid.process.client.core.context.Global;
import com.andrcid.process.client.core.message.AutoReceiveEvent;
import com.andrcid.process.client.core.message.MessageIdFactory;
import com.andrcid.process.client.core.message.MessageWaitProcessor;
import com.andrcid.process.client.core.message.WindowData;
import com.tigerjoys.communication.protocol.Protocol;
import com.tigerjoys.communication.protocol.exception.TimeoutException;
import com.tigerjoys.communication.protocol.message.IMessage;

import io.netty.channel.ChannelHandlerContext;

public class DefaultSession implements ISession {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSession.class);

	private volatile boolean closed = false;
	
	private ChannelHandlerContext ctx;
	
	private String deviceId;
	
	public DefaultSession(ChannelHandlerContext ctx , String deviceId) {
		this.ctx = ctx;
		this.deviceId = deviceId;
	}

	@Override
	public IMessage request(Protocol p) throws IOException, TimeoutException, Exception {
		LOGGER.info("客户端发送数据中.....");
		
		int messageId = p.getMessageId();
		if(messageId <= 0) {
			messageId = MessageIdFactory.createMessageId();
			p.setMessageId(messageId);
		}
		
		try {
			MessageWaitProcessor.registerEvent(messageId);
			ctx.writeAndFlush(p);
		} catch (Throwable ex) {
			LOGGER.error("client get socket Exception", ex);
			//抛出异常了则移除定时器
			MessageWaitProcessor.unregisterEvent(messageId);
			throw ex;
		}
		
		LOGGER.info("客户端等待接收数据中.....");
		WindowData wd = MessageWaitProcessor.getWindowData(messageId);
		if(wd != null) {
			try {
				AutoReceiveEvent event = wd.getEvent();
				if (!event.waitOne(ClientConstant.MAX_RECEIVE_TIME_OUT)) {
					StringBuilder buf = new StringBuilder();
					buf.append("ServerIP:[").append(Global.getInstance().getClientConfig().getServerHost());
					buf.append("],ServerPort:[").append(Global.getInstance().getClientConfig().getServerHost());
					buf.append("],Receive data timeout or error!timeout:").append(ClientConstant.MAX_RECEIVE_TIME_OUT);
					
					throw new TimeoutException(buf.toString());
				}
				return (IMessage)wd.getProtocol().getEntity();
			} finally {//移除定时
				MessageWaitProcessor.unregisterEvent(messageId);
			}
		}
		
		return null;
	}

	@Override
	public void receive(Protocol p) throws IOException, TimeoutException, Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void close() {
		this.closed = true;
		try {
			ctx.close();
		} catch (Exception e) {
			LOGGER.error(e.getMessage() , e);
		} finally {
			//从session工厂中移除
			SessionFactory.removeSession();
		}
		
	}

	@Override
	public boolean isClosed() {
		return this.closed;
	}

	@Override
	public ChannelHandlerContext getCtx() {
		return ctx;
	}
	
	@Override
	protected void finalize() throws Throwable {
		try {
			if (this.ctx != null && this.ctx.channel().isOpen()) {
				close();
			}
		} catch (Throwable t) {
			LOGGER.error("Device " + deviceId + " Session Release Error!:", t);
		} finally {
			super.finalize();
		}
	}

}
