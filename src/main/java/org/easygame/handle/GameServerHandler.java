package org.easygame.handle;

import org.easygame.Msg;
import org.easygame.logic.GameLogic;
import org.easygame.logic.OnlineUserMap;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class GameServerHandler extends SimpleChannelInboundHandler<String> {

	private static Logger logger = LoggerFactory.getLogger(GameServerHandler.class);

	public GameServerHandler() {

	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
		logger.debug("messageReceived called!");

		if (null != msg) {
			JSONTokener tokener = new JSONTokener(msg);
			logger.debug("msg is :{}", msg);
			JSONObject object = (JSONObject) tokener.nextValue();
			GameLogic.getInctance().add(new Msg(ctx.channel(), object));
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		logger.debug("有玩家离线!");
		// 有玩家离线
		OnlineUserMap.getInstance().remove(ctx.channel());
		logger.info("当前在线玩家数量:{}", OnlineUserMap.getInstance().getOnlineCount());
		
	}

}
