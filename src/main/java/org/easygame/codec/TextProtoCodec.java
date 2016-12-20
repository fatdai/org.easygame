package org.easygame.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextProtoCodec extends MessageToMessageCodec<WebSocketFrame, String> {
	private static Logger logger = LoggerFactory.getLogger(TextProtoCodec.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
		// string -> textwebsocketFrame
		out.add(new TextWebSocketFrame(msg));
		logger.debug("encode : {}", msg);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
		String text = ((TextWebSocketFrame)msg).text();
		out.add(text);
		logger.debug("decode : {}", text);
	}

}
