package org.easygame;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;

public class Msg {
	private static Logger logger = LoggerFactory.getLogger(Msg.class);
	private Channel channel;
	private JSONObject object;

	public Msg(Channel channel, JSONObject object) {
		super();
		this.channel = channel;
		this.object = object;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public JSONObject getObject() {
		return object;
	}

	public void setObject(JSONObject object) {
		this.object = object;
	}

	public String getOp() {
		return object.getString("op");
	}

	public String getString(String key) {
		return object.getString(key);
	}

	public JSONObject getObject(String key) {
		return object.getJSONObject(key);
	}

	public long getLong(String key) {
		return object.getLong(key);
	}

	public JSONArray getJsonArray(String key) {
		return object.getJSONArray(key);
	}

	public int getInt(String key) {
		return object.getInt(key);
	}

	public static void Send(Channel channel, JSONObject obj) {
		String op = obj.getString("op");
		if (null == op) {
			throw new NullPointerException("没有消息返回标示!");
		}
		logger.debug("send op : {}", op);
		channel.writeAndFlush(obj.toString());
	}
}
