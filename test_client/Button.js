

function Button(obj){
	this.x = obj.x || 0;
	this.y = obj.y || 0;
	this.w = obj.w || 80;
	this.h = obj.h || 40;
	this.text = obj.text || "button";

	this.lx = this.x - this.w/2;
	this.ly = this.y - this.h/2;
}

Button.prototype.render = function(ctx) {
	ctx.save();
	ctx.fillStyle = "white";
	ctx.fillRect(this.lx,this.ly,this.w,this.h);
	ctx.fillStyle = "black";
	ctx.textAlign = "center";
	ctx.fillText(this.text,this.x,this.y);
	ctx.restore();
};

Button.prototype.click = function(cb){
	cb(this);
}

Button.prototype.contain = function(point){
	if (point.x > this.lx && point.x < this.lx + this.w && point.y > this.ly && point.y < this.ly + this.h) {
		return true;
	}
	return false;
} 