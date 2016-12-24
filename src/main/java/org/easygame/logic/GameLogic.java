package org.easygame.logic;

import io.netty.channel.Channel;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import javax.lang.model.element.VariableElement;

import org.easygame.Msg;
import org.easygame.db.PlayerDAO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameLogic implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(GameLogic.class);

	// 消息队列
	private LinkedBlockingQueue<Msg> msgQueue = new LinkedBlockingQueue<Msg>();

	private static GameLogic gameLogic = new GameLogic();
	private Random random = new Random(System.currentTimeMillis());

	private World world = new World();

	// boolean needBroadcast = false;
	// static final long kLag = 150;

	private GameLogic() {
	}

	public static GameLogic getInctance() {
		return gameLogic;
	}

	// 50 fps 去运行
	public void run() {

		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();

		while (true) {

			// 当队列为空时 take方法会阻塞
			if (!msgQueue.isEmpty()) {
				try {
					// 处理消息队列
					Msg msg = msgQueue.take();
					if (null != msg) {
						process(msg);
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

			// // 处理输入
			// processInput();
			//
			// // send world state
			// if (needBroadcast) {
			// sendWorldState();
			// needBroadcast = false;
			// }

			end = System.currentTimeMillis();
			long epalsed = end - start;
			start = end;

			update(epalsed / 1000.0f);

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// // 同步数据到所有客户端
	// private void sendWorldState() {
	// OnlineUserMap.getInstance().broadData();
	// }

	// private void processInput() {
	//
	// while (true) {
	//
	// NetInput input = getProcessedInput();
	// if (null == input) {
	// break;
	// }
	//
	// if (input.getType().equals("startmove")) {
	// logger.debug("准备执行开始动画! input{}", input);
	// } else if (input.getType().equals("endmove")) {
	// logger.debug("准备执行 停止动画! input{}", input);
	// } else {
	// logger.debug("执行别的动画! input:{}", input);
	// }
	//
	// // 这里假设输入都有效
	// User user = OnlineUserMap.getInstance().findByUsername(input.getName());
	// user.applyInput(input);
	// user.lastProcessedFrame = input.getFrame();
	// needBroadcast = true;
	// }
	// }

	// public NetInput getProcessedInput() {
	// NetInput ret = null;
	// for (NetInput input : clientInputs) {
	// if (input.getTime() <= System.currentTimeMillis()) {
	// logger.debug("获取到需要被处理的输入!");
	// ret = input;
	// clientInputs.remove(input);
	// break;
	// }
	// }
	// return ret;
	// }

	private void update(float dt) {
		world.update(dt);
		// OnlineUserMap.getInstance().updateUser();
	}

	private void process(Msg msg) {

		String op = msg.getOp();
		if (op.startsWith("login")) {
			// 登陆相关
			processLogin(msg);
		} else if (op.startsWith("move")) {
			processMove(msg);
		} else {
			logger.error("un catched op : {}", msg.getOp());
		}
	}

	private void processMove(Msg msg) {
		String op = msg.getOp();
		if (op.equals("move_start")) {
			// 立刻处理,并且进行广播出去
			Player p = world.getPlayer(msg.getInt("id"));
			if (!p.isMoveStartValid(msg)) {
				logger.debug("不是有效操作!");

				JSONObject ret = new JSONObject();
				ret.put("op", "move_start_reply");
				ret.put("error", "unvalid pos");
				ret.put("p", p.curJson());
				Msg.Send(p.getChannel(), ret);
				return;
			}

			p.setDir(msg.getInt("dir"));

			// 有效操作,转发
			world.bc_player_move_start(p);

			// 服务器开始模拟
			p.startMove();
		} else if (op.equals("move_end")) {
			Player player = world.getPlayer(msg.getInt("id"));
			player.endMove();
		}
	}

	public void processLogin(Msg msg) {

		if (msg.getOp().equals("login_on")) {

			// 获取 username passward
			String name = msg.getString("name");
			String pwd = msg.getString("pwd");

			// 检查是否存在
			Player player = PlayerDAO.getInstance().getPlayer(name, pwd);
			if (null == player) {
				logger.debug("用户不存在!");

				// 用户不存在
				JSONObject ret = new JSONObject();
				ret.put("op", "login_on_reply");
				ret.put("error", "用户不存在!");
				Msg.Send(msg.getChannel(), ret);
			} else {

				// 判断是否已经登陆
				world.kickPlayerIfOnline(player.getId());

				// 登陆成功!
				player.setChannel(msg.getChannel());
				world.addPlayer(player);

				// 返回给客户端,进入游戏
				JSONObject ret = new JSONObject();
				ret.put("op", "login_on_reply");
				ret.put("p", player.loginOnJson());
				Msg.Send(msg.getChannel(), ret);

				world.bc_player_enter(player);

				// 将其他玩家消息同步过去
				world.bc_other_players(player);

			}
		} else if (msg.getOp().equals("login_regist")) {
			// 获取 username passward
			String name = msg.getString("name");
			String pwd = msg.getString("pwd");

			// 检查是否存在,不存在才可以注册
			if (!PlayerDAO.getInstance().isPlayerExist(name, pwd)) {

				// 可以创建新账号
				int gx = random.nextInt(64);
				int gy = random.nextInt(48);
				Player newPlayer = PlayerDAO.getInstance().insertPlayer(name, pwd, gx, gy);

				// 直接放到在线的用户里
				newPlayer.setChannel(msg.getChannel());
				world.addPlayer(newPlayer);

				JSONObject ret = new JSONObject();
				ret.put("op", "login_regist_reply");
				ret.put("p", newPlayer.loginOnJson());
				Msg.Send(msg.getChannel(), ret);

				world.bc_player_enter(newPlayer);

				world.bc_other_players(newPlayer);
			} else {
				// 返回账号已存在,不可以注册
				JSONObject ret = new JSONObject();
				ret.put("op", "login_regist_reply");
				ret.put("error", "账号已经存在!");
				Msg.Send(msg.getChannel(), ret);
			}
		} else if (msg.getOp().equals("login_out")) {

			Player player = world.getPlayer(msg.getChannel());
			if (null != player) {
				world.removePlayer(player.getId());

				// 通知其他玩家,有人离线.
				world.bc_player_exit(player);

				logger.debug("在线玩家数量:{}", world.getOnlineCount());
			}
		}
	}

	public void add(Msg msg) {
		try {
			msgQueue.put(msg);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
