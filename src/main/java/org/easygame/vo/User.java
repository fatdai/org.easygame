package org.easygame.vo;

import java.util.ArrayList;
import java.util.List;

import org.easygame.Constants;
import org.json.JSONObject;

import io.netty.channel.Channel;

public class User {
	private int id;
	private String username;
	private String password;

	// 可以有等级 血量 攻击等属性
	private Channel channel;

	private int level; // 等级
	private float attack; // 攻击力
	private float defence; // 防御力

	private int x;
	private int y;
	
	public List<NetInput> inputs = new ArrayList<NetInput>();

	public User() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public float getAttack() {
		return attack;
	}

	public void setAttack(float attack) {
		this.attack = attack;
	}

	public float getDefence() {
		return defence;
	}

	public void setDefence(float defence) {
		this.defence = defence;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("username:").append(username).append(",password:").append(password);
		sb.append(",level:").append(level).append(",attack:").append(attack).append(",defence:").append(defence);
		return sb.toString();
	}

	// 产生 user json对象,不需要传递password
	public JSONObject genUserJsonObject() {
		JSONObject object = new JSONObject();
		object.put(Constants.JK_USERNAME, username);
		object.put(Constants.JK_X, x);
		object.put(Constants.JK_Y, y);
		object.put(Constants.JK_ATTACK, x);
		object.put(Constants.JK_DEFENCE, x);
		object.put(Constants.JK_LEVEL, level);
		return object;
	}
}
