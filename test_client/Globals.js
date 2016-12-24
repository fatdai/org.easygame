//#############################################
// 系统
var canvas = null;
var ctx = null;   // canvas context
var canvasWidth,canvasHeight;
var item = 10;
var grid = new Grid(item);

var then;



//#############################################
// 全局变量
var player = null;  // 玩家
var others = []; // 其他的玩家

var validmoving  = false;



