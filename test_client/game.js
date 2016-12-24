
var validMouseDown = false; // 有效的mousedown


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

	then = Date.now();

	player = new Player(obj.p);
	gameLoop();
}	


function gameLoop(){

	var cur = Date.now();
	var elapse = cur - then;
	then = cur;

	update(elapse/1000);
	render();
	requestAnimationFrame(gameLoop);
}

function queryOtherPlayer(name) {
	for(var i = 0; i < others.length; ++i){
		if (others[i].name == name) {
			return others[i];
		}
	}
}

// 获取当前需要被执行的 input
// function getProcessedMsg(){
// 	for(var i = 0; i < recvMsg.length;i++){
// 		if (recvMsg[i].time <= Date.now() ) {
// 			var msg = recvMsg[i];
// 			recvMsg.remove(i);
// 			return msg;
// 		}
// 	}
// }

// 处理服务端的消息
// function processServerMsg(){

// 	while(true){
// 		var state = getProcessedMsg();
// 		if (!state) {
// 			break;
// 		}

// 		console.log("-----state:",state);
// 		if (state.username == player.name) {

// 			// 当前角色
// 			player.moving = (state.moving == 1?true:false);
// 			player.mx = state.dir.dx;
// 			player.my = state.dir.dy;

// 			// 继续模拟未处理的输入
// 			var j = 0;
// 			while( j < pendingInput.length){
// 				var input = pendingInput[j];
// 				if (input.frame <= state.frame) {

// 					console.log("服务器已经处理....");
// 					// 服务器已经处理
// 					pendingInput.remove(j);

// 					// 其他玩家应该计算一个更快的速度移动到指定位置
// 				}else{
// 					console.log(player.name,"    开始模拟....");
// 					player.applyInput(input);
// 					j++;
// 				}
// 			}
// 		}else{
// 			// 处理其他用户
// 			var tmp = getUser(state.username);
// 			console.log("tmp:",tmp);
// 			if (!tmp) {
// 				return;
// 			}
// 			tmp.moving = state.moving;
// 			tmp.mx = state.dir.dx;
// 			tmp.my = state.dir.dy;

// 			// 继续模拟未处理的输入
// 			var j = 0;
// 			while( j < pendingInput.length){
// 				var input = pendingInput[j];
// 				if (input.frame <= state.frame) {
// 					// 服务器已经处理
// 					pendingInput.remove(j);

// 					// 其他玩家应该计算一个更快的速度移动到指定位置
// 				}else{
// 					console.log(tmp.name,"   开始模拟....");
// 					tmp.applyInput(input);
// 					j++;
// 				}
// 			}

// 		}
// 	}
// }


function mousedown(e){
	console.log("mouse down!");	
	var point = getPointOnCanvas(e.x,e.y);

	// 在有效区域内 就shoot
	if (point.x >= 0 && 
		point.x <= canvasWidth && 
		point.y >=0 && 
		point.y <= canvasHeight) {

		// 计算方向
		var dir = player.calMDIR(point);

		// --- 发送事件
		var ret = {};
		ret.op = "move_start";
		ret.dir = dir;
		ret.gx = player.gx;
		ret.gy = player.gy;
		ret.id = player.id;
		ws.send(JSON.stringify(ret));

		player.startmove(dir);
		validMouseDown = true;
	}
}

// function sendInput(input) {
// 	var obj = {};
// 	obj[Constants.JK_OP] = Constants.OP_INPUTS; // 输入
// 	obj[Constants.JK_INPUT] = input;
// 	obj[Constants.JK_TIME] = Date.now() + lag; // 暂时不处理
// 	ws.send(JSON.stringify(obj));
// }


function mouseup(e){
	if (validMouseDown) {
		var ret = {};
		ret.op = "move_end";
		ret.id = player.id;
		ws.send(JSON.stringify(ret));

		player.endmove();
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






function update(dt){

	player.update(dt);

	for (var i = 0; i < others.length; i++) {
		others[i].update(dt);
	}
}

function render(){

	ctx.clearRect(0,0,canvasWidth,canvasHeight);

	// 绘制格子
	grid.render(ctx);

	// 绘制当前角色
	player.render(ctx);

	//console.log("player.x and y",player.x,player.y);
	for (var i = 0; i < others.length; i++) {
		others[i].render(ctx);
	}
}