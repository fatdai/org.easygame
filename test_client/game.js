var canvas;
var ctx;
var player;  // 主角
var then;
var canvasWidth,canvasHeight;


var others = []; // 其他在线的玩家

// 控制按钮
// var upBtn;
// var downBtn;
// var leftBtn;
// var rightBtn;

var moving = false;

// 开始游戏
function startGame(obj){
	canvas = document.getElementById("canvas");
	canvasWidth = canvas.width;
	canvasHeight = canvas.height;
	ctx = canvas.getContext("2d");

	// 注册键盘事件
	canvas.addEventListener("mousedown",mousedown,false);
	canvas.addEventListener('mouseup',mouseup,false);
	window.addEventListener("keydown",keydown,true);

	// initBtns();

	player = new Player(obj);
	then = Date.now();
	gameLoop();
}	


// function initBtns(){
// 	upBtn = new Button({x:60,y:canvasHeight-25,text:"上"});
// 	downBtn = new Button({x:180,y:canvasHeight-25,text:"下"});
// 	leftBtn = new Button({x:300,y:canvasHeight-25,text:"左"});
// 	rightBtn = new Button({x:420,y:canvasHeight-25,text:"右"});
// }

function mousedown(e){
	console.log("mouse down!");	
	var point = getPointOnCanvas(e.x,e.y);

	// 在有效区域内 就shoot
	if (point.x >= 0 && point.x <= canvasWidth && point.y >=0 && point.y <= canvasHeight) {

		// 移动的方向为
		var x  = point.x - player.x;
		var y = point.y - player.y;
		var len = Math.sqrt(x*x+y*y);
		x = x/len;
		y = y/len;
		move({x,y});
	}
}

function mouseup(e){

	if (moving) {
		// stop move
		var obj = {};
		obj[Constants.JK_OP] = Constants.OP_STOP_MOVE;
		ws.send(JSON.stringify(obj));

		moving = false;
	}
	
}


function move(dir){
	var moveObj = {};
	moveObj[Constants.JK_OP] = Constants.OP_START_MOVE;
	moveObj[Constants.JK_MOVE_DIR] = dir;
	ws.send(JSON.stringify(moveObj));
	moving = true;
}

function keydown(e){
	switch (e.keyCode){
		case keycode.SPACE:
		shoot();
		break;
		default:
		console.log("未捕获的按键:",e.keyCode);
	}
}

function shoot(){
	// 发射子弹
	console.log("发射子弹!");
}

//--------------------------------------

//--------------------------------------



function getPointOnCanvas(x,y){
	var bbox = canvas.getBoundingClientRect();
	return { x: x - bbox.left * (canvas.width  / bbox.width),  
		y: y - bbox.top  * (canvas.height / bbox.height)  
	}; 
}

function gameLoop(){

	var now = Date.now();
	var delta = now - then;

	update(delta/1000);
	render();

	then = now;

	requestAnimationFrame(gameLoop);
}	


function update(delta){
	player.update(delta);
}

function render(){

	ctx.clearRect(0,0,canvasWidth,canvasHeight);
	player.render(ctx);

	// render ui
	// upBtn.render(ctx);
	// downBtn.render(ctx);
	// leftBtn.render(ctx);
	// rightBtn.render(ctx);


}