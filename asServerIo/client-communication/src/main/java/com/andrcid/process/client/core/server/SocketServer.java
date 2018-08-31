package com.andrcid.process.client.core.server;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andrcid.process.client.core.config.ClientConstant;
import com.andrcid.process.client.core.config.SocketClientConfig;
import com.andrcid.process.client.core.context.Global;
import com.andrcid.process.client.core.message.IMessageProcessor;
import com.tigerjoys.communication.protocol.ProtocolConst;
import com.tigerjoys.communication.protocol.enums.MessageFromType;
import com.tigerjoys.communication.protocol.message.MessageDecoder;
import com.tigerjoys.communication.protocol.message.MessageEncoder;
import com.tigerjoys.communication.protocol.utility.ProtocolHelper;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public final class SocketServer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketServer.class);
	
	private EventLoopGroup eventLoopGroup;
	
	private SocketClientConfig config;
	
	private Bootstrap bootstrap;
	
	public SocketServer() {
		this.eventLoopGroup = new NioEventLoopGroup();
		this.config = Global.getInstance().getClientConfig();
	}
	
	public void start() throws InterruptedException {
		LOGGER.info("----------------start socket server--------------------");
		initServer();
		LOGGER.info("----------------socket server start finish-------------");
	}
	
	private void initServer() throws InterruptedException {
		bootstrap = new Bootstrap(); // 客户端引导类
        bootstrap.group(this.eventLoopGroup);// 多线程处理
        bootstrap.channel(NioSocketChannel.class);// 指定通道类型为NioServerSocketChannel，一种异步模式，OIO阻塞模式为OioServerSocketChannel
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.remoteAddress(new InetSocketAddress(config.getServerHost(), config.getServerPort()));// 指定请求地址
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
            	ChannelPipeline pipeline = ch.pipeline();
            	
            	IMessageProcessor messageProcessor = new SocketMessageProcessor();
            	
            	pipeline.addLast(new DelimiterBasedFrameDecoder(ProtocolConst.MAX_FRAME_LENGTH, true , Unpooled.copiedBuffer(ProtocolConst.P_END_TAG)));
            	pipeline.addLast(new MessageDecoder());
            	pipeline.addLast(new MessageEncoder());
            	pipeline.addLast(new ConnectionWatchdog(SocketServer.this));
            	pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));//每隔30s的时间触发一次userEventTriggered的方法，并且指定IdleState的状态位是WRITER_IDLE
            	pipeline.addLast(new ClientHeartBeatHandler());//实现userEventTriggered方法，并在state是WRITER_IDLE的时候发送一个心跳包到sever端，告诉server端我还活着
            	pipeline.addLast(new ClientMessageHandler(messageProcessor));
            }
        });
        
        ChannelFuture f = bootstrap.connect();
        f.addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isDone() && future.isSuccess()) {
					LOGGER.info("---- 服务器连接成功-------");
					//服务器连接成功，立马发送一条心跳消息
					future.channel().writeAndFlush(ProtocolHelper.createHeartBeatMessage(MessageFromType.CLIENT , ClientConstant.DEVICE_ID));
				} else {
					LOGGER.info("---- 连接服务器失败,2秒后重试 ---------host="+config.getServerHost()+",port=" + config.getServerPort());
					reconnection();
				}
			}
        	
        });
	}
	
	/**
	 * 重新连接，暂停的时间
	 * @param waitMillis - 毫秒
	 * @throws InterruptedException
	 */
	public void reconnection() throws InterruptedException {
		this.eventLoopGroup.schedule(new Runnable() {
            @Override
            public void run() {
            	try {
            		initServer();
				} catch (InterruptedException e) {
					LOGGER.error(e.getMessage() , e);
				}
            }

        }, 2L, TimeUnit.SECONDS);
	}

}
