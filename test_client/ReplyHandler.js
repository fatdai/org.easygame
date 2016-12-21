

function process(obj){
	if (obj[Constants.JK_OP] == Constants.OP_START_MOVE_REPLY) {
		if (obj[Constants.JK_CODE] != ReplyCode.SUCCESS) {
			console.log("不能move!");
			return;
		}else{
			// 开始运动
			console.log("-------------------");
			player.startMove(obj[Constants.JK_MOVE_DIR]);
		}
	}else if(obj[Constants.JK_OP] == Constants.OP_STOP_MOVE_REPLY){
		if (obj[Constants.JK_CODE] != ReplyCode.SUCCESS) {
			console.log("停止move失败!");
			return;
		}else{
			// 停止运动
			player.endMove();
		}
	}
}