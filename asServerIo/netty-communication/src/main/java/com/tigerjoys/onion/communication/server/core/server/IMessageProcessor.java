package com.tigerjoys.onion.communication.server.core.server;

import com.tigerjoys.communication.protocol.Protocol;
import com.tigerjoys.communication.protocol.message.IMessage;

import io.netty.channel.ChannelHandlerContext;

/**
 * 消息处理接口
 * @author chengang
 * 
 */
public interface IMessageProcessor {
	
	/**
	 * 接收连接请求
	 * @param ctx - ChannelHandlerContext
	 * @param msg - 消息实体
	 */
	void processReceiveConnect(ChannelHandlerContext ctx, IMessage msg);
	
	/**
	 * 断开连接请求
	 * @param ctx - ChannelHandlerContext
	 */
	void processReceiveDisconnect(ChannelHandlerContext ctx);
	
	/**
	 * ping的反馈
	 * @param ctx - ChannelHandlerContext
	 */
	void processReceivePing(ChannelHandlerContext ctx);
	
	/**
	 * 接收消息
	 * @param ctx - ChannelHandlerContext
	 * @param msg - 消息协议体
	 */
	void processReceiveMessage(ChannelHandlerContext ctx, Protocol msg);

}
