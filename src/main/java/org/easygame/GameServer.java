package org.easygame;

import org.easygame.codec.TextProtoCodec;
import org.easygame.handle.GameServerHandler;
import org.easygame.logic.GameLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class GameServer {

	private static Logger logger = LoggerFactory.getLogger(GameServer.class);

	void run(int port) throws InterruptedException {

		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(boss, worker);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.option(ChannelOption.SO_BACKLOG, 128);

		// 通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
		bootstrap.option(ChannelOption.TCP_NODELAY, true);

		// 保持长连接状态
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();

				// websocket
				// 编解码 http 请求
				pipeline.addLast(new HttpServerCodec());

				// 写内容
				pipeline.addLast(new ChunkedWriteHandler());

				// 聚合解码 HttpRequest/HttpContent/LastHttpContent 到
				// FullHttpRequest
				// 保证接收的 Http 请求的完整性
				pipeline.addLast(new HttpObjectAggregator(65536));

				// 处理其他的 websocketFrame
				pipeline.addLast(new WebSocketServerProtocolHandler("/game"));

				// 处理 websocketFrame 编解码
				pipeline.addLast(new TextProtoCodec());

				// 处理具体的业务
				pipeline.addLast(new GameServerHandler());
			}
		});

		ChannelFuture f = bootstrap.bind(port).sync();
		if (f.isSuccess()) {
			logger.info("server start at port {}", port);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		int port = 8888;
		GameServer gameServer = new GameServer();
		gameServer.run(port);
		
		// 启动游戏逻辑线程
		new Thread(GameLogic.getInctance()).start();
		logger.info("游戏逻辑线程启动......");
	}
}
