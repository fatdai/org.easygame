package org.easygame.vo;

import org.json.JSONObject;

public class NetInput {
	private String type; // 输入类型
	private int code; // 键盘事件使用
	private double x, y; // 鼠标事件使用
	private String name; // 标示哪个玩家
	private int frame; // 哪一帧
	private long time;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public JSONObject genJSONObject() {
		JSONObject object = new JSONObject();
		object.put("type", this.type);
		object.put("username", this.name);
		object.put("frame", this.frame);
		if (this.type.equals("mousedown")) {
			object.put("x", this.x);
			object.put("y", this.y);
		} else if (this.type.equals("keyboard")) {
			object.put("code", this.code);
		}
		return object;
	}

}
