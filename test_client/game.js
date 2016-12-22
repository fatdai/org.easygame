var canvas;
var ctx;
var player;  // 主角
var then;
var canvasWidth,canvasHeight;


var others = []; // 其他在线的玩家
var pendingInput = [];
var recvMsg = [];

var curFrame = 0;  // 当前多少帧

var validMouseDown = false; // 有效的mousedown
var lag = 150; // 这里假设延迟为 150ms

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

	player = new Player(obj[Constants.JK_USER]);
	then = Date.now();
	gameLoop();
}	


function gameLoop(){

	processServerMsg();

	var now = Date.now();
	var delta = now - then;

	update(delta/1000);
	render();
	then = now;

	++curFrame;

	requestAnimationFrame(gameLoop);
}

function getUser(name) {
	for(var i = 0; i < others.length; ++i){
		if (others[i].name == name) {
			return others[i];
		}
	}
}

// 获取当前需要被执行的 input
function getProcessedMsg(){
	for(var i = 0; i < recvMsg.length;i++){
		if (recvMsg[i].time <= Date.now() ) {
			var msg = recvMsg[i];
			recvMsg.remove(i);
			return msg;
		}
	}
}

// 处理服务端的消息
function processServerMsg(){

	while(true){
		var state = getProcessedMsg();
		if (!state) {
			break;
		}

		console.log("-----state:",state);
		if (state.username == player.name) {

			// 当前角色
			player.moving = (state.moving == 1?true:false);
			player.mx = state.dir.dx;
			player.my = state.dir.dy;

			// 继续模拟未处理的输入
			var j = 0;
			while( j < pendingInput.length){
				var input = pendingInput[j];
				if (input.frame <= state.frame) {

					console.log("服务器已经处理....");
					// 服务器已经处理
					pendingInput.remove(j);

					// 其他玩家应该计算一个更快的速度移动到指定位置
				}else{
					console.log("继续模拟....");
					player.applyInput(input);
					j++;
				}
			}
		}else{
			// 处理其他用户
			var tmp = getUser(state.username);
			console.log("tmp:",tmp);
			if (!tmp) {
				return;
			}
			tmp.moving = state.moving;
			tmp.mx = state.dir.dx;
			tmp.my = state.dir.dy;

			// 继续模拟未处理的输入
			var j = 0;
			while( j < pendingInput.length){
				var input = pendingInput[j];
				if (input.frame <= state.frame) {
					// 服务器已经处理
					pendingInput.remove(j);

					// 其他玩家应该计算一个更快的速度移动到指定位置
				}else{
					tmp.applyInput(input);
					j++;
				}
			}

		}
	}
}


function mousedown(e){
	console.log("mouse down!");	
	var point = getPointOnCanvas(e.x,e.y);

	// 在有效区域内 就shoot
	if (point.x >= 0 && point.x <= canvasWidth && point.y >=0 && point.y <= canvasHeight) {

		var x = point.x - player.x;
		var y = point.y - player.y;
		var len = Math.sqrt(x*x+y*y);
		var dx = x/len;
		var dy = y/len;

		// 组装成一个 input
		var input = {};
		input.type = "startmove";  // 输入名字
		input.dir = {dx:dx,dy:dy};
		input.name = player.name;
		input.frame = curFrame;

		// 发给服务器
		sendInput(input);

		// 客户端开始模拟
		player.applyInput(input);

		pendingInput.push(input);

		validMouseDown = true;
	}
}

function sendInput(input) {
	var obj = {};
	obj[Constants.JK_OP] = Constants.OP_INPUTS; // 输入
	obj[Constants.JK_INPUT] = input;
	obj[Constants.JK_TIME] = Date.now() + lag; // 暂时不处理
	ws.send(JSON.stringify(obj));
}


function mouseup(e){
	if (validMouseDown) {
		// 组装成一个 input
		var input = {};
		input.type = "endmove";
		input.frame = curFrame;
		input.name = player.name;

		// 发给服务器
		sendInput(input);

		// 客户端开始模拟
		player.applyInput(input);

		pendingInput.push(input);

		validMouseDown = false;
	}
}


// function move(dir){
// 	var moveObj = {};
// 	moveObj[Constants.JK_OP] = Constants.OP_START_MOVE;
// 	moveObj[Constants.JK_MOVE_DIR] = dir;
// 	ws.send(JSON.stringify(moveObj));
// 	moving = true;
// }

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


function update(delta){
	player.update(delta);

	for (var i = 0; i < others.length; i++) {
		others[i].update(delta);
	}
}

function render(){

	ctx.clearRect(0,0,canvasWidth,canvasHeight);
	player.render(ctx);
	//console.log("player.x and y",player.x,player.y);
	for (var i = 0; i < others.length; i++) {
		others[i].render(ctx);
	}

	// render ui
	// upBtn.render(ctx);
	// downBtn.render(ctx);
	// leftBtn.render(ctx);
	// rightBtn.render(ctx);


}