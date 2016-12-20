package org.easygame;

import io.netty.channel.Channel;

public class User {
	private String username;
	private String password;

	// 可以有等级 血量 攻击等属性
	private Channel channel;

	private int level; // 等级
	private float attack; // 攻击力
	private float defence; // 防御力
	
	

	public User(String username, String password, Channel channel) {
		super();
		this.username = username;
		this.password = password;
		this.channel = channel;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}
