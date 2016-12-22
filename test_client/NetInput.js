
// 所有的输入
function NetInput(obj){
	this.type = obj.type; // 输入类型,鼠标 or 键盘
	this.name = obj.username; // 指定是哪个玩家的输入
	this.frame = obj.frame;  // 哪一帧的
	if (obj.type == "keyboard") {
		this.code = obj.code; // 按的哪个键
	}else if(obj.type == "mousedown"){
		this.x = obj.x; 
		this.y = obj.y;  // 鼠标点击的位置
	}
}
