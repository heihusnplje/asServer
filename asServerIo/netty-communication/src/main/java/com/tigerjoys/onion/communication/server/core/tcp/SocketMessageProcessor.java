package com.tigerjoys.onion.communication.server.core.tcp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tigerjoys.communication.protocol.Protocol;
import com.tigerjoys.communication.protocol.enums.MessageFromType;
import com.tigerjoys.communication.protocol.message.IMessage;
import com.tigerjoys.communication.protocol.utility.ProtocolHelper;
import com.tigerjoys.onion.communication.server.core.context.BeatContext;
import com.tigerjoys.onion.communication.server.core.context.ServerType;
import com.tigerjoys.onion.communication.server.core.server.IMessageProcessor;
import com.tigerjoys.onion.communication.server.core.session.Session;
import com.tigerjoys.onion.communication.server.core.session.SessionManager;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

/**
 * 消息处理
 * @author chengang
 *
 */
public class SocketMessageProcessor implements IMessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SocketMessageProcessor.class);
	
	//private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("HB", CharsetUtil.UTF_8));
	
	@Override
	public void processReceiveConnect(ChannelHandlerContext ctx, IMessage msg) {
		LOGGER.info("deviceId {} register!" , msg.getDeviceId());
		
		SessionManager.getInstance().createSession(ctx, msg);
	}

	@Override
	public void processReceiveDisconnect(ChannelHandlerContext ctx) {
		Session session = SessionManager.getInstance().getSession(ctx);
		if(session != null) {
			LOGGER.info("deviceId {} unregister!" , session.getDeviceId());
			//清除session状态
			session.close();
		} else {
			ctx.close();
		}
	}

	@Override
	public void processReceivePing(ChannelHandlerContext ctx) {
		Session session = SessionManager.getInstance().getSession(ctx);
		
		if(session != null) {
			LOGGER.info("deviceId {} keep register!" , session.getDeviceId());
			
			boolean keepAlive = SessionManager.getInstance().keepSession(ctx);
			if (!keepAlive) {
				processReceiveDisconnect(ctx);
			} else {
				ctx.writeAndFlush(ProtocolHelper.createHeartBeatMessage(MessageFromType.SERVER, session.getDeviceId())).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
			}
		}
	}

	@Override
	public void processReceiveMessage(ChannelHandlerContext ctx, Protocol p) {
		Session session = SessionManager.getInstance().getSession(ctx);
		
		if (session == null || session.isClosed()) {
			LOGGER.warn("publish message's session is null or closed");
			return;
		}
		//组装BeatContext
		AsyncMessageInvoker.getInstance().invoke(BeatContext.wrapContext(p, session , ServerType.TCP));
	}
	
}
