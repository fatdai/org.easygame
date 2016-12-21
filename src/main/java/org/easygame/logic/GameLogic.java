package org.easygame.logic;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import org.easygame.Constants;
import org.easygame.Msg;
import org.easygame.User;
import org.easygame.db.UserDAO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameLogic implements Runnable {

	private LinkedBlockingQueue<Msg> msgQueue = new LinkedBlockingQueue<Msg>();

	private static Logger logger = LoggerFactory.getLogger(GameLogic.class);

	private static GameLogic gameLogic = new GameLogic();
	private Random random = new Random(System.currentTimeMillis());

	private GameLogic() {
	}

	public static GameLogic getInctance() {
		return gameLogic;
	}

	// 50 fps 去运行
	public void run() {
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

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void process(Msg msg) {
		int op = msg.getOp();
		if (op >= Constants.OP_LOGIN_MIN && op <= Constants.OP_LOGIN_MAX) {
			processLogin(msg);
		}else if (op == Constants.OP_START_MOVE) {
			// 开始运动,判断是否可以运动,直接返回
			JSONObject ret = new JSONObject();
			ret.put(Constants.JK_OP,Constants.OP_START_MOVE_REPLY);
			ret.put(Constants.JK_MOVE_DIR, msg.getObject(Constants.JK_MOVE_DIR));
			ret.put(Constants.JK_CODE,Constants.SUCCESS);
			logger.debug("发送的消息:{}",ret.toString());
			msg.getChannel().writeAndFlush(ret.toString());
		}else if (op == Constants.OP_STOP_MOVE) {
			JSONObject ret = new JSONObject();
			ret.put(Constants.JK_OP,Constants.OP_START_MOVE_REPLY);
			ret.put(Constants.JK_CODE,Constants.SUCCESS);
			msg.getChannel().writeAndFlush(ret.toString());
		}else {
			logger.debug("un process op:{}",op);
		}
	}

	public void processLogin(Msg msg) {
		if (msg.getOp() == Constants.OP_LOGIN) {

			// 获取 username passward
			String username = msg.getString(Constants.JK_USERNAME);
			String password = msg.getString(Constants.JK_PASSWORD);

			logger.debug("username:{},password:{}", username, password);

			// 查询db验证等
			// 1.检查数据库中是否存在
			User user = UserDAO.getInstance().getUser(username, password);
			if (null == user) {

				logger.debug("用户不存在!");

				// 用户不存在
				JSONObject ret = new JSONObject();
				ret.put(Constants.JK_OP, Constants.OP_LOGIN_REPLY);
				ret.put(Constants.JK_CODE, Constants.FAILED);
				ret.put(Constants.JK_DESC, "账号或者密码错误!");
				msg.getChannel().writeAndFlush(ret.toString());

			} else {

				// 登陆成功!
				user.setChannel(msg.getChannel());
				OnlineUserMap.getInstance().addUser(user);

				// 返回给客户端,进入游戏
				JSONObject ret = new JSONObject();
				ret.put(Constants.JK_OP, Constants.OP_LOGIN_REPLY);
				ret.put(Constants.JK_CODE, Constants.SUCCESS);
				ret.put(Constants.JK_USER, user.genUserJsonObject());
				msg.getChannel().writeAndFlush(ret.toString());
			}
		} else if (msg.getOp() == Constants.OP_REGIST) {
			// 获取 username passward
			String username = msg.getString(Constants.JK_USERNAME);
			String password = msg.getString(Constants.JK_PASSWORD);

			logger.debug("username:{},password:{}", username, password);

			// 检查是否存在,不存在才可以注册
			if (!UserDAO.getInstance().isUserExist(username, password)) {
				// 可以创建新账号
				User newUser = new User();
				newUser.setUsername(username);
				newUser.setPassword(password);
				newUser.setAttack(2);
				newUser.setDefence(1);
				newUser.setLevel(1);
				newUser.setX(random.nextInt(Constants.WIDTH));
				newUser.setY(random.nextInt(Constants.HEIGHT));
				newUser.setChannel(msg.getChannel());
				UserDAO.getInstance().insertUser(newUser);

				// 直接放到在线的用户里
				OnlineUserMap.getInstance().addUser(newUser);

				JSONObject ret = new JSONObject();
				ret.put(Constants.JK_OP, Constants.OP_REGIST_REPLY);
				ret.put(Constants.JK_CODE, Constants.SUCCESS);
				ret.put(Constants.JK_USER, newUser.genUserJsonObject());
				msg.getChannel().writeAndFlush(ret.toString());
			} else {
				// 返回账号已存在,不可以注册
				JSONObject ret = new JSONObject();
				ret.put(Constants.JK_OP, Constants.OP_REGIST_REPLY);
				ret.put(Constants.JK_CODE, Constants.FAILED);
				ret.put(Constants.JK_DESC, "账号已存在!");
				msg.getChannel().writeAndFlush(ret.toString());
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
