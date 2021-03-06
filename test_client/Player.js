
var unit = 10;
var speed = 200;

// 规定角色只能走8个方向
var MDIR = {
	UP:1,DOWN:2,LEFT:3,RIGHT:4,LEFT_UP:5,LEFT_DOWN:6,RIGHT_UP:7,RIGHT_DOWN:8
}

var STATE = {

	MOVING : 1, // 运动中
	STATIC : 2, // 静止
	ACCE : 3,
}

function Player(obj){
	
	this.id = obj.id;
	this.name = obj.name;
	this.gx = obj.gx;
	this.gy = obj.gy;
	this.state = obj.state;

	if (this.state == STATE.MOVING) {
		this.x = obj.x;
		this.y = obj.y;
	}else{
		this.x = this.gx * item;
		this.y = this.gy * item;
	}

	// 启动加速度
	this.acc = 25;
	this.speed = 0;

	// 方向
	this.dir = null;
}

// 根据点判断方向
Player.prototype.calMDIR = function(point){
	var rad = Math.atan2(point.y-this.y,point.x-this.x);
	var degress = rad * 180/Math.PI;
	
	// 上半部分
	if (degress<=22.5 && degress>=-22.5) {
		return MDIR.RIGHT;  // 
	}else if(degress>22.5 && degress<67.5){
		return MDIR.RIGHT_DOWN;  //
	}else if(degress>=67.5 && degress<=112.5){
		return MDIR.DOWN; //
	}else if(degress>112.5 && degress < 157.5){
		return MDIR.LEFT_DOWN; //
	}else if(degress>=157.5 || degress <= -157.5){
		return MDIR.LEFT; // 
	}else if(degress> -157.5 && degress <= -112.5){
		return MDIR.LEFT_UP; //
	}else if(degress > -112.5 && degress < -67.5){
		return MDIR.UP;  // 
	}else if(degress >= -67.5 && degress < -22.5){
		return MDIR.RIGHT_UP; //
	}else{
		alert("未知的角度 degress : " + degress);
	}
}

Player.prototype.render = function(ctx) {
	
	// 绘制名字
	ctx.save();
	ctx.fillStyle = "white";
	ctx.fillText(this.name,this.x,this.y - 10);

	// 绘制方块
	ctx.fillStyle = "red";
	ctx.fillRect(this.x - unit/2,this.y - unit/2,unit,unit);
	ctx.restore();
};

Player.prototype.updatePosition = function(dt){
	if (this.dir == MDIR.LEFT) {
		this.x -= this.speed * dt;
	}else if(this.dir == MDIR.RIGHT){
		this.x += this.speed * dt;
	}else if(this.dir == MDIR.UP){
		this.y -= this.speed* dt;
	}else if(this.dir == MDIR.DOWN){
		this.y += this.speed* dt;
	}else if(this.dir == MDIR.LEFT_UP){
		// 左上
		this.x -= this.speed* dt;
		this.y -= this.speed* dt;
	}else if(this.dir == MDIR.LEFT_DOWN){
		// 左下
		this.x -= this.speed* dt;
		this.y += this.speed* dt;
	}else if(this.dir == MDIR.RIGHT_UP){
		// 右上
		this.x += this.speed* dt;
		this.y -= this.speed* dt;
	}else if(this.dir == MDIR.RIGHT_DOWN){
		// 右下
		this.x += this.speed* dt;
		this.y += this.speed* dt;
	}
}

Player.prototype.update = function(dt){

	// 加速阶段
	if (this.state == STATE.ACCE) {
		this.speed += this.acc * 0.02;
		if (this.dir == MDIR.LEFT || this.dir == MDIR.RIGHT || this.dir == MDIR.UP || this.dir == MDIR.DOWN) {
			if (this.speed >= speed) {
				this.speed = speed;
				this.state = STATE.MOVING;
			}
		}else{
			if (this.speed >= speed/1.41) {
				this.speed = speed/1.41;
				this.state = STATE.MOVING;
			}
		}

		this.updatePosition(dt);

	}else if (this.state == STATE.MOVING) {
		this.updatePosition(dt);
	}
}

Player.prototype.startmove = function(dir){
	this.dir = dir;
	this.state = STATE.ACCE;
	this.speed = 0;
}

Player.prototype.endmove = function(){
	this.state = STATE.STATIC;
	
	// 根据方向定位
	if (this.dir == MDIR.LEFT) {
		this.gx = parseInt(this.x/item);
	}else if(this.dir == MDIR.RIGHT){
		this.gx = (this.x%item == 0 ? parseInt(this.x/item) : parseInt(this.x/item) + 1);
	}else if(this.dir == MDIR.UP){
		this.gy = parseInt(this.y/item);
	}else if(this.dir == MDIR.DOWN){
		this.gy = (this.y%item == 0 ? parseInt(this.y/item) : parseInt(this.y/item) + 1);
	}else if(this.dir == MDIR.LEFT_UP){
		// 左上
		this.gx = parseInt(this.x/item);
		this.gy = parseInt(this.y/item);
	}else if(this.dir == MDIR.LEFT_DOWN){
		// 左下
		this.gx = parseInt(this.x/item);
		this.gy = (this.y%item == 0 ? parseInt(this.y/item) : parseInt(this.y/item) + 1);
	}else if(this.dir == MDIR.RIGHT_UP){
		// 右上
		this.gx = (this.x%item == 0 ? parseInt(this.x/item) : parseInt(this.x/item) + 1);
		this.gy = parseInt(this.y/item);
	}else if(this.dir == MDIR.RIGHT_DOWN){
		// 右下
		this.gx = (this.x%item == 0 ? parseInt(this.x/item) : parseInt(this.x/item) + 1);
		this.gy = (this.y%item == 0 ? parseInt(this.y/item) : parseInt(this.y/item) + 1);
	}

	// 定位
	this.x = this.gx * item;
	this.y = this.gy * item;

	this.dir = null;
}

Player.prototype.applyInput = function(input){
	if (input.type == "startmove") {
		this.moving = true;
		this.mx = input.dir.dx;
		this.my = input.dir.dy;
	}else if (input.type == "endmove") {
		this.moving = false;
		this.mx = 0;
		this.my = 0;
	}
}





