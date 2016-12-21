
var unit = 10;
var speed = 10;

function Player(obj){
	var tmpUser = obj[Constants.JK_USER];
	this.name = tmpUser[Constants.JK_USERNAME];
	this.x = tmpUser[Constants.JK_X];
	this.y = tmpUser[Constants.JK_Y];
	this.attack = tmpUser[Constants.JK_ATTACK];
	this.defence = tmpUser[Constants.JK_DEFENCE];
	this.level = tmpUser[Constants.JK_LEVEL];

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
		this.x += (this.mx * dt);
		this.y += (this.my * dt);
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
