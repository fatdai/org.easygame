package org.easygame.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import org.easygame.Constants;
import org.easygame.Msg;
import org.easygame.db.UserDAO;
import org.easygame.vo.NetInput;
import org.easygame.vo.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameLogic implements Runnable {

	private LinkedBlockingQueue<Msg> msgQueue = new LinkedBlockingQueue<Msg>();

	private static Logger logger = LoggerFactory.getLogger(GameLogic.class);

	private static GameLogic gameLogic = new GameLogic();
	private Random random = new Random(System.currentTimeMillis());

	// 所有的用户输入
	private List<NetInput> clientInputs = new ArrayList<NetInput>();

	boolean needBroadcast = false;
	static final long kLag = 150;
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

			// 处理输入
			processInput();
			
			// send world state
			if (needBroadcast) {
				sendWorldState();
				needBroadcast = false;
			}

			update();

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// 同步数据到所有客户端
	private void sendWorldState() {
		OnlineUserMap.getInstance().broadData();
	}

	private void processInput() {
		while (true) {
			NetInput input = getProcessedInput();
			if (null == input) {
				break;
			}
			
			if (input.getType().equals("endmove")) {
				logger.debug("准备执行 停止动画!");
			}

			// 这里假设输入都有效
			User user = OnlineUserMap.getInstance().findByUsername(input.getName());
			user.applyInput(input);
			user.lastProcessedFrame = input.getFrame();
			needBroadcast = true;
		}
	}

	public NetInput getProcessedInput() {
		NetInput ret = null;
		for (NetInput input : clientInputs) {
			if (input.getTime() <= System.currentTimeMillis()) {
				logger.debug("获取到需要被处理的输入!");
				ret = input;
				clientInputs.remove(input);
				break;
			}
		}
		return ret;
	}

	private void update() {
		OnlineUserMap.getInstance().updateUser();
	}

	private void process(Msg msg) {
		int op = msg.getOp();
		if (op >= Constants.OP_LOGIN_MIN && op <= Constants.OP_LOGIN_MAX) {
			processLogin(msg);
		} else if (op == Constants.OP_INPUTS) {
			// 将输入收集起来
			JSONObject inputObj = msg.getObject(Constants.JK_INPUT);
			String inputType = inputObj.getString("type");
			NetInput input = new NetInput();
			input.setType(inputType);
			input.setFrame(inputObj.getInt("frame"));
			input.setTime(msg.getLong("time"));
			input.setName(inputObj.getString("name"));
			if (inputType.equals("startmove")) {
				JSONObject dirObj = inputObj.getJSONObject("dir");
				input.setX(dirObj.getDouble("dx"));
				input.setY(dirObj.getDouble("dy"));
			} else if (inputType.equals("endmove")) {

			} else {
				logger.warn("unsupport input type : {}", inputType);
			}
			clientInputs.add(input);
		} else {
			logger.debug("un process op:{}", op);
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

				// 判断是否已经登陆
				User oldUser = OnlineUserMap.getInstance().findByUsername(username);
				if (null != oldUser) {
					// 告诉 oldUser,你被踢下线
					JSONObject obj = new JSONObject();
					obj.put(Constants.JK_OP, Constants.OP_LOGIN_BY_OTHER);
					oldUser.getChannel().writeAndFlush(obj.toString());

					// 然后踢掉
					OnlineUserMap.getInstance().remove(oldUser.getUsername());
					OnlineUserMap.getInstance().broadcastUserLeaveGame(oldUser);
				}

				// 登陆成功!
				user.setChannel(msg.getChannel());
				OnlineUserMap.getInstance().addUser(user);

				// 返回给客户端,进入游戏
				JSONObject ret = new JSONObject();
				ret.put(Constants.JK_OP, Constants.OP_LOGIN_REPLY);
				ret.put(Constants.JK_CODE, Constants.SUCCESS);
				ret.put(Constants.JK_USER, user.genUserJsonObject());
				msg.getChannel().writeAndFlush(ret.toString());

				OnlineUserMap.getInstance().broadcastUserOnEnterGame(user);
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
				UserDAO.getInstance().insertUser(newUser);

				// 直接放到在线的用户里
				newUser.setChannel(msg.getChannel());
				OnlineUserMap.getInstance().addUser(newUser);

				JSONObject ret = new JSONObject();
				ret.put(Constants.JK_OP, Constants.OP_REGIST_REPLY);
				ret.put(Constants.JK_CODE, Constants.SUCCESS);
				ret.put(Constants.JK_USER, newUser.genUserJsonObject());
				msg.getChannel().writeAndFlush(ret.toString());

				OnlineUserMap.getInstance().broadcastUserOnEnterGame(newUser);
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
