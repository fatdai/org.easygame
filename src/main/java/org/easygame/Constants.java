package org.easygame;

public class Constants {
	
	//##############################################
	// login
	public static final int OP_LOGIN_MIN = 100;
	public static final int OP_LOGIN = 100;   // 登陆
	public static final int OP_LOGIN_REPLY = 101; // 
	public static final int OP_REGIST = 102;  // 注册
	public static final int OP_REGIST_REPLY = 103; 
	public static final int OP_OTHER_ENTER = 104; //其他玩家加入游戏
	public static final int OP_OTHERS = 105; // 其他玩家
	public static final int OP_OTHER_LEAVE = 106;
	public static final int OP_LOGIN_BY_OTHER = 107;  // 账号被别人登陆
	public static final int OP_LOGIN_MAX = 107;
	
	// move
	public static final int OP_START_MOVE = 200;
	public static final int OP_START_MOVE_REPLY = 201;
	public static final int OP_STOP_MOVE = 202;
	public static final int OP_STOP_MOVE_REPLY = 203;
	public static final int OP_INPUTS = 204; // 用户的输入
	public static final int OP_INPUTS_REPLY = 205; // 用户的输入

	//##############################################
	// json key
	public static final String JK_OP = "op";
	public static final String JK_CODE = "code";
	public static final String JK_DESC = "desc";
	
	// user相关
	public static final String JK_USERNAME = "username";
	public static final String JK_PASSWORD = "password";
	public static final String JK_X = "x";
	public static final String JK_Y = "y";
	public static final String JK_ATTACK = "atk";
	public static final String JK_DEFENCE = "def";
	public static final String JK_LEVEL = "lv";
	public static final String JK_USER = "u";
	
	public static final String JK_USER_ENTER_GAME = "newUser";  // 玩家上线
	public static final String JK_OTHERS = "others";  // 其他玩家
	
	// move
	public static final String JK_MOVE_DIR = "mdir";
	
	//##############################################
	// code
	public static final int SUCCESS = 1;
	public static final int FAILED = 2;
	public static final int ERROR = 3;
	public static final int INPROGRESS = 4;
	
	
	// 地图大小
	public static final int WIDTH = 480;
	public static final int HEIGHT = 320;
}
