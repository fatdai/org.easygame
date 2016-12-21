package org.easygame.logic;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.easygame.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnlineUserMap {

	private static Logger logger = LoggerFactory.getLogger(OnlineUserMap.class);

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

	public void remove(Channel channel) {
		for (Entry<String, User> entry : map.entrySet()) {
			if (entry.getValue().getChannel() == channel) {
				String key = entry.getKey();
				map.remove(entry.getKey());
				logger.debug("remove key : {}", key);
				break;
			}
		}
	}

	public int getOnlineCount() {
		return map.size();
	}
}
