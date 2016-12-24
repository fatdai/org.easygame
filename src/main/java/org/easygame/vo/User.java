package org.easygame.vo;

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

	public boolean moving = false;
	public float dx, dy;
	static final float speed = 1f;
	
	public int lastProcessedFrame = 0; // 上一次被处理输入的帧数
	public long time;

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

//	// 产生 user json对象,不需要传递password
//	public JSONObject genUserJsonObject() {
//		JSONObject object = new JSONObject();
//		object.put(Constants.JK_USERNAME, username);
//		object.put(Constants.JK_X, x);
//		object.put(Constants.JK_Y, y);
//		object.put(Constants.JK_ATTACK, attack);
//		object.put(Constants.JK_DEFENCE, defence);
//		object.put(Constants.JK_LEVEL, level);
//		return object;
//	}
//	
//	public JSONObject genUserState() {
//		JSONObject object = new JSONObject();
//		object.put(Constants.JK_USERNAME, username);
//		object.put(Constants.JK_X, x);
//		object.put(Constants.JK_Y, y);
//		object.put(Constants.JK_ATTACK, x);
//		object.put(Constants.JK_DEFENCE, x);
//		object.put(Constants.JK_LEVEL, level);
//		
//		JSONObject dir = new JSONObject();
//		dir.put("dx", dx);
//		dir.put("dy", dy);
//		
//		object.put("dir", dir);
//		object.put("moving",moving?1:0);
//		object.put("frame", lastProcessedFrame);
//		object.put("time", time);
//		return object;
//	}

	public void applyInput(NetInput input) {
		if (input.getType().equals("startmove")) {
			this.moving = true;
			this.dx = (float) input.getX();
			this.dy = (float) input.getY();
		} else if (input.getType().equals("endmove")) {
			this.moving = false;
			this.dx = this.dy = 0;
		}
	}

	public void update() {
		if (moving) {
			this.x += (speed * dx);
			this.y += (speed * dy);
		}
	}
}
