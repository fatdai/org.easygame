package org.easygame;

public class Constants {
	
	// login
	public static final int OP_LOGIN_MIN = 100;
	public static final int OP_LOGIN = 100;   // 登陆
	public static final int OP_LOGIN_REPLY = 101; // 
	public static final int OP_REGIST = 102;  // 注册
	public static final int OP_REGIST_REPLY = 103; 
	public static final int OP_LOGIN_MAX = 103;
	
	// move
	public static final int OP_START_MOVE = 104;
	public static final int OP_START_MOVE_REPLY = 105;
	public static final int OP_STOP_MOVE = 106;
	public static final int OP_STOP_MOVE_REPLY = 107;
	
	//--------------------------------------------------------
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
	
	// move
	public static final String JK_MOVE_DIR = "mdir";
	
	//----------------------------------------------------------
	// code
	public static final int SUCCESS = 1;
	public static final int FAILED = 2;
	public static final int ERROR = 3;
	public static final int INPROGRESS = 4;
	
	
	// 地图大小
	public static final int WIDTH = 480;
	public static final int HEIGHT = 320;
}
