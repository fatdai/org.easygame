

function processLogin(obj) {
	if (obj.op == "login_on_reply" || obj.op == "login_regist_reply") {
		if (obj.error) {
			alert("error : " + obj.error);
			return;
		}
		showGame(obj);
	}else if(obj.op == "login_out"){
		playerExit(obj.id);
	}else if(obj.op == "login_by_other"){
		alert("其他玩家登陆此账号!");
		showLogin();
		return;
	}
}



function processSys(obj){
	if (obj.op == "sys_player_exit") {
		playerExit(obj.id);
	}else if(obj.op == "sys_player_enter"){
		playerEnter(obj.p);
	}else if(obj.op == "sys_login_ps"){
		sync(obj.ps);
	}
}

function processMove(obj){

}

function process(obj){
	if (obj.op == "alldata") {
		var arr = obj.userstate;
		for(var i = 0; i < arr.length; ++i){
			recvMsg.push(arr[i]);
		}
	}
}


//$$$$$$$$$$$$$$$$$$$$$$$
function playerExit(id){
	for(var i = 0; i < others.length; ++i){
		if (others[i].id == id) {
			others.remove(i);
			return;
		}
	}
}

function playerEnter(pObj){
	var p = new Player(pObj);
	others.push(p);
}

function sync(psObjArr){

}
