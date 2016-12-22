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

	player = new Player(obj[Constants.JK_USER]);
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
		// var x  = point.x - player.x;
		// var y = point.y - player.y;
		// var len = Math.sqrt(x*x+y*y);
		// x = x/len;
		// y = y/len;
		// move({x,y});

		// 组装成一个 input
		var obj = {};
		obj.type = "mousedown";
		obj.x = parseInt(point.x);
		obj.y = parseInt(point.y);
		obj.frame = Math.max(curFrame,keyFrame);
		obj.name = player.name;
		inputArray.push(new NetInput(obj));
	}
}

function mouseup(e){

	// if (moving) {
	// 	// stop move
	// 	var obj = {};
	// 	obj[Constants.JK_OP] = Constants.OP_STOP_MOVE;
	// 	ws.send(JSON.stringify(obj));

	// 	moving = false;
	// }
	
	// 组装成一个 input
	var obj = {};
	obj.type = "mouseup";
	obj.frame = Math.max(curFrame,keyFrame);
	obj.name = player.name;
	inputArray.push(new NetInput(obj));
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


// 游戏主循环
// 简单的按照5帧当作一个关键帧
var curFrame = 0;
var keyFrame = 5;
var recvInputArray = []; // 接受的输入事件
var inputArray = []; // 当前客户端的输入事件
// var waitTime = 0;
function lockStep(){

	// 如果当前是关键帧
	if (curFrame == keyFrame) {
		// 查看是否有服务器的更新包，当前关键帧编号，下一关键帧编号，所有玩家的控制信息
    	// 获取更新包
    	var curInput;
    	for (var i = 0; i < recvInputArray.length; i++) {
    		if (recvInputArray[i].frame == keyFrame) {
    			curInput = recvInputArray[i];
    			// 从数组里面删除
    			recvInputArray.remove(i);
    			break;
    		}
    	}

    	// 如果等不到当前帧的控制数据，则返回
    	if (curInput && curInput.frame == keyFrame) {
    		var nextFrame = keyFrame + 5;

 			console.log("准备发送这一波收集到的输入事件!");
    		// 采集当前的输入当作包发送
    		var obj = {};
    		obj[Constants.JK_OP] = Constants.OP_INPUTS;
    		obj.inputs = inputArray;
    		ws.send(JSON.stringify(obj));

    		console.log("开始模拟运动!");
    		// 开始模拟
    		// 怎么知道是哪个player在运动???
    		if (curInput.type == "mousedown") {
    			// 是否是主角运动
    			var x  = curInput.x - player.x;
    			var y = curInput.y - player.y;
    			var len = Math.sqrt(x*x+y*y);
    			x = x/len;
    			y = y/len;
    			if (curInput.name == player.name) {
    				player.startMove({x,y});
    			}else{
    				for (var i = 0; i < others.length; i++) {
    					if (others[i].name == curInput.name) {
    						others[i].startMove({x,y});
    						break;
    					}
    				}
    			}
    		}else if(curInput.type == "mouseup"){
    			if (curInput.name == player.name) {
    				player.endMove();
    			}else{
    				for (var i = 0; i < others.length; i++) {
    					if (others[i].name == curInput.name) {
    						others[i].endMove();
    						break;
    					}
    				}
    			}
    		}else{
    			console.log("还没实现!");
    		}

    		// 下一个关键帧
    		keyFrame = nextFrame;
    		// waitTime = 0;
    	}
    }else{
    	curFrame++;
    }
}

function gameLoop(){

	//-----关键帧判断------------------
	lockStep();
	//--------------------------------

	var now = Date.now();
	var delta = now - then;

	update(delta/1000);
	render();
	then = now;

	++curFrame;

	requestAnimationFrame(gameLoop);
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

	for (var i = 0; i < others.length; i++) {
		others[i].render(ctx);
	}

	// render ui
	// upBtn.render(ctx);
	// downBtn.render(ctx);
	// leftBtn.render(ctx);
	// rightBtn.render(ctx);


}