
var unit = 10;
var speed = 100;

function Player(obj){
	
	this.name = obj[Constants.JK_USERNAME];
	this.x = obj[Constants.JK_X];
	this.y = obj[Constants.JK_Y];
	this.attack = obj[Constants.JK_ATTACK];
	this.defence = obj[Constants.JK_DEFENCE];
	this.level = obj[Constants.JK_LEVEL];

	// 运动
	this.moving = false;
	this.mx = 0;
	this.my = 0;
}


Player.prototype.render = function(ctx) {
	
	// 绘制名字
	ctx.save();
	ctx.fillStyle = "rgb(255,255,255)";
	ctx.fillText(this.name,this.x,this.y - 10);

	ctx.fillStyle = "rgb(255,0,0)";
	ctx.fillRect(this.x - unit*this.level/2,this.y - unit*this.level/2,unit*this.level,unit*this.level);
	ctx.restore();
};

Player.prototype.update = function(dt){
	if (this.moving) {
		this.x += (this.mx * dt * speed);
		this.y += (this.my * dt * speed);
	}
}

Player.prototype.startMove = function(dir){
	console.log("dir:",dir);
	this.moving = true;
	this.mx = dir.x;
	this.my = dir.y;
}

Player.prototype.endMove = function(){
	this.moving = false;
}
