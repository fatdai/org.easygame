

var Constants = {};

//##############################################
// login
Constants.OP_LOGIN = 100;
Constants.OP_LOGIN_REPLY = 101;
Constants.OP_REGIST = 102;
Constants.OP_REGIST_REPLY = 103;
Constants.OP_OTHER_ENTER = 104; //其他玩家加入游戏
Constants.OP_OTHERS = 105; // 其他玩家
Constants.OP_OTHER_LEAVE = 106;
Constants.OP_LOGIN_BY_OTHER = 107;  // 账号被别人登陆

// move
Constants.OP_START_MOVE = 200;
Constants.OP_START_MOVE_REPLY = 201;
Constants.OP_STOP_MOVE = 202;
Constants.OP_STOP_MOVE_REPLY = 203;

Constants.OP_INPUTS = 204; // 用户的输入
Constants.OP_INPUTS_REPLY = 205; // 用户的输入

//##############################################
// json key
Constants.JK_OP = "op";
Constants.JK_CODE = "code";
Constants.JK_DESC = "desc";

Constants.JK_USERNAME = "username";
Constants.JK_PASSWORD = "password";
Constants.JK_X = "x";
Constants.JK_Y = "y";
Constants.JK_ATTACK = "atk";
Constants.JK_DEFENCE = "def";
Constants.JK_LEVEL = "lv";
Constants.JK_USER = "u";
Constants.JK_USER_ENTER_GAME = "newUser";  // 玩家上线
Constants.JK_OTHERS = "others";  // 其他玩家

// move
Constants.JK_MOVE_DIR = "mdir";

Constants.JK_INPUT = "input"; // 输入
Constants.JK_TIME = "time"; // 时间


// 返回码
var ReplyCode = {};
ReplyCode.SUCCESS = 1;
ReplyCode.FAILED = 2;
ReplyCode.ERROR = 3;
ReplyCode.INPROGRESS = 4;

