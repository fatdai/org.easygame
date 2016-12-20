package org.easygame.logic;

import java.util.concurrent.LinkedBlockingQueue;

import org.easygame.Constants;
import org.easygame.Msg;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameLogic implements Runnable {

	private LinkedBlockingQueue<Msg> msgQueue = new LinkedBlockingQueue<Msg>();

	private static Logger logger = LoggerFactory.getLogger(GameLogic.class);

	private static GameLogic gameLogic = new GameLogic();

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
		if (op == Constants.OP_LOGIN) {

			// 获取 username passward
			String username = msg.getString(Constants.JK_USERNAME);
			String password = msg.getString(Constants.JK_PASSWORD);

			logger.debug("username:{},password:{}", username, password);

			// 查询db验证等
			// 1.检查数据库中是否存在
			
			
			
			JSONObject ret = new JSONObject();
			ret.put(Constants.JK_USERNAME, username + "_xx");
			ret.put(Constants.JK_PASSWORD, password + "_xx");
			ret.put(Constants.JK_OP, Constants.OP_LOGIN_REPLY);
			msg.getChannel().writeAndFlush(ret.toString());
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
