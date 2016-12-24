package org.easygame.logic;

import io.netty.channel.Channel;

import java.lang.annotation.Retention;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.easygame.Msg;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class World {
	private static Logger logger = LoggerFactory.getLogger(World.class);

	public static final int kWorldWidth = 640;
	public static final int kWorldHeight = 480;
	public static final int kItem = 10; // 10个一个小格子

	// 所有在线的玩家
	private Map<Integer, Player> players = new HashMap<Integer, Player>();

	public void update(float dt) {
		for (Entry<Integer, Player> entry : players.entrySet()) {
			entry.getValue().update(dt);
		}
	}

	public void addPlayer(Player player) {
		players.put(player.getId(), player);
	}

	// 查找用户
	public Player getPlayer(int id) {
		return players.get(id);
	}

	public Player getPlayer(Channel channel) {
		for (Entry<Integer, Player> entry : players.entrySet()) {
			if (entry.getValue().getChannel() == channel) {
				return entry.getValue();
			}
		}
		return null;
	}

	public void removePlayer(int id) {
		players.remove(id);
	}

	public int getOnlineCount() {
		return players.size();
	}

	// ######################################################
	// 广播消息
	public void bc_player_exit(Player leavePlayer) {
		JSONObject ret = new JSONObject();
		ret.put("op", "sys_player_exit");
		ret.put("id", leavePlayer.getId());
		for (Entry<Integer, Player> entry : players.entrySet()) {
			Msg.Send(entry.getValue().getChannel(), ret);
		}
	}

	public void bc_player_enter(Player enterPlayer) {
		JSONObject ret = new JSONObject();
		ret.put("op", "sys_player_enter");
		ret.put("p", enterPlayer.loginOnJson());
		for (Entry<Integer, Player> entry : players.entrySet()) {
			if (entry.getValue() != enterPlayer) {
				Msg.Send(entry.getValue().getChannel(), ret);
			}
		}
	}

	public void kickPlayerIfOnline(int id) {
		Player oldPlayer = getPlayer(id);
		if (null != oldPlayer) {

			// 告诉 oldUser,你被踢下线
			JSONObject obj = new JSONObject();
			obj.put("op", "login_by_other");
			Msg.Send(oldPlayer.getChannel(), obj);

			// 然后踢掉
			removePlayer(oldPlayer.getId());
			bc_player_exit(oldPlayer);
		}
	}

	// 把 others 信息同步给 player
	public void bc_other_players(Player player) {
		JSONObject ret = new JSONObject();
		JSONArray array = new JSONArray();
		int counter = 0;
		for (Entry<Integer, Player> entry : players.entrySet()) {
			if (entry.getValue() != player) {
				array.put(counter++, entry.getValue().loginOnJson());
			}
		}
		ret.put("op", "sys_login_ps");
		ret.put("ps", array);

		for (Entry<Integer, Player> entry : players.entrySet()) {
			if (entry.getValue() != player) {
				Msg.Send(entry.getValue().getChannel(), ret);
			}
		}
	}

	public void bc_player_move_start(Player player) {

		JSONObject ret = new JSONObject();
		ret.put("op", "sys_other_move_start");
		ret.put("dir", player.getDir());
		ret.put("id", player.getId());

		for (Entry<Integer, Player> entry : players.entrySet()) {
			if (entry.getValue() != player) {
				Msg.Send(entry.getValue().getChannel(), ret);
			}
		}
	}
}
