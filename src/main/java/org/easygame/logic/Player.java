package org.easygame.logic;

import org.easygame.Msg;
import org.json.JSONObject;

import io.netty.channel.Channel;

public class Player extends Entity {

	static final float kSpeed = 200.0f;

	protected String name;
	protected String pwd;
	protected Channel channel;
	protected int dir; // 运动方向

	protected float speed;

	public Player(int id) {
		super(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("name:").append(name).append(",gx:").append(gx).append(",gy:").append(gy);
		return sb.toString();
	}

	public JSONObject loginOnJson() {
		JSONObject ret = new JSONObject();
		ret.put("id", id);
		ret.put("name", name);
		ret.put("gx", gx);
		ret.put("gy", gy);
		ret.put("state", state);
		if (state == STATE.MOVING) {
			ret.put("x", getX());
			ret.put("y", getY());
		}
		return ret;
	}

	public JSONObject curJson() {
		JSONObject ret = new JSONObject();
		ret.put("id", id);
		ret.put("gx", gx);
		ret.put("gy", gy);
		ret.put("state", state);
		if (state == STATE.MOVING) {
			ret.put("x", getX());
			ret.put("y", getY());
		}
		return ret;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		Player o = (Player) obj;
		return this.id == o.id;
	}

	// 是否是有效操作
	public boolean isMoveStartValid(Msg msg) {
		int mgx = msg.getInt("gx");
		int mgy = msg.getInt("gy");
		if (this.gx != mgx || this.gy != mgy) {
			return false;
		}
		return true;
	}

	public void startMove() {
		this.state = STATE.MOVING;
		if (dir == MDIR.LEFT || dir == MDIR.RIGHT || dir == MDIR.DOWN || dir == MDIR.UP) {
			speed = kSpeed;
		} else {
			speed = kSpeed / 1.41f;
		}
	}

	private void updatePosition(float dt) {
		if (this.dir == MDIR.LEFT) {
			this.x -= this.speed * dt;
		} else if (this.dir == MDIR.RIGHT) {
			this.x += this.speed * dt;
		} else if (this.dir == MDIR.UP) {
			this.y -= this.speed * dt;
		} else if (this.dir == MDIR.DOWN) {
			this.y += this.speed * dt;
		} else if (this.dir == MDIR.LEFT_UP) {
			// 左上
			this.x -= this.speed * dt;
			this.y -= this.speed * dt;
		} else if (this.dir == MDIR.LEFT_DOWN) {
			// 左下
			this.x -= this.speed * dt;
			this.y += this.speed * dt;
		} else if (this.dir == MDIR.RIGHT_UP) {
			// 右上
			this.x += this.speed * dt;
			this.y -= this.speed * dt;
		} else if (this.dir == MDIR.RIGHT_DOWN) {
			// 右下
			this.x += this.speed * dt;
			this.y += this.speed * dt;
		}
	}

	public void update(float dt) {
		if (this.state == STATE.MOVING) {
			updatePosition(dt);
		}
	}

	public void endMove() {
		this.state = STATE.STATIC;
		
		
		
		this.dir = -1;
	}
}
