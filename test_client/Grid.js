

function Grid(item){
	this.item = item;
}

Grid.prototype.render = function(ctx){
	ctx.save();
	ctx.fillStyle = "white";

	// 绘制横着的
	for (var i = 1; i <= canvasHeight/this.item; i++) {
		ctx.fillRect(0,i * this.item,canvasWidth,1);
	}

	// 绘制竖着的
	for (var i = 1; i <= canvasWidth/this.item; i++) {
		ctx.fillRect(i * this.item,0,1,canvasHeight);
	}

	ctx.restore();
}