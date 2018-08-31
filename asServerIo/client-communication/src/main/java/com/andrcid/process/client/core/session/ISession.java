package com.andrcid.process.client.core.session;

import java.io.IOException;

import com.tigerjoys.communication.protocol.Protocol;
import com.tigerjoys.communication.protocol.exception.TimeoutException;
import com.tigerjoys.communication.protocol.message.IMessage;

import io.netty.channel.ChannelHandlerContext;

public interface ISession {
	
	/**
	 * 发送请求并接收返回
	 * @param p - 请求协议对象
	 * @return IMessage
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws Exception
	 */
	public IMessage request(Protocol p) throws IOException, TimeoutException, Exception;
	
	/**
	 * 接收请求并发送返回
	 * @param p - 接收协议对象
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws Exception
	 */
	public void receive(Protocol p) throws IOException, TimeoutException, Exception;
	
	public ChannelHandlerContext getCtx();
	
	public void close();
	
	public boolean isClosed();

}
