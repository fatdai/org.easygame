package org.easygame.logic;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.easygame.Constants;
import org.easygame.Msg;
import org.easygame.vo.User;
import org.json.JSONArray;
import org.json.JSONObject;
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

	public User findByChannel(Channel channel) {
		for (Entry<String, User> entry : map.entrySet()) {
			if (entry.getValue().getChannel() == channel) {
				return entry.getValue();
			}
		}
		return null;
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
	
	public void remove(String key){
		map.remove(key);
	}

	public int getOnlineCount() {
		return map.size();
	}

	// 开始进入游戏的时候广播
	public void broadcastUserOnEnterGame(User newUser) {

		JSONObject oRet = new JSONObject();
		oRet.put(Constants.JK_OP, Constants.OP_OTHER_ENTER);
		oRet.put(Constants.JK_CODE, Constants.SUCCESS);
		oRet.put(Constants.JK_USER_ENTER_GAME, newUser.genUserJsonObject()); // 自己的信息
		String objStr = oRet.toString();

		// 告诉其他玩家 newUser登陆了
		List<User> users = new ArrayList<User>();
		for (Entry<String, User> entry : map.entrySet()) {
			if (!entry.getKey().equals(newUser.getUsername())) {
				entry.getValue().getChannel().writeAndFlush(objStr);
				users.add(entry.getValue());
			}
		}

		// 告诉 newUser 其他玩家的位置
		if (users.size() > 0) {
			JSONArray array = new JSONArray();
			for (int i = 0; i < users.size(); i++) {
				array.put(i, users.get(i).genUserJsonObject());
			}

			JSONObject ret = new JSONObject();
			ret.put(Constants.JK_OP, Constants.OP_OTHERS);
			ret.put(Constants.JK_CODE, Constants.SUCCESS);
			ret.put(Constants.JK_OTHERS, array);
			newUser.getChannel().writeAndFlush(ret.toString());
		}
	}
	
	public void broadcastUserLeaveGame(User user){
		JSONObject ret = new JSONObject();
		ret.put(Constants.JK_OP, Constants.OP_OTHER_LEAVE);
		ret.put(Constants.JK_USERNAME, user.getUsername());
		
		for (Entry<String, User> entry : map.entrySet()) {
			entry.getValue().getChannel().writeAndFlush(ret.toString());
		}
	}
}
