package com.andrcid.process.client.core.server;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andrcid.process.client.core.config.ClientConstant;
import com.andrcid.process.client.core.session.ISession;
import com.andrcid.process.client.core.session.SessionFactory;
import com.tigerjoys.communication.protocol.Protocol;
import com.tigerjoys.communication.protocol.enums.MessageType;
import com.tigerjoys.communication.protocol.message.IMessage;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

@ChannelHandler.Sharable
public class ConnectionWatchdog extends ChannelInboundHandlerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionWatchdog.class);

	private Timer timer = new HashedWheelTimer();
	
	private SocketServer server;

	private int attempts;
	private volatile long refreshTime = 0L;
	private volatile boolean heartBeatCheck = false;
	private volatile ISession session;
	
	private volatile boolean reconnect = true;
	
	public ConnectionWatchdog(SocketServer server) {
		this.server = server;
	}

	/**
	 * 链路链接之后执行
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		this.session = SessionFactory.buildSession(ctx , ClientConstant.DEVICE_ID);
		attempts = 0;
		this.reconnect = true;
		refreshTime = System.currentTimeMillis();
		if (!heartBeatCheck) {
			heartBeatCheck = true;
			session.getCtx().channel().eventLoop().scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					if (System.currentTimeMillis() - refreshTime > 10 * 1000L) {
						session.close();
						LOGGER.info("心跳检查失败,等待重连服务器---------");
					} else {
						LOGGER.info("心跳检查Successs");
					}
				}
			}, 5L, 5L, TimeUnit.SECONDS);
		}
		LOGGER.info("Connects with {}.", session.getCtx().channel());
		ctx.fireChannelActive();
	}

	/**
	 * 因为链路断掉之后，会触发channelInActive方法，进行重连 重连11次后 不再重连
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.warn("Disconnects with {}, doReconnect = {},attemps == {}", ctx.channel(), reconnect, attempts);
		if (this.reconnect) {
			if (attempts < 12) {
				attempts++;
			} else {
				this.reconnect = false;
			}
			long timeout = 2 << attempts;
			LOGGER.info("After {} seconds client will do reconnect",timeout);
			timer.newTimeout(new TimerTask() {

				@Override
				public void run(Timeout timeout) throws Exception {
					server.reconnection();
				}
			}, timeout, TimeUnit.SECONDS);
		}
	}

	/**
	 * 心跳检测 ，将服务端返回的数据进行读取
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg != null) {
			if(msg instanceof Protocol) {
				Protocol p = (Protocol)msg;
				IMessage message = (IMessage)p.getEntity();
				
				if(message.messageType() == MessageType.HeartBeat) {
					refreshTime = System.currentTimeMillis();
					LOGGER.info("receive heartBeat response from server{},time is{}",message,refreshTime);
					return;
				}
				
				ctx.fireChannelRead(p);
			} else {
				LOGGER.warn("receive server message unknown type : " + msg.getClass().getName());
			}
		}
	}

}
