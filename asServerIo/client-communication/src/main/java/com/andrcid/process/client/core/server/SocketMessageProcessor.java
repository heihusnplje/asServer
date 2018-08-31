package com.andrcid.process.client.core.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andrcid.process.client.core.message.IMessageProcessor;
import com.andrcid.process.client.core.message.MessageWaitProcessor;
import com.andrcid.process.client.core.message.WindowData;
import com.tigerjoys.communication.protocol.Protocol;
import com.tigerjoys.communication.protocol.utility.JsonHelper;

import io.netty.channel.ChannelHandlerContext;

/**
 * 消息处理中心
 * @author chengang
 *
 */
public class SocketMessageProcessor implements IMessageProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketMessageProcessor.class);

	@Override
	public void processReceiveRequestMessage(ChannelHandlerContext ctx, Protocol p) {
		LOGGER.info("Receive Server RequestMessage : " + JsonHelper.toJson(p.getEntity()));
	}

	@Override
	public void processReceiveResponseMessage(ChannelHandlerContext ctx, Protocol p) {
		LOGGER.info("Receive Server ResponseMessage : " + JsonHelper.toJson(p.getEntity()));
		//解锁线程
		WindowData wd = MessageWaitProcessor.getWindowData(p.getMessageId());
		if(wd != null) {
			wd.setProtocol(p);
			wd.getEvent().set();
		}
	}

	@Override
	public void processReceiveExceptionMessage(ChannelHandlerContext ctx, Protocol p) {
		LOGGER.info("Receive Server ExceptionMessage : " + JsonHelper.toJson(p.getEntity()));
		//解锁线程
		WindowData wd = MessageWaitProcessor.getWindowData(p.getMessageId());
		if(wd != null) {
			wd.setProtocol(p);
			wd.getEvent().set();
		}
	}

}
