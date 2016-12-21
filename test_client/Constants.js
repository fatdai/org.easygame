

var Constants = {};

// login
Constants.OP_LOGIN = 100;
Constants.OP_LOGIN_REPLY = 101;
Constants.OP_REGIST = 102;
Constants.OP_REGIST_REPLY = 103;

// move
Constants.OP_START_MOVE = 104;
Constants.OP_START_MOVE_REPLY = 105;

Constants.OP_STOP_MOVE = 106;
Constants.OP_STOP_MOVE_REPLY = 107;

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

// move
Constants.JK_MOVE_DIR = "mdir";



// 返回码
var ReplyCode = {};
ReplyCode.SUCCESS = 1;
ReplyCode.FAILED = 2;
ReplyCode.ERROR = 3;
ReplyCode.INPROGRESS = 4;

