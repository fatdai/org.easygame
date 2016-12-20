package org.easygame;

import org.json.JSONObject;

import io.netty.channel.Channel;

public class Msg {
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

	public int getOp() {
		return object.getInt(Constants.JK_OP);
	}

	public String getString(String key) {
		return object.getString(key);
	}
}
