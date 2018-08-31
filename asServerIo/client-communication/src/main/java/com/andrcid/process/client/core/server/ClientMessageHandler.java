package com.andrcid.process.client.core.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andrcid.process.client.core.message.IMessageProcessor;
import com.tigerjoys.communication.protocol.Protocol;
import com.tigerjoys.communication.protocol.enums.MessageType;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.CorruptedFrameException;

/**
 * client message handler
 * @author chengang
 *
 */
public class ClientMessageHandler extends SimpleChannelInboundHandler<Protocol> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageHandler.class);
	
	private IMessageProcessor messageProcessor;
	
	public ClientMessageHandler(IMessageProcessor messageProcessor) {
		this.messageProcessor = messageProcessor;
	}
	
	@Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocol p) throws Exception {
		MessageType mt = p.getMessageType();
		switch(mt) {
			case Request:
				messageProcessor.processReceiveRequestMessage(ctx, p);
				break;
			case Response:
				//TODO 此处是否需要检查合法性？？
				messageProcessor.processReceiveResponseMessage(ctx, p);
				LOGGER.warn("this is client response!");
				break;
			case Exception:
				//输出异常
				messageProcessor.processReceiveExceptionMessage(ctx, p);
				LOGGER.warn("this is client exception!");
				break;
			case Status:
				LOGGER.warn("this is client status!");
				break;
			default:
				LOGGER.warn("have not match messageType");
				break;
		}
    }

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof CorruptedFrameException) {
			// something goes bad with decoding
			LOGGER.warn("Error decoding a packet, probably a bad formatted packet, message: " + cause.getMessage());
		} else {
			LOGGER.error("Ugly error on networking", cause);
		}
	}

}
