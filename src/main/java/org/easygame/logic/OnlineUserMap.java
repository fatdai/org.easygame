package org.easygame.logic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.easygame.User;

public class OnlineUserMap {
	private static OnlineUserMap onlineUserMap = new OnlineUserMap();

	// 将username作为key
	private Map<String, User> map = new ConcurrentHashMap<String, User>();

	public static OnlineUserMap getInstance() {
		return onlineUserMap;
	}

	private OnlineUserMap() {
	}

	public User findByUsername(String username) {
		return map.get(username);
	}

	public void addUser(User user) {
		map.put(user.getUsername(), user);
	}

}
